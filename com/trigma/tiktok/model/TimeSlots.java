package com.trigma.tiktok.model;

public class TimeSlots {
    public String From;
    public String To;

    public TimeSlots(String From, String To) {
        this.From = From;
        this.To = To;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String getTo() {
        return this.To;
    }

    public void setTo(String to) {
        this.To = to;
    }
}
