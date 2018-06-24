package rx.internal.operators;

import java.util.ArrayList;
import rx.internal.operators.OperatorReplay.ReplayBuffer;

final class OperatorReplay$UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
    private static final long serialVersionUID = 7063189396499112664L;
    final NotificationLite<T> nl = NotificationLite.instance();
    volatile int size;

    public OperatorReplay$UnboundedReplayBuffer(int capacityHint) {
        super(capacityHint);
    }

    public void next(T value) {
        add(this.nl.next(value));
        this.size++;
    }

    public void error(Throwable e) {
        add(this.nl.error(e));
        this.size++;
    }

    public void complete() {
        add(this.nl.completed());
        this.size++;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void replay(rx.internal.operators.OperatorReplay.InnerProducer<T> r15) {
        /*
        r14 = this;
        monitor-enter(r15);
        r11 = r15.emitting;	 Catch:{ all -> 0x004e }
        if (r11 == 0) goto L_0x000a;
    L_0x0005:
        r11 = 1;
        r15.missed = r11;	 Catch:{ all -> 0x004e }
        monitor-exit(r15);	 Catch:{ all -> 0x004e }
    L_0x0009:
        return;
    L_0x000a:
        r11 = 1;
        r15.emitting = r11;	 Catch:{ all -> 0x004e }
        monitor-exit(r15);	 Catch:{ all -> 0x004e }
    L_0x000e:
        r11 = r15.isUnsubscribed();
        if (r11 != 0) goto L_0x0009;
    L_0x0014:
        r10 = r14.size;
        r1 = r15.index();
        r1 = (java.lang.Integer) r1;
        if (r1 == 0) goto L_0x0051;
    L_0x001e:
        r0 = r1.intValue();
    L_0x0022:
        r6 = r15.get();
        r8 = r6;
        r2 = 0;
    L_0x0029:
        r12 = 0;
        r11 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
        if (r11 == 0) goto L_0x007a;
    L_0x002f:
        if (r0 >= r10) goto L_0x007a;
    L_0x0031:
        r5 = r14.get(r0);
        r11 = r14.nl;	 Catch:{ Throwable -> 0x0053 }
        r12 = r15.child;	 Catch:{ Throwable -> 0x0053 }
        r11 = r11.accept(r12, r5);	 Catch:{ Throwable -> 0x0053 }
        if (r11 != 0) goto L_0x0009;
    L_0x003f:
        r11 = r15.isUnsubscribed();
        if (r11 != 0) goto L_0x0009;
    L_0x0045:
        r0 = r0 + 1;
        r12 = 1;
        r6 = r6 - r12;
        r12 = 1;
        r2 = r2 + r12;
        goto L_0x0029;
    L_0x004e:
        r11 = move-exception;
        monitor-exit(r15);	 Catch:{ all -> 0x004e }
        throw r11;
    L_0x0051:
        r0 = 0;
        goto L_0x0022;
    L_0x0053:
        r4 = move-exception;
        rx.exceptions.Exceptions.throwIfFatal(r4);
        r15.unsubscribe();
        r11 = r14.nl;
        r11 = r11.isError(r5);
        if (r11 != 0) goto L_0x0009;
    L_0x0062:
        r11 = r14.nl;
        r11 = r11.isCompleted(r5);
        if (r11 != 0) goto L_0x0009;
    L_0x006a:
        r11 = r15.child;
        r12 = r14.nl;
        r12 = r12.getValue(r5);
        r12 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, r12);
        r11.onError(r12);
        goto L_0x0009;
    L_0x007a:
        r12 = 0;
        r11 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1));
        if (r11 == 0) goto L_0x0092;
    L_0x0080:
        r11 = java.lang.Integer.valueOf(r0);
        r15.index = r11;
        r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r11 == 0) goto L_0x0092;
    L_0x008f:
        r15.produced(r2);
    L_0x0092:
        monitor-enter(r15);
        r11 = r15.missed;	 Catch:{ all -> 0x009d }
        if (r11 != 0) goto L_0x00a0;
    L_0x0097:
        r11 = 0;
        r15.emitting = r11;	 Catch:{ all -> 0x009d }
        monitor-exit(r15);	 Catch:{ all -> 0x009d }
        goto L_0x0009;
    L_0x009d:
        r11 = move-exception;
        monitor-exit(r15);	 Catch:{ all -> 0x009d }
        throw r11;
    L_0x00a0:
        r11 = 0;
        r15.missed = r11;	 Catch:{ all -> 0x009d }
        monitor-exit(r15);	 Catch:{ all -> 0x009d }
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay$UnboundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
    }
}
