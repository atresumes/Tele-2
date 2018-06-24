package com.trigma.tiktok.model;

public class Upcoming {
    private String Age;
    private String BookingId;
    private String Dr_id;
    private String From;
    private PatientId PatientId;
    private String Schedule;
    private String ScheduleDate;
    private String SortDate;
    private String SortDatetime;
    private int Status;
    private String To;
    private String drschedulesetsId;
    private String profile;

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

    public String getDrId() {
        return this.Dr_id;
    }

    public void setDrId(String Dr_id) {
        this.Dr_id = Dr_id;
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

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
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

    public PatientId getPatientId() {
        return this.PatientId;
    }

    public void setPatientId(PatientId PatientId) {
        this.PatientId = PatientId;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAge() {
        return this.Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getScheduleDate() {
        return this.ScheduleDate;
    }

    public void setScheduleDate(String ScheduleDate) {
        this.ScheduleDate = ScheduleDate;
    }
}
