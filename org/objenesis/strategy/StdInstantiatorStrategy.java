package org.objenesis.strategy;

import java.io.Serializable;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.android.Android10Instantiator;
import org.objenesis.instantiator.android.Android17Instantiator;
import org.objenesis.instantiator.android.Android18Instantiator;
import org.objenesis.instantiator.basic.AccessibleInstantiator;
import org.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import org.objenesis.instantiator.gcj.GCJInstantiator;
import org.objenesis.instantiator.jrockit.JRockitLegacyInstantiator;
import org.objenesis.instantiator.perc.PercInstantiator;
import org.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;
import org.objenesis.instantiator.sun.UnsafeFactoryInstantiator;

public class StdInstantiatorStrategy extends BaseInstantiatorStrategy {
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
        if (PlatformDescription.isThisJVM("Java HotSpot") || PlatformDescription.isThisJVM(PlatformDescription.OPENJDK)) {
            if (!PlatformDescription.isGoogleAppEngine()) {
                return new SunReflectionFactoryInstantiator(type);
            }
            if (Serializable.class.isAssignableFrom(type)) {
                return new ObjectInputStreamInstantiator(type);
            }
            return new AccessibleInstantiator(type);
        } else if (PlatformDescription.isThisJVM(PlatformDescription.JROCKIT)) {
            if (!PlatformDescription.VM_VERSION.startsWith("1.4") || PlatformDescription.VENDOR_VERSION.startsWith("R") || (PlatformDescription.VM_INFO != null && PlatformDescription.VM_INFO.startsWith("R25.1") && PlatformDescription.VM_INFO.startsWith("R25.2"))) {
                return new SunReflectionFactoryInstantiator(type);
            }
            return new JRockitLegacyInstantiator(type);
        } else if (PlatformDescription.isThisJVM(PlatformDescription.DALVIK)) {
            if (PlatformDescription.ANDROID_VERSION <= 10) {
                return new Android10Instantiator(type);
            }
            if (PlatformDescription.ANDROID_VERSION <= 17) {
                return new Android17Instantiator(type);
            }
            return new Android18Instantiator(type);
        } else if (PlatformDescription.isThisJVM(PlatformDescription.GNU)) {
            return new GCJInstantiator(type);
        } else {
            if (PlatformDescription.isThisJVM(PlatformDescription.PERC)) {
                return new PercInstantiator(type);
            }
            return new UnsafeFactoryInstantiator(type);
        }
    }
}
