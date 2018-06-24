package io.realm;

import android.content.Context;
import android.os.Looper;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.CheckedRow;
import io.realm.internal.ColumnInfo;
import io.realm.internal.InvalidRow;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.SharedRealm.SchemaVersionListener;
import io.realm.internal.Table;
import io.realm.internal.UncheckedRow;
import io.realm.internal.Util;
import io.realm.internal.async.RealmThreadPoolExecutor;
import io.realm.log.RealmLog;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;

abstract class BaseRealm implements Closeable {
    private static final String CLOSED_REALM_MESSAGE = "This Realm instance has already been closed, making it unusable.";
    private static final String INCORRECT_THREAD_CLOSE_MESSAGE = "Realm access from incorrect thread. Realm instance can only be closed on the thread it was created.";
    private static final String INCORRECT_THREAD_MESSAGE = "Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created.";
    static final String LISTENER_NOT_ALLOWED_MESSAGE = "Listeners cannot be used on current thread.";
    private static final String NOT_IN_TRANSACTION_MESSAGE = "Changing Realm data can only be done from inside a transaction.";
    protected static final long UNVERSIONED = -1;
    static volatile Context applicationContext;
    static final RealmThreadPoolExecutor asyncTaskExecutor = RealmThreadPoolExecutor.newDefaultExecutor();
    public static final ThreadLocalRealmObjectContext objectContext = new ThreadLocalRealmObjectContext();
    protected final RealmConfiguration configuration;
    private RealmCache realmCache;
    protected final RealmSchema schema;
    protected SharedRealm sharedRealm;
    final long threadId;

    class C14751 implements SchemaVersionListener {
        C14751() {
        }

        public void onSchemaVersionChanged(long currentVersion) {
            if (BaseRealm.this.realmCache != null) {
                BaseRealm.this.realmCache.updateSchemaCache((Realm) BaseRealm.this);
            }
        }
    }

    class C14762 implements Callback0 {
        C14762() {
        }

        public void onCall() {
            if (BaseRealm.this.sharedRealm == null || BaseRealm.this.sharedRealm.isClosed()) {
                throw new IllegalStateException(BaseRealm.CLOSED_REALM_MESSAGE);
            }
            BaseRealm.this.sharedRealm.stopWaitForChange();
        }
    }

    class C14773 implements Callback {
        final /* synthetic */ RealmConfiguration val$configuration;
        final /* synthetic */ AtomicBoolean val$realmDeleted;

        C14773(RealmConfiguration realmConfiguration, AtomicBoolean atomicBoolean) {
            this.val$configuration = realmConfiguration;
            this.val$realmDeleted = atomicBoolean;
        }

        public void onResult(int count) {
            if (count != 0) {
                throw new IllegalStateException("It's not allowed to delete the file associated with an open Realm. Remember to close() all the instances of the Realm before deleting its file: " + this.val$configuration.getPath());
            }
            this.val$realmDeleted.set(Util.deleteRealm(this.val$configuration.getPath(), this.val$configuration.getRealmDirectory(), this.val$configuration.getRealmFileName()));
        }
    }

    class C14784 implements Callback {
        final /* synthetic */ MigrationCallback val$callback;
        final /* synthetic */ RealmConfiguration val$configuration;
        final /* synthetic */ AtomicBoolean val$fileNotFound;
        final /* synthetic */ RealmMigration val$migration;

        C14784(RealmConfiguration realmConfiguration, AtomicBoolean atomicBoolean, RealmMigration realmMigration, MigrationCallback migrationCallback) {
            this.val$configuration = realmConfiguration;
            this.val$fileNotFound = atomicBoolean;
            this.val$migration = realmMigration;
            this.val$callback = migrationCallback;
        }

