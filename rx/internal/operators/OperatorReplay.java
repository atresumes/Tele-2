package rx.internal.operators;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;

public final class OperatorReplay<T> extends ConnectableObservable<T> {
    static final Func0 DEFAULT_UNBOUNDED_FACTORY = new C18391();
    final Func0<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Observable<? extends T> source;

    static class C18391 implements Func0 {
        C18391() {
        }

        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    }

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerProducer<T> innerProducer);
    }

    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        long index;
        final NotificationLite<T> nl = NotificationLite.instance();
        int size;
        Node tail;

        public BoundedReplayBuffer() {
            Node n = new Node(null, 0);
            this.tail = n;
            set(n);
        }

        final void addLast(Node n) {
            this.tail.set(n);
            this.tail = n;
            this.size++;
        }

        final void removeFirst() {
            Node next = (Node) ((Node) get()).get();
            if (next == null) {
                throw new IllegalStateException("Empty list!");
            }
            this.size--;
            setFirst(next);
        }

        final void removeSome(int n) {
            Node head = (Node) get();
            while (n > 0) {
                head = (Node) head.get();
                n--;
                this.size--;
            }
            setFirst(head);
        }

        final void setFirst(Node n) {
            set(n);
        }

        public final void next(T value) {
            Object o = enterTransform(this.nl.next(value));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncate();
        }

        public final void error(Throwable e) {
            Object o = enterTransform(this.nl.error(e));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        public final void complete() {
            Object o = enterTransform(this.nl.completed());
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void replay(rx.internal.operators.OperatorReplay.InnerProducer<T> r13) {
            /*
            r12 = this;
            monitor-enter(r13);
            r9 = r13.emitting;	 Catch:{ all -> 0x008c }
            if (r9 == 0) goto L_0x000a;
        L_0x0005:
            r9 = 1;
            r13.missed = r9;	 Catch:{ all -> 0x008c }
            monitor-exit(r13);	 Catch:{ all -> 0x008c }
        L_0x0009:
            return;
        L_0x000a:
            r9 = 1;
            r13.emitting = r9;	 Catch:{ all -> 0x008c }
            monitor-exit(r13);	 Catch:{ all -> 0x008c }
        L_0x000e:
            r9 = r13.isUnsubscribed();
            if (r9 != 0) goto L_0x0009;
        L_0x0014:
            r6 = r13.get();
            r10 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r9 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
            if (r9 != 0) goto L_0x008f;
        L_0x0021:
            r5 = 1;
        L_0x0022:
            r0 = 0;
            r3 = r13.index();
            r3 = (rx.internal.operators.OperatorReplay.Node) r3;
            if (r3 != 0) goto L_0x0039;
        L_0x002c:
            r3 = r12.get();
            r3 = (rx.internal.operators.OperatorReplay.Node) r3;
            r13.index = r3;
            r10 = r3.index;
            r13.addTotalRequested(r10);
        L_0x0039:
            r9 = r13.isUnsubscribed();
            if (r9 != 0) goto L_0x0009;
        L_0x003f:
            r10 = 0;
            r9 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
            if (r9 == 0) goto L_0x00a0;
        L_0x0045:
            r8 = r3.get();
            r8 = (rx.internal.operators.OperatorReplay.Node) r8;
            if (r8 == 0) goto L_0x00a0;
        L_0x004d:
            r9 = r8.value;
            r4 = r12.leaveTransform(r9);
            r9 = r12.nl;	 Catch:{ Throwable -> 0x0061 }
            r10 = r13.child;	 Catch:{ Throwable -> 0x0061 }
            r9 = r9.accept(r10, r4);	 Catch:{ Throwable -> 0x0061 }
            if (r9 == 0) goto L_0x0091;
        L_0x005d:
            r9 = 0;
            r13.index = r9;	 Catch:{ Throwable -> 0x0061 }
            goto L_0x0009;
        L_0x0061:
            r2 = move-exception;
            r9 = 0;
            r13.index = r9;
            rx.exceptions.Exceptions.throwIfFatal(r2);
            r13.unsubscribe();
            r9 = r12.nl;
            r9 = r9.isError(r4);
            if (r9 != 0) goto L_0x0009;
        L_0x0073:
            r9 = r12.nl;
            r9 = r9.isCompleted(r4);
            if (r9 != 0) goto L_0x0009;
        L_0x007b:
            r9 = r13.child;
            r10 = r12.nl;
            r10 = r10.getValue(r4);
            r10 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r2, r10);
            r9.onError(r10);
            goto L_0x0009;
        L_0x008c:
            r9 = move-exception;
            monitor-exit(r13);	 Catch:{ all -> 0x008c }
            throw r9;
        L_0x008f:
            r5 = 0;
            goto L_0x0022;
        L_0x0091:
            r10 = 1;
            r0 = r0 + r10;
            r10 = 1;
            r6 = r6 - r10;
            r3 = r8;
            r9 = r13.isUnsubscribed();
            if (r9 == 0) goto L_0x003f;
        L_0x009e:
            goto L_0x0009;
        L_0x00a0:
            r10 = 0;
            r9 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1));
            if (r9 == 0) goto L_0x00ad;
        L_0x00a6:
            r13.index = r3;
            if (r5 != 0) goto L_0x00ad;
        L_0x00aa:
            r13.produced(r0);
        L_0x00ad:
            monitor-enter(r13);
            r9 = r13.missed;	 Catch:{ all -> 0x00b8 }
            if (r9 != 0) goto L_0x00bb;
        L_0x00b2:
            r9 = 0;
            r13.emitting = r9;	 Catch:{ all -> 0x00b8 }
            monitor-exit(r13);	 Catch:{ all -> 0x00b8 }
            goto L_0x0009;
        L_0x00b8:
            r9 = move-exception;
            monitor-exit(r13);	 Catch:{ all -> 0x00b8 }
            throw r9;
        L_0x00bb:
            r9 = 0;
            r13.missed = r9;	 Catch:{ all -> 0x00b8 }
            monitor-exit(r13);	 Catch:{ all -> 0x00b8 }
            goto L_0x000e;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.BoundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }

        Object enterTransform(Object value) {
            return value;
        }

        Object leaveTransform(Object value) {
            return value;
        }

        void truncate() {
        }

        void truncateFinal() {
        }

        final void collect(Collection<? super T> output) {
            Node n = (Node) get();
            while (true) {
                Node next = (Node) n.get();
                if (next != null) {
                    Object v = leaveTransform(next.value);
                    if (!this.nl.isCompleted(v) && !this.nl.isError(v)) {
                        output.add(this.nl.getValue(v));
                        n = next;
                    } else {
                        return;
                    }
                }
                return;
            }
        }

        boolean hasError() {
            return this.tail.value != null && this.nl.isError(leaveTransform(this.tail.value));
        }

        boolean hasCompleted() {
            return this.tail.value != null && this.nl.isCompleted(leaveTransform(this.tail.value));
        }
    }

    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        public InnerProducer(ReplaySubscriber<T> parent, Subscriber<? super T> child) {
            this.parent = parent;
            this.child = child;
        }

        public void request(long n) {
            if (n >= 0) {
                long r;
                long u;
                do {
                    r = get();
                    if (r == UNSUBSCRIBED) {
                        return;
                    }
                    if (r < 0 || n != 0) {
                        u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                addTotalRequested(n);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        void addTotalRequested(long n) {
            long r;
            long u;
            do {
                r = this.totalRequested.get();
                u = r + n;
                if (u < 0) {
                    u = Long.MAX_VALUE;
                }
            } while (!this.totalRequested.compareAndSet(r, u));
        }

        public long produced(long n) {
            if (n <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            long u;
            long r;
            do {
                r = get();
                if (r == UNSUBSCRIBED) {
                    return UNSUBSCRIBED;
                }
                u = r - n;
                if (u < 0) {
                    throw new IllegalStateException("More produced (" + n + ") than requested (" + r + ")");
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == UNSUBSCRIBED;
        }

        public void unsubscribe() {
            if (get() != UNSUBSCRIBED && getAndSet(UNSUBSCRIBED) != UNSUBSCRIBED) {
                this.parent.remove(this);
                this.parent.manageRequests();
            }
        }

        <U> U index() {
            return this.index;
        }
    }

    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final long index;
        final Object value;

        public Node(Object value, long index) {
            this.value = value;
            this.index = index;
        }
    }

    public static <T, U, R> Observable<R> multicastSelector(final Func0<? extends ConnectableObservable<U>> connectableFactory, final Func1<? super Observable<U>, ? extends Observable<R>> selector) {
        return Observable.create(new OnSubscribe<R>() {
            public void call(final Subscriber<? super R> child) {
                try {
                    ConnectableObservable<U> co = (ConnectableObservable) connectableFactory.call();
                    ((Observable) selector.call(co)).subscribe((Subscriber) child);
                    co.connect(new Action1<Subscription>() {
                        public void call(Subscription t) {
                            child.add(t);
                        }
                    });
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer) child);
                }
            }
        });
    }

    public static <T> ConnectableObservable<T> observeOn(final ConnectableObservable<T> co, Scheduler scheduler) {
        final Observable<T> observable = co.observeOn(scheduler);
        return new ConnectableObservable<T>(new OnSubscribe<T>() {
            public void call(final Subscriber<? super T> child) {
                observable.unsafeSubscribe(new Subscriber<T>(child) {
                    public void onNext(T t) {
                        child.onNext(t);
                    }

                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    public void onCompleted() {
                        child.onCompleted();
                    }
                });
            }
        }) {
            public void connect(Action1<? super Subscription> connection) {
                co.connect(connection);
            }
        };
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source) {
        return create((Observable) source, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, final int bufferSize) {
        if (bufferSize == Integer.MAX_VALUE) {
            return create(source);
        }
        return create((Observable) source, new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeBoundReplayBuffer(bufferSize);
            }
        });
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, long maxAge, TimeUnit unit, Scheduler scheduler) {
        return create(source, maxAge, unit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, long maxAge, TimeUnit unit, final Scheduler scheduler, final int bufferSize) {
        final long maxAgeInMillis = unit.toMillis(maxAge);
        return create((Observable) source, new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeAndTimeBoundReplayBuffer(bufferSize, maxAgeInMillis, scheduler);
            }
        });
    }

    static <T> ConnectableObservable<T> create(Observable<? extends T> source, final Func0<? extends ReplayBuffer<T>> bufferFactory) {
        final AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference();
        return new OperatorReplay(new OnSubscribe<T>() {
            public void call(Subscriber<? super T> child) {
                ReplaySubscriber<T> r;
                ReplaySubscriber<T> u;
                do {
                    r = (ReplaySubscriber) curr.get();
                    if (r != null) {
                        break;
                    }
                    u = new ReplaySubscriber(curr, (ReplayBuffer) bufferFactory.call());
                    u.init();
                } while (!curr.compareAndSet(r, u));
                r = u;
                InnerProducer<T> inner = new InnerProducer(r, child);
                r.add(inner);
                child.add(inner);
                r.buffer.replay(inner);
                child.setProducer(inner);
            }
        }, source, curr, bufferFactory);
    }

    private OperatorReplay(OnSubscribe<T> onSubscribe, Observable<? extends T> source, AtomicReference<ReplaySubscriber<T>> current, Func0<? extends ReplayBuffer<T>> bufferFactory) {
        super(onSubscribe);
        this.source = source;
        this.current = current;
        this.bufferFactory = bufferFactory;
    }

    public void connect(Action1<? super Subscription> connection) {
        ReplaySubscriber<T> ps;
        ReplaySubscriber<T> u;
        boolean doConnect;
        do {
            ps = (ReplaySubscriber) this.current.get();
            if (ps != null && !ps.isUnsubscribed()) {
                break;
            }
            u = new ReplaySubscriber(this.current, (ReplayBuffer) this.bufferFactory.call());
            u.init();
        } while (!this.current.compareAndSet(ps, u));
        ps = u;
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        } else {
            doConnect = true;
        }
        connection.call(ps);
        if (doConnect) {
            this.source.unsafeSubscribe(ps);
        }
    }
}
