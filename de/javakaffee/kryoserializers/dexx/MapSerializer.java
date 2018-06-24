package de.javakaffee.kryoserializers.dexx;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.andrewoma.dexx.collection.Map;
import com.github.andrewoma.dexx.collection.Maps;
import com.github.andrewoma.dexx.collection.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class MapSerializer extends Serializer<Map<Object, ? extends Object>> {
    private static final boolean DOES_NOT_ACCEPT_NULL = true;
    private static final boolean IMMUTABLE = true;

    public MapSerializer() {
        super(true, true);
    }

    public void write(Kryo kryo, Output output, Map<Object, ? extends Object> immutableMap) {
        kryo.writeObject(output, immutableMap.asMap());
    }

    public Map<Object, Object> read(Kryo kryo, Input input, Class<Map<Object, ? extends Object>> cls) {
        HashMap<Object, Object> map = (HashMap) kryo.readObject(input, HashMap.class);
        ArrayList<Pair<Object, Object>> listOfPairs = new ArrayList();
        for (Entry<Object, Object> entry : map.entrySet()) {
            listOfPairs.add(new Pair(entry.getKey(), entry.getValue()));
        }
        return Maps.copyOf(listOfPairs);
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new MapSerializer();
        kryo.register(Map.class, serializer);
        kryo.register(Maps.of().getClass(), serializer);
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        Object o4 = new Object();
        Object o5 = new Object();
        kryo.register(Maps.of(o1, o1).getClass(), serializer);
        kryo.register(Maps.of(o1, o1, o2, o2).getClass(), serializer);
        kryo.register(Maps.of(o1, o1, o2, o2, o3, o3).getClass(), serializer);
        kryo.register(Maps.of(o1, o1, o2, o2, o3, o3, o4, o4).getClass(), serializer);
        kryo.register(Maps.of(o1, o1, o2, o2, o3, o3, o4, o4, o5, o5).getClass(), serializer);
    }
}
