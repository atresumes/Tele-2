package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class EnumNameSerializer extends Serializer<Enum> {
    private final Class<? extends Enum> enumType;
    private final Serializer stringSerializer;

    public EnumNameSerializer(Kryo kryo, Class<? extends Enum> type) {
        this.enumType = type;
        this.stringSerializer = kryo.getSerializer(String.class);
        setImmutable(true);
    }

    public void write(Kryo kryo, Output output, Enum object) {
        kryo.writeObject(output, object.name(), this.stringSerializer);
    }

    public Enum read(Kryo kryo, Input input, Class<Enum> cls) {
        String name = (String) kryo.readObject(input, String.class, this.stringSerializer);
        try {
            return Enum.valueOf(this.enumType, name);
        } catch (IllegalArgumentException e) {
            throw new KryoException("Invalid name for enum \"" + this.enumType.getName() + "\": " + name, e);
        }
    }
}
