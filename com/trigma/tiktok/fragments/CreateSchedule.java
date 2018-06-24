package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.presenter.CreateSchedulePresenter;
import com.trigma.tiktok.presenter.CreateSchedulePresenterImp;
import com.trigma.tiktok.presenter.MainActivityView;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import java.util.Calendar;

public class CreateSchedule extends BaseFragment implements OnClickListener {
    private Activity activity;
    private LinearLayout bottom_tabs;
    private CreateSchedulePresenter createSchedulePresenter;
    private int current_date;
    private int current_month;
    private int current_year;
    private CheckBox fifteen_min;
    private ImageView img_header_back;
    private ImageView img_slider;
    private LinearLayout linear_save;
    private RelativeLayout loading_layout;
    private ScrollView main_layout;
    public NetWorkingService netWorkingService;
    private CheckBox ninty_min;
    private RelativeLayout rel_date;
    private RelativeLayout rel_from;
    private RelativeLayout rel_to;
    private RelativeLayout rv_current_schedule;
    private CheckBox sixteen_min;
    private CheckBox thirteen_min;
    private TextView title_name;
    private TextView tv_date_1;
    private TextView tv_date_text;
    private TextView tv_from_1;
    private TextView tv_minute;
    private TextView tv_to_1;
    private TextView tv_to_2;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.create_schedule, container, false);
        initViews(screen);
        Calendar c = Calendar.getInstance();
        this.current_year = c.get(1);
        this.current_month = c.get(2);
        this.current_date = c.get(5);
        return screen;
    }

    private void initViews(View screen) {
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_date_1 = (TextView) screen.findViewById(C1020R.id.tv_date_1);
        this.tv_from_1 = (TextView) screen.findViewById(C1020R.id.tv_from_1);
        this.tv_to_1 = (TextView) screen.findViewById(C1020R.id.tv_to_1);
        this.tv_date_text = (TextView) screen.findViewById(C1020R.id.tv_date_text);
        this.tv_minute = (TextView) screen.findViewById(C1020R.id.tv_minute);
        this.tv_to_2 = (TextView) screen.findViewById(C1020R.id.tv_to_2);
        this.fifteen_min = (CheckBox) screen.findViewById(C1020R.id.fifteen_min);
        this.thirteen_min = (CheckBox) screen.findViewById(C1020R.id.thirteen_min);
        this.sixteen_min = (CheckBox) screen.findViewById(C1020R.id.sixteen_min);
        this.ninty_min = (CheckBox) screen.findViewById(C1020R.id.ninty_min);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.main_layout = (ScrollView) screen.findViewById(C1020R.id.main_layout);
        this.loading_layout = (RelativeLayout) screen.findViewById(C1020R.id.loading_layout);
        this.rv_current_schedule = (RelativeLayout) screen.findViewById(C1020R.id.rv_current_schedule);
        this.rel_date = (RelativeLayout) screen.findViewById(C1020R.id.rel_date);
        this.rel_from = (RelativeLayout) screen.findViewById(C1020R.id.rel_from);
        this.rel_to = (RelativeLayout) screen.findViewById(C1020R.id.rel_to);
        this.linear_save = (LinearLayout) screen.findViewById(C1020R.id.linear_save);
        this.bottom_tabs = (LinearLayout) screen.findViewById(C1020R.id.bottom_tabs);
        clickListners();
    }

    private void clickListners() {
        this.rv_current_schedule.setOnClickListener(this);
        this.linear_save.setOnClickListener(this);
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
        this.rel_date.setOnClickListener(this);
        this.rel_to.setOnClickListener(this);
        this.rel_from.setOnClickListener(this);
        this.main_layout.setOnClickListener(this);
        this.fifteen_min.setOnClickListener(this);
        this.thirteen_min.setOnClickListener(this);
        this.sixteen_min.setOnClickListener(this);
        this.ninty_min.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.linear_save:
                this.createSchedulePresenter.checkValidate();
                return;
            case C1020R.id.img_slider:
                MainActivity.openDrawer();
                return;
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            case C1020R.id.rel_date:
                this.createSchedulePresenter.openDatePicker();
                return;
            case C1020R.id.rel_from:
                this.createSchedulePresenter.openFromTimePicker();
                return;
            case C1020R.id.rel_to:
                this.createSchedulePresenter.openToTimePicker();
                return;
            case C1020R.id.fifteen_min:
                seetingCheckBoxValue(1);
                return;
            case C1020R.id.thirteen_min:
                seetingCheckBoxValue(2);
                return;
            case C1020R.id.sixteen_min:
                seetingCheckBoxValue(3);
                return;
            case C1020R.id.ninty_min:
                seetingCheckBoxValue(4);
                return;
            case C1020R.id.rv_current_schedule:
                ((MainActivityView) getActivity()).changeFragment(new CurrentSchedule());
                return;
            default:
                return;
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.create_schedule));
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.createSchedulePresenter = new CreateSchedulePresenterImp(this, this.netWorkingService);
        this.createSchedulePresenter.settingCurrentDate(this.current_year, this.current_month, this.current_date);
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

    public void setDateSelected(int year, String monthOfYear, int dayOfMonth) {
        this.tv_date_1.setText(monthOfYear + " " + dayOfMonth + "," + year);
        this.tv_date_text.setText(monthOfYear + " " + dayOfMonth + "," + year);
    }

    public void setDateSelected(String monthOfYear) {
        this.tv_date_1.setText(monthOfYear);
        if (monthOfYear.equalsIgnoreCase(this.activity.getResources().getString(C1020R.string.select_date))) {
            this.tv_date_text.setText(null);
        } else {
            this.tv_date_text.setText(monthOfYear);
        }
    }

    public void clearCheckBoxes() {
        this.fifteen_min.setChecked(false);
        this.thirteen_min.setChecked(false);
        this.sixteen_min.setChecked(false);
        this.ninty_min.setChecked(false);
    }

    public void setFromTime(String time) {
        this.tv_from_1.setText(time);
        if (time.equalsIgnoreCase(this.activity.getResources().getString(C1020R.string.from))) {
            this.tv_minute.setText(null);
        } else {
            this.tv_minute.setText(time);
        }
    }

    public void setToTime(String time) {
        this.tv_to_1.setText(time);
        if (time.equalsIgnoreCase(this.activity.getResources().getString(C1020R.string.to))) {
            this.tv_to_2.setText(null);
        } else {
            this.tv_to_2.setText(time);
        }
    }

    public void seetingCheckBoxValue(int value) {
        switch (value) {
            case 1:
                if (this.fifteen_min.isChecked()) {
                    this.createSchedulePresenter.settingMaximumSelectdSlot(15);
                    return;
                } else {
                    this.createSchedulePresenter.settingMinimumSelectdSlot(15);
                    return;
                }
            case 2:
                if (this.thirteen_min.isChecked()) {
                    this.createSchedulePresenter.settingMaximumSelectdSlot(30);
                    return;
                } else {
                    this.createSchedulePresenter.settingMinimumSelectdSlot(30);
                    return;
                }
            case 3:
                if (this.sixteen_min.isChecked()) {
                    this.createSchedulePresenter.settingMaximumSelectdSlot(60);
                    return;
                } else {
                    this.createSchedulePresenter.settingMinimumSelectdSlot(60);
                    return;
                }
            case 4:
                if (this.ninty_min.isChecked()) {
                    this.createSchedulePresenter.settingMaximumSelectdSlot(90);
                    return;
                } else {
                    this.createSchedulePresenter.settingMinimumSelectdSlot(90);
                    return;
                }
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.createSchedulePresenter.unSubscribeCallbacks();
    }
}
