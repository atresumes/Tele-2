package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ImmutableMultimapSerializer extends Serializer<ImmutableMultimap<Object, Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = true;
    private static final boolean IMMUTABLE = true;

    public ImmutableMultimapSerializer() {
        super(true, true);
    }

    public void write(Kryo kryo, Output output, ImmutableMultimap<Object, Object> immutableMultiMap) {
        kryo.writeObject(output, ImmutableMap.copyOf(immutableMultiMap.asMap()));
    }

    public ImmutableMultimap<Object, Object> read(Kryo kryo, Input input, Class<ImmutableMultimap<Object, Object>> cls) {
        Set<Entry<Object, List<? extends Object>>> entries = ((Map) kryo.readObject(input, ImmutableMap.class)).entrySet();
        Builder<Object, Object> builder = ImmutableMultimap.builder();
        for (Entry<Object, List<? extends Object>> entry : entries) {
            builder.putAll(entry.getKey(), (Iterable) entry.getValue());
        }
        return builder.build();
    }

    public static void registerSerializers(Kryo kryo) {
        if (!(kryo.getSerializer(ImmutableList.class) instanceof ImmutableListSerializer)) {
            ImmutableListSerializer.registerSerializers(kryo);
        }
        if (!(kryo.getSerializer(ImmutableMap.class) instanceof ImmutableMapSerializer)) {
            ImmutableMapSerializer.registerSerializers(kryo);
        }
        Serializer serializer = new ImmutableMultimapSerializer();
        kryo.register(ImmutableMultimap.class, serializer);
        kryo.register(ImmutableMultimap.of().getClass(), serializer);
        Object o = new Object();
        kryo.register(ImmutableMultimap.of(o, o).getClass(), serializer);
    }
}
