package de.javakaffee.kryoserializers.guava;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.util.NavigableSet;
import java.util.TreeSet;

public class UnmodifiableNavigableSetSerializer extends Serializer<NavigableSet<?>> {
    Field delegate;

    public UnmodifiableNavigableSetSerializer() {
        super(false);
        try {
            this.delegate = Class.forName(Sets.class.getCanonicalName() + "$UnmodifiableNavigableSet").getDeclaredField("delegate");
            this.delegate.setAccessible(true);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Issues reflectively writing UnmodifiableNavigableSet", e);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException("Issues reflectively writing UnmodifiableNavigableSet", e2);
        } catch (SecurityException e3) {
            throw new RuntimeException("Issues reflectively writing UnmodifiableNavigableSet", e3);
        } catch (NoSuchFieldException e4) {
            throw new RuntimeException("Issues reflectively writing UnmodifiableNavigableSet", e4);
        }
    }

    @VisibleForTesting
    protected Object getDelegateFromUnmodifiableNavigableSet(NavigableSet<?> object) {
        try {
            return this.delegate.get(object);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Issues reflectively writing UnmodifiableNavigableSet", e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("Issues reflectively writing UnmodifiableNavigableSet", e2);
        }
    }

    public void write(Kryo kryo, Output output, NavigableSet<?> object) {
        kryo.writeClassAndObject(output, getDelegateFromUnmodifiableNavigableSet(object));
    }

    public NavigableSet<?> read(Kryo kryo, Input input, Class<NavigableSet<?>> cls) {
        return Sets.unmodifiableNavigableSet((NavigableSet) kryo.readClassAndObject(input));
    }

    public NavigableSet<?> copy(Kryo kryo, NavigableSet<?> original) {
        return Sets.unmodifiableNavigableSet((NavigableSet) kryo.copy(getDelegateFromUnmodifiableNavigableSet(original)));
    }

    public static void registerSerializers(Kryo kryo) {
        kryo.register(Sets.unmodifiableNavigableSet(new TreeSet()).getClass(), new UnmodifiableNavigableSetSerializer());
    }
}
