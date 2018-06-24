package com.trigma.tiktok.model;

import java.util.ArrayList;

public class MessagesResponse {
    private String error;
    private String status;
    private ArrayList<MessageUserObject> user;

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MessageUserObject> getUser() {
        return this.user;
    }

    public void setUser(ArrayList<MessageUserObject> user) {
        this.user = user;
    }
}
