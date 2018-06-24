package com.trigma.tiktok.model;

import java.util.ArrayList;

public class DrDateResponse {
    public ArrayList<DrDatePojo> data;
    public String error;
    public String status;

    public ArrayList<DrDatePojo> getData() {
        return this.data;
    }

    public void setData(ArrayList<DrDatePojo> data) {
        this.data = data;
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
}
