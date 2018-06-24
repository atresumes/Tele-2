package rx.internal.operators;

import rx.Scheduler;
import rx.internal.operators.OperatorReplay.BoundedReplayBuffer;
import rx.internal.operators.OperatorReplay.Node;
import rx.schedulers.Timestamped;

final class OperatorReplay$SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
    private static final long serialVersionUID = 3457957419649567404L;
    final int limit;
    final long maxAgeInMillis;
    final Scheduler scheduler;

    public OperatorReplay$SizeAndTimeBoundReplayBuffer(int limit, long maxAgeInMillis, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.limit = limit;
        this.maxAgeInMillis = maxAgeInMillis;
    }

    Object enterTransform(Object value) {
        return new Timestamped(this.scheduler.now(), value);
    }

    Object leaveTransform(Object value) {
        return ((Timestamped) value).getValue();
    }

    void truncate() {
        long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
        Node prev = (Node) get();
        Node next = (Node) prev.get();
        int e = 0;
        while (next != null) {
            if (this.size <= this.limit) {
                if (next.value.getTimestampMillis() > timeLimit) {
                    break;
                }
                e++;
                this.size--;
                prev = next;
                next = (Node) next.get();
            } else {
                e++;
                this.size--;
                prev = next;
                next = (Node) next.get();
            }
        }
        if (e != 0) {
            setFirst(prev);
        }
    }

    void truncateFinal() {
        long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
        Node prev = (Node) get();
        Node next = (Node) prev.get();
        int e = 0;
        while (next != null && this.size > 1 && next.value.getTimestampMillis() <= timeLimit) {
            e++;
            this.size--;
            prev = next;
            next = (Node) next.get();
        }
        if (e != 0) {
            setFirst(prev);
        }
    }
}
