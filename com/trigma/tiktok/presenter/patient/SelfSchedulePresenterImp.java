package com.trigma.tiktok.presenter.patient;

import android.text.TextUtils;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.SelfSchedule;
import com.trigma.tiktok.model.AppointmentListObject;
import com.trigma.tiktok.model.AppointmentListResponse;
import com.trigma.tiktok.model.BookingPatientObject;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.model.SchedulePojo;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface2;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelfSchedulePresenterImp implements SelfSchedulePresenter {
    public ArrayList<BookingPatientObject> BookingList = new ArrayList();
    private int addToIndex = 1;
    private ArrayList<String> bookedFromTimeList = new ArrayList();
    private ArrayList<String> bookedfromTime12HrList = new ArrayList();
    private String bookingId;
    private ArrayList<MyDoctorsObject> dataDoctorMainList = new ArrayList();
    private ArrayList<String> dateList = new ArrayList();
    private String docDeviceToken = "";
    private ArrayList<String> doctorStringList = new ArrayList();
    private ArrayList<String> durationList = new ArrayList();
    private ArrayList<String> finalFromTimeList = new ArrayList();
    private ArrayList<String> finalfromTime12HrList = new ArrayList();
    private ArrayList<String> fromTime12HrList = new ArrayList();
    private ArrayList<String> fromTime12HrListOriginal = new ArrayList();
    private String[] fromTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"};
    private String[] fromTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM"};
    private ArrayList<ArrayList<String>> fromTimeArray12HrList = new ArrayList();
    private ArrayList<ArrayList<String>> fromTimeArrayList = new ArrayList();
    private ArrayList<String> fromTimeList = new ArrayList();
    private ArrayList<String> fromTimeListOriginal = new ArrayList();
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    public ArrayList<AppointmentListObject> mainList = new ArrayList();
    private ArrayList<String> monthList = new ArrayList();
    private NetWorkingService netWorkingService;
    public ArrayList<SchedulePojo> scheduleList = new ArrayList();
    private String selected_date;
    private String selected_doc;
    private String selected_doc_email;
    private String selected_doc_id;
    private String selected_duration;
    private String selected_month;
    private String selected_time;
    private int selected_time_index = 0;
    private String selected_year;
    private ArrayList<String> tempTime12HrList = new ArrayList();
    private ArrayList<String> tempTimeList = new ArrayList();
    private SelfSchedule view;
    private ArrayList<String> yearList = new ArrayList();

    class C13641 implements AlertCallBackWithButtonsInterface2 {
        C13641() {
        }

        public void positiveClick(int selected) {
            Log.e("strDate", "" + ((String) SelfSchedulePresenterImp.this.dateList.get(selected)));
            SelfSchedulePresenterImp.this.view.setDate((String) SelfSchedulePresenterImp.this.dateList.get(selected));
            SelfSchedulePresenterImp.this.selected_date = (String) SelfSchedulePresenterImp.this.dateList.get(selected);
            SelfSchedulePresenterImp.this.selected_duration = null;
            SelfSchedulePresenterImp.this.selected_time = null;
            SelfSchedulePresenterImp.this.view.setDuaration(SelfSchedulePresenterImp.this.selected_duration);
            SelfSchedulePresenterImp.this.view.setTime(SelfSchedulePresenterImp.this.selected_time);
            SelfSchedulePresenterImp.this.view.setCompleteDate(SelfSchedulePresenterImp.this.selected_month + "" + SelfSchedulePresenterImp.this.selected_date + "," + SelfSchedulePresenterImp.this.selected_year);
            int a = 0;
            while (a < SelfSchedulePresenterImp.this.mainList.size()) {
                if (((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(a)).getMonth().equalsIgnoreCase(SelfSchedulePresenterImp.this.selected_month) && ((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(a)).getYear().equalsIgnoreCase(SelfSchedulePresenterImp.this.selected_year) && ((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(a)).getDate().equalsIgnoreCase(SelfSchedulePresenterImp.this.selected_date)) {
                    SelfSchedulePresenterImp.this.durationList = ((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(a)).getSloat();
                    SelfSchedulePresenterImp.this.BookingList = ((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(a)).getBooking();
                    SelfSchedulePresenterImp.this.scheduleList = ((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(a)).getSchedule();
                    SelfSchedulePresenterImp.this.bookingId = ((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(a)).getBookId();
                    SelfSchedulePresenterImp.this.fromTime12HrListOriginal = new ArrayList(Arrays.asList(SelfSchedulePresenterImp.this.fromTimeArr12Hr));
                    SelfSchedulePresenterImp.this.fromTimeListOriginal = new ArrayList(Arrays.asList(SelfSchedulePresenterImp.this.fromTimeArr));
                    Log.e("BookingList", "" + new Gson().toJson(SelfSchedulePresenterImp.this.BookingList));
                    Log.e("schedule", "" + new Gson().toJson(SelfSchedulePresenterImp.this.scheduleList));
                } else {
                    Log.e("durationList", "not added");
                }
                a++;
            }
            Set<String> hs = new LinkedHashSet();
            hs.addAll(SelfSchedulePresenterImp.this.durationList);
            SelfSchedulePresenterImp.this.durationList.clear();
            SelfSchedulePresenterImp.this.durationList.addAll(hs);
            Log.e("durationList", "added");
            Log.e("durationList", "" + SelfSchedulePresenterImp.this.durationList);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C13652 implements AlertCallBackWithButtonsInterface2 {
        C13652() {
        }

        public void positiveClick(int selected) {
            SelfSchedulePresenterImp.this.selected_year = (String) SelfSchedulePresenterImp.this.yearList.get(selected);
            Log.e("selected_year", SelfSchedulePresenterImp.this.selected_year);
            SelfSchedulePresenterImp.this.selected_month = null;
            SelfSchedulePresenterImp.this.selected_date = null;
            SelfSchedulePresenterImp.this.selected_duration = null;
            SelfSchedulePresenterImp.this.selected_time = null;
            SelfSchedulePresenterImp.this.view.setYear(SelfSchedulePresenterImp.this.selected_year);
            SelfSchedulePresenterImp.this.view.setMonth(SelfSchedulePresenterImp.this.selected_month);
            SelfSchedulePresenterImp.this.view.setDate(SelfSchedulePresenterImp.this.selected_date);
            SelfSchedulePresenterImp.this.view.setDuaration(SelfSchedulePresenterImp.this.selected_duration);
            SelfSchedulePresenterImp.this.view.setTime(SelfSchedulePresenterImp.this.selected_time);
            SelfSchedulePresenterImp.this.view.setCompleteDate(null);
            SelfSchedulePresenterImp.this.monthList.clear();
            for (int k = 0; k < SelfSchedulePresenterImp.this.mainList.size(); k++) {
                if (((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(k)).getYear().equalsIgnoreCase(SelfSchedulePresenterImp.this.selected_year)) {
                    SelfSchedulePresenterImp.this.monthList.add(((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(k)).getMonth());
                }
            }
            Set<String> hs = new LinkedHashSet();
            hs.addAll(SelfSchedulePresenterImp.this.monthList);
            SelfSchedulePresenterImp.this.monthList.clear();
            SelfSchedulePresenterImp.this.monthList.addAll(hs);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C13663 implements AlertCallBackWithButtonsInterface2 {
        C13663() {
        }

        public void positiveClick(int selected) {
            SelfSchedulePresenterImp.this.view.setMonth((String) SelfSchedulePresenterImp.this.monthList.get(selected));
            SelfSchedulePresenterImp.this.selected_month = (String) SelfSchedulePresenterImp.this.monthList.get(selected);
            SelfSchedulePresenterImp.this.selected_date = null;
            SelfSchedulePresenterImp.this.selected_duration = null;
            SelfSchedulePresenterImp.this.selected_time = null;
            SelfSchedulePresenterImp.this.view.setMonth(SelfSchedulePresenterImp.this.selected_month);
            SelfSchedulePresenterImp.this.view.setDate(SelfSchedulePresenterImp.this.selected_date);
            SelfSchedulePresenterImp.this.view.setDuaration(SelfSchedulePresenterImp.this.selected_duration);
            SelfSchedulePresenterImp.this.view.setTime(SelfSchedulePresenterImp.this.selected_time);
            SelfSchedulePresenterImp.this.view.setCompleteDate(null);
            SelfSchedulePresenterImp.this.dateList.clear();
            int k = 0;
            while (k < SelfSchedulePresenterImp.this.mainList.size()) {
                if (((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(k)).getYear().equalsIgnoreCase(SelfSchedulePresenterImp.this.selected_year) && ((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(k)).getMonth().equalsIgnoreCase(SelfSchedulePresenterImp.this.selected_month)) {
                    SelfSchedulePresenterImp.this.dateList.add(((AppointmentListObject) SelfSchedulePresenterImp.this.mainList.get(k)).getDate());
                }
                k++;
            }
            Set<String> hs = new LinkedHashSet();
            hs.addAll(SelfSchedulePresenterImp.this.dateList);
            SelfSchedulePresenterImp.this.dateList.clear();
            SelfSchedulePresenterImp.this.dateList.addAll(hs);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C13674 implements AlertCallBackWithButtonsInterface2 {
        C13674() {
        }

        public void positiveClick(int selected) {
            SelfSchedulePresenterImp.this.selected_doc = (String) SelfSchedulePresenterImp.this.doctorStringList.get(selected);
            SelfSchedulePresenterImp.this.selected_doc_id = ((MyDoctorsObject) SelfSchedulePresenterImp.this.dataDoctorMainList.get(selected)).getDrId();
            SelfSchedulePresenterImp.this.selected_doc_email = ((MyDoctorsObject) SelfSchedulePresenterImp.this.dataDoctorMainList.get(selected)).getEmail();
            SelfSchedulePresenterImp.this.docDeviceToken = ((MyDoctorsObject) SelfSchedulePresenterImp.this.dataDoctorMainList.get(selected)).getDeviceToken();
            Log.e("selectedItem", SelfSchedulePresenterImp.this.selected_doc);
            Log.e("selected_doc_id", SelfSchedulePresenterImp.this.selected_doc_id);
            SelfSchedulePresenterImp.this.view.settingDoctor(SelfSchedulePresenterImp.this.selected_doc);
            SelfSchedulePresenterImp.this.selected_year = null;
            SelfSchedulePresenterImp.this.selected_month = null;
            SelfSchedulePresenterImp.this.selected_date = null;
            SelfSchedulePresenterImp.this.selected_duration = null;
            SelfSchedulePresenterImp.this.selected_time = null;
            SelfSchedulePresenterImp.this.view.setYear(SelfSchedulePresenterImp.this.selected_year);
            SelfSchedulePresenterImp.this.view.setMonth(SelfSchedulePresenterImp.this.selected_month);
            SelfSchedulePresenterImp.this.view.setDate(SelfSchedulePresenterImp.this.selected_date);
            SelfSchedulePresenterImp.this.view.setDuaration(SelfSchedulePresenterImp.this.selected_duration);
            SelfSchedulePresenterImp.this.view.setTime(SelfSchedulePresenterImp.this.selected_time);
            SelfSchedulePresenterImp.this.view.setCompleteDate(null);
            SelfSchedulePresenterImp.this.yearList.clear();
            SelfSchedulePresenterImp.this.fetchDocDetail(SelfSchedulePresenterImp.this.selected_doc_id);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C13685 implements AlertCallBackWithButtonsInterface2 {
        C13685() {
        }

        public void positiveClick(int selected) {
            Log.e("durationList", "" + ((String) SelfSchedulePresenterImp.this.durationList.get(selected)));
            SelfSchedulePresenterImp.this.view.setDuaration((String) SelfSchedulePresenterImp.this.durationList.get(selected));
            SelfSchedulePresenterImp.this.selected_duration = (String) SelfSchedulePresenterImp.this.durationList.get(selected);
            SelfSchedulePresenterImp.this.selected_time = null;
            SelfSchedulePresenterImp.this.view.setDuaration(SelfSchedulePresenterImp.this.selected_duration);
            SelfSchedulePresenterImp.this.view.setTime(SelfSchedulePresenterImp.this.selected_time);
            if (SelfSchedulePresenterImp.this.selected_duration.trim().equalsIgnoreCase("30 Min")) {
                SelfSchedulePresenterImp.this.addToIndex = 2;
                SelfSchedulePresenterImp.this.filterTimeList(2);
            } else if (SelfSchedulePresenterImp.this.selected_duration.trim().equalsIgnoreCase("60 Min")) {
                SelfSchedulePresenterImp.this.addToIndex = 4;
                SelfSchedulePresenterImp.this.filterTimeList(4);
            } else if (SelfSchedulePresenterImp.this.selected_duration.trim().equalsIgnoreCase("90 Min")) {
                SelfSchedulePresenterImp.this.addToIndex = 6;
                SelfSchedulePresenterImp.this.filterTimeList(6);
            } else if (SelfSchedulePresenterImp.this.selected_duration.trim().equalsIgnoreCase("15 Min")) {
                SelfSchedulePresenterImp.this.addToIndex = 1;
                SelfSchedulePresenterImp.this.filterTimeList(1);
            } else {
                Log.e("tempTimeList", "no condition match");
            }
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C13696 implements AlertCallBackWithButtonsInterface2 {
        C13696() {
        }

        public void positiveClick(int selected) {
            Log.e("time", "" + ((String) SelfSchedulePresenterImp.this.tempTime12HrList.get(selected)));
            SelfSchedulePresenterImp.this.view.setTime((String) SelfSchedulePresenterImp.this.tempTime12HrList.get(selected));
            SelfSchedulePresenterImp.this.selected_time = (String) SelfSchedulePresenterImp.this.tempTimeList.get(selected);
            SelfSchedulePresenterImp.this.selected_time_index = selected;
            Log.e("selected_time", "" + SelfSchedulePresenterImp.this.selected_time);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C13738 implements Observer<AppointmentListResponse> {
        C13738() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (SelfSchedulePresenterImp.this.view != null) {
                SelfSchedulePresenterImp.this.view.hideDialog();
            }
            SelfSchedulePresenterImp.this.view.showError(SelfSchedulePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
        }

        public void onNext(AppointmentListResponse appointmentListResponse) {
            if (SelfSchedulePresenterImp.this.view != null) {
                SelfSchedulePresenterImp.this.view.hideDialog();
            }
            if (appointmentListResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                if (SelfSchedulePresenterImp.this.view != null) {
                    Log.e("appointmentListResponse", new Gson().toJson((Object) appointmentListResponse));
                    SelfSchedulePresenterImp.this.mainList = appointmentListResponse.getData();
                    SelfSchedulePresenterImp.this.getYearList();
                }
            } else if (appointmentListResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                if (SelfSchedulePresenterImp.this.view != null) {
                    SelfSchedulePresenterImp.this.view.showError(SelfSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.sorry_for_the_inconvenience_doctor_has_yet_to_set_their_available_timings));
                }
                SelfSchedulePresenterImp.this.view.settingDoctor(null);
            } else {
                if (SelfSchedulePresenterImp.this.view != null) {
                    SelfSchedulePresenterImp.this.view.showError(appointmentListResponse.getError());
                }
                SelfSchedulePresenterImp.this.view.settingDoctor(null);
            }
        }
    }

    public SelfSchedulePresenterImp(SelfSchedule view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        gettingDocStringList();
        this.fromTime12HrListOriginal = new ArrayList(Arrays.asList(this.fromTimeArr12Hr));
        this.fromTimeListOriginal = new ArrayList(Arrays.asList(this.fromTimeArr));
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }

    public void openDatePicker() {
        if (TextUtils.isEmpty(this.selected_month)) {
            this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_month));
        } else if (this.dateList.size() > 0) {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.dateList), new C13641());
        }
    }

    public void openYearPicker() {
        if (this.yearList.size() <= 0 || TextUtils.isEmpty(this.selected_doc)) {
            this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_doctor));
            return;
        }
        Log.e("yearList", "" + this.yearList);
        DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.yearList), new C13652());
    }

    public void openMonthPicker() {
        if (TextUtils.isEmpty(this.selected_year)) {
            this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_year));
        } else if (this.monthList.size() > 0) {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.monthList), new C13663());
        } else {
            this.view.showError(this.view.getActivity().getResources().getString(C1020R.string.sorry_for_the_inconvenience_doctor_has_yet_to_set_their_available_timings));
        }
    }

    public void openDoctorPicker() {
        DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.doctorStringList), new C13674());
    }

    public void openDurationPicker() {
        Log.e("durationList", "" + this.durationList);
        if (TextUtils.isEmpty(this.selected_date)) {
            this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_date));
        } else if (this.durationList.size() > 0) {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.durationList), new C13685());
        } else {
            this.view.showError(this.view.getActivity().getResources().getString(C1020R.string.sorry_for_the_inconvenience_doctor_has_yet_to_set_their_available_timings));
        }
    }

    public void openTimePicker() {
        if (TextUtils.isEmpty(this.selected_duration)) {
            this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_duration));
        } else if (this.tempTime12HrList.size() > 0) {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.tempTime12HrList), new C13696());
        } else {
            this.view.showError(this.view.getActivity().getResources().getString(C1020R.string.sorry_for_the_inconvenience_doctor_has_yet_to_set_their_available_timings));
            Log.e("tempTime12HrList", "null");
        }
    }

    public void saveButtonAction() {
        if (TextUtils.isEmpty(this.selected_doc) || TextUtils.isEmpty(this.selected_year) || TextUtils.isEmpty(this.selected_month) || TextUtils.isEmpty(this.selected_date) || TextUtils.isEmpty(this.selected_duration) || TextUtils.isEmpty(this.selected_time)) {
            this.view.showError(this.view.getActivity().getResources().getString(C1020R.string.please_enter_a_all_details));
            return;
        }
        Log.e("selected_doc", "" + this.selected_doc);
        Log.e("selected_doc_id", "" + this.selected_doc_id);
        Log.e("selected_doc_email", this.selected_doc_email);
        try {
            Log.e("patient_email", this.loginDocResponce.getData().getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("device token", SharedPreff.getDeviceToken());
        Log.e("selected_year", this.selected_year);
        Log.e("selected_month", this.selected_month);
        Log.e("selected_date", this.selected_date);
        Log.e("selected_duration", this.selected_duration);
        Log.e("selected_time", this.selected_time);
        String dateToSend = "";
        dateToSend = "" + this.selected_month + " " + this.selected_date + "," + this.selected_year;
        Log.e("List_Size", "" + this.tempTimeList.size());
        Log.e("SELECETD", "" + this.selected_time_index);
        Log.e("addToIndex", "" + this.addToIndex);
        final String dateStart = this.selected_date + "-" + this.selected_month + "-" + this.selected_year + " " + this.selected_time + ":00";
        final String dateEnd = this.selected_date + "-" + this.selected_month + "-" + this.selected_year + " " + ((String) this.fromTimeListOriginal.get(this.fromTimeListOriginal.indexOf(this.selected_time) + this.addToIndex)) + ":00";
        Log.e("dateStart", dateStart);
        Log.e("dateEnd", dateEnd);
        Log.e("tempTimeList", "" + this.tempTimeList);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
        try {
            if (Integer.parseInt(this.selected_date) < 10 && !this.selected_date.contains(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                this.selected_date = AppEventsConstants.EVENT_PARAM_VALUE_NO + this.selected_date;
            }
            if (new Date().after(simpleDateFormat.parse(this.selected_date + "/" + CommonUtils.getMonthInteger(this.selected_month) + "/" + this.selected_year + " " + this.selected_time + ":00"))) {
                this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_a_time_greater_than_the_current_time));
            } else if (this.loginDocResponce == null) {
            } else {
                if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
                    this.view.showProgressDialog();
                    this.mSubscription = this.netWorkingService.getAPI().patientsPatientBookDr(this.bookingId, this.loginDocResponce.getData().get_id(), this.selected_doc_id, this.selected_time, this.selected_duration.replace("Min", "").trim(), dateToSend, this.docDeviceToken, this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName(), this.selected_doc_email, this.loginDocResponce.getData().getEmail()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                        class C13711 implements AlertCallBackWithButtonsInterface {

                            class C13701 implements AlertCallBackInterface {
                                C13701() {
                                }

                                public void neutralClick() {
                                    SelfSchedulePresenterImp.this.view.getActivity().finish();
                                }
                            }

                            C13711() {
                            }

                            public void positiveClick() {
                                SelfSchedulePresenterImp.this.view.getActivity().finish();
                            }

                            public void neutralClick() {
                            }

                            public void negativeClick() {
                                DialogPopUps.confirmationPopUp(SelfSchedulePresenterImp.this.view.getActivity(), SelfSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.confirmed), SelfSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.Your_appointment_was_successfully_saved_to_your_default_calendar), new C13701());
                                try {
                                    Log.e("date_mili", "" + CommonUtils.getDateConverted(dateStart));
                                    Log.e("date2_mili", "" + CommonUtils.getDateConverted(dateEnd));
                                    CommonUtils.addEvent(SelfSchedulePresenterImp.this.view.getActivity(), CommonUtils.getDateConverted(dateStart), CommonUtils.getDateConverted(dateEnd), 0, SelfSchedulePresenterImp.this.view.getResources().getString(C1020R.string.you_have_a_scheduled_video_visit_with_your_doctor_in_15_minutes_please_log_into_tik_tok_doc_app));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        public void onCompleted() {
                        }

                        public void onError(Throwable e) {
                            if (SelfSchedulePresenterImp.this.view != null) {
                                SelfSchedulePresenterImp.this.view.hideDialog();
                                SelfSchedulePresenterImp.this.view.showError(SelfSchedulePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                            }
                        }

                        public void onNext(DocAcceptReject docAcceptReject) {
                            if (SelfSchedulePresenterImp.this.view != null) {
                                SelfSchedulePresenterImp.this.view.hideDialog();
                                if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                                    DialogPopUps.showSChedulePatientAlert(SelfSchedulePresenterImp.this.view.getActivity(), SelfSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.appointment_scheduled), SelfSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.you_have_successfully_scheduled_a_video_visit_appointment_with) + " " + SelfSchedulePresenterImp.this.selected_doc + " " + SelfSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.your_doctor_will_be_notified_of_your_scheduled_appointment) + " " + SelfSchedulePresenterImp.this.selected_doc + " " + SelfSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.will_video_call_you), "", "", "", true, new C13711());
                                } else {
                                    SelfSchedulePresenterImp.this.view.showError(docAcceptReject.getError());
                                }
                            }
                        }
                    });
                } else if (this.view != null) {
                    this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
                }
            }
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
    }

    public void gettingDocStringList() {
        ArrayList arrayList = new ArrayList();
        ArrayList<MyDoctorsObject> dataDoctorMainListTemp = SharedPreff.getDoctorList();
        for (int s = 0; s < dataDoctorMainListTemp.size(); s++) {
            if (((MyDoctorsObject) dataDoctorMainListTemp.get(s)).getStatus() == 1) {
                this.dataDoctorMainList.add(dataDoctorMainListTemp.get(s));
            }
        }
        for (int j = 0; j < this.dataDoctorMainList.size(); j++) {
            this.doctorStringList.add(((MyDoctorsObject) this.dataDoctorMainList.get(j)).getName());
        }
    }

    public void fetchDocDetail(String id) {
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.mSubscription = this.netWorkingService.getAPI().getDoctorAvailability(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13738());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    private void getYearList() {
        for (int d = 0; d < this.mainList.size(); d++) {
            this.yearList.add(((AppointmentListObject) this.mainList.get(d)).getYear());
        }
        Set<String> hs = new LinkedHashSet();
        hs.addAll(this.yearList);
        this.yearList.clear();
        this.yearList.addAll(hs);
    }

    private void filterTimeList(int val) {
        int j;
        int to_index;
        int k;
        int i;
        this.tempTimeList.clear();
        this.tempTime12HrList.clear();
        this.fromTimeList.clear();
        this.fromTime12HrList.clear();
        this.finalFromTimeList.clear();
        this.finalfromTime12HrList.clear();
        this.fromTimeArrayList.clear();
        this.fromTimeArray12HrList.clear();
        if (this.scheduleList.size() > 0) {
            for (j = 0; j < this.scheduleList.size(); j++) {
                if (((SchedulePojo) this.scheduleList.get(j)).getMeetingSlot().contains(this.selected_duration)) {
                    String to_ = ((SchedulePojo) this.scheduleList.get(j)).getTime().getTo();
                    String from_ = ((SchedulePojo) this.scheduleList.get(j)).getTime().getFrom();
                    Log.e("to_", "" + to_);
                    Log.e("from_", "" + from_);
                    to_index = this.fromTimeListOriginal.indexOf(to_);
                    int from_index = this.fromTimeListOriginal.indexOf(from_);
                    ArrayList<String> tempfinalFromTimeList = new ArrayList();
                    ArrayList<String> tempfinalfromTime12HrList = new ArrayList();
                    for (k = from_index; k < to_index; k++) {
                        this.fromTimeList.add(this.fromTimeListOriginal.get(k));
                        this.fromTime12HrList.add(this.fromTime12HrListOriginal.get(k));
                        tempfinalFromTimeList.add(this.fromTimeListOriginal.get(k));
                        tempfinalfromTime12HrList.add(this.fromTime12HrListOriginal.get(k));
                    }
                    this.fromTimeArrayList.add(tempfinalFromTimeList);
                    this.fromTimeArray12HrList.add(tempfinalfromTime12HrList);
                }
            }
        }
        Log.e("fromTimeList" + val, "" + this.fromTimeList);
        Log.e("fromTime12HrList", "" + this.fromTime12HrList);
        this.bookedFromTimeList.clear();
        this.bookedfromTime12HrList.clear();
        if (this.BookingList.size() > 0) {
            for (j = 0; j < this.BookingList.size(); j++) {
                if (((BookingPatientObject) this.BookingList.get(j)).getStatus() == 1) {
                    to_ = ((BookingPatientObject) this.BookingList.get(j)).getTo();
                    from_ = ((BookingPatientObject) this.BookingList.get(j)).getFrom();
                    Log.e("to_", "" + to_);
                    Log.e("from_", "" + from_);
                    to_index = this.fromTimeListOriginal.indexOf(to_);
                    for (k = this.fromTimeListOriginal.indexOf(from_); k < to_index; k++) {
                        this.bookedFromTimeList.add(this.fromTimeListOriginal.get(k));
                        this.bookedfromTime12HrList.add(this.fromTime12HrListOriginal.get(k));
                    }
                }
            }
        }
        Log.e("abc" + val, "" + this.finalFromTimeList);
        Log.e("abc", "" + this.finalfromTime12HrList);
        this.finalFromTimeList.addAll(this.fromTimeList);
        this.finalfromTime12HrList.addAll(this.fromTime12HrList);
        Log.e("abc" + val, "" + this.finalFromTimeList);
        Log.e("abc", "" + this.finalfromTime12HrList);
        this.finalFromTimeList.removeAll(this.bookedFromTimeList);
        this.finalfromTime12HrList.removeAll(this.bookedfromTime12HrList);
        for (i = 0; i < this.fromTimeArrayList.size(); i++) {
            ((ArrayList) this.fromTimeArrayList.get(i)).removeAll(this.bookedFromTimeList);
            ((ArrayList) this.fromTimeArray12HrList.get(i)).removeAll(this.bookedfromTime12HrList);
        }
        Log.e("abc" + val, "" + this.finalFromTimeList);
        Log.e("abc", "" + this.finalfromTime12HrList);
        if (val != 1) {
            for (int a = 0; a < this.finalFromTimeList.size(); a++) {
                int firstValue = this.fromTimeListOriginal.indexOf(this.finalFromTimeList.get(a));
                String init_val;
                if (val == 2) {
                    init_val = (String) this.fromTimeListOriginal.get(firstValue);
                    String final_val = (String) this.fromTimeListOriginal.get(firstValue + 1);
                    i = 0;
                    while (i < this.fromTimeArrayList.size()) {
                        if (((ArrayList) this.fromTimeArrayList.get(i)).contains(init_val) && ((ArrayList) this.fromTimeArrayList.get(i)).contains(final_val)) {
                            this.tempTimeList.add(this.finalFromTimeList.get(a));
                            this.tempTime12HrList.add(this.finalfromTime12HrList.get(a));
                        }
                        i++;
                    }
                } else if (val == 4) {
                    final_index = firstValue + 3;
                    init_val = (String) this.fromTimeListOriginal.get(firstValue);
                    j = 0;
                    while (j < this.fromTimeArrayList.size()) {
                        i = firstValue;
                        while (i <= final_index && ((ArrayList) this.fromTimeArrayList.get(j)).contains(this.fromTimeListOriginal.get(i)) && ((ArrayList) this.fromTimeArrayList.get(j)).contains(init_val)) {
                            if (i == final_index) {
                                this.tempTimeList.add(this.finalFromTimeList.get(a));
                                this.tempTime12HrList.add(this.finalfromTime12HrList.get(a));
                            }
                            i++;
                        }
                        j++;
                    }
                } else if (val == 6) {
                    final_index = firstValue + 5;
                    init_val = (String) this.fromTimeListOriginal.get(firstValue);
                    j = 0;
                    while (j < this.fromTimeArrayList.size()) {
                        i = firstValue;
                        while (i <= final_index && ((ArrayList) this.fromTimeArrayList.get(j)).contains(this.fromTimeListOriginal.get(i)) && ((ArrayList) this.fromTimeArrayList.get(j)).contains(init_val)) {
                            if (i == final_index) {
                                this.tempTimeList.add(this.finalFromTimeList.get(a));
                                this.tempTime12HrList.add(this.finalfromTime12HrList.get(a));
                            }
                            i++;
                        }
                        j++;
                    }
                }
            }
            return;
        }
        this.tempTimeList.addAll(this.finalFromTimeList);
        this.tempTime12HrList.addAll(this.finalfromTime12HrList);
    }
}
