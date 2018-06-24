package com.trigma.tiktok.model;

public class VideoCallRequiredFields {
    public String DrName;
    public String GroupId;
    public String PatientEmail;
    public String PatientName = "";
    public String Schedule;
    public int User;
    public String bookingId;
    public String drschedulesetsId;
    public String patientId;
    public String url;

    public String getPatientName() {
        return this.PatientName;
    }

    public void setPatientName(String patientName) {
        this.PatientName = patientName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDrschedulesetsId() {
        return this.drschedulesetsId;
    }

    public void setDrschedulesetsId(String drschedulesetsId) {
        this.drschedulesetsId = drschedulesetsId;
    }

    public String getSchedule() {
        return this.Schedule;
    }

    public void setSchedule(String schedule) {
        this.Schedule = schedule;
    }

    public String getPatientEmail() {
        return this.PatientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.PatientEmail = patientEmail;
    }

    public String getDrName() {
        return this.DrName;
    }

    public void setDrName(String drName) {
        this.DrName = drName;
    }

    public int getUser() {
        return this.User;
    }

    public void setUser(int user) {
        this.User = user;
    }

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String groupId) {
        this.GroupId = groupId;
    }
}
