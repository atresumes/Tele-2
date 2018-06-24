package com.esotericsoftware.kryo.pool;

import com.esotericsoftware.kryo.Kryo;
import java.util.Queue;

class KryoPoolQueueImpl implements KryoPool {
    private final KryoFactory factory;
    private final Queue<Kryo> queue;

    KryoPoolQueueImpl(KryoFactory factory, Queue<Kryo> queue) {
        this.factory = factory;
        this.queue = queue;
    }

    public int size() {
        return this.queue.size();
    }

    public Kryo borrow() {
        Kryo res = (Kryo) this.queue.poll();
        return res != null ? res : this.factory.create();
    }

    public void release(Kryo kryo) {
        this.queue.offer(kryo);
    }

    public <T> T run(KryoCallback<T> callback) {
        Kryo kryo = borrow();
        try {
            T execute = callback.execute(kryo);
            return execute;
        } finally {
            release(kryo);
        }
    }

    public void clear() {
        this.queue.clear();
    }
}
