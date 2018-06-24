package com.trigma.tiktok.model;

public class DoctorChatTokenData {
    private String TokenData;
    private String apiKey;
    private String groupId;
    private String sessionData;

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

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
}
