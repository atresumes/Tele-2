package com.esotericsoftware.kryo.pool;

import com.esotericsoftware.kryo.Kryo;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface KryoPool {

    public static class Builder {
        private final KryoFactory factory;
        private Queue<Kryo> queue = new ConcurrentLinkedQueue();
        private boolean softReferences;

        public Builder(KryoFactory factory) {
            if (factory == null) {
                throw new IllegalArgumentException("factory must not be null");
            }
            this.factory = factory;
        }

        public Builder queue(Queue<Kryo> queue) {
            if (queue == null) {
                throw new IllegalArgumentException("queue must not be null");
            }
            this.queue = queue;
            return this;
        }

        public Builder softReferences() {
            this.softReferences = true;
            return this;
        }

        public KryoPool build() {
            return new KryoPoolQueueImpl(this.factory, this.softReferences ? new SoftReferenceQueue(this.queue) : this.queue);
        }

        public String toString() {
            return getClass().getName() + "[queue.class=" + this.queue.getClass() + ", softReferences=" + this.softReferences + "]";
        }
    }

    Kryo borrow();

    void release(Kryo kryo);

    <T> T run(KryoCallback<T> kryoCallback);
}
