package com.trigma.tiktok.model;

public class SelectPharmacyObject {
    private String Lat;
    private String Long;
    private SelectPharmacyObjectIner data;
    private String fullAddress;

    public SelectPharmacyObjectIner getData() {
        return this.data;
    }

    public void setData(SelectPharmacyObjectIner data) {
        this.data = data;
    }

    public String getLat() {
        return this.Lat;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    public String getLong() {
        return this.Long;
    }

    public void setLong(String Long) {
        this.Long = Long;
    }

    public String getFullAddress() {
        return this.fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
