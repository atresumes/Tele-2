package com.trigma.tiktok.model;

import com.trigma.tiktok.utils.Constants;

public class MyDoctorsObject {
    private String Address;
    private String ApiKey;
    private String Bio;
    private String Code;
    private String DOB;
    private String DeviceToken;
    private String DeviceType = Constants.DEVICE_TYPE;
    private String Dr_id;
    private String Email;
    private String Gender;
    private String GroupId;
    private String Languages;
    private String Mobile;
    private String Name;
    private String ProfilePic;
    private String Qualification;
    private double Rating;
    private String Speciality;
    private String TokenData;
    private String sessionData;
    private int status;

    public String getDrId() {
        return this.Dr_id;
    }

    public void setDrId(String Dr_id) {
        this.Dr_id = Dr_id;
    }

    public String getDeviceType() {
        return this.DeviceType;
    }

    public void setDeviceType(String deviceType) {
        this.DeviceType = deviceType;
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

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getProfilePic() {
        return this.ProfilePic;
    }

    public void setProfilePic(String ProfilePic) {
        this.ProfilePic = ProfilePic;
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

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDOB() {
        return this.DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getSpeciality() {
        return this.Speciality;
    }

    public void setSpeciality(String Speciality) {
        this.Speciality = Speciality;
    }

    public String getLanguages() {
        return this.Languages;
    }

    public void setLanguages(String Languages) {
        this.Languages = Languages;
    }

    public String getQualification() {
        return this.Qualification;
    }

    public void setQualification(String Qualification) {
        this.Qualification = Qualification;
    }

    public String getBio() {
        return this.Bio;
    }

    public void setBio(String Bio) {
        this.Bio = Bio;
    }

    public String getDeviceToken() {
        return this.DeviceToken;
    }

    public void setDeviceToken(String DeviceToken) {
        this.DeviceToken = DeviceToken;
    }

    public double getRating() {
        return this.Rating;
    }

    public void setRating(double Rating) {
        this.Rating = Rating;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
