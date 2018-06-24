package com.trigma.tiktok.utils;

import com.trigma.tiktok.facebook.SocialLoginData;
import com.trigma.tiktok.model.BookingPojo;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.CreateUserPojo;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.FcmDoctorDetail;
import com.trigma.tiktok.model.LandingPage;
import com.trigma.tiktok.model.LoginDetailPojo;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.model.PatientModelUpcomingOuter;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.model.Upcoming;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import io.paperdb.Paper;
import java.util.ArrayList;
import java.util.HashMap;

public class SharedPreff {
    public static void saveLandingPageData(LandingPage data) {
        Paper.book(Constants.DOC_PREFF).write(Constants.LANDING_PAGE, data);
    }

    public static LandingPage getLandingPageData() {
        return (LandingPage) Paper.book(Constants.DOC_PREFF).read(Constants.LANDING_PAGE, null);
    }

    public static void saveDeviceToken(String token) {
        Paper.book().write(Constants.DEVICE_TOKEN, token);
    }

    public static String getDeviceToken() {
        return (String) Paper.book().read(Constants.DEVICE_TOKEN, null);
    }

    public static void savePatientLogin(LoginDetailPojo data) {
        Paper.book().write(Constants.PATIENT_DETAIL, data);
    }

    public static LoginDetailPojo getPatientLogin() {
        return (LoginDetailPojo) Paper.book().read(Constants.PATIENT_DETAIL, null);
    }

    public static void saveLoginResponce(LoginDocResponce loginDocResponce) {
        Paper.book(Constants.DOC_PREFF).write(Constants.LOGIN_RESPONSE, loginDocResponce);
    }

    public static void clearLoginResponce() {
        Paper.book(Constants.DOC_PREFF).delete(Constants.LOGIN_RESPONSE);
    }

    public static LoginDocResponce getLoginResponce() {
        return (LoginDocResponce) Paper.book(Constants.DOC_PREFF).read(Constants.LOGIN_RESPONSE, null);
    }

    public static void saveStaffLoginResponse(LoginDocResponce loginDocResponce) {
        Paper.book(Constants.DOC_PREFF).write(Constants.STAFF_LOGIN_RESPONSE, loginDocResponce);
    }

    public static LoginDocResponce getStaffLoginResponse() {
        return (LoginDocResponce) Paper.book(Constants.DOC_PREFF).read(Constants.STAFF_LOGIN_RESPONSE, null);
    }

    public static void clearStaffLoginResponse() {
        Paper.book(Constants.DOC_PREFF).delete(Constants.STAFF_LOGIN_RESPONSE);
    }

    public static void saveDocLogin(LoginDetailPojo data) {
        Paper.book().write(Constants.DOCTOR_DETAIL, data);
    }

    public static void saveStaffLogin(LoginDetailPojo data) {
        Paper.book().write(Constants.STAFF_DETAIL, data);
    }

    public static void savePatLocalNotification(HashMap<String, Long> data) {
        Paper.book().write(Constants.PATIENT_LOCAL_NOTI, data);
    }

    public static void saveDOCLocalNotification(HashMap<String, Long> data) {
        Paper.book().write(Constants.DOCTOR_LOCAL_NOTI, data);
    }

    public static HashMap<String, Long> getPatLocalNotification() {
        return (HashMap) Paper.book().read(Constants.PATIENT_LOCAL_NOTI, new HashMap());
    }

    public static HashMap<String, Long> getDocLocalNotification() {
        return (HashMap) Paper.book().read(Constants.DOCTOR_LOCAL_NOTI, new HashMap());
    }

    public static LoginDetailPojo getDocLogin() {
        return (LoginDetailPojo) Paper.book().read(Constants.DOCTOR_DETAIL, null);
    }

    public static LoginDetailPojo getStaffLogin() {
        return (LoginDetailPojo) Paper.book().read(Constants.STAFF_DETAIL, null);
    }

    public static void rememberMe(boolean value) {
        Paper.book().write(Constants.REMEMBER_ME, Boolean.valueOf(value));
    }

    public static boolean isRememberMe() {
        return ((Boolean) Paper.book().read(Constants.REMEMBER_ME, Boolean.valueOf(false))).booleanValue();
    }

