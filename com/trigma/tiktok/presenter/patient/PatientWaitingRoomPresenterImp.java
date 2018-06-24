package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.patient.PatientWaitingRoom;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.PatientModelUpcomingOuter;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Subscription;

public class PatientWaitingRoomPresenterImp implements PatientWaitingRoomPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private PatientModelUpcomingOuter upcoming;
    private PatientWaitingRoom view;

    public PatientWaitingRoomPresenterImp(PatientWaitingRoom view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscriptionCounter != null) {
            this.mSubscriptionCounter.unsubscribe();
        }
        this.view = null;
    }

    public void makeCall(final String phn, String formatedNUmber) {
        DialogPopUps.showAlertWithButtons(this.view, this.view.getResources().getString(C1020R.string.please_confirm_your_call), this.view.getResources().getString(C1020R.string.You_re_about_to_call_the_following) + "\n" + formatedNUmber, this.view.getResources().getString(C1020R.string.no_dialog), this.view.getResources().getString(C1020R.string.yes_dialog), "", false, true, new AlertCallBackWithButtonsInterface() {
            public void positiveClick() {
            }

            public void neutralClick() {
            }

            public void negativeClick() {
                CommonUtils.call(PatientWaitingRoomPresenterImp.this.view, phn);
            }
        });
    }
}
