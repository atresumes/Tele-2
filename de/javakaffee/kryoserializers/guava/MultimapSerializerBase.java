package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Multimap;
import java.util.Map.Entry;

public abstract class MultimapSerializerBase<K, V, T extends Multimap<K, V>> extends Serializer<T> {
    public MultimapSerializerBase(boolean acceptsNull, boolean immutable) {
        super(acceptsNull, immutable);
    }

    protected void writeMultimap(Kryo kryo, Output output, Multimap<K, V> multimap) {
        output.writeInt(multimap.size(), true);
        for (Entry<K, V> entry : multimap.entries()) {
            kryo.writeClassAndObject(output, entry.getKey());
            kryo.writeClassAndObject(output, entry.getValue());
        }
    }

    protected void readMultimap(Kryo kryo, Input input, Multimap<K, V> multimap) {
        int size = input.readInt(true);
        for (int i = 0; i < size; i++) {
            multimap.put(kryo.readClassAndObject(input), kryo.readClassAndObject(input));
        }
    }
}
