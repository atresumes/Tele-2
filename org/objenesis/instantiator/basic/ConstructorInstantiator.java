package org.objenesis.instantiator.basic;

import java.lang.reflect.Constructor;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class ConstructorInstantiator<T> implements ObjectInstantiator<T> {
    protected Constructor<T> constructor;

    public ConstructorInstantiator(Class<T> type) {
        try {
            this.constructor = type.getDeclaredConstructor((Class[]) null);
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        }
    }

    public T newInstance() {
        try {
            return this.constructor.newInstance((Object[]) null);
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        }
    }
}
