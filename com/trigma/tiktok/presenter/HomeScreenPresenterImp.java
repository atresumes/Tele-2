package com.trigma.tiktok.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.activity.HomeScreen;
import com.trigma.tiktok.model.DecativateResponse;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DrAppointmentDate;
import com.trigma.tiktok.model.DrAppointmentDateData;
import com.trigma.tiktok.model.DrId;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.NotificationCountCall;
import com.trigma.tiktok.model.NotificationCountPush;
import com.trigma.tiktok.model.NotificationCountResponse;
import com.trigma.tiktok.model.PrescriptionObject;
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

public class HomeScreenPresenterImp implements HomeScreenPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionAppData;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private int repeatCounter = 90000;
    private Timer timer = null;
    private Timer timerAppDates = null;
    private HomeScreen view;

    class C12341 extends TimerTask {
        C12341() {
        }

        public void run() {
            Log.e("startingTimer", "startingTimer");
            HomeScreenPresenterImp.this.timerApiCall();
            HomeScreenPresenterImp.this.checkDeactivate();
        }
    }

    class C12352 extends TimerTask {
        C12352() {
        }

        public void run() {
            Log.e("startingTimer", "checkingAppointmentDates");
            HomeScreenPresenterImp.this.callDrAppointmentDate();
        }
    }

    class C12363 implements Observer<DrAppointmentDate> {
        C12363() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(DrAppointmentDate drAppointmentDate) {
            if (drAppointmentDate.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                Log.e("drAppointmentDate", new Gson().toJson((Object) drAppointmentDate));
                if (HomeScreenPresenterImp.this.view != null) {
                    HomeScreenPresenterImp.this.settingLocalNotification(drAppointmentDate);
                }
            }
        }
    }

    class C12374 implements Observer<NotificationCountResponse> {
        C12374() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(NotificationCountResponse notificationCountResponse) {
            Log.e("notificationResponse", "" + new Gson().toJson((Object) notificationCountResponse));
            if (notificationCountResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && HomeScreenPresenterImp.this.view != null) {
                if (notificationCountResponse.getCount() > 0) {
                    HomeScreenPresenterImp.this.view.showNotificationCount("" + notificationCountResponse.getCount());
                } else {
                    HomeScreenPresenterImp.this.view.hideNotificationCount();
                }
                if (notificationCountResponse.getRequestcount() > 0) {
                    HomeScreenPresenterImp.this.view.showMyPatientCount("" + notificationCountResponse.getRequestcount());
                } else {
                    HomeScreenPresenterImp.this.view.hideMyPatientCount();
                }
                NotificationCountPush notificationCountPush;
                if (notificationCountResponse.getMessagedotshow() == 1) {
                    notificationCountPush = new NotificationCountPush();
                    notificationCountPush.setShowCount(true);
                    EventBus.getDefault().post(notificationCountPush);
                    HomeScreenPresenterImp.this.view.showMessageCount("");
                } else {
                    notificationCountPush = new NotificationCountPush();
                    notificationCountPush.setShowCount(false);
                    EventBus.getDefault().post(notificationCountPush);
                    HomeScreenPresenterImp.this.view.hideMessageCount();
                }
                if (notificationCountResponse.getScheduleCountAppointment() > 0) {
                    HomeScreenPresenterImp.this.view.showAppointmentCount("" + notificationCountResponse.getScheduleCountAppointment());
                } else {
                    HomeScreenPresenterImp.this.view.hideAppointmentCount();
                }
            }
        }
    }

    class C12385 implements Observer<DocAcceptReject> {
        C12385() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (HomeScreenPresenterImp.this.view != null) {
                HomeScreenPresenterImp.this.view.hideDialog();
                HomeScreenPresenterImp.this.view.showError(HomeScreenPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject deDocAcceptReject) {
            if (HomeScreenPresenterImp.this.view != null) {
                HomeScreenPresenterImp.this.view.hideDialog();
                if (deDocAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    SharedPreff.clearDocPreff();
                    HomeScreenPresenterImp.this.view.loginSuccessfull();
                    return;
                }
                HomeScreenPresenterImp.this.view.showError(deDocAcceptReject.getError());
            }
        }
    }

    class C12396 implements Observer<PrescriptionObject> {
        C12396() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (HomeScreenPresenterImp.this.view != null) {
                HomeScreenPresenterImp.this.view.hideDialog();
                HomeScreenPresenterImp.this.view.showError(HomeScreenPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(PrescriptionObject prescriptionObject) {
            if (HomeScreenPresenterImp.this.view != null) {
                HomeScreenPresenterImp.this.view.hideDialog();
                if (prescriptionObject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    HomeScreenPresenterImp.this.view.gotoPrescriptionScreen(prescriptionObject.getUrl(), prescriptionObject.getTransactionErrorsCount());
                } else {
                    HomeScreenPresenterImp.this.view.showError(prescriptionObject.getError());
                }
            }
        }
    }

    class C12417 implements Observer<DecativateResponse> {

        class C12401 implements AlertCallBackInterface {
            C12401() {
            }

            public void neutralClick() {
                SharedPreff.clearDocPreff();
                Intent intent = new Intent(HomeScreenPresenterImp.this.view, DashBoard.class);
                intent.setFlags(268468224);
                HomeScreenPresenterImp.this.view.startActivity(intent);
            }
        }

        C12417() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (HomeScreenPresenterImp.this.view == null) {
            }
        }

        public void onNext(DecativateResponse decativateResponse) {
            if (HomeScreenPresenterImp.this.view != null && decativateResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                if (decativateResponse.getUserStatus() == 0) {
                    DialogPopUps.alertPopUp(HomeScreenPresenterImp.this.view, HomeScreenPresenterImp.this.view.getResources().getString(C1020R.string.your_account_has_been_deleted_or_deactivated_please_contact_our_support_for_more_information), HomeScreenPresenterImp.this.view.getResources().getString(C1020R.string.ok), new C12401());
                } else {
                    HomeScreenPresenterImp.this.view.checkPrescriptionVisibility(decativateResponse.getDrType());
                }
            }
        }
    }

    public HomeScreenPresenterImp(HomeScreen view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        settingProfilePic();
    }

    private void settingProfilePic() {
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            Log.e("loginDocResponce", new Gson().toJson(this.loginDocResponce));
            if (!TextUtils.isEmpty(this.loginDocResponce.getData().getProfilePic())) {
                this.view.settingProfilePic(this.loginDocResponce.getData().getProfilePic());
                if (TextUtils.isEmpty(this.loginDocResponce.getData().getFirstName()) || TextUtils.isEmpty(this.loginDocResponce.getData().getLastName())) {
                    this.view.settingNameAndField(this.view.getResources().getString(C1020R.string.dr) + " " + this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName() + "," + this.loginDocResponce.getData().getQualification(), this.loginDocResponce.getData().getSpeciality());
                } else {
                    this.view.settingNameAndField(this.view.getResources().getString(C1020R.string.dr) + " " + CommonUtils.capWordCase(this.loginDocResponce.getData().getFirstName()) + " " + CommonUtils.capWordCase(this.loginDocResponce.getData().getLastName()) + "," + this.loginDocResponce.getData().getQualification(), this.loginDocResponce.getData().getSpeciality());
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
            this.timer.schedule(new C12341(), 0, (long) this.repeatCounter);
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
            this.timerAppDates.schedule(new C12352(), 0, (long) this.repeatCounter);
            return;
        }
        Log.e("loginDocResponce", "null");
    }

    private void callDrAppointmentDate() {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            DrId drId = new DrId();
            drId.setDr_Id(this.loginDocResponce.getData().get_id());
            this.mSubscriptionAppData = this.netWorkingService.getAPI().getDrAppointmentDate(drId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12363());
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
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getNotificationCount(notificationCountCall).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12374());
        }
    }

    public void settingLocalNotification(DrAppointmentDate tingLocalNotification) {
        if (tingLocalNotification.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
            ArrayList arrayList = new ArrayList();
            ArrayList<DrAppointmentDateData> data = tingLocalNotification.getData();
            if (data != null && data.size() > 0) {
                for (int k = 0; k < data.size(); k++) {
                    String date1 = ((DrAppointmentDateData) data.get(k)).getSchedule() + " " + ((DrAppointmentDateData) data.get(k)).getFrom() + ":00";
                    String date2 = ((DrAppointmentDateData) data.get(k)).getSchedule() + " " + ((DrAppointmentDateData) data.get(k)).getTo() + ":00";
                    try {
                        Log.e("date_mili", "" + CommonUtils.getDateConverted(date1));
                        Log.e("date2_mili", "" + CommonUtils.getDateConverted(date2));
                        CommonUtils.addEvent(this.view, CommonUtils.getDateConverted(date1), CommonUtils.getDateConverted(date2), 1, this.view.getResources().getString(C1020R.string.you_have_a_scheduled_video_visit_with_your_patient_in_5_minutes));
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
            this.mSubscriptionCounter = this.netWorkingService.getAPI().logoutApi(this.loginDocResponce.getData().get_id(), SharedPreff.getDeviceToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12385());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void callPrescriptionApi() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getTransactionErrorsCount(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12396());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void checkDeactivate() {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.mSubscriptionAppData = this.netWorkingService.getAPI().userDecativate(this.loginDocResponce.getData().get_id(), SharedPreff.getDeviceToken(), Constants.DEVICE_TYPE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12417());
            }
        } else if (this.view == null) {
        }
    }
}
