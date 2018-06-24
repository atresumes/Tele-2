package org.objenesis.instantiator.gcj;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public abstract class GCJInstantiatorBase<T> implements ObjectInstantiator<T> {
    static ObjectInputStream dummyStream;
    static Method newObjectMethod = null;
    protected final Class<T> type;

    private static class DummyStream extends ObjectInputStream {
    }

    public abstract T newInstance();

    private static void initialize() {
        if (newObjectMethod == null) {
            try {
                newObjectMethod = ObjectInputStream.class.getDeclaredMethod("newObject", new Class[]{Class.class, Class.class});
                newObjectMethod.setAccessible(true);
                dummyStream = new DummyStream();
            } catch (Throwable e) {
                throw new ObjenesisException(e);
            } catch (Throwable e2) {
                throw new ObjenesisException(e2);
            } catch (Throwable e22) {
                throw new ObjenesisException(e22);
            }
        }
    }

    public GCJInstantiatorBase(Class<T> type) {
        this.type = type;
        initialize();
    }
}
