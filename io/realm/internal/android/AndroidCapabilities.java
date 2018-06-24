package io.realm.internal.android;

import android.os.Looper;
import io.realm.internal.Capabilities;

public class AndroidCapabilities implements Capabilities {
    private final boolean isIntentServiceThread = isIntentServiceThread();
    private final Looper looper = Looper.myLooper();

    public boolean canDeliverNotification() {
        return hasLooper() && !this.isIntentServiceThread;
    }

    public void checkCanDeliverNotification(String exceptionMessage) {
        if (!hasLooper()) {
            throw new IllegalStateException(exceptionMessage == null ? "" : exceptionMessage + " " + "Realm cannot be automatically updated on a thread without a looper.");
        } else if (this.isIntentServiceThread) {
            throw new IllegalStateException(exceptionMessage == null ? "" : exceptionMessage + " " + "Realm cannot be automatically updated on an IntentService thread.");
        }
    }

    public boolean isMainThread() {
        return this.looper != null && this.looper == Looper.getMainLooper();
    }

    private boolean hasLooper() {
        return this.looper != null;
    }

    private static boolean isIntentServiceThread() {
        String threadName = Thread.currentThread().getName();
        return threadName != null && threadName.startsWith("IntentService[");
    }
}
