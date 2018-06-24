package rx.internal.operators;

import rx.internal.operators.OperatorReplay.BoundedReplayBuffer;

final class OperatorReplay$SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
    private static final long serialVersionUID = -5898283885385201806L;
    final int limit;

    public OperatorReplay$SizeBoundReplayBuffer(int limit) {
        this.limit = limit;
    }

    void truncate() {
        if (this.size > this.limit) {
            removeFirst();
        }
    }
}
