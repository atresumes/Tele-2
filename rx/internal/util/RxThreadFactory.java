package rx.internal.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class RxThreadFactory extends AtomicLong implements ThreadFactory {
    public static final ThreadFactory NONE = new C19381();
    final String prefix;

    static class C19381 implements ThreadFactory {
        C19381() {
        }

        public Thread newThread(Runnable r) {
            throw new AssertionError("No threads allowed.");
        }
    }

    public RxThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.prefix + incrementAndGet());
        t.setDaemon(true);
        return t;
    }
}
