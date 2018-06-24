package com.trigma.tiktok.model;

public class NotificationCountResponse {
    public int Messagedotshow = 0;
    public int ScheduleCount = 0;
    public int count = 0;
    public int requestCount = 0;
    public int requestcount = 0;
    public int scheduleCountAppointment = 0;
    public String status;

    public int getMessagedotshow() {
        return this.Messagedotshow;
    }

    public void setMessagedotshow(int messagedotshow) {
        this.Messagedotshow = messagedotshow;
    }

    public int getScheduleCount() {
        return this.ScheduleCount;
    }

    public void setScheduleCount(int scheduleCount) {
        this.ScheduleCount = scheduleCount;
    }

    public int getRequestCount() {
        return this.requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRequestcount() {
        return this.requestcount;
    }

    public void setRequestcount(int intrequestcount) {
        this.requestcount = intrequestcount;
    }

    public int getScheduleCountAppointment() {
        return this.scheduleCountAppointment;
    }

    public void setScheduleCountAppointment(int scheduleCountAppointment) {
        this.scheduleCountAppointment = scheduleCountAppointment;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
