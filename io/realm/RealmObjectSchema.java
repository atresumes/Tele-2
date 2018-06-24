package io.realm;

import io.realm.internal.ColumnInfo;
import io.realm.internal.Table;
import io.realm.internal.fields.FieldDescriptor;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class RealmObjectSchema {
    private static final Map<Class<?>, FieldMetaData> SUPPORTED_LINKED_FIELDS;
    private static final Map<Class<?>, FieldMetaData> SUPPORTED_SIMPLE_FIELDS;
    private final ColumnInfo columnInfo;
    private final BaseRealm realm;
    private final RealmSchema schema;
    private final Table table;

    private static final class DynamicColumnIndices extends ColumnInfo {
        private final Table table;

        DynamicColumnIndices(Table table) {
            super(null, false);
            this.table = table;
        }

        public long getColumnIndex(String columnName) {
            return this.table.getColumnIndex(columnName);
        }

        public RealmFieldType getColumnType(String columnName) {
            throw new UnsupportedOperationException("DynamicColumnIndices do not support 'getColumnType'");
        }

        public String getLinkedTable(String columnName) {
            throw new UnsupportedOperationException("DynamicColumnIndices do not support 'getLinkedTable'");
        }

        public void copyFrom(ColumnInfo src) {
            throw new UnsupportedOperationException("DynamicColumnIndices cannot be copied");
        }

        protected ColumnInfo copy(boolean immutable) {
            throw new UnsupportedOperationException("DynamicColumnIndices cannot be copied");
        }

        protected void copy(ColumnInfo src, ColumnInfo dst) {
            throw new UnsupportedOperationException("DynamicColumnIndices cannot copy");
        }
    }

    private static final class FieldMetaData {
        final boolean defaultNullable;
        final RealmFieldType realmType;

        FieldMetaData(RealmFieldType realmType, boolean defaultNullable) {
            this.realmType = realmType;
            this.defaultNullable = defaultNullable;
        }
    }

    public interface Function {
        void apply(DynamicRealmObject dynamicRealmObject);
    }

    static {
        Map<Class<?>, FieldMetaData> m = new HashMap();
        m.put(String.class, new FieldMetaData(RealmFieldType.STRING, true));
        m.put(Short.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        m.put(Short.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        m.put(Integer.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        m.put(Integer.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        m.put(Long.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        m.put(Long.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        m.put(Float.TYPE, new FieldMetaData(RealmFieldType.FLOAT, false));
        m.put(Float.class, new FieldMetaData(RealmFieldType.FLOAT, true));
        m.put(Double.TYPE, new FieldMetaData(RealmFieldType.DOUBLE, false));
        m.put(Double.class, new FieldMetaData(RealmFieldType.DOUBLE, true));
        m.put(Boolean.TYPE, new FieldMetaData(RealmFieldType.BOOLEAN, false));
        m.put(Boolean.class, new FieldMetaData(RealmFieldType.BOOLEAN, true));
        m.put(Byte.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        m.put(Byte.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        m.put(byte[].class, new FieldMetaData(RealmFieldType.BINARY, true));
        m.put(Date.class, new FieldMetaData(RealmFieldType.DATE, true));
        SUPPORTED_SIMPLE_FIELDS = Collections.unmodifiableMap(m);
        m = new HashMap();
        m.put(RealmObject.class, new FieldMetaData(RealmFieldType.OBJECT, false));
        m.put(RealmList.class, new FieldMetaData(RealmFieldType.LIST, false));
        SUPPORTED_LINKED_FIELDS = Collections.unmodifiableMap(m);
    }

    RealmObjectSchema(BaseRealm realm, RealmSchema schema, Table table) {
        this(realm, schema, table, new DynamicColumnIndices(table));
    }

    RealmObjectSchema(BaseRealm realm, RealmSchema schema, Table table, ColumnInfo columnInfo) {
        this.schema = schema;
        this.realm = realm;
        this.table = table;
        this.columnInfo = columnInfo;
    }

    @Deprecated
    public void close() {
    }

    public String getClassName() {
        return this.table.getClassName();
    }

    public RealmObjectSchema setClassName(String className) {
        this.realm.checkNotInSync();
        checkEmpty(className);
        String internalTableName = Table.getTableNameForClass(className);
        if (internalTableName.length() > 56) {
            throw new IllegalArgumentException("Class name is too long. Limit is 56 characters: '" + className + "' (" + Integer.toString(className.length()) + ")");
        } else if (this.realm.sharedRealm.hasTable(internalTableName)) {
            throw new IllegalArgumentException("Class already exists: " + className);
        } else {
            String oldTableName = null;
            String pkField = null;
            if (this.table.hasPrimaryKey()) {
                oldTableName = this.table.getName();
                pkField = getPrimaryKey();
                this.table.setPrimaryKey(null);
            }
            this.realm.sharedRealm.renameTable(this.table.getName(), internalTableName);
            if (!(pkField == null || pkField.isEmpty())) {
                try {
                    this.table.setPrimaryKey(pkField);
                } catch (Exception e) {
                    this.realm.sharedRealm.renameTable(this.table.getName(), oldTableName);
                    throw e;
                }
            }
            return this;
        }
    }

    public RealmObjectSchema addField(String fieldName, Class<?> fieldType, FieldAttribute... attributes) {
        FieldMetaData metadata = (FieldMetaData) SUPPORTED_SIMPLE_FIELDS.get(fieldType);
        if (metadata != null) {
            checkNewFieldName(fieldName);
            boolean nullable = metadata.defaultNullable;
            if (containsAttribute(attributes, FieldAttribute.REQUIRED)) {
                nullable = false;
            }
            long columnIndex = this.table.addColumn(metadata.realmType, fieldName, nullable);
            try {
                addModifiers(fieldName, attributes);
                return this;
            } catch (Exception e) {
                this.table.removeColumn(columnIndex);
                throw e;
            }
        } else if (SUPPORTED_LINKED_FIELDS.containsKey(fieldType)) {
            throw new IllegalArgumentException("Use addRealmObjectField() instead to add fields that link to other RealmObjects: " + fieldName);
        } else {
            throw new IllegalArgumentException(String.format(Locale.US, "Realm doesn't support this field type: %s(%s)", new Object[]{fieldName, fieldType}));
        }
    }

    public RealmObjectSchema addRealmObjectField(String fieldName, RealmObjectSchema objectSchema) {
        checkLegalName(fieldName);
        checkFieldNameIsAvailable(fieldName);
        this.table.addColumnLink(RealmFieldType.OBJECT, fieldName, this.realm.sharedRealm.getTable(Table.getTableNameForClass(objectSchema.getClassName())));
        return this;
    }

    public RealmObjectSchema addRealmListField(String fieldName, RealmObjectSchema objectSchema) {
        checkLegalName(fieldName);
        checkFieldNameIsAvailable(fieldName);
        this.table.addColumnLink(RealmFieldType.LIST, fieldName, this.realm.sharedRealm.getTable(Table.getTableNameForClass(objectSchema.getClassName())));
        return this;
    }

    public RealmObjectSchema removeField(String fieldName) {
        this.realm.checkNotInSync();
        checkLegalName(fieldName);
        if (hasField(fieldName)) {
            long columnIndex = getColumnIndex(fieldName);
            if (this.table.getPrimaryKey() == columnIndex) {
                this.table.setPrimaryKey(null);
            }
            this.table.removeColumn(columnIndex);
            return this;
        }
        throw new IllegalStateException(fieldName + " does not exist.");
    }

    public RealmObjectSchema renameField(String currentFieldName, String newFieldName) {
        this.realm.checkNotInSync();
        checkLegalName(currentFieldName);
        checkFieldExists(currentFieldName);
        checkLegalName(newFieldName);
        checkFieldNameIsAvailable(newFieldName);
        this.table.renameColumn(getColumnIndex(currentFieldName), newFieldName);
        return this;
    }

    public boolean hasField(String fieldName) {
        return this.table.getColumnIndex(fieldName) != -1;
    }

    public RealmObjectSchema addIndex(String fieldName) {
        checkLegalName(fieldName);
        checkFieldExists(fieldName);
        long columnIndex = getColumnIndex(fieldName);
        if (this.table.hasSearchIndex(columnIndex)) {
            throw new IllegalStateException(fieldName + " already has an index.");
        }
        this.table.addSearchIndex(columnIndex);
        return this;
    }

    public boolean hasIndex(String fieldName) {
        checkLegalName(fieldName);
        checkFieldExists(fieldName);
        return this.table.hasSearchIndex(this.table.getColumnIndex(fieldName));
    }

    public RealmObjectSchema removeIndex(String fieldName) {
        this.realm.checkNotInSync();
        checkLegalName(fieldName);
        checkFieldExists(fieldName);
        long columnIndex = getColumnIndex(fieldName);
        if (this.table.hasSearchIndex(columnIndex)) {
            this.table.removeSearchIndex(columnIndex);
            return this;
        }
        throw new IllegalStateException("Field is not indexed: " + fieldName);
    }

    public RealmObjectSchema addPrimaryKey(String fieldName) {
        checkLegalName(fieldName);
        checkFieldExists(fieldName);
        if (this.table.hasPrimaryKey()) {
            throw new IllegalStateException("A primary key is already defined");
        }
        this.table.setPrimaryKey(fieldName);
        long columnIndex = getColumnIndex(fieldName);
        if (!this.table.hasSearchIndex(columnIndex)) {
            this.table.addSearchIndex(columnIndex);
        }
        return this;
    }

    public RealmObjectSchema removePrimaryKey() {
        this.realm.checkNotInSync();
        if (this.table.hasPrimaryKey()) {
            long columnIndex = this.table.getPrimaryKey();
            if (this.table.hasSearchIndex(columnIndex)) {
                this.table.removeSearchIndex(columnIndex);
            }
            this.table.setPrimaryKey("");
            return this;
        }
        throw new IllegalStateException(getClassName() + " doesn't have a primary key.");
    }

    public RealmObjectSchema setRequired(String fieldName, boolean required) {
        long columnIndex = this.table.getColumnIndex(fieldName);
        boolean currentColumnRequired = isRequired(fieldName);
        RealmFieldType type = this.table.getColumnType(columnIndex);
        if (type == RealmFieldType.OBJECT) {
            throw new IllegalArgumentException("Cannot modify the required state for RealmObject references: " + fieldName);
        } else if (type == RealmFieldType.LIST) {
            throw new IllegalArgumentException("Cannot modify the required state for RealmList references: " + fieldName);
        } else if (required && currentColumnRequired) {
            throw new IllegalStateException("Field is already required: " + fieldName);
        } else if (required || currentColumnRequired) {
            if (required) {
                this.table.convertColumnToNotNullable(columnIndex);
            } else {
                this.table.convertColumnToNullable(columnIndex);
            }
            return this;
        } else {
            throw new IllegalStateException("Field is already nullable: " + fieldName);
        }
    }

    public RealmObjectSchema setNullable(String fieldName, boolean nullable) {
        setRequired(fieldName, !nullable);
        return this;
    }

    public boolean isRequired(String fieldName) {
        return !this.table.isColumnNullable(getColumnIndex(fieldName));
    }

    public boolean isNullable(String fieldName) {
        return this.table.isColumnNullable(getColumnIndex(fieldName));
    }

    public boolean isPrimaryKey(String fieldName) {
        return getColumnIndex(fieldName) == this.table.getPrimaryKey();
    }

    public boolean hasPrimaryKey() {
        return this.table.hasPrimaryKey();
    }

    public String getPrimaryKey() {
        if (this.table.hasPrimaryKey()) {
            return this.table.getColumnName(this.table.getPrimaryKey());
        }
        throw new IllegalStateException(getClassName() + " doesn't have a primary key.");
    }

    public Set<String> getFieldNames() {
        int columnCount = (int) this.table.getColumnCount();
        Set<String> columnNames = new LinkedHashSet(columnCount);
        for (int i = 0; i < columnCount; i++) {
            columnNames.add(this.table.getColumnName((long) i));
        }
        return columnNames;
    }

    public RealmObjectSchema transform(Function function) {
        if (function != null) {
            long size = this.table.size();
            for (long i = 0; i < size; i++) {
                function.apply(new DynamicRealmObject(this.realm, this.table.getCheckedRow(i)));
            }
        }
        return this;
    }

    public RealmFieldType getFieldType(String fieldName) {
        return this.table.getColumnType(getColumnIndex(fieldName));
    }

    protected final FieldDescriptor getColumnIndices(String fieldDescription, RealmFieldType... validColumnTypes) {
        return FieldDescriptor.createStandardFieldDescriptor(getSchemaConnector(), getTable(), fieldDescription, validColumnTypes);
    }

    RealmObjectSchema add(String name, RealmFieldType type, boolean primary, boolean indexed, boolean required) {
        long columnIndex = this.table.addColumn(type, name, !required);
        if (indexed) {
            this.table.addSearchIndex(columnIndex);
        }
        if (primary) {
            this.table.setPrimaryKey(name);
        }
        return this;
    }

    RealmObjectSchema add(String name, RealmFieldType type, RealmObjectSchema linkedTo) {
        this.table.addColumnLink(type, name, this.realm.getSharedRealm().getTable(Table.getTableNameForClass(linkedTo.getClassName())));
        return this;
    }

    long getAndCheckFieldIndex(String fieldName) {
        long index = this.columnInfo.getColumnIndex(fieldName);
        if (index >= 0) {
            return index;
        }
        throw new IllegalArgumentException("Field does not exist: " + fieldName);
    }

    Table getTable() {
        return this.table;
    }

    private SchemaConnector getSchemaConnector() {
        return new SchemaConnector(this.schema);
    }

    long getFieldIndex(String fieldName) {
        return this.columnInfo.getColumnIndex(fieldName);
    }

    private void addModifiers(String fieldName, FieldAttribute[] attributes) {
        if (attributes != null) {
            try {
                if (attributes.length > 0) {
                    if (containsAttribute(attributes, FieldAttribute.INDEXED)) {
                        addIndex(fieldName);
                    }
                    if (containsAttribute(attributes, FieldAttribute.PRIMARY_KEY)) {
                        addPrimaryKey(fieldName);
                    }
                }
            } catch (Exception e) {
                long columnIndex = getColumnIndex(fieldName);
                if (false) {
                    this.table.removeSearchIndex(columnIndex);
                }
                throw ((RuntimeException) e);
            }
        }
    }

    private boolean containsAttribute(FieldAttribute[] attributeList, FieldAttribute attribute) {
        if (attributeList == null || attributeList.length == 0) {
            return false;
        }
        for (FieldAttribute anAttributeList : attributeList) {
            if (anAttributeList == attribute) {
                return true;
            }
        }
        return false;
    }

    private void checkNewFieldName(String fieldName) {
        checkLegalName(fieldName);
        checkFieldNameIsAvailable(fieldName);
    }

    private void checkLegalName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            throw new IllegalArgumentException("Field name can not be null or empty");
        } else if (fieldName.contains(".")) {
            throw new IllegalArgumentException("Field name can not contain '.'");
        }
    }

    private void checkFieldNameIsAvailable(String fieldName) {
        if (this.table.getColumnIndex(fieldName) != -1) {
            throw new IllegalArgumentException("Field already exists in '" + getClassName() + "': " + fieldName);
        }
    }

    private void checkFieldExists(String fieldName) {
        if (this.table.getColumnIndex(fieldName) == -1) {
            throw new IllegalArgumentException("Field name doesn't exist on object '" + getClassName() + "': " + fieldName);
        }
    }

    private long getColumnIndex(String fieldName) {
        long columnIndex = this.table.getColumnIndex(fieldName);
        if (columnIndex != -1) {
            return columnIndex;
        }
        throw new IllegalArgumentException(String.format(Locale.US, "Field name '%s' does not exist on schema for '%s'", new Object[]{fieldName, getClassName()}));
    }

    private void checkEmpty(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Null or empty class names are not allowed");
        }
    }
}
