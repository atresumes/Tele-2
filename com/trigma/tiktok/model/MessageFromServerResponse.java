package com.trigma.tiktok.model;

import java.util.ArrayList;

public class MessageFromServerResponse {
    public ArrayList<MessageFromServer> data = new ArrayList();
    public String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MessageFromServer> getData() {
        return this.data;
    }

    public void setData(ArrayList<MessageFromServer> data) {
        this.data = data;
    }
}
