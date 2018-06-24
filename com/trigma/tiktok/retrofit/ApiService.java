package com.trigma.tiktok.retrofit;

import com.google.gson.JsonObject;
import com.trigma.tiktok.model.AddStaffResponse;
import com.trigma.tiktok.model.AppointmentListResponse;
import com.trigma.tiktok.model.AsyncMessageSend;
import com.trigma.tiktok.model.ContactUsResponse;
import com.trigma.tiktok.model.CreateUserPojo;
import com.trigma.tiktok.model.CurrentScheduleResponce;
import com.trigma.tiktok.model.DecativateResponse;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DocUserDetailResponse;
import com.trigma.tiktok.model.DoctorChatResponse;
import com.trigma.tiktok.model.DrAppointmentDate;
import com.trigma.tiktok.model.DrAppointmentResponse;
import com.trigma.tiktok.model.DrCancelScheduleResponce;
import com.trigma.tiktok.model.DrDateResponse;
import com.trigma.tiktok.model.DrId;
import com.trigma.tiktok.model.DrSchedulePatientListResponce;
import com.trigma.tiktok.model.DrScheduleSetResponse;
import com.trigma.tiktok.model.DrSearchCodeResponse;
import com.trigma.tiktok.model.DrSearchNameResponse;
import com.trigma.tiktok.model.DrStaffListResponce;
import com.trigma.tiktok.model.ForceUpdate;
import com.trigma.tiktok.model.ForgotPswdResponce;
import com.trigma.tiktok.model.GuestUrlResponse;
import com.trigma.tiktok.model.LandingPage;
import com.trigma.tiktok.model.LoginDetailPojo;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageFromServerResponse;
import com.trigma.tiktok.model.MessagesResponse;
import com.trigma.tiktok.model.MyDoctorsResponse;
import com.trigma.tiktok.model.NotificationCountCall;
import com.trigma.tiktok.model.NotificationCountResponse;
import com.trigma.tiktok.model.NotificationResponse;
import com.trigma.tiktok.model.PatientAppointmentResponse;
import com.trigma.tiktok.model.PatientPendingResponse;
import com.trigma.tiktok.model.PrescriptionObject;
import com.trigma.tiktok.model.SelectPharmacyResponse;
import com.trigma.tiktok.model.SignUpResponse;
import com.trigma.tiktok.model.StaffDoctorListResponse;
import com.trigma.tiktok.model.UploadingResponse;
import com.trigma.tiktok.model.VideoCallResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @FormUrlEncoded
    @POST("/docter/DRPatientCheck")
    Observable<DocAcceptReject> DRPatientCheck(@Field("DrId") String str, @Field("PatientId") String str2);

    @FormUrlEncoded
    @POST("docter/DRStaffActiveDeactiveDelete")
    Observable<DocAcceptReject> DrStaffActiveDeactiveDelete(@Field("Status") String str, @Field("DrId") String str2, @Field("StaffUserId") String str3);

    @FormUrlEncoded
    @POST("users/PharmacyAdd")
    Observable<DocAcceptReject> addPharmacy(@Field("UserId") String str, @Field("PharmacyId") String str2, @Field("PharmacyName") String str3, @Field("PharmacySelect") String str4);

    @FormUrlEncoded
    @POST("/docter/DrCancel")
    Observable<DrCancelScheduleResponce> cancelingTimeSchedule(@Field("Dr_id") String str, @Field("DrScheduleId") String str2, @Field("DrName") String str3, @Field("From") String str4, @Field("To") String str5, @Field("TimeSchedule") String str6, @Field("CancellationId") String str7, @Field("CancellationName") String str8, @Field("CancellationUserType") String str9, @Field("ScheduledDate") String str10);

    @FormUrlEncoded
    @POST("users/ChangePassword")
    Observable<DocAcceptReject> changePassword(@Field("id") String str, @Field("OldPassword") String str2, @Field("NewPassword") String str3);

    @FormUrlEncoded
    @POST("messageChat/messageRead")
    Observable<DocAcceptReject> chatMessageRead(@Field("UserId") String str, @Field("GroupId") String str2);

    @FormUrlEncoded
    @POST("/docter/DRAcceptReject")
    Observable<DocAcceptReject> dRAcceptReject(@Field("AcceptReject_Id") String str, @Field("Status") String str2, @Field("DeviceToken") String str3, @Field("Email") String str4, @Field("PatientName") String str5, @Field("CreateId") String str6, @Field("CreateName") String str7, @Field("CreateUserType") String str8, @Field("Dr_id") String str9);

    @FormUrlEncoded
    @POST("docter/DrBookingCancellation")
    Observable<DocAcceptReject> doctorCancellationBooking(@Field("BookingId") String str, @Field("drschedulesetsId") String str2, @Field("DeviceToken") String str3, @Field("DeviceType") String str4, @Field("Email") String str5, @Field("CancellationId") String str6, @Field("CancellationName") String str7, @Field("CancellationUserType") String str8, @Field("Dr_id") String str9, @Field("ScheduledDate") String str10, @Field("From") String str11, @Field("To") String str12, @Field("DrName") String str13, @Field("PatientName") String str14);

    @FormUrlEncoded
    @POST("docter/DrAppointmentList")
    Observable<DrAppointmentResponse> drAppointmentsList(@Field("Dr_Id") String str);

    @FormUrlEncoded
    @POST("docter/DrCencellationPatient")
    Observable<DocAcceptReject> drCencellationPatient(@Field("Dr_id") String str, @Field("PatientId") String str2, @Field("CancellationId") String str3, @Field("CancellationName") String str4, @Field("CancellationUserType") String str5, @Field("PatientName") String str6);

    @FormUrlEncoded
    @POST("docter/DRdrcreatetoken")
    Observable<DoctorChatResponse> drDrcreatetoken(@Field("From") String str, @Field("To") String str2);

    @FormUrlEncoded
    @POST("docter/drmemberadd")
    Observable<AddStaffResponse> drMemberAdd(@Field("FirstName") String str, @Field("LastName") String str2, @Field("Gender") String str3, @Field("Mobile") String str4, @Field("Code") String str5, @Field("ProfilePic") String str6, @Field("UserType") String str7, @Field("Email") String str8, @Field("MediaType") String str9, @Field("DrId") String str10, @Field("DeviceToken") String str11, @Field("Designation") String str12, @Field("DrName") String str13);

    @FormUrlEncoded
    @POST("users/notificationread")
    Observable<DocAcceptReject> drNotificationRead(@Field("UserId") String str, @Field("Type") String str2);

    @FormUrlEncoded
    @POST("/docter/DrSchedulePatientList")
    Observable<DrSchedulePatientListResponce> drSchedulePatientList(@Field("Dr_id") String str, @Field("DrScheduleId") String str2, @Field("From") String str3, @Field("To") String str4, @Field("ScheduleId") String str5);

    @FormUrlEncoded
    @POST("/users/ForgotPassword")
    Observable<ForgotPswdResponce> forgotPasswordApi(@Field("Email") String str);

    @GET("maps/api/geocode/json")
    Observable<JsonObject> getAddress(@Query("address") String str, @Query("key") String str2);

    @FormUrlEncoded
    @POST("/users/Contact")
    Observable<ContactUsResponse> getContactUsDeatil(@Field("UserId") String str);

    @FormUrlEncoded
    @POST("docter/Drnotification")
    Observable<NotificationResponse> getDocNotification(@Field("Dr_Id") String str);

    @FormUrlEncoded
    @POST("docter/DrScheduleList")
    Observable<AppointmentListResponse> getDoctorAvailability(@Field("Dr_Id") String str);

    @FormUrlEncoded
    @POST("users/details")
    Observable<DocUserDetailResponse> getDoctorProfile(@Field("id") String str);

    @POST("/docter/DrAppointmentDate")
    Observable<DrAppointmentDate> getDrAppointmentDate(@Body DrId drId);

    @FormUrlEncoded
    @POST("/docter/DrDate")
    Observable<DrDateResponse> getDrScheduleList(@Field("ScheduleDate") String str, @Field("Dr_Id") String str2);

    @FormUrlEncoded
    @POST("/docter/ScheduleList")
    Observable<CurrentScheduleResponce> getDrScheduleListing(@Field("Dr_id") String str);

    @FormUrlEncoded
    @POST("docter/DRStaffList")
    Observable<DrStaffListResponce> getDrStaffList(@Field("DrId") String str, @Field("StaffId") String str2);

    @POST("/users/LandingApi")
    Observable<LandingPage> getLandingPage();

    @FormUrlEncoded
    @POST("users/DrpatientChatlist")
    Observable<MessagesResponse> getMessagesList(@Field("UserId") String str, @Field("UserType") String str2);

    @POST("/users/notificationCount")
    Observable<NotificationCountResponse> getNotificationCount(@Body NotificationCountCall notificationCountCall);

    @FormUrlEncoded
    @POST("users/TokenCheck")
    Observable<ForceUpdate> getOnlineAppVersion(@Field("DeviceToken") String str);

    @FormUrlEncoded
    @POST("patients/patientpushlist")
    Observable<DrAppointmentDate> getPatientAppointmentDate(@Field("PatientId") String str);

    @FormUrlEncoded
    @POST("patients/PatientAppointmentList")
    Observable<PatientAppointmentResponse> getPatientAppointmentsList(@Field("PatientId") String str);

    @FormUrlEncoded
    @POST("patients/PatientDrList")
    Observable<MyDoctorsResponse> getPatientDoctorList(@Field("Patient_id") String str);

    @FormUrlEncoded
    @POST("/docter/DRPatientList")
    Observable<PatientPendingResponse> getPatientList(@Field("Dr_id") String str, @Field("Status") String str2);

    @FormUrlEncoded
    @POST("patients/Notification")
    Observable<NotificationResponse> getPatientNotification(@Field("PatientId") String str);

    @FormUrlEncoded
    @POST("cron/PharmacyCheck")
    Observable<SelectPharmacyResponse> getPharmacy(@Field("Value") String str);

    @FormUrlEncoded
    @POST("messageChat/messagelist")
    Observable<MessageFromServerResponse> getServerMessage(@Field("UserId") String str, @Field("GroupId") String str2);

    @FormUrlEncoded
    @POST("staffapi/staffDrList")
    Observable<StaffDoctorListResponse> getStaffDocList(@Field("StaffId") String str);

    @GET(".")
    Observable<GuestUrlResponse> getTokenForGuest(@Header("devicetype") String str);

    @FormUrlEncoded
    @POST("docter/soap")
    Observable<PrescriptionObject> getTransactionErrorsCount(@Field("DrId") String str);

    @POST("videoChat/uploadApi")
    @Multipart
    Observable<UploadingResponse> getUploadLink(@Part("filename") RequestBody requestBody, @Part MultipartBody.Part part);

    @FormUrlEncoded
    @POST("docter/DrVideo")
    Observable<VideoCallResponse> getVideoToken(@Field("DeviceToken") String str, @Field("BookingId") String str2, @Field("PatientId") String str3, @Field("DrId") String str4, @Field("drschedulesetsId") String str5);

    @FormUrlEncoded
    @POST("patients/AllDrSearch")
    Observable<DrSearchNameResponse> hflSearchByNameAndZip(@Field("Lat") String str, @Field("Long") String str2, @Field("UserId") String str3);

    @FormUrlEncoded
    @POST("staffapi/StaffCount")
    Observable<NotificationCountResponse> homeScreenStaffCount(@Field("StaffId") String str, @Field("Drid") String str2);

    @POST("/users/Login1")
    Observable<LoginDocResponce> loginApi(@Body LoginDetailPojo loginDetailPojo);

    @FormUrlEncoded
    @POST("/users/Login")
    Observable<LoginDocResponce> loginApi(@Field("Email") String str, @Field("Password") String str2, @Field("DeviceToken") String str3, @Field("DeviceType") String str4);

    @POST("/staffapi/Login")
    Observable<LoginDocResponce> loginStaffApi(@Body LoginDetailPojo loginDetailPojo);

    @FormUrlEncoded
    @POST("users/logout")
    Observable<DocAcceptReject> logoutApi(@Field("UserId") String str, @Field("DeviceToken") String str2);

    @FormUrlEncoded
    @POST("patients/request")
    Observable<DocAcceptReject> makeRequestToDoctor(@Field("Patient_id") String str, @Field("Dr_id") String str2, @Field("DeviceToken") String str3, @Field("DeviceType") String str4, @Field("Email") String str5, @Field("DrName") String str6, @Field("PatientName") String str7);

    @FormUrlEncoded
    @POST("docter/SchduleOver")
    Observable<DocAcceptReject> makeSchduleOverCall(@Field("BookingId") String str, @Field("drschedulesetsId") String str2, @Field("DeviceToken") String str3, @Field("Schedule") String str4, @Field("PatientEmail") String str5, @Field("DrName") String str6, @Field("User") String str7, @Field("GroupId") String str8, @Field("PatientName") String str9);

    @FormUrlEncoded
    @POST("users/scheduleread")
    Observable<DocAcceptReject> patientAppointmentRead(@Field("UserId") String str, @Field("Type") String str2);

    @FormUrlEncoded
    @POST("patients/PatientBookDr")
    Observable<DocAcceptReject> patientsPatientBookDr(@Field("Book_Id") String str, @Field("PatientId") String str2, @Field("DrId") String str3, @Field("From") String str4, @Field("Sloat") String str5, @Field("Date") String str6, @Field("DeviceToken") String str7, @Field("PatientName") String str8, @Field("DrEmail") String str9, @Field("PatientEmail") String str10);

    @FormUrlEncoded
    @POST("/messageChat/sessionChange")
    Observable<DocAcceptReject> refreshingOpenTokToken(@Field("UserId") String str);

    @FormUrlEncoded
    @POST("patients/DrSearch")
    Observable<DrSearchCodeResponse> searchByDoctorCode(@Field("DrCode") String str, @Field("Lat") String str2, @Field("Long") String str3, @Field("UserId") String str4);

    @FormUrlEncoded
    @POST("patients/DrSearchNameZip")
    Observable<DrSearchNameResponse> searchByNameAndZip(@Field("Search") String str, @Field("Lat") String str2, @Field("Long") String str3, @Field("UserId") String str4);

    @FormUrlEncoded
    @POST("patients/DrNearBy")
    Observable<DrSearchNameResponse> searchByNear(@Field("Lat") String str, @Field("Long") String str2, @Field("UserId") String str3);

    @POST("messageChat/messagesync")
    Observable<DocAcceptReject> sendMessageAsync(@Body AsyncMessageSend asyncMessageSend);

    @FormUrlEncoded
    @POST("/docter/DrScheduleSet")
    Observable<DrScheduleSetResponse> setDrScheduleList(@Field("Dr_id") String str, @Field("Year") String str2, @Field("Month") String str3, @Field("Date") String str4, @Field("From") String str5, @Field("To") String str6, @Field("MeetingSlot") String str7, @Field("CreateId") String str8, @Field("CreateName") String str9, @Field("CreateUserType") String str10);

    @POST("/users/sign")
    Observable<SignUpResponse> signupApi(@Body CreateUserPojo createUserPojo);

    @FormUrlEncoded
    @POST("staffapi/StaffScheduleRead")
    Observable<DocAcceptReject> staffAppRead(@Field("StaffId") String str);

    @FormUrlEncoded
    @POST("docter/drcheckstaff")
    Observable<AddStaffResponse> staffWithID(@Field("StaffId") String str, @Field("DrId") String str2, @Field("StaffUserId") String str3, @Field("DrName") String str4);

    @FormUrlEncoded
    @POST("users/useredit")
    Observable<LoginDocResponce> updateDocProfile(@Field("FirstName") String str, @Field("LastName") String str2, @Field("Mobile") String str3, @Field("Code") String str4, @Field("Lat") String str5, @Field("Long") String str6, @Field("id") String str7, @Field("Zipcode") String str8, @Field("ProfilePic") String str9, @Field("Qualification") String str10, @Field("Speciality") String str11, @Field("Languages") String str12, @Field("Bio") String str13);

    @FormUrlEncoded
    @POST("users/useredit")
    Observable<LoginDocResponce> updatePatProfile(@Field("FirstName") String str, @Field("LastName") String str2, @Field("Mobile") String str3, @Field("Code") String str4, @Field("Lat") String str5, @Field("Long") String str6, @Field("id") String str7, @Field("Zipcode") String str8, @Field("ProfilePic") String str9, @Field("DOB") String str10, @Field("Address") String str11);

    @FormUrlEncoded
    @POST("users/useredit")
    Observable<LoginDocResponce> updateStaffProfile(@Field("FirstName") String str, @Field("LastName") String str2, @Field("Mobile") String str3, @Field("Code") String str4, @Field("id") String str5, @Field("ProfilePic") String str6, @Field("Designation") String str7);

    @FormUrlEncoded
    @POST("users/agreement")
    Observable<DocAcceptReject> userAgreement(@Field("UserId") String str, @Field("agreement") String str2);

    @FormUrlEncoded
    @POST("users/LoginLogout")
    Observable<DecativateResponse> userDecativate(@Field("UserId") String str, @Field("DeviceToken") String str2, @Field("DeviceType") String str3);

    @FormUrlEncoded
    @POST("users/Rating")
    Observable<DocAcceptReject> userRating(@Field("From") String str, @Field("To") String str2, @Field("Rating") String str3, @Field("Feedback") String str4);

    @FormUrlEncoded
    @POST("users/UpdateToken")
    Observable<DocAcceptReject> userUpdateToken(@Field("UserId") String str, @Field("DeviceToken") String str2);
}
