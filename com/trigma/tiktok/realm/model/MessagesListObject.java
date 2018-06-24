package com.trigma.tiktok.realm.model;

import com.trigma.tiktok.model.ChatMessage;
import io.realm.MessagesListObjectRealmProxyInterface;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;

public class MessagesListObject extends RealmObject implements MessagesListObjectRealmProxyInterface {
    public String Address;
    public String ApiKey;
    public String Bio;
    public String City;
    public String Code;
    public String DOB;
    public String DeviceToken;
    public String Email;
    public String FirstName;
    public String Gender;
    public String GroupId;
    public String Languages;
    public String LastName;
    public String Mobile;
    public String Name;
    public String ProfilePic;
    public String Qualification;
    public String Speciality;
    public String State;
    public String TokenData;
    public int UserType;
    public String _id;
    private RealmList<ChatMessage> chatList;
    public int count;
    public String sessionData;
    public int type;
    @PrimaryKey
    public String unique_id;

    public String realmGet$Address() {
        return this.Address;
    }

    public String realmGet$ApiKey() {
        return this.ApiKey;
    }

    public String realmGet$Bio() {
        return this.Bio;
    }

    public String realmGet$City() {
        return this.City;
    }

    public String realmGet$Code() {
        return this.Code;
    }

    public String realmGet$DOB() {
        return this.DOB;
    }

    public String realmGet$DeviceToken() {
        return this.DeviceToken;
    }

    public String realmGet$Email() {
        return this.Email;
    }

    public String realmGet$FirstName() {
        return this.FirstName;
    }

    public String realmGet$Gender() {
        return this.Gender;
    }

    public String realmGet$GroupId() {
        return this.GroupId;
    }

    public String realmGet$Languages() {
        return this.Languages;
    }

    public String realmGet$LastName() {
        return this.LastName;
    }

    public String realmGet$Mobile() {
        return this.Mobile;
    }

    public String realmGet$Name() {
        return this.Name;
    }

    public String realmGet$ProfilePic() {
        return this.ProfilePic;
    }

    public String realmGet$Qualification() {
        return this.Qualification;
    }

    public String realmGet$Speciality() {
        return this.Speciality;
    }

    public String realmGet$State() {
        return this.State;
    }

    public String realmGet$TokenData() {
        return this.TokenData;
    }

    public int realmGet$UserType() {
        return this.UserType;
    }

    public String realmGet$_id() {
        return this._id;
    }

    public RealmList realmGet$chatList() {
        return this.chatList;
    }

    public int realmGet$count() {
        return this.count;
    }

    public String realmGet$sessionData() {
        return this.sessionData;
    }

    public int realmGet$type() {
        return this.type;
    }

    public String realmGet$unique_id() {
        return this.unique_id;
    }

    public void realmSet$Address(String str) {
        this.Address = str;
    }

    public void realmSet$ApiKey(String str) {
        this.ApiKey = str;
    }

    public void realmSet$Bio(String str) {
        this.Bio = str;
    }

    public void realmSet$City(String str) {
        this.City = str;
    }

    public void realmSet$Code(String str) {
        this.Code = str;
    }

    public void realmSet$DOB(String str) {
        this.DOB = str;
    }

    public void realmSet$DeviceToken(String str) {
        this.DeviceToken = str;
    }

    public void realmSet$Email(String str) {
        this.Email = str;
    }

    public void realmSet$FirstName(String str) {
        this.FirstName = str;
    }

    public void realmSet$Gender(String str) {
        this.Gender = str;
    }

    public void realmSet$GroupId(String str) {
        this.GroupId = str;
    }

    public void realmSet$Languages(String str) {
        this.Languages = str;
    }

    public void realmSet$LastName(String str) {
        this.LastName = str;
    }

    public void realmSet$Mobile(String str) {
        this.Mobile = str;
    }

    public void realmSet$Name(String str) {
        this.Name = str;
    }

    public void realmSet$ProfilePic(String str) {
        this.ProfilePic = str;
    }

    public void realmSet$Qualification(String str) {
        this.Qualification = str;
    }

    public void realmSet$Speciality(String str) {
        this.Speciality = str;
    }

    public void realmSet$State(String str) {
        this.State = str;
    }

    public void realmSet$TokenData(String str) {
        this.TokenData = str;
    }

    public void realmSet$UserType(int i) {
        this.UserType = i;
    }

    public void realmSet$_id(String str) {
        this._id = str;
    }

