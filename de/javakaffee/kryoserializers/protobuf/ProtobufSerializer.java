package de.javakaffee.kryoserializers.protobuf;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.protobuf.GeneratedMessage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProtobufSerializer<T extends GeneratedMessage> extends Serializer<T> {
    private Method parseFromMethod = null;

    public void write(Kryo kryo, Output output, T protobufMessage) {
        if (protobufMessage == null) {
            output.writeByte((byte) 0);
            output.flush();
            return;
        }
        byte[] bytes = protobufMessage.toByteArray();
        output.writeInt(bytes.length + 1, true);
        output.writeBytes(bytes);
        output.flush();
    }

    public T read(Kryo kryo, Input input, Class<T> type) {
        int length = input.readInt(true);
        if (length == 0) {
            return null;
        }
        byte[] bytes = input.readBytes(length - 1);
        try {
            return (GeneratedMessage) getParseFromMethod(type).invoke(type, new Object[]{bytes});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to deserialize protobuf " + e.getMessage(), e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException("Unable to deserialize protobuf " + e2.getMessage(), e2);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException("Unable to deserialize protobuf " + e3.getMessage(), e3);
        }
    }

    private Method getParseFromMethod(Class<T> type) throws NoSuchMethodException {
        if (this.parseFromMethod == null) {
            this.parseFromMethod = type.getMethod("parseFrom", new Class[]{byte[].class});
        }
        return this.parseFromMethod;
    }

    public boolean getAcceptsNull() {
        return true;
    }
}
