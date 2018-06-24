package com.esotericsoftware.kryo;

import com.esotericsoftware.kryo.util.Util;
import com.esotericsoftware.minlog.Log;
import org.objenesis.instantiator.ObjectInstantiator;

public class Registration {
    private final int id;
    private ObjectInstantiator instantiator;
    private Serializer serializer;
    private final Class type;

    public Registration(Class type, Serializer serializer, int id) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            this.type = type;
            this.serializer = serializer;
            this.id = id;
        }
    }

    public Class getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public Serializer getSerializer() {
        return this.serializer;
    }

    public void setSerializer(Serializer serializer) {
        if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        }
        this.serializer = serializer;
        if (Log.TRACE) {
            Log.trace("kryo", "Update registered serializer: " + this.type.getName() + " (" + serializer.getClass().getName() + ")");
        }
    }

    public ObjectInstantiator getInstantiator() {
        return this.instantiator;
    }

    public void setInstantiator(ObjectInstantiator instantiator) {
        if (instantiator == null) {
            throw new IllegalArgumentException("instantiator cannot be null.");
        }
        this.instantiator = instantiator;
    }

    public String toString() {
        return "[" + this.id + ", " + Util.className(this.type) + "]";
    }
}
