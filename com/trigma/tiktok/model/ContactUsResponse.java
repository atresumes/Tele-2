package com.trigma.tiktok.model;

public class ContactUsResponse {
    public ContactUsData data;
    public String error;
    public String status;

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

    public ContactUsData getData() {
        return this.data;
    }

    public void setData(ContactUsData data) {
        this.data = data;
    }
}
