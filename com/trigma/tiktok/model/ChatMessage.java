package com.trigma.tiktok.model;

import io.realm.ChatMessageRealmProxyInterface;
import io.realm.RealmObject;
import io.realm.internal.RealmObjectProxy;

public class ChatMessage extends RealmObject implements ChatMessageRealmProxyInterface {
    public boolean isSameUser;
    public String link;
    public String messageText;
    public String name;
    public String type;
    public int user_type;

    public boolean realmGet$isSameUser() {
        return this.isSameUser;
    }

    public String realmGet$link() {
        return this.link;
    }

    public String realmGet$messageText() {
        return this.messageText;
    }

    public String realmGet$name() {
        return this.name;
    }

    public String realmGet$type() {
        return this.type;
    }

    public int realmGet$user_type() {
        return this.user_type;
    }

    public void realmSet$isSameUser(boolean z) {
        this.isSameUser = z;
    }

    public void realmSet$link(String str) {
        this.link = str;
    }

    public void realmSet$messageText(String str) {
        this.messageText = str;
    }

    public void realmSet$name(String str) {
        this.name = str;
    }

    public void realmSet$type(String str) {
        this.type = str;
    }

    public void realmSet$user_type(int i) {
        this.user_type = i;
    }

    public ChatMessage() {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$user_type(0);
        realmSet$isSameUser(false);
    }

    public int getUser_type() {
        return realmGet$user_type();
    }

    public void setUser_type(int user_type) {
        realmSet$user_type(user_type);
    }

    public String getType() {
        return realmGet$type();
    }

    public void setType(String type) {
        realmSet$type(type);
    }

    public String getMessageText() {
        return realmGet$messageText();
    }

    public void setMessageText(String messageText) {
        realmSet$messageText(messageText);
    }

    public boolean isSameUser() {
        return realmGet$isSameUser();
    }

    public void setSameUser(boolean sameUser) {
        realmSet$isSameUser(sameUser);
    }

    public String getLink() {
        return realmGet$link();
    }

    public void setLink(String link) {
        realmSet$link(link);
    }

    public String getName() {
        return realmGet$name();
    }

    public void setName(String name) {
        realmSet$name(name);
    }
}
