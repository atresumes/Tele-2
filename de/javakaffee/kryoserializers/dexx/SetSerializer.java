package de.javakaffee.kryoserializers.dexx;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.andrewoma.dexx.collection.Builder;
import com.github.andrewoma.dexx.collection.Set;
import com.github.andrewoma.dexx.collection.Sets;

public class SetSerializer extends Serializer<Set<Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = false;
    private static final boolean IMMUTABLE = true;

    public SetSerializer() {
        super(false, true);
    }

    public void write(Kryo kryo, Output output, Set<Object> object) {
        output.writeInt(object.size(), true);
        for (Object elm : object) {
            kryo.writeClassAndObject(output, elm);
        }
    }

    public Set<Object> read(Kryo kryo, Input input, Class<Set<Object>> cls) {
        int size = input.readInt(true);
        Builder<Object, Set<Object>> builder = Sets.builder();
        for (int i = 0; i < size; i++) {
            builder.add(kryo.readClassAndObject(input));
        }
        return (Set) builder.build();
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new SetSerializer();
        kryo.register(Set.class, serializer);
        kryo.register(Sets.of().getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10)).getClass(), serializer);
        kryo.register(Sets.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), new Integer[]{Integer.valueOf(11)}).getClass(), serializer);
    }
}
