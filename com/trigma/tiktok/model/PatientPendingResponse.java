package com.trigma.tiktok.model;

import java.util.ArrayList;

public class PatientPendingResponse {
    public ArrayList<PatientPendingData> data = new ArrayList();
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

    public ArrayList<PatientPendingData> getData() {
        return this.data;
    }

    public void setData(ArrayList<PatientPendingData> data) {
        this.data = data;
    }
}
