package org.objenesis.instantiator.perc;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class PercSerializationInstantiator<T> implements ObjectInstantiator<T> {
    private final Method newInstanceMethod;
    private Object[] typeArgs;

    public PercSerializationInstantiator(Class<T> type) {
        for (Class<? super T> unserializableType = type; Serializable.class.isAssignableFrom(unserializableType); unserializableType = unserializableType.getSuperclass()) {
        }
        try {
            Class<?> percMethodClass = Class.forName("COM.newmonics.PercClassLoader.Method");
            this.newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("noArgConstruct", new Class[]{Class.class, Object.class, percMethodClass});
            this.newInstanceMethod.setAccessible(true);
            Object someObject = Class.forName("COM.newmonics.PercClassLoader.PercClass").getDeclaredMethod("getPercClass", new Class[]{Class.class}).invoke(null, new Object[]{unserializableType});
            Object percMethod = someObject.getClass().getDeclaredMethod("findMethod", new Class[]{String.class}).invoke(someObject, new Object[]{"<init>()V"});
            this.typeArgs = new Object[]{unserializableType, type, percMethod};
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

    public T newInstance() {
        try {
            return this.newInstanceMethod.invoke(null, this.typeArgs);
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        } catch (Throwable e2) {
            throw new ObjenesisException(e2);
        }
    }
}
