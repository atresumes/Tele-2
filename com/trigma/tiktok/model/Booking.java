package com.trigma.tiktok.model;

import java.util.ArrayList;

public class Booking {
    private ArrayList<Past> past;
    private ArrayList<Upcoming> upcoming;

    public ArrayList<Upcoming> getUpcoming() {
        return this.upcoming;
    }

    public void setUpcoming(ArrayList<Upcoming> upcoming) {
        this.upcoming = upcoming;
    }

    public ArrayList<Past> getPast() {
        return this.past;
    }

    public void setPast(ArrayList<Past> past) {
        this.past = past;
    }
}
