package io.realm;

import android.util.JsonReader;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.realm.model.MessagesListObject;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.annotations.RealmModule;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmObjectProxy.CacheData;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {
    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;

    public boolean transformerApplied() {
        return true;
    }

    DefaultRealmModuleMediator() {
    }

    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet();
        modelClasses.add(MessagesListObject.class);
        modelClasses.add(ChatMessage.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    public Map<Class<? extends RealmModel>, OsObjectSchemaInfo> getExpectedObjectSchemaInfoMap() {
        Map<Class<? extends RealmModel>, OsObjectSchemaInfo> infoMap = new HashMap();
        infoMap.put(MessagesListObject.class, MessagesListObjectRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(ChatMessage.class, ChatMessageRealmProxy.getExpectedObjectSchemaInfo());
        return infoMap;
    }

    public ColumnInfo validateTable(Class<? extends RealmModel> clazz, SharedRealm sharedRealm, boolean allowExtraColumns) {
        RealmProxyMediator.checkClass(clazz);
        if (clazz.equals(MessagesListObject.class)) {
            return MessagesListObjectRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        }
        if (clazz.equals(ChatMessage.class)) {
            return ChatMessageRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        }
        throw RealmProxyMediator.getMissingProxyClassException(clazz);
    }

    public List<String> getFieldNames(Class<? extends RealmModel> clazz) {
        RealmProxyMediator.checkClass(clazz);
        if (clazz.equals(MessagesListObject.class)) {
            return MessagesListObjectRealmProxy.getFieldNames();
        }
        if (clazz.equals(ChatMessage.class)) {
            return ChatMessageRealmProxy.getFieldNames();
        }
        throw RealmProxyMediator.getMissingProxyClassException(clazz);
    }

    public String getTableName(Class<? extends RealmModel> clazz) {
        RealmProxyMediator.checkClass(clazz);
        if (clazz.equals(MessagesListObject.class)) {
            return MessagesListObjectRealmProxy.getTableName();
        }
        if (clazz.equals(ChatMessage.class)) {
            return ChatMessageRealmProxy.getTableName();
        }
        throw RealmProxyMediator.getMissingProxyClassException(clazz);
    }

    public <E extends RealmModel> E newInstance(Class<E> clazz, Object baseRealm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
        RealmObjectContext objectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        try {
            E e;
            objectContext.set((BaseRealm) baseRealm, row, columnInfo, acceptDefaultValue, excludeFields);
            RealmProxyMediator.checkClass(clazz);
            if (clazz.equals(MessagesListObject.class)) {
                e = (RealmModel) clazz.cast(new MessagesListObjectRealmProxy());
            } else if (clazz.equals(ChatMessage.class)) {
                RealmModel realmModel = (RealmModel) clazz.cast(new ChatMessageRealmProxy());
                objectContext.clear();
            } else {
                throw RealmProxyMediator.getMissingProxyClassException(clazz);
            }
            return e;
        } finally {
            objectContext.clear();
        }
    }

    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        Class<E> clazz = obj instanceof RealmObjectProxy ? obj.getClass().getSuperclass() : obj.getClass();
        if (clazz.equals(MessagesListObject.class)) {
            return (RealmModel) clazz.cast(MessagesListObjectRealmProxy.copyOrUpdate(realm, (MessagesListObject) obj, update, cache));
        }
        if (clazz.equals(ChatMessage.class)) {
            return (RealmModel) clazz.cast(ChatMessageRealmProxy.copyOrUpdate(realm, (ChatMessage) obj, update, cache));
        }
        throw RealmProxyMediator.getMissingProxyClassException(clazz);
    }

    public void insert(Realm realm, RealmModel object, Map<RealmModel, Long> cache) {
        Class<RealmModel> clazz = object instanceof RealmObjectProxy ? object.getClass().getSuperclass() : object.getClass();
        if (clazz.equals(MessagesListObject.class)) {
            MessagesListObjectRealmProxy.insert(realm, (MessagesListObject) object, (Map) cache);
        } else if (clazz.equals(ChatMessage.class)) {
            ChatMessageRealmProxy.insert(realm, (ChatMessage) object, (Map) cache);
        } else {
            throw RealmProxyMediator.getMissingProxyClassException(clazz);
        }
    }

    public void insert(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator iterator = objects.iterator();
        Map cache = new HashMap(objects.size());
        if (iterator.hasNext()) {
            RealmModel object = (RealmModel) iterator.next();
            Class<RealmModel> clazz = object instanceof RealmObjectProxy ? object.getClass().getSuperclass() : object.getClass();
            if (clazz.equals(MessagesListObject.class)) {
                MessagesListObjectRealmProxy.insert(realm, (MessagesListObject) object, cache);
            } else if (clazz.equals(ChatMessage.class)) {
                ChatMessageRealmProxy.insert(realm, (ChatMessage) object, cache);
            } else {
                throw RealmProxyMediator.getMissingProxyClassException(clazz);
            }
            if (!iterator.hasNext()) {
                return;
            }
            if (clazz.equals(MessagesListObject.class)) {
                MessagesListObjectRealmProxy.insert(realm, iterator, cache);
            } else if (clazz.equals(ChatMessage.class)) {
                ChatMessageRealmProxy.insert(realm, iterator, cache);
            } else {
                throw RealmProxyMediator.getMissingProxyClassException(clazz);
            }
        }
    }

    public void insertOrUpdate(Realm realm, RealmModel obj, Map<RealmModel, Long> cache) {
        Class<RealmModel> clazz = obj instanceof RealmObjectProxy ? obj.getClass().getSuperclass() : obj.getClass();
        if (clazz.equals(MessagesListObject.class)) {
            MessagesListObjectRealmProxy.insertOrUpdate(realm, (MessagesListObject) obj, (Map) cache);
        } else if (clazz.equals(ChatMessage.class)) {
            ChatMessageRealmProxy.insertOrUpdate(realm, (ChatMessage) obj, (Map) cache);
        } else {
            throw RealmProxyMediator.getMissingProxyClassException(clazz);
        }
    }

    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator iterator = objects.iterator();
        Map cache = new HashMap(objects.size());
        if (iterator.hasNext()) {
            RealmModel object = (RealmModel) iterator.next();
            Class<RealmModel> clazz = object instanceof RealmObjectProxy ? object.getClass().getSuperclass() : object.getClass();
            if (clazz.equals(MessagesListObject.class)) {
                MessagesListObjectRealmProxy.insertOrUpdate(realm, (MessagesListObject) object, cache);
            } else if (clazz.equals(ChatMessage.class)) {
                ChatMessageRealmProxy.insertOrUpdate(realm, (ChatMessage) object, cache);
            } else {
                throw RealmProxyMediator.getMissingProxyClassException(clazz);
            }
            if (!iterator.hasNext()) {
                return;
            }
            if (clazz.equals(MessagesListObject.class)) {
                MessagesListObjectRealmProxy.insertOrUpdate(realm, iterator, cache);
            } else if (clazz.equals(ChatMessage.class)) {
                ChatMessageRealmProxy.insertOrUpdate(realm, iterator, cache);
            } else {
                throw RealmProxyMediator.getMissingProxyClassException(clazz);
            }
        }
    }

    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update) throws JSONException {
        RealmProxyMediator.checkClass(clazz);
        if (clazz.equals(MessagesListObject.class)) {
            return (RealmModel) clazz.cast(MessagesListObjectRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(ChatMessage.class)) {
            return (RealmModel) clazz.cast(ChatMessageRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        throw RealmProxyMediator.getMissingProxyClassException(clazz);
    }

    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader) throws IOException {
        RealmProxyMediator.checkClass(clazz);
        if (clazz.equals(MessagesListObject.class)) {
            return (RealmModel) clazz.cast(MessagesListObjectRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(ChatMessage.class)) {
            return (RealmModel) clazz.cast(ChatMessageRealmProxy.createUsingJsonStream(realm, reader));
        }
        throw RealmProxyMediator.getMissingProxyClassException(clazz);
    }

    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        Class<E> clazz = realmObject.getClass().getSuperclass();
        if (clazz.equals(MessagesListObject.class)) {
            return (RealmModel) clazz.cast(MessagesListObjectRealmProxy.createDetachedCopy((MessagesListObject) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(ChatMessage.class)) {
            return (RealmModel) clazz.cast(ChatMessageRealmProxy.createDetachedCopy((ChatMessage) realmObject, 0, maxDepth, cache));
        }
        throw RealmProxyMediator.getMissingProxyClassException(clazz);
    }
}
