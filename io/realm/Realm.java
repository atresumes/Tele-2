package io.realm;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.SystemClock;
import android.util.JsonReader;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.realm.BaseRealm.InstanceCallback;
import io.realm.RealmConfiguration.Builder;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmFileException;
import io.realm.exceptions.RealmFileException.Kind;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnIndices;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ObjectServerFacade;
import io.realm.internal.OsObject;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmCore;
import io.realm.internal.RealmNotifier;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmObjectProxy.CacheData;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.SharedRealm;
import io.realm.internal.SharedRealm.VersionID;
import io.realm.internal.Table;
import io.realm.internal.async.RealmAsyncTaskImpl;
import io.realm.internal.util.Pair;
import io.realm.log.RealmLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;

public class Realm extends BaseRealm {
    public static final String DEFAULT_REALM_NAME = "default.realm";
    private static final String NULL_CONFIG_MSG = "A non-null RealmConfiguration must be provided";
    private static RealmConfiguration defaultConfiguration;
    private static final Object defaultConfigurationLock = new Object();

    public interface Transaction {

        public static class Callback {
            public void onSuccess() {
            }

            public void onError(Exception ignore) {
            }
        }

        public interface OnError {
            void onError(Throwable th);
        }

        public interface OnSuccess {
            void onSuccess();
        }

        void execute(Realm realm);
    }

    class C14862 implements MigrationCallback {
        C14862() {
        }

        public void migrationComplete() {
        }
    }

    class C14873 implements MigrationCallback {
        C14873() {
        }

        public void migrationComplete() {
        }
    }

    class C14884 implements Callback {
        final /* synthetic */ AtomicInteger val$globalCount;

        C14884(AtomicInteger atomicInteger) {
            this.val$globalCount = atomicInteger;
        }

        public void onResult(int count) {
            this.val$globalCount.set(count);
        }
    }

    public static abstract class Callback extends InstanceCallback<Realm> {
        public abstract void onSuccess(Realm realm);

        public void onError(Throwable exception) {
            super.onError(exception);
        }
    }

    public /* bridge */ /* synthetic */ void beginTransaction() {
        super.beginTransaction();
    }

    public /* bridge */ /* synthetic */ void cancelTransaction() {
        super.cancelTransaction();
    }

    public /* bridge */ /* synthetic */ void close() {
        super.close();
    }

    public /* bridge */ /* synthetic */ void commitTransaction() {
        super.commitTransaction();
    }

    public /* bridge */ /* synthetic */ void deleteAll() {
        super.deleteAll();
    }

    public /* bridge */ /* synthetic */ RealmConfiguration getConfiguration() {
        return super.getConfiguration();
    }

    public /* bridge */ /* synthetic */ String getPath() {
        return super.getPath();
    }

    public /* bridge */ /* synthetic */ RealmSchema getSchema() {
        return super.getSchema();
    }

    public /* bridge */ /* synthetic */ long getVersion() {
        return super.getVersion();
    }

    public /* bridge */ /* synthetic */ boolean isAutoRefresh() {
        return super.isAutoRefresh();
    }

