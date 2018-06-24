package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ImmutableMapSerializer extends Serializer<ImmutableMap<Object, ? extends Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = true;
    private static final boolean IMMUTABLE = true;

    private enum DummyEnum {
        VALUE1,
        VALUE2
    }

    public ImmutableMapSerializer() {
        super(true, true);
    }

    public void write(Kryo kryo, Output output, ImmutableMap<Object, ? extends Object> immutableMap) {
        kryo.writeObject(output, Maps.newHashMap(immutableMap));
    }

    public ImmutableMap<Object, Object> read(Kryo kryo, Input input, Class<ImmutableMap<Object, ? extends Object>> cls) {
        return ImmutableMap.copyOf((Map) kryo.readObject(input, HashMap.class));
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new ImmutableMapSerializer();
        kryo.register(ImmutableMap.class, serializer);
        kryo.register(ImmutableMap.of().getClass(), serializer);
        Object o1 = new Object();
        Object o2 = new Object();
        kryo.register(ImmutableMap.of(o1, o1).getClass(), serializer);
        kryo.register(ImmutableMap.of(o1, o1, o2, o2).getClass(), serializer);
        Map<DummyEnum, Object> enumMap = new EnumMap(DummyEnum.class);
        for (DummyEnum e : DummyEnum.values()) {
            enumMap.put(e, o1);
        }
        kryo.register(ImmutableMap.copyOf(enumMap).getClass(), serializer);
    }
}
