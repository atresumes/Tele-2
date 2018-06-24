package com.esotericsoftware.kryo.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ReferenceResolver;
import java.util.ArrayList;

public class MapReferenceResolver implements ReferenceResolver {
    protected Kryo kryo;
    protected final ArrayList readObjects = new ArrayList();
    protected final IdentityObjectIntMap writtenObjects = new IdentityObjectIntMap();

    public void setKryo(Kryo kryo) {
        this.kryo = kryo;
    }

    public int addWrittenObject(Object object) {
        int id = this.writtenObjects.size;
        this.writtenObjects.put(object, id);
        return id;
    }

    public int getWrittenId(Object object) {
        return this.writtenObjects.get(object, -1);
    }

    public int nextReadId(Class type) {
        int id = this.readObjects.size();
        this.readObjects.add(null);
        return id;
    }

    public void setReadObject(int id, Object object) {
        this.readObjects.set(id, object);
    }

    public Object getReadObject(Class type, int id) {
        return this.readObjects.get(id);
    }

    public void reset() {
        this.readObjects.clear();
        this.writtenObjects.clear();
    }

    public boolean useReferences(Class type) {
        return !Util.isWrapperClass(type);
    }
}
