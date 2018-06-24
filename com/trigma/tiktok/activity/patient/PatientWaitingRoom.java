package com.trigma.tiktok.activity.patient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.BaseActivity;
import com.trigma.tiktok.model.PatientModelUpcomingOuter;
import com.trigma.tiktok.presenter.patient.PatientWaitingRoomPresenter;
import com.trigma.tiktok.presenter.patient.PatientWaitingRoomPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import com.tuyenmonkey.textdecorator.TextDecorator;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.Arrays;

public class PatientWaitingRoom extends BaseActivity implements OnClickListener {
    private CircleImageView doc_profile;
    private ImageView img_back;
    private CircleImageView img_profile;
    private NetWorkingService netWorkingService;
    private PatientWaitingRoomPresenter patientWaitingRoomPresenter;
    private String[] toTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"};
    private String[] toTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM"};
    private ArrayList<String> toTimeArr12HrList;
    private ArrayList<String> toTimeArrList;
    private TextView tv_address;
    private TextView tv_doc_name;
    private TextView tv_language;
    private TextView tv_office_phone;
    private TextView tv_phone;
    private TextView tv_qualification;
    private TextView tv_schedule;
    private PatientModelUpcomingOuter upcoming;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.patient_waiting_room);
        this.upcoming = SharedPreff.getPatientUpcomingObject();
        this.toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
        this.toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
        initView();
        settingData();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.patientWaitingRoomPresenter = new PatientWaitingRoomPresenterImp(this, this.netWorkingService);
    }

    private void settingData() {
        if (this.upcoming != null) {
            Log.e("upcoming", new Gson().toJson(this.upcoming));
            String name = "";
            String speciality = "";
            int name_length = 0;
            int speciality_length = 0;
            name = getResources().getString(C1020R.string.dr) + " " + this.upcoming.getDrId().getFirstName() + " " + this.upcoming.getDrId().getLastName() + "," + this.upcoming.getDrId().getQualification();
            if (TextUtils.isEmpty(name)) {
                speciality = "\n" + this.upcoming.getDrId().getSpeciality();
            } else {
                name_length = name.length();
                if (!TextUtils.isEmpty(this.upcoming.getDrId().getSpeciality())) {
                    speciality_length = this.upcoming.getDrId().getSpeciality().length();
                    speciality = "\n" + this.upcoming.getDrId().getSpeciality();
                }
            }
            speciality_length += name_length;
            this.tv_doc_name.setText(name + speciality);
            try {
                TextDecorator.decorate(this.tv_doc_name, name + speciality).setTextColor(C1020R.color.black, 0, name_length + 1).setTextColor(C1020R.color.app_text, name_length + 1, this.tv_doc_name.getText().toString().length()).setTextStyle(1, 0, name_length + 1).setTextStyle(0, name_length + 1, this.tv_doc_name.getText().toString().length()).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.tv_schedule.setText(this.upcoming.getScheduleDate() + " at " + ((String) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(this.upcoming.getFrom()))) + "(" + getMin(gettingDiff(this.upcoming.getFrom(), this.upcoming.getTo())) + ")");
            } catch (Exception e2) {
                e2.printStackTrace();
                this.tv_schedule.setText(this.upcoming.getScheduleDate() + " at " + ((String) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(this.upcoming.getFrom()))));
            }
            this.tv_qualification.setText(this.upcoming.getDrId().getQualification());
            this.tv_language.setText(this.upcoming.getDrId().getLanguages());
            this.tv_phone.setText("+1(" + this.upcoming.getDrId().getCode() + ")" + CommonUtils.phoneFormatter(this.upcoming.getDrId().getMobile()));
            this.tv_office_phone.setText(Html.fromHtml("<u>+1(" + this.upcoming.getDrId().getCode() + ")" + CommonUtils.phoneFormatter(this.upcoming.getDrId().getMobile()) + "</u>"));
            this.tv_address.setText(this.upcoming.getDrId().getAddress());
            Picasso.with(this).load(Constants.HTTP + this.upcoming.getProfile()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.doc_profile);
        }
    }

    public String getMin(int diff) {
        Log.e("DIFFF", "" + diff);
        String result = "";
        if (diff == 1) {
            return "15 Min";
        }
        if (diff == 2) {
            return "30 Min";
        }
        if (diff == 4) {
            return "60 Min";
        }
        if (diff == 6) {
            return "90 Min";
        }
        return result;
    }

    private int gettingDiff(String from, String to) {
        try {
            return this.toTimeArrList.indexOf(to) - this.toTimeArrList.indexOf(from);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private void initView() {
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.img_profile = (CircleImageView) findViewById(C1020R.id.img_profile);
        this.doc_profile = (CircleImageView) findViewById(C1020R.id.doc_profile);
        this.tv_doc_name = (TextView) findViewById(C1020R.id.tv_doc_name);
        this.tv_schedule = (TextView) findViewById(C1020R.id.tv_schedule);
        this.tv_qualification = (TextView) findViewById(C1020R.id.tv_qualification);
        this.tv_language = (TextView) findViewById(C1020R.id.tv_language);
        this.tv_phone = (TextView) findViewById(C1020R.id.tv_phone);
        this.tv_address = (TextView) findViewById(C1020R.id.tv_address);
        this.tv_office_phone = (TextView) findViewById(C1020R.id.tv_office_phone);
        this.tv_phone.setOnClickListener(this);
        this.tv_office_phone.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.tv_phone:
                this.patientWaitingRoomPresenter.makeCall(this.upcoming.getDrId().getCode() + this.upcoming.getDrId().getMobile(), this.tv_phone.getText().toString());
                return;
            case C1020R.id.tv_office_phone:
                this.patientWaitingRoomPresenter.makeCall(this.upcoming.getDrId().getCode() + this.upcoming.getDrId().getMobile(), this.tv_phone.getText().toString());
                return;
            default:
                return;
        }
    }
}
