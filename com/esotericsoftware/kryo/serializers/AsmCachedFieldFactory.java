package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedFieldFactory;
import java.lang.reflect.Field;

class AsmCachedFieldFactory implements CachedFieldFactory {
    AsmCachedFieldFactory() {
    }

    public CachedField createCachedField(Class fieldClass, Field field, FieldSerializer ser) {
        if (fieldClass.isPrimitive()) {
            if (fieldClass == Boolean.TYPE) {
                return new AsmBooleanField();
            }
            if (fieldClass == Byte.TYPE) {
                return new AsmByteField();
            }
            if (fieldClass == Character.TYPE) {
                return new AsmCharField();
            }
            if (fieldClass == Short.TYPE) {
                return new AsmShortField();
            }
            if (fieldClass == Integer.TYPE) {
                return new AsmIntField();
            }
            if (fieldClass == Long.TYPE) {
                return new AsmLongField();
            }
            if (fieldClass == Float.TYPE) {
                return new AsmFloatField();
            }
            if (fieldClass == Double.TYPE) {
                return new AsmDoubleField();
            }
            return new AsmObjectField(ser);
        } else if (fieldClass != String.class || (ser.kryo.getReferences() && ser.kryo.getReferenceResolver().useReferences(String.class))) {
            return new AsmObjectField(ser);
        } else {
            return new AsmStringField();
        }
    }
}
