package com.trigma.tiktok.model;

import java.util.ArrayList;

public class CurrentScheduleResponce {
    public ArrayList<GetSListPojo> data;
    public String error = "";
    public String status = "";

    public ArrayList<GetSListPojo> getData() {
        return this.data;
    }

    public void setData(ArrayList<GetSListPojo> data) {
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
