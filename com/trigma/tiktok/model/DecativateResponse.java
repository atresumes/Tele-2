package com.trigma.tiktok.model;

public class DecativateResponse {
    public String DrType;
    public String status;
    public int userStatus;

    public String getDrType() {
        return this.DrType;
    }

    public void setDrType(String drType) {
        this.DrType = drType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
