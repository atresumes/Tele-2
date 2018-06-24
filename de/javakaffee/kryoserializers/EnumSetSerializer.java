package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Iterator;

public class EnumSetSerializer extends Serializer<EnumSet<? extends Enum<?>>> {
    private static final Field TYPE_FIELD;

    static {
        try {
            TYPE_FIELD = EnumSet.class.getDeclaredField("elementType");
            TYPE_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("The EnumSet class seems to have changed, could not access expected field.", e);
        }
    }

    public EnumSet<? extends Enum<?>> copy(Kryo kryo, EnumSet<? extends Enum<?>> original) {
        return original.clone();
    }

    public EnumSet read(Kryo kryo, Input input, Class<EnumSet<? extends Enum<?>>> cls) {
        Class<Enum> elementType = kryo.readClass(input).getType();
        EnumSet result = EnumSet.noneOf(elementType);
        int size = input.readInt(true);
        Enum[] enumConstants = (Enum[]) elementType.getEnumConstants();
        for (int i = 0; i < size; i++) {
            result.add(enumConstants[input.readInt(true)]);
        }
        return result;
    }

    public void write(Kryo kryo, Output output, EnumSet<? extends Enum<?>> set) {
        kryo.writeClass(output, getElementType(set));
        output.writeInt(set.size(), true);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            output.writeInt(((Enum) it.next()).ordinal(), true);
        }
        if (Log.TRACE) {
            Log.trace("kryo", "Wrote EnumSet: " + set);
        }
    }

    private Class<? extends Enum<?>> getElementType(EnumSet<? extends Enum<?>> set) {
        try {
            return (Class) TYPE_FIELD.get(set);
        } catch (Exception e) {
            throw new RuntimeException("Could not access keys field.", e);
        }
    }
}