    public static void showMessageCount(boolean value) {
        Paper.book().write(Constants.MSG_COUNT, Boolean.valueOf(value));
    }

    public static boolean showMessageCount() {
        return ((Boolean) Paper.book().read(Constants.MSG_COUNT, Boolean.valueOf(false))).booleanValue();
    }

    public static void saveLat(String lat) {
        Paper.book().write(Constants.LAT, lat);
    }

    public static void saveLng(String lng) {
        Paper.book().write(Constants.LNG, lng);
    }

    public static String getLat() {
        return (String) Paper.book().read(Constants.LAT, "");
    }

    public static String getLng() {
        return (String) Paper.book().read(Constants.LNG, "");
    }

    public static void saveBookingDetail(ArrayList<BookingPojo> bookingDetailToSend) {
        Paper.book(Constants.DOC_PREFF).write(Constants.BOOKING_DEATIL, bookingDetailToSend);
    }

    public static ArrayList<BookingPojo> getBookingDetail() {
        return (ArrayList) Paper.book(Constants.DOC_PREFF).read(Constants.BOOKING_DEATIL, null);
    }

    public static void addingDocPatientDetail(PatientPendingData data) {
        Paper.book(Constants.DOC_PREFF).write(Constants.DOC_PATIENT_DEATIL, data);
    }

    public static void addingDocSearchDetail(DrSearchNameObject drSearchNameObject) {
        Paper.book(Constants.DOC_PREFF).write(Constants.DOC_SEARCHED_DEATIL, drSearchNameObject);
    }

    public static DrSearchNameObject getDocSearchDetail() {
        return (DrSearchNameObject) Paper.book(Constants.DOC_PREFF).read(Constants.DOC_SEARCHED_DEATIL, null);
    }

    public static PatientPendingData getDocPatientDetail() {
        return (PatientPendingData) Paper.book(Constants.DOC_PREFF).read(Constants.DOC_PATIENT_DEATIL, null);
    }

    public static DrStaffListObject getStaffDetailListObject() {
        return (DrStaffListObject) Paper.book(Constants.DOC_PREFF).read(Constants.STAFF_DETAIL_LIST_OBJECT, null);
    }

    public static void saveStaffDetailListObject(DrStaffListObject drStaffListObject) {
        Paper.book(Constants.DOC_PREFF).write(Constants.STAFF_DETAIL_LIST_OBJECT, drStaffListObject);
    }

    public static void savingChatDetailObject(MessageUserObject messageUserObject) {
        Paper.book(Constants.DOC_PREFF).write(Constants.CHAT_DETAIL_OBJECT, messageUserObject);
    }

    public static MessageUserObject getChatDetailObject() {
        return (MessageUserObject) Paper.book(Constants.DOC_PREFF).read(Constants.CHAT_DETAIL_OBJECT, null);
    }

    public static void saveUpcomingObject(Upcoming data) {
        Paper.book(Constants.DOC_PREFF).write(Constants.DOC_UPCOMING, data);
    }

    public static void savePatientUpcomingObject(PatientModelUpcomingOuter data) {
        Paper.book(Constants.DOC_PREFF).write(Constants.PAT_UPCOMING, data);
    }

    public static PatientModelUpcomingOuter getPatientUpcomingObject() {
        return (PatientModelUpcomingOuter) Paper.book(Constants.DOC_PREFF).read(Constants.PAT_UPCOMING, null);
    }

    public static Upcoming getUpcomingObject() {
        return (Upcoming) Paper.book(Constants.DOC_PREFF).read(Constants.DOC_UPCOMING, null);
    }

    public static void saveSocialLoginData(SocialLoginData socialLoginData) {
        Paper.book(Constants.DOC_PREFF).write(Constants.SOCIAL_LOGIN, socialLoginData);
    }

    public static void clearSocialLoginData() {
        Paper.book(Constants.DOC_PREFF).delete(Constants.SOCIAL_LOGIN);
    }

    public static SocialLoginData getSocialLoginData() {
        return (SocialLoginData) Paper.book(Constants.DOC_PREFF).read(Constants.SOCIAL_LOGIN, null);
    }

