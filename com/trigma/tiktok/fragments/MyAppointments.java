package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.activity.MakeVideoCall;
import com.trigma.tiktok.activity.PatientDetail;
import com.trigma.tiktok.adapter.ApppointmentPast;
import com.trigma.tiktok.adapter.ApppointmentUpcoming;
import com.trigma.tiktok.model.Past;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.Upcoming;
import com.trigma.tiktok.presenter.MyAppointmentsPresenter;
import com.trigma.tiktok.presenter.MyAppointmentsPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.HashMap;

public class MyAppointments extends BaseFragment implements OnClickListener {
    private Activity activity;
    private View appointment_error_msg;
    private ApppointmentPast apppointmentPast;
    private ApppointmentUpcoming apppointmentUpcoming;
    private int callApi = 4;
    private ExpandableListView expandableListView;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    private int lastExpandedPosition = -1;
    private LinearLayout lin_past;
    private LinearLayout lin_upcomming;
    private MyAppointmentsPresenter myAppointmentsPresenter;
    public NetWorkingService netWorkingService;
    private TextView title_name;
    private TextView tv_active;
    private TextView tv_error_msg;
    private TextView tv_pending;

    class C11531 implements OnGroupExpandListener {
        C11531() {
        }

        public void onGroupExpand(int groupPosition) {
            if (!(MyAppointments.this.lastExpandedPosition == -1 || groupPosition == MyAppointments.this.lastExpandedPosition)) {
                MyAppointments.this.expandableListView.collapseGroup(MyAppointments.this.lastExpandedPosition);
            }
            MyAppointments.this.lastExpandedPosition = groupPosition;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.my_appointments, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_active = (TextView) screen.findViewById(C1020R.id.tv_active);
        this.tv_pending = (TextView) screen.findViewById(C1020R.id.tv_pending);
        this.tv_error_msg = (TextView) screen.findViewById(C1020R.id.tv_error_msg);
        this.appointment_error_msg = screen.findViewById(C1020R.id.appointment_error_msg);
        this.tv_error_msg.setVisibility(8);
        this.lin_past = (LinearLayout) screen.findViewById(C1020R.id.lin_past);
        this.lin_upcomming = (LinearLayout) screen.findViewById(C1020R.id.lin_upcomming);
        this.expandableListView = (ExpandableListView) screen.findViewById(C1020R.id.expandableListView);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.img_error = (ImageView) screen.findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.currently_no_upcoming);
        this.expandableListView.setOnGroupExpandListener(new C11531());
        clickListners();
    }

    private void clickListners() {
        this.tv_active.setOnClickListener(this);
        this.tv_pending.setOnClickListener(this);
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.my_appointments));
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.myAppointmentsPresenter = new MyAppointmentsPresenterImp(this, this.netWorkingService);
        this.myAppointmentsPresenter.fetchUpcoming();
        this.expandableListView.setVisibility(8);
        this.appointment_error_msg.setVisibility(0);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                MainActivity.openDrawer();
                return;
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            case C1020R.id.tv_active:
                this.img_error.setImageResource(C1020R.drawable.currently_no_upcoming);
                this.lin_upcomming.setBackgroundResource(C1020R.drawable.while_left_selecetd);
                this.lin_past.setBackground(null);
                this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
                this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
                this.myAppointmentsPresenter.fetchUpcoming();
                this.expandableListView.setVisibility(8);
                this.appointment_error_msg.setVisibility(0);
                return;
            case C1020R.id.tv_pending:
                this.img_error.setImageResource(C1020R.drawable.currently_no_past);
                this.lin_past.setBackgroundResource(C1020R.drawable.white_right_selected);
                this.lin_upcomming.setBackground(null);
                this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
                this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
                this.myAppointmentsPresenter.fetchPast();
                this.expandableListView.setVisibility(8);
                this.appointment_error_msg.setVisibility(0);
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void settingUpCommingAdapter(ArrayList<String> _listDataHeader, HashMap<String, ArrayList<Upcoming>> _listDataChild) {
        if (_listDataHeader.size() == 0) {
            this.appointment_error_msg.setVisibility(0);
            this.expandableListView.setVisibility(8);
        } else {
            this.appointment_error_msg.setVisibility(8);
            this.expandableListView.setVisibility(0);
        }
        this.apppointmentUpcoming = new ApppointmentUpcoming(this.activity, _listDataHeader, _listDataChild, this.myAppointmentsPresenter);
        this.expandableListView.setAdapter(this.apppointmentUpcoming);
        this.apppointmentUpcoming.notifyDataSetChanged();
    }

    public void settingPastAdapter(ArrayList<String> _listDataHeader, HashMap<String, ArrayList<Past>> _listDataChild) {
        if (_listDataHeader.size() == 0) {
            this.appointment_error_msg.setVisibility(0);
            this.expandableListView.setVisibility(8);
        } else {
            this.appointment_error_msg.setVisibility(8);
            this.expandableListView.setVisibility(0);
        }
        this.apppointmentPast = new ApppointmentPast(this.activity, _listDataHeader, _listDataChild, this.myAppointmentsPresenter);
        this.expandableListView.setAdapter(this.apppointmentPast);
        this.apppointmentPast.notifyDataSetChanged();
    }

    public void upCommingEmpty() {
        this.appointment_error_msg.setVisibility(0);
        this.expandableListView.setVisibility(8);
    }

    public void pastEmpty() {
        this.appointment_error_msg.setVisibility(0);
        this.expandableListView.setVisibility(8);
    }

    public static String getStatusResponse(String status, int userType) {
        if (status.equalsIgnoreCase("2")) {
            return "Status:Completed";
        }
        if (status.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
            return "Status:Not Completed";
        }
        if (userType == 3) {
            return "Status:Canceled by Staff";
        }
        return "Status:Canceled by Doctor";
    }

    public void goToPatientDetail(PatientPendingData patientPendingData) {
        SharedPreff.addingDocPatientDetail(patientPendingData);
        Intent scDeatil = new Intent(this.activity, PatientDetail.class);
        scDeatil.putExtra("isFromActive", 2);
        startActivity(scDeatil);
        this.callApi = 1;
    }

    public void goToMakeVideoCall(Upcoming data) {
        SharedPreff.saveUpcomingObject(data);
        Intent scDeatil = new Intent(this.activity, MakeVideoCall.class);
        scDeatil.putExtra("isFromActive", 2);
        startActivity(scDeatil);
        this.callApi = 2;
    }

    public void onResume() {
        super.onResume();
        if (this.callApi == 1) {
            this.callApi = 4;
            this.img_error.setImageResource(C1020R.drawable.currently_no_past);
            this.lin_past.setBackgroundResource(C1020R.drawable.white_right_selected);
            this.lin_upcomming.setBackground(null);
            this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
            this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.myAppointmentsPresenter.fetchPast();
            this.expandableListView.setVisibility(8);
            this.appointment_error_msg.setVisibility(0);
        } else if (this.callApi == 2) {
            this.callApi = 4;
            this.img_error.setImageResource(C1020R.drawable.currently_no_upcoming);
            this.lin_upcomming.setBackgroundResource(C1020R.drawable.while_left_selecetd);
            this.lin_past.setBackground(null);
            this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
            this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.myAppointmentsPresenter.fetchUpcoming();
            this.expandableListView.setVisibility(8);
            this.appointment_error_msg.setVisibility(0);
        }
    }
}
