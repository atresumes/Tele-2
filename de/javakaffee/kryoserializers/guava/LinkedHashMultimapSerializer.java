package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.LinkedHashMultimap;

public class LinkedHashMultimapSerializer extends MultimapSerializerBase<Object, Object, LinkedHashMultimap<Object, Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = false;
    private static final boolean IMMUTABLE = false;

    public LinkedHashMultimapSerializer() {
        super(false, false);
    }

    public void write(Kryo kryo, Output output, LinkedHashMultimap<Object, Object> multimap) {
        writeMultimap(kryo, output, multimap);
    }

    public LinkedHashMultimap<Object, Object> read(Kryo kryo, Input input, Class<LinkedHashMultimap<Object, Object>> cls) {
        LinkedHashMultimap<Object, Object> multimap = LinkedHashMultimap.create();
        readMultimap(kryo, input, multimap);
        return multimap;
    }

    public static void registerSerializers(Kryo kryo) {
        kryo.register(LinkedHashMultimap.class, new LinkedHashMultimapSerializer());
    }
}
