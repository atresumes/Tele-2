package com.trigma.tiktok.model;

public class PateintDataPojo {
    public String DrId;
    public String DrPush;
    public String From;
    public PatientId PatientId;
    public String PatientName;
    public String PatientPush;
    public String Schedule;
    public String SortDate;
    public String Status;
    public String StatusDr;
    public String StatusDrSchedule;
    public String StatusPatient;
    public String StatusPatientSchedule;
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

    public String getStatusPatient() {
        return this.StatusPatient;
    }

    public void setStatusPatient(String statusPatient) {
        this.StatusPatient = statusPatient;
    }

    public String getStatusPatientSchedule() {
        return this.StatusPatientSchedule;
    }

    public void setStatusPatientSchedule(String statusPatientSchedule) {
        this.StatusPatientSchedule = statusPatientSchedule;
    }

    public String getStatusDr() {
        return this.StatusDr;
    }

    public void setStatusDr(String statusDr) {
        this.StatusDr = statusDr;
    }

    public String getStatusDrSchedule() {
        return this.StatusDrSchedule;
    }

    public void setStatusDrSchedule(String statusDrSchedule) {
        this.StatusDrSchedule = statusDrSchedule;
    }

    public String getDrPush() {
        return this.DrPush;
    }

    public void setDrPush(String drPush) {
        this.DrPush = drPush;
    }

    public String getPatientPush() {
        return this.PatientPush;
    }

    public void setPatientPush(String patientPush) {
        this.PatientPush = patientPush;
    }

    public String getTo() {
        return this.To;
    }

    public void setTo(String to) {
        this.To = to;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String status) {
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

    public PatientId getPatientId() {
        return this.PatientId;
    }

    public void setPatientId(PatientId patientId) {
        this.PatientId = patientId;
    }
}
