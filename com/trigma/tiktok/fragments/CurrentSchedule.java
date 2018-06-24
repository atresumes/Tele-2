package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.activity.ScheduleDetail;
import com.trigma.tiktok.adapter.CurrentScheduleAdapter;
import com.trigma.tiktok.model.BookingPojo;
import com.trigma.tiktok.model.SchedulePojo;
import com.trigma.tiktok.presenter.CurrentSchedulePresenter;
import com.trigma.tiktok.presenter.CurrentSchedulePresenterImp;
import com.trigma.tiktok.presenter.MainActivityView;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.DialogPopUps;
import java.util.ArrayList;

public class CurrentSchedule extends BaseFragment implements OnClickListener {
    private String DrScheduleId;
    private String TimeSchedule;
    private Activity activity;
    private LinearLayout bottom_tabs;
    private CurrentScheduleAdapter currentScheduleAdapter;
    private CurrentSchedulePresenter currentSchedulePresenter;
    private ImageView img_header_back;
    private ImageView img_slider;
    private RelativeLayout loading_layout;
    private LinearLayout main_layout;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    private RelativeLayout rel_date;
    private RelativeLayout rel_month;
    private RelativeLayout rel_year;
    private RelativeLayout rv_create_schedule;
    private RelativeLayout rv_current_schedule;
    private TextView title_name;
    private TextView tv_day;
    private TextView tv_error;
    private TextView tv_month;
    private TextView tv_year;

    class C11521 implements AlertCallBackInterface {
        C11521() {
        }

        public void neutralClick() {
            if (CurrentSchedule.this.currentScheduleAdapter != null && CurrentSchedule.this.currentScheduleAdapter.getItemCount() <= 0) {
                CurrentSchedule.this.setDate(CurrentSchedule.this.activity.getResources().getString(C1020R.string.select_date));
                CurrentSchedule.this.setMonth(CurrentSchedule.this.activity.getResources().getString(C1020R.string.month_c));
                CurrentSchedule.this.setYear(CurrentSchedule.this.activity.getResources().getString(C1020R.string.year_c));
                CurrentSchedule.this.currentSchedulePresenter.makeApiCall();
            }
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.current_schedule, container, false);
        initViews(screen);
        return screen;
    }

    private void settingRecyclerView() {
        this.currentScheduleAdapter = new CurrentScheduleAdapter(this.currentSchedulePresenter, this);
        this.recyclerView.setAdapter(this.currentScheduleAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initViews(View screen) {
        this.recyclerView = (RecyclerView) screen.findViewById(C1020R.id.recyclerView);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_year = (TextView) screen.findViewById(C1020R.id.tv_year);
        this.tv_month = (TextView) screen.findViewById(C1020R.id.tv_month);
        this.tv_day = (TextView) screen.findViewById(C1020R.id.tv_day);
        this.tv_error = (TextView) screen.findViewById(C1020R.id.tv_error);
        this.main_layout = (LinearLayout) screen.findViewById(C1020R.id.main_layout);
        this.bottom_tabs = (LinearLayout) screen.findViewById(C1020R.id.bottom_tabs);
        this.loading_layout = (RelativeLayout) screen.findViewById(C1020R.id.loading_layout);
        this.rel_year = (RelativeLayout) screen.findViewById(C1020R.id.rel_year);
        this.rel_month = (RelativeLayout) screen.findViewById(C1020R.id.rel_month);
        this.rel_date = (RelativeLayout) screen.findViewById(C1020R.id.rel_date);
        this.rv_create_schedule = (RelativeLayout) screen.findViewById(C1020R.id.rv_create_schedule);
        this.rv_current_schedule = (RelativeLayout) screen.findViewById(C1020R.id.rv_current_schedule);
        clickListners();
    }

    private void clickListners() {
        this.rel_year.setOnClickListener(this);
        this.tv_year.setOnClickListener(this);
        this.rel_month.setOnClickListener(this);
        this.rel_date.setOnClickListener(this);
        this.rv_create_schedule.setOnClickListener(this);
        this.rv_current_schedule.setOnClickListener(this);
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.current_schedule));
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.currentSchedulePresenter = new CurrentSchedulePresenterImp(this, this.netWorkingService);
        settingRecyclerView();
        this.currentSchedulePresenter.makeApiCall();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                MainActivity.openDrawer();
                return;
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            case C1020R.id.rel_date:
            case C1020R.id.tv_day:
                this.currentSchedulePresenter.openDatePicker();
                return;
            case C1020R.id.rv_create_schedule:
                ((MainActivityView) getActivity()).changeFragment(new CreateSchedule());
                return;
            case C1020R.id.rel_year:
            case C1020R.id.tv_year:
                this.currentSchedulePresenter.openYearPicker();
                return;
            case C1020R.id.rel_month:
            case C1020R.id.tv_month:
                this.currentSchedulePresenter.openMonthPicker();
                return;
            default:
                return;
        }
    }

    public void settingYear(String value) {
        this.tv_year.setText(value);
    }

    public void settingDate(String value) {
        this.tv_day.setText(value);
    }

    public void settingMonth(String value) {
        this.tv_month.setText(value);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.currentSchedulePresenter.unSubscribeCallbacks();
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
        this.tv_day.setText(value);
    }

    public void setMonth(String value) {
        this.tv_month.setText(value);
    }

    public void settingAdapter(ArrayList<SchedulePojo> scheduleList, ArrayList<BookingPojo> bookingList, String Dr_id, String DrScheduleId, String schedule) {
        this.currentScheduleAdapter.addingList(scheduleList, bookingList, Dr_id, DrScheduleId, schedule);
    }

    public void showRecyclerView(boolean val) {
        if (val) {
            this.recyclerView.setVisibility(0);
        } else {
            this.recyclerView.setVisibility(8);
        }
    }

    public void gotoDetailScreen(String Dr_id, String DrScheduleId, String ScheduleId, String From, String To) {
        Intent scDeatil = new Intent(this.activity, ScheduleDetail.class);
        scDeatil.putExtra("Dr_id", Dr_id);
        scDeatil.putExtra("DrScheduleId", DrScheduleId);
        scDeatil.putExtra("ScheduleId", ScheduleId);
        scDeatil.putExtra("From", From);
        scDeatil.putExtra("To", To);
        startActivity(scDeatil);
    }

    public void deletedSuccessFully(int pos) {
        this.currentScheduleAdapter.deleteItem(pos);
        DialogPopUps.confirmationPopUp(this.activity, this.activity.getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.schedule_deleted_successfully), new C11521());
    }
}
