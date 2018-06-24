package com.trigma.tiktok.fragments.patient;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.fragments.BaseFragment;
import com.trigma.tiktok.presenter.patient.SelfSchedulePresenter;
import com.trigma.tiktok.presenter.patient.SelfSchedulePresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;

public class SelfSchedule extends BaseFragment implements OnClickListener {
    private Activity activity;
    private ImageView img_header_back;
    private ImageView img_slider;
    private LinearLayout linear_save;
    public NetWorkingService netWorkingService;
    private RelativeLayout rel_date;
    private RelativeLayout rel_doc;
    private RelativeLayout rel_duration;
    private RelativeLayout rel_month;
    private RelativeLayout rel_time;
    private RelativeLayout rel_year;
    private SelfSchedulePresenter selfSchedulePresenter;
    private TextView title_name;
    private TextView tv_date_1;
    private TextView tv_date_app_selected;
    private TextView tv_doc_selected;
    private TextView tv_doctor;
    private TextView tv_duration;
    private TextView tv_duration_selected;
    private TextView tv_month;
    private TextView tv_time;
    private TextView tv_time_selected;
    private TextView tv_year;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.self_schedule, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.tv_doctor = (TextView) screen.findViewById(C1020R.id.tv_doctor);
        this.tv_year = (TextView) screen.findViewById(C1020R.id.tv_year);
        this.tv_month = (TextView) screen.findViewById(C1020R.id.tv_month);
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_date_1 = (TextView) screen.findViewById(C1020R.id.tv_date_1);
        this.tv_duration = (TextView) screen.findViewById(C1020R.id.tv_duration);
        this.tv_time = (TextView) screen.findViewById(C1020R.id.tv_time);
        this.tv_doc_selected = (TextView) screen.findViewById(C1020R.id.tv_doc_selected);
        this.tv_date_app_selected = (TextView) screen.findViewById(C1020R.id.tv_date_app_selected);
        this.tv_time_selected = (TextView) screen.findViewById(C1020R.id.tv_time_selected);
        this.tv_duration_selected = (TextView) screen.findViewById(C1020R.id.tv_duration_selected);
        this.rel_doc = (RelativeLayout) screen.findViewById(C1020R.id.rel_doc);
        this.rel_year = (RelativeLayout) screen.findViewById(C1020R.id.rel_year);
        this.rel_month = (RelativeLayout) screen.findViewById(C1020R.id.rel_month);
        this.rel_date = (RelativeLayout) screen.findViewById(C1020R.id.rel_date);
        this.rel_duration = (RelativeLayout) screen.findViewById(C1020R.id.rel_duration);
        this.rel_time = (RelativeLayout) screen.findViewById(C1020R.id.rel_time);
        this.linear_save = (LinearLayout) screen.findViewById(C1020R.id.linear_save);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        clickListners();
    }

    private void clickListners() {
        this.rel_doc.setOnClickListener(this);
        this.tv_doctor.setOnClickListener(this);
        this.rel_year.setOnClickListener(this);
        this.tv_year.setOnClickListener(this);
        this.rel_month.setOnClickListener(this);
        this.tv_month.setOnClickListener(this);
        this.rel_date.setOnClickListener(this);
        this.tv_date_1.setOnClickListener(this);
        this.rel_duration.setOnClickListener(this);
        this.tv_duration.setOnClickListener(this);
        this.rel_time.setOnClickListener(this);
        this.tv_time.setOnClickListener(this);
        this.linear_save.setOnClickListener(this);
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.linear_save:
                this.selfSchedulePresenter.saveButtonAction();
                return;
            case C1020R.id.img_slider:
                PatientMainActivity.openDrawer();
                return;
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            case C1020R.id.rel_date:
            case C1020R.id.tv_date_1:
                this.selfSchedulePresenter.openDatePicker();
                return;
            case C1020R.id.rel_year:
            case C1020R.id.tv_year:
                this.selfSchedulePresenter.openYearPicker();
                return;
            case C1020R.id.rel_month:
            case C1020R.id.tv_month:
                this.selfSchedulePresenter.openMonthPicker();
                return;
            case C1020R.id.tv_time:
            case C1020R.id.rel_time:
                this.selfSchedulePresenter.openTimePicker();
                return;
            case C1020R.id.rel_doc:
            case C1020R.id.tv_doctor:
                this.selfSchedulePresenter.openDoctorPicker();
                return;
            case C1020R.id.rel_duration:
            case C1020R.id.tv_duration:
                this.selfSchedulePresenter.openDurationPicker();
                return;
            default:
                return;
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.tv_year.setText(CommonUtils.capWordCase(this.activity.getResources().getString(C1020R.string.year)));
        this.tv_month.setText(CommonUtils.capWordCase(this.activity.getResources().getString(C1020R.string.month)));
        this.tv_date_1.setText(CommonUtils.capWordCase(this.activity.getResources().getString(C1020R.string.date)));
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.self_schedule_appointment));
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.selfSchedulePresenter = new SelfSchedulePresenterImp(this, this.netWorkingService);
    }

    public void settingDoctor(String doc) {
        this.tv_doctor.setText(doc);
        this.tv_doc_selected.setText(doc);
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

    public void setYear(String value) {
        this.tv_year.setText(value);
    }

    public void setDate(String value) {
        this.tv_date_1.setText(value);
    }

    public void setCompleteDate(String value) {
        this.tv_date_app_selected.setText(value);
    }

    public void setMonth(String value) {
        this.tv_month.setText(value);
    }

    public void setDuaration(String value) {
        this.tv_duration.setText(value);
        this.tv_duration_selected.setText(value);
    }

    public void setTime(String value) {
        this.tv_time.setText(value);
        this.tv_time_selected.setText(value);
    }

    public void onDestroy() {
        super.onDestroy();
        this.selfSchedulePresenter.unSubscribeCallbacks();
    }
}
