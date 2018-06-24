package com.trigma.tiktok.model;

public class AddStaffResponse {
    private int UserType;
    private String status;
    private String userId;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return this.UserType;
    }

    public void setUserType(int UserType) {
        this.UserType = UserType;
    }
}
