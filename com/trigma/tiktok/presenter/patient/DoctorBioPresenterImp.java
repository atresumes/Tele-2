package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.patient.DoctorBio;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DoctorChatResponse;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DoctorBioPresenterImp implements DoctorBioPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    String nameShown = "";
    private NetWorkingService netWorkingService;
    private String patient_name = "";
    private String userID = "";
    private DoctorBio view;

    class C13272 implements Observer<DocAcceptReject> {

        class C13241 implements AlertCallBackInterface {
            C13241() {
            }

            public void neutralClick() {
                DoctorBioPresenterImp.this.view.goToSelectPharmacy();
            }
        }

        class C13252 implements AlertCallBackInterface {
            C13252() {
            }

            public void neutralClick() {
                DoctorBioPresenterImp.this.view.finishActivity();
            }
        }

        class C13263 implements AlertCallBackInterface {
            C13263() {
            }

            public void neutralClick() {
                DoctorBioPresenterImp.this.view.finishActivity();
            }
        }

        C13272() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (DoctorBioPresenterImp.this.view != null) {
                DoctorBioPresenterImp.this.view.hideDialog();
                DoctorBioPresenterImp.this.view.showError(DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject contactUsResponse) {
            if (DoctorBioPresenterImp.this.view != null) {
                DoctorBioPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    DialogPopUps.confirmationPopUp(DoctorBioPresenterImp.this.view, DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.request_sent), DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.you_can_self_schedule_your_video_visits_with) + " " + DoctorBioPresenterImp.this.nameShown + " " + DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.once_they_accept_your_request), new C13241());
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_410)) {
                    DialogPopUps.confirmationPopUp(DoctorBioPresenterImp.this.view, DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.alert), DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.request_has_already_been_sent), new C13252());
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_409)) {
                    DialogPopUps.confirmationPopUp(DoctorBioPresenterImp.this.view, DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.alert), DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.request_has_already_been_sent), new C13263());
                } else {
                    DoctorBioPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    public DoctorBioPresenterImp(DoctorBio view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            this.userID = this.loginDocResponce.getData().get_id();
            if (this.loginDocResponce.getData().getUserType() == 1) {
                view.settingButtonText(view.getResources().getString(C1020R.string.chat_now));
            }
            try {
                this.patient_name = this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.userID = SharedPreff.getUerID();
        try {
            if (SharedPreff.getCreateUserPojo() != null) {
                this.patient_name = SharedPreff.getCreateUserPojo().getFirstName() + " " + SharedPreff.getCreateUserPojo().getLastName();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
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

    public void makeCall(final String phone, String formatedNUM) {
        DialogPopUps.showAlertWithButtons(this.view, this.view.getResources().getString(C1020R.string.please_confirm_your_call), this.view.getResources().getString(C1020R.string.You_re_about_to_call_the_following) + "\n" + formatedNUM, this.view.getResources().getString(C1020R.string.no_dialog), this.view.getResources().getString(C1020R.string.yes_dialog), "", false, true, new AlertCallBackWithButtonsInterface() {
            public void positiveClick() {
            }

            public void neutralClick() {
            }

            public void negativeClick() {
                CommonUtils.call(DoctorBioPresenterImp.this.view, phone);
            }
        });
    }

    public void sendRequestToDoc(String DrID, String Email, String DrName, String DeviceToken, String name, String deviceType) {
        if (name.contains(this.view.getResources().getString(C1020R.string.dr))) {
            this.nameShown = name;
        } else {
            this.nameShown = this.view.getResources().getString(C1020R.string.dr) + " " + name;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().makeRequestToDoctor(this.userID, DrID, DeviceToken, deviceType, Email, DrName, this.patient_name).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13272());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void chatNowApi(final String drID, final DrSearchNameObject drSearchNameObject) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().drDrcreatetoken(this.userID, drID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DoctorChatResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (DoctorBioPresenterImp.this.view != null) {
                        DoctorBioPresenterImp.this.view.hideDialog();
                        DoctorBioPresenterImp.this.view.showError(DoctorBioPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DoctorChatResponse contactUsResponse) {
                    if (DoctorBioPresenterImp.this.view != null) {
                        DoctorBioPresenterImp.this.view.hideDialog();
                        ChatUserForDb chatUserForDb = new ChatUserForDb();
                        chatUserForDb.set_id(drID);
                        chatUserForDb.setAddress(drSearchNameObject.getAddress());
                        chatUserForDb.setApiKey(contactUsResponse.getData().getApiKey());
                        chatUserForDb.setBio(drSearchNameObject.getBio());
                        chatUserForDb.setCity("");
                        chatUserForDb.setCode(drSearchNameObject.getCode());
                        chatUserForDb.setCount(0);
                        chatUserForDb.setUserType(1);
                        chatUserForDb.setMobile(drSearchNameObject.getMobile());
                        chatUserForDb.setDeviceToken(drSearchNameObject.getDeviceToken());
                        chatUserForDb.setSessionData(contactUsResponse.getData().getSessionData());
                        chatUserForDb.setProfilePic(drSearchNameObject.getProfilePic());
                        chatUserForDb.setGroupId(contactUsResponse.getData().getGroupId());
                        chatUserForDb.setDOB("");
                        chatUserForDb.setEmail(drSearchNameObject.getEmail());
                        chatUserForDb.setFirstName("");
                        chatUserForDb.setLastName("");
                        chatUserForDb.setQualification(drSearchNameObject.getQualification());
                        chatUserForDb.setSpeciality(drSearchNameObject.getSpeciality());
                        chatUserForDb.setName(drSearchNameObject.getName());
                        chatUserForDb.setTokenData(contactUsResponse.getData().getTokenData());
                        chatUserForDb.setGender("");
                        SharedPreff.saveChatDbDetail(chatUserForDb);
                        DoctorBioPresenterImp.this.view.gotoChatScreen(chatUserForDb);
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }
}
