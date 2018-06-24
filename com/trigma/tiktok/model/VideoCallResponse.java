package com.trigma.tiktok.model;

public class VideoCallResponse {
    public String GroupId;
    public String Redirecturl;
    public String Status;
    public String Url;
    public String apikey;
    public String error;
    public String inviteurl;
    public String session;
    public int status;
    public String token;

    public String getRedirecturl() {
        return this.Redirecturl;
    }

    public void setRedirecturl(String redirecturl) {
        this.Redirecturl = redirecturl;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String groupId) {
        this.GroupId = groupId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return this.Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public String getApikey() {
        return this.apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getInviteurl() {
        return this.inviteurl;
    }

    public void setInviteurl(String inviteurl) {
        this.inviteurl = inviteurl;
    }

    public String getSession() {
        return this.session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
