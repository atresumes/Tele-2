package com.trigma.tiktok.model;

public class NotificationMainObject {
    private String Date;
    private String DaysLeft;
    private NotiDrDeatils DrDeatils;
    private String Message;
    private String profile;

    public String getDate() {
        return this.Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getDaysLeft() {
        return this.DaysLeft;
    }

    public void setDaysLeft(String DaysLeft) {
        this.DaysLeft = DaysLeft;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public NotiDrDeatils getDrDeatils() {
        return this.DrDeatils;
    }

    public void setDrDeatils(NotiDrDeatils DrDeatils) {
        this.DrDeatils = DrDeatils;
    }
}
