package de.javakaffee.kryoserializers.wicket;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import org.apache.wicket.util.collections.MiniMap;

public class MiniMapSerializer extends Serializer<MiniMap<Object, Object>> {
    private static final Field KEYS_FIELD;

    static {
        try {
            KEYS_FIELD = MiniMap.class.getDeclaredField("keys");
            KEYS_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("The MiniMap seems to have changed, could not access expected field.", e);
        }
    }

    private int getMaxEntries(MiniMap<?, ?> map) {
        try {
            return ((Object[]) KEYS_FIELD.get(map)).length;
        } catch (Exception e) {
            throw new RuntimeException("Could not access keys field.", e);
        }
    }

    public void write(Kryo kryo, Output output, MiniMap<Object, Object> map) {
        output.writeInt(getMaxEntries(map), true);
        output.writeInt(map.size(), true);
        for (Entry<?, ?> entry : map.entrySet()) {
            kryo.writeClassAndObject(output, entry.getKey());
            kryo.writeClassAndObject(output, entry.getValue());
        }
        if (Log.TRACE) {
            Log.trace("kryo", "Wrote map: " + map);
        }
    }

    public MiniMap<Object, Object> read(Kryo kryo, Input input, Class<MiniMap<Object, Object>> cls) {
        MiniMap<Object, Object> result = new MiniMap(input.readInt(true));
        int size = input.readInt(true);
        for (int i = 0; i < size; i++) {
            result.put(kryo.readClassAndObject(input), kryo.readClassAndObject(input));
        }
        return result;
    }
}
