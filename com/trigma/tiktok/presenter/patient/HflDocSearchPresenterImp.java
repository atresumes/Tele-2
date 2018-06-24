package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.HflDocSearch;
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

public class HflDocSearchPresenterImp implements HflDocSearchPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private String userID;
    private HflDocSearch view;

    class C13301 implements Observer<DrSearchNameResponse> {
        C13301() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (HflDocSearchPresenterImp.this.view != null) {
                HflDocSearchPresenterImp.this.view.hideDialog();
                HflDocSearchPresenterImp.this.view.showError(HflDocSearchPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrSearchNameResponse drSearchNameResponse) {
            if (HflDocSearchPresenterImp.this.view != null) {
                HflDocSearchPresenterImp.this.view.hideDialog();
                if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    HflDocSearchPresenterImp.this.view.settingAdapter(drSearchNameResponse.getData());
                } else if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    HflDocSearchPresenterImp.this.view.showError(HflDocSearchPresenterImp.this.view.getActivity().getString(C1020R.string.no_doctor_found_please_try_later));
                    HflDocSearchPresenterImp.this.view.settingEmptyData();
                    HflDocSearchPresenterImp.this.view.emptyField();
                } else {
                    HflDocSearchPresenterImp.this.view.showError(HflDocSearchPresenterImp.this.view.getActivity().getString(C1020R.string.no_doctor_found_please_try_later));
                    HflDocSearchPresenterImp.this.view.settingEmptyData();
                }
            }
        }
    }

    public HflDocSearchPresenterImp(HflDocSearch view, NetWorkingService netWorkingService) {
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
            this.mSubscriptionCounter = this.netWorkingService.getAPI().hflSearchByNameAndZip(SharedPreff.getLat(), SharedPreff.getLng(), this.userID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13301());
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
