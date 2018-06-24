package io.realm;

public interface ChatMessageRealmProxyInterface {
    boolean realmGet$isSameUser();

    String realmGet$link();

    String realmGet$messageText();

    String realmGet$name();

    String realmGet$type();

    int realmGet$user_type();

    void realmSet$isSameUser(boolean z);

    void realmSet$link(String str);

    void realmSet$messageText(String str);

    void realmSet$name(String str);

    void realmSet$type(String str);

    void realmSet$user_type(int i);
}
