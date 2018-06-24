package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.minlog.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

final class FieldSerializerGenericsUtil {
    private Kryo kryo;
    private FieldSerializer serializer;

    public FieldSerializerGenericsUtil(FieldSerializer serializer) {
        this.serializer = serializer;
        this.kryo = serializer.getKryo();
    }

    Generics buildGenericsScope(Class clazz, Class[] generics) {
        Class typ = clazz;
        TypeVariable[] typeParams = null;
        while (typ != null) {
            if (typ == this.serializer.type) {
                typeParams = this.serializer.typeParameters;
            } else {
                typeParams = typ.getTypeParameters();
            }
            if (typeParams != null && typeParams.length != 0) {
                break;
            } else if (typ == this.serializer.type) {
                typ = this.serializer.componentType;
                if (typ == null) {
                    Type superclass;
                    typ = this.serializer.type;
                    do {
                        superclass = typ.getGenericSuperclass();
                        typ = typ.getSuperclass();
                        if (superclass == null) {
                            break;
                        }
                    } while (!(superclass instanceof ParameterizedType));
                    if (superclass != null) {
                        Type[] typeArgs = ((ParameterizedType) superclass).getActualTypeArguments();
                        typeParams = typ.getTypeParameters();
                        generics = new Class[typeArgs.length];
                        for (int i = 0; i < typeArgs.length; i++) {
                            generics[i] = typeArgs[i] instanceof Class ? (Class) typeArgs[i] : Object.class;
                        }
                    }
                }
            } else {
                typ = typ.getComponentType();
            }
        }
        if (typeParams == null || typeParams.length <= 0) {
            return null;
        }
        if (Log.TRACE) {
            Log.trace("kryo", "Class " + clazz.getName() + " has generic type parameters");
        }
        int typeVarNum = 0;
        Map<String, Class> typeVar2concreteClass = new HashMap();
        for (TypeVariable typeVar : typeParams) {
            String typeVarName = typeVar.getName();
            if (Log.TRACE) {
                Log.trace("kryo", "Type parameter variable: name=" + typeVarName + " type bounds=" + Arrays.toString(typeVar.getBounds()));
            }
            Class<?> concreteClass = getTypeVarConcreteClass(generics, typeVarNum, typeVarName);
            if (concreteClass != null) {
                typeVar2concreteClass.put(typeVarName, concreteClass);
                if (Log.TRACE) {
                    Log.trace("kryo", "Concrete type used for " + typeVarName + " is: " + concreteClass.getName());
                }
            }
            typeVarNum++;
        }
        return new Generics(typeVar2concreteClass);
    }

    private Class<?> getTypeVarConcreteClass(Class[] generics, int typeVarNum, String typeVarName) {
        if (generics != null && generics.length > typeVarNum) {
            return generics[typeVarNum];
        }
        if (Log.TRACE) {
            Log.trace("kryo", "Trying to use kryo.getGenericScope");
        }
        GenericsResolver scope = this.kryo.getGenericsResolver();
        if (scope.isSet()) {
            return scope.getConcreteClass(typeVarName);
        }
        return null;
    }

