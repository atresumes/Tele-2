package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map.Entry;

public class EnumMapSerializer extends Serializer<EnumMap<? extends Enum<?>, ?>> {
    private static final Object FAKE_REFERENCE = new Object();
    private static final Field TYPE_FIELD;

    static {
        try {
            TYPE_FIELD = EnumMap.class.getDeclaredField("keyType");
            TYPE_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("The EnumMap class seems to have changed, could not access expected field.", e);
        }
    }

    public EnumMap<? extends Enum<?>, ?> copy(Kryo kryo, EnumMap<? extends Enum<?>, ?> original) {
        EnumMap copy = new EnumMap(original);
        for (Entry entry : original.entrySet()) {
            copy.put((Enum) entry.getKey(), kryo.copy(entry.getValue()));
        }
        return copy;
    }

    private EnumMap<? extends Enum<?>, ?> create(Kryo kryo, Input input, Class<EnumMap<? extends Enum<?>, ?>> cls) {
        return new EnumMap(kryo.readClass(input).getType());
    }

    public EnumMap<? extends Enum<?>, ?> read(Kryo kryo, Input input, Class<EnumMap<? extends Enum<?>, ?>> type) {
        kryo.reference(FAKE_REFERENCE);
        EnumMap<? extends Enum<?>, ?> result = create(kryo, input, type);
        Enum[] enumConstants = (Enum[]) getKeyType(result).getEnumConstants();
        EnumMap<? extends Enum<?>, ?> rawResult = result;
        int size = input.readInt(true);
        for (int i = 0; i < size; i++) {
            rawResult.put(enumConstants[input.readInt(true)], kryo.readClassAndObject(input));
        }
        return result;
    }

    public void write(Kryo kryo, Output output, EnumMap<? extends Enum<?>, ?> map) {
        kryo.writeClass(output, getKeyType(map));
        output.writeInt(map.size(), true);
        for (Entry<? extends Enum<?>, ?> entry : map.entrySet()) {
            output.writeInt(((Enum) entry.getKey()).ordinal(), true);
            kryo.writeClassAndObject(output, entry.getValue());
        }
        if (Log.TRACE) {
            Log.trace("kryo", "Wrote EnumMap: " + map);
        }
    }

    private Class<Enum<?>> getKeyType(EnumMap<?, ?> map) {
        try {
            return (Class) TYPE_FIELD.get(map);
        } catch (Exception e) {
            throw new RuntimeException("Could not access keys field.", e);
        }
    }
}
