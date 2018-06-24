package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.util.IntArray;
import com.esotericsoftware.kryo.util.UnsafeUtil;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.reflectasm.FieldAccess;
import java.lang.reflect.Field;
import java.util.List;

final class FieldSerializerUnsafeUtilImpl implements FieldSerializerUnsafeUtil {
    private FieldSerializer serializer;

    public FieldSerializerUnsafeUtilImpl(FieldSerializer serializer) {
        this.serializer = serializer;
    }

    public void createUnsafeCacheFieldsAndRegions(List<Field> validFields, List<CachedField> cachedFields, int baseIndex, IntArray useAsm) {
        long endPrimitives;
        long startPrimitives = 0;
        boolean lastWasPrimitive = false;
        int primitiveLength = 0;
        int lastAccessIndex = -1;
        Field lastField = null;
        long lastFieldEndOffset = -1;
        int i = 0;
        int n = validFields.size();
        while (i < n) {
            Field field = (Field) validFields.get(i);
            int accessIndex = -1;
            if (this.serializer.access != null && useAsm.get(baseIndex + i) == 1) {
                accessIndex = ((FieldAccess) this.serializer.access).getIndex(field.getName());
            }
            long fieldOffset = UnsafeUtil.unsafe().objectFieldOffset(field);
            long fieldEndOffset = fieldOffset + ((long) fieldSizeOf(field.getType()));
            if (!field.getType().isPrimitive() && lastWasPrimitive) {
                endPrimitives = lastFieldEndOffset;
                lastWasPrimitive = false;
                if (primitiveLength > 1) {
                    if (Log.TRACE) {
                        Log.trace("kryo", "Class " + this.serializer.getType().getName() + ". Found a set of consecutive primitive fields. Number of fields = " + primitiveLength + ". Byte length = " + (endPrimitives - startPrimitives) + " Start offset = " + startPrimitives + " endOffset=" + endPrimitives);
                    }
                    CachedField cf = new UnsafeRegionField(startPrimitives, endPrimitives - startPrimitives);
                    cf.field = lastField;
                    cachedFields.add(cf);
                } else if (lastField != null) {
                    cachedFields.add(this.serializer.newCachedField(lastField, cachedFields.size(), lastAccessIndex));
                }
                cachedFields.add(this.serializer.newCachedField(field, cachedFields.size(), accessIndex));
            } else if (!field.getType().isPrimitive()) {
                cachedFields.add(this.serializer.newCachedField(field, cachedFields.size(), accessIndex));
            } else if (lastWasPrimitive) {
                primitiveLength++;
            } else {
                startPrimitives = fieldOffset;
                lastWasPrimitive = true;
                primitiveLength = 1;
            }
            lastAccessIndex = accessIndex;
            lastField = field;
            lastFieldEndOffset = fieldEndOffset;
            i++;
        }
        if (!this.serializer.getUseAsmEnabled() && this.serializer.getUseMemRegions() && lastWasPrimitive) {
            endPrimitives = lastFieldEndOffset;
            if (primitiveLength > 1) {
                if (Log.TRACE) {
                    Log.trace("kryo", "Class " + this.serializer.getType().getName() + ". Found a set of consecutive primitive fields. Number of fields = " + primitiveLength + ". Byte length = " + (endPrimitives - startPrimitives) + " Start offset = " + startPrimitives + " endOffset=" + endPrimitives);
                }
                cf = new UnsafeRegionField(startPrimitives, endPrimitives - startPrimitives);
                cf.field = lastField;
                cachedFields.add(cf);
            } else if (lastField != null) {
                cachedFields.add(this.serializer.newCachedField(lastField, cachedFields.size(), lastAccessIndex));
            }
        }
    }

    private int fieldSizeOf(Class<?> clazz) {
        if (clazz == Integer.TYPE || clazz == Float.TYPE) {
            return 4;
        }
        if (clazz == Long.TYPE || clazz == Double.TYPE) {
            return 8;
        }
        if (clazz == Byte.TYPE || clazz == Boolean.TYPE) {
            return 1;
        }
        if (clazz == Short.TYPE || clazz == Character.TYPE) {
            return 2;
        }
        return UnsafeUtil.unsafe().addressSize();
    }

    public long getObjectFieldOffset(Field field) {
        return UnsafeUtil.unsafe().objectFieldOffset(field);
    }
}
