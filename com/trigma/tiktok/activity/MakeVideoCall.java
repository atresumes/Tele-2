package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.Upcoming;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.model.VideoCallResponse;
import com.trigma.tiktok.presenter.MakeVideoCallPresenter;
import com.trigma.tiktok.presenter.MakeVideoCallPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MakeVideoCall extends BaseActivity implements OnClickListener {
    private String endDate = "";
    private ImageView img_back;
    private CircleImageView img_profile;
    private LinearLayout linear_confirm;
    private LoginDocResponce loginDocResponce;
    private MakeVideoCallPresenter makeVideoCallPresenter;
    private NetWorkingService netWorkingService;
    private SimpleDateFormat simpleDateFormat;
    private String startDate = "";
    private String[] toTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"};
    private String[] toTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM"};
    private ArrayList<String> toTimeArr12HrList;
    private ArrayList<String> toTimeArrList;
    private TextView tv_address;
    private TextView tv_age;
    private TextView tv_cancel_appointment;
    private TextView tv_gender;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_schedule;
    private Upcoming upcoming;

    class C10571 implements AlertCallBackWithButtonsInterface {
        C10571() {
        }

        public void positiveClick() {
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            CommonUtils.call(MakeVideoCall.this, MakeVideoCall.this.upcoming.getPatientId().getCode() + MakeVideoCall.this.upcoming.getPatientId().getMobile());
        }
    }

    class C10582 implements AlertCallBackWithButtonsInterface {
        C10582() {
        }

        public void positiveClick() {
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            MakeVideoCall.this.makeVideoCallPresenter.cancelAppointment(MakeVideoCall.this.upcoming);
        }
    }

    class C10593 implements AlertCallBackWithButtonsInterface {
        C10593() {
        }

        public void positiveClick() {
            MakeVideoCall.this.makeVideoCallPresenter.makeVideoCall(MakeVideoCall.this.upcoming.getBookingId(), MakeVideoCall.this.upcoming.getPatientId().get_id(), MakeVideoCall.this.upcoming.getDrschedulesetsId());
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.make_video_call);
        this.simpleDateFormat = new SimpleDateFormat("d-MMMMM-yyyy HH:mm:ss");
        this.loginDocResponce = SharedPreff.getLoginResponce();
        this.toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
        this.toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
        this.upcoming = SharedPreff.getUpcomingObject();
        initView();
        settingData();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.makeVideoCallPresenter = new MakeVideoCallPresenterImp(this, this.netWorkingService);
    }

    private void settingData() {
        if (this.upcoming != null) {
            this.startDate = this.upcoming.getSchedule() + " " + this.upcoming.getFrom();
            this.endDate = this.upcoming.getSchedule() + " " + this.upcoming.getTo();
            Log.e("Upcoming", new Gson().toJson(this.upcoming));
            this.tv_name.setText(this.upcoming.getPatientId().getFirstName() + " " + this.upcoming.getPatientId().getLastName());
            this.tv_address.setText(this.upcoming.getPatientId().getAddress());
            try {
                this.tv_schedule.setText(this.upcoming.getScheduleDate() + " at " + ((String) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(this.upcoming.getFrom()))) + "(" + getMin(gettingDiff(this.upcoming.getFrom(), this.upcoming.getTo())) + ")");
            } catch (Exception e) {
                e.printStackTrace();
                this.tv_schedule.setText(this.upcoming.getScheduleDate() + " at " + ((String) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(this.upcoming.getFrom()))));
            }
            this.tv_gender.setText(this.upcoming.getPatientId().getGender());
            this.tv_age.setText(this.upcoming.getAge());
            this.tv_phone.setText("+1(" + this.upcoming.getPatientId().getCode() + ")" + CommonUtils.phoneFormatter(this.upcoming.getPatientId().getMobile()));
            Picasso.with(this).load(Constants.HTTP + this.upcoming.getProfile()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_profile);
        }
    }

    private void initView() {
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.img_profile = (CircleImageView) findViewById(C1020R.id.img_profile);
        this.tv_cancel_appointment = (TextView) findViewById(C1020R.id.tv_cancel_appointment);
        this.tv_name = (TextView) findViewById(C1020R.id.tv_name);
        this.tv_address = (TextView) findViewById(C1020R.id.tv_address);
        this.tv_schedule = (TextView) findViewById(C1020R.id.tv_schedule);
        this.tv_gender = (TextView) findViewById(C1020R.id.tv_gender);
        this.tv_age = (TextView) findViewById(C1020R.id.tv_age);
        this.tv_phone = (TextView) findViewById(C1020R.id.tv_phone);
        this.linear_confirm = (LinearLayout) findViewById(C1020R.id.linear_confirm);
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.linear_confirm.setVisibility(8);
        }
        clickListners();
    }

    private void clickListners() {
        this.tv_cancel_appointment.setOnClickListener(this);
        this.linear_confirm.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.tv_phone.setOnClickListener(this);
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error);
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.tv_phone:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.please_confirm_your_call), getResources().getString(C1020R.string.You_re_about_to_call_the_following) + "\n" + this.tv_phone.getText().toString(), getResources().getString(C1020R.string.no_dialog), getResources().getString(C1020R.string.yes_dialog), "", false, true, new C10571());
                return;
            case C1020R.id.linear_confirm:
                try {
                    Date dateFrom = this.simpleDateFormat.parse(this.startDate + ":00");
                    Date dateTo = this.simpleDateFormat.parse(this.endDate + ":00");
                    if (new Date().after(dateFrom) && new Date().before(dateTo)) {
                        this.makeVideoCallPresenter.makeVideoCall(this.upcoming.getBookingId(), this.upcoming.getPatientId().get_id(), this.upcoming.getDrschedulesetsId());
                        return;
                    } else {
                        DialogPopUps.showAlertWithButtons(this, "", getResources().getString(C1020R.string.you_are_trying_to_start_the_consultation_before_the_schedule_time_do_you_really_want_to_start_early), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10593());
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
            case C1020R.id.tv_cancel_appointment:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.canceled_appointment), getResources().getString(C1020R.string.you_have_cancelled_this_appointment_with_your_patient), getResources().getString(C1020R.string.go_back), getResources().getString(C1020R.string.confirm), "", true, false, new C10582());
                return;
            default:
                return;
        }
    }

    public String getMin(int diff) {
        String result = "";
        if (diff == 1) {
            return "15 Min";
        }
        if (diff == 2) {
            return "30 Min";
        }
        if (diff == 4) {
            return "60 Min";
        }
        if (diff == 6) {
            return "90 Min";
        }
        return result;
    }

    private int gettingDiff(String from, String to) {
        try {
            return this.toTimeArrList.indexOf(to) - this.toTimeArrList.indexOf(from);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public void cancelSuccess() {
        if (SharedPreff.getStaffLoginResponse() != null) {
            Intent signUp = new Intent(this, StaffHomeScreen.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
            return;
        }
        signUp = new Intent(this, HomeScreen.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    public void gotoVideoCallingScreen(String token, String ApiKey, String session, String groupId, String url, VideoCallResponse videoCallResponse) {
        VideoCallRequiredFields videoCallRequiredFields = new VideoCallRequiredFields();
        videoCallRequiredFields.setDrschedulesetsId(this.upcoming.getDrschedulesetsId());
        videoCallRequiredFields.setBookingId(this.upcoming.getBookingId());
        videoCallRequiredFields.setPatientEmail(this.upcoming.getPatientId().getEmail());
        try {
            videoCallRequiredFields.setPatientName(this.upcoming.getPatientId().getFirstName() + " " + this.upcoming.getPatientId().getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoCallRequiredFields.setPatientId(this.upcoming.getPatientId().get_id());
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
        VideoCallScreenChanged.USER_ID = this.upcoming.getPatientId().get_id();
        VideoCallScreenChanged.REALAM_UNIQUE_ID = this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + this.upcoming.getPatientId().get_id();
        try {
            VideoCallScreenChanged.INVITE_URL = videoCallResponse.getInviteurl();
            VideoCallScreenChanged.REDIRECT_URL = videoCallResponse.getRedirecturl();
        } catch (Exception e2) {
            e2.printStackTrace();
            VideoCallScreenChanged.INVITE_URL = videoCallResponse.getInviteurl();
            VideoCallScreenChanged.REDIRECT_URL = videoCallResponse.getInviteurl();
        }
        ChatUserForDb chatUserForDb = new ChatUserForDb();
        chatUserForDb.set_id(this.upcoming.getPatientId().get_id());
        chatUserForDb.setAddress(this.upcoming.getPatientId().getAddress());
        chatUserForDb.setApiKey(ApiKey);
        chatUserForDb.setBio("");
        chatUserForDb.setCity("");
        chatUserForDb.setCode(this.upcoming.getPatientId().getCode());
        chatUserForDb.setCount(0);
        chatUserForDb.setUserType(0);
        chatUserForDb.setMobile(this.upcoming.getPatientId().getMobile());
        chatUserForDb.setDeviceToken(this.upcoming.getPatientId().getDeviceToken());
        chatUserForDb.setSessionData(session);
        chatUserForDb.setProfilePic(this.upcoming.getPatientId().getProfilePic());
        chatUserForDb.setGroupId(groupId);
        chatUserForDb.setDOB(this.upcoming.getPatientId().getDOB());
        chatUserForDb.setEmail(this.upcoming.getPatientId().getEmail());
        chatUserForDb.setFirstName(this.upcoming.getPatientId().getFirstName());
        chatUserForDb.setLastName(this.upcoming.getPatientId().getLastName());
        chatUserForDb.setQualification("");
        chatUserForDb.setName(this.upcoming.getPatientId().getFirstName() + "" + this.upcoming.getPatientId().getLastName());
        chatUserForDb.setTokenData(token);
        chatUserForDb.setGender(this.upcoming.getPatientId().getGender());
        SharedPreff.saveChatDbDetail(chatUserForDb);
        startActivity(new Intent(this, VideoCallScreenChanged.class));
    }
}
