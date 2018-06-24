package com.trigma.tiktok.model;

public class DrDatePojo {
    public String MeetingSlot;
    public TimeSlots Time;
    public String _id;

    public String getMeetingSlot() {
        return this.MeetingSlot;
    }

    public void setMeetingSlot(String meetingSlot) {
        this.MeetingSlot = meetingSlot;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public TimeSlots getTime() {
        return this.Time;
    }

    public void setTime(TimeSlots time) {
        this.Time = time;
    }
}
