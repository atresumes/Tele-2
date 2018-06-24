package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sun.reflect.ReflectionFactory;

public class KryoReflectionFactorySupport extends Kryo {
    private static final Object[] INITARGS = new Object[0];
    private static final ReflectionFactory REFLECTION_FACTORY = ReflectionFactory.getReflectionFactory();
    private static final Map<Class<?>, Constructor<?>> _constructors = new ConcurrentHashMap();

    public Serializer<?> getDefaultSerializer(Class type) {
        Serializer<?> result = super.getDefaultSerializer(type);
        if (result instanceof FieldSerializer) {
            ((FieldSerializer) result).setIgnoreSyntheticFields(false);
        }
        return result;
    }

    public <T> T newInstance(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        Constructor<?> constructor = (Constructor) _constructors.get(type);
        if (constructor == null) {
            constructor = getNoArgsConstructor(type);
            if (constructor == null) {
                constructor = newConstructorForSerialization(type);
            }
            _constructors.put(type, constructor);
        }
        return newInstanceFrom(constructor);
    }

    private static Object newInstanceFrom(Constructor<?> constructor) {
        try {
            return constructor.newInstance(INITARGS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstanceFromReflectionFactory(Class<T> type) {
        Constructor<?> constructor = (Constructor) _constructors.get(type);
        if (constructor == null) {
            constructor = newConstructorForSerialization(type);
            _constructors.put(type, constructor);
        }
        return newInstanceFrom(constructor);
    }

    private static <T> Constructor<?> newConstructorForSerialization(Class<T> type) {
        try {
            Constructor<?> constructor = REFLECTION_FACTORY.newConstructorForSerialization(type, Object.class.getDeclaredConstructor(new Class[0]));
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Constructor<?> getNoArgsConstructor(Class<?> type) {
        for (Constructor<?> constructor : type.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                constructor.setAccessible(true);
                return constructor;
            }
        }
        return null;
    }
}
