package com.trigma.tiktok.model;

public class StaffDoctorListObject {
    private String ApiKey;
    private String Code;
    private String DeviceToken;
    private String DrId;
    private String Email;
    private String Gender;
    private String GroupId;
    private String Id;
    private String Mobile;
    private String Name;
    private String ProfilePic;
    private int ScheduleNotificationStatus;
    private String Speciality;
    private int StaffStatus;
    private String TokenData;
    private String sessionData;
    private int showDot;

    public String getId() {
        return this.Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public int getStaffStatus() {
        return this.StaffStatus;
    }

    public void setStaffStatus(int StaffStatus) {
        this.StaffStatus = StaffStatus;
    }

    public int getScheduleNotificationStatus() {
        return this.ScheduleNotificationStatus;
    }

    public void setScheduleNotificationStatus(int ScheduleNotificationStatus) {
        this.ScheduleNotificationStatus = ScheduleNotificationStatus;
    }

    public int getShowDot() {
        return this.showDot;
    }

    public void setShowDot(int showDot) {
        this.showDot = showDot;
    }

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public String getSessionData() {
        return this.sessionData;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }

    public String getTokenData() {
        return this.TokenData;
    }

    public void setTokenData(String TokenData) {
        this.TokenData = TokenData;
    }

    public String getApiKey() {
        return this.ApiKey;
    }

    public void setApiKey(String ApiKey) {
        this.ApiKey = ApiKey;
    }

    public String getDrId() {
        return this.DrId;
    }

    public void setDrId(String DrId) {
        this.DrId = DrId;
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

    public String getSpeciality() {
        return this.Speciality;
    }

    public void setSpeciality(String Speciality) {
        this.Speciality = Speciality;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
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
