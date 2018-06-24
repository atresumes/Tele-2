package com.trigma.tiktok.activity.patient;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.share.internal.ShareConstants;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.BaseActivity;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.model.NotificationCountPush;
import com.trigma.tiktok.presenter.patient.HomeScreenPatientPresenter;
import com.trigma.tiktok.presenter.patient.HomeScreenPatientPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.TimeZone;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeScreenPatient extends BaseActivity implements OnClickListener {
    private TextView doc_field;
    private TextView doc_name;
    private HomeScreenPatientPresenter homeScreenPresenter;
    private ImageView img_header;
    private TextView img_my_app_count;
    private int isAnyActiveDoc = 0;
    private int isDoctorAvailable = 0;
    private LinearLayout linear_chat;
    private LinearLayout linear_contact_us;
    private LinearLayout linear_logut;
    private LinearLayout linear_my_profile;
    private LinearLayout linear_noti;
    public NetWorkingService netWorkingService;
    private RelativeLayout rel_doc_staff;
    private RelativeLayout rel_my_appointment;
    private RelativeLayout rel_my_patients;
    private RelativeLayout rel_self_schedule;
    private TextView tv_msg_cout;
    private TextView tv_not_cout;
    private TextView tv_patient_count;
    private TextView tv_self_count;

    class C10881 implements AlertCallBackWithButtonsInterface {
        C10881() {
        }

        public void positiveClick() {
            HomeScreenPatient.this.homeScreenPresenter.logout();
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C10892 implements AlertCallBackInterface {
        C10892() {
        }

        public void neutralClick() {
        }
    }

    class C10903 implements AlertCallBackInterface {
        C10903() {
        }

        public void neutralClick() {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView((int) C1020R.layout.patient_home_screen);
        iniit();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.homeScreenPresenter = new HomeScreenPatientPresenterImp(this, this.netWorkingService);
    }

    protected void onResume() {
        super.onResume();
        this.homeScreenPresenter.startNotificationCountApi();
        this.homeScreenPresenter.checkingAppointmentDates();
        this.homeScreenPresenter.checkingForDoctors(false, false);
        this.homeScreenPresenter.checkDeactivate();
        if (SharedPreff.showMessageCount()) {
            this.tv_msg_cout.setVisibility(0);
        } else {
            this.tv_msg_cout.setVisibility(8);
        }
    }

    private void iniit() {
        this.doc_name = (TextView) findViewById(C1020R.id.doc_name);
        this.doc_field = (TextView) findViewById(C1020R.id.doc_field);
        this.img_my_app_count = (TextView) findViewById(C1020R.id.img_my_app_count);
        this.tv_self_count = (TextView) findViewById(C1020R.id.tv_self_count);
        this.tv_patient_count = (TextView) findViewById(C1020R.id.tv_patient_count);
        this.tv_not_cout = (TextView) findViewById(C1020R.id.tv_not_cout);
        this.tv_msg_cout = (TextView) findViewById(C1020R.id.tv_msg_cout);
        this.rel_my_appointment = (RelativeLayout) findViewById(C1020R.id.rel_my_appointment);
        this.rel_self_schedule = (RelativeLayout) findViewById(C1020R.id.rel_self_schedule);
        this.rel_my_patients = (RelativeLayout) findViewById(C1020R.id.rel_my_patients);
        this.rel_doc_staff = (RelativeLayout) findViewById(C1020R.id.rel_doc_staff);
        this.img_header = (ImageView) findViewById(C1020R.id.img_header);
        this.linear_contact_us = (LinearLayout) findViewById(C1020R.id.linear_contact_us);
        this.linear_noti = (LinearLayout) findViewById(C1020R.id.linear_noti);
        this.linear_my_profile = (LinearLayout) findViewById(C1020R.id.linear_my_profile);
        this.linear_logut = (LinearLayout) findViewById(C1020R.id.linear_logut);
        this.linear_chat = (LinearLayout) findViewById(C1020R.id.linear_chat);
        clickListner();
    }

    private void clickListner() {
        this.linear_contact_us.setOnClickListener(this);
        this.linear_noti.setOnClickListener(this);
        this.linear_my_profile.setOnClickListener(this);
        this.linear_logut.setOnClickListener(this);
        this.linear_chat.setOnClickListener(this);
        this.rel_my_appointment.setOnClickListener(this);
        this.rel_self_schedule.setOnClickListener(this);
        this.rel_my_patients.setOnClickListener(this);
        this.rel_doc_staff.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.linear_contact_us:
                Intent home = new Intent(this, PatientMainActivity.class);
                home.putExtra(Constants.WHICH_FRAGMENT, 1);
                startActivity(home);
                return;
            case C1020R.id.rel_my_appointment:
                Intent mypatient = new Intent(this, PatientMainActivity.class);
                mypatient.putExtra(Constants.WHICH_FRAGMENT, 5);
                startActivity(mypatient);
                return;
            case C1020R.id.rel_self_schedule:
                this.homeScreenPresenter.checkingForDoctors(true, true);
                return;
            case C1020R.id.rel_my_patients:
                if (this.isDoctorAvailable == 2) {
                    Intent myApp = new Intent(this, PatientMainActivity.class);
                    myApp.putExtra(Constants.WHICH_FRAGMENT, 8);
                    startActivity(myApp);
                    return;
                } else if (this.isDoctorAvailable == 1) {
                    DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.your_doctor_has_not_verified_your_account_yet), new C10892());
                    return;
                } else {
                    this.homeScreenPresenter.checkingForDoctors(false, false);
                    return;
                }
            case C1020R.id.linear_noti:
                Intent noti = new Intent(this, PatientMainActivity.class);
                noti.putExtra(Constants.WHICH_FRAGMENT, 7);
                startActivity(noti);
                return;
            case C1020R.id.linear_my_profile:
                Intent my_profile = new Intent(this, PatientMainActivity.class);
                my_profile.putExtra(Constants.WHICH_FRAGMENT, 9);
                startActivity(my_profile);
                return;
            case C1020R.id.linear_chat:
                Intent messages = new Intent(this, PatientMainActivity.class);
                messages.putExtra(Constants.WHICH_FRAGMENT, 12);
                startActivity(messages);
                SharedPreff.showMessageCount(false);
                return;
            case C1020R.id.linear_logut:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_logout), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10881());
                return;
            case C1020R.id.rel_doc_staff:
                this.homeScreenPresenter.checkingForDoctors(true, false);
                return;
            default:
                return;
        }
    }

    public void goToDoctorStaff() {
        startActivity(new Intent(this, PatientSideStaffList.class));
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

    public void hideMessageCount() {
        this.tv_msg_cout.setVisibility(8);
    }

    public void showMessageCount(String a) {
        this.tv_msg_cout.setVisibility(0);
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

    public void loginOutSuccessfull() {
        Intent intent = new Intent(this, DashBoard.class);
        intent.setFlags(268468224);
        startActivity(intent);
    }

    public void setDoctors(ArrayList<MyDoctorsObject> data) {
        if (data.size() > 0) {
            this.isDoctorAvailable = 2;
            SharedPreff.saveDoctorList(data);
            Log.e("doctor", "available");
            return;
        }
        this.isDoctorAvailable = 1;
        Log.e("doctor", " not available");
    }

    public void addingEvent() {
        Uri baseUri;
        ContentValues event = new ContentValues();
        event.put("calendar_id", Integer.valueOf(1));
        event.put("title", "reminder");
        event.put(ShareConstants.WEB_DIALOG_PARAM_DESCRIPTION, "dummy event");
        event.put("eventLocation", "chandigarh");
        event.put("dtstart", "1498203000000");
        event.put("dtend", "1498204800000");
        event.put("allDay", Integer.valueOf(0));
        event.put("hasAlarm", Integer.valueOf(1));
        event.put("eventTimezone", TimeZone.getDefault().getID());
        String reminderUriString = "content://com.android.calendar/reminders";
        if (VERSION.SDK_INT >= 8) {
            baseUri = Uri.parse("content://com.android.calendar/events");
            reminderUriString = "content://com.android.calendar/reminders";
        } else {
            baseUri = Uri.parse("content://calendar/events");
            reminderUriString = "content://calendar/reminders";
        }
        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(this) + "reminders");
        Uri eventU = getContentResolver().insert(baseUri, event);
        event = new ContentValues();
        event.put("minutes", Integer.valueOf(1));
        event.put("method", Integer.valueOf(1));
        event.put("event_id", Long.valueOf(Long.parseLong(eventU.getLastPathSegment())));
        getContentResolver().insert(REMINDERS_URI, event);
    }

    private String getCalendarUriBase(Activity act) {
        Cursor managedCursor = null;
        try {
            managedCursor = act.managedQuery(Uri.parse("content://calendar/calendars"), null, null, null, null);
        } catch (Exception e) {
        }
        if (managedCursor != null) {
            return "content://calendar/";
        }
        try {
            managedCursor = act.managedQuery(Uri.parse("content://com.android.calendar/calendars"), null, null, null, null);
        } catch (Exception e2) {
        }
        if (managedCursor != null) {
            return "content://com.android.calendar/";
        }
        return null;
    }

    public void isDoctorActive(boolean status, boolean goToSchedule) {
        if (!status) {
            DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.your_doctor_has_not_verified_your_account_yet), new C10903());
        } else if (goToSchedule) {
            Intent intent = new Intent(this, PatientMainActivity.class);
            intent.putExtra(Constants.WHICH_FRAGMENT, 10);
            startActivity(intent);
        } else {
            goToDoctorStaff();
        }
    }
}
