package com.trigma.tiktok.presenter;

import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.StaffDetail;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DoctorChatResponse;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.StaffDoctorListObject;
import com.trigma.tiktok.model.StaffDoctorListResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StaffDetailPresenterImp implements StaffDetailPresenter {
    private ArrayList<String> doc_list = new ArrayList();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private StaffDetail view;

    class C12962 implements Observer<StaffDoctorListResponse> {
        C12962() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (StaffDetailPresenterImp.this.view != null) {
                StaffDetailPresenterImp.this.view.hideDialog();
            }
        }

        public void onNext(StaffDoctorListResponse staffDoctorListResponse) {
            Log.e("staffDoctorListResponse", new Gson().toJson((Object) staffDoctorListResponse));
            if (StaffDetailPresenterImp.this.view != null) {
                StaffDetailPresenterImp.this.view.hideDialog();
                if (!staffDoctorListResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    StaffDetailPresenterImp.this.view.settingDocList(StaffDetailPresenterImp.this.doc_list);
                } else if (staffDoctorListResponse.getData().size() > 0) {
                    StaffDetailPresenterImp.this.doc_list.clear();
                    for (int a = 0; a < staffDoctorListResponse.getData().size(); a++) {
                        if (((StaffDoctorListObject) staffDoctorListResponse.getData().get(a)).getStaffStatus() == 1) {
                            StaffDetailPresenterImp.this.doc_list.add(((StaffDoctorListObject) staffDoctorListResponse.getData().get(a)).getName());
                        }
                    }
                    StaffDetailPresenterImp.this.view.settingDocList(StaffDetailPresenterImp.this.doc_list);
                }
            }
        }
    }

    public StaffDetailPresenterImp(StaffDetail view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }

    public void chatNowApi(final String drID, final DrStaffListObject drStaffListObject) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().drDrcreatetoken(SharedPreff.getLoginResponce().getData().get_id(), drID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DoctorChatResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (StaffDetailPresenterImp.this.view != null) {
                        StaffDetailPresenterImp.this.view.hideDialog();
                        StaffDetailPresenterImp.this.view.showError(StaffDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DoctorChatResponse contactUsResponse) {
                    if (StaffDetailPresenterImp.this.view != null) {
                        StaffDetailPresenterImp.this.view.hideDialog();
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
                        chatUserForDb.setGender("");
                        SharedPreff.saveChatDbDetail(chatUserForDb);
                        StaffDetailPresenterImp.this.view.gotoChatScreen(chatUserForDb);
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void fetchStaffDocList(String staffID) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getStaffDocList(staffID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12962());
        }
    }

    public void showDrList(ArrayList<String> list) {
        if (this.view != null) {
            this.view.showDrList(list);
        }
    }
}
