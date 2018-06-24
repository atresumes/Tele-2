package com.trigma.tiktok.model;

public class DrStaffListObject {
    private String AcceptReject_Id;
    private String ApiKey;
    private String Code;
    private String Designation;
    private String DeviceToken;
    private String Email;
    private String Gender;
    private String Mobile;
    private String Name;
    private String ProfilePic;
    private String StaffCode;
    private String StaffId;
    private int StaffStatus;

    public String getAcceptRejectId() {
        return this.AcceptReject_Id;
    }

    public void setAcceptRejectId(String AcceptReject_Id) {
        this.AcceptReject_Id = AcceptReject_Id;
    }

    public int getStaffStatus() {
        return this.StaffStatus;
    }

    public void setStaffStatus(int StaffStatus) {
        this.StaffStatus = StaffStatus;
    }

    public String getApiKey() {
        return this.ApiKey;
    }

    public void setApiKey(String ApiKey) {
        this.ApiKey = ApiKey;
    }

    public String getStaffId() {
        return this.StaffId;
    }

    public void setStaffId(String StaffId) {
        this.StaffId = StaffId;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getProfilePic() {
        return this.ProfilePic;
    }

    public void setProfilePic(String ProfilePic) {
        this.ProfilePic = ProfilePic;
    }

    public String getStaffCode() {
        return this.StaffCode;
    }

    public void setStaffCode(String StaffCode) {
        this.StaffCode = StaffCode;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDesignation() {
        return this.Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getMobile() {
        return this.Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getCode() {
        return this.Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getDeviceToken() {
        return this.DeviceToken;
    }

    public void setDeviceToken(String DeviceToken) {
        this.DeviceToken = DeviceToken;
    }
}
