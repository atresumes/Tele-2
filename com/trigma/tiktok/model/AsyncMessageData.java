package com.trigma.tiktok.model;

public class AsyncMessageData {
    public String DisplayName;
    public String From;
    public String GroupId;
    public String message;
    public String messageId;
    public String messageType;

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String groupId) {
        this.GroupId = groupId;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getDisplayName() {
        return this.DisplayName;
    }

    public void setDisplayName(String displayName) {
        this.DisplayName = displayName;
    }
}
