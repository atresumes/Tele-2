package com.trigma.tiktok.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.BuildConfig;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.BaseActivity;
import com.trigma.tiktok.activity.ChatScreen;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.activity.DoctorStaff;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.MessageUserDetail;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.presenter.patient.DoctorBioPresenter;
import com.trigma.tiktok.presenter.patient.DoctorBioPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.services.events.EventsFilesManager;

public class DoctorBio extends BaseActivity implements OnClickListener {
    private static final int REQUEST_CODE = 101;
    private CardView card_phone;
    private DoctorBioPresenter doctorBioPresenter;
    private String drID = "";
    private DrSearchNameObject drSearchNameObject;
    private ImageView img_back;
    private CircleImageView img_profile;
    private ImageView img_slider;
    private LinearLayout linear_confirm;
    public NetWorkingService netWorkingService;
    private ScrollView scrollView;
    private TextView send_request;
    private boolean showSendRequestButton = true;
    private TextView tv_address;
    private TextView tv_bio;
    private TextView tv_language;
    private TextView tv_mobile;
    private TextView tv_name;
    private TextView tv_name_field;
    private TextView tv_qualification;
    private TextView tv_title;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.doctor_bio);
        this.drSearchNameObject = SharedPreff.getDocSearchDetail();
        Log.e("drSearchNameObject", "" + new Gson().toJson(this.drSearchNameObject));
        try {
            Log.e("drSearchNameObject", new Gson().toJson(this.drSearchNameObject));
            if (BuildConfig.FLAVOR.equals("Health4Life")) {
                if (this.drSearchNameObject.get_id() != null) {
                    this.drID = this.drSearchNameObject.get_id();
                } else {
                    this.drID = this.drSearchNameObject.getDr_id();
                }
                if (getIntent().hasExtra("showSendRequestButton")) {
                    this.showSendRequestButton = getIntent().getBooleanExtra("showSendRequestButton", true);
                }
                initViews();
                settingData(this.drSearchNameObject);
                this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
                this.doctorBioPresenter = new DoctorBioPresenterImp(this, this.netWorkingService);
            }
            this.drID = this.drSearchNameObject.getDrId();
            if (getIntent().hasExtra("showSendRequestButton")) {
                this.showSendRequestButton = getIntent().getBooleanExtra("showSendRequestButton", true);
            }
            initViews();
            settingData(this.drSearchNameObject);
            this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
            this.doctorBioPresenter = new DoctorBioPresenterImp(this, this.netWorkingService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.tv_title = (TextView) findViewById(C1020R.id.tv_title);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_slider.setImageResource(C1020R.drawable.add_staff_new);
        this.send_request = (TextView) findViewById(C1020R.id.send_request);
        this.tv_name_field = (TextView) findViewById(C1020R.id.tv_name_field);
        this.tv_name = (TextView) findViewById(C1020R.id.tv_name);
        this.tv_qualification = (TextView) findViewById(C1020R.id.tv_qualification);
        this.tv_language = (TextView) findViewById(C1020R.id.tv_language);
        this.tv_mobile = (TextView) findViewById(C1020R.id.tv_mobile);
        this.tv_address = (TextView) findViewById(C1020R.id.tv_address);
        this.tv_bio = (TextView) findViewById(C1020R.id.tv_bio);
        this.card_phone = (CardView) findViewById(C1020R.id.card_phone);
        this.linear_confirm = (LinearLayout) findViewById(C1020R.id.linear_confirm);
        this.img_profile = (CircleImageView) findViewById(C1020R.id.img_profile);
        this.scrollView = (ScrollView) findViewById(C1020R.id.scrollView);
        if (!SharedPreff.isFirstTimePharmacySelect()) {
            this.img_slider.setVisibility(0);
            this.img_slider.setOnClickListener(this);
        }
        settingClickListner();
    }

    private void settingClickListner() {
        this.img_back.setOnClickListener(this);
        this.linear_confirm.setOnClickListener(this);
        this.card_phone.setOnClickListener(this);
    }

    public void settingButtonText(String name) {
        this.send_request.setText(name);
        if (name.equalsIgnoreCase(getResources().getString(C1020R.string.chat_now))) {
            this.img_slider.setVisibility(0);
            this.img_slider.setOnClickListener(this);
            return;
        }
        this.img_slider.setVisibility(8);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.img_slider:
                Intent signUp = new Intent(this, DoctorStaff.class);
                signUp.putExtra(Constants.SHOW_BACK, true);
                signUp.putExtra(Constants.HIDE_DEACTIVATE, true);
                signUp.putExtra(Constants.DOCTOR_ID, this.drID);
                signUp.putExtra(Constants.DOCTOR_NAME, this.tv_name.getText().toString());
                startActivity(signUp);
                return;
            case C1020R.id.linear_confirm:
                if (this.send_request.getText().toString().equalsIgnoreCase(getResources().getString(C1020R.string.chat_now))) {
                    this.doctorBioPresenter.chatNowApi(this.drID, this.drSearchNameObject);
                    return;
                } else {
                    this.doctorBioPresenter.sendRequestToDoc(this.drID, this.drSearchNameObject.getEmail(), this.drSearchNameObject.getName(), this.drSearchNameObject.getDeviceToken(), this.drSearchNameObject.getName(), this.drSearchNameObject.getDeviceType());
                    return;
                }
            case C1020R.id.card_phone:
                this.doctorBioPresenter.makeCall(this.drSearchNameObject.getCode() + "" + this.drSearchNameObject.getMobile(), this.tv_mobile.getText().toString());
                return;
            default:
                return;
        }
    }

    public void settingData(DrSearchNameObject tingData) {
        this.tv_name_field.setText(tingData.getSpeciality());
        if (tingData.getName().contains(getResources().getString(C1020R.string.dr))) {
            this.tv_name.setText(tingData.getName());
        } else {
            this.tv_name.setText(getResources().getString(C1020R.string.dr) + " " + tingData.getName());
        }
        this.tv_qualification.setText(tingData.getQualification());
        this.tv_language.setText(tingData.getLanguages());
        this.tv_mobile.setText("+1(" + tingData.getCode() + ")" + CommonUtils.phoneFormatter(tingData.getMobile()));
        this.tv_address.setText(tingData.getAddress());
        this.tv_bio.setText(tingData.getBio());
        Picasso.with(this).load(Constants.HTTP + tingData.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_profile);
        if (this.showSendRequestButton) {
            this.linear_confirm.setVisibility(0);
        } else {
            this.linear_confirm.setVisibility(8);
        }
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error);
    }

    public void finishActivity() {
        finish();
    }

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void goToSelectPharmacy() {
        if (SharedPreff.isFirstTimePharmacySelect()) {
            Intent selectPharmacy = new Intent(this, SelectPharmacy.class);
            selectPharmacy.setFlags(268468224);
            selectPharmacy.putExtra(Constants.SHOW_BACK, false);
            startActivityForResult(selectPharmacy, 101);
            return;
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 101 || resultCode != -1 || data == null) {
            return;
        }
        if (SharedPreff.getLoginResponce() != null) {
            finish();
            return;
        }
        Intent signUp = new Intent(this, DashBoard.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.doctorBioPresenter.unSubscribeCallbacks();
    }

    public void finishScreen() {
        finish();
    }

    public void gotoChatScreen(ChatUserForDb chatUserForDb) {
        MessageUserObject messageUserObject = new MessageUserObject();
        MessageUserDetail messageUserDetail = new MessageUserDetail();
        messageUserObject.setGender("");
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
}
