package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.Constructor;
import java.util.Date;

public class DateSerializer extends Serializer<Date> {
    private final Constructor<? extends Date> _constructor;

    public DateSerializer(Class<? extends Date> clazz) {
        try {
            this._constructor = clazz.getConstructor(new Class[]{Long.TYPE});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Date read(Kryo kryo, Input input, Class<Date> cls) {
        try {
            return (Date) this._constructor.newInstance(new Object[]{Long.valueOf(input.readLong(true))});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Kryo kryo, Output output, Date obj) {
        output.writeLong(obj.getTime(), true);
    }

    public Date copy(Kryo kryo, Date original) {
        return (Date) original.clone();
    }
}
