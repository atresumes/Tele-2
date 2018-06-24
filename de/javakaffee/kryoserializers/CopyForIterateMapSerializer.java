package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CopyForIterateMapSerializer extends MapSerializer {
    public void write(Kryo kryo, Output output, Map object) {
        Map map;
        if (object instanceof LinkedHashMap) {
            map = new LinkedHashMap(object);
        } else {
            map = new HashMap(object);
        }
        super.write(kryo, output, map);
    }
}
