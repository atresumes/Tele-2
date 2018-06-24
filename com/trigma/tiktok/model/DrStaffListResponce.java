package com.trigma.tiktok.model;

import java.util.ArrayList;

public class DrStaffListResponce {
    private ArrayList<DrStaffListObject> data;
    public String error;
    private int limit = 0;
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

    public ArrayList<DrStaffListObject> getData() {
        return this.data;
    }

    public void setData(ArrayList<DrStaffListObject> data) {
        this.data = data;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
