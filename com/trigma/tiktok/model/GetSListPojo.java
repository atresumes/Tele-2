package com.trigma.tiktok.model;

public class GetSListPojo {
    public String Month;
    public String Month_id;
    public String Year;
    public String day;
    public OuterSchedulePojo schedule;
    public String sortdate;

    public OuterSchedulePojo getSchedule() {
        return this.schedule;
    }

    public void setSchedule(OuterSchedulePojo schedule) {
        this.schedule = schedule;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return this.Month;
    }

    public void setMonth(String month) {
        this.Month = month;
    }

    public String getSortdate() {
        return this.sortdate;
    }

    public void setSortdate(String sortdate) {
        this.sortdate = sortdate;
    }

    public String getMonth_id() {
        return this.Month_id;
    }

    public void setMonth_id(String month_id) {
        this.Month_id = month_id;
    }

    public String getYear() {
        return this.Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }
}
