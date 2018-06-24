package com.trigma.tiktok.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.facebook.internal.AnalyticsEvents;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.VideoCallScreen;
import com.trigma.tiktok.activity.VideoCallScreenChanged;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.FcmDoctorDetail;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.NotificationCountPush;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.Random;
import org.greenrobot.eventbus.EventBus;

public class FCMMessageReceiverService extends FirebaseMessagingService {
    String Status = "";
    private LoginDocResponce loginDocResponce;
    String msg = "";
    int userType = 0;

    class C11481 extends TypeToken<FcmDoctorDetail> {
        C11481() {
        }
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("fcm", "received notification" + remoteMessage);
        Log.e("remoteMessage", "" + remoteMessage.getData().toString());
        this.loginDocResponce = SharedPreff.getLoginResponce();
        try {
            if (this.loginDocResponce != null) {
                this.userType = CommonUtils.getIntValue(remoteMessage.getData(), "UserTye");
                this.msg = CommonUtils.getStringValue(remoteMessage.getData(), "msg");
                this.Status = CommonUtils.getStringValue(remoteMessage.getData(), "Status");
                if (this.Status.equalsIgnoreCase(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_VIDEO) && this.loginDocResponce.getData().getUserType() == 0) {
                    Object fcmDoctorDetail = (FcmDoctorDetail) new Gson().fromJson(CommonUtils.getStringValue(remoteMessage.getData(), "Dr"), new C11481().getType());
                    Log.e("fcmDoctorDetail", "" + new Gson().toJson(fcmDoctorDetail));
                    SharedPreff.saveFcmDocDetail(fcmDoctorDetail);
                    VideoCallRequiredFields videoCallRequiredFields = new VideoCallRequiredFields();
                    videoCallRequiredFields.setDrschedulesetsId(CommonUtils.getStringValue(remoteMessage.getData(), "drschedulesetsId"));
                    videoCallRequiredFields.setBookingId(CommonUtils.getStringValue(remoteMessage.getData(), "BookingId"));
                    videoCallRequiredFields.setPatientEmail(this.loginDocResponce.getData().getEmail());
                    videoCallRequiredFields.setPatientName(this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName());
                    videoCallRequiredFields.setPatientId(this.loginDocResponce.getData().get_id());
                    videoCallRequiredFields.setDrName(fcmDoctorDetail.getFirstName() + " " + fcmDoctorDetail.getLastName());
                    videoCallRequiredFields.setUser(0);
                    videoCallRequiredFields.setSchedule(CommonUtils.getCurrentDate());
                    videoCallRequiredFields.setGroupId(CommonUtils.getStringValue(remoteMessage.getData(), "GroupId"));
                    videoCallRequiredFields.setUrl("");
                    SharedPreff.saveVideoCallRequireData(videoCallRequiredFields);
                    VideoCallScreen.TOKEN = CommonUtils.getStringValue(remoteMessage.getData(), "TokenData");
                    VideoCallScreen.API_KEY = CommonUtils.getStringValue(remoteMessage.getData(), "apiKey");
                    VideoCallScreen.SESSION_ID = CommonUtils.getStringValue(remoteMessage.getData(), "sessionData");
                    VideoCallScreenChanged.USER_ID = fcmDoctorDetail.getId();
                    VideoCallScreenChanged.REALAM_UNIQUE_ID = this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + fcmDoctorDetail.getId();
                    VideoCallScreenChanged.TOKEN = CommonUtils.getStringValue(remoteMessage.getData(), "TokenData");
                    VideoCallScreenChanged.API_KEY = CommonUtils.getStringValue(remoteMessage.getData(), "apiKey");
                    VideoCallScreenChanged.SESSION_ID = CommonUtils.getStringValue(remoteMessage.getData(), "sessionData");
                    ChatUserForDb chatUserForDb = new ChatUserForDb();
                    chatUserForDb.set_id(fcmDoctorDetail.getId());
                    chatUserForDb.setAddress(fcmDoctorDetail.getAddress());
                    chatUserForDb.setApiKey(VideoCallScreen.API_KEY);
                    chatUserForDb.setBio("");
                    chatUserForDb.setCity("");
                    chatUserForDb.setCode(fcmDoctorDetail.getCode());
                    chatUserForDb.setCount(0);
                    chatUserForDb.setMobile(fcmDoctorDetail.getMobile());
                    chatUserForDb.setDeviceToken(fcmDoctorDetail.getDeviceToken());
                    chatUserForDb.setSessionData(VideoCallScreen.SESSION_ID);
                    chatUserForDb.setProfilePic(fcmDoctorDetail.getProfilePic());
                    chatUserForDb.setGroupId(CommonUtils.getStringValue(remoteMessage.getData(), "GroupId"));
                    chatUserForDb.setDOB("");
                    chatUserForDb.setUserType(0);
                    chatUserForDb.setEmail(fcmDoctorDetail.getEmail());
                    chatUserForDb.setFirstName(fcmDoctorDetail.getFirstName());
                    chatUserForDb.setLastName(fcmDoctorDetail.getLastName());
                    chatUserForDb.setQualification(fcmDoctorDetail.getQualification());
                    chatUserForDb.setName(fcmDoctorDetail.getFirstName() + "" + fcmDoctorDetail.getLastName());
                    chatUserForDb.setTokenData(VideoCallScreenChanged.TOKEN);
                    chatUserForDb.setGender(fcmDoctorDetail.getGender());
                    chatUserForDb.setSpeciality(fcmDoctorDetail.getSpeciality());
                    SharedPreff.saveChatDbDetail(chatUserForDb);
                } else if (this.Status.equalsIgnoreCase("Dr notification status") && this.loginDocResponce.getData().getUserType() == 1) {
                }
                if (this.Status.equalsIgnoreCase(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_VIDEO)) {
                    if (Constants.CONTEXT == null || this.loginDocResponce.getData().getUserType() != 0) {
                        sendNotification(this.msg, this.userType, this.Status, true);
                        return;
                    }
                    Intent intent = new Intent(this, VideoCallScreenChanged.class);
                    intent.setFlags(268468224);
                    startActivity(intent);
                } else if (this.Status.equalsIgnoreCase("chat") && Constants.CHAT_CONTEXT != null) {
                } else {
                    if (this.Status.equalsIgnoreCase("chat")) {
                        sendNotification(this.msg, this.loginDocResponce.getData().getUserType(), this.Status, false);
                        SharedPreff.showMessageCount(true);
                        NotificationCountPush notificationCountPush = new NotificationCountPush();
                        notificationCountPush.setShowCount(true);
                        EventBus.getDefault().post(notificationCountPush);
                        EventBus.getDefault().post(Constants.REFRESH_MESSAGE_USER);
                    } else if (this.Status.equalsIgnoreCase(Constants.STAFF_NOTIFICATION_STATUS) && Constants.STAFF_HOME_CONTEXT != null && SharedPreff.getStaffLoginResponse() != null) {
                        EventBus.getDefault().post(Constants.STAFF_NOTIFICATION_STATUS);
                        sendNotification(this.msg, this.userType, this.Status, false);
                    } else if (!this.Status.equalsIgnoreCase(Constants.STAFF_NOTIFICATION_STATUS) || SharedPreff.getStaffLoginResponse() != null) {
                        sendNotification(this.msg, this.userType, this.Status, false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String msg, int userType, String status, boolean isCalling) {
        Uri soundUri;
        if (isCalling) {
            try {
                soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + C1020R.raw.ringtone);
            } catch (Exception e) {
                e.printStackTrace();
                soundUri = RingtoneManager.getDefaultUri(2);
            }
        } else {
            soundUri = RingtoneManager.getDefaultUri(2);
        }
        Intent intent = new Intent(this, CommonUtils.getFlagClass(status, userType));
        intent.addFlags(67108864);
        ((NotificationManager) getSystemService("notification")).notify(new Random().nextInt(100), new Builder(this).setSmallIcon(C1020R.drawable.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), C1020R.drawable.ic_launcher)).setContentTitle(getResources().getString(C1020R.string.app_name)).setContentText(msg).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(this, 0, intent, 1207959552)).setSound(soundUri).setStyle(new BigTextStyle().bigText(msg)).build());
    }
}
