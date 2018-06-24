package com.trigma.tiktok.activity;

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
import com.trigma.tiktok.adapter.ScheduleDetailAdapter;
import com.trigma.tiktok.model.PateintDataPojoMain;
import com.trigma.tiktok.presenter.ScheduleDetailPresntor;
import com.trigma.tiktok.presenter.ScheduleDetailPresntorImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import java.util.ArrayList;

public class ScheduleDetail extends BaseActivity implements OnClickListener {
    private String DrScheduleId;
    private String Dr_id;
    private String From;
    private String ScheduleId;
    private String To;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    private View loading_layout;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    private ScheduleDetailAdapter scheduleDetailAdapter;
    private ScheduleDetailPresntor scheduleDetailPresntor;
    private TextView title_name;
    private TextView tv_error;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.schedule_date);
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        initViews();
        this.scheduleDetailPresntor = new ScheduleDetailPresntorImp(this, this.netWorkingService);
        settingIntentData();
    }

    private void settingIntentData() {
        if (getIntent().hasExtra("DrScheduleId")) {
            this.Dr_id = getIntent().getStringExtra("Dr_id");
            this.DrScheduleId = getIntent().getStringExtra("DrScheduleId");
            this.ScheduleId = getIntent().getStringExtra("ScheduleId");
            this.From = getIntent().getStringExtra("From");
            this.To = getIntent().getStringExtra("To");
            this.scheduleDetailPresntor.callApi(this.Dr_id, this.DrScheduleId, this.ScheduleId, this.From, this.To);
        }
    }

    private void initViews() {
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_error = (ImageView) findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.no_scheduled_patients);
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.tv_error = (TextView) findViewById(C1020R.id.tv_error_msg);
        this.loading_layout = findViewById(C1020R.id.loading_layout);
        this.img_slider.setVisibility(4);
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.title_name.setText(getResources().getString(C1020R.string.schedule_detail));
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        clickListner();
        settingRecyclerView();
    }

    private void clickListner() {
        this.img_header_back.setOnClickListener(this);
    }

    private void settingRecyclerView() {
        this.scheduleDetailAdapter = new ScheduleDetailAdapter(this);
        this.recyclerView.setAdapter(this.scheduleDetailAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void addingDataToAdapter(ArrayList<PateintDataPojoMain> patient) {
        if (patient.size() > 0) {
            this.loading_layout.setVisibility(8);
            this.recyclerView.setVisibility(0);
            this.scheduleDetailAdapter.addingList(patient);
            return;
        }
        emptyData();
    }

    public void emptyData() {
        this.loading_layout.setVisibility(0);
        this.recyclerView.setVisibility(8);
        this.img_error.setImageResource(C1020R.drawable.no_scheduled_patients);
        this.tv_error.setVisibility(8);
        this.tv_error.setText(getResources().getString(C1020R.string.currently_you_have_no_scheduled_patients_for_this_day));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_header_back:
                finish();
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
}
