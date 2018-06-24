package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageUserDetail;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.model.VideoCallResponse;
import com.trigma.tiktok.presenter.PatientDetailPresenter;
import com.trigma.tiktok.presenter.PatientDetailPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.services.events.EventsFilesManager;

public class PatientDetail extends BaseActivity implements OnClickListener {
    String CreateId = "";
    String CreateName = "";
    String CreateUserType = "";
    String Dr_id = "";
    String PatientName = "";
    private LinearLayout call_linear;
    private CardView card_subscription;
    private ImageView img_back;
    private CircleImageView img_profile;
    private int isFromActive = 0;
    private LinearLayout linear_call_again;
    private LinearLayout linear_cancel_subscription;
    private LinearLayout linear_confirm;
    private LinearLayout linear_decline;
    private LoginDocResponce loginDocResponce;
    private NetWorkingService netWorkingService;
    private PatientDetailPresenter patientDetailPresenter;
    private PatientPendingData patientPendingData;
    private TextView tv_address_field;
    private TextView tv_age_field;
    private TextView tv_cancel_sub;
    private TextView tv_gender_field;
    private TextView tv_name_field;
    private TextView tv_phone_field;
    private TextView tv_prescription;
    private TextView tv_subscription_field;

    class C10601 implements AlertCallBackWithButtonsInterface {
        C10601() {
        }

        public void positiveClick() {
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            CommonUtils.call(PatientDetail.this, PatientDetail.this.patientPendingData.getCode() + PatientDetail.this.patientPendingData.getMobile());
        }
    }

    class C10612 implements AlertCallBackWithButtonsInterface {
        C10612() {
        }

