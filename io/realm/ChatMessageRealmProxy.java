package io.realm;

import android.annotation.TargetApi;
import android.util.JsonReader;
import android.util.JsonToken;
import com.trigma.tiktok.model.ChatMessage;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsObjectSchemaInfo.Builder;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmObjectProxy.CacheData;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.Table;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatMessageRealmProxy extends ChatMessage implements RealmObjectProxy, ChatMessageRealmProxyInterface {
    private static final List<String> FIELD_NAMES;
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private ChatMessageColumnInfo columnInfo;
    private ProxyState<ChatMessage> proxyState;

    static final class ChatMessageColumnInfo extends ColumnInfo {
        long isSameUserIndex;
        long linkIndex;
        long messageTextIndex;
        long nameIndex;
        long typeIndex;
        long user_typeIndex;

        ChatMessageColumnInfo(SharedRealm realm, Table table) {
            super(6);
            this.typeIndex = addColumnDetails(table, "type", RealmFieldType.STRING);
            this.user_typeIndex = addColumnDetails(table, "user_type", RealmFieldType.INTEGER);
            this.nameIndex = addColumnDetails(table, "name", RealmFieldType.STRING);
            this.messageTextIndex = addColumnDetails(table, "messageText", RealmFieldType.STRING);
            this.isSameUserIndex = addColumnDetails(table, "isSameUser", RealmFieldType.BOOLEAN);
            this.linkIndex = addColumnDetails(table, "link", RealmFieldType.STRING);
        }

        ChatMessageColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        protected final ColumnInfo copy(boolean mutable) {
            return new ChatMessageColumnInfo((ColumnInfo) this, mutable);
        }

        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            ChatMessageColumnInfo src = (ChatMessageColumnInfo) rawSrc;
            ChatMessageColumnInfo dst = (ChatMessageColumnInfo) rawDst;
            dst.typeIndex = src.typeIndex;
            dst.user_typeIndex = src.user_typeIndex;
            dst.nameIndex = src.nameIndex;
            dst.messageTextIndex = src.messageTextIndex;
            dst.isSameUserIndex = src.isSameUserIndex;
            dst.linkIndex = src.linkIndex;
        }
    }

    static {
        List<String> fieldNames = new ArrayList();
        fieldNames.add("type");
        fieldNames.add("user_type");
        fieldNames.add("name");
        fieldNames.add("messageText");
        fieldNames.add("isSameUser");
        fieldNames.add("link");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    ChatMessageRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext context = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (ChatMessageColumnInfo) context.getColumnInfo();
            this.proxyState = new ProxyState(this);
            this.proxyState.setRealm$realm(context.getRealm());
            this.proxyState.setRow$realm(context.getRow());
            this.proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(context.getExcludeFields());
        }
    }

    public String realmGet$type() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.typeIndex);
    }

    public void realmSet$type(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.typeIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.typeIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.typeIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.typeIndex, row.getIndex(), value, true);
            }
        }
    }

    public int realmGet$user_type() {
        this.proxyState.getRealm$realm().checkIfValid();
        return (int) this.proxyState.getRow$realm().getLong(this.columnInfo.user_typeIndex);
    }

    public void realmSet$user_type(int value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setLong(this.columnInfo.user_typeIndex, (long) value);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            row.getTable().setLong(this.columnInfo.user_typeIndex, row.getIndex(), (long) value, true);
        }
    }

    public String realmGet$name() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.nameIndex);
    }

    public void realmSet$name(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.nameIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.nameIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.nameIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.nameIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$messageText() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.messageTextIndex);
    }

    public void realmSet$messageText(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.messageTextIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.messageTextIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.messageTextIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.messageTextIndex, row.getIndex(), value, true);
            }
        }
    }

    public boolean realmGet$isSameUser() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getBoolean(this.columnInfo.isSameUserIndex);
    }

    public void realmSet$isSameUser(boolean value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setBoolean(this.columnInfo.isSameUserIndex, value);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            row.getTable().setBoolean(this.columnInfo.isSameUserIndex, row.getIndex(), value, true);
        }
    }

    public String realmGet$link() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.linkIndex);
    }

    public void realmSet$link(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.linkIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.linkIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.linkIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.linkIndex, row.getIndex(), value, true);
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder("ChatMessage");
        builder.addProperty("type", RealmFieldType.STRING, false, false, false);
        builder.addProperty("user_type", RealmFieldType.INTEGER, false, false, true);
        builder.addProperty("name", RealmFieldType.STRING, false, false, false);
        builder.addProperty("messageText", RealmFieldType.STRING, false, false, false);
        builder.addProperty("isSameUser", RealmFieldType.BOOLEAN, false, false, true);
        builder.addProperty("link", RealmFieldType.STRING, false, false, false);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ChatMessageColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (sharedRealm.hasTable("class_ChatMessage")) {
            Table table = sharedRealm.getTable("class_ChatMessage");
            long columnCount = table.getColumnCount();
            if (columnCount != 6) {
                if (columnCount < 6) {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 6 but was " + columnCount);
                } else if (allowExtraColumns) {
                    RealmLog.debug("Field count is more than expected - expected 6 but was %1$d", Long.valueOf(columnCount));
                } else {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 6 but was " + columnCount);
                }
            }
            Map<String, RealmFieldType> columnTypes = new HashMap();
            for (long i = 0; i < columnCount; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }
            ChatMessageColumnInfo columnInfo = new ChatMessageColumnInfo(sharedRealm, table);
            if (table.hasPrimaryKey()) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary Key defined for field " + table.getColumnName(table.getPrimaryKey()) + " was removed.");
            } else if (!columnTypes.containsKey("type")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'type' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("type") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'type' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.typeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'type' is required. Either set @Required to field 'type' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("user_type")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'user_type' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("user_type") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'user_type' in existing Realm file.");
            } else if (table.isColumnNullable(columnInfo.user_typeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'user_type' does support null values in the existing Realm file. Use corresponding boxed type for field 'user_type' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("name") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'name' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.nameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'name' is required. Either set @Required to field 'name' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("messageText")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'messageText' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("messageText") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'messageText' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.messageTextIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'messageText' is required. Either set @Required to field 'messageText' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("isSameUser")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'isSameUser' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("isSameUser") != RealmFieldType.BOOLEAN) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'boolean' for field 'isSameUser' in existing Realm file.");
            } else if (table.isColumnNullable(columnInfo.isSameUserIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'isSameUser' does support null values in the existing Realm file. Use corresponding boxed type for field 'isSameUser' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("link")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'link' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("link") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'link' in existing Realm file.");
            } else if (table.isColumnNullable(columnInfo.linkIndex)) {
                return columnInfo;
            } else {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'link' is required. Either set @Required to field 'link' or migrate using RealmObjectSchema.setNullable().");
            }
        }
        throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'ChatMessage' class is missing from the schema for this Realm.");
    }

    public static String getTableName() {
        return "class_ChatMessage";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static ChatMessage createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update) throws JSONException {
        ChatMessage obj = (ChatMessage) realm.createObjectInternal(ChatMessage.class, true, Collections.emptyList());
        if (json.has("type")) {
            if (json.isNull("type")) {
                obj.realmSet$type(null);
            } else {
                obj.realmSet$type(json.getString("type"));
            }
        }
        if (json.has("user_type")) {
            if (json.isNull("user_type")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'user_type' to null.");
            }
            obj.realmSet$user_type(json.getInt("user_type"));
        }
        if (json.has("name")) {
            if (json.isNull("name")) {
                obj.realmSet$name(null);
            } else {
                obj.realmSet$name(json.getString("name"));
            }
        }
        if (json.has("messageText")) {
            if (json.isNull("messageText")) {
                obj.realmSet$messageText(null);
            } else {
                obj.realmSet$messageText(json.getString("messageText"));
            }
        }
        if (json.has("isSameUser")) {
            if (json.isNull("isSameUser")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isSameUser' to null.");
            }
            obj.realmSet$isSameUser(json.getBoolean("isSameUser"));
        }
        if (json.has("link")) {
            if (json.isNull("link")) {
                obj.realmSet$link(null);
            } else {
                obj.realmSet$link(json.getString("link"));
            }
        }
        return obj;
    }

    @TargetApi(11)
    public static ChatMessage createUsingJsonStream(Realm realm, JsonReader reader) throws IOException {
        RealmModel obj = new ChatMessage();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("type")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((ChatMessageRealmProxyInterface) obj).realmSet$type(null);
                } else {
                    ((ChatMessageRealmProxyInterface) obj).realmSet$type(reader.nextString());
                }
            } else if (name.equals("user_type")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'user_type' to null.");
                }
                ((ChatMessageRealmProxyInterface) obj).realmSet$user_type(reader.nextInt());
            } else if (name.equals("name")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((ChatMessageRealmProxyInterface) obj).realmSet$name(null);
                } else {
                    ((ChatMessageRealmProxyInterface) obj).realmSet$name(reader.nextString());
                }
            } else if (name.equals("messageText")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((ChatMessageRealmProxyInterface) obj).realmSet$messageText(null);
                } else {
                    ((ChatMessageRealmProxyInterface) obj).realmSet$messageText(reader.nextString());
                }
            } else if (name.equals("isSameUser")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isSameUser' to null.");
                }
                ((ChatMessageRealmProxyInterface) obj).realmSet$isSameUser(reader.nextBoolean());
            } else if (!name.equals("link")) {
                reader.skipValue();
            } else if (reader.peek() == JsonToken.NULL) {
                reader.skipValue();
                ((ChatMessageRealmProxyInterface) obj).realmSet$link(null);
            } else {
                ((ChatMessageRealmProxyInterface) obj).realmSet$link(reader.nextString());
            }
        }
        reader.endObject();
        return (ChatMessage) realm.copyToRealm(obj);
    }

    public static ChatMessage copyOrUpdate(Realm realm, ChatMessage object, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        } else if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        } else {
            RealmObjectContext objectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            RealmObjectProxy cachedRealmObject = (RealmObjectProxy) cache.get(object);
            if (cachedRealmObject != null) {
                return (ChatMessage) cachedRealmObject;
            }
            return copy(realm, object, update, cache);
        }
    }

    public static ChatMessage copy(Realm realm, ChatMessage newObject, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = (RealmObjectProxy) cache.get(newObject);
        if (cachedRealmObject != null) {
            return (ChatMessage) cachedRealmObject;
        }
        ChatMessage realmObject = (ChatMessage) realm.createObjectInternal(ChatMessage.class, false, Collections.emptyList());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        ChatMessageRealmProxyInterface realmObjectSource = newObject;
        ChatMessageRealmProxyInterface realmObjectCopy = realmObject;
        realmObjectCopy.realmSet$type(realmObjectSource.realmGet$type());
        realmObjectCopy.realmSet$user_type(realmObjectSource.realmGet$user_type());
        realmObjectCopy.realmSet$name(realmObjectSource.realmGet$name());
        realmObjectCopy.realmSet$messageText(realmObjectSource.realmGet$messageText());
        realmObjectCopy.realmSet$isSameUser(realmObjectSource.realmGet$isSameUser());
        realmObjectCopy.realmSet$link(realmObjectSource.realmGet$link());
        return realmObject;
    }

    public static long insert(Realm realm, ChatMessage object, Map<RealmModel, Long> cache) {
        if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(ChatMessage.class);
        long tableNativePtr = table.getNativePtr();
        ChatMessageColumnInfo columnInfo = (ChatMessageColumnInfo) realm.schema.getColumnInfo(ChatMessage.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, Long.valueOf(rowIndex));
        String realmGet$type = object.realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.user_typeIndex, rowIndex, (long) object.realmGet$user_type(), false);
        String realmGet$name = object.realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        }
        String realmGet$messageText = object.realmGet$messageText();
        if (realmGet$messageText != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageTextIndex, rowIndex, realmGet$messageText, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isSameUserIndex, rowIndex, object.realmGet$isSameUser(), false);
        String realmGet$link = object.realmGet$link();
        if (realmGet$link == null) {
            return rowIndex;
        }
        Table.nativeSetString(tableNativePtr, columnInfo.linkIndex, rowIndex, realmGet$link, false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel, Long> cache) {
        Table table = realm.getTable(ChatMessage.class);
        long tableNativePtr = table.getNativePtr();
        ChatMessageColumnInfo columnInfo = (ChatMessageColumnInfo) realm.schema.getColumnInfo(ChatMessage.class);
        while (objects.hasNext()) {
            ChatMessage object = (ChatMessage) objects.next();
            if (!cache.containsKey(object)) {
                if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, Long.valueOf(((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex()));
                } else {
                    long rowIndex = OsObject.createRow(table);
                    cache.put(object, Long.valueOf(rowIndex));
                    String realmGet$type = object.realmGet$type();
                    if (realmGet$type != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.user_typeIndex, rowIndex, (long) object.realmGet$user_type(), false);
                    String realmGet$name = object.realmGet$name();
                    if (realmGet$name != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
                    }
                    String realmGet$messageText = object.realmGet$messageText();
                    if (realmGet$messageText != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.messageTextIndex, rowIndex, realmGet$messageText, false);
                    }
                    Table.nativeSetBoolean(tableNativePtr, columnInfo.isSameUserIndex, rowIndex, object.realmGet$isSameUser(), false);
                    String realmGet$link = object.realmGet$link();
                    if (realmGet$link != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.linkIndex, rowIndex, realmGet$link, false);
                    }
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, ChatMessage object, Map<RealmModel, Long> cache) {
        if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(ChatMessage.class);
        long tableNativePtr = table.getNativePtr();
        ChatMessageColumnInfo columnInfo = (ChatMessageColumnInfo) realm.schema.getColumnInfo(ChatMessage.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, Long.valueOf(rowIndex));
        String realmGet$type = object.realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.user_typeIndex, rowIndex, (long) object.realmGet$user_type(), false);
        String realmGet$name = object.realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
        }
        String realmGet$messageText = object.realmGet$messageText();
        if (realmGet$messageText != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageTextIndex, rowIndex, realmGet$messageText, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.messageTextIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isSameUserIndex, rowIndex, object.realmGet$isSameUser(), false);
        String realmGet$link = object.realmGet$link();
        if (realmGet$link != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.linkIndex, rowIndex, realmGet$link, false);
            return rowIndex;
        }
        Table.nativeSetNull(tableNativePtr, columnInfo.linkIndex, rowIndex, false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel, Long> cache) {
        Table table = realm.getTable(ChatMessage.class);
        long tableNativePtr = table.getNativePtr();
        ChatMessageColumnInfo columnInfo = (ChatMessageColumnInfo) realm.schema.getColumnInfo(ChatMessage.class);
        while (objects.hasNext()) {
            ChatMessage object = (ChatMessage) objects.next();
            if (!cache.containsKey(object)) {
                if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, Long.valueOf(((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex()));
                } else {
                    long rowIndex = OsObject.createRow(table);
                    cache.put(object, Long.valueOf(rowIndex));
                    String realmGet$type = object.realmGet$type();
                    if (realmGet$type != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.user_typeIndex, rowIndex, (long) object.realmGet$user_type(), false);
                    String realmGet$name = object.realmGet$name();
                    if (realmGet$name != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
                    }
                    String realmGet$messageText = object.realmGet$messageText();
                    if (realmGet$messageText != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.messageTextIndex, rowIndex, realmGet$messageText, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.messageTextIndex, rowIndex, false);
                    }
                    Table.nativeSetBoolean(tableNativePtr, columnInfo.isSameUserIndex, rowIndex, object.realmGet$isSameUser(), false);
                    String realmGet$link = object.realmGet$link();
                    if (realmGet$link != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.linkIndex, rowIndex, realmGet$link, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.linkIndex, rowIndex, false);
                    }
                }
            }
        }
    }

    public static ChatMessage createDetachedCopy(ChatMessage realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        ChatMessage unmanagedObject;
        CacheData<RealmModel> cachedObject = (CacheData) cache.get(realmObject);
        if (cachedObject == null) {
            unmanagedObject = new ChatMessage();
            cache.put(realmObject, new CacheData(currentDepth, unmanagedObject));
        } else if (currentDepth >= cachedObject.minDepth) {
            return (ChatMessage) cachedObject.object;
        } else {
            unmanagedObject = (ChatMessage) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        ChatMessageRealmProxyInterface unmanagedCopy = unmanagedObject;
        ChatMessageRealmProxyInterface realmSource = realmObject;
        unmanagedCopy.realmSet$type(realmSource.realmGet$type());
        unmanagedCopy.realmSet$user_type(realmSource.realmGet$user_type());
        unmanagedCopy.realmSet$name(realmSource.realmGet$name());
        unmanagedCopy.realmSet$messageText(realmSource.realmGet$messageText());
        unmanagedCopy.realmSet$isSameUser(realmSource.realmGet$isSameUser());
        unmanagedCopy.realmSet$link(realmSource.realmGet$link());
        return unmanagedObject;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("ChatMessage = proxy[");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type() != null ? realmGet$type() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{user_type:");
        stringBuilder.append(realmGet$user_type());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{name:");
        stringBuilder.append(realmGet$name() != null ? realmGet$name() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{messageText:");
        stringBuilder.append(realmGet$messageText() != null ? realmGet$messageText() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isSameUser:");
        stringBuilder.append(realmGet$isSameUser());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{link:");
        stringBuilder.append(realmGet$link() != null ? realmGet$link() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public ProxyState<?> realmGet$proxyState() {
        return this.proxyState;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        String realmName = this.proxyState.getRealm$realm().getPath();
        String tableName = this.proxyState.getRow$realm().getTable().getName();
        long rowIndex = this.proxyState.getRow$realm().getIndex();
        if (realmName != null) {
            hashCode = realmName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode + 527) * 31;
        if (tableName != null) {
            i = tableName.hashCode();
        }
        return ((hashCode + i) * 31) + ((int) ((rowIndex >>> 32) ^ rowIndex));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatMessageRealmProxy aChatMessage = (ChatMessageRealmProxy) o;
        String path = this.proxyState.getRealm$realm().getPath();
        String otherPath = aChatMessage.proxyState.getRealm$realm().getPath();
        if (path == null ? otherPath != null : !path.equals(otherPath)) {
            return false;
        }
        String tableName = this.proxyState.getRow$realm().getTable().getName();
        String otherTableName = aChatMessage.proxyState.getRow$realm().getTable().getName();
        if (tableName == null ? otherTableName != null : !tableName.equals(otherTableName)) {
            return false;
        }
        if (this.proxyState.getRow$realm().getIndex() != aChatMessage.proxyState.getRow$realm().getIndex()) {
            return false;
        }
        return true;
    }
}
