package com.trigma.tiktok.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.activity.StaffHomeScreen;
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
import com.trigma.tiktok.model.StaffDoctorListObject;
import com.trigma.tiktok.model.StaffDoctorListResponse;
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

public class StaffHomePresenterImp implements StaffHomePresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionAppData;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private int repeatCounter = 90000;
    private Timer timer = null;
    private Timer timerAppDates = null;
    private StaffHomeScreen view;

    class C13001 extends TimerTask {
        C13001() {
        }

        public void run() {
            Log.e("startingTimer", "startingTimer");
            if (StaffHomePresenterImp.this.view != null) {
                StaffHomePresenterImp.this.timerApiCall();
            }
        }
    }

    class C13012 extends TimerTask {
        C13012() {
        }

        public void run() {
            Log.e("startingTimer", "checkingAppointmentDates");
            if (StaffHomePresenterImp.this.view != null) {
                StaffHomePresenterImp.this.callDrAppointmentDate();
                if (SharedPreff.getStaffLoginResponse().getData() != null) {
                    StaffHomePresenterImp.this.checkStaffExist(SharedPreff.getStaffLoginResponse().getData().get_id());
                }
            }
        }
    }

    class C13023 implements Observer<DrAppointmentDate> {
        C13023() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(DrAppointmentDate drAppointmentDate) {
            if (drAppointmentDate.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                Log.e("drAppointmentDate", new Gson().toJson((Object) drAppointmentDate));
                if (StaffHomePresenterImp.this.view != null) {
                    StaffHomePresenterImp.this.settingLocalNotification(drAppointmentDate);
                }
            }
        }
    }

    class C13034 implements Observer<NotificationCountResponse> {
        C13034() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(NotificationCountResponse notificationCountResponse) {
            Log.e("notificationResponse", "" + new Gson().toJson((Object) notificationCountResponse));
            if (notificationCountResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && StaffHomePresenterImp.this.view != null) {
                if (notificationCountResponse.getRequestCount() > 0) {
                    StaffHomePresenterImp.this.view.showMyPatientCount("" + notificationCountResponse.getRequestCount());
                } else {
                    StaffHomePresenterImp.this.view.hideMyPatientCount();
                }
                NotificationCountPush notificationCountPush;
                if (notificationCountResponse.getMessagedotshow() == 1) {
                    notificationCountPush = new NotificationCountPush();
                    notificationCountPush.setShowCount(true);
                    EventBus.getDefault().post(notificationCountPush);
                    StaffHomePresenterImp.this.view.showMessageCount("");
                } else {
                    notificationCountPush = new NotificationCountPush();
                    notificationCountPush.setShowCount(false);
                    EventBus.getDefault().post(notificationCountPush);
                    StaffHomePresenterImp.this.view.hideMessageCount();
                }
                if (notificationCountResponse.getScheduleCount() > 0) {
                    StaffHomePresenterImp.this.view.showAppointmentCount("" + notificationCountResponse.getScheduleCount());
                } else {
                    StaffHomePresenterImp.this.view.hideAppointmentCount();
                }
            }
        }
    }

    class C13045 implements Observer<DocAcceptReject> {
        C13045() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (StaffHomePresenterImp.this.view != null) {
                StaffHomePresenterImp.this.view.hideDialog();
                StaffHomePresenterImp.this.view.showError(StaffHomePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject deDocAcceptReject) {
            if (StaffHomePresenterImp.this.view != null) {
                StaffHomePresenterImp.this.view.hideDialog();
                if (deDocAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    SharedPreff.clearDocPreff();
                    StaffHomePresenterImp.this.view.loginSuccessfull();
                    return;
                }
                StaffHomePresenterImp.this.view.showError(deDocAcceptReject.getError());
            }
        }
    }

    class C13056 implements Observer<PrescriptionObject> {
        C13056() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (StaffHomePresenterImp.this.view != null) {
                StaffHomePresenterImp.this.view.hideDialog();
                StaffHomePresenterImp.this.view.showError(StaffHomePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(PrescriptionObject prescriptionObject) {
            if (StaffHomePresenterImp.this.view != null) {
                StaffHomePresenterImp.this.view.hideDialog();
                if (prescriptionObject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    StaffHomePresenterImp.this.view.gotoPrescriptionScreen(prescriptionObject.getUrl(), prescriptionObject.getTransactionErrorsCount());
                } else {
                    StaffHomePresenterImp.this.view.showError(prescriptionObject.getError());
                }
            }
        }
    }

    class C13077 implements Observer<DecativateResponse> {

        class C13061 implements AlertCallBackInterface {
            C13061() {
            }

            public void neutralClick() {
                SharedPreff.clearDocPreff();
                Intent intent = new Intent(StaffHomePresenterImp.this.view, DashBoard.class);
                intent.setFlags(268468224);
                StaffHomePresenterImp.this.view.startActivity(intent);
            }
        }

        C13077() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (StaffHomePresenterImp.this.view == null) {
            }
        }

        public void onNext(DecativateResponse decativateResponse) {
            if (StaffHomePresenterImp.this.view != null && decativateResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && decativateResponse.getUserStatus() == 0) {
                DialogPopUps.alertPopUp(StaffHomePresenterImp.this.view, StaffHomePresenterImp.this.view.getResources().getString(C1020R.string.your_account_has_been_deleted_or_deactivated_please_contact_our_support_for_more_information), StaffHomePresenterImp.this.view.getResources().getString(C1020R.string.ok), new C13061());
            }
        }
    }

    class C13088 implements Observer<StaffDoctorListResponse> {
        C13088() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (StaffHomePresenterImp.this.view == null) {
            }
        }

        public void onNext(StaffDoctorListResponse staffDoctorListResponse) {
            boolean noDocActivated = false;
            if (StaffHomePresenterImp.this.view != null) {
                if (staffDoctorListResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    if (staffDoctorListResponse.getData().size() > 0) {
                        for (int a = 0; a < staffDoctorListResponse.getData().size(); a++) {
                            if (((StaffDoctorListObject) staffDoctorListResponse.getData().get(a)).getStaffStatus() == 1) {
                                noDocActivated = true;
                                break;
                            }
                        }
                    } else {
                        return;
                    }
                }
                if (!noDocActivated) {
                    StaffHomePresenterImp.this.stopingAppDate();
                    StaffHomePresenterImp.this.stopingTimer();
                    StaffHomePresenterImp.this.view.clearPreffAndLoggOut();
                }
            }
        }
    }

    public StaffHomePresenterImp(StaffHomeScreen view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
    }

    private void settingProfilePic() {
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            Log.e("loginDocResponce", new Gson().toJson(this.loginDocResponce));
            if (!TextUtils.isEmpty(this.loginDocResponce.getData().getProfilePic())) {
                this.view.settingProfilePic(this.loginDocResponce.getData().getProfilePic());
                if (TextUtils.isEmpty(this.loginDocResponce.getData().getFirstName()) || TextUtils.isEmpty(this.loginDocResponce.getData().getLastName())) {
                    this.view.settingNameAndField(this.view.getResources().getString(C1020R.string.dr) + " " + this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName(), this.loginDocResponce.getData().getSpeciality());
                } else {
                    this.view.settingNameAndField(this.view.getResources().getString(C1020R.string.dr) + " " + CommonUtils.capWordCase(this.loginDocResponce.getData().getFirstName()) + " " + CommonUtils.capWordCase(this.loginDocResponce.getData().getLastName()), this.loginDocResponce.getData().getSpeciality());
                }
            }
        }
    }

    public void startNotificationCountApi() {
        settingProfilePic();
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            if (this.timer == null) {
                this.timer = new Timer();
            } else {
                this.timer.cancel();
                this.timer = null;
                this.timer = new Timer();
            }
            this.timer.schedule(new C13001(), 0, (long) this.repeatCounter);
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
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            if (this.timerAppDates == null) {
                this.timerAppDates = new Timer();
            } else {
                this.timerAppDates.cancel();
                this.timerAppDates = null;
                this.timerAppDates = new Timer();
            }
            this.timerAppDates.schedule(new C13012(), 0, (long) this.repeatCounter);
            return;
        }
        Log.e("loginDocResponce", "null");
    }

    private void callDrAppointmentDate() {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            DrId drId = new DrId();
            drId.setDr_Id(this.loginDocResponce.getData().get_id());
            this.mSubscriptionAppData = this.netWorkingService.getAPI().getDrAppointmentDate(drId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13023());
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
            this.mSubscriptionCounter = this.netWorkingService.getAPI().homeScreenStaffCount(SharedPreff.getStaffLoginResponse().getData().get_id(), SharedPreff.getLoginResponce().getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13034());
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

    public void logout(String idd) {
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().logoutApi(idd, SharedPreff.getDeviceToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13045());
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
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getTransactionErrorsCount(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13056());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void checkDeactivate(String id) {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.mSubscriptionAppData = this.netWorkingService.getAPI().userDecativate(id, SharedPreff.getDeviceToken(), Constants.DEVICE_TYPE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13077());
            }
        } else if (this.view == null) {
        }
    }

    public void checkStaffExist(String id) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.mSubscriptionAppData = this.netWorkingService.getAPI().getStaffDocList(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13088());
        }
    }

    public void fetchStaffDocList(String staffID, final boolean firstTime) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionAppData = this.netWorkingService.getAPI().getStaffDocList(staffID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StaffDoctorListResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (StaffHomePresenterImp.this.view != null) {
                        StaffHomePresenterImp.this.view.hideDialog();
                        if (firstTime) {
                            StaffHomePresenterImp.this.view.showErrorWithInterface(StaffHomePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                        }
                    }
                }

                public void onNext(StaffDoctorListResponse staffDoctorListResponse) {
                    boolean noDocActivated = false;
                    if (StaffHomePresenterImp.this.view != null) {
                        StaffHomePresenterImp.this.view.hideDialog();
                        if (staffDoctorListResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                            if (staffDoctorListResponse.getData().size() > 0) {
                                for (int a = 0; a < staffDoctorListResponse.getData().size(); a++) {
                                    if (((StaffDoctorListObject) staffDoctorListResponse.getData().get(a)).getStaffStatus() == 1) {
                                        noDocActivated = true;
                                        break;
                                    }
                                }
                                if (!noDocActivated) {
                                    StaffHomePresenterImp.this.stopingAppDate();
                                    StaffHomePresenterImp.this.stopingTimer();
                                    StaffHomePresenterImp.this.view.staffDeactivated(Constants.CONTEXT);
                                }
                                StaffHomePresenterImp.this.view.settingLeftMenuAdapter(staffDoctorListResponse.getData(), firstTime);
                            }
                        } else if (firstTime) {
                            StaffHomePresenterImp.this.view.showErrorWithInterface(StaffHomePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                        }
                    }
                }
            });
        }
    }
}
