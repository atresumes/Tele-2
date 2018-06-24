package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.Notification;
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

public class NotificationPresenterImp implements NotificationPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private Notification view;

    class C12741 implements Observer<NotificationResponse> {
        C12741() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (NotificationPresenterImp.this.view != null) {
                NotificationPresenterImp.this.view.hideDialog();
                NotificationPresenterImp.this.view.showError(NotificationPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(NotificationResponse notificationResponse) {
            if (NotificationPresenterImp.this.view != null) {
                NotificationPresenterImp.this.view.hideDialog();
                if (notificationResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    NotificationPresenterImp.this.view.setNotiifcation(notificationResponse.getData());
                    NotificationPresenterImp.this.notificationRead();
                    return;
                }
                NotificationPresenterImp.this.view.showError(notificationResponse.getError());
            }
        }
    }

    class C12752 implements Observer<DocAcceptReject> {
        C12752() {
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

    class C12763 implements Observer<NotificationResponse> {
        C12763() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (NotificationPresenterImp.this.view != null) {
                NotificationPresenterImp.this.view.hideDialog();
                NotificationPresenterImp.this.view.showError(NotificationPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(NotificationResponse notificationResponse) {
            if (NotificationPresenterImp.this.view != null) {
                NotificationPresenterImp.this.view.hideDialog();
                if (notificationResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    NotificationPresenterImp.this.view.setNotiifcation(notificationResponse.getData());
                    NotificationPresenterImp.this.notificationRead();
                    return;
                }
                NotificationPresenterImp.this.view.showError(notificationResponse.getError());
            }
        }
    }

    public NotificationPresenterImp(Notification view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void getNotification() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getDocNotification(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12741());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void notificationRead() {
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.mSubscription = this.netWorkingService.getAPI().drNotificationRead(this.loginDocResponce.getData().get_id(), "" + this.loginDocResponce.getData().getUserType()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12752());
        }
    }

    public void getPatientNotification() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getPatientNotification(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12763());
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
