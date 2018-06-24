package com.trigma.tiktok.presenter.patient;

import android.util.Log;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.DoctorFragment;
import com.trigma.tiktok.model.DrSearchCodeResponse;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DoctorFragmentPresenterImp implements DoctorFragmentPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private String userID = "";
    private DoctorFragment view;

    class C13291 implements Observer<DrSearchCodeResponse> {
        C13291() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (DoctorFragmentPresenterImp.this.view != null) {
                DoctorFragmentPresenterImp.this.view.hideDialog();
                DoctorFragmentPresenterImp.this.view.showError(DoctorFragmentPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrSearchCodeResponse drSearchNameResponse) {
            if (DoctorFragmentPresenterImp.this.view != null) {
                DoctorFragmentPresenterImp.this.view.hideDialog();
                if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    try {
                        Log.e("profileRes", "" + drSearchNameResponse.getData().getProfilePic());
                        ArrayList<DrSearchNameObject> dataList = new ArrayList();
                        DrSearchNameObject drSearchNameObject = new DrSearchNameObject();
                        drSearchNameObject.setDrCode(drSearchNameResponse.getData().getDrCode());
                        drSearchNameObject.setAddress(drSearchNameResponse.getData().getAddress());
                        drSearchNameObject.setBio(drSearchNameResponse.getData().getBio());
                        drSearchNameObject.setCode(drSearchNameResponse.getData().getCode());
                        drSearchNameObject.setDeviceToken(drSearchNameResponse.getData().getDeviceToken());
                        drSearchNameObject.setDistance(drSearchNameResponse.getData().getDistance());
                        drSearchNameObject.setEmail(drSearchNameResponse.getData().getEmail());
                        drSearchNameObject.setDrId(drSearchNameResponse.getData().getId());
                        drSearchNameObject.setLanguages(drSearchNameResponse.getData().getLanguages());
                        drSearchNameObject.setMobile(drSearchNameResponse.getData().getMobile());
                        drSearchNameObject.setName(drSearchNameResponse.getData().getName());
                        drSearchNameObject.setProfile(drSearchNameResponse.getData().getProfilePic());
                        drSearchNameObject.setProfilePic(drSearchNameResponse.getData().getProfilePic());
                        drSearchNameObject.setQualification(drSearchNameResponse.getData().getQualification());
                        drSearchNameObject.setSpeciality(drSearchNameResponse.getData().getSpeciality());
                        drSearchNameObject.setZipcode(drSearchNameResponse.getData().getZipcode());
                        dataList.add(drSearchNameObject);
                        DoctorFragmentPresenterImp.this.view.settingAdapter(dataList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        DoctorFragmentPresenterImp.this.view.showError(DoctorFragmentPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                        DoctorFragmentPresenterImp.this.view.settingEmptyData();
                    }
                } else if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    DoctorFragmentPresenterImp.this.view.showError(DoctorFragmentPresenterImp.this.view.getActivity().getString(C1020R.string.no_doctors_found_using_this_criteria));
                    DoctorFragmentPresenterImp.this.view.settingEmptyData();
                    DoctorFragmentPresenterImp.this.view.emptyField();
                } else {
                    DoctorFragmentPresenterImp.this.view.showError(drSearchNameResponse.getError());
                    DoctorFragmentPresenterImp.this.view.settingEmptyData();
                }
            }
        }
    }

    public DoctorFragmentPresenterImp(DoctorFragment view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            this.userID = this.loginDocResponce.getData().get_id();
        } else {
            this.userID = SharedPreff.getUerID();
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

    public void searchDoctor(String code) {
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().searchByDoctorCode(code, SharedPreff.getLat(), SharedPreff.getLng(), this.userID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13291());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void gotoDoctorDetail() {
    }

    public void goToDetail(DrSearchNameObject drSearchNameObject) {
        this.view.goToPatientDetail(drSearchNameObject);
    }
}
