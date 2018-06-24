package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorSearch;
import com.trigma.tiktok.adapter.StaffNavigationAdapter;
import com.trigma.tiktok.model.LoginDocData;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.NotificationCountPush;
import com.trigma.tiktok.model.StaffDoctorListObject;
import com.trigma.tiktok.presenter.StaffHomePresenter;
import com.trigma.tiktok.presenter.StaffHomePresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.objectweb.asm.Opcodes;

public class StaffHomeScreen extends BaseActivity implements OnClickListener {
    public static DrawerLayout drawerLayout;
    private TextView doc_field;
    private TextView doc_name;
    ActionBarDrawerToggle drawerToggle;
    private StaffHomePresenter homeScreenPresenter;
    private ImageView img_doc_search;
    private ImageView img_doc_video;
    private ImageView img_header;
    private TextView img_my_app_count;
    private ImageView img_slider;
    private int left_side_selected = -1;
    private View line_left_bottom;
    private View line_right_bottom;
    private LinearLayout linear_chat;
    private LinearLayout linear_contact_us;
    private LinearLayout linear_logut;
    private LinearLayout linear_my_profile;
    private LinearLayout linear_staff;
    public NetWorkingService netWorkingService;
    RecyclerView recyclerView;
    private RelativeLayout rel_my_appointment;
    private RelativeLayout rel_my_patients;
    private RelativeLayout rel_prescription;
    private RelativeLayout rel_self_schedule;
    private StaffNavigationAdapter staffNavigationAdapter;
    private TextView tv_msg_cout;
    private TextView tv_not_cout;
    private TextView tv_patient_count;
    private TextView tv_prescrip_count;
    private TextView tv_self_count;

    class C10731 implements AlertCallBackInterface {
        C10731() {
        }

        public void neutralClick() {
            SharedPreff.clearDocPreff();
            Intent intent = new Intent(StaffHomeScreen.this, DashBoard.class);
            intent.setFlags(268468224);
            StaffHomeScreen.this.startActivity(intent);
        }
    }

    class C10742 implements DrawerListener {
        C10742() {
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        public void onDrawerOpened(View drawerView) {
            StaffHomeScreen.this.homeScreenPresenter.fetchStaffDocList(SharedPreff.getStaffLoginResponse().getData().get_id(), false);
        }

        public void onDrawerClosed(View drawerView) {
        }

        public void onDrawerStateChanged(int newState) {
        }
    }

    class C10753 implements AlertCallBackWithButtonsInterface {
        C10753() {
        }

