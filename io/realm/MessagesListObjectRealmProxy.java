package io.realm;

import android.annotation.TargetApi;
import android.util.JsonReader;
import android.util.JsonToken;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.realm.model.MessagesListObject;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.LinkView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessagesListObjectRealmProxy extends MessagesListObject implements RealmObjectProxy, MessagesListObjectRealmProxyInterface {
    private static final List<String> FIELD_NAMES;
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private RealmList<ChatMessage> chatListRealmList;
    private MessagesListObjectColumnInfo columnInfo;
    private ProxyState<MessagesListObject> proxyState;

    static final class MessagesListObjectColumnInfo extends ColumnInfo {
        long AddressIndex;
        long ApiKeyIndex;
        long BioIndex;
        long CityIndex;
        long CodeIndex;
        long DOBIndex;
        long DeviceTokenIndex;
        long EmailIndex;
        long FirstNameIndex;
        long GenderIndex;
        long GroupIdIndex;
        long LanguagesIndex;
        long LastNameIndex;
        long MobileIndex;
        long NameIndex;
        long ProfilePicIndex;
        long QualificationIndex;
        long SpecialityIndex;
        long StateIndex;
        long TokenDataIndex;
        long UserTypeIndex;
        long _idIndex;
        long chatListIndex;
        long countIndex;
        long sessionDataIndex;
        long typeIndex;
        long unique_idIndex;

        MessagesListObjectColumnInfo(SharedRealm realm, Table table) {
            super(27);
            this._idIndex = addColumnDetails(table, "_id", RealmFieldType.STRING);
            this.unique_idIndex = addColumnDetails(table, "unique_id", RealmFieldType.STRING);
            this.AddressIndex = addColumnDetails(table, "Address", RealmFieldType.STRING);
            this.ApiKeyIndex = addColumnDetails(table, "ApiKey", RealmFieldType.STRING);
            this.countIndex = addColumnDetails(table, "count", RealmFieldType.INTEGER);
            this.BioIndex = addColumnDetails(table, "Bio", RealmFieldType.STRING);
            this.CityIndex = addColumnDetails(table, "City", RealmFieldType.STRING);
            this.CodeIndex = addColumnDetails(table, "Code", RealmFieldType.STRING);
            this.DeviceTokenIndex = addColumnDetails(table, "DeviceToken", RealmFieldType.STRING);
            this.DOBIndex = addColumnDetails(table, "DOB", RealmFieldType.STRING);
            this.EmailIndex = addColumnDetails(table, "Email", RealmFieldType.STRING);
            this.FirstNameIndex = addColumnDetails(table, "FirstName", RealmFieldType.STRING);
            this.GenderIndex = addColumnDetails(table, "Gender", RealmFieldType.STRING);
            this.LanguagesIndex = addColumnDetails(table, "Languages", RealmFieldType.STRING);
            this.LastNameIndex = addColumnDetails(table, "LastName", RealmFieldType.STRING);
            this.MobileIndex = addColumnDetails(table, "Mobile", RealmFieldType.STRING);
            this.ProfilePicIndex = addColumnDetails(table, "ProfilePic", RealmFieldType.STRING);
            this.QualificationIndex = addColumnDetails(table, "Qualification", RealmFieldType.STRING);
            this.SpecialityIndex = addColumnDetails(table, "Speciality", RealmFieldType.STRING);
            this.StateIndex = addColumnDetails(table, "State", RealmFieldType.STRING);
            this.UserTypeIndex = addColumnDetails(table, "UserType", RealmFieldType.INTEGER);
            this.GroupIdIndex = addColumnDetails(table, "GroupId", RealmFieldType.STRING);
            this.sessionDataIndex = addColumnDetails(table, "sessionData", RealmFieldType.STRING);
            this.TokenDataIndex = addColumnDetails(table, "TokenData", RealmFieldType.STRING);
            this.typeIndex = addColumnDetails(table, "type", RealmFieldType.INTEGER);
            this.NameIndex = addColumnDetails(table, "Name", RealmFieldType.STRING);
            this.chatListIndex = addColumnDetails(table, "chatList", RealmFieldType.LIST);
        }

        MessagesListObjectColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        protected final ColumnInfo copy(boolean mutable) {
            return new MessagesListObjectColumnInfo((ColumnInfo) this, mutable);
        }

        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            MessagesListObjectColumnInfo src = (MessagesListObjectColumnInfo) rawSrc;
            MessagesListObjectColumnInfo dst = (MessagesListObjectColumnInfo) rawDst;
            dst._idIndex = src._idIndex;
            dst.unique_idIndex = src.unique_idIndex;
            dst.AddressIndex = src.AddressIndex;
            dst.ApiKeyIndex = src.ApiKeyIndex;
            dst.countIndex = src.countIndex;
            dst.BioIndex = src.BioIndex;
            dst.CityIndex = src.CityIndex;
            dst.CodeIndex = src.CodeIndex;
            dst.DeviceTokenIndex = src.DeviceTokenIndex;
            dst.DOBIndex = src.DOBIndex;
            dst.EmailIndex = src.EmailIndex;
            dst.FirstNameIndex = src.FirstNameIndex;
            dst.GenderIndex = src.GenderIndex;
            dst.LanguagesIndex = src.LanguagesIndex;
            dst.LastNameIndex = src.LastNameIndex;
            dst.MobileIndex = src.MobileIndex;
            dst.ProfilePicIndex = src.ProfilePicIndex;
            dst.QualificationIndex = src.QualificationIndex;
            dst.SpecialityIndex = src.SpecialityIndex;
            dst.StateIndex = src.StateIndex;
            dst.UserTypeIndex = src.UserTypeIndex;
            dst.GroupIdIndex = src.GroupIdIndex;
            dst.sessionDataIndex = src.sessionDataIndex;
            dst.TokenDataIndex = src.TokenDataIndex;
            dst.typeIndex = src.typeIndex;
            dst.NameIndex = src.NameIndex;
            dst.chatListIndex = src.chatListIndex;
        }
    }

    static {
        List<String> fieldNames = new ArrayList();
        fieldNames.add("_id");
        fieldNames.add("unique_id");
        fieldNames.add("Address");
        fieldNames.add("ApiKey");
        fieldNames.add("count");
        fieldNames.add("Bio");
        fieldNames.add("City");
        fieldNames.add("Code");
        fieldNames.add("DeviceToken");
        fieldNames.add("DOB");
        fieldNames.add("Email");
        fieldNames.add("FirstName");
        fieldNames.add("Gender");
        fieldNames.add("Languages");
        fieldNames.add("LastName");
        fieldNames.add("Mobile");
        fieldNames.add("ProfilePic");
        fieldNames.add("Qualification");
        fieldNames.add("Speciality");
        fieldNames.add("State");
        fieldNames.add("UserType");
        fieldNames.add("GroupId");
        fieldNames.add("sessionData");
        fieldNames.add("TokenData");
        fieldNames.add("type");
        fieldNames.add("Name");
        fieldNames.add("chatList");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    MessagesListObjectRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext context = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (MessagesListObjectColumnInfo) context.getColumnInfo();
            this.proxyState = new ProxyState(this);
            this.proxyState.setRealm$realm(context.getRealm());
            this.proxyState.setRow$realm(context.getRow());
            this.proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(context.getExcludeFields());
        }
    }

    public String realmGet$_id() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo._idIndex);
    }

    public void realmSet$_id(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo._idIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo._idIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo._idIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo._idIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$unique_id() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.unique_idIndex);
    }

    public void realmSet$unique_id(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            throw new RealmException("Primary key field 'unique_id' cannot be changed after object was created.");
        }
    }

    public String realmGet$Address() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.AddressIndex);
    }

    public void realmSet$Address(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.AddressIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.AddressIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.AddressIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.AddressIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$ApiKey() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.ApiKeyIndex);
    }

    public void realmSet$ApiKey(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.ApiKeyIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.ApiKeyIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.ApiKeyIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.ApiKeyIndex, row.getIndex(), value, true);
            }
        }
    }

    public int realmGet$count() {
        this.proxyState.getRealm$realm().checkIfValid();
        return (int) this.proxyState.getRow$realm().getLong(this.columnInfo.countIndex);
    }

    public void realmSet$count(int value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setLong(this.columnInfo.countIndex, (long) value);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            row.getTable().setLong(this.columnInfo.countIndex, row.getIndex(), (long) value, true);
        }
    }

    public String realmGet$Bio() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.BioIndex);
    }

    public void realmSet$Bio(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.BioIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.BioIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.BioIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.BioIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$City() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.CityIndex);
    }

    public void realmSet$City(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.CityIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.CityIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.CityIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.CityIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$Code() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.CodeIndex);
    }

    public void realmSet$Code(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.CodeIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.CodeIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.CodeIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.CodeIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$DeviceToken() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.DeviceTokenIndex);
    }

    public void realmSet$DeviceToken(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.DeviceTokenIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.DeviceTokenIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.DeviceTokenIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.DeviceTokenIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$DOB() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.DOBIndex);
    }

    public void realmSet$DOB(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.DOBIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.DOBIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.DOBIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.DOBIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$Email() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.EmailIndex);
    }

    public void realmSet$Email(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.EmailIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.EmailIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.EmailIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.EmailIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$FirstName() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.FirstNameIndex);
    }

    public void realmSet$FirstName(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.FirstNameIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.FirstNameIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.FirstNameIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.FirstNameIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$Gender() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.GenderIndex);
    }

    public void realmSet$Gender(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.GenderIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.GenderIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.GenderIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.GenderIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$Languages() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.LanguagesIndex);
    }

    public void realmSet$Languages(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.LanguagesIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.LanguagesIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.LanguagesIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.LanguagesIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$LastName() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.LastNameIndex);
    }

    public void realmSet$LastName(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.LastNameIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.LastNameIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.LastNameIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.LastNameIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$Mobile() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.MobileIndex);
    }

    public void realmSet$Mobile(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.MobileIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.MobileIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.MobileIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.MobileIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$ProfilePic() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.ProfilePicIndex);
    }

    public void realmSet$ProfilePic(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.ProfilePicIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.ProfilePicIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.ProfilePicIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.ProfilePicIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$Qualification() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.QualificationIndex);
    }

    public void realmSet$Qualification(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.QualificationIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.QualificationIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.QualificationIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.QualificationIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$Speciality() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.SpecialityIndex);
    }

    public void realmSet$Speciality(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.SpecialityIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.SpecialityIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.SpecialityIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.SpecialityIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$State() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.StateIndex);
    }

    public void realmSet$State(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.StateIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.StateIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.StateIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.StateIndex, row.getIndex(), value, true);
            }
        }
    }

    public int realmGet$UserType() {
        this.proxyState.getRealm$realm().checkIfValid();
        return (int) this.proxyState.getRow$realm().getLong(this.columnInfo.UserTypeIndex);
    }

    public void realmSet$UserType(int value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setLong(this.columnInfo.UserTypeIndex, (long) value);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            row.getTable().setLong(this.columnInfo.UserTypeIndex, row.getIndex(), (long) value, true);
        }
    }

    public String realmGet$GroupId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.GroupIdIndex);
    }

    public void realmSet$GroupId(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.GroupIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.GroupIdIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.GroupIdIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.GroupIdIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$sessionData() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.sessionDataIndex);
    }

    public void realmSet$sessionData(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.sessionDataIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.sessionDataIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.sessionDataIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.sessionDataIndex, row.getIndex(), value, true);
            }
        }
    }

    public String realmGet$TokenData() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.TokenDataIndex);
    }

    public void realmSet$TokenData(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.TokenDataIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.TokenDataIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.TokenDataIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.TokenDataIndex, row.getIndex(), value, true);
            }
        }
    }

    public int realmGet$type() {
        this.proxyState.getRealm$realm().checkIfValid();
        return (int) this.proxyState.getRow$realm().getLong(this.columnInfo.typeIndex);
    }

    public void realmSet$type(int value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setLong(this.columnInfo.typeIndex, (long) value);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            row.getTable().setLong(this.columnInfo.typeIndex, row.getIndex(), (long) value, true);
        }
    }

    public String realmGet$Name() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.NameIndex);
    }

    public void realmSet$Name(String value) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (value == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.NameIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.NameIndex, value);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row = this.proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(this.columnInfo.NameIndex, row.getIndex(), true);
            } else {
                row.getTable().setString(this.columnInfo.NameIndex, row.getIndex(), value, true);
            }
        }
    }

    public RealmList<ChatMessage> realmGet$chatList() {
        this.proxyState.getRealm$realm().checkIfValid();
        if (this.chatListRealmList != null) {
            return this.chatListRealmList;
        }
        this.chatListRealmList = new RealmList(ChatMessage.class, this.proxyState.getRow$realm().getLinkList(this.columnInfo.chatListIndex), this.proxyState.getRealm$realm());
        return this.chatListRealmList;
    }

    public void realmSet$chatList(RealmList<ChatMessage> value) {
        if (this.proxyState.isUnderConstruction()) {
            if (!this.proxyState.getAcceptDefaultValue$realm() || this.proxyState.getExcludeFields$realm().contains("chatList")) {
                return;
            }
            if (!(value == null || value.isManaged())) {
                Realm realm = (Realm) this.proxyState.getRealm$realm();
                RealmList<ChatMessage> original = value;
                value = new RealmList();
                Iterator it = original.iterator();
                while (it.hasNext()) {
                    RealmModel item = (ChatMessage) it.next();
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }
        this.proxyState.getRealm$realm().checkIfValid();
        LinkView links = this.proxyState.getRow$realm().getLinkList(this.columnInfo.chatListIndex);
        links.clear();
        if (value != null) {
            Iterator it2 = value.iterator();
            while (it2.hasNext()) {
                RealmModel linkedObject = (RealmModel) it2.next();
                if (!RealmObject.isManaged(linkedObject) || !RealmObject.isValid(linkedObject)) {
                    throw new IllegalArgumentException("Each element of 'value' must be a valid managed object.");
                } else if (((RealmObjectProxy) linkedObject).realmGet$proxyState().getRealm$realm() != this.proxyState.getRealm$realm()) {
                    throw new IllegalArgumentException("Each element of 'value' must belong to the same Realm.");
                } else {
                    links.add(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
                }
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder("MessagesListObject");
        builder.addProperty("_id", RealmFieldType.STRING, false, false, false);
        builder.addProperty("unique_id", RealmFieldType.STRING, true, true, false);
        builder.addProperty("Address", RealmFieldType.STRING, false, false, false);
        builder.addProperty("ApiKey", RealmFieldType.STRING, false, false, false);
        builder.addProperty("count", RealmFieldType.INTEGER, false, false, true);
        builder.addProperty("Bio", RealmFieldType.STRING, false, false, false);
        builder.addProperty("City", RealmFieldType.STRING, false, false, false);
        builder.addProperty("Code", RealmFieldType.STRING, false, false, false);
        builder.addProperty("DeviceToken", RealmFieldType.STRING, false, false, false);
        builder.addProperty("DOB", RealmFieldType.STRING, false, false, false);
        builder.addProperty("Email", RealmFieldType.STRING, false, false, false);
        builder.addProperty("FirstName", RealmFieldType.STRING, false, false, false);
        builder.addProperty("Gender", RealmFieldType.STRING, false, false, false);
        builder.addProperty("Languages", RealmFieldType.STRING, false, false, false);
        builder.addProperty("LastName", RealmFieldType.STRING, false, false, false);
        builder.addProperty("Mobile", RealmFieldType.STRING, false, false, false);
        builder.addProperty("ProfilePic", RealmFieldType.STRING, false, false, false);
        builder.addProperty("Qualification", RealmFieldType.STRING, false, false, false);
        builder.addProperty("Speciality", RealmFieldType.STRING, false, false, false);
        builder.addProperty("State", RealmFieldType.STRING, false, false, false);
        builder.addProperty("UserType", RealmFieldType.INTEGER, false, false, true);
        builder.addProperty("GroupId", RealmFieldType.STRING, false, false, false);
        builder.addProperty("sessionData", RealmFieldType.STRING, false, false, false);
        builder.addProperty("TokenData", RealmFieldType.STRING, false, false, false);
        builder.addProperty("type", RealmFieldType.INTEGER, false, false, true);
        builder.addProperty("Name", RealmFieldType.STRING, false, false, false);
        builder.addLinkedProperty("chatList", RealmFieldType.LIST, "ChatMessage");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static MessagesListObjectColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (sharedRealm.hasTable("class_MessagesListObject")) {
            Table table = sharedRealm.getTable("class_MessagesListObject");
            long columnCount = table.getColumnCount();
            if (columnCount != 27) {
                if (columnCount < 27) {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 27 but was " + columnCount);
                } else if (allowExtraColumns) {
                    RealmLog.debug("Field count is more than expected - expected 27 but was %1$d", Long.valueOf(columnCount));
                } else {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 27 but was " + columnCount);
                }
            }
            Map<String, RealmFieldType> columnTypes = new HashMap();
            for (long i = 0; i < columnCount; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }
            MessagesListObjectColumnInfo columnInfo = new MessagesListObjectColumnInfo(sharedRealm, table);
            if (!table.hasPrimaryKey()) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary key not defined for field 'unique_id' in existing Realm file. @PrimaryKey was added.");
            } else if (table.getPrimaryKey() != columnInfo.unique_idIndex) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary Key annotation definition was changed, from field " + table.getColumnName(table.getPrimaryKey()) + " to field unique_id");
            } else if (!columnTypes.containsKey("_id")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field '_id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("_id") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field '_id' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo._idIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field '_id' is required. Either set @Required to field '_id' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("unique_id")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'unique_id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("unique_id") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'unique_id' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.unique_idIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "@PrimaryKey field 'unique_id' does not support null values in the existing Realm file. Migrate using RealmObjectSchema.setNullable(), or mark the field as @Required.");
            } else if (!table.hasSearchIndex(table.getColumnIndex("unique_id"))) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Index not defined for field 'unique_id' in existing Realm file. Either set @Index or migrate using io.realm.internal.Table.removeSearchIndex().");
            } else if (!columnTypes.containsKey("Address")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Address' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Address") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Address' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.AddressIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Address' is required. Either set @Required to field 'Address' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("ApiKey")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'ApiKey' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("ApiKey") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'ApiKey' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.ApiKeyIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'ApiKey' is required. Either set @Required to field 'ApiKey' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("count")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'count' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("count") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'count' in existing Realm file.");
            } else if (table.isColumnNullable(columnInfo.countIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'count' does support null values in the existing Realm file. Use corresponding boxed type for field 'count' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Bio")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Bio' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Bio") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Bio' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.BioIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Bio' is required. Either set @Required to field 'Bio' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("City")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'City' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("City") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'City' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.CityIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'City' is required. Either set @Required to field 'City' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Code")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Code' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Code") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Code' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.CodeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Code' is required. Either set @Required to field 'Code' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("DeviceToken")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'DeviceToken' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("DeviceToken") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'DeviceToken' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.DeviceTokenIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'DeviceToken' is required. Either set @Required to field 'DeviceToken' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("DOB")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'DOB' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("DOB") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'DOB' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.DOBIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'DOB' is required. Either set @Required to field 'DOB' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Email")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Email' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Email") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Email' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.EmailIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Email' is required. Either set @Required to field 'Email' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("FirstName")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'FirstName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("FirstName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'FirstName' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.FirstNameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'FirstName' is required. Either set @Required to field 'FirstName' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Gender")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Gender' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Gender") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Gender' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.GenderIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Gender' is required. Either set @Required to field 'Gender' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Languages")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Languages' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Languages") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Languages' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.LanguagesIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Languages' is required. Either set @Required to field 'Languages' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("LastName")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'LastName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("LastName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'LastName' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.LastNameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'LastName' is required. Either set @Required to field 'LastName' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Mobile")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Mobile' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Mobile") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Mobile' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.MobileIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Mobile' is required. Either set @Required to field 'Mobile' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("ProfilePic")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'ProfilePic' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("ProfilePic") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'ProfilePic' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.ProfilePicIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'ProfilePic' is required. Either set @Required to field 'ProfilePic' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Qualification")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Qualification' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Qualification") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Qualification' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.QualificationIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Qualification' is required. Either set @Required to field 'Qualification' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Speciality")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Speciality' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Speciality") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Speciality' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.SpecialityIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Speciality' is required. Either set @Required to field 'Speciality' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("State")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'State' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("State") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'State' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.StateIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'State' is required. Either set @Required to field 'State' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("UserType")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'UserType' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("UserType") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'UserType' in existing Realm file.");
            } else if (table.isColumnNullable(columnInfo.UserTypeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'UserType' does support null values in the existing Realm file. Use corresponding boxed type for field 'UserType' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("GroupId")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'GroupId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("GroupId") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'GroupId' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.GroupIdIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'GroupId' is required. Either set @Required to field 'GroupId' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("sessionData")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'sessionData' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("sessionData") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'sessionData' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.sessionDataIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'sessionData' is required. Either set @Required to field 'sessionData' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("TokenData")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'TokenData' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("TokenData") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'TokenData' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.TokenDataIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'TokenData' is required. Either set @Required to field 'TokenData' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("type")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'type' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("type") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'type' in existing Realm file.");
            } else if (table.isColumnNullable(columnInfo.typeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'type' does support null values in the existing Realm file. Use corresponding boxed type for field 'type' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("Name")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'Name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            } else if (columnTypes.get("Name") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'Name' in existing Realm file.");
            } else if (!table.isColumnNullable(columnInfo.NameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'Name' is required. Either set @Required to field 'Name' or migrate using RealmObjectSchema.setNullable().");
            } else if (!columnTypes.containsKey("chatList")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'chatList'");
            } else if (columnTypes.get("chatList") != RealmFieldType.LIST) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'ChatMessage' for field 'chatList'");
            } else if (sharedRealm.hasTable("class_ChatMessage")) {
                Table table_26 = sharedRealm.getTable("class_ChatMessage");
                if (table.getLinkTarget(columnInfo.chatListIndex).hasSameSchema(table_26)) {
                    return columnInfo;
                }
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid RealmList type for field 'chatList': '" + table.getLinkTarget(columnInfo.chatListIndex).getName() + "' expected - was '" + table_26.getName() + "'");
            } else {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing class 'class_ChatMessage' for field 'chatList'");
            }
        }
        throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'MessagesListObject' class is missing from the schema for this Realm.");
    }

    public static String getTableName() {
        return "class_MessagesListObject";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static MessagesListObject createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update) throws JSONException {
        List<String> excludeFields = new ArrayList(1);
        MessagesListObject messagesListObject = null;
        if (update) {
            long rowIndex;
            Table table = realm.getTable(MessagesListObject.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (json.isNull("unique_id")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("unique_id"));
            }
            if (rowIndex != -1) {
                RealmObjectContext objectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.schema.getColumnInfo(MessagesListObject.class), false, Collections.emptyList());
                    messagesListObject = new MessagesListObjectRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (messagesListObject == null) {
            if (json.has("chatList")) {
                excludeFields.add("chatList");
            }
            if (json.has("unique_id")) {
                if (json.isNull("unique_id")) {
                    messagesListObject = (MessagesListObjectRealmProxy) realm.createObjectInternal(MessagesListObject.class, null, true, excludeFields);
                } else {
                    String string = json.getString("unique_id");
                    MessagesListObjectRealmProxy obj = (MessagesListObjectRealmProxy) realm.createObjectInternal(MessagesListObject.class, string, true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'unique_id'.");
            }
        }
        if (json.has("_id")) {
            if (json.isNull("_id")) {
                messagesListObject.realmSet$_id(null);
            } else {
                messagesListObject.realmSet$_id(json.getString("_id"));
            }
        }
        if (json.has("Address")) {
            if (json.isNull("Address")) {
                messagesListObject.realmSet$Address(null);
            } else {
                messagesListObject.realmSet$Address(json.getString("Address"));
            }
        }
        if (json.has("ApiKey")) {
            if (json.isNull("ApiKey")) {
                messagesListObject.realmSet$ApiKey(null);
            } else {
                messagesListObject.realmSet$ApiKey(json.getString("ApiKey"));
            }
        }
        if (json.has("count")) {
            if (json.isNull("count")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'count' to null.");
            }
            messagesListObject.realmSet$count(json.getInt("count"));
        }
        if (json.has("Bio")) {
            if (json.isNull("Bio")) {
                messagesListObject.realmSet$Bio(null);
            } else {
                messagesListObject.realmSet$Bio(json.getString("Bio"));
            }
        }
        if (json.has("City")) {
            if (json.isNull("City")) {
                messagesListObject.realmSet$City(null);
            } else {
                messagesListObject.realmSet$City(json.getString("City"));
            }
        }
        if (json.has("Code")) {
            if (json.isNull("Code")) {
                messagesListObject.realmSet$Code(null);
            } else {
                messagesListObject.realmSet$Code(json.getString("Code"));
            }
        }
        if (json.has("DeviceToken")) {
            if (json.isNull("DeviceToken")) {
                messagesListObject.realmSet$DeviceToken(null);
            } else {
                messagesListObject.realmSet$DeviceToken(json.getString("DeviceToken"));
            }
        }
        if (json.has("DOB")) {
            if (json.isNull("DOB")) {
                messagesListObject.realmSet$DOB(null);
            } else {
                messagesListObject.realmSet$DOB(json.getString("DOB"));
            }
        }
        if (json.has("Email")) {
            if (json.isNull("Email")) {
                messagesListObject.realmSet$Email(null);
            } else {
                messagesListObject.realmSet$Email(json.getString("Email"));
            }
        }
        if (json.has("FirstName")) {
            if (json.isNull("FirstName")) {
                messagesListObject.realmSet$FirstName(null);
            } else {
                messagesListObject.realmSet$FirstName(json.getString("FirstName"));
            }
        }
        if (json.has("Gender")) {
            if (json.isNull("Gender")) {
                messagesListObject.realmSet$Gender(null);
            } else {
                messagesListObject.realmSet$Gender(json.getString("Gender"));
            }
        }
        if (json.has("Languages")) {
            if (json.isNull("Languages")) {
                messagesListObject.realmSet$Languages(null);
            } else {
                messagesListObject.realmSet$Languages(json.getString("Languages"));
            }
        }
        if (json.has("LastName")) {
            if (json.isNull("LastName")) {
                messagesListObject.realmSet$LastName(null);
            } else {
                messagesListObject.realmSet$LastName(json.getString("LastName"));
            }
        }
        if (json.has("Mobile")) {
            if (json.isNull("Mobile")) {
                messagesListObject.realmSet$Mobile(null);
            } else {
                messagesListObject.realmSet$Mobile(json.getString("Mobile"));
            }
        }
        if (json.has("ProfilePic")) {
            if (json.isNull("ProfilePic")) {
                messagesListObject.realmSet$ProfilePic(null);
            } else {
                messagesListObject.realmSet$ProfilePic(json.getString("ProfilePic"));
            }
        }
        if (json.has("Qualification")) {
            if (json.isNull("Qualification")) {
                messagesListObject.realmSet$Qualification(null);
            } else {
                messagesListObject.realmSet$Qualification(json.getString("Qualification"));
            }
        }
        if (json.has("Speciality")) {
            if (json.isNull("Speciality")) {
                messagesListObject.realmSet$Speciality(null);
            } else {
                messagesListObject.realmSet$Speciality(json.getString("Speciality"));
            }
        }
        if (json.has("State")) {
            if (json.isNull("State")) {
                messagesListObject.realmSet$State(null);
            } else {
                messagesListObject.realmSet$State(json.getString("State"));
            }
        }
        if (json.has("UserType")) {
            if (json.isNull("UserType")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'UserType' to null.");
            }
            messagesListObject.realmSet$UserType(json.getInt("UserType"));
        }
        if (json.has("GroupId")) {
            if (json.isNull("GroupId")) {
                messagesListObject.realmSet$GroupId(null);
            } else {
                messagesListObject.realmSet$GroupId(json.getString("GroupId"));
            }
        }
        if (json.has("sessionData")) {
            if (json.isNull("sessionData")) {
                messagesListObject.realmSet$sessionData(null);
            } else {
                messagesListObject.realmSet$sessionData(json.getString("sessionData"));
            }
        }
        if (json.has("TokenData")) {
            if (json.isNull("TokenData")) {
                messagesListObject.realmSet$TokenData(null);
            } else {
                messagesListObject.realmSet$TokenData(json.getString("TokenData"));
            }
        }
        if (json.has("type")) {
            if (json.isNull("type")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'type' to null.");
            }
            messagesListObject.realmSet$type(json.getInt("type"));
        }
        if (json.has("Name")) {
            if (json.isNull("Name")) {
                messagesListObject.realmSet$Name(null);
            } else {
                messagesListObject.realmSet$Name(json.getString("Name"));
            }
        }
        if (json.has("chatList")) {
            if (json.isNull("chatList")) {
                messagesListObject.realmSet$chatList(null);
            } else {
                messagesListObject.realmGet$chatList().clear();
                JSONArray array = json.getJSONArray("chatList");
                for (int i = 0; i < array.length(); i++) {
                    messagesListObject.realmGet$chatList().add(ChatMessageRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update));
                }
            }
        }
        return messagesListObject;
    }

    @TargetApi(11)
    public static MessagesListObject createUsingJsonStream(Realm realm, JsonReader reader) throws IOException {
        boolean jsonHasPrimaryKey = false;
        RealmModel obj = new MessagesListObject();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("_id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$_id(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$_id(reader.nextString());
                }
            } else if (name.equals("unique_id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$unique_id(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$unique_id(reader.nextString());
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("Address")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Address(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Address(reader.nextString());
                }
            } else if (name.equals("ApiKey")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$ApiKey(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$ApiKey(reader.nextString());
                }
            } else if (name.equals("count")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'count' to null.");
                }
                ((MessagesListObjectRealmProxyInterface) obj).realmSet$count(reader.nextInt());
            } else if (name.equals("Bio")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Bio(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Bio(reader.nextString());
                }
            } else if (name.equals("City")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$City(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$City(reader.nextString());
                }
            } else if (name.equals("Code")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Code(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Code(reader.nextString());
                }
            } else if (name.equals("DeviceToken")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$DeviceToken(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$DeviceToken(reader.nextString());
                }
            } else if (name.equals("DOB")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$DOB(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$DOB(reader.nextString());
                }
            } else if (name.equals("Email")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Email(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Email(reader.nextString());
                }
            } else if (name.equals("FirstName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$FirstName(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$FirstName(reader.nextString());
                }
            } else if (name.equals("Gender")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Gender(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Gender(reader.nextString());
                }
            } else if (name.equals("Languages")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Languages(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Languages(reader.nextString());
                }
            } else if (name.equals("LastName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$LastName(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$LastName(reader.nextString());
                }
            } else if (name.equals("Mobile")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Mobile(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Mobile(reader.nextString());
                }
            } else if (name.equals("ProfilePic")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$ProfilePic(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$ProfilePic(reader.nextString());
                }
            } else if (name.equals("Qualification")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Qualification(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Qualification(reader.nextString());
                }
            } else if (name.equals("Speciality")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Speciality(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Speciality(reader.nextString());
                }
            } else if (name.equals("State")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$State(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$State(reader.nextString());
                }
            } else if (name.equals("UserType")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'UserType' to null.");
                }
                ((MessagesListObjectRealmProxyInterface) obj).realmSet$UserType(reader.nextInt());
            } else if (name.equals("GroupId")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$GroupId(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$GroupId(reader.nextString());
                }
            } else if (name.equals("sessionData")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$sessionData(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$sessionData(reader.nextString());
                }
            } else if (name.equals("TokenData")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$TokenData(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$TokenData(reader.nextString());
                }
            } else if (name.equals("type")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'type' to null.");
                }
                ((MessagesListObjectRealmProxyInterface) obj).realmSet$type(reader.nextInt());
            } else if (name.equals("Name")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Name(null);
                } else {
                    ((MessagesListObjectRealmProxyInterface) obj).realmSet$Name(reader.nextString());
                }
            } else if (!name.equals("chatList")) {
                reader.skipValue();
            } else if (reader.peek() == JsonToken.NULL) {
                reader.skipValue();
                ((MessagesListObjectRealmProxyInterface) obj).realmSet$chatList(null);
            } else {
                ((MessagesListObjectRealmProxyInterface) obj).realmSet$chatList(new RealmList());
                reader.beginArray();
                while (reader.hasNext()) {
                    ((MessagesListObjectRealmProxyInterface) obj).realmGet$chatList().add(ChatMessageRealmProxy.createUsingJsonStream(realm, reader));
                }
                reader.endArray();
            }
        }
        reader.endObject();
        if (jsonHasPrimaryKey) {
            return (MessagesListObject) realm.copyToRealm(obj);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'unique_id'.");
    }

    public static MessagesListObject copyOrUpdate(Realm realm, MessagesListObject object, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        Throwable th;
        if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        } else if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        } else {
            RealmObjectContext objectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            RealmObjectProxy cachedRealmObject = (RealmObjectProxy) cache.get(object);
            if (cachedRealmObject != null) {
                return (MessagesListObject) cachedRealmObject;
            }
            MessagesListObject realmObject = null;
            boolean canUpdate = update;
            if (canUpdate) {
                long rowIndex;
                Table table = realm.getTable(MessagesListObject.class);
                long pkColumnIndex = table.getPrimaryKey();
                String value = object.realmGet$unique_id();
                if (value == null) {
                    rowIndex = table.findFirstNull(pkColumnIndex);
                } else {
                    rowIndex = table.findFirstString(pkColumnIndex, value);
                }
                if (rowIndex != -1) {
                    try {
                        objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.schema.getColumnInfo(MessagesListObject.class), false, Collections.emptyList());
                        MessagesListObject realmObject2 = new MessagesListObjectRealmProxy();
                        try {
                            cache.put(object, (RealmObjectProxy) realmObject2);
                            objectContext.clear();
                            realmObject = realmObject2;
                        } catch (Throwable th2) {
                            th = th2;
                            realmObject = realmObject2;
                            objectContext.clear();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        objectContext.clear();
                        throw th;
                    }
                }
                canUpdate = false;
            }
            if (canUpdate) {
                return update(realm, realmObject, object, cache);
            }
            return copy(realm, object, update, cache);
        }
    }

    public static MessagesListObject copy(Realm realm, MessagesListObject newObject, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = (RealmObjectProxy) cache.get(newObject);
        if (cachedRealmObject != null) {
            return (MessagesListObject) cachedRealmObject;
        }
        MessagesListObject realmObject = (MessagesListObject) realm.createObjectInternal(MessagesListObject.class, newObject.realmGet$unique_id(), false, Collections.emptyList());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        MessagesListObjectRealmProxyInterface realmObjectSource = newObject;
        MessagesListObjectRealmProxyInterface realmObjectCopy = realmObject;
        realmObjectCopy.realmSet$_id(realmObjectSource.realmGet$_id());
        realmObjectCopy.realmSet$Address(realmObjectSource.realmGet$Address());
        realmObjectCopy.realmSet$ApiKey(realmObjectSource.realmGet$ApiKey());
        realmObjectCopy.realmSet$count(realmObjectSource.realmGet$count());
        realmObjectCopy.realmSet$Bio(realmObjectSource.realmGet$Bio());
        realmObjectCopy.realmSet$City(realmObjectSource.realmGet$City());
        realmObjectCopy.realmSet$Code(realmObjectSource.realmGet$Code());
        realmObjectCopy.realmSet$DeviceToken(realmObjectSource.realmGet$DeviceToken());
        realmObjectCopy.realmSet$DOB(realmObjectSource.realmGet$DOB());
        realmObjectCopy.realmSet$Email(realmObjectSource.realmGet$Email());
        realmObjectCopy.realmSet$FirstName(realmObjectSource.realmGet$FirstName());
        realmObjectCopy.realmSet$Gender(realmObjectSource.realmGet$Gender());
        realmObjectCopy.realmSet$Languages(realmObjectSource.realmGet$Languages());
        realmObjectCopy.realmSet$LastName(realmObjectSource.realmGet$LastName());
        realmObjectCopy.realmSet$Mobile(realmObjectSource.realmGet$Mobile());
        realmObjectCopy.realmSet$ProfilePic(realmObjectSource.realmGet$ProfilePic());
        realmObjectCopy.realmSet$Qualification(realmObjectSource.realmGet$Qualification());
        realmObjectCopy.realmSet$Speciality(realmObjectSource.realmGet$Speciality());
        realmObjectCopy.realmSet$State(realmObjectSource.realmGet$State());
        realmObjectCopy.realmSet$UserType(realmObjectSource.realmGet$UserType());
        realmObjectCopy.realmSet$GroupId(realmObjectSource.realmGet$GroupId());
        realmObjectCopy.realmSet$sessionData(realmObjectSource.realmGet$sessionData());
        realmObjectCopy.realmSet$TokenData(realmObjectSource.realmGet$TokenData());
        realmObjectCopy.realmSet$type(realmObjectSource.realmGet$type());
        realmObjectCopy.realmSet$Name(realmObjectSource.realmGet$Name());
        RealmList<ChatMessage> chatListList = realmObjectSource.realmGet$chatList();
        if (chatListList != null) {
            RealmList<ChatMessage> chatListRealmList = realmObjectCopy.realmGet$chatList();
            for (int i = 0; i < chatListList.size(); i++) {
                ChatMessage chatListItem = (ChatMessage) chatListList.get(i);
                RealmModel cachechatList = (ChatMessage) cache.get(chatListItem);
                if (cachechatList != null) {
                    chatListRealmList.add(cachechatList);
                } else {
                    chatListRealmList.add(ChatMessageRealmProxy.copyOrUpdate(realm, chatListItem, update, cache));
                }
            }
        }
        return realmObject;
    }

    public static long insert(Realm realm, MessagesListObject object, Map<RealmModel, Long> cache) {
        if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        long rowIndex;
        Table table = realm.getTable(MessagesListObject.class);
        long tableNativePtr = table.getNativePtr();
        MessagesListObjectColumnInfo columnInfo = (MessagesListObjectColumnInfo) realm.schema.getColumnInfo(MessagesListObject.class);
        long pkColumnIndex = table.getPrimaryKey();
        String primaryKeyValue = object.realmGet$unique_id();
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == -1) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, Long.valueOf(rowIndex));
        String realmGet$_id = object.realmGet$_id();
        if (realmGet$_id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo._idIndex, rowIndex, realmGet$_id, false);
        }
        String realmGet$Address = object.realmGet$Address();
        if (realmGet$Address != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.AddressIndex, rowIndex, realmGet$Address, false);
        }
        String realmGet$ApiKey = object.realmGet$ApiKey();
        if (realmGet$ApiKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ApiKeyIndex, rowIndex, realmGet$ApiKey, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, (long) object.realmGet$count(), false);
        String realmGet$Bio = object.realmGet$Bio();
        if (realmGet$Bio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.BioIndex, rowIndex, realmGet$Bio, false);
        }
        String realmGet$City = object.realmGet$City();
        if (realmGet$City != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.CityIndex, rowIndex, realmGet$City, false);
        }
        String realmGet$Code = object.realmGet$Code();
        if (realmGet$Code != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.CodeIndex, rowIndex, realmGet$Code, false);
        }
        String realmGet$DeviceToken = object.realmGet$DeviceToken();
        if (realmGet$DeviceToken != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.DeviceTokenIndex, rowIndex, realmGet$DeviceToken, false);
        }
        String realmGet$DOB = object.realmGet$DOB();
        if (realmGet$DOB != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.DOBIndex, rowIndex, realmGet$DOB, false);
        }
        String realmGet$Email = object.realmGet$Email();
        if (realmGet$Email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.EmailIndex, rowIndex, realmGet$Email, false);
        }
        String realmGet$FirstName = object.realmGet$FirstName();
        if (realmGet$FirstName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.FirstNameIndex, rowIndex, realmGet$FirstName, false);
        }
        String realmGet$Gender = object.realmGet$Gender();
        if (realmGet$Gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.GenderIndex, rowIndex, realmGet$Gender, false);
        }
        String realmGet$Languages = object.realmGet$Languages();
        if (realmGet$Languages != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.LanguagesIndex, rowIndex, realmGet$Languages, false);
        }
        String realmGet$LastName = object.realmGet$LastName();
        if (realmGet$LastName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.LastNameIndex, rowIndex, realmGet$LastName, false);
        }
        String realmGet$Mobile = object.realmGet$Mobile();
        if (realmGet$Mobile != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.MobileIndex, rowIndex, realmGet$Mobile, false);
        }
        String realmGet$ProfilePic = object.realmGet$ProfilePic();
        if (realmGet$ProfilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ProfilePicIndex, rowIndex, realmGet$ProfilePic, false);
        }
        String realmGet$Qualification = object.realmGet$Qualification();
        if (realmGet$Qualification != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.QualificationIndex, rowIndex, realmGet$Qualification, false);
        }
        String realmGet$Speciality = object.realmGet$Speciality();
        if (realmGet$Speciality != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.SpecialityIndex, rowIndex, realmGet$Speciality, false);
        }
        String realmGet$State = object.realmGet$State();
        if (realmGet$State != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.StateIndex, rowIndex, realmGet$State, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.UserTypeIndex, rowIndex, (long) object.realmGet$UserType(), false);
        String realmGet$GroupId = object.realmGet$GroupId();
        if (realmGet$GroupId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.GroupIdIndex, rowIndex, realmGet$GroupId, false);
        }
        String realmGet$sessionData = object.realmGet$sessionData();
        if (realmGet$sessionData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sessionDataIndex, rowIndex, realmGet$sessionData, false);
        }
        String realmGet$TokenData = object.realmGet$TokenData();
        if (realmGet$TokenData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.TokenDataIndex, rowIndex, realmGet$TokenData, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, (long) object.realmGet$type(), false);
        String realmGet$Name = object.realmGet$Name();
        if (realmGet$Name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.NameIndex, rowIndex, realmGet$Name, false);
        }
        RealmList<ChatMessage> chatListList = object.realmGet$chatList();
        if (chatListList == null) {
            return rowIndex;
        }
        long chatListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.chatListIndex, rowIndex);
        Iterator it = chatListList.iterator();
        while (it.hasNext()) {
            ChatMessage chatListItem = (ChatMessage) it.next();
            Long cacheItemIndexchatList = (Long) cache.get(chatListItem);
            if (cacheItemIndexchatList == null) {
                cacheItemIndexchatList = Long.valueOf(ChatMessageRealmProxy.insert(realm, chatListItem, (Map) cache));
            }
            LinkView.nativeAdd(chatListNativeLinkViewPtr, cacheItemIndexchatList.longValue());
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel, Long> cache) {
        Table table = realm.getTable(MessagesListObject.class);
        long tableNativePtr = table.getNativePtr();
        MessagesListObjectColumnInfo columnInfo = (MessagesListObjectColumnInfo) realm.schema.getColumnInfo(MessagesListObject.class);
        long pkColumnIndex = table.getPrimaryKey();
        while (objects.hasNext()) {
            MessagesListObject object = (MessagesListObject) objects.next();
            if (!cache.containsKey(object)) {
                if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, Long.valueOf(((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex()));
                } else {
                    long rowIndex;
                    String primaryKeyValue = object.realmGet$unique_id();
                    if (primaryKeyValue == null) {
                        rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
                    } else {
                        rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
                    }
                    if (rowIndex == -1) {
                        rowIndex = OsObject.createRowWithPrimaryKey(table, primaryKeyValue);
                    } else {
                        Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
                    }
                    cache.put(object, Long.valueOf(rowIndex));
                    String realmGet$_id = object.realmGet$_id();
                    if (realmGet$_id != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo._idIndex, rowIndex, realmGet$_id, false);
                    }
                    String realmGet$Address = object.realmGet$Address();
                    if (realmGet$Address != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.AddressIndex, rowIndex, realmGet$Address, false);
                    }
                    String realmGet$ApiKey = object.realmGet$ApiKey();
                    if (realmGet$ApiKey != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.ApiKeyIndex, rowIndex, realmGet$ApiKey, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, (long) object.realmGet$count(), false);
                    String realmGet$Bio = object.realmGet$Bio();
                    if (realmGet$Bio != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.BioIndex, rowIndex, realmGet$Bio, false);
                    }
                    String realmGet$City = object.realmGet$City();
                    if (realmGet$City != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.CityIndex, rowIndex, realmGet$City, false);
                    }
                    String realmGet$Code = object.realmGet$Code();
                    if (realmGet$Code != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.CodeIndex, rowIndex, realmGet$Code, false);
                    }
                    String realmGet$DeviceToken = object.realmGet$DeviceToken();
                    if (realmGet$DeviceToken != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.DeviceTokenIndex, rowIndex, realmGet$DeviceToken, false);
                    }
                    String realmGet$DOB = object.realmGet$DOB();
                    if (realmGet$DOB != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.DOBIndex, rowIndex, realmGet$DOB, false);
                    }
                    String realmGet$Email = object.realmGet$Email();
                    if (realmGet$Email != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.EmailIndex, rowIndex, realmGet$Email, false);
                    }
                    String realmGet$FirstName = object.realmGet$FirstName();
                    if (realmGet$FirstName != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.FirstNameIndex, rowIndex, realmGet$FirstName, false);
                    }
                    String realmGet$Gender = object.realmGet$Gender();
                    if (realmGet$Gender != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.GenderIndex, rowIndex, realmGet$Gender, false);
                    }
                    String realmGet$Languages = object.realmGet$Languages();
                    if (realmGet$Languages != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.LanguagesIndex, rowIndex, realmGet$Languages, false);
                    }
                    String realmGet$LastName = object.realmGet$LastName();
                    if (realmGet$LastName != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.LastNameIndex, rowIndex, realmGet$LastName, false);
                    }
                    String realmGet$Mobile = object.realmGet$Mobile();
                    if (realmGet$Mobile != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.MobileIndex, rowIndex, realmGet$Mobile, false);
                    }
                    String realmGet$ProfilePic = object.realmGet$ProfilePic();
                    if (realmGet$ProfilePic != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.ProfilePicIndex, rowIndex, realmGet$ProfilePic, false);
                    }
                    String realmGet$Qualification = object.realmGet$Qualification();
                    if (realmGet$Qualification != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.QualificationIndex, rowIndex, realmGet$Qualification, false);
                    }
                    String realmGet$Speciality = object.realmGet$Speciality();
                    if (realmGet$Speciality != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.SpecialityIndex, rowIndex, realmGet$Speciality, false);
                    }
                    String realmGet$State = object.realmGet$State();
                    if (realmGet$State != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.StateIndex, rowIndex, realmGet$State, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.UserTypeIndex, rowIndex, (long) object.realmGet$UserType(), false);
                    String realmGet$GroupId = object.realmGet$GroupId();
                    if (realmGet$GroupId != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.GroupIdIndex, rowIndex, realmGet$GroupId, false);
                    }
                    String realmGet$sessionData = object.realmGet$sessionData();
                    if (realmGet$sessionData != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.sessionDataIndex, rowIndex, realmGet$sessionData, false);
                    }
                    String realmGet$TokenData = object.realmGet$TokenData();
                    if (realmGet$TokenData != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.TokenDataIndex, rowIndex, realmGet$TokenData, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, (long) object.realmGet$type(), false);
                    String realmGet$Name = object.realmGet$Name();
                    if (realmGet$Name != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.NameIndex, rowIndex, realmGet$Name, false);
                    }
                    RealmList<ChatMessage> chatListList = object.realmGet$chatList();
                    if (chatListList != null) {
                        long chatListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.chatListIndex, rowIndex);
                        Iterator it = chatListList.iterator();
                        while (it.hasNext()) {
                            ChatMessage chatListItem = (ChatMessage) it.next();
                            Long cacheItemIndexchatList = (Long) cache.get(chatListItem);
                            if (cacheItemIndexchatList == null) {
                                cacheItemIndexchatList = Long.valueOf(ChatMessageRealmProxy.insert(realm, chatListItem, (Map) cache));
                            }
                            LinkView.nativeAdd(chatListNativeLinkViewPtr, cacheItemIndexchatList.longValue());
                        }
                    }
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, MessagesListObject object, Map<RealmModel, Long> cache) {
        if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        long rowIndex;
        Table table = realm.getTable(MessagesListObject.class);
        long tableNativePtr = table.getNativePtr();
        MessagesListObjectColumnInfo columnInfo = (MessagesListObjectColumnInfo) realm.schema.getColumnInfo(MessagesListObject.class);
        long pkColumnIndex = table.getPrimaryKey();
        String primaryKeyValue = object.realmGet$unique_id();
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == -1) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, primaryKeyValue);
        }
        cache.put(object, Long.valueOf(rowIndex));
        String realmGet$_id = object.realmGet$_id();
        if (realmGet$_id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo._idIndex, rowIndex, realmGet$_id, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo._idIndex, rowIndex, false);
        }
        String realmGet$Address = object.realmGet$Address();
        if (realmGet$Address != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.AddressIndex, rowIndex, realmGet$Address, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.AddressIndex, rowIndex, false);
        }
        String realmGet$ApiKey = object.realmGet$ApiKey();
        if (realmGet$ApiKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ApiKeyIndex, rowIndex, realmGet$ApiKey, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.ApiKeyIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, (long) object.realmGet$count(), false);
        String realmGet$Bio = object.realmGet$Bio();
        if (realmGet$Bio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.BioIndex, rowIndex, realmGet$Bio, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.BioIndex, rowIndex, false);
        }
        String realmGet$City = object.realmGet$City();
        if (realmGet$City != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.CityIndex, rowIndex, realmGet$City, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.CityIndex, rowIndex, false);
        }
        String realmGet$Code = object.realmGet$Code();
        if (realmGet$Code != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.CodeIndex, rowIndex, realmGet$Code, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.CodeIndex, rowIndex, false);
        }
        String realmGet$DeviceToken = object.realmGet$DeviceToken();
        if (realmGet$DeviceToken != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.DeviceTokenIndex, rowIndex, realmGet$DeviceToken, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.DeviceTokenIndex, rowIndex, false);
        }
        String realmGet$DOB = object.realmGet$DOB();
        if (realmGet$DOB != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.DOBIndex, rowIndex, realmGet$DOB, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.DOBIndex, rowIndex, false);
        }
        String realmGet$Email = object.realmGet$Email();
        if (realmGet$Email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.EmailIndex, rowIndex, realmGet$Email, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.EmailIndex, rowIndex, false);
        }
        String realmGet$FirstName = object.realmGet$FirstName();
        if (realmGet$FirstName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.FirstNameIndex, rowIndex, realmGet$FirstName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.FirstNameIndex, rowIndex, false);
        }
        String realmGet$Gender = object.realmGet$Gender();
        if (realmGet$Gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.GenderIndex, rowIndex, realmGet$Gender, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.GenderIndex, rowIndex, false);
        }
        String realmGet$Languages = object.realmGet$Languages();
        if (realmGet$Languages != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.LanguagesIndex, rowIndex, realmGet$Languages, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.LanguagesIndex, rowIndex, false);
        }
        String realmGet$LastName = object.realmGet$LastName();
        if (realmGet$LastName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.LastNameIndex, rowIndex, realmGet$LastName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.LastNameIndex, rowIndex, false);
        }
        String realmGet$Mobile = object.realmGet$Mobile();
        if (realmGet$Mobile != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.MobileIndex, rowIndex, realmGet$Mobile, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.MobileIndex, rowIndex, false);
        }
        String realmGet$ProfilePic = object.realmGet$ProfilePic();
        if (realmGet$ProfilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ProfilePicIndex, rowIndex, realmGet$ProfilePic, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.ProfilePicIndex, rowIndex, false);
        }
        String realmGet$Qualification = object.realmGet$Qualification();
        if (realmGet$Qualification != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.QualificationIndex, rowIndex, realmGet$Qualification, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.QualificationIndex, rowIndex, false);
        }
        String realmGet$Speciality = object.realmGet$Speciality();
        if (realmGet$Speciality != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.SpecialityIndex, rowIndex, realmGet$Speciality, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.SpecialityIndex, rowIndex, false);
        }
        String realmGet$State = object.realmGet$State();
        if (realmGet$State != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.StateIndex, rowIndex, realmGet$State, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.StateIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.UserTypeIndex, rowIndex, (long) object.realmGet$UserType(), false);
        String realmGet$GroupId = object.realmGet$GroupId();
        if (realmGet$GroupId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.GroupIdIndex, rowIndex, realmGet$GroupId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.GroupIdIndex, rowIndex, false);
        }
        String realmGet$sessionData = object.realmGet$sessionData();
        if (realmGet$sessionData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sessionDataIndex, rowIndex, realmGet$sessionData, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.sessionDataIndex, rowIndex, false);
        }
        String realmGet$TokenData = object.realmGet$TokenData();
        if (realmGet$TokenData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.TokenDataIndex, rowIndex, realmGet$TokenData, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.TokenDataIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, (long) object.realmGet$type(), false);
        String realmGet$Name = object.realmGet$Name();
        if (realmGet$Name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.NameIndex, rowIndex, realmGet$Name, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.NameIndex, rowIndex, false);
        }
        long chatListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.chatListIndex, rowIndex);
        LinkView.nativeClear(chatListNativeLinkViewPtr);
        RealmList<ChatMessage> chatListList = object.realmGet$chatList();
        if (chatListList == null) {
            return rowIndex;
        }
        Iterator it = chatListList.iterator();
        while (it.hasNext()) {
            ChatMessage chatListItem = (ChatMessage) it.next();
            Long cacheItemIndexchatList = (Long) cache.get(chatListItem);
            if (cacheItemIndexchatList == null) {
                cacheItemIndexchatList = Long.valueOf(ChatMessageRealmProxy.insertOrUpdate(realm, chatListItem, (Map) cache));
            }
            LinkView.nativeAdd(chatListNativeLinkViewPtr, cacheItemIndexchatList.longValue());
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel, Long> cache) {
        Table table = realm.getTable(MessagesListObject.class);
        long tableNativePtr = table.getNativePtr();
        MessagesListObjectColumnInfo columnInfo = (MessagesListObjectColumnInfo) realm.schema.getColumnInfo(MessagesListObject.class);
        long pkColumnIndex = table.getPrimaryKey();
        while (objects.hasNext()) {
            MessagesListObject object = (MessagesListObject) objects.next();
            if (!cache.containsKey(object)) {
                if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, Long.valueOf(((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex()));
                } else {
                    long rowIndex;
                    String primaryKeyValue = object.realmGet$unique_id();
                    if (primaryKeyValue == null) {
                        rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
                    } else {
                        rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
                    }
                    if (rowIndex == -1) {
                        rowIndex = OsObject.createRowWithPrimaryKey(table, primaryKeyValue);
                    }
                    cache.put(object, Long.valueOf(rowIndex));
                    String realmGet$_id = object.realmGet$_id();
                    if (realmGet$_id != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo._idIndex, rowIndex, realmGet$_id, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo._idIndex, rowIndex, false);
                    }
                    String realmGet$Address = object.realmGet$Address();
                    if (realmGet$Address != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.AddressIndex, rowIndex, realmGet$Address, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.AddressIndex, rowIndex, false);
                    }
                    String realmGet$ApiKey = object.realmGet$ApiKey();
                    if (realmGet$ApiKey != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.ApiKeyIndex, rowIndex, realmGet$ApiKey, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.ApiKeyIndex, rowIndex, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, (long) object.realmGet$count(), false);
                    String realmGet$Bio = object.realmGet$Bio();
                    if (realmGet$Bio != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.BioIndex, rowIndex, realmGet$Bio, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.BioIndex, rowIndex, false);
                    }
                    String realmGet$City = object.realmGet$City();
                    if (realmGet$City != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.CityIndex, rowIndex, realmGet$City, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.CityIndex, rowIndex, false);
                    }
                    String realmGet$Code = object.realmGet$Code();
                    if (realmGet$Code != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.CodeIndex, rowIndex, realmGet$Code, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.CodeIndex, rowIndex, false);
                    }
                    String realmGet$DeviceToken = object.realmGet$DeviceToken();
                    if (realmGet$DeviceToken != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.DeviceTokenIndex, rowIndex, realmGet$DeviceToken, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.DeviceTokenIndex, rowIndex, false);
                    }
                    String realmGet$DOB = object.realmGet$DOB();
                    if (realmGet$DOB != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.DOBIndex, rowIndex, realmGet$DOB, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.DOBIndex, rowIndex, false);
                    }
                    String realmGet$Email = object.realmGet$Email();
                    if (realmGet$Email != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.EmailIndex, rowIndex, realmGet$Email, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.EmailIndex, rowIndex, false);
                    }
                    String realmGet$FirstName = object.realmGet$FirstName();
                    if (realmGet$FirstName != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.FirstNameIndex, rowIndex, realmGet$FirstName, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.FirstNameIndex, rowIndex, false);
                    }
                    String realmGet$Gender = object.realmGet$Gender();
                    if (realmGet$Gender != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.GenderIndex, rowIndex, realmGet$Gender, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.GenderIndex, rowIndex, false);
                    }
                    String realmGet$Languages = object.realmGet$Languages();
                    if (realmGet$Languages != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.LanguagesIndex, rowIndex, realmGet$Languages, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.LanguagesIndex, rowIndex, false);
                    }
                    String realmGet$LastName = object.realmGet$LastName();
                    if (realmGet$LastName != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.LastNameIndex, rowIndex, realmGet$LastName, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.LastNameIndex, rowIndex, false);
                    }
                    String realmGet$Mobile = object.realmGet$Mobile();
                    if (realmGet$Mobile != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.MobileIndex, rowIndex, realmGet$Mobile, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.MobileIndex, rowIndex, false);
                    }
                    String realmGet$ProfilePic = object.realmGet$ProfilePic();
                    if (realmGet$ProfilePic != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.ProfilePicIndex, rowIndex, realmGet$ProfilePic, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.ProfilePicIndex, rowIndex, false);
                    }
                    String realmGet$Qualification = object.realmGet$Qualification();
                    if (realmGet$Qualification != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.QualificationIndex, rowIndex, realmGet$Qualification, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.QualificationIndex, rowIndex, false);
                    }
                    String realmGet$Speciality = object.realmGet$Speciality();
                    if (realmGet$Speciality != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.SpecialityIndex, rowIndex, realmGet$Speciality, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.SpecialityIndex, rowIndex, false);
                    }
                    String realmGet$State = object.realmGet$State();
                    if (realmGet$State != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.StateIndex, rowIndex, realmGet$State, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.StateIndex, rowIndex, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.UserTypeIndex, rowIndex, (long) object.realmGet$UserType(), false);
                    String realmGet$GroupId = object.realmGet$GroupId();
                    if (realmGet$GroupId != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.GroupIdIndex, rowIndex, realmGet$GroupId, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.GroupIdIndex, rowIndex, false);
                    }
                    String realmGet$sessionData = object.realmGet$sessionData();
                    if (realmGet$sessionData != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.sessionDataIndex, rowIndex, realmGet$sessionData, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.sessionDataIndex, rowIndex, false);
                    }
                    String realmGet$TokenData = object.realmGet$TokenData();
                    if (realmGet$TokenData != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.TokenDataIndex, rowIndex, realmGet$TokenData, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.TokenDataIndex, rowIndex, false);
                    }
                    Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, (long) object.realmGet$type(), false);
                    String realmGet$Name = object.realmGet$Name();
                    if (realmGet$Name != null) {
                        Table.nativeSetString(tableNativePtr, columnInfo.NameIndex, rowIndex, realmGet$Name, false);
                    } else {
                        Table.nativeSetNull(tableNativePtr, columnInfo.NameIndex, rowIndex, false);
                    }
                    long chatListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.chatListIndex, rowIndex);
                    LinkView.nativeClear(chatListNativeLinkViewPtr);
                    RealmList<ChatMessage> chatListList = object.realmGet$chatList();
                    if (chatListList != null) {
                        Iterator it = chatListList.iterator();
                        while (it.hasNext()) {
                            ChatMessage chatListItem = (ChatMessage) it.next();
                            Long cacheItemIndexchatList = (Long) cache.get(chatListItem);
                            if (cacheItemIndexchatList == null) {
                                cacheItemIndexchatList = Long.valueOf(ChatMessageRealmProxy.insertOrUpdate(realm, chatListItem, (Map) cache));
                            }
                            LinkView.nativeAdd(chatListNativeLinkViewPtr, cacheItemIndexchatList.longValue());
                        }
                    }
                }
            }
        }
    }

    public static MessagesListObject createDetachedCopy(MessagesListObject realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        MessagesListObject unmanagedObject;
        CacheData<RealmModel> cachedObject = (CacheData) cache.get(realmObject);
        if (cachedObject == null) {
            unmanagedObject = new MessagesListObject();
            cache.put(realmObject, new CacheData(currentDepth, unmanagedObject));
        } else if (currentDepth >= cachedObject.minDepth) {
            return (MessagesListObject) cachedObject.object;
        } else {
            unmanagedObject = (MessagesListObject) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        MessagesListObjectRealmProxyInterface unmanagedCopy = unmanagedObject;
        MessagesListObjectRealmProxyInterface realmSource = realmObject;
        unmanagedCopy.realmSet$_id(realmSource.realmGet$_id());
        unmanagedCopy.realmSet$unique_id(realmSource.realmGet$unique_id());
        unmanagedCopy.realmSet$Address(realmSource.realmGet$Address());
        unmanagedCopy.realmSet$ApiKey(realmSource.realmGet$ApiKey());
        unmanagedCopy.realmSet$count(realmSource.realmGet$count());
        unmanagedCopy.realmSet$Bio(realmSource.realmGet$Bio());
        unmanagedCopy.realmSet$City(realmSource.realmGet$City());
        unmanagedCopy.realmSet$Code(realmSource.realmGet$Code());
        unmanagedCopy.realmSet$DeviceToken(realmSource.realmGet$DeviceToken());
        unmanagedCopy.realmSet$DOB(realmSource.realmGet$DOB());
        unmanagedCopy.realmSet$Email(realmSource.realmGet$Email());
        unmanagedCopy.realmSet$FirstName(realmSource.realmGet$FirstName());
        unmanagedCopy.realmSet$Gender(realmSource.realmGet$Gender());
        unmanagedCopy.realmSet$Languages(realmSource.realmGet$Languages());
        unmanagedCopy.realmSet$LastName(realmSource.realmGet$LastName());
        unmanagedCopy.realmSet$Mobile(realmSource.realmGet$Mobile());
        unmanagedCopy.realmSet$ProfilePic(realmSource.realmGet$ProfilePic());
        unmanagedCopy.realmSet$Qualification(realmSource.realmGet$Qualification());
        unmanagedCopy.realmSet$Speciality(realmSource.realmGet$Speciality());
        unmanagedCopy.realmSet$State(realmSource.realmGet$State());
        unmanagedCopy.realmSet$UserType(realmSource.realmGet$UserType());
        unmanagedCopy.realmSet$GroupId(realmSource.realmGet$GroupId());
        unmanagedCopy.realmSet$sessionData(realmSource.realmGet$sessionData());
        unmanagedCopy.realmSet$TokenData(realmSource.realmGet$TokenData());
        unmanagedCopy.realmSet$type(realmSource.realmGet$type());
        unmanagedCopy.realmSet$Name(realmSource.realmGet$Name());
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$chatList(null);
        } else {
            RealmList<ChatMessage> managedchatListList = realmSource.realmGet$chatList();
            RealmList<ChatMessage> unmanagedchatListList = new RealmList();
            unmanagedCopy.realmSet$chatList(unmanagedchatListList);
            int nextDepth = currentDepth + 1;
            int size = managedchatListList.size();
            for (int i = 0; i < size; i++) {
                unmanagedchatListList.add(ChatMessageRealmProxy.createDetachedCopy((ChatMessage) managedchatListList.get(i), nextDepth, maxDepth, cache));
            }
        }
        return unmanagedObject;
    }

    static MessagesListObject update(Realm realm, MessagesListObject realmObject, MessagesListObject newObject, Map<RealmModel, RealmObjectProxy> cache) {
        MessagesListObjectRealmProxyInterface realmObjectTarget = realmObject;
        MessagesListObjectRealmProxyInterface realmObjectSource = newObject;
        realmObjectTarget.realmSet$_id(realmObjectSource.realmGet$_id());
        realmObjectTarget.realmSet$Address(realmObjectSource.realmGet$Address());
        realmObjectTarget.realmSet$ApiKey(realmObjectSource.realmGet$ApiKey());
        realmObjectTarget.realmSet$count(realmObjectSource.realmGet$count());
        realmObjectTarget.realmSet$Bio(realmObjectSource.realmGet$Bio());
        realmObjectTarget.realmSet$City(realmObjectSource.realmGet$City());
        realmObjectTarget.realmSet$Code(realmObjectSource.realmGet$Code());
        realmObjectTarget.realmSet$DeviceToken(realmObjectSource.realmGet$DeviceToken());
        realmObjectTarget.realmSet$DOB(realmObjectSource.realmGet$DOB());
        realmObjectTarget.realmSet$Email(realmObjectSource.realmGet$Email());
        realmObjectTarget.realmSet$FirstName(realmObjectSource.realmGet$FirstName());
        realmObjectTarget.realmSet$Gender(realmObjectSource.realmGet$Gender());
        realmObjectTarget.realmSet$Languages(realmObjectSource.realmGet$Languages());
        realmObjectTarget.realmSet$LastName(realmObjectSource.realmGet$LastName());
        realmObjectTarget.realmSet$Mobile(realmObjectSource.realmGet$Mobile());
        realmObjectTarget.realmSet$ProfilePic(realmObjectSource.realmGet$ProfilePic());
        realmObjectTarget.realmSet$Qualification(realmObjectSource.realmGet$Qualification());
        realmObjectTarget.realmSet$Speciality(realmObjectSource.realmGet$Speciality());
        realmObjectTarget.realmSet$State(realmObjectSource.realmGet$State());
        realmObjectTarget.realmSet$UserType(realmObjectSource.realmGet$UserType());
        realmObjectTarget.realmSet$GroupId(realmObjectSource.realmGet$GroupId());
        realmObjectTarget.realmSet$sessionData(realmObjectSource.realmGet$sessionData());
        realmObjectTarget.realmSet$TokenData(realmObjectSource.realmGet$TokenData());
        realmObjectTarget.realmSet$type(realmObjectSource.realmGet$type());
        realmObjectTarget.realmSet$Name(realmObjectSource.realmGet$Name());
        RealmList<ChatMessage> chatListList = realmObjectSource.realmGet$chatList();
        RealmList<ChatMessage> chatListRealmList = realmObjectTarget.realmGet$chatList();
        chatListRealmList.clear();
        if (chatListList != null) {
            for (int i = 0; i < chatListList.size(); i++) {
                ChatMessage chatListItem = (ChatMessage) chatListList.get(i);
                RealmModel cachechatList = (ChatMessage) cache.get(chatListItem);
                if (cachechatList != null) {
                    chatListRealmList.add(cachechatList);
                } else {
                    chatListRealmList.add(ChatMessageRealmProxy.copyOrUpdate(realm, chatListItem, true, cache));
                }
            }
        }
        return realmObject;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("MessagesListObject = proxy[");
        stringBuilder.append("{_id:");
        stringBuilder.append(realmGet$_id() != null ? realmGet$_id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{unique_id:");
        stringBuilder.append(realmGet$unique_id() != null ? realmGet$unique_id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Address:");
        stringBuilder.append(realmGet$Address() != null ? realmGet$Address() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ApiKey:");
        stringBuilder.append(realmGet$ApiKey() != null ? realmGet$ApiKey() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{count:");
        stringBuilder.append(realmGet$count());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Bio:");
        stringBuilder.append(realmGet$Bio() != null ? realmGet$Bio() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{City:");
        stringBuilder.append(realmGet$City() != null ? realmGet$City() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Code:");
        stringBuilder.append(realmGet$Code() != null ? realmGet$Code() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{DeviceToken:");
        stringBuilder.append(realmGet$DeviceToken() != null ? realmGet$DeviceToken() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{DOB:");
        stringBuilder.append(realmGet$DOB() != null ? realmGet$DOB() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Email:");
        stringBuilder.append(realmGet$Email() != null ? realmGet$Email() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{FirstName:");
        stringBuilder.append(realmGet$FirstName() != null ? realmGet$FirstName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Gender:");
        stringBuilder.append(realmGet$Gender() != null ? realmGet$Gender() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Languages:");
        stringBuilder.append(realmGet$Languages() != null ? realmGet$Languages() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{LastName:");
        stringBuilder.append(realmGet$LastName() != null ? realmGet$LastName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Mobile:");
        stringBuilder.append(realmGet$Mobile() != null ? realmGet$Mobile() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ProfilePic:");
        stringBuilder.append(realmGet$ProfilePic() != null ? realmGet$ProfilePic() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Qualification:");
        stringBuilder.append(realmGet$Qualification() != null ? realmGet$Qualification() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Speciality:");
        stringBuilder.append(realmGet$Speciality() != null ? realmGet$Speciality() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{State:");
        stringBuilder.append(realmGet$State() != null ? realmGet$State() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{UserType:");
        stringBuilder.append(realmGet$UserType());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{GroupId:");
        stringBuilder.append(realmGet$GroupId() != null ? realmGet$GroupId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sessionData:");
        stringBuilder.append(realmGet$sessionData() != null ? realmGet$sessionData() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{TokenData:");
        stringBuilder.append(realmGet$TokenData() != null ? realmGet$TokenData() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{Name:");
        stringBuilder.append(realmGet$Name() != null ? realmGet$Name() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{chatList:");
        stringBuilder.append("RealmList<ChatMessage>[").append(realmGet$chatList().size()).append("]");
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
        MessagesListObjectRealmProxy aMessagesListObject = (MessagesListObjectRealmProxy) o;
        String path = this.proxyState.getRealm$realm().getPath();
        String otherPath = aMessagesListObject.proxyState.getRealm$realm().getPath();
        if (path == null ? otherPath != null : !path.equals(otherPath)) {
            return false;
        }
        String tableName = this.proxyState.getRow$realm().getTable().getName();
        String otherTableName = aMessagesListObject.proxyState.getRow$realm().getTable().getName();
        if (tableName == null ? otherTableName != null : !tableName.equals(otherTableName)) {
            return false;
        }
        if (this.proxyState.getRow$realm().getIndex() != aMessagesListObject.proxyState.getRow$realm().getIndex()) {
            return false;
        }
        return true;
    }
}
