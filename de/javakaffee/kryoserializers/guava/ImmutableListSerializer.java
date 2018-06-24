package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;

public class ImmutableListSerializer extends Serializer<ImmutableList<Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = false;
    private static final boolean IMMUTABLE = true;

    public ImmutableListSerializer() {
        super(false, true);
    }

    public void write(Kryo kryo, Output output, ImmutableList<Object> object) {
        output.writeInt(object.size(), true);
        UnmodifiableIterator it = object.iterator();
        while (it.hasNext()) {
            kryo.writeClassAndObject(output, it.next());
        }
    }

    public ImmutableList<Object> read(Kryo kryo, Input input, Class<ImmutableList<Object>> cls) {
        int size = input.readInt(true);
        Object[] list = new Object[size];
        for (int i = 0; i < size; i++) {
            list[i] = kryo.readClassAndObject(input);
        }
        return ImmutableList.copyOf(list);
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new ImmutableListSerializer();
        kryo.register(ImmutableList.class, serializer);
        kryo.register(ImmutableList.of().getClass(), serializer);
        kryo.register(ImmutableList.of(Integer.valueOf(1)).getClass(), serializer);
        kryo.register(ImmutableList.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)).subList(1, 2).getClass(), serializer);
        kryo.register(ImmutableList.of().reverse().getClass(), serializer);
        kryo.register(Lists.charactersOf("KryoRocks").getClass(), serializer);
        Table<Integer, Integer, Integer> baseTable = HashBasedTable.create();
        baseTable.put(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
        baseTable.put(Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6));
        kryo.register(ImmutableTable.copyOf(baseTable).values().getClass(), serializer);
    }
}