        public void positiveClick() {
            StaffHomeScreen.this.homeScreenPresenter.logout(SharedPreff.getStaffLoginResponse().getData().get_id());
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C10764 implements AlertCallBackInterface {
        C10764() {
        }

        public void neutralClick() {
            StaffHomeScreen.this.homeScreenPresenter.fetchStaffDocList(SharedPreff.getStaffLoginResponse().getData().get_id(), true);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.STAFF_HOME_CONTEXT = this;
        setContentView((int) C1020R.layout.staff_home_screen);
        EventBus.getDefault().register(this);
        iniit();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.homeScreenPresenter = new StaffHomePresenterImp(this, this.netWorkingService);
        this.homeScreenPresenter.fetchStaffDocList(SharedPreff.getStaffLoginResponse().getData().get_id(), true);
    }

    protected void onResume() {
        super.onResume();
        if (this.left_side_selected > 0) {
            this.homeScreenPresenter.startNotificationCountApi();
            this.homeScreenPresenter.checkingAppointmentDates();
        }
        if (SharedPreff.showMessageCount()) {
            this.tv_msg_cout.setVisibility(0);
        } else {
            this.tv_msg_cout.setVisibility(8);
        }
    }

    public void hideMessageCount() {
        this.tv_msg_cout.setVisibility(8);
    }

    public void showMessageCount(String a) {
        this.tv_msg_cout.setVisibility(0);
    }

    public void checkPrescriptionVisibility(String val) {
        LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
        if (loginDocResponce != null) {
            if (loginDocResponce.getData().getDrType() != null) {
                if (!loginDocResponce.getData().getDrType().equalsIgnoreCase(val)) {
                    DialogPopUps.alertPopUp(this, getResources().getString(C1020R.string.your_prescription_has_been_changed_by_admin_please_log_in_into_the_app_again), getResources().getString(C1020R.string.ok), new C10731());
                } else if (val.equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
                    this.line_left_bottom.setVisibility(8);
                    this.line_right_bottom.setVisibility(8);
                    this.rel_prescription.setVisibility(8);
                } else {
                    this.line_left_bottom.setVisibility(0);
                    this.line_right_bottom.setVisibility(0);
                    this.rel_prescription.setVisibility(0);
                }
            } else if (val.equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
                this.line_left_bottom.setVisibility(8);
                this.line_right_bottom.setVisibility(8);
                this.rel_prescription.setVisibility(8);
            } else {
                this.line_left_bottom.setVisibility(0);
                this.line_right_bottom.setVisibility(0);
                this.rel_prescription.setVisibility(0);
            }
        } else if (val.equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
            this.line_left_bottom.setVisibility(8);
            this.line_right_bottom.setVisibility(8);
            this.rel_prescription.setVisibility(8);
        } else {
            this.line_left_bottom.setVisibility(0);
            this.line_right_bottom.setVisibility(0);
            this.rel_prescription.setVisibility(0);
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
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_doc_search = (ImageView) findViewById(C1020R.id.img_doc_search);
        this.img_doc_video = (ImageView) findViewById(C1020R.id.img_doc_video);
        this.linear_contact_us = (LinearLayout) findViewById(C1020R.id.linear_contact_us);
        this.linear_my_profile = (LinearLayout) findViewById(C1020R.id.linear_my_profile);
        this.linear_logut = (LinearLayout) findViewById(C1020R.id.linear_logut);
        this.linear_chat = (LinearLayout) findViewById(C1020R.id.linear_chat);
        this.linear_staff = (LinearLayout) findViewById(C1020R.id.linear_staff);
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        drawerLayout = (DrawerLayout) findViewById(C1020R.id.drawerMainActivity);
        this.staffNavigationAdapter = new StaffNavigationAdapter(this.homeScreenPresenter, this);
        this.recyclerView.setAdapter(this.staffNavigationAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.getLayoutParams().width = (int) (((double) this.deviceWidth) / 1.2d);
        this.recyclerView.requestLayout();
        setupDrawerToggle();
        LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
        if (loginDocResponce != null && loginDocResponce.getData().getDrType() != null) {
            if (loginDocResponce.getData().getDrType().equalsIgnoreCase(Constants.PRESCRIPTIONPROHIBITED)) {
                this.line_left_bottom.setVisibility(8);
                this.line_right_bottom.setVisibility(8);
                this.rel_prescription.setVisibility(8);
                return;
            }
            this.line_left_bottom.setVisibility(0);
            this.line_right_bottom.setVisibility(0);
            this.rel_prescription.setVisibility(0);
        }
    }

    void setupDrawerToggle() {
        this.drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, C1020R.string.app_name, C1020R.string.app_name);
        this.drawerToggle.syncState();
        drawerLayout.addDrawerListener(new C10742());
    }

    private void clickListner() {
        this.linear_contact_us.setOnClickListener(this);
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
        this.img_slider.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case C1020R.id.img_slider:
                drawerLayout.openDrawer(3);
                return;
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
                goToDoctorSearch();
                return;
            case C1020R.id.linear_noti:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.WHICH_FRAGMENT, 7);
                startActivity(intent);
                return;
            case C1020R.id.linear_my_profile:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.WHICH_FRAGMENT, 13);
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
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_logout), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10753());
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

    public void hideNotificationCount() {
        this.tv_not_cout.setVisibility(4);
    }

    public void hideAppointmentCount() {
        this.img_my_app_count.setVisibility(4);
    }

    public void hideMyPatientCount() {
        this.tv_patient_count.setVisibility(4);
    }

    protected void onDestroy() {
        super.onDestroy();
        Constants.STAFF_HOME_CONTEXT = null;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkDoctorDist(String event) {
        if (event.equalsIgnoreCase(Constants.STAFF_NOTIFICATION_STATUS)) {
            this.homeScreenPresenter.fetchStaffDocList(SharedPreff.getStaffLoginResponse().getData().get_id(), true);
        }
    }

    public void settingProfilePic(String url) {
        if (url.contains("http") || url.contains("https")) {
            Picasso.with(this).load(url).resize(Opcodes.FCMPG, Opcodes.FCMPG).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_header);
        } else {
            Picasso.with(this).load(Constants.HTTP + url).resize(Opcodes.FCMPG, Opcodes.FCMPG).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_header);
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

    public void showErrorWithInterface(String error) {
        DialogPopUps.alertPopUp(this, error, getResources().getString(C1020R.string.ok_dialog), new C10764());
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
            signUp.putExtra(Constants.HIDE_DEACTIVATE, true);
            signUp.putExtra(Constants.DOCTOR_ID, loginDocResponce.getData().get_id());
            signUp.putExtra(Constants.DOCTOR_NAME, getResources().getString(C1020R.string.dr) + " " + loginDocResponce.getData().getFirstName() + " " + loginDocResponce.getData().getLastName());
            startActivity(signUp);
        }
    }

