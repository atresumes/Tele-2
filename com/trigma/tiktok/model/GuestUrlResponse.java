package com.trigma.tiktok.model;

public class GuestUrlResponse {
    private GuestUrlObject data;
    private String error;
    private String status;

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GuestUrlObject getData() {
        return this.data;
    }

    public void setData(GuestUrlObject data) {
        this.data = data;
    }
}
