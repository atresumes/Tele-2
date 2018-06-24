package com.esotericsoftware.kryo;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public abstract class Serializer<T> {
    private boolean acceptsNull;
    private boolean immutable;

    public abstract T read(Kryo kryo, Input input, Class<T> cls);

    public abstract void write(Kryo kryo, Output output, T t);

    public Serializer(boolean acceptsNull) {
        this.acceptsNull = acceptsNull;
    }

    public Serializer(boolean acceptsNull, boolean immutable) {
        this.acceptsNull = acceptsNull;
        this.immutable = immutable;
    }

    public boolean getAcceptsNull() {
        return this.acceptsNull;
    }

    public void setAcceptsNull(boolean acceptsNull) {
        this.acceptsNull = acceptsNull;
    }

    public boolean isImmutable() {
        return this.immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    public void setGenerics(Kryo kryo, Class[] generics) {
    }

    public T copy(Kryo kryo, T original) {
        if (this.immutable) {
            return original;
        }
        throw new KryoException("Serializer does not support copy: " + getClass().getName());
    }
}
