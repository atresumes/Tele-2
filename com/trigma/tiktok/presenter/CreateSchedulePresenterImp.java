package com.trigma.tiktok.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.ShareConstants;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.CreateSchedule;
import com.trigma.tiktok.model.DrDatePojo;
import com.trigma.tiktok.model.DrDateResponse;
import com.trigma.tiktok.model.DrScheduleSetResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface2;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import fr.quentinklein.slt.TrackerSettings;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateSchedulePresenterImp implements CreateSchedulePresenter, OnDateSetListener, OnTimeSetListener {
    private String dateVale;
    private ArrayList<String> fromTime12HrList;
    private ArrayList<String> fromTime12HrListOriginal = new ArrayList();
    private String[] fromTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30"};
    private String[] fromTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM"};
    private ArrayList<String> fromTimeList;
    private ArrayList<String> fromTimeListOriginal = new ArrayList();
    private String fromValue;
    int hours = 0;
    private ArrayList<Integer> indexList = new ArrayList();
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private Subscription mSubscriptionCounter;
    private int mainIndex = 0;
    private int max_selected_slot = 0;
    private String meetingSlots;
    int min = 0;
    private String monthValue;
    private NetWorkingService netWorkingService;
    private ArrayList<DrDatePojo> selectedDateSlots = new ArrayList();
    private String selected_from_time;
    private ArrayList<Integer> slots_list = new ArrayList();
    private ArrayList<String> slots_list_to_send = new ArrayList();
    private String[] toTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"};
    private String[] toTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM"};
    private ArrayList<String> toTimeArr12HrList;
    private ArrayList<String> toTimeArr12HrListMain = new ArrayList();
    private ArrayList<String> toTimeArrList;
    private ArrayList<String> toTimeArrListMain = new ArrayList();
    private String toValue;
    private CreateSchedule view;
    private String yearValue;

    class C12161 implements AlertCallBackWithButtonsInterface2 {
        C12161() {
        }

        public void positiveClick(int selected) {
            CreateSchedulePresenterImp.this.view.setToTime(CreateSchedulePresenterImp.this.view.getActivity().getResources().getString(C1020R.string.to));
            CreateSchedulePresenterImp.this.toValue = null;
            CreateSchedulePresenterImp.this.view.setFromTime((String) CreateSchedulePresenterImp.this.fromTime12HrList.get(selected));
            CreateSchedulePresenterImp.this.fromValue = (String) CreateSchedulePresenterImp.this.fromTimeList.get(selected);
            CreateSchedulePresenterImp.this.selected_from_time = (String) CreateSchedulePresenterImp.this.fromTime12HrList.get(selected);
            CreateSchedulePresenterImp.this.mainIndex = CreateSchedulePresenterImp.this.toTimeArr12HrList.indexOf(CreateSchedulePresenterImp.this.fromTime12HrList.get(selected));
            CreateSchedulePresenterImp.this.findIndexList(CreateSchedulePresenterImp.this.selectedDateSlots);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C12172 implements AlertCallBackWithButtonsInterface2 {
        C12172() {
        }

        public void positiveClick(int selected) {
            CreateSchedulePresenterImp.this.view.setToTime((String) CreateSchedulePresenterImp.this.toTimeArr12HrListMain.get(selected));
            CreateSchedulePresenterImp.this.toValue = (String) CreateSchedulePresenterImp.this.toTimeArrListMain.get(selected);
            CreateSchedulePresenterImp.this.getTimeDifference(CreateSchedulePresenterImp.this.selected_from_time, (String) CreateSchedulePresenterImp.this.toTimeArr12HrListMain.get(selected));
            CreateSchedulePresenterImp.this.checkSelectedSlotsInterval();
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C12204 implements Observer<DrScheduleSetResponse> {

        class C12191 implements AlertCallBackInterface {
            C12191() {
            }

            public void neutralClick() {
            }
        }

        C12204() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (CreateSchedulePresenterImp.this.view != null) {
                CreateSchedulePresenterImp.this.view.hideDialog();
                CreateSchedulePresenterImp.this.view.showError(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrScheduleSetResponse drScheduleSetResponse) {
            if (CreateSchedulePresenterImp.this.view != null) {
                CreateSchedulePresenterImp.this.view.hideDialog();
                if (drScheduleSetResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    CreateSchedulePresenterImp.this.view.setDateSelected(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.select_date));
                    CreateSchedulePresenterImp.this.view.setFromTime(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.from));
                    CreateSchedulePresenterImp.this.view.setToTime(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.to));
                    CreateSchedulePresenterImp.this.view.clearCheckBoxes();
                    CreateSchedulePresenterImp.this.fromValue = null;
                    CreateSchedulePresenterImp.this.toValue = null;
                    CreateSchedulePresenterImp.this.dateVale = null;
                    CreateSchedulePresenterImp.this.monthValue = null;
                    CreateSchedulePresenterImp.this.yearValue = null;
                    CreateSchedulePresenterImp.this.meetingSlots = null;
                    CreateSchedulePresenterImp.this.fromTime12HrList = new ArrayList(Arrays.asList(CreateSchedulePresenterImp.this.fromTimeArr12Hr));
                    CreateSchedulePresenterImp.this.fromTimeList = new ArrayList(Arrays.asList(CreateSchedulePresenterImp.this.fromTimeArr));
                    CreateSchedulePresenterImp.this.selectedDateSlots.clear();
                    CreateSchedulePresenterImp.this.filterList(CreateSchedulePresenterImp.this.selectedDateSlots);
                    CreateSchedulePresenterImp.this.slots_list.clear();
                    CreateSchedulePresenterImp.this.slots_list_to_send.clear();
                    DialogPopUps.confirmationPopUp(CreateSchedulePresenterImp.this.view.getActivity(), "", "", new C12191());
                    return;
                }
                CreateSchedulePresenterImp.this.view.showError(drScheduleSetResponse.getError());
            }
        }
    }

    public CreateSchedulePresenterImp(CreateSchedule view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        this.fromTime12HrList = new ArrayList(Arrays.asList(this.fromTimeArr12Hr));
        this.fromTimeList = new ArrayList(Arrays.asList(this.fromTimeArr));
        this.toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
        this.toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
        this.fromTime12HrListOriginal = new ArrayList(Arrays.asList(this.fromTimeArr12Hr));
        this.fromTimeListOriginal = new ArrayList(Arrays.asList(this.fromTimeArr));
    }

    public void openDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(1), now.get(2), now.get(5));
        dpd.setMinDate(now);
        dpd.setVersion(Version.VERSION_2);
        dpd.show(this.view.getFragmentManager(), "Datepickerdialog");
    }

    public void openFromTimePicker() {
        if (TextUtils.isEmpty(this.dateVale) || TextUtils.isEmpty(this.yearValue) || TextUtils.isEmpty(this.monthValue)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_date_first));
        } else {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.fromTime12HrList), new C12161());
        }
    }

    public void findIndexList(ArrayList<DrDatePojo> toFilter) {
        int k;
        if (toFilter.size() > 0) {
            this.toTimeArr12HrListMain.clear();
            this.toTimeArrListMain.clear();
            this.indexList.clear();
            this.toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
            this.toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
            for (int i = 0; i < toFilter.size(); i++) {
                int to_index = this.toTimeArrList.indexOf(((DrDatePojo) toFilter.get(i)).getTime().getFrom());
                if (to_index > this.mainIndex) {
                    this.indexList.add(Integer.valueOf(to_index));
                }
            }
            if (this.indexList.size() > 0) {
                int value = ((Integer) this.indexList.get(this.indexList.indexOf(Collections.min(this.indexList)))).intValue();
                for (k = this.mainIndex + 1; k <= value; k++) {
                    this.toTimeArr12HrListMain.add(this.toTimeArr12HrList.get(k));
                    this.toTimeArrListMain.add(this.toTimeArrList.get(k));
                }
                return;
            }
            for (k = this.mainIndex + 1; k < this.toTimeArr12HrList.size(); k++) {
                this.toTimeArr12HrListMain.add(this.toTimeArr12HrList.get(k));
                this.toTimeArrListMain.add(this.toTimeArrList.get(k));
            }
            return;
        }
        this.toTimeArr12HrListMain.clear();
        this.toTimeArrListMain.clear();
        for (k = this.mainIndex + 1; k < this.toTimeArr12HrList.size(); k++) {
            this.toTimeArr12HrListMain.add(this.toTimeArr12HrList.get(k));
            this.toTimeArrListMain.add(this.toTimeArrList.get(k));
        }
    }

    public void openToTimePicker() {
        if (TextUtils.isEmpty(this.fromValue)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_from_time));
        } else {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.toTimeArr12HrListMain), new C12172());
        }
    }

    public void saveButtonAction() {
    }

    public void setTimeSlots(int value) {
    }

    public void checkValidate() {
        if (TextUtils.isEmpty(this.toValue) && TextUtils.isEmpty(this.fromValue) && TextUtils.isEmpty(this.dateVale) && TextUtils.isEmpty(this.yearValue) && TextUtils.isEmpty(this.monthValue) && TextUtils.isEmpty(this.meetingSlots)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_date_first));
        } else if (TextUtils.isEmpty(this.fromValue)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_from_time));
        } else if (TextUtils.isEmpty(this.toValue)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_to_time));
        } else if (TextUtils.isEmpty(this.meetingSlots)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_time_slots));
        } else {
            String DateSelecetd = this.dateVale;
            Log.e("dateVale", "" + this.dateVale);
            Log.e("monthValue", "" + this.monthValue);
            Log.e("yearValue", "" + this.yearValue);
            Log.e("fromValue", "" + this.fromValue);
            Log.e("toValue", "" + this.toValue);
            Log.e("meetingSlots", "" + this.meetingSlots);
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
                if (Integer.parseInt(DateSelecetd) < 10 && !DateSelecetd.contains(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    DateSelecetd = AppEventsConstants.EVENT_PARAM_VALUE_NO + DateSelecetd;
                }
                if (new Date().after(simpleDateFormat.parse(DateSelecetd + "/" + CommonUtils.getMonthInteger(this.monthValue) + "/" + this.yearValue + " " + this.fromValue + ":00"))) {
                    this.view.showError(this.view.getActivity().getResources().getString(C1020R.string.please_select_a_time_greater_than_the_current_time));
                    return;
                }
                Log.e("done", "done");
                String CreateId = "";
                String CreateName = "";
                String CreateUserType = "";
                if (SharedPreff.getStaffLoginResponse() != null) {
                    CreateId = SharedPreff.getStaffLoginResponse().getData().get_id();
                    CreateName = SharedPreff.getStaffLoginResponse().getData().getFirstName() + " " + SharedPreff.getStaffLoginResponse().getData().getLastName();
                    CreateUserType = "" + SharedPreff.getStaffLoginResponse().getData().getUserType();
                } else {
                    CreateId = SharedPreff.getLoginResponce().getData().get_id();
                    CreateName = SharedPreff.getLoginResponce().getData().getFirstName() + " " + SharedPreff.getLoginResponce().getData().getLastName();
                    CreateUserType = "" + SharedPreff.getLoginResponce().getData().getUserType();
                }
                updatingScheduleaList(CreateId, CreateName, CreateUserType);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("ParseException", "");
            }
        }
    }

    public void settingMaximumSelectdSlot(int value) {
        if (!this.slots_list.contains(new Integer(value))) {
            this.slots_list.add(Integer.valueOf(value));
            this.slots_list_to_send.add("" + value + " Min");
        }
        this.meetingSlots = CommonUtils.getCommaSepratedStrings(this.slots_list_to_send);
        checkSelectedSlotsInterval();
    }

    public void settingMinimumSelectdSlot(int value) {
        if (this.slots_list.contains(new Integer(value))) {
            this.slots_list.remove(new Integer(value));
            this.slots_list_to_send.remove("" + value + " Min");
        }
        this.meetingSlots = CommonUtils.getCommaSepratedStrings(this.slots_list_to_send);
        checkSelectedSlotsInterval();
    }

    private void checkSelectedSlotsInterval() {
        if (!TextUtils.isEmpty(this.toValue)) {
            this.min = Math.abs(this.min);
            this.hours = Math.abs(this.hours);
            Log.e("time", "" + this.hours + ":" + this.min);
            if (this.slots_list.size() > 0) {
                int max_value = ((Integer) this.slots_list.get(this.slots_list.indexOf(Collections.max(this.slots_list)))).intValue();
                if (max_value == 15) {
                    if (this.hours <= 0 && this.min < 15) {
                        this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.gap_error) + " " + max_value + " " + this.view.getActivity().getResources().getString(C1020R.string.minutes));
                        this.view.setToTime(this.view.getActivity().getResources().getString(C1020R.string.to));
                        this.toValue = null;
                    }
                } else if (max_value == 30) {
                    if (this.hours <= 0 && this.min < 30) {
                        this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.gap_error) + " " + max_value + " " + this.view.getActivity().getResources().getString(C1020R.string.minutes));
                        this.view.setToTime(this.view.getActivity().getResources().getString(C1020R.string.to));
                        this.toValue = null;
                    }
                } else if (max_value == 60) {
                    if (this.hours < 1 && this.min < 60) {
                        this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.gap_error) + " " + max_value + " " + this.view.getActivity().getResources().getString(C1020R.string.minutes));
                        this.view.setToTime(this.view.getActivity().getResources().getString(C1020R.string.to));
                        this.toValue = null;
                    }
                } else if (max_value != 90) {
                } else {
                    if ((this.hours < 1 || this.min < 30) && this.hours < 2) {
                        this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.gap_error) + " " + max_value + " " + this.view.getActivity().getResources().getString(C1020R.string.minutes));
                        this.view.setToTime(this.view.getActivity().getResources().getString(C1020R.string.to));
                        this.toValue = null;
                    }
                }
            }
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (this.loginDocResponce != null) {
            callGetDrSelectedDates(this.loginDocResponce.getData().get_id(), dayOfMonth + "-" + CommonUtils.getMonth("" + (monthOfYear + 1)) + "-" + year, year, monthOfYear, dayOfMonth);
            return;
        }
        this.view.setDateSelected(year, CommonUtils.getMonth("" + (monthOfYear + 1)), dayOfMonth);
        this.dateVale = "" + dayOfMonth;
        this.monthValue = CommonUtils.getMonth("" + (monthOfYear + 1));
        this.yearValue = "" + year;
        this.selectedDateSlots.clear();
        filterList(this.selectedDateSlots);
    }

    public void settingCurrentDate(int year, int monthOfYear, int dayOfMonth) {
        if (this.loginDocResponce != null) {
            callGetDrSelectedDates(this.loginDocResponce.getData().get_id(), dayOfMonth + "-" + CommonUtils.getMonth("" + (monthOfYear + 1)) + "-" + year, year, monthOfYear, dayOfMonth);
            return;
        }
        this.view.setDateSelected(year, CommonUtils.getMonth("" + (monthOfYear + 1)), dayOfMonth);
        this.dateVale = "" + dayOfMonth;
        this.monthValue = CommonUtils.getMonth("" + (monthOfYear + 1));
        this.yearValue = "" + year;
        this.selectedDateSlots.clear();
        filterList(this.selectedDateSlots);
    }

    private void callGetDrSelectedDates(String id, String date, final int year, final int monthOfYear, final int dayOfMonth) {
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            Log.e("dateSent", "" + date);
            Log.e(ShareConstants.WEB_DIALOG_PARAM_ID, "" + id);
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.mSubscription = this.netWorkingService.getAPI().getDrScheduleList(date, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DrDateResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (CreateSchedulePresenterImp.this.view != null) {
                        CreateSchedulePresenterImp.this.view.hideDialog();
                        CreateSchedulePresenterImp.this.view.showError(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                        CreateSchedulePresenterImp.this.view.setDateSelected(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.select_date));
                        CreateSchedulePresenterImp.this.view.setFromTime(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.from));
                        CreateSchedulePresenterImp.this.view.setToTime(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.to));
                        CreateSchedulePresenterImp.this.fromValue = null;
                        CreateSchedulePresenterImp.this.toValue = null;
                        CreateSchedulePresenterImp.this.fromTime12HrList = new ArrayList(Arrays.asList(CreateSchedulePresenterImp.this.fromTimeArr12Hr));
                        CreateSchedulePresenterImp.this.fromTimeList = new ArrayList(Arrays.asList(CreateSchedulePresenterImp.this.fromTimeArr));
                        CreateSchedulePresenterImp.this.dateVale = null;
                        CreateSchedulePresenterImp.this.monthValue = null;
                        CreateSchedulePresenterImp.this.yearValue = null;
                        CreateSchedulePresenterImp.this.selectedDateSlots.clear();
                        CreateSchedulePresenterImp.this.filterList(CreateSchedulePresenterImp.this.selectedDateSlots);
                    }
                }

                public void onNext(DrDateResponse drDateResponse) {
                    if (CreateSchedulePresenterImp.this.view != null) {
                        CreateSchedulePresenterImp.this.view.hideDialog();
                        CreateSchedulePresenterImp.this.view.setDateSelected(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.select_date));
                        CreateSchedulePresenterImp.this.view.setFromTime(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.from));
                        CreateSchedulePresenterImp.this.view.setToTime(CreateSchedulePresenterImp.this.view.getResources().getString(C1020R.string.to));
                        CreateSchedulePresenterImp.this.fromValue = null;
                        CreateSchedulePresenterImp.this.toValue = null;
                        CreateSchedulePresenterImp.this.fromTime12HrList = new ArrayList(Arrays.asList(CreateSchedulePresenterImp.this.fromTimeArr12Hr));
                        CreateSchedulePresenterImp.this.fromTimeList = new ArrayList(Arrays.asList(CreateSchedulePresenterImp.this.fromTimeArr));
                        if (drDateResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                            CreateSchedulePresenterImp.this.view.setDateSelected(year, CommonUtils.getMonth("" + (monthOfYear + 1)), dayOfMonth);
                            CreateSchedulePresenterImp.this.dateVale = "" + dayOfMonth;
                            CreateSchedulePresenterImp.this.monthValue = CommonUtils.getMonth("" + (monthOfYear + 1));
                            CreateSchedulePresenterImp.this.yearValue = "" + year;
                            CreateSchedulePresenterImp.this.selectedDateSlots = drDateResponse.getData();
                            CreateSchedulePresenterImp.this.filterList(CreateSchedulePresenterImp.this.selectedDateSlots);
                            return;
                        }
                        CreateSchedulePresenterImp.this.view.setDateSelected(year, CommonUtils.getMonth("" + (monthOfYear + 1)), dayOfMonth);
                        CreateSchedulePresenterImp.this.dateVale = "" + dayOfMonth;
                        CreateSchedulePresenterImp.this.monthValue = CommonUtils.getMonth("" + (monthOfYear + 1));
                        CreateSchedulePresenterImp.this.yearValue = "" + year;
                        CreateSchedulePresenterImp.this.selectedDateSlots.clear();
                        CreateSchedulePresenterImp.this.filterList(CreateSchedulePresenterImp.this.selectedDateSlots);
                    }
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    }

    public void filteringFromList(int pos, int mainSize, ArrayList<DrDatePojo> toFilter) {
        if (pos != mainSize - 1) {
            String to_ = ((DrDatePojo) toFilter.get(pos)).getTime().getTo();
            String from_ = ((DrDatePojo) toFilter.get(pos)).getTime().getFrom();
            Log.e("to_", "" + to_);
            Log.e("from_", "" + from_);
            this.fromTime12HrList = removingFromINdex(this.fromTimeList.indexOf(to_), this.fromTimeList.indexOf(from_));
            filteringFromList(pos + 1, mainSize, toFilter);
        }
    }

    public ArrayList<String> filterList(ArrayList<DrDatePojo> toFilter) {
        ArrayList<String> bookedFromTimeList = new ArrayList();
        ArrayList<String> bookedfromTime12HrList = new ArrayList();
        if (toFilter.size() > 0) {
            for (int i = 0; i < toFilter.size(); i++) {
                String to_ = ((DrDatePojo) toFilter.get(i)).getTime().getTo();
                String from_ = ((DrDatePojo) toFilter.get(i)).getTime().getFrom();
                Log.e("to_", "" + to_);
                Log.e("from_", "" + from_);
                int to_index = this.fromTimeListOriginal.indexOf(to_);
                for (int k = this.fromTimeListOriginal.indexOf(from_); k < to_index; k++) {
                    bookedFromTimeList.add(this.fromTimeListOriginal.get(k));
                    bookedfromTime12HrList.add(this.fromTime12HrListOriginal.get(k));
                }
            }
            this.fromTimeList.addAll(this.fromTimeListOriginal);
            this.fromTime12HrList.addAll(this.fromTime12HrListOriginal);
            this.fromTimeList.removeAll(bookedFromTimeList);
            this.fromTime12HrList.removeAll(bookedfromTime12HrList);
        } else {
            this.fromTime12HrList = new ArrayList(Arrays.asList(this.fromTimeArr12Hr));
            this.fromTimeList = new ArrayList(Arrays.asList(this.fromTimeArr));
            this.toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
            this.toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
        }
        return this.fromTime12HrList;
    }

    private ArrayList<String> removingFromINdex(int to_index, int from_index) {
        if (to_index == -1) {
            int counter = from_index;
            while (this.fromTimeList.size() > counter) {
                this.fromTimeList.remove(from_index);
                this.fromTime12HrList.remove(from_index);
            }
        } else {
            for (int a = from_index; a < to_index; a++) {
                this.fromTimeList.remove(from_index);
                this.fromTime12HrList.remove(from_index);
            }
        }
        return this.fromTime12HrList;
    }

    public int getTimeDifference(String from, String to) {
        Log.e("from", "" + from);
        Log.e(ShareConstants.WEB_DIALOG_PARAM_TO, "" + to);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            long difference = simpleDateFormat.parse(to).getTime() - simpleDateFormat.parse(from).getTime();
            int days = (int) (difference / 86400000);
            this.hours = (int) ((difference - ((long) (86400000 * days))) / 3600000);
            this.min = ((int) ((difference - ((long) (86400000 * days))) - ((long) (3600000 * this.hours)))) / TrackerSettings.DEFAULT_TIMEOUT;
            this.hours = this.hours < 0 ? -this.hours : this.hours;
            Log.e("DIFF:hours", "" + this.hours);
            Log.e("DIFF:min", "" + this.min);
            Log.e("DIFF:hours", "" + this.hours);
            return this.min;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updatingScheduleaList(String CreateId, String CreateName, String CreateUserType) {
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.mSubscription = this.netWorkingService.getAPI().setDrScheduleList(this.loginDocResponce.getData().get_id(), this.yearValue, this.monthValue, this.dateVale, this.fromValue, this.toValue, this.meetingSlots, CreateId, CreateName, CreateUserType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12204());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }
}
