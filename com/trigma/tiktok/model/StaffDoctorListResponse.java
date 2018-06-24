package com.trigma.tiktok.model;

import java.util.ArrayList;

public class StaffDoctorListResponse {
    private ArrayList<StaffDoctorListObject> data;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<StaffDoctorListObject> getData() {
        return this.data;
    }

    public void setData(ArrayList<StaffDoctorListObject> data) {
        this.data = data;
    }
}