    public void settingLeftMenuAdapter(ArrayList<StaffDoctorListObject> data, boolean firstTime) {
        if (data != null && data.size() > 0) {
            this.left_side_selected = 1;
            ArrayList<StaffDoctorListObject> mainList = new ArrayList();
            for (int k = 0; k < data.size(); k++) {
                if (((StaffDoctorListObject) data.get(k)).getStaffStatus() == 1) {
                    mainList.add(data.get(k));
                }
            }
            this.staffNavigationAdapter.addingList(mainList, firstTime);
            if (mainList.size() > 0) {
                if (firstTime) {
                    LoginDocResponce loginDocResponce = new LoginDocResponce();
                    loginDocResponce.setStatus(Constants.STATUS_OK);
                    LoginDocData loginDocData = new LoginDocData();
                    loginDocData.setDrRequest(1);
                    loginDocData.setDrCode(null);
                    loginDocData.setEmail(((StaffDoctorListObject) mainList.get(0)).getEmail());
                    loginDocData.set_id(((StaffDoctorListObject) mainList.get(0)).getDrId());
                    loginDocData.setAddress("");
                    loginDocData.setBio("");
                    loginDocData.setCity("");
                    loginDocData.setUserType(1);
                    loginDocData.setAdminStatus(1);
                    loginDocData.setCode(Integer.parseInt(((StaffDoctorListObject) mainList.get(0)).getCode()));
                    loginDocData.setDeviceType(Constants.DEVICE_TYPE);
                    try {
                        loginDocData.setFirstName(CommonUtils.getFirstNameAndLastName(((StaffDoctorListObject) mainList.get(0)).getName(), true));
                        loginDocData.setLastName(CommonUtils.getFirstNameAndLastName(((StaffDoctorListObject) mainList.get(0)).getName(), false));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginDocData.setQualification("");
                    loginDocData.setState("");
                    loginDocData.setMobile(((StaffDoctorListObject) mainList.get(0)).getMobile());
                    loginDocData.setGender(((StaffDoctorListObject) mainList.get(0)).getGender());
                    loginDocData.setMediaType("M");
                    loginDocData.setLanguages("");
                    loginDocData.setLat(SharedPreff.getLat());
                    loginDocData.setLong(SharedPreff.getLng());
                    loginDocData.setZipcode("");
                    loginDocData.setPharmacyName("");
                    loginDocData.setLoginAllready(AppEventsConstants.EVENT_PARAM_VALUE_YES);
                    loginDocData.setSpeciality(((StaffDoctorListObject) mainList.get(0)).getSpeciality());
                    loginDocData.setProfilePic(((StaffDoctorListObject) mainList.get(0)).getProfilePic());
                    loginDocResponce.setData(loginDocData);
                    SharedPreff.saveLoginResponce(loginDocResponce);
                    this.homeScreenPresenter.startNotificationCountApi();
                    this.homeScreenPresenter.checkingAppointmentDates();
                    clickListner();
                }
            } else if (!firstTime) {
                clearPreffAndLoggOut();
            } else if (Constants.CONTEXT != null) {
                staffDeactivated(Constants.CONTEXT);
            } else {
                clearPreffAndLoggOut();
            }
        }
    }

    public void clearPreffAndLoggOut() {
        SharedPreff.clearDocPreff();
        Intent intent = new Intent(this, DashBoard.class);
        intent.setFlags(268468224);
        startActivity(intent);
    }

    public void navigationItemClicked(int pos, LoginDocResponce loginDocResponce) {
        drawerLayout.closeDrawers();
        if (this.staffNavigationAdapter != null) {
            this.staffNavigationAdapter.notifyDataSetChanged();
            this.homeScreenPresenter.startNotificationCountApi();
            this.homeScreenPresenter.checkingAppointmentDates();
        }
    }
}
