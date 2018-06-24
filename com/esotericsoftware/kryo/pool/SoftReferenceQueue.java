package com.esotericsoftware.kryo.pool;

import com.esotericsoftware.kryo.Kryo;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

class SoftReferenceQueue implements Queue<Kryo> {
    private Queue<SoftReference<Kryo>> delegate;

    public SoftReferenceQueue(Queue<?> delegate) {
        this.delegate = delegate;
    }

    public Kryo poll() {
        Kryo res;
        do {
            SoftReference<Kryo> ref = (SoftReference) this.delegate.poll();
            if (ref == null) {
                return null;
            }
            res = (Kryo) ref.get();
        } while (res == null);
        return res;
    }

    public boolean offer(Kryo e) {
        return this.delegate.offer(new SoftReference(e));
    }

    public boolean add(Kryo e) {
        return this.delegate.add(new SoftReference(e));
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    public boolean contains(Object o) {
        return this.delegate.contains(o);
    }

    public void clear() {
        this.delegate.clear();
    }

    public boolean equals(Object o) {
        return this.delegate.equals(o);
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public String toString() {
        return getClass().getSimpleName() + super.toString();
    }

    public Iterator<Kryo> iterator() {
        throw new UnsupportedOperationException();
    }

    public Kryo remove() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public Kryo element() {
        throw new UnsupportedOperationException();
    }

    public Kryo peek() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] tArr) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends Kryo> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
}
