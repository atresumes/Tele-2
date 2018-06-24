package rx.observers;

import rx.Observer;
import rx.Subscriber;

public class SerializedSubscriber<T> extends Subscriber<T> {
    private final Observer<T> f244s;

    public SerializedSubscriber(Subscriber<? super T> s) {
        this(s, true);
    }

    public SerializedSubscriber(Subscriber<? super T> s, boolean shareSubscriptions) {
        super(s, shareSubscriptions);
        this.f244s = new SerializedObserver(s);
    }

    public void onCompleted() {
        this.f244s.onCompleted();
    }

    public void onError(Throwable e) {
        this.f244s.onError(e);
    }

    public void onNext(T t) {
        this.f244s.onNext(t);
    }
}