    public /* bridge */ /* synthetic */ boolean isClosed() {
        return super.isClosed();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ boolean isInTransaction() {
        return super.isInTransaction();
    }

    public /* bridge */ /* synthetic */ void refresh() {
        super.refresh();
    }

    public /* bridge */ /* synthetic */ void setAutoRefresh(boolean z) {
        super.setAutoRefresh(z);
    }

    public /* bridge */ /* synthetic */ void stopWaitForChange() {
        super.stopWaitForChange();
    }

    public /* bridge */ /* synthetic */ boolean waitForChange() {
        return super.waitForChange();
    }

    public /* bridge */ /* synthetic */ void writeCopyTo(File file) {
        super.writeCopyTo(file);
    }

    public /* bridge */ /* synthetic */ void writeEncryptedCopyTo(File file, byte[] bArr) {
        super.writeEncryptedCopyTo(file, bArr);
    }

    private Realm(RealmCache cache) {
        super(cache);
    }

    public Observable<Realm> asObservable() {
        return this.configuration.getRxFactory().from(this);
    }

    public static synchronized void init(Context context) {
        synchronized (Realm.class) {
            if (BaseRealm.applicationContext == null) {
                if (context == null) {
                    throw new IllegalArgumentException("Non-null context required.");
                }
                checkFilesDirAvailable(context);
                RealmCore.loadLibrary(context);
                setDefaultConfiguration(new Builder(context).build());
                ObjectServerFacade.getSyncFacadeIfPossible().init(context);
                BaseRealm.applicationContext = context.getApplicationContext();
                SharedRealm.initialize(new File(context.getFilesDir(), ".realm.temp"));
            }
        }
    }

    private static void checkFilesDirAvailable(Context context) {
        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            if (!filesDir.exists()) {
                try {
                    filesDir.mkdirs();
                } catch (SecurityException e) {
                }
            } else {
                return;
            }
        }
        if (filesDir == null || !filesDir.exists()) {
            long[] timeoutsMs = new long[]{1, 2, 5, 10, 16};
            long currentTotalWaitMs = 0;
            int waitIndex = -1;
            do {
                if (context.getFilesDir() != null && context.getFilesDir().exists()) {
                    break;
                }
                waitIndex++;
                long waitMs = timeoutsMs[Math.min(waitIndex, timeoutsMs.length - 1)];
                SystemClock.sleep(waitMs);
                currentTotalWaitMs += waitMs;
            } while (currentTotalWaitMs <= 200);
        }
        if (context.getFilesDir() == null || !context.getFilesDir().exists()) {
            throw new IllegalStateException("Context.getFilesDir() returns " + context.getFilesDir() + " which is not an existing directory. See https://issuetracker.google.com/issues/36918154");
        }
    }

    public static Realm getDefaultInstance() {
        RealmConfiguration configuration = getDefaultConfiguration();
        if (configuration != null) {
            return (Realm) RealmCache.createRealmOrGetFromCache(configuration, Realm.class);
        }
        if (BaseRealm.applicationContext == null) {
            throw new IllegalStateException("Call `Realm.init(Context)` before calling this method.");
        }
        throw new IllegalStateException("Set default configuration by using `Realm.setDefaultConfiguration(RealmConfiguration)`.");
    }

    public static Realm getInstance(RealmConfiguration configuration) {
        if (configuration != null) {
            return (Realm) RealmCache.createRealmOrGetFromCache(configuration, Realm.class);
        }
        throw new IllegalArgumentException(NULL_CONFIG_MSG);
    }

    public static RealmAsyncTask getInstanceAsync(RealmConfiguration configuration, Callback callback) {
        if (configuration != null) {
            return RealmCache.createRealmOrGetFromCacheAsync(configuration, callback, Realm.class);
        }
        throw new IllegalArgumentException(NULL_CONFIG_MSG);
    }

