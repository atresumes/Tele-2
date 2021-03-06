package com.trigma.tiktok.model;

import java.util.ArrayList;

public class DrAppointmentResponse {
    private ArrayList<BookingOuterAppointment> data;
    private String error = "";
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

    public ArrayList<BookingOuterAppointment> getData() {
        return this.data;
    }

    public void setData(ArrayList<BookingOuterAppointment> data) {
        this.data = data;
    }
}
