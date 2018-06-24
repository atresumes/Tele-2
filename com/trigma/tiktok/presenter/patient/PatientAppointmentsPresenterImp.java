package com.trigma.tiktok.presenter.patient;

import android.util.Log;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.PatientMyAppointments;
import com.trigma.tiktok.model.BookingOuterAppointment;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DrAppointmentResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.model.MyDoctorsResponse;
import com.trigma.tiktok.model.Past;
import com.trigma.tiktok.model.PatientAppointmentResponse;
import com.trigma.tiktok.model.PatientBookingArray;
import com.trigma.tiktok.model.PatientModelUpcomingOuter;
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

public class PatientAppointmentsPresenterImp implements PatientAppointmentsPresenter {
    private HashMap<String, ArrayList<PatientModelUpcomingOuter>> listDataChild = new HashMap();
    private HashMap<String, ArrayList<PatientModelUpcomingOuter>> listDataChildPast = new HashMap();
    private ArrayList<String> listDataHeader = new ArrayList();
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private PatientMyAppointments view;

    class C13501 implements Observer<PatientAppointmentResponse> {
        C13501() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (PatientAppointmentsPresenterImp.this.view != null) {
                PatientAppointmentsPresenterImp.this.view.hideDialog();
                PatientAppointmentsPresenterImp.this.view.showError(PatientAppointmentsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(PatientAppointmentResponse contactUsResponse) {
            if (PatientAppointmentsPresenterImp.this.view != null) {
                PatientAppointmentsPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    PatientAppointmentsPresenterImp.this.appointmentRead();
                    PatientAppointmentsPresenterImp.this.listDataHeader = PatientAppointmentsPresenterImp.this.getHeaderList(contactUsResponse);
                    Set<String> hs = new LinkedHashSet();
                    hs.addAll(PatientAppointmentsPresenterImp.this.listDataHeader);
                    PatientAppointmentsPresenterImp.this.listDataHeader.clear();
                    PatientAppointmentsPresenterImp.this.listDataHeader.addAll(hs);
                    try {
                        if (PatientAppointmentsPresenterImp.this.listDataHeader.size() > 0) {
                            String headerString = "";
                            ArrayList<PatientModelUpcomingOuter> upcoming = new ArrayList();
                            upcoming = ((PatientBookingArray) contactUsResponse.getData().get(0)).getBooking().getUpcoming();
                            for (int x = 0; x < PatientAppointmentsPresenterImp.this.listDataHeader.size(); x++) {
                                headerString = (String) PatientAppointmentsPresenterImp.this.listDataHeader.get(x);
                                ArrayList<PatientModelUpcomingOuter> upcomingInner = new ArrayList();
                                for (int s = 0; s < upcoming.size(); s++) {
                                    PatientModelUpcomingOuter upcoming1 = (PatientModelUpcomingOuter) upcoming.get(s);
                                    if (upcoming1.getScheduleDate().equalsIgnoreCase(headerString)) {
                                        upcomingInner.add(upcoming1);
                                    }
                                }
                                PatientAppointmentsPresenterImp.this.listDataChild.put(headerString, upcomingInner);
                            }
                            PatientAppointmentsPresenterImp.this.view.settingUpCommingAdapter(PatientAppointmentsPresenterImp.this.listDataHeader, PatientAppointmentsPresenterImp.this.listDataChild);
                            Log.e("listDataHeader", "" + PatientAppointmentsPresenterImp.this.listDataHeader);
                            Log.e("listDataChild", "" + new Gson().toJson(PatientAppointmentsPresenterImp.this.listDataChild));
                            return;
                        }
                        PatientAppointmentsPresenterImp.this.view.upCommingEmpty();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    PatientAppointmentsPresenterImp.this.view.upCommingEmpty();
                } else {
                    PatientAppointmentsPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    class C13512 implements Observer<PatientAppointmentResponse> {
        C13512() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (PatientAppointmentsPresenterImp.this.view != null) {
                PatientAppointmentsPresenterImp.this.view.hideDialog();
                PatientAppointmentsPresenterImp.this.view.showError(PatientAppointmentsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(PatientAppointmentResponse contactUsResponse) {
            if (PatientAppointmentsPresenterImp.this.view != null) {
                PatientAppointmentsPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    PatientAppointmentsPresenterImp.this.listDataHeader = PatientAppointmentsPresenterImp.this.getHeaderListPast(contactUsResponse);
                    Set<String> hs = new LinkedHashSet();
                    hs.addAll(PatientAppointmentsPresenterImp.this.listDataHeader);
                    PatientAppointmentsPresenterImp.this.listDataHeader.clear();
                    PatientAppointmentsPresenterImp.this.listDataHeader.addAll(hs);
                    if (PatientAppointmentsPresenterImp.this.listDataHeader.size() > 0) {
                        String headerString = "";
                        ArrayList<PatientModelUpcomingOuter> upcoming = new ArrayList();
                        upcoming = ((PatientBookingArray) contactUsResponse.getData().get(0)).getBooking().getPast();
                        for (int x = 0; x < PatientAppointmentsPresenterImp.this.listDataHeader.size(); x++) {
                            headerString = (String) PatientAppointmentsPresenterImp.this.listDataHeader.get(x);
                            ArrayList<PatientModelUpcomingOuter> upcomingInner = new ArrayList();
                            for (int s = 0; s < upcoming.size(); s++) {
                                PatientModelUpcomingOuter past = (PatientModelUpcomingOuter) upcoming.get(s);
                                if (past.getScheduleDate().equalsIgnoreCase(headerString)) {
                                    upcomingInner.add(past);
                                }
                            }
                            PatientAppointmentsPresenterImp.this.listDataChildPast.put(headerString, upcomingInner);
                        }
                        PatientAppointmentsPresenterImp.this.view.settingPastAdapter(PatientAppointmentsPresenterImp.this.listDataHeader, PatientAppointmentsPresenterImp.this.listDataChildPast);
                        Log.e("listDataHeader", "" + PatientAppointmentsPresenterImp.this.listDataHeader);
                        Log.e("listDataChild", "" + new Gson().toJson(PatientAppointmentsPresenterImp.this.listDataChild));
                        return;
                    }
                    PatientAppointmentsPresenterImp.this.view.pastEmpty();
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    PatientAppointmentsPresenterImp.this.view.pastEmpty();
                } else {
                    PatientAppointmentsPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    class C13523 implements Observer<DocAcceptReject> {
        C13523() {
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

    class C13534 implements Observer<MyDoctorsResponse> {
        C13534() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (PatientAppointmentsPresenterImp.this.view != null) {
                PatientAppointmentsPresenterImp.this.view.hideDialog();
                PatientAppointmentsPresenterImp.this.view.showError(PatientAppointmentsPresenterImp.this.view.getActivity().getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(MyDoctorsResponse myDoctorsResponse) {
            if (PatientAppointmentsPresenterImp.this.view != null) {
                PatientAppointmentsPresenterImp.this.view.hideDialog();
                if (!myDoctorsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    PatientAppointmentsPresenterImp.this.view.showError(myDoctorsResponse.getError());
                } else if (myDoctorsResponse.getData().size() > 0) {
                    for (int z = 0; z < myDoctorsResponse.getData().size(); z++) {
                        if (((MyDoctorsObject) myDoctorsResponse.getData().get(z)).getStatus() == 1) {
                            PatientAppointmentsPresenterImp.this.view.isDoctorActive(true);
                            return;
                        }
                    }
                    PatientAppointmentsPresenterImp.this.view.isDoctorActive(false);
                } else {
                    PatientAppointmentsPresenterImp.this.view.isDoctorActive(false);
                }
            }
        }
    }

    class C13545 implements Callable<ArrayList<String>> {
        C13545() {
        }

        public ArrayList<String> call() {
            return PatientAppointmentsPresenterImp.this.listDataHeader;
        }
    }

    public PatientAppointmentsPresenterImp(PatientMyAppointments view, NetWorkingService netWorkingService) {
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
                this.mSubscriptionCounter = this.netWorkingService.getAPI().getPatientAppointmentsList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13501());
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
                this.mSubscriptionCounter = this.netWorkingService.getAPI().getPatientAppointmentsList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13512());
            }
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void gotoDetailScreen(PatientPendingData patientPendingData) {
    }

    public void gotoMakeVideoCall(Upcoming data) {
    }

    public void appointmentRead() {
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.mSubscriptionCounter = this.netWorkingService.getAPI().patientAppointmentRead(this.loginDocResponce.getData().get_id(), AppEventsConstants.EVENT_PARAM_VALUE_NO).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13523());
        }
    }

    public void goToWaitingRoom(PatientModelUpcomingOuter upcoming) {
        this.view.goToWaitingRoom(upcoming);
    }

    public void checkingForDoctors() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getPatientDoctorList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13534());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
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
        return Observable.fromCallable(new C13545()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<HashMap<String, ArrayList<Past>>> createChildListPast(final DrAppointmentResponse contactUsResponse) {
        return Observable.fromCallable(new Callable<HashMap<String, ArrayList<Past>>>() {
            public HashMap<String, ArrayList<Past>> call() {
                return PatientAppointmentsPresenterImp.this.getChildListPast(((BookingOuterAppointment) contactUsResponse.getData().get(0)).getBooking().getPast());
            }
        });
    }

    public ArrayList<String> getHeaderList(PatientAppointmentResponse contactUsResponse) {
        ArrayList<PatientModelUpcomingOuter> upcomingList = new ArrayList();
        ArrayList<String> _listDataHeader = new ArrayList();
        upcomingList = ((PatientBookingArray) contactUsResponse.getData().get(0)).getBooking().getUpcoming();
        if (upcomingList.size() > 0) {
            for (int j = 0; j < upcomingList.size(); j++) {
                _listDataHeader.add(((PatientModelUpcomingOuter) upcomingList.get(j)).getScheduleDate());
            }
        }
        return _listDataHeader;
    }

    public ArrayList<String> getHeaderListPast(PatientAppointmentResponse contactUsResponse) {
        ArrayList<PatientModelUpcomingOuter> upcomingList = new ArrayList();
        ArrayList<String> _listDataHeader = new ArrayList();
        upcomingList = ((PatientBookingArray) contactUsResponse.getData().get(0)).getBooking().getPast();
        if (upcomingList.size() > 0) {
            for (int j = 0; j < upcomingList.size(); j++) {
                _listDataHeader.add(((PatientModelUpcomingOuter) upcomingList.get(j)).getScheduleDate().trim());
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
