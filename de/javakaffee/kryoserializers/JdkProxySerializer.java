package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkProxySerializer extends Serializer<Object> {
    public Object read(Kryo kryo, Input input, Class<Object> cls) {
        InvocationHandler invocationHandler = (InvocationHandler) kryo.readClassAndObject(input);
        Class[] interfaces = (Class[]) kryo.readObject(input, Class[].class);
        ClassLoader classLoader = kryo.getClassLoader();
        try {
            return Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        } catch (RuntimeException e) {
            System.err.println(getClass().getName() + ".read:\nCould not create proxy using classLoader " + classLoader + ", have invocationhandler.classloader: " + invocationHandler.getClass().getClassLoader() + " have contextclassloader: " + Thread.currentThread().getContextClassLoader());
            throw e;
        }
    }

    public void write(Kryo kryo, Output output, Object obj) {
        kryo.writeClassAndObject(output, Proxy.getInvocationHandler(obj));
        kryo.writeObject(output, obj.getClass().getInterfaces());
    }

    public Object copy(Kryo kryo, Object original) {
        return Proxy.newProxyInstance(kryo.getClassLoader(), original.getClass().getInterfaces(), Proxy.getInvocationHandler(original));
    }
}
