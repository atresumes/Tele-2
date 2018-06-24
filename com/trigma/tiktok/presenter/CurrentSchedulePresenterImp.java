package com.trigma.tiktok.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.CurrentSchedule;
import com.trigma.tiktok.model.BookingPojo;
import com.trigma.tiktok.model.CurrentScheduleResponce;
import com.trigma.tiktok.model.DrCancelScheduleResponce;
import com.trigma.tiktok.model.GetSListPojo;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.OuterSchedulePojo;
import com.trigma.tiktok.model.SchedulePojo;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface2;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CurrentSchedulePresenterImp implements CurrentSchedulePresenter {
    String CreateId = "";
    String CreateName = "";
    String CreateUserType = "";
    private String DrScheduleId;
    private String Dr_id;
    private String ScheduleId;
    public ArrayList<BookingPojo> bookingList = new ArrayList();
    public ArrayList<GetSListPojo> dataGetSListPojo = new ArrayList();
    private ArrayList<String> dateList = new ArrayList();
    public GetSListPojo getSListPojo;
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private ArrayList<String> monthList = new ArrayList();
    private NetWorkingService netWorkingService;
    public OuterSchedulePojo schedule;
    public ArrayList<SchedulePojo> scheduleList = new ArrayList();
    public ArrayList<SchedulePojo> scheduleListTemp = new ArrayList();
    String schedule_date;
    private int selectedPos = -1;
    private String strDate;
    private String strMonth;
    private String strYear;
    private CurrentSchedule view;
    private ArrayList<String> yearList = new ArrayList();

    class C12211 implements AlertCallBackWithButtonsInterface2 {
        C12211() {
        }

        public void positiveClick(int selected) {
            CurrentSchedulePresenterImp.this.view.setYear((String) CurrentSchedulePresenterImp.this.yearList.get(selected));
            CurrentSchedulePresenterImp.this.strYear = (String) CurrentSchedulePresenterImp.this.yearList.get(selected);
            CurrentSchedulePresenterImp.this.view.setDate(CurrentSchedulePresenterImp.this.view.getResources().getString(C1020R.string.select_date));
            CurrentSchedulePresenterImp.this.view.setMonth(CurrentSchedulePresenterImp.this.view.getResources().getString(C1020R.string.month_c));
            CurrentSchedulePresenterImp.this.monthList.clear();
            CurrentSchedulePresenterImp.this.dateList.clear();
            CurrentSchedulePresenterImp.this.view.showRecyclerView(false);
            for (int k = 0; k < CurrentSchedulePresenterImp.this.dataGetSListPojo.size(); k++) {
                if (((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(k)).getYear().equalsIgnoreCase(CurrentSchedulePresenterImp.this.strYear)) {
                    CurrentSchedulePresenterImp.this.monthList.add(((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(k)).getMonth());
                }
            }
            Set<String> hs = new LinkedHashSet();
            hs.addAll(CurrentSchedulePresenterImp.this.monthList);
            CurrentSchedulePresenterImp.this.monthList.clear();
            CurrentSchedulePresenterImp.this.monthList.addAll(hs);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C12222 implements AlertCallBackWithButtonsInterface2 {
        C12222() {
        }

        public void positiveClick(int selected) {
            CurrentSchedulePresenterImp.this.view.setMonth((String) CurrentSchedulePresenterImp.this.monthList.get(selected));
            CurrentSchedulePresenterImp.this.strMonth = (String) CurrentSchedulePresenterImp.this.monthList.get(selected);
            CurrentSchedulePresenterImp.this.view.setDate(CurrentSchedulePresenterImp.this.view.getResources().getString(C1020R.string.select_date));
            CurrentSchedulePresenterImp.this.strDate = null;
            CurrentSchedulePresenterImp.this.view.showRecyclerView(false);
            CurrentSchedulePresenterImp.this.dateList.clear();
            int k = 0;
            while (k < CurrentSchedulePresenterImp.this.dataGetSListPojo.size()) {
                if (((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(k)).getYear().equalsIgnoreCase(CurrentSchedulePresenterImp.this.strYear) && ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(k)).getMonth().equalsIgnoreCase(CurrentSchedulePresenterImp.this.strMonth)) {
                    CurrentSchedulePresenterImp.this.dateList.add(((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(k)).getDay());
                }
                k++;
            }
            Set<String> hs = new LinkedHashSet();
            hs.addAll(CurrentSchedulePresenterImp.this.dateList);
            CurrentSchedulePresenterImp.this.dateList.clear();
            CurrentSchedulePresenterImp.this.dateList.addAll(hs);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C12233 implements AlertCallBackWithButtonsInterface2 {
        C12233() {
        }

        public void positiveClick(int selected) {
            Log.e("index", "" + selected);
            Log.e("strDate", "" + ((String) CurrentSchedulePresenterImp.this.dateList.get(selected)));
            CurrentSchedulePresenterImp.this.view.setDate((String) CurrentSchedulePresenterImp.this.dateList.get(selected));
            CurrentSchedulePresenterImp.this.strDate = (String) CurrentSchedulePresenterImp.this.dateList.get(selected);
            int a = 0;
            while (a < CurrentSchedulePresenterImp.this.dataGetSListPojo.size()) {
                if (((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().getDate().getMonth().equalsIgnoreCase(CurrentSchedulePresenterImp.this.strMonth) && ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().getDate().getYear().equalsIgnoreCase(CurrentSchedulePresenterImp.this.strYear) && ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().getDate().getDate().equalsIgnoreCase(CurrentSchedulePresenterImp.this.strDate)) {
                    CurrentSchedulePresenterImp.this.scheduleList = ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().getSchedule();
                    CurrentSchedulePresenterImp.this.bookingList = ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().getBooking();
                    Log.e("dataGetSListPojo" + a, "" + new Gson().toJson(((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule()));
                    CurrentSchedulePresenterImp.this.Dr_id = ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().getDr_id();
                    CurrentSchedulePresenterImp.this.schedule_date = ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().getScheduleDate();
                    CurrentSchedulePresenterImp.this.DrScheduleId = ((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a)).getSchedule().get_id();
                    CurrentSchedulePresenterImp.this.selectedPos = a;
                    CurrentSchedulePresenterImp.this.getSListPojo = (GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(a);
                    CurrentSchedulePresenterImp.this.schedule = CurrentSchedulePresenterImp.this.getSListPojo.getSchedule();
                    a = CurrentSchedulePresenterImp.this.dataGetSListPojo.size() + 1;
                }
                a++;
            }
            CurrentSchedulePresenterImp.this.scheduleListTemp.clear();
            for (int z = 0; z < CurrentSchedulePresenterImp.this.scheduleList.size(); z++) {
                if (((SchedulePojo) CurrentSchedulePresenterImp.this.scheduleList.get(z)).getStatus() == 1) {
                    CurrentSchedulePresenterImp.this.scheduleListTemp.add(CurrentSchedulePresenterImp.this.scheduleList.get(z));
                }
            }
            CurrentSchedulePresenterImp.this.scheduleList = CurrentSchedulePresenterImp.this.scheduleListTemp;
            Log.e("scheduleList::", "" + CurrentSchedulePresenterImp.this.scheduleList);
            Log.e("bookingList::", "" + CurrentSchedulePresenterImp.this.bookingList);
            Log.e("dataGetSListPojo::", "" + new Gson().toJson(((GetSListPojo) CurrentSchedulePresenterImp.this.dataGetSListPojo.get(0)).getSchedule()));
            CurrentSchedulePresenterImp.this.view.settingAdapter(CurrentSchedulePresenterImp.this.scheduleList, CurrentSchedulePresenterImp.this.bookingList, CurrentSchedulePresenterImp.this.Dr_id, CurrentSchedulePresenterImp.this.DrScheduleId, CurrentSchedulePresenterImp.this.schedule_date);
            CurrentSchedulePresenterImp.this.view.showRecyclerView(true);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C12244 implements Observer<CurrentScheduleResponce> {
        C12244() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (CurrentSchedulePresenterImp.this.view != null) {
                CurrentSchedulePresenterImp.this.view.hideDialog();
            }
        }

        public void onNext(CurrentScheduleResponce currentScheduleResponce) {
            if (currentScheduleResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                if (CurrentSchedulePresenterImp.this.view != null) {
                    CurrentSchedulePresenterImp.this.strYear = null;
                    CurrentSchedulePresenterImp.this.view.hideDialog();
                    CurrentSchedulePresenterImp.this.yearList.clear();
                    CurrentSchedulePresenterImp.this.monthList.clear();
                    CurrentSchedulePresenterImp.this.dateList.clear();
                    CurrentSchedulePresenterImp.this.dataGetSListPojo = currentScheduleResponce.getData();
                    Log.e("dataGetSListPojo", "" + new Gson().toJson(CurrentSchedulePresenterImp.this.dataGetSListPojo));
                    if (CurrentSchedulePresenterImp.this.dataGetSListPojo.size() > 0) {
                        Iterator it = CurrentSchedulePresenterImp.this.dataGetSListPojo.iterator();
                        while (it.hasNext()) {
                            CurrentSchedulePresenterImp.this.yearList.add(((GetSListPojo) it.next()).getYear());
                        }
                        Log.e("yearList_before", "" + CurrentSchedulePresenterImp.this.yearList);
                        if (CurrentSchedulePresenterImp.this.yearList.contains(null)) {
                            CurrentSchedulePresenterImp.this.yearList.remove(null);
                        }
                        Set<String> hs = new LinkedHashSet();
                        hs.addAll(CurrentSchedulePresenterImp.this.yearList);
                        CurrentSchedulePresenterImp.this.yearList.clear();
                        CurrentSchedulePresenterImp.this.yearList.addAll(hs);
                        Log.e("yearList_after", "" + CurrentSchedulePresenterImp.this.yearList);
                        return;
                    }
                    CurrentSchedulePresenterImp.this.view.showError(CurrentSchedulePresenterImp.this.view.getResources().getString(C1020R.string.currently_you_have_no_schedule));
                }
            } else if (CurrentSchedulePresenterImp.this.loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_400)) {
                CurrentSchedulePresenterImp.this.view.showError(CurrentSchedulePresenterImp.this.view.getResources().getString(C1020R.string.currently_you_have_no_schedule));
            } else {
                CurrentSchedulePresenterImp.this.view.showError(currentScheduleResponce.getError());
            }
        }
    }

    public CurrentSchedulePresenterImp(CurrentSchedule view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.CreateId = SharedPreff.getStaffLoginResponse().getData().get_id();
            this.CreateName = SharedPreff.getStaffLoginResponse().getData().getFirstName() + " " + SharedPreff.getStaffLoginResponse().getData().getLastName();
            this.CreateUserType = "" + SharedPreff.getStaffLoginResponse().getData().getUserType();
            return;
        }
        this.CreateId = SharedPreff.getLoginResponce().getData().get_id();
        this.CreateName = SharedPreff.getLoginResponce().getData().getFirstName() + " " + SharedPreff.getLoginResponce().getData().getLastName();
        this.CreateUserType = "" + SharedPreff.getLoginResponce().getData().getUserType();
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }

    public void openYearPicker() {
        if (this.yearList.size() > 0) {
            Log.e("yearList", "" + this.yearList);
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.yearList), new C12211());
            return;
        }
        makeApiCall();
    }

    public void openMonthPicker() {
        if (TextUtils.isEmpty(this.strYear)) {
            this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_year));
        } else if (this.monthList.size() > 0) {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.monthList), new C12222());
        }
    }

    public void openDatePicker() {
        if (TextUtils.isEmpty(this.strMonth)) {
            this.view.showToastError(this.view.getActivity().getResources().getString(C1020R.string.please_select_month));
        } else if (this.dateList.size() > 0) {
            DialogPopUps.showTimePicker(this.view.getActivity(), CommonUtils.getArrayFromList(this.dateList), new C12233());
        }
    }

    public void makeApiCall() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getDrScheduleListing(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12244());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void deleteDataFromMain(final int pos, final SchedulePojo schedulePojo, String Dr_id, String DrScheduleId, String ScheduleId, String From, String To, String schedule_date) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            String doc_name = this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName();
            Log.e("Dr_id", "" + Dr_id);
            Log.e("DrScheduleId", "" + DrScheduleId);
            Log.e("doc_name", "" + doc_name);
            Log.e("From", "" + From);
            Log.e("To", "" + To);
            Log.e("ScheduleId", "" + ScheduleId);
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().cancelingTimeSchedule(Dr_id, DrScheduleId, doc_name, From, To, ScheduleId, this.CreateId, this.CreateName, this.CreateUserType, schedule_date).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DrCancelScheduleResponce>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (CurrentSchedulePresenterImp.this.view != null) {
                        CurrentSchedulePresenterImp.this.view.hideDialog();
                        CurrentSchedulePresenterImp.this.view.showError(CurrentSchedulePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DrCancelScheduleResponce drCancelScheduleResponce) {
                    if (CurrentSchedulePresenterImp.this.view != null) {
                        CurrentSchedulePresenterImp.this.view.hideDialog();
                        if (drCancelScheduleResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                            CurrentSchedulePresenterImp.this.scheduleList.set(pos, schedulePojo);
                            CurrentSchedulePresenterImp.this.schedule.setSchedule(CurrentSchedulePresenterImp.this.scheduleList);
                            CurrentSchedulePresenterImp.this.getSListPojo.setSchedule(CurrentSchedulePresenterImp.this.schedule);
                            CurrentSchedulePresenterImp.this.dataGetSListPojo.set(CurrentSchedulePresenterImp.this.selectedPos, CurrentSchedulePresenterImp.this.getSListPojo);
                            CurrentSchedulePresenterImp.this.view.deletedSuccessFully(pos);
                            return;
                        }
                        CurrentSchedulePresenterImp.this.view.showError(drCancelScheduleResponce.getError());
                    }
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }
}