        public void onResult(int count) {
            if (count != 0) {
                throw new IllegalStateException("Cannot migrate a Realm file that is already open: " + this.val$configuration.getPath());
            } else if (new File(this.val$configuration.getPath()).exists()) {
                RealmMigration realmMigration = this.val$migration == null ? this.val$configuration.getMigration() : this.val$migration;
                DynamicRealm realm = null;
                try {
                    realm = DynamicRealm.createInstance(this.val$configuration);
                    realm.beginTransaction();
                    realmMigration.migrate(realm, realm.getVersion(), this.val$configuration.getSchemaVersion());
                    realm.setVersion(this.val$configuration.getSchemaVersion());
                    realm.commitTransaction();
                    if (realm != null) {
                        realm.close();
                        this.val$callback.migrationComplete();
                    }
                } catch (RuntimeException e) {
                    if (realm != null) {
                        realm.cancelTransaction();
                    }
                    throw e;
                } catch (Throwable th) {
                    if (realm != null) {
                        realm.close();
                        this.val$callback.migrationComplete();
                    }
                }
            } else {
                this.val$fileNotFound.set(true);
            }
        }
    }

    public static abstract class InstanceCallback<T extends BaseRealm> {
        public abstract void onSuccess(T t);

        public void onError(Throwable exception) {
            throw new RealmException("Exception happens when initializing Realm in the background thread.", exception);
        }
    }

    protected interface MigrationCallback {
        void migrationComplete();
    }

    public static final class RealmObjectContext {
        private boolean acceptDefaultValue;
        private ColumnInfo columnInfo;
        private List<String> excludeFields;
        private BaseRealm realm;
        private Row row;

        public void set(BaseRealm realm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
            this.realm = realm;
            this.row = row;
            this.columnInfo = columnInfo;
            this.acceptDefaultValue = acceptDefaultValue;
            this.excludeFields = excludeFields;
        }

        BaseRealm getRealm() {
            return this.realm;
        }

        public Row getRow() {
            return this.row;
        }

        public ColumnInfo getColumnInfo() {
            return this.columnInfo;
        }

        public boolean getAcceptDefaultValue() {
            return this.acceptDefaultValue;
        }

        public List<String> getExcludeFields() {
            return this.excludeFields;
        }

        public void clear() {
            this.realm = null;
            this.row = null;
            this.columnInfo = null;
            this.acceptDefaultValue = false;
            this.excludeFields = null;
        }
    }

    static final class ThreadLocalRealmObjectContext extends ThreadLocal<RealmObjectContext> {
        ThreadLocalRealmObjectContext() {
        }

        protected RealmObjectContext initialValue() {
            return new RealmObjectContext();
        }
    }

    public abstract Observable asObservable();

    BaseRealm(RealmCache cache) {
        this(cache.getConfiguration());
        this.realmCache = cache;
    }

    BaseRealm(RealmConfiguration configuration) {
        SchemaVersionListener schemaVersionListener = null;
        this.threadId = Thread.currentThread().getId();
        this.configuration = configuration;
        this.realmCache = null;
        if (this instanceof Realm) {
            schemaVersionListener = new C14751();
        }
        this.sharedRealm = SharedRealm.getInstance(configuration, schemaVersionListener, true);
        this.schema = new RealmSchema(this);
    }

    public void setAutoRefresh(boolean autoRefresh) {
        checkIfValid();
        this.sharedRealm.setAutoRefresh(autoRefresh);
    }

    public boolean isAutoRefresh() {
        return this.sharedRealm.isAutoRefresh();
    }

    public void refresh() {
        checkIfValid();
        if (isInTransaction()) {
            throw new IllegalStateException("Cannot refresh a Realm instance inside a transaction.");
        }
        this.sharedRealm.refresh();
    }

    public boolean isInTransaction() {
        checkIfValid();
        return this.sharedRealm.isInTransaction();
    }

