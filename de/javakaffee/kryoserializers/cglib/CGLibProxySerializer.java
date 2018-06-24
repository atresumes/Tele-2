package de.javakaffee.kryoserializers.cglib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

public class CGLibProxySerializer extends Serializer<Object> {
    public static final String DEFAULT_NAMING_MARKER = "$$EnhancerByCGLIB$$";

    public interface CGLibProxyMarker {
    }

    public static boolean canSerialize(Class<?> cls) {
        return Enhancer.isEnhanced(cls) && cls.getName().indexOf(DEFAULT_NAMING_MARKER) > 0;
    }

    public Object read(Kryo kryo, Input input, Class<Object> cls) {
        return createProxy(kryo.readClass(input).getType(), (Class[]) kryo.readObject(input, Class[].class), (Callback[]) kryo.readObject(input, Callback[].class));
    }

    public void write(Kryo kryo, Output output, Object obj) {
        kryo.writeClass(output, obj.getClass().getSuperclass());
        kryo.writeObject(output, obj.getClass().getInterfaces());
        kryo.writeObject(output, ((Factory) obj).getCallbacks());
    }

    private Object createProxy(Class<?> targetClass, Class<?>[] interfaces, Callback[] callbacks) {
        Enhancer e = new Enhancer();
        e.setInterfaces(interfaces);
        e.setSuperclass(targetClass);
        e.setCallbacks(callbacks);
        return e.create();
    }
}
