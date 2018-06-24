package com.trigma.tiktok.model;

public class BookingPojo {
    public String DrId;
    public int DrPush;
    public String From;
    public String PatientId;
    public String PatientName;
    public int PatientPush;
    public String Schedule = null;
    public String SortDate = null;
    public int Status;
    public int StatusDr;
    public int StatusDrSchedule;
    public int StatusPatient;
    public int StatusPatientSchedule;
    public String To;
    public String _id;
    public String created_at;

    public String getSchedule() {
        return this.Schedule;
    }

    public void setSchedule(String schedule) {
        this.Schedule = schedule;
    }

    public String getSortDate() {
        return this.SortDate;
    }

    public void setSortDate(String sortDate) {
        this.SortDate = sortDate;
    }

    public String getPatientId() {
        return this.PatientId;
    }

    public void setPatientId(String patientId) {
        this.PatientId = patientId;
    }

    public String getPatientName() {
        return this.PatientName;
    }

    public void setPatientName(String patientName) {
        this.PatientName = patientName;
    }

    public String getDrId() {
        return this.DrId;
    }

    public void setDrId(String drId) {
        this.DrId = drId;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public int getStatusPatient() {
        return this.StatusPatient;
    }

    public void setStatusPatient(int statusPatient) {
        this.StatusPatient = statusPatient;
    }

    public int getStatusPatientSchedule() {
        return this.StatusPatientSchedule;
    }

    public void setStatusPatientSchedule(int statusPatientSchedule) {
        this.StatusPatientSchedule = statusPatientSchedule;
    }

    public int getStatusDr() {
        return this.StatusDr;
    }

    public void setStatusDr(int statusDr) {
        this.StatusDr = statusDr;
    }

    public int getStatusDrSchedule() {
        return this.StatusDrSchedule;
    }

    public void setStatusDrSchedule(int statusDrSchedule) {
        this.StatusDrSchedule = statusDrSchedule;
    }

    public int getDrPush() {
        return this.DrPush;
    }

    public void setDrPush(int drPush) {
        this.DrPush = drPush;
    }

    public int getPatientPush() {
        return this.PatientPush;
    }

    public void setPatientPush(int patientPush) {
        this.PatientPush = patientPush;
    }

    public String getTo() {
        return this.To;
    }

    public void setTo(String to) {
        this.To = to;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