        public void positiveClick() {
            Constants.PRESCRIPTION_URL = PatientDetail.this.patientPendingData.getUrl();
            Intent signUp = new Intent(PatientDetail.this, PatientPrescription.class);
            signUp.setFlags(268468224);
            PatientDetail.this.startActivity(signUp);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C10623 implements AlertCallBackWithButtonsInterface {
        C10623() {
        }

        public void positiveClick() {
            Log.e("patientPendingData", new Gson().toJson(PatientDetail.this.patientPendingData));
            Log.e("p_id", "" + PatientDetail.this.patientPendingData.getPatient_id());
            PatientDetail.this.patientDetailPresenter.cacelSubscription(PatientDetail.this.patientPendingData.getEmail(), PatientDetail.this.patientPendingData.getPatient_id(), 0, PatientDetail.this.patientPendingData.getName());
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C10634 implements AlertCallBackWithButtonsInterface {
        C10634() {
        }

        public void positiveClick() {
            PatientDetail.this.patientDetailPresenter.acceptRequest(PatientDetail.this.patientPendingData.getEmail(), PatientDetail.this.patientPendingData.getAcceptReject_Id(), 0, "2", PatientDetail.this.tv_name_field.getText().toString().toLowerCase(), PatientDetail.this.CreateId, PatientDetail.this.CreateName, PatientDetail.this.CreateUserType, PatientDetail.this.Dr_id);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.patient_details);
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (getIntent().hasExtra("isFromActive")) {
            this.isFromActive = getIntent().getIntExtra("isFromActive", 0);
        }
        this.patientPendingData = SharedPreff.getDocPatientDetail();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.patientDetailPresenter = new PatientDetailPresenterImp(this, this.netWorkingService);
        initView();
        settingData();
    }

    private void settingData() {
        if (this.patientPendingData != null) {
            Log.e("patientPendingData", new Gson().toJson(this.patientPendingData));
            this.tv_name_field.setText(this.patientPendingData.getName());
            this.tv_gender_field.setText(this.patientPendingData.getGender());
            this.tv_age_field.setText(this.patientPendingData.getDOB());
            this.tv_phone_field.setText("+1(" + this.patientPendingData.getCode() + ")" + CommonUtils.phoneFormatter(this.patientPendingData.getMobile()));
            this.tv_address_field.setText(this.patientPendingData.getAddress());
            this.tv_subscription_field.setText(this.patientPendingData.getSubscription());
            Picasso.with(this).load(Constants.HTTP + this.patientPendingData.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_profile);
        }
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.CreateId = SharedPreff.getStaffLoginResponse().getData().get_id();
            this.CreateName = SharedPreff.getStaffLoginResponse().getData().getFirstName() + " " + SharedPreff.getStaffLoginResponse().getData().getLastName();
            this.CreateUserType = "" + SharedPreff.getStaffLoginResponse().getData().getUserType();
            this.Dr_id = SharedPreff.getLoginResponce().getData().get_id();
        } else {
            this.CreateId = SharedPreff.getLoginResponce().getData().get_id();
            this.CreateName = SharedPreff.getLoginResponce().getData().getFirstName() + " " + SharedPreff.getLoginResponce().getData().getLastName();
            this.CreateUserType = "" + SharedPreff.getLoginResponce().getData().getUserType();
            this.Dr_id = SharedPreff.getLoginResponce().getData().get_id();
        }
        try {
            this.PatientName = this.tv_name_field.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.isFromActive == 0) {
            this.linear_cancel_subscription.setVisibility(8);
            this.linear_confirm.setVisibility(0);
            this.linear_decline.setVisibility(0);
            this.linear_call_again.setVisibility(8);
            this.tv_cancel_sub.setVisibility(8);
            this.card_subscription.setVisibility(8);
        } else if (this.isFromActive == 2) {
            this.linear_cancel_subscription.setVisibility(8);
            this.linear_confirm.setVisibility(8);
            this.linear_decline.setVisibility(8);
            this.linear_call_again.setVisibility(0);
            this.tv_cancel_sub.setVisibility(0);
            this.card_subscription.setVisibility(8);
            this.linear_call_again.setVisibility(8);
            this.tv_cancel_sub.setVisibility(8);
            this.patientDetailPresenter.checkChatButtonVisibility(SharedPreff.getLoginResponce().getData().get_id(), this.patientPendingData.getPatient_id());
        } else {
            this.linear_cancel_subscription.setVisibility(0);
            this.linear_confirm.setVisibility(8);
            this.linear_decline.setVisibility(8);
            this.linear_call_again.setVisibility(8);
            this.tv_cancel_sub.setVisibility(0);
            this.card_subscription.setVisibility(8);
            if (SharedPreff.getStaffLoginResponse() != null) {
                this.tv_prescription.setText(getResources().getString(C1020R.string.chat_now));
                this.tv_cancel_sub.setVisibility(0);
            } else if (Constants.PRESCRIPTION_HIDE == 0) {
                this.linear_cancel_subscription.setVisibility(8);
            }
            this.linear_call_again.setVisibility(8);
            this.tv_cancel_sub.setVisibility(8);
            this.patientDetailPresenter.checkChatButtonVisibility(SharedPreff.getLoginResponce().getData().get_id(), this.patientPendingData.getPatient_id());
        }
    }

    private void initView() {
        this.tv_name_field = (TextView) findViewById(C1020R.id.tv_name_field);
        this.tv_gender_field = (TextView) findViewById(C1020R.id.tv_gender_field);
        this.tv_age_field = (TextView) findViewById(C1020R.id.tv_age_field);
        this.tv_phone_field = (TextView) findViewById(C1020R.id.tv_phone_field);
        this.tv_address_field = (TextView) findViewById(C1020R.id.tv_address_field);
        this.tv_cancel_sub = (TextView) findViewById(C1020R.id.tv_cancel_sub);
        this.tv_subscription_field = (TextView) findViewById(C1020R.id.tv_subscription_field);
        this.tv_prescription = (TextView) findViewById(C1020R.id.tv_prescription);
        this.card_subscription = (CardView) findViewById(C1020R.id.card_subscription);
        this.call_linear = (LinearLayout) findViewById(C1020R.id.call_linear);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.img_profile = (CircleImageView) findViewById(C1020R.id.img_profile);
        this.linear_cancel_subscription = (LinearLayout) findViewById(C1020R.id.linear_cancel_subscription);
        this.linear_confirm = (LinearLayout) findViewById(C1020R.id.linear_confirm);
        this.linear_decline = (LinearLayout) findViewById(C1020R.id.linear_decline);
        this.linear_call_again = (LinearLayout) findViewById(C1020R.id.linear_call_again);
        clickListners();
    }

    private void clickListners() {
        this.linear_cancel_subscription.setOnClickListener(this);
        this.linear_confirm.setOnClickListener(this);
        this.linear_decline.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.tv_cancel_sub.setOnClickListener(this);
        this.linear_call_again.setOnClickListener(this);
        this.call_linear.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.linear_confirm:
                if (this.patientPendingData != null) {
                    this.patientDetailPresenter.acceptRequest(this.patientPendingData.getEmail(), this.patientPendingData.getAcceptReject_Id(), 0, AppEventsConstants.EVENT_PARAM_VALUE_YES, this.tv_name_field.getText().toString().toLowerCase(), this.CreateId, this.CreateName, this.CreateUserType, this.Dr_id);
                    return;
                }
                return;
            case C1020R.id.call_linear:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.please_confirm_your_call), getResources().getString(C1020R.string.You_re_about_to_call_the_following) + "\n" + this.tv_phone_field.getText().toString(), getResources().getString(C1020R.string.no_dialog), getResources().getString(C1020R.string.yes_dialog), "", false, true, new C10601());
                return;
            case C1020R.id.linear_cancel_subscription:
                if (this.tv_prescription.getText().toString().equalsIgnoreCase(getResources().getString(C1020R.string.chat_now))) {
                    this.patientDetailPresenter.chatNowApi(this.patientPendingData.getPatient_id(), this.patientPendingData);
                    return;
                } else {
                    DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.do_you_need_to_write_a_prescription_for_your_patient), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10612());
                    return;
                }
            case C1020R.id.linear_call_again:
                this.patientDetailPresenter.makeVideoCall(this.patientPendingData.getBookingId(), this.patientPendingData.getPateientID(), this.patientPendingData.getDrschedulesetsId());
                return;
            case C1020R.id.linear_decline:
                if (this.patientPendingData != null) {
                    DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_decline_the_request), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10634());
                    return;
                }
                return;
            case C1020R.id.tv_cancel_sub:
                if (this.patientPendingData != null) {
                    DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_deactivate_this_patient_account), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10623());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error);
    }

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void successfullyDeleted(int pos) {
        finish();
    }

    public void successfullyAccepted(int pos) {
        finish();
    }

    public void successfullyUnfriend(int pos) {
        Intent signUp = new Intent(this, HomeScreen.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.patientDetailPresenter.unSubscribeCallbacks();
    }

    public void gotoVideoCallingScreen(String token, String ApiKey, String session, String groupId, String url, VideoCallResponse videoCallResponse) {
        VideoCallRequiredFields videoCallRequiredFields = new VideoCallRequiredFields();
        videoCallRequiredFields.setDrschedulesetsId(this.patientPendingData.getDrschedulesetsId());
        videoCallRequiredFields.setBookingId(this.patientPendingData.getBookingId());
        videoCallRequiredFields.setPatientEmail(this.patientPendingData.getEmail());
        try {
            videoCallRequiredFields.setPatientName(this.PatientName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoCallRequiredFields.setPatientId(this.patientPendingData.getPateientID());
        videoCallRequiredFields.setDrName(this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName());
        videoCallRequiredFields.setUser(this.loginDocResponce.getData().getUserType());
        videoCallRequiredFields.setGroupId(groupId);
        videoCallRequiredFields.setSchedule(CommonUtils.getCurrentDate());
        videoCallRequiredFields.setUrl(url);
        SharedPreff.saveVideoCallRequireData(videoCallRequiredFields);
        VideoCallScreen.TOKEN = token;
        VideoCallScreen.API_KEY = ApiKey;
        VideoCallScreen.SESSION_ID = session;
        VideoCallScreenChanged.TOKEN = token;
        VideoCallScreenChanged.API_KEY = ApiKey;
        VideoCallScreenChanged.SESSION_ID = session;
        VideoCallScreenChanged.USER_ID = this.patientPendingData.getPatient_id();
        VideoCallScreenChanged.REALAM_UNIQUE_ID = this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + this.patientPendingData.getPatient_id();
        try {
            VideoCallScreenChanged.INVITE_URL = videoCallResponse.getInviteurl();
            VideoCallScreenChanged.REDIRECT_URL = videoCallResponse.getRedirecturl();
        } catch (Exception e2) {
            e2.printStackTrace();
            VideoCallScreenChanged.INVITE_URL = videoCallResponse.getInviteurl();
            VideoCallScreenChanged.REDIRECT_URL = videoCallResponse.getInviteurl();
        }
        ChatUserForDb chatUserForDb = new ChatUserForDb();
        chatUserForDb.set_id(this.patientPendingData.getPatient_id());
        chatUserForDb.setAddress(this.patientPendingData.getAddress());
        chatUserForDb.setApiKey(ApiKey);
        chatUserForDb.setBio("");
        chatUserForDb.setCity("");
        chatUserForDb.setCode(this.patientPendingData.getCode());
        chatUserForDb.setCount(0);
        chatUserForDb.setMobile(this.patientPendingData.getMobile());
        chatUserForDb.setDeviceToken(this.patientPendingData.getDeviceToken());
        chatUserForDb.setSessionData(session);
        chatUserForDb.setProfilePic(this.patientPendingData.getProfilePic());
        chatUserForDb.setGroupId(groupId);
        chatUserForDb.setDOB(this.patientPendingData.getDOB());
        chatUserForDb.setEmail(this.patientPendingData.getEmail());
        chatUserForDb.setFirstName("");
        chatUserForDb.setUserType(0);
        chatUserForDb.setLastName("");
        chatUserForDb.setQualification("");
        chatUserForDb.setName(this.patientPendingData.getName());
        chatUserForDb.setTokenData(token);
        chatUserForDb.setGender(this.patientPendingData.getGender());
        SharedPreff.saveChatDbDetail(chatUserForDb);
        startActivity(new Intent(this, VideoCallScreenChanged.class));
    }

    public void gotoChatScreen(ChatUserForDb chatUserForDb) {
        MessageUserObject messageUserObject = new MessageUserObject();
        MessageUserDetail messageUserDetail = new MessageUserDetail();
        messageUserObject.setGender(chatUserForDb.getGender());
        messageUserObject.setTokenData(chatUserForDb.getTokenData());
        messageUserObject.setDOB(chatUserForDb.getDOB());
        messageUserObject.setName(chatUserForDb.getName());
        messageUserObject.setApiKey(chatUserForDb.getApiKey());
        messageUserObject.setCount(chatUserForDb.getCount());
        messageUserObject.setGroupId(chatUserForDb.getGroupId());
        messageUserObject.setDeviceToken(chatUserForDb.getDeviceToken());
        messageUserObject.setId(chatUserForDb.get_id());
        messageUserObject.setSessionData(chatUserForDb.getSessionData());
        messageUserObject.setProfilePic(chatUserForDb.getProfilePic());
        messageUserObject.setSpeciality(chatUserForDb.getSpeciality());
        messageUserObject.setType(chatUserForDb.getType());
        messageUserObject.setUserType(chatUserForDb.getUserType());
        messageUserDetail.setSpeciality(chatUserForDb.getSpeciality());
        messageUserDetail.setProfilePic(chatUserForDb.getProfilePic());
        messageUserDetail.setAddress(chatUserForDb.getAddress());
        messageUserDetail.setBio(chatUserForDb.getBio());
        messageUserDetail.setCity(chatUserForDb.getCity());
        messageUserDetail.setDOB(chatUserForDb.getDOB());
        messageUserDetail.setFirstName(chatUserForDb.getFirstName());
        messageUserDetail.setLastName(chatUserForDb.getLastName());
        messageUserDetail.setQualification(chatUserForDb.getQualification());
        messageUserDetail.setCode(chatUserForDb.getCode());
        messageUserDetail.setMobile(chatUserForDb.getMobile());
        messageUserObject.setUser(messageUserDetail);
        ChatScreen.TOKEN = messageUserObject.getTokenData();
        ChatScreen.API_KEY = messageUserObject.getApiKey();
        ChatScreen.SESSION_ID = messageUserObject.getSessionData();
        ChatScreen.USER_ID = chatUserForDb.get_id();
        if (SharedPreff.getStaffLoginResponse() != null) {
            ChatScreen.REALAM_UNIQUE_ID = SharedPreff.getStaffLoginResponse().getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + chatUserForDb.get_id();
        } else {
            ChatScreen.REALAM_UNIQUE_ID = SharedPreff.getLoginResponce().getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + chatUserForDb.get_id();
        }
        ChatScreen.isFetchfromServer = true;
        SharedPreff.savingChatDetailObject(messageUserObject);
        startActivity(new Intent(this, ChatScreen.class));
    }

    public void showChatNowButton(boolean value) {
        if (value) {
            this.linear_call_again.setVisibility(0);
            this.tv_cancel_sub.setVisibility(0);
            return;
        }
        this.linear_call_again.setVisibility(8);
        this.tv_cancel_sub.setVisibility(8);
    }
}
