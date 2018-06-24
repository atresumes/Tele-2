package io.realm.internal;

import io.realm.RealmFieldType;
import java.util.ArrayList;
import java.util.List;

public class OsObjectSchemaInfo implements NativeObject {
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
    private long nativePtr;

    public static class Builder {
        private String className;
        private List<Property> propertyList = new ArrayList();

        public Builder(String className) {
            this.className = className;
        }

        public Builder addProperty(String name, RealmFieldType type, boolean isPrimaryKey, boolean isIndexed, boolean isRequired) {
            this.propertyList.add(new Property(name, type, isPrimaryKey, isIndexed, isRequired));
            return this;
        }

        public Builder addLinkedProperty(String name, RealmFieldType type, String linkedClassName) {
            this.propertyList.add(new Property(name, type, linkedClassName));
            return this;
        }

        public OsObjectSchemaInfo build() {
            OsObjectSchemaInfo info = new OsObjectSchemaInfo(this.className);
            for (Property property : this.propertyList) {
                OsObjectSchemaInfo.nativeAddProperty(info.nativePtr, property.getNativePtr());
            }
            return info;
        }
    }

    private static native void nativeAddProperty(long j, long j2);

    private static native long nativeCreateRealmObjectSchema(String str);

    private static native String nativeGetClassName(long j);

    private static native long nativeGetFinalizerPtr();

    private OsObjectSchemaInfo(String className) {
        this(nativeCreateRealmObjectSchema(className));
    }

    private OsObjectSchemaInfo(long nativePtr) {
        this.nativePtr = nativePtr;
        NativeContext.dummyContext.addReference(this);
    }

    public String getClassName() {
        return nativeGetClassName(this.nativePtr);
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }
}
