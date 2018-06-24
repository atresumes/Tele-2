package com.trigma.tiktok.model;

public class SchedulePojo {
    public String MeetingSlot;
    public int Status;
    public TimeSlots Time;
    public String _id;

    public String getMeetingSlot() {
        return this.MeetingSlot;
    }

    public void setMeetingSlot(String meetingSlot) {
        this.MeetingSlot = meetingSlot;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
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
