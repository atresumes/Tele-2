package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.VideoCallScreen;
import com.trigma.tiktok.model.AsyncMessageSend;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.concurrent.TimeUnit;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoCallScreenPresenterImp implements VideoCallScreenPresenter {
    private boolean isSubscriberConnected = false;
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    Observer observer = new C13222();
    private boolean schduleOverCalled = false;
    private Subscription timerSubscription;
    private VideoCallScreen view;

    class C13201 implements Observer<DocAcceptReject> {
        C13201() {
        }

        public void onCompleted() {
            VideoCallScreenPresenterImp.this.schduleOverCalled = false;
        }

        public void onError(Throwable e) {
            VideoCallScreenPresenterImp.this.schduleOverCalled = false;
            if (VideoCallScreenPresenterImp.this.view != null) {
                VideoCallScreenPresenterImp.this.view.hideDialog();
                VideoCallScreenPresenterImp.this.view.showError(VideoCallScreenPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject contactUsResponse) {
            VideoCallScreenPresenterImp.this.schduleOverCalled = false;
            if (VideoCallScreenPresenterImp.this.view != null) {
                VideoCallScreenPresenterImp.this.view.hideDialog();
                if (!contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    VideoCallScreenPresenterImp.this.view.showError(contactUsResponse.getError());
                } else if (VideoCallScreenPresenterImp.this.loginDocResponce.getData().getUserType() == 1) {
                    VideoCallScreenPresenterImp.this.view.showPrescriptionDialog(1);
                } else {
                    VideoCallScreenPresenterImp.this.view.gotoRatingScreen();
                }
            }
        }
    }

    class C13222 implements Observer {

        class C13211 implements AlertCallBackInterface {
            C13211() {
            }

            public void neutralClick() {
                VideoCallScreenPresenterImp.this.view.gotoHomeScreen(VideoCallScreenPresenterImp.this.loginDocResponce.getData().getUserType());
            }
        }

        C13222() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object o) {
            if (VideoCallScreenPresenterImp.this.view != null && VideoCallScreenPresenterImp.this.loginDocResponce != null && !VideoCallScreenPresenterImp.this.isSubscriberConnected) {
                String msg = "";
                if (VideoCallScreenPresenterImp.this.loginDocResponce.getData().getUserType() == 1) {
                    msg = VideoCallScreenPresenterImp.this.view.getResources().getString(C1020R.string.patient_did_not_picked_the_call_please_try_after_sometime);
                } else {
                    msg = VideoCallScreenPresenterImp.this.view.getResources().getString(C1020R.string.doctor_did_not_picked_the_call_please_try_after_sometime);
                }
                VideoCallScreenPresenterImp.this.view.hideDialog();
                DialogPopUps.alertPopUp(VideoCallScreenPresenterImp.this.view, msg, VideoCallScreenPresenterImp.this.view.getResources().getString(C1020R.string.ok_dialog), new C13211());
            }
        }
    }

    public VideoCallScreenPresenterImp(VideoCallScreen view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
    }

    public void checkSubscriberConnected() {
        this.timerSubscription = Observable.timer(50, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(this.observer);
    }

    public void setSubscribed(boolean val) {
        this.isSubscriberConnected = val;
    }

    public void callDisconnected(VideoCallRequiredFields videoCallRequiredFields) {
        if (!this.schduleOverCalled && this.loginDocResponce != null && this.view != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.schduleOverCalled = true;
                this.view.showProgressDialog();
                this.mSubscription = this.netWorkingService.getAPI().makeSchduleOverCall(videoCallRequiredFields.getBookingId(), videoCallRequiredFields.getDrschedulesetsId(), SharedPreff.getDeviceToken(), videoCallRequiredFields.getSchedule(), videoCallRequiredFields.getPatientEmail(), videoCallRequiredFields.getDrName(), "" + videoCallRequiredFields.getUser(), videoCallRequiredFields.getGroupId(), videoCallRequiredFields.getPatientName()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13201());
                return;
            }
            if (this.view != null) {
                this.view.setCallFailed(true);
            }
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void fetchChatFromDb(String parentId) {
    }

    public void fetchFromServer(String groupId, String userId, String uniqueID) {
    }

    public void saveChat(ChatMessage chatMessage, String parentId) {
    }

    public void gettingLink(Part p, RequestBody name) {
    }

    public void sendMsgToServer(AsyncMessageSend asyncMessageSend, ChatMessage chatMessage, String userID) {
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        if (this.timerSubscription != null) {
            this.timerSubscription.unsubscribe();
        }
        this.view = null;
    }
}
