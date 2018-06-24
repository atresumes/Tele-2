package io.realm.internal;

public class LinkView implements NativeObject {
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
    final long columnIndexInParent;
    private final NativeContext context;
    private final long nativePtr;
    final Table parent;

    public static native void nativeAdd(long j, long j2);

    public static native void nativeClear(long j);

    private native long nativeFind(long j, long j2);

    private static native long nativeGetFinalizerPtr();

    private native long nativeGetTargetRowIndex(long j, long j2);

    private native long nativeGetTargetTable(long j);

    private native void nativeInsert(long j, long j2, long j3);

    private native boolean nativeIsAttached(long j);

    private native boolean nativeIsEmpty(long j);

    private native void nativeMove(long j, long j2, long j3);

    private native void nativeRemove(long j, long j2);

    private native void nativeRemoveAllTargetRows(long j);

    private native void nativeRemoveTargetRow(long j, long j2);

    private native void nativeSet(long j, long j2, long j3);

    private native long nativeSize(long j);

    native long nativeGetRow(long j, long j2);

    protected native long nativeWhere(long j);

    public LinkView(NativeContext context, Table parent, long columnIndexInParent, long nativeLinkViewPtr) {
        this.context = context;
        this.parent = parent;
        this.columnIndexInParent = columnIndexInParent;
        this.nativePtr = nativeLinkViewPtr;
        context.addReference(this);
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }

    public UncheckedRow getUncheckedRow(long index) {
        return UncheckedRow.getByRowIndex(this.context, this, index);
    }

    public CheckedRow getCheckedRow(long index) {
        return CheckedRow.get(this.context, this, index);
    }

    public long getTargetRowIndex(long linkViewIndex) {
        return nativeGetTargetRowIndex(this.nativePtr, linkViewIndex);
    }

    public void add(long rowIndex) {
        checkImmutable();
        nativeAdd(this.nativePtr, rowIndex);
    }

    public void insert(long pos, long rowIndex) {
        checkImmutable();
        nativeInsert(this.nativePtr, pos, rowIndex);
    }

    public void set(long pos, long rowIndex) {
        checkImmutable();
        nativeSet(this.nativePtr, pos, rowIndex);
    }

    public void move(long oldPos, long newPos) {
        checkImmutable();
        nativeMove(this.nativePtr, oldPos, newPos);
    }

    public void remove(long pos) {
        checkImmutable();
        nativeRemove(this.nativePtr, pos);
    }

    public void clear() {
        checkImmutable();
        nativeClear(this.nativePtr);
    }

    public boolean contains(long tableRowIndex) {
        return nativeFind(this.nativePtr, tableRowIndex) != -1;
    }

    public long size() {
        return nativeSize(this.nativePtr);
    }

    public boolean isEmpty() {
        return nativeIsEmpty(this.nativePtr);
    }

    public TableQuery where() {
        return new TableQuery(this.context, getTargetTable(), nativeWhere(this.nativePtr));
    }

    public boolean isAttached() {
        return nativeIsAttached(this.nativePtr);
    }

    public void removeAllTargetRows() {
        checkImmutable();
        nativeRemoveAllTargetRows(this.nativePtr);
    }

    public void removeTargetRow(int index) {
        checkImmutable();
        nativeRemoveTargetRow(this.nativePtr, (long) index);
    }

    public Table getTargetTable() {
        return new Table(this.parent, nativeGetTargetTable(this.nativePtr));
    }

    private void checkImmutable() {
        if (this.parent.isImmutable()) {
            throw new IllegalStateException("Changing Realm data can only be done from inside a write transaction.");
        }
    }
}
