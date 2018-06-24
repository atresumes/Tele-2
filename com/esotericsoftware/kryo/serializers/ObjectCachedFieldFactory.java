package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedFieldFactory;
import java.lang.reflect.Field;

class ObjectCachedFieldFactory implements CachedFieldFactory {
    ObjectCachedFieldFactory() {
    }

    public CachedField createCachedField(Class fieldClass, Field field, FieldSerializer ser) {
        if (!fieldClass.isPrimitive()) {
            return new ObjectField(ser);
        }
        if (fieldClass == Boolean.TYPE) {
            return new ObjectBooleanField(ser);
        }
        if (fieldClass == Byte.TYPE) {
            return new ObjectByteField(ser);
        }
        if (fieldClass == Character.TYPE) {
            return new ObjectCharField(ser);
        }
        if (fieldClass == Short.TYPE) {
            return new ObjectShortField(ser);
        }
        if (fieldClass == Integer.TYPE) {
            return new ObjectIntField(ser);
        }
        if (fieldClass == Long.TYPE) {
            return new ObjectLongField(ser);
        }
        if (fieldClass == Float.TYPE) {
            return new ObjectFloatField(ser);
        }
        if (fieldClass == Double.TYPE) {
            return new ObjectDoubleField(ser);
        }
        return new ObjectField(ser);
    }
}
