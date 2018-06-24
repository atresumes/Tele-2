package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ArraysAsListSerializer extends Serializer<List<?>> {
    private Field _arrayField;

    public ArraysAsListSerializer() {
        try {
            this._arrayField = Class.forName("java.util.Arrays$ArrayList").getDeclaredField("a");
            this._arrayField.setAccessible(true);
            setImmutable(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<?> read(Kryo kryo, Input input, Class<List<?>> cls) {
        int length = input.readInt(true);
        Class<?> componentType = kryo.readClass(input).getType();
        if (componentType.isPrimitive()) {
            componentType = getPrimitiveWrapperClass(componentType);
        }
        try {
            Object items = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                Array.set(items, i, kryo.readClassAndObject(input));
            }
            return Arrays.asList((Object[]) items);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Kryo kryo, Output output, List<?> obj) {
        try {
            Object[] array = (Object[]) this._arrayField.get(obj);
            output.writeInt(array.length, true);
            kryo.writeClass(output, array.getClass().getComponentType());
            for (Object item : array) {
                kryo.writeClassAndObject(output, item);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private static Class<?> getPrimitiveWrapperClass(Class<?> c) {
        if (!c.isPrimitive()) {
            return c;
        }
        if (c.equals(Long.TYPE)) {
            return Long.class;
        }
        if (c.equals(Integer.TYPE)) {
            return Integer.class;
        }
        if (c.equals(Double.TYPE)) {
            return Double.class;
        }
        if (c.equals(Float.TYPE)) {
            return Float.class;
        }
        if (c.equals(Boolean.TYPE)) {
            return Boolean.class;
        }
        if (c.equals(Character.TYPE)) {
            return Character.class;
        }
        if (c.equals(Short.TYPE)) {
            return Short.class;
        }
        if (c.equals(Byte.TYPE)) {
            return Byte.class;
        }
        return c;
    }
}
