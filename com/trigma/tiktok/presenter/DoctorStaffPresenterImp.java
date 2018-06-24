package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DoctorStaff;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DoctorChatResponse;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.DrStaffListResponce;
import com.trigma.tiktok.model.LoginDocResponce;
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

public class DoctorStaffPresenterImp implements DoctorStaffPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private DoctorStaff view;

    class C12292 implements Observer<DrStaffListResponce> {
        C12292() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (DoctorStaffPresenterImp.this.view != null) {
                DoctorStaffPresenterImp.this.view.hideDialog();
                DoctorStaffPresenterImp.this.view.showError(DoctorStaffPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrStaffListResponce drStaffListResponce) {
            if (DoctorStaffPresenterImp.this.view != null) {
                DoctorStaffPresenterImp.this.view.hideDialog();
                if (drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    try {
                        DoctorStaffPresenterImp.this.view.settingCountLimit(drStaffListResponce.getLimit());
                        if (drStaffListResponce.getData() != null) {
                            DoctorStaffPresenterImp.this.view.addingDataToList(drStaffListResponce.getData());
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public DoctorStaffPresenterImp(DoctorStaff view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        if (this.loginDocResponce != null) {
            if (this.loginDocResponce.getData().getAddStaffLimit() != null) {
                try {
                    view.settingCountLimit(Integer.parseInt(this.loginDocResponce.getData().getAddStaffLimit()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            view.settingDoctorName(this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName(), this.loginDocResponce.getData().get_id());
        }
    }

    public void deactivateRequest(final String Status, String DrId, final int pos, String StaffId, final String buttonText) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().DrStaffActiveDeactiveDelete(Status, this.loginDocResponce.getData().get_id(), StaffId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                class C12271 implements AlertCallBackInterface {
                    C12271() {
                    }

                    public void neutralClick() {
                        DoctorStaffPresenterImp.this.view.activateDeactivate(Status, pos);
                    }
                }

                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (DoctorStaffPresenterImp.this.view != null) {
                        DoctorStaffPresenterImp.this.view.hideDialog();
                        DoctorStaffPresenterImp.this.view.showError(DoctorStaffPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject drStaffListResponce) {
                    if (DoctorStaffPresenterImp.this.view != null) {
                        DoctorStaffPresenterImp.this.view.hideDialog();
                        if (drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                            DialogPopUps.confirmationPopUp(DoctorStaffPresenterImp.this.view, DoctorStaffPresenterImp.this.view.getResources().getString(C1020R.string.alert), DoctorStaffPresenterImp.this.view.getResources().getString(C1020R.string.staff_member) + " " + buttonText + " " + DoctorStaffPresenterImp.this.view.getResources().getString(C1020R.string.successfully), new C12271());
                        }
                    }
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void gotoChat(String email, String id, int pos, String deviceToken) {
    }

    public void fetchMyStaff(String drId, String staffId) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getDrStaffList(drId, staffId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12292());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void chatNowApi(final String drID, final DrStaffListObject drStaffListObject) {
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
                    if (DoctorStaffPresenterImp.this.view != null) {
                        DoctorStaffPresenterImp.this.view.hideDialog();
                        DoctorStaffPresenterImp.this.view.showError(DoctorStaffPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DoctorChatResponse contactUsResponse) {
                    if (DoctorStaffPresenterImp.this.view != null) {
                        DoctorStaffPresenterImp.this.view.hideDialog();
                        ChatUserForDb chatUserForDb = new ChatUserForDb();
                        chatUserForDb.set_id(drID);
                        chatUserForDb.setAddress("");
                        chatUserForDb.setApiKey(contactUsResponse.getData().getApiKey());
                        chatUserForDb.setBio("");
                        chatUserForDb.setCity("");
                        chatUserForDb.setCode(drStaffListObject.getCode());
                        chatUserForDb.setCount(0);
                        chatUserForDb.setUserType(3);
                        chatUserForDb.setMobile(drStaffListObject.getMobile());
                        chatUserForDb.setDeviceToken(drStaffListObject.getDeviceToken());
                        chatUserForDb.setSessionData(contactUsResponse.getData().getSessionData());
                        chatUserForDb.setProfilePic(drStaffListObject.getProfilePic());
                        chatUserForDb.setGroupId(contactUsResponse.getData().getGroupId());
                        chatUserForDb.setDOB("");
                        chatUserForDb.setEmail(drStaffListObject.getEmail());
                        chatUserForDb.setFirstName("");
                        chatUserForDb.setLastName("");
                        chatUserForDb.setQualification("");
                        chatUserForDb.setSpeciality(drStaffListObject.getDesignation());
                        chatUserForDb.setName(drStaffListObject.getName());
                        chatUserForDb.setTokenData(contactUsResponse.getData().getTokenData());
                        chatUserForDb.setGender(drStaffListObject.getGender());
                        SharedPreff.saveChatDbDetail(chatUserForDb);
                        DoctorStaffPresenterImp.this.view.gotoChatScreen(chatUserForDb);
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void gotoStaffDetail(DrStaffListObject drStaffListObject) {
        if (this.view != null) {
            this.view.gotoStaffDetail(drStaffListObject);
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
