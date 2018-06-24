package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.Field;
import java.util.List;

public class SubListSerializers {
    private static final Object FAKE_REFERENCE = new Object();

    public static class ArrayListSubListSerializer extends Serializer<List<?>> {
        public static final Class<?> SUBLIST_CLASS = SubListSerializers.getClassOrNull("java.util.ArrayList$SubList");
        private Field _parentField;
        private Field _parentOffsetField;
        private Field _sizeField;

        public ArrayListSubListSerializer() {
            try {
                Class<?> clazz = Class.forName("java.util.ArrayList$SubList");
                this._parentField = clazz.getDeclaredField("parent");
                this._parentOffsetField = clazz.getDeclaredField("parentOffset");
                this._sizeField = clazz.getDeclaredField("size");
                this._parentField.setAccessible(true);
                this._parentOffsetField.setAccessible(true);
                this._sizeField.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean canSerialize(Class<?> type) {
            return SUBLIST_CLASS != null && SUBLIST_CLASS.isAssignableFrom(type);
        }

        public static Kryo addDefaultSerializer(Kryo kryo) {
            if (SUBLIST_CLASS != null) {
                kryo.addDefaultSerializer(SUBLIST_CLASS, new ArrayListSubListSerializer());
            }
            return kryo;
        }

        public List<?> read(Kryo kryo, Input input, Class<List<?>> cls) {
            kryo.reference(SubListSerializers.FAKE_REFERENCE);
            return ((List) kryo.readClassAndObject(input)).subList(input.readInt(true), input.readInt(true));
        }

        public void write(Kryo kryo, Output output, List<?> obj) {
            try {
                kryo.writeClassAndObject(output, this._parentField.get(obj));
                int fromIndex = this._parentOffsetField.getInt(obj);
                output.writeInt(fromIndex, true);
                output.writeInt(fromIndex + this._sizeField.getInt(obj), true);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }

        public List<?> copy(Kryo kryo, List<?> original) {
            kryo.reference(SubListSerializers.FAKE_REFERENCE);
            try {
                List<?> list = (List) this._parentField.get(original);
                int fromIndex = this._parentOffsetField.getInt(original);
                return ((List) kryo.copy(list)).subList(fromIndex, fromIndex + this._sizeField.getInt(original));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class JavaUtilSubListSerializer extends Serializer<List<?>> {
        public static final Class<?> SUBLIST_CLASS = SubListSerializers.getClassOrNull("java.util.SubList");
        private Field _listField;
        private Field _offsetField;
        private Field _sizeField;

        public JavaUtilSubListSerializer() {
            try {
                Class<?> clazz = Class.forName("java.util.SubList");
                this._listField = clazz.getDeclaredField("l");
                this._offsetField = clazz.getDeclaredField("offset");
                this._sizeField = clazz.getDeclaredField("size");
                this._listField.setAccessible(true);
                this._offsetField.setAccessible(true);
                this._sizeField.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean canSerialize(Class<?> type) {
            return SUBLIST_CLASS != null && SUBLIST_CLASS.isAssignableFrom(type);
        }

        public static Kryo addDefaultSerializer(Kryo kryo) {
            if (SUBLIST_CLASS != null) {
                kryo.addDefaultSerializer(SUBLIST_CLASS, new JavaUtilSubListSerializer());
            }
            return kryo;
        }

        public List<?> read(Kryo kryo, Input input, Class<List<?>> cls) {
            kryo.reference(SubListSerializers.FAKE_REFERENCE);
            return ((List) kryo.readClassAndObject(input)).subList(input.readInt(true), input.readInt(true));
        }

        public void write(Kryo kryo, Output output, List<?> obj) {
            try {
                kryo.writeClassAndObject(output, this._listField.get(obj));
                int fromIndex = this._offsetField.getInt(obj);
                output.writeInt(fromIndex, true);
                output.writeInt(fromIndex + this._sizeField.getInt(obj), true);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }

        public List<?> copy(Kryo kryo, List<?> obj) {
            kryo.reference(SubListSerializers.FAKE_REFERENCE);
            try {
                List<?> list = (List) this._listField.get(obj);
                int fromIndex = this._offsetField.getInt(obj);
                return ((List) kryo.copy(list)).subList(fromIndex, fromIndex + this._sizeField.getInt(obj));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Class<?> getClassOrNull(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

    public static Serializer<List<?>> createFor(Class type) {
        if (ArrayListSubListSerializer.canSerialize(type)) {
            return new ArrayListSubListSerializer();
        }
        if (JavaUtilSubListSerializer.canSerialize(type)) {
            return new JavaUtilSubListSerializer();
        }
        return null;
    }

    public static Kryo addDefaultSerializers(Kryo kryo) {
        ArrayListSubListSerializer.addDefaultSerializer(kryo);
        JavaUtilSubListSerializer.addDefaultSerializer(kryo);
        return kryo;
    }
}
