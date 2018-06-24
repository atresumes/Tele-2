package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

public final class CompletableOnSubscribeMerge implements CompletableOnSubscribe {
    final boolean delayErrors;
    final int maxConcurrency;
    final Observable<Completable> source;

    static final class CompletableMergeSubscriber extends Subscriber<Completable> {
        static final AtomicReferenceFieldUpdater<CompletableMergeSubscriber, Queue> ERRORS = AtomicReferenceFieldUpdater.newUpdater(CompletableMergeSubscriber.class, Queue.class, "errors");
        static final AtomicIntegerFieldUpdater<CompletableMergeSubscriber> ONCE = AtomicIntegerFieldUpdater.newUpdater(CompletableMergeSubscriber.class, "once");
        final CompletableSubscriber actual;
        final boolean delayErrors;
        volatile boolean done;
        volatile Queue<Throwable> errors;
        final int maxConcurrency;
        volatile int once;
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(1);

        class C17551 implements CompletableSubscriber {
            Subscription f233d;
            boolean innerDone;

            C17551() {
            }

            public void onSubscribe(Subscription d) {
                this.f233d = d;
                CompletableMergeSubscriber.this.set.add(d);
            }

            public void onError(Throwable e) {
                if (this.innerDone) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                    return;
                }
                this.innerDone = true;
                CompletableMergeSubscriber.this.set.remove(this.f233d);
                CompletableMergeSubscriber.this.getOrCreateErrors().offer(e);
                CompletableMergeSubscriber.this.terminate();
                if (CompletableMergeSubscriber.this.delayErrors && !CompletableMergeSubscriber.this.done) {
                    CompletableMergeSubscriber.this.request(1);
                }
            }

            public void onCompleted() {
                if (!this.innerDone) {
                    this.innerDone = true;
                    CompletableMergeSubscriber.this.set.remove(this.f233d);
                    CompletableMergeSubscriber.this.terminate();
                    if (!CompletableMergeSubscriber.this.done) {
                        CompletableMergeSubscriber.this.request(1);
                    }
                }
            }
        }

        public CompletableMergeSubscriber(CompletableSubscriber actual, int maxConcurrency, boolean delayErrors) {
            this.actual = actual;
            this.maxConcurrency = maxConcurrency;
            this.delayErrors = delayErrors;
            if (maxConcurrency == Integer.MAX_VALUE) {
                request(Long.MAX_VALUE);
            } else {
                request((long) maxConcurrency);
            }
        }

        Queue<Throwable> getOrCreateErrors() {
            Queue<Throwable> q = this.errors;
            if (q != null) {
                return q;
            }
            q = new ConcurrentLinkedQueue();
            if (ERRORS.compareAndSet(this, null, q)) {
                return q;
            }
            return this.errors;
        }

        public void onNext(Completable t) {
            if (!this.done) {
                this.wip.getAndIncrement();
                t.subscribe(new C17551());
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
                return;
            }
            getOrCreateErrors().offer(t);
            this.done = true;
            terminate();
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                terminate();
            }
        }

        void terminate() {
            Queue<Throwable> q;
            Throwable e;
            if (this.wip.decrementAndGet() == 0) {
                q = this.errors;
                if (q == null || q.isEmpty()) {
                    this.actual.onCompleted();
                    return;
                }
                e = CompletableOnSubscribeMerge.collectErrors(q);
                if (ONCE.compareAndSet(this, 0, 1)) {
                    this.actual.onError(e);
                } else {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                }
            } else if (!this.delayErrors) {
                q = this.errors;
                if (q != null && !q.isEmpty()) {
                    e = CompletableOnSubscribeMerge.collectErrors(q);
                    if (ONCE.compareAndSet(this, 0, 1)) {
                        this.actual.onError(e);
                    } else {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeMerge(Observable<? extends Completable> source, int maxConcurrency, boolean delayErrors) {
        this.source = source;
        this.maxConcurrency = maxConcurrency;
        this.delayErrors = delayErrors;
    }

    public void call(CompletableSubscriber s) {
        Subscriber parent = new CompletableMergeSubscriber(s, this.maxConcurrency, this.delayErrors);
        s.onSubscribe(parent);
        this.source.subscribe(parent);
    }

    public static Throwable collectErrors(Queue<Throwable> q) {
        Collection list = new ArrayList();
        while (true) {
            Throwable t = (Throwable) q.poll();
            if (t == null) {
                break;
            }
            list.add(t);
        }
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return (Throwable) list.get(0);
        }
        return new CompositeException(list);
    }
}
