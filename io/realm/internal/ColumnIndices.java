package io.realm.internal;

import io.realm.RealmModel;
import io.realm.internal.util.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class ColumnIndices {
    private final Map<Class<? extends RealmModel>, ColumnInfo> classes;
    private final Map<String, ColumnInfo> classesByName;
    private final Map<Pair<Class<? extends RealmModel>, String>, ColumnInfo> classesToColumnInfo;
    private final boolean mutable;
    private long schemaVersion;

    public ColumnIndices(long schemaVersion, Map<Pair<Class<? extends RealmModel>, String>, ColumnInfo> classesMap) {
        this(schemaVersion, new HashMap(classesMap), true);
        for (Entry<Pair<Class<? extends RealmModel>, String>, ColumnInfo> entry : classesMap.entrySet()) {
            ColumnInfo columnInfo = (ColumnInfo) entry.getValue();
            if (this.mutable != columnInfo.isMutable()) {
                throw new IllegalArgumentException("ColumnInfo mutability does not match ColumnIndices");
            }
            Pair<Class<? extends RealmModel>, String> classDescription = (Pair) entry.getKey();
            this.classes.put(classDescription.first, columnInfo);
            this.classesByName.put(classDescription.second, columnInfo);
        }
    }

    public ColumnIndices(ColumnIndices other, boolean mutable) {
        this(other.schemaVersion, new HashMap(other.classesToColumnInfo.size()), mutable);
        for (Entry<Pair<Class<? extends RealmModel>, String>, ColumnInfo> entry : other.classesToColumnInfo.entrySet()) {
            ColumnInfo columnInfo = ((ColumnInfo) entry.getValue()).copy(mutable);
            Pair<Class<? extends RealmModel>, String> key = (Pair) entry.getKey();
            this.classes.put(key.first, columnInfo);
            this.classesByName.put(key.second, columnInfo);
            this.classesToColumnInfo.put(key, columnInfo);
        }
    }

    private ColumnIndices(long schemaVersion, Map<Pair<Class<? extends RealmModel>, String>, ColumnInfo> classesMap, boolean mutable) {
        this.schemaVersion = schemaVersion;
        this.classesToColumnInfo = classesMap;
        this.mutable = mutable;
        this.classes = new HashMap(classesMap.size());
        this.classesByName = new HashMap(classesMap.size());
    }

    public long getSchemaVersion() {
        return this.schemaVersion;
    }

    public ColumnInfo getColumnInfo(Class<? extends RealmModel> clazz) {
        return (ColumnInfo) this.classes.get(clazz);
    }

    public ColumnInfo getColumnInfo(String className) {
        return (ColumnInfo) this.classesByName.get(className);
    }

    @Deprecated
    public long getColumnIndex(Class<? extends RealmModel> clazz, String fieldName) {
        ColumnInfo columnInfo = getColumnInfo((Class) clazz);
        if (columnInfo == null) {
            return -1;
        }
        return columnInfo.getColumnIndex(fieldName);
    }

    public void copyFrom(ColumnIndices src) {
        if (this.mutable) {
            for (Entry<String, ColumnInfo> entry : this.classesByName.entrySet()) {
                ColumnInfo otherColumnInfo = (ColumnInfo) src.classesByName.get(entry.getKey());
                if (otherColumnInfo == null) {
                    throw new IllegalStateException("Failed to copy ColumnIndices cache for class: " + ((String) entry.getKey()));
                }
                ((ColumnInfo) entry.getValue()).copyFrom(otherColumnInfo);
            }
            this.schemaVersion = src.schemaVersion;
            return;
        }
        throw new UnsupportedOperationException("Attempt to modify immutable cache");
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("ColumnIndices[");
        buf.append(this.schemaVersion).append(",");
        buf.append(this.mutable).append(",");
        if (this.classes != null) {
            boolean commaNeeded = false;
            for (Entry<String, ColumnInfo> entry : this.classesByName.entrySet()) {
                if (commaNeeded) {
                    buf.append(",");
                }
                buf.append((String) entry.getKey()).append("->").append(entry.getValue());
                commaNeeded = true;
            }
        }
        return buf.append("]").toString();
    }
}
