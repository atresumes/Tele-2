package com.trigma.tiktok.presenter;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.PatientDetail;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DoctorChatResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.VideoCallResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PatientDetailPresenterImp implements PatientDetailPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private PatientDetail view;

    class C12834 implements Observer<VideoCallResponse> {
        C12834() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (PatientDetailPresenterImp.this.view != null) {
                PatientDetailPresenterImp.this.view.hideDialog();
                PatientDetailPresenterImp.this.view.showError(PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(VideoCallResponse videoCallResponse) {
            if (PatientDetailPresenterImp.this.view != null) {
                PatientDetailPresenterImp.this.view.hideDialog();
                if (videoCallResponse.getStatus() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    Log.e("videoCallResponse", new Gson().toJson((Object) videoCallResponse));
                    PatientDetailPresenterImp.this.view.gotoVideoCallingScreen(videoCallResponse.getToken(), videoCallResponse.getApikey(), videoCallResponse.getSession(), videoCallResponse.getGroupId(), videoCallResponse.getUrl(), videoCallResponse);
                    return;
                }
                PatientDetailPresenterImp.this.view.showError(videoCallResponse.getError());
            }
        }
    }

    class C12856 implements Observer<DocAcceptReject> {
        C12856() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (PatientDetailPresenterImp.this.view != null) {
                PatientDetailPresenterImp.this.view.hideDialog();
                PatientDetailPresenterImp.this.view.showError(PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject videoCallResponse) {
            if (PatientDetailPresenterImp.this.view != null) {
                PatientDetailPresenterImp.this.view.hideDialog();
                if (videoCallResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    PatientDetailPresenterImp.this.view.showChatNowButton(true);
                } else {
                    PatientDetailPresenterImp.this.view.showChatNowButton(false);
                }
            }
        }
    }

    public PatientDetailPresenterImp(PatientDetail view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.loginDocResponce = SharedPreff.getStaffLoginResponse();
        } else {
            this.loginDocResponce = SharedPreff.getLoginResponce();
        }
    }

    public void cacelSubscription(String email, String id, final int pos, String patName) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().drCencellationPatient(SharedPreff.getLoginResponce().getData().get_id(), id, this.loginDocResponce.getData().get_id(), this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName(), "" + this.loginDocResponce.getData().getUserType(), patName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (PatientDetailPresenterImp.this.view != null) {
                        PatientDetailPresenterImp.this.view.hideDialog();
                        PatientDetailPresenterImp.this.view.showError(PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject docAcceptReject) {
                    if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        PatientDetailPresenterImp.this.view.successfullyUnfriend(pos);
                    } else {
                        PatientDetailPresenterImp.this.view.showError(docAcceptReject.getError());
                    }
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void deleteRequest(String email, String id, final int pos, String patientName, String CreateId, String CreateName, String CreateUserType, String Dr_id) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().dRAcceptReject(id, "2", SharedPreff.getDeviceToken(), email, patientName, CreateId, CreateName, CreateUserType, Dr_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                class C12781 implements AlertCallBackInterface {
                    C12781() {
                    }

                    public void neutralClick() {
                        PatientDetailPresenterImp.this.view.successfullyDeleted(pos);
                    }
                }

                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (PatientDetailPresenterImp.this.view != null) {
                        PatientDetailPresenterImp.this.view.hideDialog();
                        PatientDetailPresenterImp.this.view.showError(PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject docAcceptReject) {
                    if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        DialogPopUps.confirmationPopUp(PatientDetailPresenterImp.this.view, "", PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.patient_deleted_successfully), new C12781());
                    } else {
                        PatientDetailPresenterImp.this.view.showError(docAcceptReject.getError());
                    }
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void acceptRequest(String email, String id, final int pos, final String acceptStatus, String patientName, String CreateId, String CreateName, String CreateUserType, String Dr_id) {
        if (this.loginDocResponce == null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.something_went_wrong));
        } else if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().dRAcceptReject(id, acceptStatus, SharedPreff.getDeviceToken(), email, patientName, CreateId, CreateName, CreateUserType, Dr_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                class C12801 implements AlertCallBackInterface {
                    C12801() {
                    }

                    public void neutralClick() {
                        PatientDetailPresenterImp.this.view.successfullyAccepted(pos);
                    }
                }

                class C12812 implements AlertCallBackInterface {
                    C12812() {
                    }

                    public void neutralClick() {
                        PatientDetailPresenterImp.this.view.successfullyAccepted(pos);
                    }
                }

                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (PatientDetailPresenterImp.this.view != null) {
                        PatientDetailPresenterImp.this.view.hideDialog();
                        PatientDetailPresenterImp.this.view.showError(PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject docAcceptReject) {
                    if (!docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        PatientDetailPresenterImp.this.view.showError(docAcceptReject.getError());
                    } else if (acceptStatus.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
                        DialogPopUps.confirmationPopUp(PatientDetailPresenterImp.this.view, "", PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.your_have_successfully_accepted_patient), new C12801());
                    } else {
                        DialogPopUps.confirmationPopUp(PatientDetailPresenterImp.this.view, PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.alert), PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.patient_declined_successfully), new C12812());
                    }
                }
            });
        } else {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }

    public void makeVideoCall(String BookingId, String PatientId, String drschedulesetsId) {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.view.showProgressDialog();
                this.mSubscription = this.netWorkingService.getAPI().getVideoToken(SharedPreff.getDeviceToken(), BookingId, PatientId, this.loginDocResponce.getData().get_id(), drschedulesetsId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12834());
            }
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void chatNowApi(final String drID, final PatientPendingData patientPendingData) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            String from_id = "";
            if (SharedPreff.getStaffLoginResponse() != null) {
                from_id = SharedPreff.getStaffLoginResponse().getData().get_id();
            } else {
                from_id = SharedPreff.getLoginResponce().getData().get_id();
            }
            this.mSubscription = this.netWorkingService.getAPI().drDrcreatetoken(from_id, drID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DoctorChatResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (PatientDetailPresenterImp.this.view != null) {
                        PatientDetailPresenterImp.this.view.hideDialog();
                        PatientDetailPresenterImp.this.view.showError(PatientDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DoctorChatResponse contactUsResponse) {
                    if (PatientDetailPresenterImp.this.view != null) {
                        PatientDetailPresenterImp.this.view.hideDialog();
                        ChatUserForDb chatUserForDb = new ChatUserForDb();
                        chatUserForDb.set_id(drID);
                        chatUserForDb.setAddress("");
                        chatUserForDb.setApiKey(contactUsResponse.getData().getApiKey());
                        chatUserForDb.setBio("");
                        chatUserForDb.setCity("");
                        chatUserForDb.setCode(patientPendingData.getCode());
                        chatUserForDb.setCount(0);
                        chatUserForDb.setUserType(0);
                        chatUserForDb.setMobile(patientPendingData.getMobile());
                        chatUserForDb.setDeviceToken(patientPendingData.getDeviceToken());
                        chatUserForDb.setSessionData(contactUsResponse.getData().getSessionData());
                        chatUserForDb.setProfilePic(patientPendingData.getProfilePic());
                        chatUserForDb.setGroupId(contactUsResponse.getData().getGroupId());
                        chatUserForDb.setDOB(patientPendingData.getDOB());
                        chatUserForDb.setEmail(patientPendingData.getEmail());
                        chatUserForDb.setFirstName("");
                        chatUserForDb.setLastName("");
                        chatUserForDb.setQualification("");
                        chatUserForDb.setSpeciality("");
                        chatUserForDb.setName(patientPendingData.getName());
                        chatUserForDb.setTokenData(contactUsResponse.getData().getTokenData());
                        chatUserForDb.setGender(patientPendingData.getGender());
                        SharedPreff.saveChatDbDetail(chatUserForDb);
                        PatientDetailPresenterImp.this.view.gotoChatScreen(chatUserForDb);
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void checkChatButtonVisibility(String drID, String ptId) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().DRPatientCheck(drID, ptId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12856());
        }
    }
}
