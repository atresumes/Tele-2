package com.trigma.tiktok.model;

import java.util.ArrayList;

public class DrSchedulePatientListResponce {
    public String error;
    public ArrayList<PateintDataPojoMain> patient = new ArrayList();
    public String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<PateintDataPojoMain> getPatient() {
        return this.patient;
    }

    public void setPatient(ArrayList<PateintDataPojoMain> patient) {
        this.patient = patient;
    }
}
