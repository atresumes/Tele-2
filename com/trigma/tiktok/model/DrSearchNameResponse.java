package com.trigma.tiktok.model;

import java.util.ArrayList;

public class DrSearchNameResponse {
    private ArrayList<DrSearchNameObject> data;
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

    public ArrayList<DrSearchNameObject> getData() {
        return this.data;
    }

    public void setData(ArrayList<DrSearchNameObject> data) {
        this.data = data;
    }
}
