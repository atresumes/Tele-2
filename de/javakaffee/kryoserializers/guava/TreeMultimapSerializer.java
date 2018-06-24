package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.TreeMultimap;

public class TreeMultimapSerializer extends MultimapSerializerBase<Comparable, Comparable, TreeMultimap<Comparable, Comparable>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = true;
    private static final boolean IMMUTABLE = false;

    public TreeMultimapSerializer() {
        super(true, false);
    }

    public void write(Kryo kryo, Output output, TreeMultimap<Comparable, Comparable> multimap) {
        writeMultimap(kryo, output, multimap);
    }

    public TreeMultimap<Comparable, Comparable> read(Kryo kryo, Input input, Class<TreeMultimap<Comparable, Comparable>> cls) {
        TreeMultimap<Comparable, Comparable> multimap = TreeMultimap.create();
        readMultimap(kryo, input, multimap);
        return multimap;
    }

    public static void registerSerializers(Kryo kryo) {
        kryo.register(TreeMultimap.class, new TreeMultimapSerializer());
    }
}