    public static void setDefaultConfiguration(RealmConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(NULL_CONFIG_MSG);
        }
        synchronized (defaultConfigurationLock) {
            defaultConfiguration = configuration;
        }
    }

    public static RealmConfiguration getDefaultConfiguration() {
        RealmConfiguration realmConfiguration;
        synchronized (defaultConfigurationLock) {
            realmConfiguration = defaultConfiguration;
        }
        return realmConfiguration;
    }

    public static void removeDefaultConfiguration() {
        synchronized (defaultConfigurationLock) {
            defaultConfiguration = null;
        }
    }

    static Realm createInstance(RealmCache cache) {
        RealmConfiguration configuration = cache.getConfiguration();
        try {
            return createAndValidateFromCache(cache);
        } catch (RealmMigrationNeededException e) {
            if (configuration.shouldDeleteRealmIfMigrationNeeded()) {
                deleteRealm(configuration);
            } else {
                try {
                    if (configuration.getMigration() != null) {
                        migrateRealm(configuration, e);
                    }
                } catch (Throwable fileNotFoundException) {
                    throw new RealmFileException(Kind.NOT_FOUND, fileNotFoundException);
                }
            }
            return createAndValidateFromCache(cache);
        }
    }

    private static Realm createAndValidateFromCache(RealmCache cache) {
        Realm realm = new Realm(cache);
        RealmConfiguration configuration = realm.configuration;
        long currentVersion = realm.getVersion();
        long requiredVersion = configuration.getSchemaVersion();
        ColumnIndices columnIndices = RealmCache.findColumnIndices(cache.getTypedColumnIndicesArray(), requiredVersion);
        if (columnIndices != null) {
            realm.schema.setInitialColumnIndices(columnIndices);
        } else {
            if (!(configuration.isSyncConfiguration() || currentVersion == -1)) {
                if (currentVersion < requiredVersion) {
                    realm.doClose();
                    throw new RealmMigrationNeededException(configuration.getPath(), String.format(Locale.US, "Realm on disk need to migrate from v%s to v%s", new Object[]{Long.valueOf(currentVersion), Long.valueOf(requiredVersion)}));
                } else if (requiredVersion < currentVersion) {
                    realm.doClose();
                    throw new IllegalArgumentException(String.format(Locale.US, "Realm on disk is newer than the one specified: v%s vs. v%s", new Object[]{Long.valueOf(currentVersion), Long.valueOf(requiredVersion)}));
                }
            }
            try {
                initializeRealm(realm);
            } catch (RuntimeException e) {
                realm.doClose();
                throw e;
            }
        }
        return realm;
    }

    private static void initializeRealm(Realm realm) {
        boolean commitChanges = false;
        try {
            realm.beginTransaction(true);
            RealmConfiguration configuration = realm.getConfiguration();
            long currentVersion = realm.getVersion();
            boolean unversioned = currentVersion == -1;
            long newVersion = configuration.getSchemaVersion();
            RealmProxyMediator mediator = configuration.getSchemaMediator();
            Set<Class<? extends RealmModel>> modelClasses = mediator.getModelClasses();
            if (configuration.isSyncConfiguration()) {
                if (!configuration.isReadOnly()) {
                    realm.sharedRealm.updateSchema(new OsSchemaInfo(mediator.getExpectedObjectSchemaInfoMap().values()), newVersion);
                    commitChanges = true;
                }
            } else if (unversioned) {
                if (configuration.isReadOnly()) {
                    throw new IllegalArgumentException("Cannot create the Realm schema in a read-only file.");
                }
                realm.sharedRealm.updateSchema(new OsSchemaInfo(mediator.getExpectedObjectSchemaInfoMap().values()), newVersion);
                commitChanges = true;
            }
            Map<Pair<Class<? extends RealmModel>, String>, ColumnInfo> columnInfoMap = new HashMap(modelClasses.size());
            for (Class<? extends RealmModel> modelClass : modelClasses) {
                columnInfoMap.put(Pair.create(modelClass, Table.getClassNameForTable(mediator.getTableName(modelClass))), mediator.validateTable(modelClass, realm.sharedRealm, configuration.isSyncConfiguration()));
            }
            RealmSchema schema = realm.getSchema();
            if (!unversioned) {
                newVersion = currentVersion;
            }
            schema.setInitialColumnIndices(newVersion, columnInfoMap);
            Transaction transaction = configuration.getInitialDataTransaction();
            if (transaction != null && unversioned) {
                transaction.execute(realm);
            }
            if (commitChanges) {
                realm.commitTransaction();
            } else if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
        } catch (Exception e) {
            throw e;
        } catch (Throwable th) {
            if (false) {
                realm.commitTransaction();
            } else if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
        }
    }

    public <E extends RealmModel> void createAllFromJson(Class<E> clazz, JSONArray json) {
        if (clazz != null && json != null) {
            checkIfValid();
            int i = 0;
            while (i < json.length()) {
                try {
                    this.configuration.getSchemaMediator().createOrUpdateUsingJsonObject(clazz, this, json.getJSONObject(i), false);
                    i++;
                } catch (JSONException e) {
                    throw new RealmException("Could not map JSON", e);
                }
            }
        }
    }

    public <E extends RealmModel> void createOrUpdateAllFromJson(Class<E> clazz, JSONArray json) {
        if (clazz != null && json != null) {
            checkIfValid();
            checkHasPrimaryKey(clazz);
            int i = 0;
            while (i < json.length()) {
                try {
                    this.configuration.getSchemaMediator().createOrUpdateUsingJsonObject(clazz, this, json.getJSONObject(i), true);
                    i++;
                } catch (JSONException e) {
                    throw new RealmException("Could not map JSON", e);
                }
            }
        }
    }

    public <E extends RealmModel> void createAllFromJson(Class<E> clazz, String json) {
        if (clazz != null && json != null && json.length() != 0) {
            try {
                createAllFromJson((Class) clazz, new JSONArray(json));
            } catch (JSONException e) {
                throw new RealmException("Could not create JSON array from string", e);
            }
        }
    }

    public <E extends RealmModel> void createOrUpdateAllFromJson(Class<E> clazz, String json) {
        if (clazz != null && json != null && json.length() != 0) {
            checkIfValid();
            checkHasPrimaryKey(clazz);
            try {
                createOrUpdateAllFromJson((Class) clazz, new JSONArray(json));
            } catch (JSONException e) {
                throw new RealmException("Could not create JSON array from string", e);
            }
        }
    }

    @TargetApi(11)
    public <E extends RealmModel> void createAllFromJson(Class<E> clazz, InputStream inputStream) throws IOException {
        if (clazz != null && inputStream != null) {
            checkIfValid();
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, HttpRequest.CHARSET_UTF8));
            try {
                reader.beginArray();
                while (reader.hasNext()) {
                    this.configuration.getSchemaMediator().createUsingJsonStream(clazz, this, reader);
                }
                reader.endArray();
            } finally {
                reader.close();
            }
        }
    }

    @TargetApi(11)
    public <E extends RealmModel> void createOrUpdateAllFromJson(Class<E> clazz, InputStream in) {
        if (clazz != null && in != null) {
            checkIfValid();
            checkHasPrimaryKey(clazz);
            Scanner scanner = null;
            try {
                scanner = getFullStringScanner(in);
                JSONArray json = new JSONArray(scanner.next());
                for (int i = 0; i < json.length(); i++) {
                    this.configuration.getSchemaMediator().createOrUpdateUsingJsonObject(clazz, this, json.getJSONObject(i), true);
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (JSONException e) {
                throw new RealmException("Failed to read JSON", e);
            } catch (Throwable th) {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
    }

    public <E extends RealmModel> E createObjectFromJson(Class<E> clazz, JSONObject json) {
        if (clazz == null || json == null) {
            return null;
        }
        checkIfValid();
        try {
            return this.configuration.getSchemaMediator().createOrUpdateUsingJsonObject(clazz, this, json, false);
        } catch (JSONException e) {
            throw new RealmException("Could not map JSON", e);
        }
    }

    public <E extends RealmModel> E createOrUpdateObjectFromJson(Class<E> clazz, JSONObject json) {
        if (clazz == null || json == null) {
            return null;
        }
        checkIfValid();
        checkHasPrimaryKey(clazz);
        try {
            return this.configuration.getSchemaMediator().createOrUpdateUsingJsonObject(clazz, this, json, true);
        } catch (JSONException e) {
            throw new RealmException("Could not map JSON", e);
        }
    }

    public <E extends RealmModel> E createObjectFromJson(Class<E> clazz, String json) {
        if (clazz == null || json == null || json.length() == 0) {
            return null;
        }
        try {
            return createObjectFromJson((Class) clazz, new JSONObject(json));
        } catch (JSONException e) {
            throw new RealmException("Could not create Json object from string", e);
        }
    }

    public <E extends RealmModel> E createOrUpdateObjectFromJson(Class<E> clazz, String json) {
        if (clazz == null || json == null || json.length() == 0) {
            return null;
        }
        checkIfValid();
        checkHasPrimaryKey(clazz);
        try {
            return createOrUpdateObjectFromJson((Class) clazz, new JSONObject(json));
        } catch (JSONException e) {
            throw new RealmException("Could not create Json object from string", e);
        }
    }

    @TargetApi(11)
    public <E extends RealmModel> E createObjectFromJson(Class<E> clazz, InputStream inputStream) throws IOException {
        if (clazz == null || inputStream == null) {
            return null;
        }
        checkIfValid();
        E realmObject;
        if (this.schema.getTable((Class) clazz).hasPrimaryKey()) {
            Scanner scanner = null;
            try {
                scanner = getFullStringScanner(inputStream);
                realmObject = this.configuration.getSchemaMediator().createOrUpdateUsingJsonObject(clazz, this, new JSONObject(scanner.next()), false);
                if (scanner == null) {
                    return realmObject;
                }
                scanner.close();
                return realmObject;
            } catch (JSONException e) {
                throw new RealmException("Failed to read JSON", e);
            } catch (Throwable th) {
                if (scanner != null) {
                    scanner.close();
                }
            }
        } else {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, HttpRequest.CHARSET_UTF8));
            try {
                realmObject = this.configuration.getSchemaMediator().createUsingJsonStream(clazz, this, reader);
                return realmObject;
            } finally {
                reader.close();
            }
        }
    }

    @TargetApi(11)
    public <E extends RealmModel> E createOrUpdateObjectFromJson(Class<E> clazz, InputStream in) {
        if (clazz == null || in == null) {
            return null;
        }
        checkIfValid();
        checkHasPrimaryKey(clazz);
        Scanner scanner = null;
        try {
            scanner = getFullStringScanner(in);
            E createOrUpdateObjectFromJson = createOrUpdateObjectFromJson((Class) clazz, new JSONObject(scanner.next()));
            if (scanner == null) {
                return createOrUpdateObjectFromJson;
            }
            scanner.close();
            return createOrUpdateObjectFromJson;
        } catch (JSONException e) {
            throw new RealmException("Failed to read JSON", e);
        } catch (Throwable th) {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private Scanner getFullStringScanner(InputStream in) {
        return new Scanner(in, HttpRequest.CHARSET_UTF8).useDelimiter("\\A");
    }

    public <E extends RealmModel> E createObject(Class<E> clazz) {
        checkIfValid();
        return createObjectInternal(clazz, true, Collections.emptyList());
    }

    <E extends RealmModel> E createObjectInternal(Class<E> clazz, boolean acceptDefaultValue, List<String> excludeFields) {
        Table table = this.schema.getTable((Class) clazz);
        if (table.hasPrimaryKey()) {
            throw new RealmException(String.format(Locale.US, "'%s' has a primary key, use 'createObject(Class<E>, Object)' instead.", new Object[]{table.getClassName()}));
        }
        return this.configuration.getSchemaMediator().newInstance(clazz, this, OsObject.create(table), this.schema.getColumnInfo((Class) clazz), acceptDefaultValue, excludeFields);
    }

    public <E extends RealmModel> E createObject(Class<E> clazz, Object primaryKeyValue) {
        checkIfValid();
        return createObjectInternal(clazz, primaryKeyValue, true, Collections.emptyList());
    }

    <E extends RealmModel> E createObjectInternal(Class<E> clazz, Object primaryKeyValue, boolean acceptDefaultValue, List<String> excludeFields) {
        Table table = this.schema.getTable((Class) clazz);
        return this.configuration.getSchemaMediator().newInstance(clazz, this, OsObject.createWithPrimaryKey(table, primaryKeyValue), this.schema.getColumnInfo((Class) clazz), acceptDefaultValue, excludeFields);
    }

    public <E extends RealmModel> E copyToRealm(E object) {
        checkNotNullObject(object);
        return copyOrUpdate(object, false, new HashMap());
    }

    public <E extends RealmModel> E copyToRealmOrUpdate(E object) {
        checkNotNullObject(object);
        checkHasPrimaryKey(object.getClass());
        return copyOrUpdate(object, true, new HashMap());
    }

    public <E extends RealmModel> List<E> copyToRealm(Iterable<E> objects) {
        if (objects == null) {
            return new ArrayList();
        }
        Map<RealmModel, RealmObjectProxy> cache = new HashMap();
        List<E> realmObjects = new ArrayList();
        for (E object : objects) {
            checkNotNullObject(object);
            realmObjects.add(copyOrUpdate(object, false, cache));
        }
        return realmObjects;
    }

    public void insert(Collection<? extends RealmModel> objects) {
        checkIfValidAndInTransaction();
        if (objects == null) {
            throw new IllegalArgumentException("Null objects cannot be inserted into Realm.");
        } else if (!objects.isEmpty()) {
            this.configuration.getSchemaMediator().insert(this, objects);
        }
    }

    public void insert(RealmModel object) {
        checkIfValidAndInTransaction();
        if (object == null) {
            throw new IllegalArgumentException("Null object cannot be inserted into Realm.");
        }
        this.configuration.getSchemaMediator().insert(this, object, new HashMap());
    }

    public void insertOrUpdate(Collection<? extends RealmModel> objects) {
        checkIfValidAndInTransaction();
        if (objects == null) {
            throw new IllegalArgumentException("Null objects cannot be inserted into Realm.");
        } else if (!objects.isEmpty()) {
            this.configuration.getSchemaMediator().insertOrUpdate(this, objects);
        }
    }

    public void insertOrUpdate(RealmModel object) {
        checkIfValidAndInTransaction();
        if (object == null) {
            throw new IllegalArgumentException("Null object cannot be inserted into Realm.");
        }
        this.configuration.getSchemaMediator().insertOrUpdate(this, object, new HashMap());
    }

    public <E extends RealmModel> List<E> copyToRealmOrUpdate(Iterable<E> objects) {
        if (objects == null) {
            return new ArrayList(0);
        }
        Map<RealmModel, RealmObjectProxy> cache = new HashMap();
        List<E> realmObjects = new ArrayList();
        for (E object : objects) {
            checkNotNullObject(object);
            realmObjects.add(copyOrUpdate(object, true, cache));
        }
        return realmObjects;
    }

    public <E extends RealmModel> List<E> copyFromRealm(Iterable<E> realmObjects) {
        return copyFromRealm((Iterable) realmObjects, Integer.MAX_VALUE);
    }

    public <E extends RealmModel> List<E> copyFromRealm(Iterable<E> realmObjects, int maxDepth) {
        checkMaxDepth(maxDepth);
        if (realmObjects == null) {
            return new ArrayList(0);
        }
        List<E> unmanagedObjects = new ArrayList();
        Map<RealmModel, CacheData<RealmModel>> listCache = new HashMap();
        for (E object : realmObjects) {
            checkValidObjectForDetach(object);
            unmanagedObjects.add(createDetachedCopy(object, maxDepth, listCache));
        }
        return unmanagedObjects;
    }

    public <E extends RealmModel> E copyFromRealm(E realmObject) {
        return copyFromRealm((RealmModel) realmObject, Integer.MAX_VALUE);
    }

    public <E extends RealmModel> E copyFromRealm(E realmObject, int maxDepth) {
        checkMaxDepth(maxDepth);
        checkValidObjectForDetach(realmObject);
        return createDetachedCopy(realmObject, maxDepth, new HashMap());
    }

    public <E extends RealmModel> RealmQuery<E> where(Class<E> clazz) {
        checkIfValid();
        return RealmQuery.createQuery(this, clazz);
    }

    public void addChangeListener(RealmChangeListener<Realm> listener) {
        addListener(listener);
    }

    public void removeChangeListener(RealmChangeListener<Realm> listener) {
        removeListener(listener);
    }

    public void removeAllChangeListeners() {
        removeAllListeners();
    }

    public void executeTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction should not be null");
        }
        beginTransaction();
        try {
            transaction.execute(this);
            commitTransaction();
        } catch (Throwable th) {
            if (isInTransaction()) {
                cancelTransaction();
            } else {
                RealmLog.warn("Could not cancel transaction, not currently in a transaction.", new Object[0]);
            }
        }
    }

    public RealmAsyncTask executeTransactionAsync(Transaction transaction) {
        return executeTransactionAsync(transaction, null, null);
    }

    public RealmAsyncTask executeTransactionAsync(Transaction transaction, OnSuccess onSuccess) {
        if (onSuccess != null) {
            return executeTransactionAsync(transaction, onSuccess, null);
        }
        throw new IllegalArgumentException("onSuccess callback can't be null");
    }

    public RealmAsyncTask executeTransactionAsync(Transaction transaction, OnError onError) {
        if (onError != null) {
            return executeTransactionAsync(transaction, null, onError);
        }
        throw new IllegalArgumentException("onError callback can't be null");
    }

    public RealmAsyncTask executeTransactionAsync(Transaction transaction, OnSuccess onSuccess, OnError onError) {
        checkIfValid();
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction should not be null");
        }
        final boolean canDeliverNotification = this.sharedRealm.capabilities.canDeliverNotification();
        if (!(onSuccess == null && onError == null)) {
            this.sharedRealm.capabilities.checkCanDeliverNotification("Callback cannot be delivered on current thread.");
        }
        final RealmConfiguration realmConfiguration = getConfiguration();
        final RealmNotifier realmNotifier = this.sharedRealm.realmNotifier;
        final Transaction transaction2 = transaction;
        final OnSuccess onSuccess2 = onSuccess;
        final OnError onError2 = onError;
        return new RealmAsyncTaskImpl(asyncTaskExecutor.submitTransaction(new Runnable() {
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    VersionID versionID = null;
                    Throwable exception = null;
                    Realm bgRealm = Realm.getInstance(realmConfiguration);
                    bgRealm.beginTransaction();
                    try {
                        transaction2.execute(bgRealm);
                        if (Thread.currentThread().isInterrupted()) {
                            try {
                                if (bgRealm.isInTransaction()) {
                                    bgRealm.cancelTransaction();
                                }
                                bgRealm.close();
                            } catch (Throwable th) {
                                bgRealm.close();
                            }
                        } else {
                            bgRealm.commitTransaction();
                            versionID = bgRealm.sharedRealm.getVersionID();
                            try {
                                if (bgRealm.isInTransaction()) {
                                    bgRealm.cancelTransaction();
                                }
                                bgRealm.close();
                                final Throwable backgroundException = exception;
                                final VersionID backgroundVersionID = versionID;
                                if (canDeliverNotification) {
                                    if (backgroundVersionID != null && onSuccess2 != null) {
                                        realmNotifier.post(new Runnable() {

                                            class C14821 implements Runnable {
                                                C14821() {
                                                }

                                                public void run() {
                                                    onSuccess2.onSuccess();
                                                }
                                            }

                                            public void run() {
                                                if (Realm.this.isClosed()) {
                                                    onSuccess2.onSuccess();
                                                } else if (Realm.this.sharedRealm.getVersionID().compareTo(backgroundVersionID) < 0) {
                                                    Realm.this.sharedRealm.realmNotifier.addTransactionCallback(new C14821());
                                                } else {
                                                    onSuccess2.onSuccess();
                                                }
                                            }
                                        });
                                    } else if (backgroundException != null) {
                                        realmNotifier.post(new Runnable() {
                                            public void run() {
                                                if (onError2 != null) {
                                                    onError2.onError(backgroundException);
                                                    return;
                                                }
                                                throw new RealmException("Async transaction failed", backgroundException);
                                            }
                                        });
                                    }
                                } else if (backgroundException != null) {
                                    throw new RealmException("Async transaction failed", backgroundException);
                                }
                            } catch (Throwable th2) {
                                bgRealm.close();
                            }
                        }
                    } catch (Throwable th3) {
                        bgRealm.close();
                    }
                }
            }
        }), asyncTaskExecutor);
    }

    public void delete(Class<? extends RealmModel> clazz) {
        checkIfValid();
        this.schema.getTable((Class) clazz).clear();
    }

    private <E extends RealmModel> E copyOrUpdate(E object, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        checkIfValid();
        return this.configuration.getSchemaMediator().copyOrUpdate(this, object, update, cache);
    }

    private <E extends RealmModel> E createDetachedCopy(E object, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        checkIfValid();
        return this.configuration.getSchemaMediator().createDetachedCopy(object, maxDepth, cache);
    }

    private <E extends RealmModel> void checkNotNullObject(E object) {
        if (object == null) {
            throw new IllegalArgumentException("Null objects cannot be copied into Realm.");
        }
    }

    private void checkHasPrimaryKey(Class<? extends RealmModel> clazz) {
        if (!this.schema.getTable((Class) clazz).hasPrimaryKey()) {
            throw new IllegalArgumentException("A RealmObject with no @PrimaryKey cannot be updated: " + clazz.toString());
        }
    }

    private void checkMaxDepth(int maxDepth) {
        if (maxDepth < 0) {
            throw new IllegalArgumentException("maxDepth must be > 0. It was: " + maxDepth);
        }
    }

    private <E extends RealmModel> void checkValidObjectForDetach(E realmObject) {
        if (realmObject == null) {
            throw new IllegalArgumentException("Null objects cannot be copied from Realm.");
        } else if (!RealmObject.isManaged(realmObject) || !RealmObject.isValid(realmObject)) {
            throw new IllegalArgumentException("Only valid managed objects can be copied from Realm.");
        } else if (realmObject instanceof DynamicRealmObject) {
            throw new IllegalArgumentException("DynamicRealmObject cannot be copied from Realm.");
        }
    }

    public static void migrateRealm(RealmConfiguration configuration) throws FileNotFoundException {
        migrateRealm(configuration, (RealmMigration) null);
    }

    private static void migrateRealm(RealmConfiguration configuration, RealmMigrationNeededException cause) throws FileNotFoundException {
        BaseRealm.migrateRealm(configuration, null, new C14862(), cause);
    }

    public static void migrateRealm(RealmConfiguration configuration, RealmMigration migration) throws FileNotFoundException {
        BaseRealm.migrateRealm(configuration, migration, new C14873(), null);
    }

    public static boolean deleteRealm(RealmConfiguration configuration) {
        return BaseRealm.deleteRealm(configuration);
    }

    public static boolean compactRealm(RealmConfiguration configuration) {
        if (!configuration.isSyncConfiguration()) {
            return BaseRealm.compactRealm(configuration);
        }
        throw new UnsupportedOperationException("Compacting is not supported yet on synced Realms. See https://github.com/realm/realm-core/issues/2345");
    }

    Table getTable(Class<? extends RealmModel> clazz) {
        return this.schema.getTable((Class) clazz);
    }

    ColumnIndices updateSchemaCache(ColumnIndices[] globalCacheArray) {
        long currentSchemaVersion = this.sharedRealm.getSchemaVersion();
        if (currentSchemaVersion == this.schema.getSchemaVersion()) {
            return null;
        }
        ColumnIndices columnIndices = null;
        ColumnIndices cacheForCurrentVersion = RealmCache.findColumnIndices(globalCacheArray, currentSchemaVersion);
        if (cacheForCurrentVersion == null) {
            RealmProxyMediator mediator = getConfiguration().getSchemaMediator();
            Set<Class<? extends RealmModel>> modelClasses = mediator.getModelClasses();
            Map map = new HashMap(modelClasses.size());
            try {
                for (Class<? extends RealmModel> clazz : modelClasses) {
                    map.put(Pair.create(clazz, Table.getClassNameForTable(mediator.getTableName(clazz))), mediator.validateTable(clazz, this.sharedRealm, true));
                }
                columnIndices = new ColumnIndices(currentSchemaVersion, map);
                cacheForCurrentVersion = columnIndices;
            } catch (RealmMigrationNeededException e) {
                throw e;
            }
        }
        this.schema.updateColumnIndices(cacheForCurrentVersion);
        return columnIndices;
    }

    public static Object getDefaultModule() {
        String moduleName = "io.realm.DefaultRealmModule";
        try {
            Constructor<?> constructor = Class.forName(moduleName).getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[0]);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (InvocationTargetException e2) {
            throw new RealmException("Could not create an instance of " + moduleName, e2);
        } catch (InstantiationException e3) {
            throw new RealmException("Could not create an instance of " + moduleName, e3);
        } catch (IllegalAccessException e4) {
            throw new RealmException("Could not create an instance of " + moduleName, e4);
        }
    }

    public static int getGlobalInstanceCount(RealmConfiguration configuration) {
        AtomicInteger globalCount = new AtomicInteger(0);
        RealmCache.invokeWithGlobalRefCount(configuration, new C14884(globalCount));
        return globalCount.get();
    }

    public static int getLocalInstanceCount(RealmConfiguration configuration) {
        return RealmCache.getLocalThreadCount(configuration);
    }
}
