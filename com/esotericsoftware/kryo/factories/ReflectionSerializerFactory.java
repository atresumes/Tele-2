package com.esotericsoftware.kryo.factories;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.util.Util;

public class ReflectionSerializerFactory implements SerializerFactory {
    private final Class<? extends Serializer> serializerClass;

    public ReflectionSerializerFactory(Class<? extends Serializer> serializerClass) {
        this.serializerClass = serializerClass;
    }

    public Serializer makeSerializer(Kryo kryo, Class<?> type) {
        return makeSerializer(kryo, this.serializerClass, type);
    }

    public static Serializer makeSerializer(Kryo kryo, Class<? extends Serializer> serializerClass, Class<?> type) {
        try {
            return (Serializer) serializerClass.getConstructor(new Class[]{Kryo.class, Class.class}).newInstance(new Object[]{kryo, type});
        } catch (NoSuchMethodException e) {
            try {
                return (Serializer) serializerClass.getConstructor(new Class[]{Kryo.class}).newInstance(new Object[]{kryo});
            } catch (NoSuchMethodException e2) {
                try {
                    return (Serializer) serializerClass.getConstructor(new Class[]{Class.class}).newInstance(new Object[]{type});
                } catch (NoSuchMethodException e3) {
                    try {
                        return (Serializer) serializerClass.newInstance();
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Unable to create serializer \"" + serializerClass.getName() + "\" for class: " + Util.className(type), ex);
                    }
                }
            }
        }
    }
}
