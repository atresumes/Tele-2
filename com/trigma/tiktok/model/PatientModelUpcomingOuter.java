package com.trigma.tiktok.model;

public class PatientModelUpcomingOuter {
    private String BookingId;
    private PatientUpcomingPastModel DrId;
    private String From;
    private String Schedule;
    private String ScheduleDate;
    private String SortDate;
    private String SortDatetime;
    private String To;
    private String drschedulesetsId;
    private String profile;
    private int status;

    public String getDrschedulesetsId() {
        return this.drschedulesetsId;
    }

    public void setDrschedulesetsId(String drschedulesetsId) {
        this.drschedulesetsId = drschedulesetsId;
    }

    public String getBookingId() {
        return this.BookingId;
    }

    public void setBookingId(String BookingId) {
        this.BookingId = BookingId;
    }

    public String getSortDate() {
        return this.SortDate;
    }

    public void setSortDate(String SortDate) {
        this.SortDate = SortDate;
    }

    public String getSortDatetime() {
        return this.SortDatetime;
    }

    public void setSortDatetime(String SortDatetime) {
        this.SortDatetime = SortDatetime;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getTo() {
        return this.To;
    }

    public void setTo(String To) {
        this.To = To;
    }

    public String getSchedule() {
        return this.Schedule;
    }

    public void setSchedule(String Schedule) {
        this.Schedule = Schedule;
    }

    public PatientUpcomingPastModel getDrId() {
        return this.DrId;
    }

    public void setDrId(PatientUpcomingPastModel DrId) {
        this.DrId = DrId;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getScheduleDate() {
        return this.ScheduleDate;
    }

    public void setScheduleDate(String ScheduleDate) {
        this.ScheduleDate = ScheduleDate;
    }
}
