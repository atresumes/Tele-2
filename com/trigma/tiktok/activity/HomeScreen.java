package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorSearch;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.NotificationCountPush;
import com.trigma.tiktok.presenter.HomeScreenPresenter;
import com.trigma.tiktok.presenter.HomeScreenPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeScreen extends BaseActivity implements OnClickListener {
    private TextView doc_field;
    private TextView doc_name;
    private HomeScreenPresenter homeScreenPresenter;
    private ImageView img_doc_search;
    private ImageView img_doc_video;
    private ImageView img_header;
    private TextView img_my_app_count;
    private View line_left_bottom;
    private View line_right_bottom;
    private LinearLayout linear_chat;
    private LinearLayout linear_contact_us;
    private LinearLayout linear_logut;
    private LinearLayout linear_my_profile;
    private LinearLayout linear_noti;
    private LinearLayout linear_staff;
    public NetWorkingService netWorkingService;
    private RelativeLayout rel_my_appointment;
    private RelativeLayout rel_my_patients;
    private RelativeLayout rel_prescription;
    private RelativeLayout rel_self_schedule;
    private TextView tv_msg_cout;
    private TextView tv_not_cout;
    private TextView tv_patient_count;
    private TextView tv_prescrip_count;
    private TextView tv_self_count;

    class C10501 implements AlertCallBackInterface {
        C10501() {
        }

        public void neutralClick() {
            SharedPreff.clearDocPreff();
            Intent intent = new Intent(HomeScreen.this, DashBoard.class);
            intent.setFlags(268468224);
            HomeScreen.this.startActivity(intent);
        }
    }

    class C10512 implements AlertCallBackWithButtonsInterface {
        C10512() {
        }

        public void positiveClick() {
            HomeScreen.this.homeScreenPresenter.logout();
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.home_screen);
        EventBus.getDefault().register(this);
        iniit();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.homeScreenPresenter = new HomeScreenPresenterImp(this, this.netWorkingService);
    }

    protected void onResume() {
        super.onResume();
        this.homeScreenPresenter.startNotificationCountApi();
        this.homeScreenPresenter.checkingAppointmentDates();
        this.homeScreenPresenter.checkDeactivate();
    }

    public void checkPrescriptionVisibility(String val) {
        LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
        if (loginDocResponce != null) {
            if (loginDocResponce.getData().getDrType() != null) {
                if (!loginDocResponce.getData().getDrType().equalsIgnoreCase(val)) {
                    DialogPopUps.alertPopUp(this, getResources().getString(C1020R.string.your_prescription_has_been_changed_by_admin_please_log_in_into_the_app_again), getResources().getString(C1020R.string.ok), new C10501());
                } else if (val.equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
                    this.line_left_bottom.setVisibility(8);
                    this.line_right_bottom.setVisibility(8);
                    this.rel_prescription.setVisibility(8);
                    Constants.PRESCRIPTION_HIDE = 0;
                } else {
                    this.line_left_bottom.setVisibility(0);
                    this.line_right_bottom.setVisibility(0);
                    this.rel_prescription.setVisibility(0);
                    Constants.PRESCRIPTION_HIDE = 1;
                }
            } else if (val.equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
                this.line_left_bottom.setVisibility(8);
                this.line_right_bottom.setVisibility(8);
                this.rel_prescription.setVisibility(8);
                Constants.PRESCRIPTION_HIDE = 0;
            } else {
                this.line_left_bottom.setVisibility(0);
                this.line_right_bottom.setVisibility(0);
                this.rel_prescription.setVisibility(0);
                Constants.PRESCRIPTION_HIDE = 1;
            }
        } else if (val.equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
            this.line_left_bottom.setVisibility(8);
            this.line_right_bottom.setVisibility(8);
            this.rel_prescription.setVisibility(8);
            Constants.PRESCRIPTION_HIDE = 0;
        } else {
            this.line_left_bottom.setVisibility(0);
            this.line_right_bottom.setVisibility(0);
            this.rel_prescription.setVisibility(0);
            Constants.PRESCRIPTION_HIDE = 1;
        }
    }

    private void iniit() {
        this.doc_name = (TextView) findViewById(C1020R.id.doc_name);
        this.doc_field = (TextView) findViewById(C1020R.id.doc_field);
        this.img_my_app_count = (TextView) findViewById(C1020R.id.img_my_app_count);
        this.tv_self_count = (TextView) findViewById(C1020R.id.tv_self_count);
        this.tv_patient_count = (TextView) findViewById(C1020R.id.tv_patient_count);
        this.tv_prescrip_count = (TextView) findViewById(C1020R.id.tv_prescrip_count);
        this.tv_not_cout = (TextView) findViewById(C1020R.id.tv_not_cout);
        this.tv_msg_cout = (TextView) findViewById(C1020R.id.tv_msg_cout);
        this.line_left_bottom = findViewById(C1020R.id.line_left_bottom);
        this.line_right_bottom = findViewById(C1020R.id.line_right_bottom);
        this.rel_my_appointment = (RelativeLayout) findViewById(C1020R.id.rel_my_appointment);
        this.rel_self_schedule = (RelativeLayout) findViewById(C1020R.id.rel_self_schedule);
        this.rel_my_patients = (RelativeLayout) findViewById(C1020R.id.rel_my_patients);
        this.rel_prescription = (RelativeLayout) findViewById(C1020R.id.rel_prescription);
        this.img_header = (ImageView) findViewById(C1020R.id.img_header);
        this.img_doc_search = (ImageView) findViewById(C1020R.id.img_doc_search);
        this.img_doc_video = (ImageView) findViewById(C1020R.id.img_doc_video);
        this.linear_contact_us = (LinearLayout) findViewById(C1020R.id.linear_contact_us);
        this.linear_noti = (LinearLayout) findViewById(C1020R.id.linear_noti);
        this.linear_my_profile = (LinearLayout) findViewById(C1020R.id.linear_my_profile);
        this.linear_logut = (LinearLayout) findViewById(C1020R.id.linear_logut);
        this.linear_chat = (LinearLayout) findViewById(C1020R.id.linear_chat);
        this.linear_staff = (LinearLayout) findViewById(C1020R.id.linear_staff);
        clickListner();
        LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
        if (loginDocResponce != null && loginDocResponce.getData().getDrType() != null) {
            if (loginDocResponce.getData().getDrType().equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
                this.line_left_bottom.setVisibility(8);
                this.line_right_bottom.setVisibility(8);
                this.rel_prescription.setVisibility(8);
                Constants.PRESCRIPTION_HIDE = 0;
                return;
            }
            this.line_left_bottom.setVisibility(0);
            this.line_right_bottom.setVisibility(0);
            this.rel_prescription.setVisibility(0);
            Constants.PRESCRIPTION_HIDE = 1;
        }
    }

    private void clickListner() {
        this.linear_contact_us.setOnClickListener(this);
        this.linear_noti.setOnClickListener(this);
        this.linear_my_profile.setOnClickListener(this);
        this.linear_logut.setOnClickListener(this);
        this.linear_chat.setOnClickListener(this);
        this.linear_staff.setOnClickListener(this);
        this.rel_my_appointment.setOnClickListener(this);
        this.rel_self_schedule.setOnClickListener(this);
        this.rel_my_patients.setOnClickListener(this);
        this.rel_prescription.setOnClickListener(this);
        this.img_doc_search.setOnClickListener(this);
        this.img_doc_video.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case C1020R.id.linear_contact_us:
                Intent home = new Intent(this, MainActivity.class);
                home.putExtra(Constants.WHICH_FRAGMENT, 1);
                startActivity(home);
                return;
            case C1020R.id.img_doc_video:
                Intent guest_ = new Intent(this, GuestLinkScreen.class);
                guest_.putExtra("isDocCalling", true);
                startActivity(guest_);
                return;
            case C1020R.id.img_doc_search:
                goToDoctorSearch();
                return;
            case C1020R.id.rel_my_appointment:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.WHICH_FRAGMENT, 5);
                startActivity(intent);
                return;
            case C1020R.id.rel_self_schedule:
                Intent intent2 = new Intent(this, MainActivity.class);
                intent2.putExtra(Constants.WHICH_FRAGMENT, 3);
                startActivity(intent2);
                return;
            case C1020R.id.rel_my_patients:
                Intent myApp = new Intent(this, MainActivity.class);
                myApp.putExtra(Constants.WHICH_FRAGMENT, 4);
                startActivity(myApp);
                return;
            case C1020R.id.rel_prescription:
                this.homeScreenPresenter.callPrescriptionApi();
                return;
            case C1020R.id.linear_noti:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.WHICH_FRAGMENT, 7);
                startActivity(intent);
                return;
            case C1020R.id.linear_my_profile:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.WHICH_FRAGMENT, 6);
                startActivity(intent);
                return;
            case C1020R.id.linear_chat:
                Intent messages = new Intent(this, MainActivity.class);
                messages.putExtra(Constants.WHICH_FRAGMENT, 12);
                startActivity(messages);
                SharedPreff.showMessageCount(false);
                return;
            case C1020R.id.linear_staff:
                goToDoctorStaff();
                return;
            case C1020R.id.linear_logut:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_logout), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10512());
                return;
            default:
                return;
        }
    }

    public void showNotificationCount(String a) {
        this.tv_not_cout.setVisibility(0);
        this.tv_not_cout.setText(a);
    }

    public void showAppointmentCount(String a) {
        this.img_my_app_count.setVisibility(0);
        this.img_my_app_count.setText(a);
    }

    public void showMyPatientCount(String a) {
        this.tv_patient_count.setVisibility(0);
        this.tv_patient_count.setText(a);
    }

    public void setLocalNotification() {
    }

    public void hideNotificationCount() {
        this.tv_not_cout.setVisibility(4);
    }

    public void hideAppointmentCount() {
        this.img_my_app_count.setVisibility(4);
    }

    public void hideMyPatientCount() {
        this.tv_patient_count.setVisibility(4);
    }

    public void hideMessageCount() {
        this.tv_msg_cout.setVisibility(8);
    }

    public void showMessageCount(String a) {
        this.tv_msg_cout.setVisibility(0);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.homeScreenPresenter.unSubscribeCallbacks();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotificationCountPush event) {
        if (SharedPreff.showMessageCount()) {
            this.tv_msg_cout.setVisibility(0);
        } else {
            this.tv_msg_cout.setVisibility(8);
        }
    }

    public void settingProfilePic(String url) {
        if (url.contains("http") || url.contains("https")) {
            Picasso.with(this).load(url).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_header);
        } else {
            Picasso.with(this).load(Constants.HTTP + url).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_header);
        }
    }

    public void settingNameAndField(String name, String field) {
        this.doc_name.setText(name);
        this.doc_field.setText(field);
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

    public void loginSuccessfull() {
        Intent intent = new Intent(this, DashBoard.class);
        intent.setFlags(268468224);
        startActivity(intent);
    }

    public void gotoPrescriptionScreen(String url, int count) {
        Constants.PRESCRIPTION_COUNT = count;
        Constants.PRESCRIPTION_URL = url;
        Intent myApp = new Intent(this, MainActivity.class);
        myApp.putExtra(Constants.WHICH_FRAGMENT, 11);
        startActivity(myApp);
    }

    public void goToDoctorSearch() {
        SharedPreff.saveFirstTimeDoctorSearch(false);
        SharedPreff.saveFirstTimePharmacySelect(false);
        Intent signUp = new Intent(this, DoctorSearch.class);
        signUp.putExtra(Constants.SHOW_BACK, true);
        startActivity(signUp);
    }

    public void goToDoctorStaff() {
        LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
        if (loginDocResponce != null) {
            Intent signUp = new Intent(this, DoctorStaff.class);
            signUp.putExtra(Constants.SHOW_BACK, true);
            signUp.putExtra(Constants.HIDE_DEACTIVATE, false);
            signUp.putExtra(Constants.DOCTOR_ID, loginDocResponce.getData().get_id());
            signUp.putExtra(Constants.DOCTOR_NAME, getResources().getString(C1020R.string.dr) + " " + loginDocResponce.getData().getFirstName() + " " + loginDocResponce.getData().getLastName());
            startActivity(signUp);
        }
    }
}
