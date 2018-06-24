package de.javakaffee.kryoserializers.dexx;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.andrewoma.dexx.collection.IndexedLists;
import com.github.andrewoma.dexx.collection.List;

public class ListSerializer extends Serializer<List> {
    private static final boolean DOES_NOT_ACCEPT_NULL = true;
    private static final boolean IMMUTABLE = true;

    public ListSerializer() {
        super(true, true);
    }

    public void write(Kryo kryo, Output output, List object) {
        output.writeInt(object.size(), true);
        for (Object elm : object) {
            kryo.writeClassAndObject(output, elm);
        }
    }

    public List<Object> read(Kryo kryo, Input input, Class<List> cls) {
        int size = input.readInt(true);
        Object[] list = new Object[size];
        for (int i = 0; i < size; i++) {
            list[i] = kryo.readClassAndObject(input);
        }
        return IndexedLists.copyOf(list);
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new ListSerializer();
        kryo.register(List.class, serializer);
        kryo.register(IndexedLists.of().getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(10)).getClass(), serializer);
        kryo.register(IndexedLists.of(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(10), Integer.valueOf(11)).getClass(), serializer);
    }
}
