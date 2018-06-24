package com.trigma.tiktok.model;

import java.util.ArrayList;

public class DrAppointmentDate {
    public ArrayList<DrAppointmentDateData> data;
    public String error = "";
    public ArrayList<DrAppointmentDateData> records;
    public String status = "";

    public ArrayList<DrAppointmentDateData> getData() {
        return this.data;
    }

    public void setData(ArrayList<DrAppointmentDateData> data) {
        this.data = data;
    }

    public ArrayList<DrAppointmentDateData> getRecords() {
        return this.records;
    }

    public void setRecords(ArrayList<DrAppointmentDateData> records) {
        this.records = records;
    }

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
}
