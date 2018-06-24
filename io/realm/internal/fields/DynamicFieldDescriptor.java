package io.realm.internal.fields;

import io.realm.RealmFieldType;
import io.realm.internal.Table;
import java.util.List;
import java.util.Locale;
import java.util.Set;

class DynamicFieldDescriptor extends FieldDescriptor {
    private final Table table;

    DynamicFieldDescriptor(Table table, String fieldDescription, Set<RealmFieldType> validInternalColumnTypes, Set<RealmFieldType> validFinalColumnTypes) {
        super(fieldDescription, validInternalColumnTypes, validFinalColumnTypes);
        this.table = table;
    }

    protected void compileFieldDescription(List<String> fields) {
        int nFields = fields.size();
        long[] columnIndices = new long[nFields];
        Table currentTable = this.table;
        String tableName = null;
        String columnName = null;
        RealmFieldType columnType = null;
        for (int i = 0; i < nFields; i++) {
            columnName = (String) fields.get(i);
            if (columnName == null || columnName.length() <= 0) {
                throw new IllegalArgumentException("Invalid query: Field descriptor contains an empty field.  A field description may not begin with or contain adjacent periods ('.').");
            }
            tableName = currentTable.getClassName();
            long columnIndex = currentTable.getColumnIndex(columnName);
            if (columnIndex < 0) {
                throw new IllegalArgumentException(String.format(Locale.US, "Invalid query: field '%s' not found in table '%s'.", new Object[]{columnName, tableName}));
            }
            columnType = currentTable.getColumnType(columnIndex);
            if (i < nFields - 1) {
                verifyInternalColumnType(tableName, columnName, columnType);
                currentTable = currentTable.getLinkTarget(columnIndex);
            }
            columnIndices[i] = columnIndex;
        }
        setCompilationResults(tableName, columnName, columnType, columnIndices, new long[nFields]);
    }
}
