package com.trigma.tiktok.model;

import java.util.ArrayList;

public class PatientBookingObject {
    private ArrayList<PatientModelUpcomingOuter> past;
    private ArrayList<PatientModelUpcomingOuter> upcoming;

    public ArrayList<PatientModelUpcomingOuter> getUpcoming() {
        return this.upcoming;
    }

    public void setUpcoming(ArrayList<PatientModelUpcomingOuter> upcoming) {
        this.upcoming = upcoming;
    }

    public ArrayList<PatientModelUpcomingOuter> getPast() {
        return this.past;
    }

    public void setPast(ArrayList<PatientModelUpcomingOuter> past) {
        this.past = past;
    }
}
