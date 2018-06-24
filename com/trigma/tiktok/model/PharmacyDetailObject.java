package com.trigma.tiktok.model;

public class PharmacyDetailObject {
    private String ActiveEndTime;
    private String ActiveStartTime;
    private String Address;
    private String AddressFull;
    private String City;
    private String CrossStreet;
    private String DoseSpotPharmacyID;
    private String Fax;
    private String Hours24;
    private String IsFromSurescripts;
    private String LastModifiedDate;
    private String NCPDPID;
    private String PhonePrimary;
    private String ServiceLevel;
    private String Specialties;
    private String State;
    private String StoreName;
    private String StoreNumber;
    private String Version;
    private String Zip;
    private int __v;
    private String _id;
    private String created_at;

    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return this.created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getDoseSpotPharmacyID() {
        return this.DoseSpotPharmacyID;
    }

    public void setDoseSpotPharmacyID(String DoseSpotPharmacyID) {
        this.DoseSpotPharmacyID = DoseSpotPharmacyID;
    }

    public String getNCPDPID() {
        return this.NCPDPID;
    }

    public void setNCPDPID(String NCPDPID) {
        this.NCPDPID = NCPDPID;
    }

    public String getStoreNumber() {
        return this.StoreNumber;
    }

    public void setStoreNumber(String StoreNumber) {
        this.StoreNumber = StoreNumber;
    }

    public String getStoreName() {
        return this.StoreName;
    }

    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAddressFull() {
        return this.AddressFull;
    }

    public void setAddressFull(String AddressFull) {
        this.AddressFull = AddressFull;
    }

    public String getCity() {
        return this.City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getState() {
        return this.State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getZip() {
        return this.Zip;
    }

    public void setZip(String Zip) {
        this.Zip = Zip;
    }

    public String getPhonePrimary() {
        return this.PhonePrimary;
    }

    public void setPhonePrimary(String PhonePrimary) {
        this.PhonePrimary = PhonePrimary;
    }

    public String getFax() {
        return this.Fax;
    }

    public void setFax(String Fax) {
        this.Fax = Fax;
    }

    public String getActiveStartTime() {
        return this.ActiveStartTime;
    }

    public void setActiveStartTime(String ActiveStartTime) {
        this.ActiveStartTime = ActiveStartTime;
    }

    public String getActiveEndTime() {
        return this.ActiveEndTime;
    }

    public void setActiveEndTime(String ActiveEndTime) {
        this.ActiveEndTime = ActiveEndTime;
    }

    public String getServiceLevel() {
        return this.ServiceLevel;
    }

    public void setServiceLevel(String ServiceLevel) {
        this.ServiceLevel = ServiceLevel;
    }

    public String getSpecialties() {
        return this.Specialties;
    }

    public void setSpecialties(String Specialties) {
        this.Specialties = Specialties;
    }

    public String getLastModifiedDate() {
        return this.LastModifiedDate;
    }

    public void setLastModifiedDate(String LastModifiedDate) {
        this.LastModifiedDate = LastModifiedDate;
    }

    public String getHours24() {
        return this.Hours24;
    }

    public void setHours24(String Hours24) {
        this.Hours24 = Hours24;
    }

    public String getCrossStreet() {
        return this.CrossStreet;
    }

    public void setCrossStreet(String CrossStreet) {
        this.CrossStreet = CrossStreet;
    }

    public String getVersion() {
        return this.Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getIsFromSurescripts() {
        return this.IsFromSurescripts;
    }

    public void setIsFromSurescripts(String IsFromSurescripts) {
        this.IsFromSurescripts = IsFromSurescripts;
    }

    public int getV() {
        return this.__v;
    }

    public void setV(int __v) {
        this.__v = __v;
    }
}
