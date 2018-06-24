package io.realm.internal.fields;

import io.realm.RealmFieldType;
import io.realm.internal.ColumnInfo;
import io.realm.internal.fields.FieldDescriptor.SchemaProxy;
import java.util.List;
import java.util.Locale;
import java.util.Set;

class CachedFieldDescriptor extends FieldDescriptor {
    private final String className;
    private final SchemaProxy schema;

    CachedFieldDescriptor(SchemaProxy schema, String className, String fieldDescription, Set<RealmFieldType> validInternalColumnTypes, Set<RealmFieldType> validFinalColumnTypes) {
        super(fieldDescription, validInternalColumnTypes, validFinalColumnTypes);
        this.className = className;
        this.schema = schema;
    }

    protected void compileFieldDescription(List<String> fields) {
        int nFields = fields.size();
        long[] columnIndices = new long[nFields];
        long[] tableNativePointers = new long[nFields];
        String currentTable = this.className;
        String columnName = null;
        RealmFieldType columnType = null;
        for (int i = 0; i < nFields; i++) {
            columnName = (String) fields.get(i);
            if (columnName == null || columnName.length() <= 0) {
                throw new IllegalArgumentException("Invalid query: Field descriptor contains an empty field.  A field description may not begin with or contain adjacent periods ('.').");
            }
            ColumnInfo tableInfo = this.schema.getColumnInfo(currentTable);
            if (tableInfo == null) {
                throw new IllegalArgumentException(String.format(Locale.US, "Invalid query: table '%s' not found in this schema.", new Object[]{currentTable}));
            }
            long columnIndex = tableInfo.getColumnIndex(columnName);
            if (columnIndex < 0) {
                throw new IllegalArgumentException(String.format(Locale.US, "Invalid query: field '%s' not found in table '%s'.", new Object[]{columnName, currentTable}));
            }
            long j;
            columnType = tableInfo.getColumnType(columnName);
            if (i < nFields - 1) {
                verifyInternalColumnType(currentTable, columnName, columnType);
            }
            currentTable = tableInfo.getLinkedTable(columnName);
            columnIndices[i] = columnIndex;
            if (columnType != RealmFieldType.LINKING_OBJECTS) {
                j = 0;
            } else {
                j = this.schema.getNativeTablePtr(currentTable);
            }
            tableNativePointers[i] = j;
        }
        setCompilationResults(this.className, columnName, columnType, columnIndices, tableNativePointers);
    }
}
