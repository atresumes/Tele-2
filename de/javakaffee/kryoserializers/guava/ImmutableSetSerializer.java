package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;

public class ImmutableSetSerializer extends Serializer<ImmutableSet<Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = false;
    private static final boolean IMMUTABLE = true;

    private enum SomeEnum {
        A,
        B,
        C
    }

    public ImmutableSetSerializer() {
        super(false, true);
    }

    public void write(Kryo kryo, Output output, ImmutableSet<Object> object) {
        output.writeInt(object.size(), true);
        UnmodifiableIterator it = object.iterator();
        while (it.hasNext()) {
            kryo.writeClassAndObject(output, it.next());
        }
    }

    public ImmutableSet<Object> read(Kryo kryo, Input input, Class<ImmutableSet<Object>> cls) {
        int size = input.readInt(true);
        Builder<Object> builder = ImmutableSet.builder();
        for (int i = 0; i < size; i++) {
            builder.add(kryo.readClassAndObject(input));
        }
        return builder.build();
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new ImmutableSetSerializer();
        kryo.register(ImmutableSet.class, serializer);
        kryo.register(ImmutableSet.of().getClass(), serializer);
        kryo.register(ImmutableSet.of(Integer.valueOf(1)).getClass(), serializer);
        kryo.register(ImmutableSet.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)).getClass(), serializer);
        kryo.register(Sets.immutableEnumSet(SomeEnum.A, new SomeEnum[]{SomeEnum.B, SomeEnum.C}).getClass(), serializer);
    }
}
