package com.trigma.tiktok.model;

import java.util.ArrayList;

public class AsyncMessageSend {
    public String BatchPush;
    public ArrayList<AsyncMessageData> Data = new ArrayList();
    public String GroupId;
    public String PushUserId;

    public ArrayList<AsyncMessageData> getData() {
        return this.Data;
    }

    public void setData(ArrayList<AsyncMessageData> data) {
        this.Data = data;
    }

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String groupId) {
        this.GroupId = groupId;
    }

    public String getBatchPush() {
        return this.BatchPush;
    }

    public void setBatchPush(String batchPush) {
        this.BatchPush = batchPush;
    }

    public String getPushUserId() {
        return this.PushUserId;
    }

    public void setPushUserId(String pushUserId) {
        this.PushUserId = pushUserId;
    }
}
