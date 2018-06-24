package com.trigma.tiktok.model;

import java.util.HashMap;
import java.util.Map;

public class LandingPage {
    private Map<String, Object> additionalProperties = new HashMap();
    private LandingData data;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LandingData getData() {
        return this.data;
    }

    public void setData(LandingData data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
