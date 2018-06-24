package com.trigma.tiktok.model;

public class MessageUserObject {
    private String ApiKey;
    private String DOB;
    private String DeviceToken;
    private String Gender;
    private String GroupId;
    private String Name;
    private String ProfilePic;
    private String Speciality;
    private String TokenData;
    private int UserType;
    private String _id;
    private int count = 0;
    private int messagedotshow = 1;
    private String sessionData;
    private int type;
    private MessageUserDetail user;

    public int getMessagedotshow() {
        return this.messagedotshow;
    }

    public void setMessagedotshow(int messagedotshow) {
        this.messagedotshow = messagedotshow;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public MessageUserDetail getUser() {
        return this.user;
    }

    public void setUser(MessageUserDetail user) {
        this.user = user;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getProfilePic() {
        return this.ProfilePic;
    }

    public void setProfilePic(String ProfilePic) {
        this.ProfilePic = ProfilePic;
    }

    public String getDOB() {
        return this.DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public int getUserType() {
        return this.UserType;
    }

    public void setUserType(int UserType) {
        this.UserType = UserType;
    }

    public String getDeviceToken() {
        return this.DeviceToken;
    }

    public void setDeviceToken(String DeviceToken) {
        this.DeviceToken = DeviceToken;
    }

    public String getSpeciality() {
        return this.Speciality;
    }

    public void setSpeciality(String Speciality) {
        this.Speciality = Speciality;
    }

    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSessionData() {
        return this.sessionData;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }

    public String getTokenData() {
        return this.TokenData;
    }

    public void setTokenData(String TokenData) {
        this.TokenData = TokenData;
    }

    public String getApiKey() {
        return this.ApiKey;
    }

    public void setApiKey(String ApiKey) {
        this.ApiKey = ApiKey;
    }

    public String getGroupId() {
        return this.GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