    public void realmSet$chatList(RealmList realmList) {
        this.chatList = realmList;
    }

    public void realmSet$count(int i) {
        this.count = i;
    }

    public void realmSet$sessionData(String str) {
        this.sessionData = str;
    }

    public void realmSet$type(int i) {
        this.type = i;
    }

    public void realmSet$unique_id(String str) {
        this.unique_id = str;
    }

    public MessagesListObject() {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$chatList(new RealmList());
    }

    public String getUnique_id() {
        return realmGet$unique_id();
    }

    public void setUnique_id(String unique_id) {
        realmSet$unique_id(unique_id);
    }

    public RealmList<ChatMessage> getChatList() {
        return realmGet$chatList();
    }

    public void setChatList(RealmList<ChatMessage> chatList) {
        realmSet$chatList(chatList);
    }

    public String get_id() {
        return realmGet$_id();
    }

    public void set_id(String _id) {
        realmSet$_id(_id);
    }

    public String getAddress() {
        return realmGet$Address();
    }

    public void setAddress(String address) {
        realmSet$Address(address);
    }

    public String getApiKey() {
        return realmGet$ApiKey();
    }

    public void setApiKey(String apiKey) {
        realmSet$ApiKey(apiKey);
    }

    public int getCount() {
        return realmGet$count();
    }

    public void setCount(int count) {
        realmSet$count(count);
    }

    public String getBio() {
        return realmGet$Bio();
    }

    public void setBio(String bio) {
        realmSet$Bio(bio);
    }

    public String getCity() {
        return realmGet$City();
    }

    public void setCity(String city) {
        realmSet$City(city);
    }

    public String getCode() {
        return realmGet$Code();
    }

    public void setCode(String code) {
        realmSet$Code(code);
    }

    public String getDeviceToken() {
        return realmGet$DeviceToken();
    }

    public void setDeviceToken(String deviceToken) {
        realmSet$DeviceToken(deviceToken);
    }

    public String getDOB() {
        return realmGet$DOB();
    }

    public void setDOB(String DOB) {
        realmSet$DOB(DOB);
    }

    public String getEmail() {
        return realmGet$Email();
    }

    public void setEmail(String email) {
        realmSet$Email(email);
    }

    public String getFirstName() {
        return realmGet$FirstName();
    }

    public void setFirstName(String firstName) {
        realmSet$FirstName(firstName);
    }

    public String getGender() {
        return realmGet$Gender();
    }

    public void setGender(String gender) {
        realmSet$Gender(gender);
    }

    public String getLanguages() {
        return realmGet$Languages();
    }

    public void setLanguages(String languages) {
        realmSet$Languages(languages);
    }

    public String getLastName() {
        return realmGet$LastName();
    }

    public void setLastName(String lastName) {
        realmSet$LastName(lastName);
    }

    public String getMobile() {
        return realmGet$Mobile();
    }

    public void setMobile(String mobile) {
        realmSet$Mobile(mobile);
    }

    public String getProfilePic() {
        return realmGet$ProfilePic();
    }

    public void setProfilePic(String profilePic) {
        realmSet$ProfilePic(profilePic);
    }

    public String getQualification() {
        return realmGet$Qualification();
    }

    public void setQualification(String qualification) {
        realmSet$Qualification(qualification);
    }

    public String getSpeciality() {
        return realmGet$Speciality();
    }

    public void setSpeciality(String speciality) {
        realmSet$Speciality(speciality);
    }

    public String getState() {
        return realmGet$State();
    }

    public void setState(String state) {
        realmSet$State(state);
    }

    public int getUserType() {
        return realmGet$UserType();
    }

    public void setUserType(int userType) {
        realmSet$UserType(userType);
    }

    public String getGroupId() {
        return realmGet$GroupId();
    }

    public void setGroupId(String groupId) {
        realmSet$GroupId(groupId);
    }

    public String getSessionData() {
        return realmGet$sessionData();
    }

    public void setSessionData(String sessionData) {
        realmSet$sessionData(sessionData);
    }

    public String getTokenData() {
        return realmGet$TokenData();
    }

    public void setTokenData(String tokenData) {
        realmSet$TokenData(tokenData);
    }

    public int getType() {
        return realmGet$type();
    }

    public void setType(int type) {
        realmSet$type(type);
    }

    public String getName() {
        return realmGet$Name();
    }

    public void setName(String name) {
        realmSet$Name(name);
    }
}
