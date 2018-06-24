package org.objenesis.strategy;

import java.io.NotSerializableException;
import java.io.Serializable;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.android.AndroidSerializationInstantiator;
import org.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import org.objenesis.instantiator.basic.ObjectStreamClassInstantiator;
import org.objenesis.instantiator.gcj.GCJSerializationInstantiator;
import org.objenesis.instantiator.perc.PercSerializationInstantiator;

public class SerializingInstantiatorStrategy extends BaseInstantiatorStrategy {
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
        if (!Serializable.class.isAssignableFrom(type)) {
            throw new ObjenesisException(new NotSerializableException(type + " not serializable"));
        } else if (PlatformDescription.JVM_NAME.startsWith("Java HotSpot") || PlatformDescription.isThisJVM(PlatformDescription.OPENJDK)) {
            if (PlatformDescription.isGoogleAppEngine()) {
                return new ObjectInputStreamInstantiator(type);
            }
            return new ObjectStreamClassInstantiator(type);
        } else if (PlatformDescription.JVM_NAME.startsWith(PlatformDescription.DALVIK)) {
            return new AndroidSerializationInstantiator(type);
        } else {
            if (PlatformDescription.JVM_NAME.startsWith(PlatformDescription.GNU)) {
                return new GCJSerializationInstantiator(type);
            }
            if (PlatformDescription.JVM_NAME.startsWith(PlatformDescription.PERC)) {
                return new PercSerializationInstantiator(type);
            }
            return new ObjectStreamClassInstantiator(type);
        }
    }
}
