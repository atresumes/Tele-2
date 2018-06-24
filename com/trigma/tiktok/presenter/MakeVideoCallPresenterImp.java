package com.trigma.tiktok.presenter;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.MakeVideoCall;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.Upcoming;
import com.trigma.tiktok.model.VideoCallResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MakeVideoCallPresenterImp implements MakeVideoCallPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private Upcoming upcoming;
    private MakeVideoCall view;

    class C12501 implements Observer<DocAcceptReject> {
        C12501() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MakeVideoCallPresenterImp.this.view != null) {
                MakeVideoCallPresenterImp.this.view.hideDialog();
                MakeVideoCallPresenterImp.this.view.showError(MakeVideoCallPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject contactUsResponse) {
            if (MakeVideoCallPresenterImp.this.view != null) {
                MakeVideoCallPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MakeVideoCallPresenterImp.this.view.cancelSuccess();
                } else {
                    MakeVideoCallPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    class C12512 implements Observer<VideoCallResponse> {
        C12512() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MakeVideoCallPresenterImp.this.view != null) {
                MakeVideoCallPresenterImp.this.view.hideDialog();
                MakeVideoCallPresenterImp.this.view.showError(MakeVideoCallPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(VideoCallResponse videoCallResponse) {
            if (MakeVideoCallPresenterImp.this.view != null) {
                MakeVideoCallPresenterImp.this.view.hideDialog();
                if (videoCallResponse.getStatus() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    MakeVideoCallPresenterImp.this.view.gotoVideoCallingScreen(videoCallResponse.getToken(), videoCallResponse.getApikey(), videoCallResponse.getSession(), videoCallResponse.getGroupId(), videoCallResponse.getUrl(), videoCallResponse);
                } else {
                    MakeVideoCallPresenterImp.this.view.showError(videoCallResponse.getError());
                }
            }
        }
    }

    public MakeVideoCallPresenterImp(MakeVideoCall view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.loginDocResponce = SharedPreff.getStaffLoginResponse();
        } else {
            this.loginDocResponce = SharedPreff.getLoginResponce();
        }
    }

    public void cancelAppointment(Upcoming upcoming) {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.view.showProgressDialog();
                this.mSubscriptionCounter = this.netWorkingService.getAPI().doctorCancellationBooking(upcoming.getBookingId(), upcoming.getDrschedulesetsId(), upcoming.getPatientId().getDeviceToken(), Constants.DEVICE_TYPE, upcoming.getPatientId().getEmail(), this.loginDocResponce.getData().get_id(), this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName(), "" + this.loginDocResponce.getData().getUserType(), SharedPreff.getLoginResponce().getData().get_id(), upcoming.getScheduleDate(), upcoming.getFrom(), upcoming.getTo(), SharedPreff.getLoginResponce().getData().getFirstName() + " " + SharedPreff.getLoginResponce().getData().getLastName(), upcoming.getPatientId().getFirstName() + " " + upcoming.getPatientId().getLastName()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12501());
            }
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void makeVideoCall(String BookingId, String PatientId, String drschedulesetsId) {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.view.showProgressDialog();
                this.mSubscriptionCounter = this.netWorkingService.getAPI().getVideoToken(SharedPreff.getDeviceToken(), BookingId, PatientId, this.loginDocResponce.getData().get_id(), drschedulesetsId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12512());
            }
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
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
}
