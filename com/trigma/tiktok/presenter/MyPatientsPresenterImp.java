package com.trigma.tiktok.presenter;

import com.facebook.appevents.AppEventsConstants;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.MyPatients;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DoctorChatResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.PatientPendingResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyPatientsPresenterImp implements MyPatientsPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private MyPatients view;

    class C12611 implements Observer<PatientPendingResponse> {
        C12611() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MyPatientsPresenterImp.this.view != null) {
                MyPatientsPresenterImp.this.view.hideDialog();
                MyPatientsPresenterImp.this.view.showError(MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(PatientPendingResponse patientPendingResponse) {
            if (MyPatientsPresenterImp.this.view != null) {
                MyPatientsPresenterImp.this.view.hideDialog();
                if (patientPendingResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MyPatientsPresenterImp.this.view.setPendingAdapter(patientPendingResponse.getData());
                } else if (patientPendingResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    MyPatientsPresenterImp.this.view.setPendingAdapter(new ArrayList());
                } else {
                    MyPatientsPresenterImp.this.view.showError(patientPendingResponse.getError());
                }
            }
        }
    }

    class C12622 implements Observer<PatientPendingResponse> {
        C12622() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MyPatientsPresenterImp.this.view != null) {
                MyPatientsPresenterImp.this.view.hideDialog();
                MyPatientsPresenterImp.this.view.showError(MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(PatientPendingResponse patientPendingResponse) {
            if (MyPatientsPresenterImp.this.view != null) {
                MyPatientsPresenterImp.this.view.hideDialog();
                if (patientPendingResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MyPatientsPresenterImp.this.view.setActiveAdapter(patientPendingResponse.getData());
                } else if (patientPendingResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    MyPatientsPresenterImp.this.view.setActiveAdapter(new ArrayList());
                } else {
                    MyPatientsPresenterImp.this.view.showError(patientPendingResponse.getError());
                }
            }
        }
    }

    public MyPatientsPresenterImp(MyPatients view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void callPendingPatient() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getPatientList(this.loginDocResponce.getData().get_id(), AppEventsConstants.EVENT_PARAM_VALUE_NO).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12611());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void callActivePatient() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getPatientList(this.loginDocResponce.getData().get_id(), AppEventsConstants.EVENT_PARAM_VALUE_YES).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12622());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void callConfirm() {
        if (this.loginDocResponce != null && !CommonUtils.isConnectedToInternet(this.view.getActivity())) {
        }
    }

    public void callDelete() {
        if (this.loginDocResponce != null && !CommonUtils.isConnectedToInternet(this.view.getActivity())) {
        }
    }

    public void deleteRequest(String email, String id, final int pos, String deviceToken, String patientName, String CreateId, String CreateName, String CreateUserType, String Dr_id) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().dRAcceptReject(id, "2", deviceToken, email, patientName, CreateId, CreateName, CreateUserType, Dr_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                class C12631 implements AlertCallBackInterface {
                    C12631() {
                    }

                    public void neutralClick() {
                    }
                }

                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (MyPatientsPresenterImp.this.view != null) {
                        MyPatientsPresenterImp.this.view.hideDialog();
                        MyPatientsPresenterImp.this.view.showError(MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject docAcceptReject) {
                    if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        MyPatientsPresenterImp.this.view.successfullyDeleted(pos);
                        DialogPopUps.confirmationPopUp(MyPatientsPresenterImp.this.view.getActivity(), MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.alert), MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.patient_declined_successfully), new C12631());
                        return;
                    }
                    MyPatientsPresenterImp.this.view.showError(docAcceptReject.getError());
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void acceptRequest(String email, String id, final int pos, String deviceToken, String patientName, String CreateId, String CreateName, String CreateUserType, String Dr_id) {
        if (this.loginDocResponce == null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.something_went_wrong));
        } else if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().dRAcceptReject(id, AppEventsConstants.EVENT_PARAM_VALUE_YES, deviceToken, email, patientName, CreateId, CreateName, CreateUserType, Dr_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                class C12651 implements AlertCallBackInterface {
                    C12651() {
                    }

                    public void neutralClick() {
                    }
                }

                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (MyPatientsPresenterImp.this.view != null) {
                        MyPatientsPresenterImp.this.view.hideDialog();
                        MyPatientsPresenterImp.this.view.showError(MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject docAcceptReject) {
                    if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        MyPatientsPresenterImp.this.view.successfullyAccepted(pos);
                        DialogPopUps.confirmationPopUp(MyPatientsPresenterImp.this.view.getActivity(), MyPatientsPresenterImp.this.view.getActivity().getResources().getString(C1020R.string.patient_accepted), MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.your_have_successfully_accepted_patient), new C12651());
                        return;
                    }
                    MyPatientsPresenterImp.this.view.showError(docAcceptReject.getError());
                }
            });
        } else {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void chatNowApi(final String drID, final PatientPendingData drStaffListObject) {
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
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
                    if (MyPatientsPresenterImp.this.view != null) {
                        MyPatientsPresenterImp.this.view.hideDialog();
                        MyPatientsPresenterImp.this.view.showError(MyPatientsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DoctorChatResponse contactUsResponse) {
                    if (MyPatientsPresenterImp.this.view != null) {
                        MyPatientsPresenterImp.this.view.hideDialog();
                        ChatUserForDb chatUserForDb = new ChatUserForDb();
                        chatUserForDb.set_id(drID);
                        chatUserForDb.setAddress("");
                        chatUserForDb.setApiKey(contactUsResponse.getData().getApiKey());
                        chatUserForDb.setBio("");
                        chatUserForDb.setCity("");
                        chatUserForDb.setCode(drStaffListObject.getCode());
                        chatUserForDb.setCount(0);
                        chatUserForDb.setUserType(0);
                        chatUserForDb.setMobile(drStaffListObject.getMobile());
                        chatUserForDb.setDeviceToken(drStaffListObject.getDeviceToken());
                        chatUserForDb.setSessionData(contactUsResponse.getData().getSessionData());
                        chatUserForDb.setProfilePic(drStaffListObject.getProfilePic());
                        chatUserForDb.setGroupId(contactUsResponse.getData().getGroupId());
                        chatUserForDb.setDOB(drStaffListObject.getDOB());
                        chatUserForDb.setEmail(drStaffListObject.getEmail());
                        chatUserForDb.setFirstName("");
                        chatUserForDb.setLastName("");
                        chatUserForDb.setQualification("");
                        chatUserForDb.setSpeciality("");
                        chatUserForDb.setName(drStaffListObject.getName());
                        chatUserForDb.setTokenData(contactUsResponse.getData().getTokenData());
                        chatUserForDb.setGender(drStaffListObject.getGender());
                        SharedPreff.saveChatDbDetail(chatUserForDb);
                        MyPatientsPresenterImp.this.view.gotoChatScreen(chatUserForDb);
                    }
                }
            });
        } else if (this.view != null) {
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
}
