package com.trigma.tiktok.model;

public class LoginDetailPojo {
    public String DeviceToken;
    public String DeviceType;
    public String Email;
    public String LoginKey;
    public String LoginType;
    public String UserType;

    public String getLoginType() {
        return this.LoginType;
    }

    public void setLoginType(String loginType) {
        this.LoginType = loginType;
    }

    public String getLoginKey() {
        return this.LoginKey;
    }

    public void setLoginKey(String loginKey) {
        this.LoginKey = loginKey;
    }

    public String getUserType() {
        return this.UserType;
    }

    public void setUserType(String userType) {
        this.UserType = userType;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getDeviceToken() {
        return this.DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.DeviceToken = deviceToken;
    }

    public String getDeviceType() {
        return this.DeviceType;
    }

    public void setDeviceType(String deviceType) {
        this.DeviceType = deviceType;
    }
}
