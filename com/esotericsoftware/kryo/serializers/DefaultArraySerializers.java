package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;

public class DefaultArraySerializers {

    public static class BooleanArraySerializer extends Serializer<boolean[]> {
        public BooleanArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, boolean[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            for (boolean writeBoolean : object) {
                output.writeBoolean(writeBoolean);
            }
        }

        public boolean[] read(Kryo kryo, Input input, Class<boolean[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            length--;
            boolean[] array = new boolean[length];
            for (int i = 0; i < length; i++) {
                array[i] = input.readBoolean();
            }
            return array;
        }

        public boolean[] copy(Kryo kryo, boolean[] original) {
            boolean[] copy = new boolean[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class ByteArraySerializer extends Serializer<byte[]> {
        public ByteArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, byte[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            output.writeBytes(object);
        }

        public byte[] read(Kryo kryo, Input input, Class<byte[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            return input.readBytes(length - 1);
        }

        public byte[] copy(Kryo kryo, byte[] original) {
            byte[] copy = new byte[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class CharArraySerializer extends Serializer<char[]> {
        public CharArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, char[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            output.writeChars(object);
        }

        public char[] read(Kryo kryo, Input input, Class<char[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            return input.readChars(length - 1);
        }

        public char[] copy(Kryo kryo, char[] original) {
            char[] copy = new char[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class DoubleArraySerializer extends Serializer<double[]> {
        public DoubleArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, double[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            output.writeDoubles(object);
        }

        public double[] read(Kryo kryo, Input input, Class<double[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            return input.readDoubles(length - 1);
        }

        public double[] copy(Kryo kryo, double[] original) {
            double[] copy = new double[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class FloatArraySerializer extends Serializer<float[]> {
        public FloatArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, float[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            output.writeFloats(object);
        }

        public float[] read(Kryo kryo, Input input, Class<float[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            return input.readFloats(length - 1);
        }

        public float[] copy(Kryo kryo, float[] original) {
            float[] copy = new float[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class IntArraySerializer extends Serializer<int[]> {
        public IntArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, int[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            output.writeInts(object, false);
        }

        public int[] read(Kryo kryo, Input input, Class<int[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            return input.readInts(length - 1, false);
        }

        public int[] copy(Kryo kryo, int[] original) {
            int[] copy = new int[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class LongArraySerializer extends Serializer<long[]> {
        public LongArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, long[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            output.writeLongs(object, false);
        }

        public long[] read(Kryo kryo, Input input, Class<long[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            return input.readLongs(length - 1, false);
        }

        public long[] copy(Kryo kryo, long[] original) {
            long[] copy = new long[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class ObjectArraySerializer extends Serializer<Object[]> {
        private boolean elementsAreSameType;
        private boolean elementsCanBeNull = true;
        private Class[] generics;
        private final Class type;

        public ObjectArraySerializer(Kryo kryo, Class type) {
            setAcceptsNull(true);
            this.type = type;
            if ((type.getComponentType().getModifiers() & 16) != 0) {
                setElementsAreSameType(true);
            }
        }

        public void write(Kryo kryo, Output output, Object[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            Class elementClass = object.getClass().getComponentType();
            int n;
            int i;
            if (this.elementsAreSameType || Modifier.isFinal(elementClass.getModifiers())) {
                Serializer elementSerializer = kryo.getSerializer(elementClass);
                elementSerializer.setGenerics(kryo, this.generics);
                n = object.length;
                for (i = 0; i < n; i++) {
                    if (this.elementsCanBeNull) {
                        kryo.writeObjectOrNull(output, object[i], elementSerializer);
                    } else {
                        kryo.writeObject(output, object[i], elementSerializer);
                    }
                }
                return;
            }
            n = object.length;
            for (i = 0; i < n; i++) {
                if (object[i] != null) {
                    kryo.getSerializer(object[i].getClass()).setGenerics(kryo, this.generics);
                }
                kryo.writeClassAndObject(output, object[i]);
            }
        }

        public Object[] read(Kryo kryo, Input input, Class<Object[]> type) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            Object[] object = (Object[]) Array.newInstance(type.getComponentType(), length - 1);
            kryo.reference(object);
            Class elementClass = object.getClass().getComponentType();
            int n;
            int i;
            if (this.elementsAreSameType || Modifier.isFinal(elementClass.getModifiers())) {
                Serializer elementSerializer = kryo.getSerializer(elementClass);
                elementSerializer.setGenerics(kryo, this.generics);
                n = object.length;
                for (i = 0; i < n; i++) {
                    if (this.elementsCanBeNull) {
                        object[i] = kryo.readObjectOrNull(input, elementClass, elementSerializer);
                    } else {
                        object[i] = kryo.readObject(input, elementClass, elementSerializer);
                    }
                }
                return object;
            }
            n = object.length;
            for (i = 0; i < n; i++) {
                Registration registration = kryo.readClass(input);
                if (registration != null) {
                    registration.getSerializer().setGenerics(kryo, this.generics);
                    object[i] = kryo.readObject(input, registration.getType(), registration.getSerializer());
                } else {
                    object[i] = null;
                }
            }
            return object;
        }

        public Object[] copy(Kryo kryo, Object[] original) {
            Object[] copy = (Object[]) Array.newInstance(original.getClass().getComponentType(), original.length);
            int n = original.length;
            for (int i = 0; i < n; i++) {
                copy[i] = kryo.copy(original[i]);
            }
            return copy;
        }

        public void setElementsCanBeNull(boolean elementsCanBeNull) {
            this.elementsCanBeNull = elementsCanBeNull;
        }

        public void setElementsAreSameType(boolean elementsAreSameType) {
            this.elementsAreSameType = elementsAreSameType;
        }

        public void setGenerics(Kryo kryo, Class[] generics) {
            if (Log.TRACE) {
                Log.trace("kryo", "setting generics for ObjectArraySerializer");
            }
            this.generics = generics;
        }
    }

    public static class ShortArraySerializer extends Serializer<short[]> {
        public ShortArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, short[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            output.writeShorts(object);
        }

        public short[] read(Kryo kryo, Input input, Class<short[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            return input.readShorts(length - 1);
        }

        public short[] copy(Kryo kryo, short[] original) {
            short[] copy = new short[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }

    public static class StringArraySerializer extends Serializer<String[]> {
        public StringArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, String[] object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            output.writeVarInt(object.length + 1, true);
            if (kryo.getReferences() && kryo.getReferenceResolver().useReferences(String.class)) {
                Serializer serializer = kryo.getSerializer(String.class);
                for (Object writeObjectOrNull : object) {
                    kryo.writeObjectOrNull(output, writeObjectOrNull, serializer);
                }
                return;
            }
            for (String writeString : object) {
                output.writeString(writeString);
            }
        }

        public String[] read(Kryo kryo, Input input, Class<String[]> cls) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            length--;
            String[] array = new String[length];
            int i;
            if (kryo.getReferences() && kryo.getReferenceResolver().useReferences(String.class)) {
                Serializer serializer = kryo.getSerializer(String.class);
                for (i = 0; i < length; i++) {
                    array[i] = (String) kryo.readObjectOrNull(input, String.class, serializer);
                }
                return array;
            }
            for (i = 0; i < length; i++) {
                array[i] = input.readString();
            }
            return array;
        }

        public String[] copy(Kryo kryo, String[] original) {
            String[] copy = new String[original.length];
            System.arraycopy(original, 0, copy, 0, copy.length);
            return copy;
        }
    }
}
