package org.objenesis.instantiator.basic;

import org.objenesis.instantiator.ObjectInstantiator;

public class NullInstantiator<T> implements ObjectInstantiator<T> {
    public NullInstantiator(Class<T> cls) {
    }

    public T newInstance() {
        return null;
    }
}
