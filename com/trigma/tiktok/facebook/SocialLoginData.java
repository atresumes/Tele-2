package com.trigma.tiktok.facebook;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SocialLoginData implements Parcelable {
    public static Creator<SocialLoginData> CREATOR = new C11471();
    private String about_me;
    private String access_token;
    private String birthday_date;
    private String books;
    private String email;
    private String first_name;
    private String interests;
    private String last_name;
    private String movies;
    private String music;
    private String pic_big;
    private String sex;
    private String socialUserID;
    private String userType;

    static class C11471 implements Creator<SocialLoginData> {
        C11471() {
        }

        public SocialLoginData createFromParcel(Parcel parcel) {
            return new SocialLoginData(parcel);
        }

        public SocialLoginData[] newArray(int i) {
            return new SocialLoginData[i];
        }
    }

    public SocialLoginData(String email, String first_name, String last_name, String birthday_date, String sex, String access_token, String books, String interests, String movies, String music, String about_me, String pic_big, String socialUserID, String userType) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthday_date = birthday_date;
        this.sex = sex;
        this.access_token = access_token;
        this.books = books;
        this.interests = interests;
        this.movies = movies;
        this.music = music;
        this.about_me = about_me;
        this.pic_big = pic_big;
        this.socialUserID = socialUserID;
        this.userType = userType;
    }

    public SocialLoginData(Parcel source) {
        this.email = source.readString();
        this.first_name = source.readString();
        this.last_name = source.readString();
        this.birthday_date = source.readString();
        this.sex = source.readString();
        this.access_token = source.readString();
        this.books = source.readString();
        this.interests = source.readString();
        this.movies = source.readString();
        this.music = source.readString();
        this.about_me = source.readString();
        this.pic_big = source.readString();
        this.socialUserID = source.readString();
        this.userType = source.readString();
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday_date() {
        return this.birthday_date;
    }

    public void setBirthday_date(String birthday_date) {
        this.birthday_date = birthday_date;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getBooks() {
        return this.books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getInterests() {
        return this.interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getMovies() {
        return this.movies;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }

    public String getMusic() {
        return this.music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getAbout_me() {
        return this.about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getPic_big() {
        return this.pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getSocialUserID() {
        return this.socialUserID;
    }

    public void setSocialUserID(String socialUserID) {
        this.socialUserID = socialUserID;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.email);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.birthday_date);
        dest.writeString(this.sex);
        dest.writeString(this.access_token);
        dest.writeString(this.books);
        dest.writeString(this.interests);
        dest.writeString(this.movies);
        dest.writeString(this.music);
        dest.writeString(this.about_me);
        dest.writeString(this.pic_big);
        dest.writeString(this.socialUserID);
        dest.writeString(this.userType);
    }
}
