package com.esotericsoftware.kryo.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ReferenceResolver;
import java.util.ArrayList;

public class ListReferenceResolver implements ReferenceResolver {
    protected Kryo kryo;
    protected final ArrayList seenObjects = new ArrayList();

    public void setKryo(Kryo kryo) {
        this.kryo = kryo;
    }

    public int addWrittenObject(Object object) {
        int id = this.seenObjects.size();
        this.seenObjects.add(object);
        return id;
    }

    public int getWrittenId(Object object) {
        int n = this.seenObjects.size();
        for (int i = 0; i < n; i++) {
            if (this.seenObjects.get(i) == object) {
                return i;
            }
        }
        return -1;
    }

    public int nextReadId(Class type) {
        int id = this.seenObjects.size();
        this.seenObjects.add(null);
        return id;
    }

    public void setReadObject(int id, Object object) {
        this.seenObjects.set(id, object);
    }

    public Object getReadObject(Class type, int id) {
        return this.seenObjects.get(id);
    }

    public void reset() {
        this.seenObjects.clear();
    }

    public boolean useReferences(Class type) {
        return !Util.isWrapperClass(type);
    }
}
