package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class ClosureSerializer extends Serializer {
    private static Method readResolve;
    private static Class serializedLambda = SerializedLambda.class;

    public static class Closure {
    }

    static {
        try {
            readResolve = serializedLambda.getDeclaredMethod("readResolve", new Class[0]);
            readResolve.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Could not obtain SerializedLambda or its methods via reflection", e);
        }
    }

    public void write(Kryo kryo, Output output, Object object) {
        try {
            Method writeReplace = object.getClass().getDeclaredMethod("writeReplace", new Class[0]);
            writeReplace.setAccessible(true);
            Object replacement = writeReplace.invoke(object, new Object[0]);
            if (serializedLambda.isInstance(replacement)) {
                kryo.writeObject(output, replacement);
                return;
            }
            throw new RuntimeException("Could not serialize lambda");
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize lambda", e);
        }
    }

    public Object read(Kryo kryo, Input input, Class type) {
        try {
            return readResolve.invoke(kryo.readObject(input, serializedLambda), new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize lambda", e);
        }
    }

    public Object copy(Kryo kryo, Object original) {
        try {
            Method writeReplace = original.getClass().getDeclaredMethod("writeReplace", new Class[0]);
            writeReplace.setAccessible(true);
            Object replacement = writeReplace.invoke(original, new Object[0]);
            if (serializedLambda.isInstance(replacement)) {
                return readResolve.invoke(replacement, new Object[0]);
            }
            throw new RuntimeException("Could not serialize lambda");
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize lambda", e);
        }
    }
}
