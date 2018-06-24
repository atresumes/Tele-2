package com.trigma.tiktok.model;

import java.util.ArrayList;

public class OuterSchedulePojo {
    public ArrayList<BookingPojo> Booking;
    public DatePojo Date;
    public String Dr_id;
    public ArrayList<SchedulePojo> Schedule;
    public String ScheduleDate;
    public int Status;
    public int __v;
    public String _id;
    public String created_at;

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getScheduleDate() {
        return this.ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.ScheduleDate = scheduleDate;
    }

    public String getDr_id() {
        return this.Dr_id;
    }

    public void setDr_id(String dr_id) {
        this.Dr_id = dr_id;
    }

    public int get__v() {
        return this.__v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public ArrayList<BookingPojo> getBooking() {
        return this.Booking;
    }

    public void setBooking(ArrayList<BookingPojo> booking) {
        this.Booking = booking;
    }

    public ArrayList<SchedulePojo> getSchedule() {
        return this.Schedule;
    }

    public void setSchedule(ArrayList<SchedulePojo> schedule) {
        this.Schedule = schedule;
    }

    public DatePojo getDate() {
        return this.Date;
    }

    public void setDate(DatePojo date) {
        this.Date = date;
    }
}
