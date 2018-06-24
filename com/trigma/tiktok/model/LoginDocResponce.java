package com.trigma.tiktok.model;

public class LoginDocResponce {
    public LoginDocData data;
    public String error;
    public String status;

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LoginDocData getData() {
        return this.data;
    }

    public void setData(LoginDocData data) {
        this.data = data;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
