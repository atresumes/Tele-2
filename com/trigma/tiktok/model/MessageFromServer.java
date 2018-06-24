package com.trigma.tiktok.model;

public class MessageFromServer {
    private int DStatus;
    private String DisplayName;
    private String From;
    private String GroupId;
    private int PStatus;
    private int Status;
    private String TimeStamp;
    private String UserType;
    private String _id;
    private String message;
    private String messageId;
    private String messageType;

    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getDisplayName() {
        return this.DisplayName;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public String getUserType() {
        return this.UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getTimeStamp() {
        return this.TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getDStatus() {
        return this.DStatus;
    }

    public void setDStatus(int DStatus) {
        this.DStatus = DStatus;
    }

    public int getPStatus() {
        return this.PStatus;
    }

    public void setPStatus(int PStatus) {
        this.PStatus = PStatus;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String From) {
        this.From = From;
    }
}
