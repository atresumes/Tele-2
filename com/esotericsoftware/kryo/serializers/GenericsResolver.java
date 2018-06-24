package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.minlog.Log;
import java.util.Iterator;
import java.util.LinkedList;

public final class GenericsResolver {
    private LinkedList<Generics> stack = new LinkedList();

    Class getConcreteClass(String typeVar) {
        Iterator it = this.stack.iterator();
        while (it.hasNext()) {
            Class clazz = ((Generics) it.next()).getConcreteClass(typeVar);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }

    boolean isSet() {
        return !this.stack.isEmpty();
    }

    void pushScope(Class type, Generics scope) {
        if (Log.TRACE) {
            Log.trace("generics", "Settting a new generics scope for class " + type.getName() + ": " + scope);
        }
        this.stack.addFirst(scope);
    }

    void popScope() {
        this.stack.removeFirst();
    }
}
