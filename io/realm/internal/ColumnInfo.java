package io.realm.internal;

import io.realm.RealmFieldType;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ColumnInfo {
    private final Map<String, ColumnDetails> indicesMap;
    private final boolean mutable;

    private static final class ColumnDetails {
        public final long columnIndex;
        public final RealmFieldType columnType;
        public final String linkTable;

        ColumnDetails(long columnIndex, RealmFieldType columnType, String srcTable) {
            this.columnIndex = columnIndex;
            this.columnType = columnType;
            this.linkTable = srcTable;
        }

        public String toString() {
            StringBuilder buf = new StringBuilder("ColumnDetails[");
            buf.append(this.columnIndex);
            buf.append(", ").append(this.columnType);
            buf.append(", ").append(this.linkTable);
            return buf.append("]").toString();
        }
    }

    protected abstract ColumnInfo copy(boolean z);

    protected abstract void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2);

    protected ColumnInfo(int mapSize) {
        this(mapSize, true);
    }

    protected ColumnInfo(ColumnInfo src, boolean mutable) {
        this(src == null ? 0 : src.indicesMap.size(), mutable);
        if (src != null) {
            this.indicesMap.putAll(src.indicesMap);
        }
    }

    private ColumnInfo(int mapSize, boolean mutable) {
        this.indicesMap = new HashMap(mapSize);
        this.mutable = mutable;
    }

    public final boolean isMutable() {
        return this.mutable;
    }

    public long getColumnIndex(String columnName) {
        ColumnDetails details = (ColumnDetails) this.indicesMap.get(columnName);
        return details == null ? -1 : details.columnIndex;
    }

    public RealmFieldType getColumnType(String columnName) {
        ColumnDetails details = (ColumnDetails) this.indicesMap.get(columnName);
        return details == null ? RealmFieldType.UNSUPPORTED_TABLE : details.columnType;
    }

    public String getLinkedTable(String columnName) {
        ColumnDetails details = (ColumnDetails) this.indicesMap.get(columnName);
        return details == null ? null : details.linkTable;
    }

    public void copyFrom(ColumnInfo src) {
        if (!this.mutable) {
            throw new UnsupportedOperationException("Attempt to modify an immutable ColumnInfo");
        } else if (src == null) {
            throw new NullPointerException("Attempt to copy null ColumnInfo");
        } else {
            this.indicesMap.clear();
            this.indicesMap.putAll(src.indicesMap);
            copy(src, this);
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("ColumnInfo[");
        buf.append(this.mutable).append(",");
        if (this.indicesMap != null) {
            boolean commaNeeded = false;
            for (Entry<String, ColumnDetails> entry : this.indicesMap.entrySet()) {
                if (commaNeeded) {
                    buf.append(",");
                }
                buf.append((String) entry.getKey()).append("->").append(entry.getValue());
                commaNeeded = true;
            }
        }
        return buf.append("]").toString();
    }

    protected final long addColumnDetails(Table table, String columnName, RealmFieldType columnType) {
        long columnIndex = table.getColumnIndex(columnName);
        if (columnIndex >= 0) {
            String linkedTableName;
            if (columnType == RealmFieldType.OBJECT || columnType == RealmFieldType.LIST) {
                linkedTableName = table.getLinkTarget(columnIndex).getClassName();
            } else {
                linkedTableName = null;
            }
            this.indicesMap.put(columnName, new ColumnDetails(columnIndex, columnType, linkedTableName));
        }
        return columnIndex;
    }

    protected final void addBacklinkDetails(SharedRealm realm, String columnName, String sourceTableName, String sourceColumnName) {
        this.indicesMap.put(columnName, new ColumnDetails(realm.getTable(Table.getTableNameForClass(sourceTableName)).getColumnIndex(sourceColumnName), RealmFieldType.LINKING_OBJECTS, sourceTableName));
    }

    public Map<String, ColumnDetails> getIndicesMap() {
        return this.indicesMap;
    }
}
