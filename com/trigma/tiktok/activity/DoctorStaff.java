package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.adapter.DoctorStaffAdapter;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageUserDetail;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.presenter.DoctorStaffPresenter;
import com.trigma.tiktok.presenter.DoctorStaffPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.ArrayList;

public class DoctorStaff extends BaseActivity implements OnClickListener {
    private static final int REQUEST_CODE = 100;
    private View appointment_error_msg;
    private int countLimit = 0;
    private String doc_id = "";
    private String doc_name = "";
    private DoctorStaffAdapter doctorStaffAdapter;
    private DoctorStaffPresenter doctorStaffPresenter;
    private boolean hide_deactivate = true;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    private TextView title_name;
    private TextView tv_error_msg;

    class C10491 implements AlertCallBackInterface {
        C10491() {
        }

        public void neutralClick() {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.doctors_staff);
        if (getIntent().hasExtra(Constants.DOCTOR_ID)) {
            this.doc_id = getIntent().getStringExtra(Constants.DOCTOR_ID);
        }
        if (getIntent().hasExtra(Constants.HIDE_DEACTIVATE)) {
            this.hide_deactivate = getIntent().getBooleanExtra(Constants.HIDE_DEACTIVATE, true);
        }
        if (getIntent().hasExtra(Constants.DOCTOR_NAME)) {
            this.doc_name = getIntent().getStringExtra(Constants.DOCTOR_NAME);
        }
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        initViews();
    }

    private void initViews() {
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.tv_error_msg = (TextView) findViewById(C1020R.id.tv_error_msg);
        this.tv_error_msg.setVisibility(8);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.img_error = (ImageView) findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.no_staffmember);
        this.img_slider.setImageResource(C1020R.drawable.add_staff);
        this.appointment_error_msg = findViewById(C1020R.id.appointment_error_msg);
        clickListners();
        settingRecyclerView();
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.img_slider.setVisibility(8);
        }
    }

    private void settingRecyclerView() {
        this.doctorStaffPresenter = new DoctorStaffPresenterImp(this, this.netWorkingService);
        this.doctorStaffAdapter = new DoctorStaffAdapter(this.doctorStaffPresenter, this, this.doc_id);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setVisibility(8);
        this.recyclerView.setAdapter(this.doctorStaffAdapter);
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.doctorStaffPresenter.fetchMyStaff(this.doc_id, SharedPreff.getStaffLoginResponse().getData().get_id());
        } else {
            this.doctorStaffPresenter.fetchMyStaff(this.doc_id, "ab");
        }
    }

    public void settingDoctorName(String name, String loggedUserID) {
        this.title_name.setText(this.doc_name + " " + getResources().getString(C1020R.string.staff));
        if (this.doc_id.equalsIgnoreCase(loggedUserID)) {
            this.img_slider.setVisibility(0);
            this.img_slider.setEnabled(true);
            return;
        }
        this.img_slider.setVisibility(4);
        this.img_slider.setEnabled(false);
    }

    private void clickListners() {
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
    }

    public void settingCountLimit(int a) {
        this.countLimit = a;
        Log.e("countLimit", "" + this.countLimit);
        LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
        if (loginDocResponce != null) {
            loginDocResponce.getData().setAddStaffLimit("" + a);
            SharedPreff.saveLoginResponce(loginDocResponce);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                if (this.countLimit == 0) {
                    DialogPopUps.alertPopUp(this, getResources().getString(C1020R.string.you_reached_the_limit_of_adding_staff), getResources().getString(C1020R.string.ok), new C10491());
                    return;
                } else {
                    goToAddStaff();
                    return;
                }
            case C1020R.id.img_header_back:
                finish();
                return;
            default:
                return;
        }
    }

    public void addingDataToList(ArrayList<DrStaffListObject> dataList) {
        ArrayList<DrStaffListObject> dataListMain = new ArrayList();
        if (this.doctorStaffAdapter != null && dataList.size() > 0) {
            if (this.hide_deactivate) {
                for (int a = 0; a < dataList.size(); a++) {
                    if (((DrStaffListObject) dataList.get(a)).getStaffStatus() == 1) {
                        dataListMain.add(dataList.get(a));
                    }
                }
                if (dataListMain.size() > 0) {
                    this.recyclerView.setVisibility(0);
                    this.appointment_error_msg.setVisibility(8);
                    this.doctorStaffAdapter.addingList(dataListMain);
                    return;
                }
                return;
            }
            this.recyclerView.setVisibility(0);
            this.appointment_error_msg.setVisibility(8);
            this.doctorStaffAdapter.addingList(dataList);
        }
    }

    public void goToAddStaff() {
        startActivityForResult(new Intent(this, AddStaff.class), 100);
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

    public void gotoHomeScreen() {
        Intent signUp = new Intent(this, HomeScreen.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    public void gotoStaffDetail(DrStaffListObject drStaffListObject) {
        SharedPreff.saveStaffDetailListObject(drStaffListObject);
        startActivity(new Intent(this, StaffDetail.class));
    }

    public void activateDeactivate(String activate_status, int pos) {
        if (this.doctorStaffAdapter != null) {
            if (activate_status.equalsIgnoreCase("5")) {
                gotoHomeScreen();
            }
            this.doctorStaffAdapter.activateDeactivate(pos, activate_status);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1 && data != null) {
            this.recyclerView.setVisibility(8);
            this.appointment_error_msg.setVisibility(0);
            this.doctorStaffPresenter.fetchMyStaff(this.doc_id, "ab");
        }
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
}
