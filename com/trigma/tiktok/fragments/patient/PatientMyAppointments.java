package com.trigma.tiktok.fragments.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.activity.patient.PatientWaitingRoom;
import com.trigma.tiktok.adapter.patient.PatientAppointmentPast;
import com.trigma.tiktok.adapter.patient.PatientApppointmentUpcoming;
import com.trigma.tiktok.fragments.BaseFragment;
import com.trigma.tiktok.model.PatientModelUpcomingOuter;
import com.trigma.tiktok.presenter.patient.PatientAppointmentsPresenter;
import com.trigma.tiktok.presenter.patient.PatientAppointmentsPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.HashMap;

public class PatientMyAppointments extends BaseFragment implements OnClickListener {
    private Activity activity;
    private View appointment_error_msg;
    private PatientAppointmentPast apppointmentPast;
    private PatientApppointmentUpcoming apppointmentUpcoming;
    private int callApi = 4;
    private ExpandableListView expandableListView;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    private int lastExpandedPosition = -1;
    private LinearLayout lin_bottom_note;
    private LinearLayout lin_past;
    private LinearLayout lin_upcomming;
    private LinearLayout linear_done;
    private PatientAppointmentsPresenter myAppointmentsPresenter;
    public NetWorkingService netWorkingService;
    private TextView title_name;
    private TextView tv_active;
    private TextView tv_confirm;
    private TextView tv_error_msg;
    private TextView tv_note;
    private TextView tv_pending;

    class C11971 implements OnGroupExpandListener {
        C11971() {
        }

        public void onGroupExpand(int groupPosition) {
            if (!(PatientMyAppointments.this.lastExpandedPosition == -1 || groupPosition == PatientMyAppointments.this.lastExpandedPosition)) {
                PatientMyAppointments.this.expandableListView.collapseGroup(PatientMyAppointments.this.lastExpandedPosition);
            }
            PatientMyAppointments.this.lastExpandedPosition = groupPosition;
        }
    }

    class C11982 implements AlertCallBackInterface {
        C11982() {
        }

        public void neutralClick() {
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.patient_my_appointments, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_active = (TextView) screen.findViewById(C1020R.id.tv_active);
        this.tv_pending = (TextView) screen.findViewById(C1020R.id.tv_pending);
        this.tv_note = (TextView) screen.findViewById(C1020R.id.tv_note);
        this.tv_confirm = (TextView) screen.findViewById(C1020R.id.tv_confirm);
        this.tv_error_msg = (TextView) screen.findViewById(C1020R.id.tv_error_msg);
        this.appointment_error_msg = screen.findViewById(C1020R.id.appointment_error_msg);
        this.tv_error_msg.setVisibility(8);
        this.lin_past = (LinearLayout) screen.findViewById(C1020R.id.lin_past);
        this.lin_upcomming = (LinearLayout) screen.findViewById(C1020R.id.lin_upcomming);
        this.linear_done = (LinearLayout) screen.findViewById(C1020R.id.linear_done);
        this.lin_bottom_note = (LinearLayout) screen.findViewById(C1020R.id.lin_bottom_note);
        this.expandableListView = (ExpandableListView) screen.findViewById(C1020R.id.expandableListView);
        this.expandableListView.setOnGroupExpandListener(new C11971());
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.img_error = (ImageView) screen.findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.currently_no_upcoming);
        clickListners();
    }

    private void clickListners() {
        this.tv_active.setOnClickListener(this);
        this.tv_pending.setOnClickListener(this);
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
        this.tv_confirm.setOnClickListener(this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.my_appointments));
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.myAppointmentsPresenter = new PatientAppointmentsPresenterImp(this, this.netWorkingService);
        this.myAppointmentsPresenter.fetchUpcoming();
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
                PatientMainActivity.openDrawer();
                return;
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            case C1020R.id.tv_confirm:
                this.myAppointmentsPresenter.checkingForDoctors();
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
                this.tv_note.setVisibility(0);
                this.lin_bottom_note.setVisibility(0);
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
                this.tv_note.setVisibility(8);
                this.lin_bottom_note.setVisibility(8);
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.myAppointmentsPresenter.unSubscribeCallbacks();
    }

    public void settingUpCommingAdapter(ArrayList<String> _listDataHeader, HashMap<String, ArrayList<PatientModelUpcomingOuter>> _listDataChild) {
        if (_listDataHeader.size() == 0) {
            this.appointment_error_msg.setVisibility(0);
            this.expandableListView.setVisibility(8);
            this.tv_note.setVisibility(8);
            this.lin_bottom_note.setVisibility(8);
        } else {
            this.appointment_error_msg.setVisibility(8);
            this.expandableListView.setVisibility(0);
            this.tv_note.setVisibility(0);
            this.lin_bottom_note.setVisibility(0);
        }
        this.apppointmentUpcoming = new PatientApppointmentUpcoming(this.activity, _listDataHeader, _listDataChild, this.myAppointmentsPresenter);
        this.expandableListView.setAdapter(this.apppointmentUpcoming);
        this.apppointmentUpcoming.notifyDataSetChanged();
    }

    public void settingPastAdapter(ArrayList<String> _listDataHeader, HashMap<String, ArrayList<PatientModelUpcomingOuter>> _listDataChild) {
        if (_listDataHeader.size() == 0) {
            this.appointment_error_msg.setVisibility(0);
            this.expandableListView.setVisibility(8);
            this.tv_note.setVisibility(8);
            this.lin_bottom_note.setVisibility(8);
        } else {
            this.appointment_error_msg.setVisibility(8);
            this.expandableListView.setVisibility(0);
        }
        this.apppointmentPast = new PatientAppointmentPast(this.activity, _listDataHeader, _listDataChild, this.myAppointmentsPresenter);
        this.expandableListView.setAdapter(this.apppointmentPast);
        this.apppointmentPast.notifyDataSetChanged();
    }

    public void upCommingEmpty() {
        this.appointment_error_msg.setVisibility(0);
        this.expandableListView.setVisibility(8);
        this.tv_note.setVisibility(8);
        this.lin_bottom_note.setVisibility(8);
    }

    public void pastEmpty() {
        this.appointment_error_msg.setVisibility(0);
        this.expandableListView.setVisibility(8);
        this.tv_note.setVisibility(8);
        this.lin_bottom_note.setVisibility(8);
    }

    public static String getStatusResponse(String status) {
        if (status.equalsIgnoreCase("2")) {
            return "Status:Completed";
        }
        if (status.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
            return "Status:Not Completed";
        }
        return "Status:Canceled by Doctor";
    }

    public void goToWaitingRoom(PatientModelUpcomingOuter upcoming) {
        SharedPreff.savePatientUpcomingObject(upcoming);
        Intent scDeatil = new Intent(this.activity, PatientWaitingRoom.class);
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
            this.tv_note.setVisibility(0);
            this.lin_bottom_note.setVisibility(0);
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
            this.tv_note.setVisibility(8);
            this.lin_bottom_note.setVisibility(8);
            this.appointment_error_msg.setVisibility(0);
        }
    }

    public void isDoctorActive(boolean status) {
        if (status) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(C1020R.id.containerView, new SelfSchedule());
            fragmentTransaction.commit();
            return;
        }
        DialogPopUps.confirmationPopUp(this.activity, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.your_doctor_has_not_verified_your_account_yet), new C11982());
    }
}
