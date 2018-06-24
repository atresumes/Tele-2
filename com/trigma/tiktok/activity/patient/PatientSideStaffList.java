package com.trigma.tiktok.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.BaseActivity;
import com.trigma.tiktok.activity.ChatScreen;
import com.trigma.tiktok.activity.StaffDetail;
import com.trigma.tiktok.adapter.patient.PatientSideStaffAdapter;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.MessageUserDetail;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.presenter.patient.PatientSideStaffListPresenter;
import com.trigma.tiktok.presenter.patient.PatientSideStaffListPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.ArrayList;

public class PatientSideStaffList extends BaseActivity implements OnClickListener {
    private ImageView img_header_back;
    private ImageView img_slider;
    public NetWorkingService netWorkingService;
    private PatientSideStaffAdapter patientSideStaffAdapter;
    private PatientSideStaffListPresenter patientSideStaffListPresenter;
    private RecyclerView recyclerView;
    private RelativeLayout rel_doc;
    private TextView title_name;
    private TextView tv_doctor;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.patient_side_staff);
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        initViews();
    }

    private void initViews() {
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.rel_doc = (RelativeLayout) findViewById(C1020R.id.rel_doc);
        this.title_name.setText(getResources().getString(C1020R.string.doctor_s_staff));
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.tv_doctor = (TextView) findViewById(C1020R.id.tv_doctor);
        this.img_slider.setVisibility(4);
        clickListners();
        settingRecyclerView();
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.img_slider.setVisibility(8);
        }
    }

    private void settingRecyclerView() {
        this.patientSideStaffListPresenter = new PatientSideStaffListPresenterImp(this, this.netWorkingService);
        this.patientSideStaffAdapter = new PatientSideStaffAdapter(this.patientSideStaffListPresenter, this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setVisibility(8);
        this.recyclerView.setAdapter(this.patientSideStaffAdapter);
    }

    private void clickListners() {
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
        this.rel_doc.setOnClickListener(this);
    }

    public void settingDoctor(String doc) {
        this.tv_doctor.setText(doc);
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

    public void addingDataToList(ArrayList<DrStaffListObject> dataList) {
        ArrayList<DrStaffListObject> dataListMain = new ArrayList();
        if (this.patientSideStaffAdapter != null && dataList.size() > 0) {
            for (int a = 0; a < dataList.size(); a++) {
                if (((DrStaffListObject) dataList.get(a)).getStaffStatus() == 1) {
                    dataListMain.add(dataList.get(a));
                }
            }
            if (dataListMain.size() > 0) {
                this.recyclerView.setVisibility(0);
                this.patientSideStaffAdapter.addingList(dataListMain);
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_header_back:
                finish();
                return;
            case C1020R.id.rel_doc:
                this.patientSideStaffListPresenter.openDoctorPicker();
                return;
            default:
                return;
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
        ChatScreen.REALAM_UNIQUE_ID = SharedPreff.getLoginResponce().getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + chatUserForDb.get_id();
        ChatScreen.isFetchfromServer = true;
        SharedPreff.savingChatDetailObject(messageUserObject);
        startActivity(new Intent(this, ChatScreen.class));
    }

    public void gotoStaffDetail(DrStaffListObject drStaffListObject) {
        SharedPreff.saveStaffDetailListObject(drStaffListObject);
        startActivity(new Intent(this, StaffDetail.class));
    }
}
