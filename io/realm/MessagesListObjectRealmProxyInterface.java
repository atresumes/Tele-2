package io.realm;

import com.trigma.tiktok.model.ChatMessage;

public interface MessagesListObjectRealmProxyInterface {
    String realmGet$Address();

    String realmGet$ApiKey();

    String realmGet$Bio();

    String realmGet$City();

    String realmGet$Code();

    String realmGet$DOB();

    String realmGet$DeviceToken();

    String realmGet$Email();

    String realmGet$FirstName();

    String realmGet$Gender();

    String realmGet$GroupId();

    String realmGet$Languages();

    String realmGet$LastName();

    String realmGet$Mobile();

    String realmGet$Name();

    String realmGet$ProfilePic();

    String realmGet$Qualification();

    String realmGet$Speciality();

    String realmGet$State();

    String realmGet$TokenData();

    int realmGet$UserType();

    String realmGet$_id();

    RealmList<ChatMessage> realmGet$chatList();

    int realmGet$count();

    String realmGet$sessionData();

    int realmGet$type();

    String realmGet$unique_id();

    void realmSet$Address(String str);

    void realmSet$ApiKey(String str);

    void realmSet$Bio(String str);

    void realmSet$City(String str);

    void realmSet$Code(String str);

    void realmSet$DOB(String str);

    void realmSet$DeviceToken(String str);

    void realmSet$Email(String str);

    void realmSet$FirstName(String str);

    void realmSet$Gender(String str);

    void realmSet$GroupId(String str);

    void realmSet$Languages(String str);

    void realmSet$LastName(String str);

    void realmSet$Mobile(String str);

    void realmSet$Name(String str);

    void realmSet$ProfilePic(String str);

    void realmSet$Qualification(String str);

    void realmSet$Speciality(String str);

    void realmSet$State(String str);

    void realmSet$TokenData(String str);

    void realmSet$UserType(int i);

    void realmSet$_id(String str);

    void realmSet$chatList(RealmList<ChatMessage> realmList);

    void realmSet$count(int i);

    void realmSet$sessionData(String str);

    void realmSet$type(int i);

    void realmSet$unique_id(String str);
}
