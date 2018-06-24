package com.trigma.tiktok.model;

public class BookingPatientObject {
    private String DrId;
    private int DrPush;
    private String From;
    private String PatientId;
    private String PatientName;
    private int PatientPush;
    private String Schedule;
    private String SortDate;
    private int Status;
    private int StatusDr;
    private int StatusDrSchedule;
    private int StatusPatient;
    private int StatusPatientSchedule;
    private String To;
    private String _id;
    private String created_at;

    public String getSchedule() {
        return this.Schedule;
    }

    public void setSchedule(String Schedule) {
        this.Schedule = Schedule;
    }

    public String getSortDate() {
        return this.SortDate;
    }

    public void setSortDate(String SortDate) {
        this.SortDate = SortDate;
    }

    public String getPatientId() {
        return this.PatientId;
    }

    public void setPatientId(String PatientId) {
        this.PatientId = PatientId;
    }

    public String getPatientName() {
        return this.PatientName;
    }

    public void setPatientName(String PatientName) {
        this.PatientName = PatientName;
    }

    public String getDrId() {
        return this.DrId;
    }

    public void setDrId(String DrId) {
        this.DrId = DrId;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public int getStatusPatient() {
        return this.StatusPatient;
    }

    public void setStatusPatient(int StatusPatient) {
        this.StatusPatient = StatusPatient;
    }

    public int getStatusPatientSchedule() {
        return this.StatusPatientSchedule;
    }

    public void setStatusPatientSchedule(int StatusPatientSchedule) {
        this.StatusPatientSchedule = StatusPatientSchedule;
    }

    public int getStatusDr() {
        return this.StatusDr;
    }

    public void setStatusDr(int StatusDr) {
        this.StatusDr = StatusDr;
    }

    public int getStatusDrSchedule() {
        return this.StatusDrSchedule;
    }

    public void setStatusDrSchedule(int StatusDrSchedule) {
        this.StatusDrSchedule = StatusDrSchedule;
    }

    public int getDrPush() {
        return this.DrPush;
    }

    public void setDrPush(int DrPush) {
        this.DrPush = DrPush;
    }

    public int getPatientPush() {
        return this.PatientPush;
    }

    public void setPatientPush(int PatientPush) {
        this.PatientPush = PatientPush;
    }

    public String getTo() {
        return this.To;
    }

    public void setTo(String To) {
        this.To = To;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getCreatedAt() {
        return this.created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
