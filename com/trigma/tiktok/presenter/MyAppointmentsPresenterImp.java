package com.trigma.tiktok.presenter;

import android.util.Log;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.MyAppointments;
import com.trigma.tiktok.model.BookingOuterAppointment;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DrAppointmentResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.Past;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.Upcoming;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyAppointmentsPresenterImp implements MyAppointmentsPresenter {
    private HashMap<String, ArrayList<Upcoming>> listDataChild = new HashMap();
    private HashMap<String, ArrayList<Past>> listDataChildPast = new HashMap();
    private ArrayList<String> listDataHeader = new ArrayList();
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private MyAppointments view;

    class C12551 implements Observer<DrAppointmentResponse> {
        C12551() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MyAppointmentsPresenterImp.this.view != null) {
                MyAppointmentsPresenterImp.this.view.hideDialog();
                MyAppointmentsPresenterImp.this.view.showError(MyAppointmentsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrAppointmentResponse contactUsResponse) {
            if (MyAppointmentsPresenterImp.this.view != null) {
                MyAppointmentsPresenterImp.this.view.hideDialog();
                if (SharedPreff.getStaffLoginResponse() != null) {
                    MyAppointmentsPresenterImp.this.staffAppointmentRead();
                } else {
                    MyAppointmentsPresenterImp.this.appointmentRead();
                }
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MyAppointmentsPresenterImp.this.listDataHeader = MyAppointmentsPresenterImp.this.getHeaderList(contactUsResponse);
                    Set<String> hs = new LinkedHashSet();
                    hs.addAll(MyAppointmentsPresenterImp.this.listDataHeader);
                    MyAppointmentsPresenterImp.this.listDataHeader.clear();
                    MyAppointmentsPresenterImp.this.listDataHeader.addAll(hs);
                    try {
                        if (MyAppointmentsPresenterImp.this.listDataHeader.size() > 0) {
                            String headerString = "";
                            ArrayList<Upcoming> upcoming = new ArrayList();
                            upcoming = ((BookingOuterAppointment) contactUsResponse.getData().get(0)).getBooking().getUpcoming();
                            for (int x = 0; x < MyAppointmentsPresenterImp.this.listDataHeader.size(); x++) {
                                headerString = (String) MyAppointmentsPresenterImp.this.listDataHeader.get(x);
                                ArrayList<Upcoming> upcomingInner = new ArrayList();
                                for (int s = 0; s < upcoming.size(); s++) {
                                    Upcoming upcoming1 = (Upcoming) upcoming.get(s);
                                    if (upcoming1.getScheduleDate().equalsIgnoreCase(headerString)) {
                                        upcomingInner.add(upcoming1);
                                    }
                                }
                                MyAppointmentsPresenterImp.this.listDataChild.put(headerString, upcomingInner);
                            }
                            MyAppointmentsPresenterImp.this.view.settingUpCommingAdapter(MyAppointmentsPresenterImp.this.listDataHeader, MyAppointmentsPresenterImp.this.listDataChild);
                            Log.e("listDataHeader", "" + MyAppointmentsPresenterImp.this.listDataHeader);
                            Log.e("listDataChild", "" + new Gson().toJson(MyAppointmentsPresenterImp.this.listDataChild));
                            return;
                        }
                        MyAppointmentsPresenterImp.this.view.upCommingEmpty();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    MyAppointmentsPresenterImp.this.view.upCommingEmpty();
                } else {
                    MyAppointmentsPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    class C12562 implements Observer<DrAppointmentResponse> {
        C12562() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MyAppointmentsPresenterImp.this.view != null) {
                MyAppointmentsPresenterImp.this.view.hideDialog();
                MyAppointmentsPresenterImp.this.view.showError(MyAppointmentsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrAppointmentResponse contactUsResponse) {
            if (MyAppointmentsPresenterImp.this.view != null) {
                MyAppointmentsPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MyAppointmentsPresenterImp.this.listDataHeader = MyAppointmentsPresenterImp.this.getHeaderListPast(contactUsResponse);
                    Set<String> hs = new LinkedHashSet();
                    hs.addAll(MyAppointmentsPresenterImp.this.listDataHeader);
                    MyAppointmentsPresenterImp.this.listDataHeader.clear();
                    MyAppointmentsPresenterImp.this.listDataHeader.addAll(hs);
                    if (MyAppointmentsPresenterImp.this.listDataHeader.size() > 0) {
                        String headerString = "";
                        ArrayList<Past> upcoming = new ArrayList();
                        upcoming = ((BookingOuterAppointment) contactUsResponse.getData().get(0)).getBooking().getPast();
                        for (int x = 0; x < MyAppointmentsPresenterImp.this.listDataHeader.size(); x++) {
                            headerString = (String) MyAppointmentsPresenterImp.this.listDataHeader.get(x);
                            ArrayList<Past> upcomingInner = new ArrayList();
                            for (int s = 0; s < upcoming.size(); s++) {
                                Past past = (Past) upcoming.get(s);
                                if (past.getScheduleDate().equalsIgnoreCase(headerString)) {
                                    upcomingInner.add(past);
                                }
                            }
                            MyAppointmentsPresenterImp.this.listDataChildPast.put(headerString, upcomingInner);
                        }
                        MyAppointmentsPresenterImp.this.view.settingPastAdapter(MyAppointmentsPresenterImp.this.listDataHeader, MyAppointmentsPresenterImp.this.listDataChildPast);
                        Log.e("listDataHeader", "" + MyAppointmentsPresenterImp.this.listDataHeader);
                        Log.e("listDataChild", "" + new Gson().toJson(MyAppointmentsPresenterImp.this.listDataChild));
                        return;
                    }
                    MyAppointmentsPresenterImp.this.view.pastEmpty();
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    MyAppointmentsPresenterImp.this.view.pastEmpty();
                } else {
                    MyAppointmentsPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    class C12573 implements Observer<DocAcceptReject> {
        C12573() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(DocAcceptReject docAcceptReject) {
            if (!docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
            }
        }
    }

    class C12584 implements Observer<DocAcceptReject> {
        C12584() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(DocAcceptReject docAcceptReject) {
            if (!docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
            }
        }
    }

    class C12595 implements Callable<ArrayList<String>> {
        C12595() {
        }

        public ArrayList<String> call() {
            return MyAppointmentsPresenterImp.this.listDataHeader;
        }
    }

    public MyAppointmentsPresenterImp(MyAppointments view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
    }

    public void fetchUpcoming() {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
                this.view.showProgressDialog();
                this.listDataHeader.clear();
                this.listDataChild.clear();
                this.mSubscriptionCounter = this.netWorkingService.getAPI().drAppointmentsList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12551());
            } else if (this.view != null) {
                this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
            }
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void fetchPast() {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
                this.view.showProgressDialog();
                this.listDataHeader.clear();
                this.listDataChildPast.clear();
                this.mSubscriptionCounter = this.netWorkingService.getAPI().drAppointmentsList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12562());
            }
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void gotoDetailScreen(PatientPendingData patientPendingData) {
        this.view.goToPatientDetail(patientPendingData);
    }

    public void gotoMakeVideoCall(Upcoming data) {
        this.view.goToMakeVideoCall(data);
    }

    public void appointmentRead() {
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.mSubscriptionCounter = this.netWorkingService.getAPI().patientAppointmentRead(this.loginDocResponce.getData().get_id(), "" + this.loginDocResponce.getData().getUserType()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12573());
        }
    }

    public void staffAppointmentRead() {
        if (SharedPreff.getStaffLoginResponse() != null && CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.mSubscriptionCounter = this.netWorkingService.getAPI().staffAppRead(SharedPreff.getStaffLoginResponse().getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12584());
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscriptionCounter != null) {
            this.mSubscriptionCounter.unsubscribe();
        }
        this.view = null;
    }

    private Observable<ArrayList<String>> createDataHeader() {
        return Observable.fromCallable(new C12595()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<HashMap<String, ArrayList<Past>>> createChildListPast(final DrAppointmentResponse contactUsResponse) {
        return Observable.fromCallable(new Callable<HashMap<String, ArrayList<Past>>>() {
            public HashMap<String, ArrayList<Past>> call() {
                return MyAppointmentsPresenterImp.this.getChildListPast(((BookingOuterAppointment) contactUsResponse.getData().get(0)).getBooking().getPast());
            }
        });
    }

    public ArrayList<String> getHeaderList(DrAppointmentResponse contactUsResponse) {
        ArrayList<Upcoming> upcomingList = new ArrayList();
        ArrayList<String> _listDataHeader = new ArrayList();
        upcomingList = ((BookingOuterAppointment) contactUsResponse.getData().get(0)).getBooking().getUpcoming();
        if (upcomingList.size() > 0) {
            for (int j = 0; j < upcomingList.size(); j++) {
                _listDataHeader.add(((Upcoming) upcomingList.get(j)).getScheduleDate());
            }
        }
        return _listDataHeader;
    }

    public ArrayList<String> getHeaderListPast(DrAppointmentResponse contactUsResponse) {
        ArrayList<Past> upcomingList = new ArrayList();
        ArrayList<String> _listDataHeader = new ArrayList();
        upcomingList = ((BookingOuterAppointment) contactUsResponse.getData().get(0)).getBooking().getPast();
        if (upcomingList.size() > 0) {
            for (int j = 0; j < upcomingList.size(); j++) {
                _listDataHeader.add(((Past) upcomingList.get(j)).getScheduleDate().trim());
            }
        }
        return _listDataHeader;
    }

    public HashMap<String, ArrayList<Past>> getChildListPast(ArrayList<Past> upcomings) {
        HashMap<String, ArrayList<Past>> _listDataChild = new HashMap();
        if (this.listDataHeader.size() > 0) {
            String headerString = "";
            ArrayList arrayList = new ArrayList();
            ArrayList<Past> upcomingR = upcomings;
            for (int x = 0; x < this.listDataHeader.size(); x++) {
                headerString = (String) this.listDataHeader.get(x);
                ArrayList<Past> upcomingInner = new ArrayList();
                for (int s = 0; s < upcomingR.size(); s++) {
                    Past past = (Past) upcomingR.get(s);
                    if (past.getScheduleDate().equalsIgnoreCase(headerString)) {
                        upcomingInner.add(past);
                    }
                }
                _listDataChild.put(headerString, upcomingInner);
            }
            this.view.settingPastAdapter(this.listDataHeader, this.listDataChildPast);
            Log.e("listDataHeader", "" + this.listDataHeader);
            Log.e("listDataChild", "" + new Gson().toJson(this.listDataChild));
            this.view.showToastError(GraphResponse.SUCCESS_KEY);
        }
        return _listDataChild;
    }
}
