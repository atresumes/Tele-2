package org.objenesis.instantiator.jrockit;

import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class JRockitLegacyInstantiator<T> implements ObjectInstantiator<T> {
    private static Method safeAllocObjectMethod = null;
    private final Class<T> type;

    private static void initialize() {
        if (safeAllocObjectMethod == null) {
            try {
                safeAllocObjectMethod = Class.forName("jrockit.vm.MemSystem").getDeclaredMethod("safeAllocObject", new Class[]{Class.class});
                safeAllocObjectMethod.setAccessible(true);
            } catch (Throwable e) {
                throw new ObjenesisException(e);
            } catch (Throwable e2) {
                throw new ObjenesisException(e2);
            } catch (Throwable e22) {
                throw new ObjenesisException(e22);
            }
        }
    }

    public JRockitLegacyInstantiator(Class<T> type) {
        initialize();
        this.type = type;
    }

    public T newInstance() {
        try {
            return this.type.cast(safeAllocObjectMethod.invoke(null, new Object[]{this.type}));
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        }
    }
}
