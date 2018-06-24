package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.HomeScreenPatient;
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.adapter.NotificationFromPushAdapter;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.NotificationMainObject;
import com.trigma.tiktok.presenter.NotificationFromPushPresenter;
import com.trigma.tiktok.presenter.NotificationFromPushPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;

public class NotificationFromPush extends BaseActivity implements OnClickListener {
    private View appointment_error_msg;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    LoginDocResponce loginDocResponce;
    public NetWorkingService netWorkingService;
    private NotificationFromPushAdapter notificationFromPushAdapter;
    private NotificationFromPushPresenter notificationPresenter;
    private RecyclerView recyclerView;
    private TextView title_name;
    private TextView tv_error_msg;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loginDocResponce = SharedPreff.getLoginResponce();
        setContentView((int) C1020R.layout.notification);
        initViews();
    }

    private void initViews() {
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.tv_error_msg = (TextView) findViewById(C1020R.id.tv_error_msg);
        this.tv_error_msg.setVisibility(8);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_slider.setVisibility(4);
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.img_error = (ImageView) findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.no_notification);
        this.appointment_error_msg = findViewById(C1020R.id.appointment_error_msg);
        clickListners();
        this.title_name.setText(getResources().getString(C1020R.string.notifications_title));
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.notificationPresenter = new NotificationFromPushPresenterImp(this, this.netWorkingService);
        settingRecyclerView();
    }

    private void settingRecyclerView() {
        this.notificationFromPushAdapter = new NotificationFromPushAdapter(this.notificationPresenter, this);
        this.recyclerView.setAdapter(this.notificationFromPushAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setVisibility(8);
        if (this.loginDocResponce == null) {
            return;
        }
        if (this.loginDocResponce.getData().getUserType() == 0) {
            this.notificationPresenter.getPatientNotification();
        } else {
            this.notificationPresenter.getNotification();
        }
    }

    private void clickListners() {
        this.img_header_back.setOnClickListener(this);
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

    protected void onDestroy() {
        super.onDestroy();
        this.notificationPresenter.unSubscribeCallbacks();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                if (this.loginDocResponce == null) {
                    return;
                }
                if (this.loginDocResponce.getData().getUserType() == 0) {
                    PatientMainActivity.openDrawer();
                    return;
                } else {
                    MainActivity.openDrawer();
                    return;
                }
            case C1020R.id.img_header_back:
                if (this.loginDocResponce == null) {
                    return;
                }
                Intent signUp;
                if (this.loginDocResponce.getData().getUserType() == 0) {
                    signUp = new Intent(this, HomeScreenPatient.class);
                    signUp.setFlags(268468224);
                    startActivity(signUp);
                    return;
                }
                signUp = new Intent(this, HomeScreen.class);
                signUp.setFlags(268468224);
                startActivity(signUp);
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (this.loginDocResponce.getData().getUserType() == 0) {
            Intent signUp = new Intent(this, HomeScreenPatient.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
            return;
        }
        signUp = new Intent(this, HomeScreen.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    public void setNotiifcation(ArrayList<NotificationMainObject> notificationList) {
        if (notificationList.size() > 0) {
            this.notificationFromPushAdapter.addingList(notificationList);
            this.appointment_error_msg.setVisibility(8);
            this.recyclerView.setVisibility(0);
            return;
        }
        this.appointment_error_msg.setVisibility(0);
        this.recyclerView.setVisibility(8);
    }
}
