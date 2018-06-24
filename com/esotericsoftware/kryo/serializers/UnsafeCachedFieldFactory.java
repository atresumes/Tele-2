package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedFieldFactory;
import java.lang.reflect.Field;

class UnsafeCachedFieldFactory implements CachedFieldFactory {
    UnsafeCachedFieldFactory() {
    }

    public CachedField createCachedField(Class fieldClass, Field field, FieldSerializer ser) {
        if (fieldClass.isPrimitive()) {
            if (fieldClass == Boolean.TYPE) {
                return new UnsafeBooleanField(field);
            }
            if (fieldClass == Byte.TYPE) {
                return new UnsafeByteField(field);
            }
            if (fieldClass == Character.TYPE) {
                return new UnsafeCharField(field);
            }
            if (fieldClass == Short.TYPE) {
                return new UnsafeShortField(field);
            }
            if (fieldClass == Integer.TYPE) {
                return new UnsafeIntField(field);
            }
            if (fieldClass == Long.TYPE) {
                return new UnsafeLongField(field);
            }
            if (fieldClass == Float.TYPE) {
                return new UnsafeFloatField(field);
            }
            if (fieldClass == Double.TYPE) {
                return new UnsafeDoubleField(field);
            }
            return new UnsafeObjectField(ser);
        } else if (fieldClass != String.class || (ser.kryo.getReferences() && ser.kryo.getReferenceResolver().useReferences(String.class))) {
            return new UnsafeObjectField(ser);
        } else {
            return new UnsafeStringField(field);
        }
    }
}
