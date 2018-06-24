package io.realm.internal;

import io.realm.RealmFieldType;

public class Property implements NativeObject {
    public static final boolean INDEXED = true;
    public static final boolean PRIMARY_KEY = true;
    public static final boolean REQUIRED = true;
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
    private long nativePtr;

    private static native long nativeCreateProperty(String str, int i, String str2);

    private static native long nativeCreateProperty(String str, int i, boolean z, boolean z2, boolean z3);

    private static native long nativeGetFinalizerPtr();

    Property(String name, RealmFieldType type, boolean isPrimary, boolean isIndexed, boolean isRequired) {
        this.nativePtr = nativeCreateProperty(name, type.getNativeValue(), isPrimary, isIndexed, !isRequired);
        NativeContext.dummyContext.addReference(this);
    }

    Property(String name, RealmFieldType type, String linkedClassName) {
        this.nativePtr = nativeCreateProperty(name, type.getNativeValue(), linkedClassName);
        NativeContext.dummyContext.addReference(this);
    }

    protected Property(long nativePtr) {
        this.nativePtr = nativePtr;
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }
}
