package com.trigma.tiktok.model;

import java.util.ArrayList;

public class PatientAppointmentResponse {
    private ArrayList<PatientBookingArray> data;
    public String error;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<PatientBookingArray> getData() {
        return this.data;
    }

    public void setData(ArrayList<PatientBookingArray> data) {
        this.data = data;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
