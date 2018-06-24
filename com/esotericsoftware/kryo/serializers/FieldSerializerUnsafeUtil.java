package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.util.IntArray;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

interface FieldSerializerUnsafeUtil {

    public static class Factory {
        static Constructor<FieldSerializerUnsafeUtil> fieldSerializerUnsafeUtilConstructor;

        static {
            try {
                fieldSerializerUnsafeUtilConstructor = FieldSerializer.class.getClassLoader().loadClass("com.esotericsoftware.kryo.serializers.FieldSerializerUnsafeUtilImpl").getConstructor(new Class[]{FieldSerializer.class});
            } catch (Throwable th) {
            }
        }

        static FieldSerializerUnsafeUtil getInstance(FieldSerializer serializer) {
            if (fieldSerializerUnsafeUtilConstructor != null) {
                try {
                    return (FieldSerializerUnsafeUtil) fieldSerializerUnsafeUtilConstructor.newInstance(new Object[]{serializer});
                } catch (Exception e) {
                }
            }
            return null;
        }
    }

    void createUnsafeCacheFieldsAndRegions(List<Field> list, List<CachedField> list2, int i, IntArray intArray);

    long getObjectFieldOffset(Field field);
}
