package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.NameZipFragment;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.DrSearchNameResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NameZipPresenterImp implements NameZipPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private String userID;
    private NameZipFragment view;

    class C13481 implements Observer<DrSearchNameResponse> {
        C13481() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (NameZipPresenterImp.this.view != null) {
                NameZipPresenterImp.this.view.hideDialog();
                NameZipPresenterImp.this.view.showError(NameZipPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrSearchNameResponse drSearchNameResponse) {
            if (NameZipPresenterImp.this.view != null) {
                NameZipPresenterImp.this.view.hideDialog();
                if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    NameZipPresenterImp.this.view.settingAdapter(drSearchNameResponse.getData());
                } else if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    NameZipPresenterImp.this.view.showError(NameZipPresenterImp.this.view.getActivity().getString(C1020R.string.no_doctors_found_using_this_criteria));
                    NameZipPresenterImp.this.view.settingEmptyData();
                    NameZipPresenterImp.this.view.emptyField();
                } else {
                    NameZipPresenterImp.this.view.showError(drSearchNameResponse.getError());
                    NameZipPresenterImp.this.view.settingEmptyData();
                }
            }
        }
    }

    public NameZipPresenterImp(NameZipFragment view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
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
            this.mSubscriptionCounter = this.netWorkingService.getAPI().searchByNameAndZip(code, SharedPreff.getLat(), SharedPreff.getLng(), this.userID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13481());
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