    Class[] computeFieldGenerics(Type fieldGenericType, Field field, Class[] fieldClass) {
        Class[] fieldGenerics = null;
        if (fieldGenericType != null) {
            if ((fieldGenericType instanceof TypeVariable) && this.serializer.getGenericsScope() != null) {
                Class concreteClass = this.serializer.getGenericsScope().getConcreteClass(((TypeVariable) fieldGenericType).getName());
                if (concreteClass != null) {
                    fieldClass[0] = concreteClass;
                    fieldGenerics = new Class[]{fieldClass[0]};
                    if (Log.TRACE) {
                        Log.trace("kryo", "Determined concrete class of '" + field.getName() + "' to be " + fieldClass[0].getName());
                    }
                }
            } else if (fieldGenericType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) fieldGenericType).getActualTypeArguments();
                if (actualTypeArguments != null) {
                    fieldGenerics = new Class[actualTypeArguments.length];
                    for (int i = 0; i < actualTypeArguments.length; i++) {
                        Type t = actualTypeArguments[i];
                        if (t instanceof Class) {
                            fieldGenerics[i] = (Class) t;
                        } else if (t instanceof ParameterizedType) {
                            fieldGenerics[i] = (Class) ((ParameterizedType) t).getRawType();
                        } else if ((t instanceof TypeVariable) && this.serializer.getGenericsScope() != null) {
                            fieldGenerics[i] = this.serializer.getGenericsScope().getConcreteClass(((TypeVariable) t).getName());
                            if (fieldGenerics[i] == null) {
                                fieldGenerics[i] = Object.class;
                            }
                        } else if (t instanceof WildcardType) {
                            fieldGenerics[i] = Object.class;
                        } else if (t instanceof GenericArrayType) {
                            Type componentType = ((GenericArrayType) t).getGenericComponentType();
                            if (componentType instanceof Class) {
                                fieldGenerics[i] = Array.newInstance((Class) componentType, 0).getClass();
                            } else if (componentType instanceof TypeVariable) {
                                Generics scope = this.serializer.getGenericsScope();
                                if (scope != null) {
                                    Class clazz = scope.getConcreteClass(((TypeVariable) componentType).getName());
                                    if (clazz != null) {
                                        fieldGenerics[i] = Array.newInstance(clazz, 0).getClass();
                                    }
                                }
                            }
                        } else {
                            fieldGenerics[i] = null;
                        }
                    }
                    if (Log.TRACE && fieldGenerics != null) {
                        Log.trace("kryo", "Determined concrete class of parametrized '" + field.getName() + "' to be " + fieldGenericType + " where type parameters are " + Arrays.toString(fieldGenerics));
                    }
                }
            } else if (fieldGenericType instanceof GenericArrayType) {
                fieldGenerics = computeFieldGenerics(((GenericArrayType) fieldGenericType).getGenericComponentType(), field, new Class[]{fieldClass[0]});
                if (Log.TRACE && fieldGenerics != null) {
                    Log.trace("kryo", "Determined concrete class of a generic array '" + field.getName() + "' to be " + fieldGenericType + " where type parameters are " + Arrays.toString(fieldGenerics));
                } else if (Log.TRACE) {
                    Log.trace("kryo", "Determined concrete class of '" + field.getName() + "' to be " + fieldGenericType);
                }
            }
        }
        return fieldGenerics;
    }

    CachedField newCachedFieldOfGenericType(Field field, int accessIndex, Class[] fieldClass, Type fieldGenericType) {
        if (Log.TRACE) {
            Log.trace("kryo", "Field '" + field.getName() + "' of type " + fieldClass[0] + " of generic type " + fieldGenericType);
        }
        if (Log.TRACE && fieldGenericType != null) {
            Log.trace("kryo", "Field generic type is of class " + fieldGenericType.getClass().getName());
        }
        Generics scope = buildGenericsScope(fieldClass[0], getGenerics(fieldGenericType, this.kryo));
        if (fieldClass[0] == Object.class && (fieldGenericType instanceof TypeVariable) && this.serializer.getGenericsScope() != null) {
            TypeVariable typeVar = (TypeVariable) fieldGenericType;
            Class concreteClass = this.serializer.getGenericsScope().getConcreteClass(typeVar.getName());
            if (concreteClass != null) {
                scope = new Generics();
                scope.add(typeVar.getName(), concreteClass);
            }
        }
        if (Log.TRACE) {
            Log.trace("kryo", "Generics scope of field '" + field.getName() + "' of class " + fieldGenericType + " is " + scope);
        }
        Class[] fieldGenerics = computeFieldGenerics(fieldGenericType, field, fieldClass);
        CachedField cachedField = this.serializer.newMatchingCachedField(field, accessIndex, fieldClass[0], fieldGenericType, fieldGenerics);
        if (fieldGenerics != null && (cachedField instanceof ObjectField) && fieldGenerics.length > 0 && fieldGenerics[0] != null) {
            ((ObjectField) cachedField).generics = fieldGenerics;
            if (Log.TRACE) {
                Log.trace("kryo", "Field generics: " + Arrays.toString(fieldGenerics));
            }
        }
        return cachedField;
    }

    public static Class[] getGenerics(Type genericType, Kryo kryo) {
        Type componentType;
        if (genericType instanceof GenericArrayType) {
            componentType = ((GenericArrayType) genericType).getGenericComponentType();
            if (!(componentType instanceof Class)) {
                return getGenerics(componentType, kryo);
            }
            return new Class[]{(Class) componentType};
        } else if (!(genericType instanceof ParameterizedType)) {
            return null;
        } else {
            if (Log.TRACE) {
                Log.trace("kryo", "Processing generic type " + genericType);
            }
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            Class[] generics = new Class[actualTypes.length];
            int count = 0;
            int n = actualTypes.length;
            for (int i = 0; i < n; i++) {
                Type actualType = actualTypes[i];
                if (Log.TRACE) {
                    Log.trace("kryo", "Processing actual type " + actualType + " (" + actualType.getClass().getName() + ")");
                }
                generics[i] = Object.class;
                if (actualType instanceof Class) {
                    generics[i] = (Class) actualType;
                } else if (actualType instanceof ParameterizedType) {
                    generics[i] = (Class) ((ParameterizedType) actualType).getRawType();
                } else if (actualType instanceof TypeVariable) {
                    scope = kryo.getGenericsResolver();
                    if (scope.isSet()) {
                        clazz = scope.getConcreteClass(((TypeVariable) actualType).getName());
                        if (clazz != null) {
                            generics[i] = clazz;
                        }
                    }
                } else if (actualType instanceof GenericArrayType) {
                    componentType = ((GenericArrayType) actualType).getGenericComponentType();
                    if (componentType instanceof Class) {
                        generics[i] = Array.newInstance((Class) componentType, 0).getClass();
                    } else if (componentType instanceof TypeVariable) {
                        scope = kryo.getGenericsResolver();
                        if (scope.isSet()) {
                            clazz = scope.getConcreteClass(((TypeVariable) componentType).getName());
                            if (clazz != null) {
                                generics[i] = Array.newInstance(clazz, 0).getClass();
                            }
                        }
                    } else {
                        Class[] componentGenerics = getGenerics(componentType, kryo);
                        if (componentGenerics != null) {
                            generics[i] = componentGenerics[0];
                        }
                    }
                } else {
                }
                count++;
            }
            if (count == 0) {
                return null;
            }
            return generics;
        }
    }
}
