package org.objenesis.instantiator.sun;

import java.lang.reflect.Field;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import sun.misc.Unsafe;

public class UnsafeFactoryInstantiator<T> implements ObjectInstantiator<T> {
    private static Unsafe unsafe;
    private final Class<T> type;

    public UnsafeFactoryInstantiator(Class<T> type) {
        if (unsafe == null) {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                try {
                    unsafe = (Unsafe) f.get(null);
                } catch (Throwable e) {
                    throw new ObjenesisException(e);
                }
            } catch (Throwable e2) {
                throw new ObjenesisException(e2);
            }
        }
        this.type = type;
    }

    public T newInstance() {
        try {
            return this.type.cast(unsafe.allocateInstance(this.type));
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        }
    }
}
