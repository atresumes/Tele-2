package com.trigma.tiktok.model;

public class NotificationCountCall {
    public String Type;
    public String UserId;

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public String getType() {
        return this.Type;
    }

    public void setType(String type) {
        this.Type = type;
    }
}
