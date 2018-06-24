package org.objenesis.instantiator.basic;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class NewInstanceInstantiator<T> implements ObjectInstantiator<T> {
    private final Class<T> type;

    public NewInstanceInstantiator(Class<T> type) {
        this.type = type;
    }

    public T newInstance() {
        try {
            return this.type.newInstance();
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        }
    }
}
