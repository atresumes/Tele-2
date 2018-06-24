package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.operators.OperatorReplay.InnerProducer;
import rx.internal.operators.OperatorReplay.ReplayBuffer;
import rx.subscriptions.Subscriptions;

final class OperatorReplay$ReplaySubscriber<T> extends Subscriber<T> implements Subscription {
    static final InnerProducer[] EMPTY = new InnerProducer[0];
    static final InnerProducer[] TERMINATED = new InnerProducer[0];
    final ReplayBuffer<T> buffer;
    boolean done;
    boolean emitting;
    long maxChildRequested;
    long maxUpstreamRequested;
    boolean missed;
    final NotificationLite<T> nl = NotificationLite.instance();
    volatile Producer producer;
    final AtomicReference<InnerProducer[]> producers = new AtomicReference(EMPTY);
    final AtomicBoolean shouldConnect = new AtomicBoolean();

    class C18481 implements Action0 {
        C18481() {
        }

        public void call() {
            OperatorReplay$ReplaySubscriber.this.producers.getAndSet(OperatorReplay$ReplaySubscriber.TERMINATED);
        }
    }

    public OperatorReplay$ReplaySubscriber(AtomicReference<OperatorReplay$ReplaySubscriber<T>> atomicReference, ReplayBuffer<T> buffer) {
        this.buffer = buffer;
        request(0);
    }

    void init() {
        add(Subscriptions.create(new C18481()));
    }

    boolean add(InnerProducer<T> producer) {
        if (producer == null) {
            throw new NullPointerException();
        }
        InnerProducer[] c;
        InnerProducer[] u;
        do {
            c = (InnerProducer[]) this.producers.get();
            if (c == TERMINATED) {
                return false;
            }
            int len = c.length;
            u = new InnerProducer[(len + 1)];
            System.arraycopy(c, 0, u, 0, len);
            u[len] = producer;
        } while (!this.producers.compareAndSet(c, u));
        return true;
    }

