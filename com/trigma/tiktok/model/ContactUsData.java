package com.trigma.tiktok.model;

public class ContactUsData {
    public String Address;
    public String Email;
    public String Lat;
    public String Long;
    public String Phone;
    public String __v;
    public String _icreated_atd;
    public String _id;

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_icreated_atd() {
        return this._icreated_atd;
    }

    public void set_icreated_atd(String _icreated_atd) {
        this._icreated_atd = _icreated_atd;
    }

    public String get__v() {
        return this.__v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getLat() {
        return this.Lat;
    }

    public void setLat(String lat) {
        this.Lat = lat;
    }

    public String getLong() {
        return this.Long;
    }

    public void setLong(String aLong) {
        this.Long = aLong;
    }
}
