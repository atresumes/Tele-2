package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.NotificationFromPush;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.NotificationResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationFromPushPresenterImp implements NotificationFromPushPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private NotificationFromPush view;

    class C12711 implements Observer<NotificationResponse> {
        C12711() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (NotificationFromPushPresenterImp.this.view != null) {
                NotificationFromPushPresenterImp.this.view.hideDialog();
                NotificationFromPushPresenterImp.this.view.showError(NotificationFromPushPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(NotificationResponse notificationResponse) {
            if (NotificationFromPushPresenterImp.this.view != null) {
                NotificationFromPushPresenterImp.this.view.hideDialog();
                if (notificationResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    NotificationFromPushPresenterImp.this.view.setNotiifcation(notificationResponse.getData());
                    NotificationFromPushPresenterImp.this.notificationRead();
                    return;
                }
                NotificationFromPushPresenterImp.this.view.showError(notificationResponse.getError());
            }
        }
    }

    class C12722 implements Observer<DocAcceptReject> {
        C12722() {
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

    class C12733 implements Observer<NotificationResponse> {
        C12733() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (NotificationFromPushPresenterImp.this.view != null) {
                NotificationFromPushPresenterImp.this.view.hideDialog();
                NotificationFromPushPresenterImp.this.view.showError(NotificationFromPushPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(NotificationResponse notificationResponse) {
            if (NotificationFromPushPresenterImp.this.view != null) {
                NotificationFromPushPresenterImp.this.view.hideDialog();
                if (notificationResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    NotificationFromPushPresenterImp.this.view.setNotiifcation(notificationResponse.getData());
                    NotificationFromPushPresenterImp.this.notificationRead();
                    return;
                }
                NotificationFromPushPresenterImp.this.view.showError(notificationResponse.getError());
            }
        }
    }

    public NotificationFromPushPresenterImp(NotificationFromPush view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void getNotification() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getDocNotification(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12711());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void notificationRead() {
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this.view)) {
            this.mSubscription = this.netWorkingService.getAPI().drNotificationRead(this.loginDocResponce.getData().get_id(), "" + this.loginDocResponce.getData().getUserType()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12722());
        }
    }

    public void getPatientNotification() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getPatientNotification(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12733());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }
}