    protected <T extends BaseRealm> void addListener(RealmChangeListener<T> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener should not be null");
        }
        checkIfValid();
        this.sharedRealm.capabilities.checkCanDeliverNotification(LISTENER_NOT_ALLOWED_MESSAGE);
        this.sharedRealm.realmNotifier.addChangeListener(this, listener);
    }

    protected <T extends BaseRealm> void removeListener(RealmChangeListener<T> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener should not be null");
        }
        checkIfValid();
        this.sharedRealm.capabilities.checkCanDeliverNotification(LISTENER_NOT_ALLOWED_MESSAGE);
        this.sharedRealm.realmNotifier.removeChangeListener(this, listener);
    }

    protected void removeAllListeners() {
        checkIfValid();
        this.sharedRealm.capabilities.checkCanDeliverNotification("removeListener cannot be called on current thread.");
        this.sharedRealm.realmNotifier.removeChangeListeners(this);
    }

    public void writeCopyTo(File destination) {
        writeEncryptedCopyTo(destination, null);
    }

    public void writeEncryptedCopyTo(File destination, byte[] key) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination argument cannot be null");
        }
        checkIfValid();
        this.sharedRealm.writeCopy(destination, key);
    }

    public boolean waitForChange() {
        checkIfValid();
        if (isInTransaction()) {
            throw new IllegalStateException("Cannot wait for changes inside of a transaction.");
        } else if (Looper.myLooper() != null) {
            throw new IllegalStateException("Cannot wait for changes inside a Looper thread. Use RealmChangeListeners instead.");
        } else {
            boolean hasChanged = this.sharedRealm.waitForChange();
            if (hasChanged) {
                this.sharedRealm.refresh();
            }
            return hasChanged;
        }
    }

    public void stopWaitForChange() {
        if (this.realmCache != null) {
            this.realmCache.invokeWithLock(new C14762());
            return;
        }
        throw new IllegalStateException(CLOSED_REALM_MESSAGE);
    }

    public void beginTransaction() {
        beginTransaction(false);
    }

    void beginTransaction(boolean ignoreReadOnly) {
        checkIfValid();
        this.sharedRealm.beginTransaction(ignoreReadOnly);
    }

    public void commitTransaction() {
        checkIfValid();
        this.sharedRealm.commitTransaction();
    }

    public void cancelTransaction() {
        checkIfValid();
        this.sharedRealm.cancelTransaction();
    }

    protected void checkIfValid() {
        if (this.sharedRealm == null || this.sharedRealm.isClosed()) {
            throw new IllegalStateException(CLOSED_REALM_MESSAGE);
        } else if (this.threadId != Thread.currentThread().getId()) {
            throw new IllegalStateException(INCORRECT_THREAD_MESSAGE);
        }
    }

    protected void checkIfInTransaction() {
        if (!this.sharedRealm.isInTransaction()) {
            throw new IllegalStateException(NOT_IN_TRANSACTION_MESSAGE);
        }
    }

    protected void checkIfValidAndInTransaction() {
        if (!isInTransaction()) {
            throw new IllegalStateException(NOT_IN_TRANSACTION_MESSAGE);
        }
    }

    void checkNotInSync() {
        if (this.configuration.isSyncConfiguration()) {
            throw new IllegalArgumentException("You cannot perform changes to a schema. Please update app and restart.");
        }
    }

    public String getPath() {
        return this.configuration.getPath();
    }

    public RealmConfiguration getConfiguration() {
        return this.configuration;
    }

    public long getVersion() {
        return this.sharedRealm.getSchemaVersion();
    }

    public void close() {
        if (this.threadId != Thread.currentThread().getId()) {
            throw new IllegalStateException(INCORRECT_THREAD_CLOSE_MESSAGE);
        } else if (this.realmCache != null) {
            this.realmCache.release(this);
        } else {
            doClose();
        }
    }

    void doClose() {
        this.realmCache = null;
        if (this.sharedRealm != null) {
            this.sharedRealm.close();
            this.sharedRealm = null;
        }
        if (this.schema != null) {
            this.schema.close();
        }
    }

    public boolean isClosed() {
        if (this.threadId == Thread.currentThread().getId()) {
            return this.sharedRealm == null || this.sharedRealm.isClosed();
        } else {
            throw new IllegalStateException(INCORRECT_THREAD_MESSAGE);
        }
    }

    public boolean isEmpty() {
        checkIfValid();
        return this.sharedRealm.isEmpty();
    }

    void setVersion(long version) {
        this.sharedRealm.setSchemaVersion(version);
    }

    public RealmSchema getSchema() {
        return this.schema;
    }

    <E extends RealmModel> E get(Class<E> clazz, String dynamicClassName, UncheckedRow row) {
        boolean isDynamicRealmObject;
        if (dynamicClassName != null) {
            isDynamicRealmObject = true;
        } else {
            isDynamicRealmObject = false;
        }
        if (isDynamicRealmObject) {
            return new DynamicRealmObject(this, CheckedRow.getFromRow(row));
        }
        return this.configuration.getSchemaMediator().newInstance(clazz, this, row, this.schema.getColumnInfo((Class) clazz), false, Collections.emptyList());
    }

    <E extends RealmModel> E get(Class<E> clazz, long rowIndex, boolean acceptDefaultValue, List<String> excludeFields) {
        UncheckedRow row = this.schema.getTable((Class) clazz).getUncheckedRow(rowIndex);
        return this.configuration.getSchemaMediator().newInstance(clazz, this, row, this.schema.getColumnInfo((Class) clazz), acceptDefaultValue, excludeFields);
    }

    <E extends RealmModel> E get(Class<E> clazz, String dynamicClassName, long rowIndex) {
        boolean isDynamicRealmObject;
        if (dynamicClassName != null) {
            isDynamicRealmObject = true;
        } else {
            isDynamicRealmObject = false;
        }
        Table table = isDynamicRealmObject ? this.schema.getTable(dynamicClassName) : this.schema.getTable((Class) clazz);
        if (isDynamicRealmObject) {
            return new DynamicRealmObject(this, rowIndex != -1 ? table.getCheckedRow(rowIndex) : InvalidRow.INSTANCE);
        }
        return this.configuration.getSchemaMediator().newInstance(clazz, this, rowIndex != -1 ? table.getUncheckedRow(rowIndex) : InvalidRow.INSTANCE, this.schema.getColumnInfo((Class) clazz), false, Collections.emptyList());
    }

    public void deleteAll() {
        checkIfValid();
        for (RealmObjectSchema objectSchema : this.schema.getAll()) {
            this.schema.getTable(objectSchema.getClassName()).clear();
        }
    }

    static boolean deleteRealm(RealmConfiguration configuration) {
        AtomicBoolean realmDeleted = new AtomicBoolean(true);
        RealmCache.invokeWithGlobalRefCount(configuration, new C14773(configuration, realmDeleted));
        return realmDeleted.get();
    }

    static boolean compactRealm(RealmConfiguration configuration) {
        SharedRealm sharedRealm = SharedRealm.getInstance(configuration);
        Boolean result = Boolean.valueOf(sharedRealm.compact());
        sharedRealm.close();
        return result.booleanValue();
    }

    protected static void migrateRealm(RealmConfiguration configuration, RealmMigration migration, MigrationCallback callback, RealmMigrationNeededException cause) throws FileNotFoundException {
        if (configuration == null) {
            throw new IllegalArgumentException("RealmConfiguration must be provided");
        } else if (configuration.isSyncConfiguration()) {
            throw new IllegalArgumentException("Manual migrations are not supported for synced Realms");
        } else if (migration == null && configuration.getMigration() == null) {
            throw new RealmMigrationNeededException(configuration.getPath(), "RealmMigration must be provided", cause);
        } else {
            AtomicBoolean fileNotFound = new AtomicBoolean(false);
            RealmCache.invokeWithGlobalRefCount(configuration, new C14784(configuration, fileNotFound, migration, callback));
            if (fileNotFound.get()) {
                throw new FileNotFoundException("Cannot migrate a Realm file which doesn't exist: " + configuration.getPath());
            }
        }
    }

    protected void finalize() throws Throwable {
        if (!(this.sharedRealm == null || this.sharedRealm.isClosed())) {
            RealmLog.warn("Remember to call close() on all Realm instances. Realm %s is being finalized without being closed, this can lead to running out of native memory.", this.configuration.getPath());
            if (this.realmCache != null) {
                this.realmCache.leak();
            }
        }
        super.finalize();
    }

    SharedRealm getSharedRealm() {
        return this.sharedRealm;
    }
}
