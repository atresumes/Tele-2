package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.factories.ReflectionSerializerFactory;
import com.esotericsoftware.kryo.serializers.CollectionSerializer.BindCollection;
import com.esotericsoftware.kryo.serializers.FieldSerializer.Bind;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.serializers.MapSerializer.BindMap;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

final class FieldSerializerAnnotationsUtil {
    public FieldSerializerAnnotationsUtil(FieldSerializer serializer) {
    }

    public void processAnnotatedFields(FieldSerializer fieldSerializer) {
        CachedField[] fields = fieldSerializer.getFields();
        int n = fields.length;
        for (int i = 0; i < n; i++) {
            Field field = fields[i].getField();
            if (field.isAnnotationPresent(Bind.class)) {
                Class<? extends Serializer> serializerClass = ((Bind) field.getAnnotation(Bind.class)).value();
                fields[i].setSerializer(ReflectionSerializerFactory.makeSerializer(fieldSerializer.getKryo(), serializerClass, field.getClass()));
            }
            if (!field.isAnnotationPresent(BindCollection.class) || field.isAnnotationPresent(BindMap.class)) {
            }
            if (field.isAnnotationPresent(BindCollection.class)) {
                if (fields[i].serializer != null) {
                    throw new RuntimeException("CollectionSerialier.Bind cannot be used with field " + fields[i].getField().getDeclaringClass().getName() + "." + fields[i].getField().getName() + ", because it has a serializer already.");
                }
                BindCollection annotation = (BindCollection) field.getAnnotation(BindCollection.class);
                if (Collection.class.isAssignableFrom(fields[i].field.getType())) {
                    Serializer elementSerializer;
                    Class<? extends Serializer> elementSerializerClass = annotation.elementSerializer();
                    if (elementSerializerClass == Serializer.class) {
                        elementSerializerClass = null;
                    }
                    if (elementSerializerClass == null) {
                        elementSerializer = null;
                    } else {
                        elementSerializer = ReflectionSerializerFactory.makeSerializer(fieldSerializer.getKryo(), elementSerializerClass, field.getClass());
                    }
                    boolean elementsCanBeNull = annotation.elementsCanBeNull();
                    Class<?> elementClass = annotation.elementClass();
                    if (elementClass == Object.class) {
                        elementClass = null;
                    }
                    Serializer serializer = new CollectionSerializer();
                    serializer.setElementsCanBeNull(elementsCanBeNull);
                    serializer.setElementClass(elementClass, elementSerializer);
                    fields[i].setSerializer(serializer);
                } else {
                    throw new RuntimeException("CollectionSerialier.Bind should be used only with fields implementing java.util.Collection, but field " + fields[i].getField().getDeclaringClass().getName() + "." + fields[i].getField().getName() + " does not implement it.");
                }
            }
            if (field.isAnnotationPresent(BindMap.class)) {
                if (fields[i].serializer != null) {
                    throw new RuntimeException("MapSerialier.Bind cannot be used with field " + fields[i].getField().getDeclaringClass().getName() + "." + fields[i].getField().getName() + ", because it has a serializer already.");
                }
                BindMap annotation2 = (BindMap) field.getAnnotation(BindMap.class);
                if (Map.class.isAssignableFrom(fields[i].field.getType())) {
                    Serializer valueSerializer;
                    Serializer keySerializer;
                    Class<? extends Serializer> valueSerializerClass = annotation2.valueSerializer();
                    Class<? extends Serializer> keySerializerClass = annotation2.keySerializer();
                    if (valueSerializerClass == Serializer.class) {
                        valueSerializerClass = null;
                    }
                    if (keySerializerClass == Serializer.class) {
                        keySerializerClass = null;
                    }
                    if (valueSerializerClass == null) {
                        valueSerializer = null;
                    } else {
                        valueSerializer = ReflectionSerializerFactory.makeSerializer(fieldSerializer.getKryo(), valueSerializerClass, field.getClass());
                    }
                    if (keySerializerClass == null) {
                        keySerializer = null;
                    } else {
                        keySerializer = ReflectionSerializerFactory.makeSerializer(fieldSerializer.getKryo(), keySerializerClass, field.getClass());
                    }
                    boolean valuesCanBeNull = annotation2.valuesCanBeNull();
                    boolean keysCanBeNull = annotation2.keysCanBeNull();
                    Class<?> keyClass = annotation2.keyClass();
                    Class<?> valueClass = annotation2.valueClass();
                    if (keyClass == Object.class) {
                        keyClass = null;
                    }
                    if (valueClass == Object.class) {
                        valueClass = null;
                    }
                    serializer = new MapSerializer();
                    serializer.setKeysCanBeNull(keysCanBeNull);
                    serializer.setValuesCanBeNull(valuesCanBeNull);
                    serializer.setKeyClass(keyClass, keySerializer);
                    serializer.setValueClass(valueClass, valueSerializer);
                    fields[i].setSerializer(serializer);
                } else {
                    throw new RuntimeException("MapSerialier.Bind should be used only with fields implementing java.util.Map, but field " + fields[i].getField().getDeclaringClass().getName() + "." + fields[i].getField().getName() + " does not implement it.");
                }
            }
        }
    }
}
