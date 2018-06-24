package com.trigma.tiktok.model;

import java.util.ArrayList;

public class MyDoctorsResponse {
    private ArrayList<MyDoctorsObject> data;
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

    public ArrayList<MyDoctorsObject> getData() {
        return this.data;
    }

    public void setData(ArrayList<MyDoctorsObject> data) {
        this.data = data;
    }
}
