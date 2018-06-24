package com.trigma.tiktok.model;

import java.util.ArrayList;

public class AppointmentListResponse {
    public ArrayList<AppointmentListObject> data = new ArrayList();
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

    public ArrayList<AppointmentListObject> getData() {
        return this.data;
    }

    public void setData(ArrayList<AppointmentListObject> data) {
        this.data = data;
    }
}
