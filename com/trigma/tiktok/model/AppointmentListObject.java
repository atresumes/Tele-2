package com.trigma.tiktok.model;

import java.util.ArrayList;

public class AppointmentListObject {
    private String BookId;
    private ArrayList<BookingPatientObject> Booking;
    private String Date;
    private String Dr_id;
    private String Month;
    private ArrayList<SchedulePojo> Schedule;
    private ArrayList<String> Sloat;
    private String Year;
    private String sortdate;

    public String getBookId() {
        return this.BookId;
    }

    public void setBookId(String BookId) {
        this.BookId = BookId;
    }

    public String getDrId() {
        return this.Dr_id;
    }

    public void setDrId(String Dr_id) {
        this.Dr_id = Dr_id;
    }

    public String getYear() {
        return this.Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public String getMonth() {
        return this.Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getSortdate() {
        return this.sortdate;
    }

    public void setSortdate(String sortdate) {
        this.sortdate = sortdate;
    }

    public ArrayList<SchedulePojo> getSchedule() {
        return this.Schedule;
    }

    public void setSchedule(ArrayList<SchedulePojo> Schedule) {
        this.Schedule = Schedule;
    }

    public ArrayList<BookingPatientObject> getBooking() {
        return this.Booking;
    }

    public void setBooking(ArrayList<BookingPatientObject> Booking) {
        this.Booking = Booking;
    }

    public ArrayList<String> getSloat() {
        return this.Sloat;
    }

    public void setSloat(ArrayList<String> Sloat) {
        this.Sloat = Sloat;
    }
}
