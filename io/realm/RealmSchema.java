package io.realm;

import io.realm.internal.ColumnIndices;
import io.realm.internal.ColumnInfo;
import io.realm.internal.Table;
import io.realm.internal.Util;
import io.realm.internal.util.Pair;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class RealmSchema {
    static final String EMPTY_STRING_MSG = "Null or empty class names are not allowed";
    private final Map<Class<? extends RealmModel>, RealmObjectSchema> classToSchema = new HashMap();
    private final Map<Class<? extends RealmModel>, Table> classToTable = new HashMap();
    private ColumnIndices columnIndices;
    private final Map<String, RealmObjectSchema> dynamicClassToSchema = new HashMap();
    private final Map<String, Table> dynamicClassToTable = new HashMap();
    private final BaseRealm realm;

    RealmSchema(BaseRealm realm) {
        this.realm = realm;
    }

    @Deprecated
    public void close() {
    }

    public RealmObjectSchema get(String className) {
        checkEmpty(className, EMPTY_STRING_MSG);
        String internalClassName = Table.getTableNameForClass(className);
        if (!this.realm.getSharedRealm().hasTable(internalClassName)) {
            return null;
        }
        return new RealmObjectSchema(this.realm, this, this.realm.getSharedRealm().getTable(internalClassName));
    }

    public Set<RealmObjectSchema> getAll() {
        int tableCount = (int) this.realm.getSharedRealm().size();
        Set<RealmObjectSchema> schemas = new LinkedHashSet(tableCount);
        for (int i = 0; i < tableCount; i++) {
            String tableName = this.realm.getSharedRealm().getTableName(i);
            if (Table.isModelTable(tableName)) {
                schemas.add(new RealmObjectSchema(this.realm, this, this.realm.getSharedRealm().getTable(tableName)));
            }
        }
        return schemas;
    }

    public RealmObjectSchema create(String className) {
        checkEmpty(className, EMPTY_STRING_MSG);
        String internalTableName = Table.getTableNameForClass(className);
        if (internalTableName.length() <= 56) {
            return new RealmObjectSchema(this.realm, this, this.realm.getSharedRealm().createTable(internalTableName));
        }
        throw new IllegalArgumentException("Class name is too long. Limit is 56 characters: " + className.length());
    }

    public void remove(String className) {
        this.realm.checkNotInSync();
        checkEmpty(className, EMPTY_STRING_MSG);
        String internalTableName = Table.getTableNameForClass(className);
        checkHasTable(className, "Cannot remove class because it is not in this Realm: " + className);
        Table table = getTable(className);
        if (table.hasPrimaryKey()) {
            table.setPrimaryKey(null);
        }
        this.realm.getSharedRealm().removeTable(internalTableName);
    }

    public RealmObjectSchema rename(String oldClassName, String newClassName) {
        this.realm.checkNotInSync();
        checkEmpty(oldClassName, "Class names cannot be empty or null");
        checkEmpty(newClassName, "Class names cannot be empty or null");
        String oldInternalName = Table.getTableNameForClass(oldClassName);
        String newInternalName = Table.getTableNameForClass(newClassName);
        checkHasTable(oldClassName, "Cannot rename class because it doesn't exist in this Realm: " + oldClassName);
        if (this.realm.getSharedRealm().hasTable(newInternalName)) {
            throw new IllegalArgumentException(oldClassName + " cannot be renamed because the new class already exists: " + newClassName);
        }
        Table oldTable = getTable(oldClassName);
        String pkField = null;
        if (oldTable.hasPrimaryKey()) {
            pkField = oldTable.getColumnName(oldTable.getPrimaryKey());
            oldTable.setPrimaryKey(null);
        }
        this.realm.getSharedRealm().renameTable(oldInternalName, newInternalName);
        Table table = this.realm.getSharedRealm().getTable(newInternalName);
        if (pkField != null) {
            table.setPrimaryKey(pkField);
        }
        return new RealmObjectSchema(this.realm, this, table);
    }

    public boolean contains(String className) {
        return this.realm.getSharedRealm().hasTable(Table.getTableNameForClass(className));
    }

    private void checkEmpty(String str, String error) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
    }

    private void checkHasTable(String className, String errorMsg) {
        if (!this.realm.getSharedRealm().hasTable(Table.getTableNameForClass(className))) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    Table getTable(String className) {
        String tableName = Table.getTableNameForClass(className);
        Table table = (Table) this.dynamicClassToTable.get(tableName);
        if (table != null) {
            return table;
        }
        table = this.realm.getSharedRealm().getTable(tableName);
        this.dynamicClassToTable.put(tableName, table);
        return table;
    }

    Table getTable(Class<? extends RealmModel> clazz) {
        Table table = (Table) this.classToTable.get(clazz);
        if (table != null) {
            return table;
        }
        Class<? extends RealmModel> originalClass = Util.getOriginalModelClass(clazz);
        if (isProxyClass(originalClass, clazz)) {
            table = (Table) this.classToTable.get(originalClass);
        }
        if (table == null) {
            table = this.realm.getSharedRealm().getTable(this.realm.getConfiguration().getSchemaMediator().getTableName(originalClass));
            this.classToTable.put(originalClass, table);
        }
        if (isProxyClass(originalClass, clazz)) {
            this.classToTable.put(clazz, table);
        }
        return table;
    }

    RealmObjectSchema getSchemaForClass(Class<? extends RealmModel> clazz) {
        RealmObjectSchema realmObjectSchema = (RealmObjectSchema) this.classToSchema.get(clazz);
        if (realmObjectSchema != null) {
            return realmObjectSchema;
        }
        Class originalClass = Util.getOriginalModelClass(clazz);
        if (isProxyClass(originalClass, clazz)) {
            realmObjectSchema = (RealmObjectSchema) this.classToSchema.get(originalClass);
        }
        if (realmObjectSchema == null) {
            realmObjectSchema = new RealmObjectSchema(this.realm, this, getTable((Class) clazz), getColumnInfo(originalClass));
            this.classToSchema.put(originalClass, realmObjectSchema);
        }
        if (isProxyClass(originalClass, clazz)) {
            this.classToSchema.put(clazz, realmObjectSchema);
        }
        return realmObjectSchema;
    }

    RealmObjectSchema getSchemaForClass(String className) {
        String tableName = Table.getTableNameForClass(className);
        RealmObjectSchema dynamicSchema = (RealmObjectSchema) this.dynamicClassToSchema.get(tableName);
        if (dynamicSchema != null) {
            return dynamicSchema;
        }
        if (this.realm.getSharedRealm().hasTable(tableName)) {
            dynamicSchema = new RealmObjectSchema(this.realm, this, this.realm.getSharedRealm().getTable(tableName));
            this.dynamicClassToSchema.put(tableName, dynamicSchema);
            return dynamicSchema;
        }
        throw new IllegalArgumentException("The class " + className + " doesn't exist in this Realm.");
    }

    final void setInitialColumnIndices(ColumnIndices columnIndices) {
        if (this.columnIndices != null) {
            throw new IllegalStateException("An instance of ColumnIndices is already set.");
        }
        this.columnIndices = new ColumnIndices(columnIndices, true);
    }

    final void setInitialColumnIndices(long version, Map<Pair<Class<? extends RealmModel>, String>, ColumnInfo> columnInfoMap) {
        if (this.columnIndices != null) {
            throw new IllegalStateException("An instance of ColumnIndices is already set.");
        }
        this.columnIndices = new ColumnIndices(version, (Map) columnInfoMap);
    }

    void updateColumnIndices(ColumnIndices schemaVersion) {
        this.columnIndices.copyFrom(schemaVersion);
    }

    final boolean isProxyClass(Class<? extends RealmModel> modelClass, Class<? extends RealmModel> testee) {
        return modelClass.equals(testee);
    }

    final ColumnIndices getImmutableColumnIndicies() {
        checkIndices();
        return new ColumnIndices(this.columnIndices, false);
    }

    final boolean haveColumnInfo() {
        return this.columnIndices != null;
    }

    final long getSchemaVersion() {
        checkIndices();
        return this.columnIndices.getSchemaVersion();
    }

    final ColumnInfo getColumnInfo(Class<? extends RealmModel> clazz) {
        checkIndices();
        return this.columnIndices.getColumnInfo((Class) clazz);
    }

    protected final ColumnInfo getColumnInfo(String className) {
        checkIndices();
        return this.columnIndices.getColumnInfo(className);
    }

    private void checkIndices() {
        if (!haveColumnInfo()) {
            throw new IllegalStateException("Attempt to use column index before set.");
        }
    }
}
