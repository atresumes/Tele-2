package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.reflectasm.MethodAccess;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BeanSerializer<T> extends Serializer<T> {
    static final Object[] noArgs = new Object[0];
    Object access;
    private CachedProperty[] properties;

    class C04711 implements Comparator<PropertyDescriptor> {
        C04711() {
        }

        public int compare(PropertyDescriptor o1, PropertyDescriptor o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    class CachedProperty<X> {
        Method getMethod;
        int getterAccessIndex;
        String name;
        Serializer serializer;
        Method setMethod;
        Class setMethodType;
        int setterAccessIndex;

        CachedProperty() {
        }

        public String toString() {
            return this.name;
        }

        Object get(Object object) throws IllegalAccessException, InvocationTargetException {
            if (BeanSerializer.this.access != null) {
                return ((MethodAccess) BeanSerializer.this.access).invoke(object, this.getterAccessIndex, new Object[0]);
            }
            return this.getMethod.invoke(object, BeanSerializer.noArgs);
        }

        void set(Object object, Object value) throws IllegalAccessException, InvocationTargetException {
            if (BeanSerializer.this.access != null) {
                ((MethodAccess) BeanSerializer.this.access).invoke(object, this.setterAccessIndex, value);
                return;
            }
            this.setMethod.invoke(object, new Object[]{value});
        }
    }

    public BeanSerializer(Kryo kryo, Class type) {
        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(type).getPropertyDescriptors();
            Arrays.sort(descriptors, new C04711());
            ArrayList<CachedProperty> cachedProperties = new ArrayList(descriptors.length);
            for (PropertyDescriptor property : descriptors) {
                String name = property.getName();
                if (!name.equals("class")) {
                    Method getMethod = property.getReadMethod();
                    Method setMethod = property.getWriteMethod();
                    if (!(getMethod == null || setMethod == null)) {
                        Serializer serializer = null;
                        Class returnType = getMethod.getReturnType();
                        if (kryo.isFinal(returnType)) {
                            serializer = kryo.getRegistration(returnType).getSerializer();
                        }
                        CachedProperty cachedProperty = new CachedProperty();
                        cachedProperty.name = name;
                        cachedProperty.getMethod = getMethod;
                        cachedProperty.setMethod = setMethod;
                        cachedProperty.serializer = serializer;
                        cachedProperty.setMethodType = setMethod.getParameterTypes()[0];
                        cachedProperties.add(cachedProperty);
                    }
                }
            }
            this.properties = (CachedProperty[]) cachedProperties.toArray(new CachedProperty[cachedProperties.size()]);
            try {
                this.access = MethodAccess.get(type);
                for (CachedProperty property2 : this.properties) {
                    property2.getterAccessIndex = ((MethodAccess) this.access).getIndex(property2.getMethod.getName(), property2.getMethod.getParameterTypes());
                    property2.setterAccessIndex = ((MethodAccess) this.access).getIndex(property2.setMethod.getName(), property2.setMethod.getParameterTypes());
                }
            } catch (Throwable th) {
            }
        } catch (IntrospectionException ex) {
            throw new KryoException("Error getting bean info.", ex);
        }
    }

    public void write(Kryo kryo, Output output, T object) {
        KryoException ex;
        Class type = object.getClass();
        int i = 0;
        int n = this.properties.length;
        while (i < n) {
            CachedProperty property = this.properties[i];
            try {
                if (Log.TRACE) {
                    Log.trace("kryo", "Write property: " + property + " (" + type.getName() + ")");
                }
                Object value = property.get(object);
                Serializer serializer = property.serializer;
                if (serializer != null) {
                    kryo.writeObjectOrNull(output, value, serializer);
                } else {
                    kryo.writeClassAndObject(output, value);
                }
                i++;
            } catch (IllegalAccessException ex2) {
                throw new KryoException("Error accessing getter method: " + property + " (" + type.getName() + ")", ex2);
            } catch (InvocationTargetException ex3) {
                throw new KryoException("Error invoking getter method: " + property + " (" + type.getName() + ")", ex3);
            } catch (KryoException ex4) {
                ex4.addTrace(property + " (" + type.getName() + ")");
                throw ex4;
            } catch (Throwable runtimeEx) {
                ex4 = new KryoException(runtimeEx);
                ex4.addTrace(property + " (" + type.getName() + ")");
                throw ex4;
            }
        }
    }

    public T read(Kryo kryo, Input input, Class<T> type) {
        KryoException ex;
        T object = kryo.newInstance(type);
        kryo.reference(object);
        int i = 0;
        int n = this.properties.length;
        while (i < n) {
            CachedProperty property = this.properties[i];
            try {
                Object value;
                if (Log.TRACE) {
                    Log.trace("kryo", "Read property: " + property + " (" + object.getClass() + ")");
                }
                Serializer serializer = property.serializer;
                if (serializer != null) {
                    value = kryo.readObjectOrNull(input, property.setMethodType, serializer);
                } else {
                    value = kryo.readClassAndObject(input);
                }
                property.set(object, value);
                i++;
            } catch (IllegalAccessException ex2) {
                throw new KryoException("Error accessing setter method: " + property + " (" + object.getClass().getName() + ")", ex2);
            } catch (InvocationTargetException ex3) {
                throw new KryoException("Error invoking setter method: " + property + " (" + object.getClass().getName() + ")", ex3);
            } catch (KryoException ex4) {
                ex4.addTrace(property + " (" + object.getClass().getName() + ")");
                throw ex4;
            } catch (Throwable runtimeEx) {
                ex4 = new KryoException(runtimeEx);
                ex4.addTrace(property + " (" + object.getClass().getName() + ")");
                throw ex4;
            }
        }
        return object;
    }

    public T copy(Kryo kryo, T original) {
        KryoException ex;
        T copy = kryo.newInstance(original.getClass());
        int i = 0;
        int n = this.properties.length;
        while (i < n) {
            CachedProperty property = this.properties[i];
            try {
                property.set(copy, property.get(original));
                i++;
            } catch (KryoException ex2) {
                ex2.addTrace(property + " (" + copy.getClass().getName() + ")");
                throw ex2;
            } catch (Throwable runtimeEx) {
                ex2 = new KryoException(runtimeEx);
                ex2.addTrace(property + " (" + copy.getClass().getName() + ")");
                throw ex2;
            } catch (Exception ex3) {
                throw new KryoException("Error copying bean property: " + property + " (" + copy.getClass().getName() + ")", ex3);
            }
        }
        return copy;
    }
}
