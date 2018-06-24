package com.trigma.tiktok.model;

import com.facebook.appevents.AppEventsConstants;

public class ForceUpdate {
    public String AndroidFourceUpdate = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    public String Androidversion = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    public String error;
    public String status;
    public int token = 0;
    public String version;

    public String getAndroidversion() {
        return this.Androidversion;
    }

    public void setAndroidversion(String androidversion) {
        this.Androidversion = androidversion;
    }

    public String getAndroidFourceUpdate() {
        return this.AndroidFourceUpdate;
    }

    public void setAndroidFourceUpdate(String androidFourceUpdate) {
        this.AndroidFourceUpdate = androidFourceUpdate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getToken() {
        return this.token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
