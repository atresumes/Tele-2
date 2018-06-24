package com.trigma.tiktok.model;

public class LandingData {
    private String FaqDr;
    private String FaqPatient;
    private String TeliMedicine;
    private String WhoAreWe;
    private Integer __v;
    private String _id;
    private String version;

    public Integer get__v() {
        return this.__v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWhoAreWe() {
        return this.WhoAreWe;
    }

    public void setWhoAreWe(String WhoAreWe) {
        this.WhoAreWe = WhoAreWe;
    }

    public String getTeliMedicine() {
        return this.TeliMedicine;
    }

    public void setTeliMedicine(String teliMedicine) {
        this.TeliMedicine = teliMedicine;
    }

    public String getFaqDr() {
        return this.FaqDr;
    }

    public void setFaqDr(String FaqDr) {
        this.FaqDr = FaqDr;
    }

    public String getFaqPatient() {
        return this.FaqPatient;
    }

    public void setFaqPatient(String FaqPatient) {
        this.FaqPatient = FaqPatient;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
