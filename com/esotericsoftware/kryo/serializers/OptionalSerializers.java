package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Util;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public final class OptionalSerializers {

    private static class OptionalDoubleSerializer extends Serializer<OptionalDouble> {
        private OptionalDoubleSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, OptionalDouble object) {
            output.writeBoolean(object.isPresent());
            if (object.isPresent()) {
                output.writeDouble(object.getAsDouble());
            }
        }

        public OptionalDouble read(Kryo kryo, Input input, Class<OptionalDouble> cls) {
            return input.readBoolean() ? OptionalDouble.of(input.readDouble()) : OptionalDouble.empty();
        }
    }

    private static class OptionalIntSerializer extends Serializer<OptionalInt> {
        private OptionalIntSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, OptionalInt object) {
            output.writeBoolean(object.isPresent());
            if (object.isPresent()) {
                output.writeInt(object.getAsInt());
            }
        }

        public OptionalInt read(Kryo kryo, Input input, Class<OptionalInt> cls) {
            return input.readBoolean() ? OptionalInt.of(input.readInt()) : OptionalInt.empty();
        }
    }

    private static class OptionalLongSerializer extends Serializer<OptionalLong> {
        private OptionalLongSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, OptionalLong object) {
            output.writeBoolean(object.isPresent());
            if (object.isPresent()) {
                output.writeLong(object.getAsLong());
            }
        }

        public OptionalLong read(Kryo kryo, Input input, Class<OptionalLong> cls) {
            return input.readBoolean() ? OptionalLong.of(input.readLong()) : OptionalLong.empty();
        }
    }

    private static class OptionalSerializer extends Serializer<Optional> {
        private OptionalSerializer() {
            setAcceptsNull(false);
        }

        public void write(Kryo kryo, Output output, Optional object) {
            kryo.writeClassAndObject(output, object.isPresent() ? object.get() : null);
        }

        public Optional read(Kryo kryo, Input input, Class<Optional> cls) {
            return Optional.ofNullable(kryo.readClassAndObject(input));
        }

        public Optional copy(Kryo kryo, Optional original) {
            if (original.isPresent()) {
                return Optional.of(kryo.copy(original.get()));
            }
            return original;
        }
    }

    public static void addDefaultSerializers(Kryo kryo) {
        if (Util.isClassAvailable("java.util.Optional")) {
            kryo.addDefaultSerializer(Optional.class, new OptionalSerializer());
        }
        if (Util.isClassAvailable("java.util.OptionalInt")) {
            kryo.addDefaultSerializer(OptionalInt.class, new OptionalIntSerializer());
        }
        if (Util.isClassAvailable("java.util.OptionalLong")) {
            kryo.addDefaultSerializer(OptionalLong.class, new OptionalLongSerializer());
        }
        if (Util.isClassAvailable("java.util.OptionalDouble")) {
            kryo.addDefaultSerializer(OptionalDouble.class, new OptionalDoubleSerializer());
        }
    }
}