    void remove(InnerProducer<T> producer) {
        InnerProducer[] c;
        InnerProducer[] u;
        do {
            c = (InnerProducer[]) this.producers.get();
            if (c != EMPTY && c != TERMINATED) {
                int j = -1;
                int len = c.length;
                for (int i = 0; i < len; i++) {
                    if (c[i].equals(producer)) {
                        j = i;
                        break;
                    }
                }
                if (j < 0) {
                    return;
                }
                if (len == 1) {
                    u = EMPTY;
                } else {
                    u = new InnerProducer[(len - 1)];
                    System.arraycopy(c, 0, u, 0, j);
                    System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.producers.compareAndSet(c, u));
    }

    public void setProducer(Producer p) {
        if (this.producer != null) {
            throw new IllegalStateException("Only a single producer can be set on a Subscriber.");
        }
        this.producer = p;
        manageRequests();
        replay();
    }

    public void onNext(T t) {
        if (!this.done) {
            this.buffer.next(t);
            replay();
        }
    }

    public void onError(Throwable e) {
        if (!this.done) {
            this.done = true;
            try {
                this.buffer.error(e);
                replay();
            } finally {
                unsubscribe();
            }
        }
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            try {
                this.buffer.complete();
                replay();
            } finally {
                unsubscribe();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void manageRequests() {
        /*
        r22 = this;
        r20 = r22.isUnsubscribed();
        if (r20 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        monitor-enter(r22);
        r0 = r22;
        r0 = r0.emitting;	 Catch:{ all -> 0x001a }
        r20 = r0;
        if (r20 == 0) goto L_0x001d;
    L_0x0010:
        r20 = 1;
        r0 = r20;
        r1 = r22;
        r1.missed = r0;	 Catch:{ all -> 0x001a }
        monitor-exit(r22);	 Catch:{ all -> 0x001a }
        goto L_0x0006;
    L_0x001a:
        r20 = move-exception;
        monitor-exit(r22);	 Catch:{ all -> 0x001a }
        throw r20;
    L_0x001d:
        r20 = 1;
        r0 = r20;
        r1 = r22;
        r1.emitting = r0;	 Catch:{ all -> 0x001a }
        monitor-exit(r22);	 Catch:{ all -> 0x001a }
    L_0x0026:
        r20 = r22.isUnsubscribed();
        if (r20 != 0) goto L_0x0006;
    L_0x002c:
        r0 = r22;
        r0 = r0.producers;
        r20 = r0;
        r4 = r20.get();
        r4 = (rx.internal.operators.OperatorReplay.InnerProducer[]) r4;
        r0 = r22;
        r14 = r0.maxChildRequested;
        r10 = r14;
        r5 = r4;
        r9 = r5.length;
        r8 = 0;
    L_0x0040:
        if (r8 >= r9) goto L_0x0055;
    L_0x0042:
        r13 = r5[r8];
        r0 = r13.totalRequested;
        r20 = r0;
        r20 = r20.get();
        r0 = r20;
        r10 = java.lang.Math.max(r10, r0);
        r8 = r8 + 1;
        goto L_0x0040;
    L_0x0055:
        r0 = r22;
        r0 = r0.maxUpstreamRequested;
        r18 = r0;
        r0 = r22;
        r12 = r0.producer;
        r6 = r10 - r14;
        r20 = 0;
        r20 = (r6 > r20 ? 1 : (r6 == r20 ? 0 : -1));
        if (r20 == 0) goto L_0x00b1;
    L_0x0067:
        r0 = r22;
        r0.maxChildRequested = r10;
        if (r12 == 0) goto L_0x009d;
    L_0x006d:
        r20 = 0;
        r20 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
        if (r20 == 0) goto L_0x0099;
    L_0x0073:
        r20 = 0;
        r0 = r20;
        r2 = r22;
        r2.maxUpstreamRequested = r0;
        r20 = r18 + r6;
        r0 = r20;
        r12.request(r0);
    L_0x0082:
        monitor-enter(r22);
        r0 = r22;
        r0 = r0.missed;	 Catch:{ all -> 0x0096 }
        r20 = r0;
        if (r20 != 0) goto L_0x00c7;
    L_0x008b:
        r20 = 0;
        r0 = r20;
        r1 = r22;
        r1.emitting = r0;	 Catch:{ all -> 0x0096 }
        monitor-exit(r22);	 Catch:{ all -> 0x0096 }
        goto L_0x0006;
    L_0x0096:
        r20 = move-exception;
        monitor-exit(r22);	 Catch:{ all -> 0x0096 }
        throw r20;
    L_0x0099:
        r12.request(r6);
        goto L_0x0082;
    L_0x009d:
        r16 = r18 + r6;
        r20 = 0;
        r20 = (r16 > r20 ? 1 : (r16 == r20 ? 0 : -1));
        if (r20 >= 0) goto L_0x00aa;
    L_0x00a5:
        r16 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x00aa:
        r0 = r16;
        r2 = r22;
        r2.maxUpstreamRequested = r0;
        goto L_0x0082;
    L_0x00b1:
        r20 = 0;
        r20 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
        if (r20 == 0) goto L_0x0082;
    L_0x00b7:
        if (r12 == 0) goto L_0x0082;
    L_0x00b9:
        r20 = 0;
        r0 = r20;
        r2 = r22;
        r2.maxUpstreamRequested = r0;
        r0 = r18;
        r12.request(r0);
        goto L_0x0082;
    L_0x00c7:
        r20 = 0;
        r0 = r20;
        r1 = r22;
        r1.missed = r0;	 Catch:{ all -> 0x0096 }
        monitor-exit(r22);	 Catch:{ all -> 0x0096 }
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay$ReplaySubscriber.manageRequests():void");
    }

    void replay() {
        for (InnerProducer<T> rp : (InnerProducer[]) this.producers.get()) {
            this.buffer.replay(rp);
        }
    }
}
