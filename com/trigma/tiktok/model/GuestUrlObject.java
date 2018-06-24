package com.trigma.tiktok.model;

public class GuestUrlObject {
    private String GroupId;
    private String TokenData;
    private String apiKey;
    private String sessionData;

    public String getSessionData() {
        return this.sessionData;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }

    public String getTokenData() {
        return this.TokenData;
    }

    public void setTokenData(String TokenData) {
        this.TokenData = TokenData;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }
}
