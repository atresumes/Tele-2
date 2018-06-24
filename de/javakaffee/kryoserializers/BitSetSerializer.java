package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.BitSet;

public class BitSetSerializer extends Serializer<BitSet> {
    public BitSet copy(Kryo kryo, BitSet original) {
        BitSet result = new BitSet();
        int length = original.length();
        for (int i = 0; i < length; i++) {
            result.set(i, original.get(i));
        }
        return result;
    }

    public void write(Kryo kryo, Output output, BitSet bitSet) {
        int len = bitSet.length();
        output.writeInt(len, true);
        for (int i = 0; i < len; i++) {
            output.writeBoolean(bitSet.get(i));
        }
    }

    public BitSet read(Kryo kryo, Input input, Class<BitSet> cls) {
        int len = input.readInt(true);
        BitSet ret = new BitSet(len);
        for (int i = 0; i < len; i++) {
            ret.set(i, input.readBoolean());
        }
        return ret;
    }
}
