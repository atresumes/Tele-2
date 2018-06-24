package com.trigma.tiktok.model;

public class DrSearchCodeResponse {
    private DrSearchCodeObject data;
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

    public DrSearchCodeObject getData() {
        return this.data;
    }

    public void setData(DrSearchCodeObject data) {
        this.data = data;
    }
}
