package rx.internal.util;

import rx.Subscription;

public class SynchronizedSubscription implements Subscription {
    private final Subscription f242s;

    public SynchronizedSubscription(Subscription s) {
        this.f242s = s;
    }

    public synchronized void unsubscribe() {
        this.f242s.unsubscribe();
    }

    public synchronized boolean isUnsubscribed() {
        return this.f242s.isUnsubscribed();
    }
}
