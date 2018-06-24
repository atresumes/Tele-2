package com.trigma.tiktok.model;

public class DocUserDetailResponse {
    public DocUserDetail data;
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

    public DocUserDetail getData() {
        return this.data;
    }

    public void setData(DocUserDetail data) {
        this.data = data;
    }
}
