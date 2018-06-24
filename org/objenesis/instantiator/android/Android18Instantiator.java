package org.objenesis.instantiator.android;

import java.io.ObjectStreamClass;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class Android18Instantiator<T> implements ObjectInstantiator<T> {
    private final Method newInstanceMethod = getNewInstanceMethod();
    private final Long objectConstructorId = findConstructorIdForJavaLangObjectConstructor();
    private final Class<T> type;

    public Android18Instantiator(Class<T> type) {
        this.type = type;
    }

    public T newInstance() {
        try {
            return this.type.cast(this.newInstanceMethod.invoke(null, new Object[]{this.type, this.objectConstructorId}));
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        }
    }

    private static Method getNewInstanceMethod() {
        try {
            Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[]{Class.class, Long.TYPE});
            newInstanceMethod.setAccessible(true);
            return newInstanceMethod;
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        } catch (Throwable e2) {
            throw new ObjenesisException(e2);
        }
    }

    private static Long findConstructorIdForJavaLangObjectConstructor() {
        try {
            Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[]{Class.class});
            newInstanceMethod.setAccessible(true);
            return (Long) newInstanceMethod.invoke(null, new Object[]{Object.class});
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        } catch (Throwable e2) {
            throw new ObjenesisException(e2);
        } catch (Throwable e22) {
            throw new ObjenesisException(e22);
        } catch (Throwable e222) {
            throw new ObjenesisException(e222);
        }
    }
}
