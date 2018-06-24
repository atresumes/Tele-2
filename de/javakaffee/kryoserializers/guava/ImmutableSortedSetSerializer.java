package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Comparator;

public class ImmutableSortedSetSerializer extends Serializer<ImmutableSortedSet<Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = false;
    private static final boolean IMMUTABLE = true;

    public ImmutableSortedSetSerializer() {
        super(false, true);
    }

    public void write(Kryo kryo, Output output, ImmutableSortedSet<Object> object) {
        kryo.writeClassAndObject(output, object.comparator());
        output.writeInt(object.size(), true);
        UnmodifiableIterator it = object.iterator();
        while (it.hasNext()) {
            kryo.writeClassAndObject(output, it.next());
        }
    }

    public ImmutableSortedSet<Object> read(Kryo kryo, Input input, Class<ImmutableSortedSet<Object>> cls) {
        Builder<Object> builder = ImmutableSortedSet.orderedBy((Comparator) kryo.readClassAndObject(input));
        int size = input.readInt(true);
        for (int i = 0; i < size; i++) {
            builder.add(kryo.readClassAndObject(input));
        }
        return builder.build();
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new ImmutableSortedSetSerializer();
        kryo.register(ImmutableSortedSet.class, serializer);
        kryo.register(ImmutableSortedSet.of().getClass(), serializer);
        kryo.register(ImmutableSortedSet.of("").getClass(), serializer);
        kryo.register(ImmutableSortedSet.of().descendingSet().getClass(), serializer);
    }
}
