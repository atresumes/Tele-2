package com.trigma.tiktok.model;

import com.facebook.appevents.AppEventsConstants;
import java.util.ArrayList;

public class LoginDocData {
    public String AddStaffLimit = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    public String Address;
    public int AdminStatus;
    public String Bio;
    public String City;
    public String ClinicId;
    public String ClinicKey;
    public String ClinicianID;
    public int Code;
    public String CodeId;
    public String Designation;
    public String DeviceToken;
    public String DeviceType;
    public String DrCode;
    public int DrRequest;
    public String DrType;
    public String Email;
    public ArrayList<FeedBack> Feedback = new ArrayList();
    public String FirstName;
    public String Gender;
    public String HashToken;
    public String Languages;
    public String LastName;
    public String Lat;
    public String LoginAllready;
    public String Long;
    public String MediaID;
    public String MediaType;
    public String Mobile;
    public String Password;
    public String PharmacyName;
    public String ProfilePic;
    public String Qualification;
    public String Speciality;
    public int StaffDrStatus = 0;
    public String StaffId;
    public String State;
    public int Status;
    public int UserType;
    public String Zipcode;
    public String __v;
    public String _id;

    public String getDesignation() {
        return this.Designation;
    }

    public void setDesignation(String designation) {
        this.Designation = designation;
    }

    public String getStaffId() {
        return this.StaffId;
    }

    public void setStaffId(String staffId) {
        this.StaffId = staffId;
    }

    public int getStaffDrStatus() {
        return this.StaffDrStatus;
    }

    public void setStaffDrStatus(int staffDrStatus) {
        this.StaffDrStatus = staffDrStatus;
    }

    public String getClinicKey() {
        return this.ClinicKey;
    }

    public void setClinicKey(String clinicKey) {
        this.ClinicKey = clinicKey;
    }

    public String getClinicianID() {
        return this.ClinicianID;
    }

    public void setClinicianID(String clinicianID) {
        this.ClinicianID = clinicianID;
    }

    public String getClinicId() {
        return this.ClinicId;
    }

    public void setClinicId(String clinicId) {
        this.ClinicId = clinicId;
    }

    public String getDrType() {
        return this.DrType;
    }

    public void setDrType(String drType) {
        this.DrType = drType;
    }

    public String getAddStaffLimit() {
        return this.AddStaffLimit;
    }

    public void setAddStaffLimit(String addStaffLimit) {
        this.AddStaffLimit = addStaffLimit;
    }

    public String getMediaType() {
        return this.MediaType;
    }

    public void setMediaType(String mediaType) {
        this.MediaType = mediaType;
    }

    public String getMediaID() {
        return this.MediaID;
    }

    public void setMediaID(String mediaID) {
        this.MediaID = mediaID;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public int getAdminStatus() {
        return this.AdminStatus;
    }

    public void setAdminStatus(int adminStatus) {
        this.AdminStatus = adminStatus;
    }

    public String getBio() {
        return this.Bio;
    }

    public void setBio(String bio) {
        this.Bio = bio;
    }

    public String getCity() {
        return this.City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public int getCode() {
        return this.Code;
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public String getCodeId() {
        return this.CodeId;
    }

    public void setCodeId(String codeId) {
        this.CodeId = codeId;
    }

    public String getDeviceToken() {
        return this.DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.DeviceToken = deviceToken;
    }

    public String getDeviceType() {
        return this.DeviceType;
    }

    public void setDeviceType(String deviceType) {
        this.DeviceType = deviceType;
    }

    public String getDrCode() {
        return this.DrCode;
    }

    public void setDrCode(String drCode) {
        this.DrCode = drCode;
    }

    public int getDrRequest() {
        return this.DrRequest;
    }

    public void setDrRequest(int drRequest) {
        this.DrRequest = drRequest;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getHashToken() {
        return this.HashToken;
    }

    public void setHashToken(String hashToken) {
        this.HashToken = hashToken;
    }

    public String getLanguages() {
        return this.Languages;
    }

    public void setLanguages(String languages) {
        this.Languages = languages;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getLat() {
        return this.Lat;
    }

    public void setLat(String lat) {
        this.Lat = lat;
    }

    public String getLoginAllready() {
        return this.LoginAllready;
    }

    public void setLoginAllready(String loginAllready) {
        this.LoginAllready = loginAllready;
    }

    public String getLong() {
        return this.Long;
    }

    public void setLong(String aLong) {
        this.Long = aLong;
    }

    public String getMobile() {
        return this.Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPharmacyName() {
        return this.PharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.PharmacyName = pharmacyName;
    }

    public String getProfilePic() {
        return this.ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        this.ProfilePic = profilePic;
    }

    public String getQualification() {
        return this.Qualification;
    }

    public void setQualification(String qualification) {
        this.Qualification = qualification;
    }

    public String getSpeciality() {
        return this.Speciality;
    }

    public void setSpeciality(String speciality) {
        this.Speciality = speciality;
    }

    public String getState() {
        return this.State;
    }

    public void setState(String state) {
        this.State = state;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public int getUserType() {
        return this.UserType;
    }

    public void setUserType(int userType) {
        this.UserType = userType;
    }

    public String getZipcode() {
        return this.Zipcode;
    }

    public void setZipcode(String zipcode) {
        this.Zipcode = zipcode;
    }

    public String get__v() {
        return this.__v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<FeedBack> getFeedback() {
        return this.Feedback;
    }

    public void setFeedback(ArrayList<FeedBack> feedback) {
        this.Feedback = feedback;
    }
}
