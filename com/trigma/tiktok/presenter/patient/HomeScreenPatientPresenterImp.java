package com.trigma.tiktok.presenter.patient;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.activity.patient.HomeScreenPatient;
import com.trigma.tiktok.model.DecativateResponse;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DrAppointmentDate;
import com.trigma.tiktok.model.DrAppointmentDateData;
import com.trigma.tiktok.model.DrId;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.model.MyDoctorsResponse;
import com.trigma.tiktok.model.NotificationCountCall;
import com.trigma.tiktok.model.NotificationCountPush;
import com.trigma.tiktok.model.NotificationCountResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeScreenPatientPresenterImp implements HomeScreenPatientPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionAppData;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private int repeatCounter = 90000;
    private Timer timer = null;
    private Timer timerAppDates = null;
    private HomeScreenPatient view;

    class C13311 extends TimerTask {
        C13311() {
        }

        public void run() {
            Log.e("startingTimer", "startingTimer");
            HomeScreenPatientPresenterImp.this.timerApiCall();
        }
    }

    class C13322 extends TimerTask {
        C13322() {
        }

        public void run() {
            Log.e("startingTimer", "checkingAppointmentDates");
            HomeScreenPatientPresenterImp.this.callDrAppointmentDate();
        }
    }

    class C13354 implements Observer<DecativateResponse> {

        class C13341 implements AlertCallBackInterface {
            C13341() {
            }

            public void neutralClick() {
                SharedPreff.clearDocPreff();
                Intent intent = new Intent(HomeScreenPatientPresenterImp.this.view, DashBoard.class);
                intent.setFlags(268468224);
                HomeScreenPatientPresenterImp.this.view.startActivity(intent);
            }
        }

        C13354() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (HomeScreenPatientPresenterImp.this.view == null) {
            }
        }

        public void onNext(DecativateResponse decativateResponse) {
            if (HomeScreenPatientPresenterImp.this.view != null && decativateResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && decativateResponse.getUserStatus() == 0) {
                DialogPopUps.alertPopUp(HomeScreenPatientPresenterImp.this.view, HomeScreenPatientPresenterImp.this.view.getResources().getString(C1020R.string.your_account_has_been_deleted_or_deactivated_please_contact_our_support_for_more_information), HomeScreenPatientPresenterImp.this.view.getResources().getString(C1020R.string.ok), new C13341());
            }
        }
    }

    class C13365 implements Observer<DrAppointmentDate> {
        C13365() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(DrAppointmentDate drAppointmentDate) {
            if (drAppointmentDate.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                Log.e("drAppointmentDate", new Gson().toJson((Object) drAppointmentDate));
                if (HomeScreenPatientPresenterImp.this.view == null) {
                }
            }
        }
    }

    class C13376 implements Observer<NotificationCountResponse> {
        C13376() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(NotificationCountResponse notificationCountResponse) {
            Log.e("notificationResponse", "" + new Gson().toJson((Object) notificationCountResponse));
            if (notificationCountResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && HomeScreenPatientPresenterImp.this.view != null) {
                if (notificationCountResponse.getCount() > 0) {
                    HomeScreenPatientPresenterImp.this.view.showNotificationCount("" + notificationCountResponse.getCount());
                } else {
                    HomeScreenPatientPresenterImp.this.view.hideNotificationCount();
                }
                if (notificationCountResponse.getRequestcount() > 0) {
                    HomeScreenPatientPresenterImp.this.view.showMyPatientCount("" + notificationCountResponse.getRequestcount());
                } else {
                    HomeScreenPatientPresenterImp.this.view.hideMyPatientCount();
                }
                NotificationCountPush notificationCountPush;
                if (notificationCountResponse.getMessagedotshow() == 1) {
                    notificationCountPush = new NotificationCountPush();
                    notificationCountPush.setShowCount(true);
                    EventBus.getDefault().post(notificationCountPush);
                    HomeScreenPatientPresenterImp.this.view.showMessageCount("");
                } else {
                    notificationCountPush = new NotificationCountPush();
                    notificationCountPush.setShowCount(false);
                    EventBus.getDefault().post(notificationCountPush);
                    HomeScreenPatientPresenterImp.this.view.hideMessageCount();
                }
                if (notificationCountResponse.getScheduleCountAppointment() > 0) {
                    HomeScreenPatientPresenterImp.this.view.showAppointmentCount("" + notificationCountResponse.getScheduleCountAppointment());
                } else {
                    HomeScreenPatientPresenterImp.this.view.hideAppointmentCount();
                }
            }
        }
    }

    class C13387 implements Observer<DocAcceptReject> {
        C13387() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (HomeScreenPatientPresenterImp.this.view != null) {
                HomeScreenPatientPresenterImp.this.view.hideDialog();
                HomeScreenPatientPresenterImp.this.view.showError(HomeScreenPatientPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject deDocAcceptReject) {
            if (HomeScreenPatientPresenterImp.this.view != null) {
                HomeScreenPatientPresenterImp.this.view.hideDialog();
                if (deDocAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    SharedPreff.clearDocPreff();
                    HomeScreenPatientPresenterImp.this.view.loginOutSuccessfull();
                    return;
                }
                HomeScreenPatientPresenterImp.this.view.showError(deDocAcceptReject.getError());
            }
        }
    }

    public HomeScreenPatientPresenterImp(HomeScreenPatient view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        settingProfilePic();
    }

    private void settingProfilePic() {
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            Log.e("loginDocResponce", new Gson().toJson(this.loginDocResponce));
            String address = "";
            if (!TextUtils.isEmpty(this.loginDocResponce.getData().getAddress())) {
                address = this.loginDocResponce.getData().getAddress();
            }
            if (!TextUtils.isEmpty(this.loginDocResponce.getData().getProfilePic())) {
                this.view.settingProfilePic(this.loginDocResponce.getData().getProfilePic());
                if (TextUtils.isEmpty(this.loginDocResponce.getData().getFirstName()) || TextUtils.isEmpty(this.loginDocResponce.getData().getLastName())) {
                    this.view.settingNameAndField(this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName(), address);
                } else {
                    this.view.settingNameAndField(CommonUtils.capWordCase(this.loginDocResponce.getData().getFirstName()) + " " + CommonUtils.capWordCase(this.loginDocResponce.getData().getLastName()), address);
                }
            }
        }
    }

    public void startNotificationCountApi() {
        settingProfilePic();
        if (this.loginDocResponce != null) {
            if (this.timer == null) {
                this.timer = new Timer();
            } else {
                this.timer.cancel();
                this.timer = null;
                this.timer = new Timer();
            }
            this.timer.schedule(new C13311(), 0, (long) this.repeatCounter);
            return;
        }
        Log.e("loginDocResponce", "null");
    }

    public void stopingTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void stopingAppDate() {
        if (this.timerAppDates != null) {
            this.timerAppDates.cancel();
            this.timerAppDates = null;
        }
    }

    public void callContactUs() {
    }

    public void checkingAppointmentDates() {
        if (this.loginDocResponce != null) {
            if (this.timerAppDates == null) {
                this.timerAppDates = new Timer();
            } else {
                this.timerAppDates.cancel();
                this.timerAppDates = null;
                this.timerAppDates = new Timer();
            }
            this.timerAppDates.schedule(new C13322(), 0, (long) this.repeatCounter);
            return;
        }
        Log.e("loginDocResponce", "null");
    }

    public void checkingForDoctors(final boolean forSelfSchedule, final boolean goToScshedule) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getPatientDoctorList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<MyDoctorsResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (HomeScreenPatientPresenterImp.this.view != null) {
                        HomeScreenPatientPresenterImp.this.view.hideDialog();
                        HomeScreenPatientPresenterImp.this.view.showError(HomeScreenPatientPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(MyDoctorsResponse myDoctorsResponse) {
                    if (HomeScreenPatientPresenterImp.this.view != null) {
                        HomeScreenPatientPresenterImp.this.view.hideDialog();
                        if (forSelfSchedule) {
                            if (!myDoctorsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                                HomeScreenPatientPresenterImp.this.view.showError(myDoctorsResponse.getError());
                            } else if (myDoctorsResponse.getData().size() > 0) {
                                for (int z = 0; z < myDoctorsResponse.getData().size(); z++) {
                                    if (((MyDoctorsObject) myDoctorsResponse.getData().get(z)).getStatus() == 1) {
                                        HomeScreenPatientPresenterImp.this.view.isDoctorActive(true, goToScshedule);
                                        return;
                                    }
                                }
                                HomeScreenPatientPresenterImp.this.view.isDoctorActive(false, goToScshedule);
                            } else {
                                HomeScreenPatientPresenterImp.this.view.isDoctorActive(false, goToScshedule);
                            }
                        } else if (myDoctorsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                            HomeScreenPatientPresenterImp.this.view.setDoctors(myDoctorsResponse.getData());
                        } else {
                            HomeScreenPatientPresenterImp.this.view.showError(myDoctorsResponse.getError());
                        }
                    }
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void checkDeactivate() {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.mSubscriptionAppData = this.netWorkingService.getAPI().userDecativate(this.loginDocResponce.getData().get_id(), SharedPreff.getDeviceToken(), Constants.DEVICE_TYPE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13354());
            }
        } else if (this.view == null) {
        }
    }

    private void callDrAppointmentDate() {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            new DrId().setDr_Id(this.loginDocResponce.getData().get_id());
            this.mSubscriptionAppData = this.netWorkingService.getAPI().getPatientAppointmentDate(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13365());
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscriptionCounter != null) {
            this.mSubscriptionCounter.unsubscribe();
            stopingTimer();
        }
        if (this.mSubscriptionAppData != null) {
            this.mSubscriptionAppData.unsubscribe();
            stopingAppDate();
        }
        this.view = null;
    }

    public void timerApiCall() {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            NotificationCountCall notificationCountCall = new NotificationCountCall();
            notificationCountCall.setType("" + this.loginDocResponce.getData().getUserType());
            notificationCountCall.setUserId(this.loginDocResponce.getData().get_id());
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getNotificationCount(notificationCountCall).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13376());
        }
    }

    public void settingLocalNotification(DrAppointmentDate tingLocalNotification) {
        if (tingLocalNotification.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
            ArrayList arrayList = new ArrayList();
            ArrayList<DrAppointmentDateData> data = tingLocalNotification.getRecords();
            if (data != null && data.size() > 0) {
                for (int k = 0; k < data.size(); k++) {
                    String date1 = ((DrAppointmentDateData) data.get(k)).getSchedule() + " " + ((DrAppointmentDateData) data.get(k)).getFrom() + ":00";
                    String date2 = ((DrAppointmentDateData) data.get(k)).getSchedule() + " " + ((DrAppointmentDateData) data.get(k)).getTo() + ":00";
                    try {
                        Log.e("date_mili", "" + CommonUtils.getDateConverted(date1));
                        Log.e("date2_mili", "" + CommonUtils.getDateConverted(date2));
                        CommonUtils.addEvent(this.view, CommonUtils.getDateConverted(date1), CommonUtils.getDateConverted(date2), 0, this.view.getResources().getString(C1020R.string.you_have_a_scheduled_video_visit_with_your_doctor_in_15_minutes_please_log_into_tik_tok_doc_app));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void logout() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().logoutApi(this.loginDocResponce.getData().get_id(), SharedPreff.getDeviceToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13387());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }
}
