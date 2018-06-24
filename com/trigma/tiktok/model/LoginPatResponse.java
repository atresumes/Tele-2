package com.trigma.tiktok.model;

public class LoginPatResponse {
    public LoginPatData data;
    public String error;
    public String status;

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LoginPatData getData() {
        return this.data;
    }

    public void setData(LoginPatData data) {
        this.data = data;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
