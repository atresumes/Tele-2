package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.adapter.DrNotiifcationAdapter;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.NotificationMainObject;
import com.trigma.tiktok.presenter.NotificationPresenter;
import com.trigma.tiktok.presenter.NotificationPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;

public class Notification extends BaseFragment implements OnClickListener {
    private Activity activity;
    private View appointment_error_msg;
    private DrNotiifcationAdapter drNotiifcationAdapter;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    LoginDocResponce loginDocResponce;
    public NetWorkingService netWorkingService;
    private NotificationPresenter notificationPresenter;
    private RecyclerView recyclerView;
    private TextView title_name;
    private TextView tv_error_msg;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.notification, container, false);
        this.loginDocResponce = SharedPreff.getLoginResponce();
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.recyclerView = (RecyclerView) screen.findViewById(C1020R.id.recyclerView);
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_error_msg = (TextView) screen.findViewById(C1020R.id.tv_error_msg);
        this.tv_error_msg.setVisibility(8);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.img_error = (ImageView) screen.findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.no_notification);
        this.appointment_error_msg = screen.findViewById(C1020R.id.appointment_error_msg);
        clickListners();
    }

    private void clickListners() {
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.notifications_title));
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.notificationPresenter = new NotificationPresenterImp(this, this.netWorkingService);
        settingRecyclerView();
    }

    private void settingRecyclerView() {
        this.drNotiifcationAdapter = new DrNotiifcationAdapter(this.notificationPresenter, this);
        this.recyclerView.setAdapter(this.drNotiifcationAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
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

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this.activity, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this.activity, error);
    }

    public void showToastError(String error) {
        Toast.makeText(this.activity.getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void onDestroyView() {
        super.onDestroyView();
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
                getActivity().finish();
                return;
            default:
                return;
        }
    }

    public void setNotiifcation(ArrayList<NotificationMainObject> notificationList) {
        if (notificationList.size() > 0) {
            this.drNotiifcationAdapter.addingList(notificationList);
            this.appointment_error_msg.setVisibility(8);
            this.recyclerView.setVisibility(0);
            return;
        }
        this.appointment_error_msg.setVisibility(0);
        this.recyclerView.setVisibility(8);
    }
}
