package com.trigma.tiktok.model;

public class PatientPendingData {
    public String AcceptReject_Id;
    public String Address;
    public String BookingId;
    public String Code;
    public String DOB;
    public String DeviceToken;
    public String DrschedulesetsId;
    public String Email;
    public String Gender;
    public String Mobile;
    public String Name;
    public String PateientID;
    public String Patient_id;
    public String ProfilePic;
    public int Status = 0;
    public String Subscription;
    public String Url;

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public String getUrl() {
        return this.Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public String getBookingId() {
        return this.BookingId;
    }

    public void setBookingId(String bookingId) {
        this.BookingId = bookingId;
    }

    public String getPateientID() {
        return this.PateientID;
    }

    public void setPateientID(String pateientID) {
        this.PateientID = pateientID;
    }

    public String getDrschedulesetsId() {
        return this.DrschedulesetsId;
    }

    public void setDrschedulesetsId(String drschedulesetsId) {
        this.DrschedulesetsId = drschedulesetsId;
    }

    public String getAcceptReject_Id() {
        return this.AcceptReject_Id;
    }

    public void setAcceptReject_Id(String acceptReject_Id) {
        this.AcceptReject_Id = acceptReject_Id;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getCode() {
        return this.Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getDOB() {
        return this.DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getDeviceToken() {
        return this.DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.DeviceToken = deviceToken;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getMobile() {
        return this.Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPatient_id() {
        return this.Patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.Patient_id = patient_id;
    }

    public String getProfilePic() {
        return this.ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        this.ProfilePic = profilePic;
    }

    public String getSubscription() {
        return this.Subscription;
    }

    public void setSubscription(String subscription) {
        this.Subscription = subscription;
    }
}