    public static void saveCreateUserPojo(CreateUserPojo createUserPojo) {
        Paper.book(Constants.DOC_PREFF).write(Constants.CREATE_USER_POJO, createUserPojo);
    }

    public static CreateUserPojo getCreateUserPojo() {
        return (CreateUserPojo) Paper.book(Constants.DOC_PREFF).read(Constants.CREATE_USER_POJO, null);
    }

    public static void saveFirstTimeDoctorSearch(boolean value) {
        Paper.book(Constants.FIRST_TIME).write(Constants.DOCTOR_SEARCH, Boolean.valueOf(value));
    }

    public static boolean isFirstTimeDoctorSearch() {
        return ((Boolean) Paper.book(Constants.FIRST_TIME).read(Constants.DOCTOR_SEARCH, Boolean.valueOf(true))).booleanValue();
    }

    public static void saveFirstTimePharmacySelect(boolean value) {
        Paper.book(Constants.FIRST_TIME).write(Constants.PHARMACY_SELECT, Boolean.valueOf(value));
    }

    public static void saveFirstPharmacySelected(boolean value) {
        Paper.book(Constants.FIRST_TIME).write(Constants.PHARMACY_SELECTED, Boolean.valueOf(value));
    }

    public static boolean getFirstPharmacySelected() {
        return ((Boolean) Paper.book(Constants.FIRST_TIME).read(Constants.PHARMACY_SELECTED, Boolean.valueOf(true))).booleanValue();
    }

    public static boolean isFirstTimePharmacySelect() {
        return ((Boolean) Paper.book(Constants.FIRST_TIME).read(Constants.PHARMACY_SELECT, Boolean.valueOf(true))).booleanValue();
    }

    public static void saveSelectPharmacyObject(SelectPharmacyObject selectPharmacyObject) {
        Paper.book(Constants.DOC_PREFF).write(Constants.PHARMACY_DETAIL, selectPharmacyObject);
    }

    public static SelectPharmacyObject getSelectPharmacyObject() {
        return (SelectPharmacyObject) Paper.book(Constants.DOC_PREFF).read(Constants.PHARMACY_DETAIL, null);
    }

    public static void saveDoctorList(ArrayList<MyDoctorsObject> data) {
        Paper.book(Constants.DOC_PREFF).write(Constants.DOCTOR_LIST, data);
    }

    public static ArrayList<MyDoctorsObject> getDoctorList() {
        return (ArrayList) Paper.book(Constants.DOC_PREFF).read(Constants.DOCTOR_LIST, null);
    }

    public static void saveUserId(String token) {
        Paper.book().write("user_id", token);
    }

    public static String getUerID() {
        return (String) Paper.book().read("user_id", null);
    }

    public static void clearDocPreff() {
        Paper.book(Constants.DOC_PREFF).destroy();
    }

    public static void saveVideoCallRequireData(VideoCallRequiredFields videoCallRequiredFields) {
        Paper.book(Constants.DOC_PREFF).write(Constants.VIDEO_CALL_REQUIRE, videoCallRequiredFields);
    }

    public static VideoCallRequiredFields getVideoCallRequireData() {
        return (VideoCallRequiredFields) Paper.book(Constants.DOC_PREFF).read(Constants.VIDEO_CALL_REQUIRE, null);
    }

    public static void saveFcmDocDetail(FcmDoctorDetail fcmDoctorDetail) {
        Paper.book(Constants.DOC_PREFF).write(Constants.FCM_DOC_DETAIL, fcmDoctorDetail);
    }

    public static FcmDoctorDetail getFcmDocDetail() {
        return (FcmDoctorDetail) Paper.book(Constants.DOC_PREFF).read(Constants.FCM_DOC_DETAIL, null);
    }

    public static void saveChatDbDetail(ChatUserForDb chatUserForDb) {
        Paper.book(Constants.DOC_PREFF).write(Constants.CHAT_DB_DETAIL, chatUserForDb);
    }

    public static ChatUserForDb getChatDbDetail() {
        return (ChatUserForDb) Paper.book(Constants.DOC_PREFF).read(Constants.CHAT_DB_DETAIL, null);
    }
}
