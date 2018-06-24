package com.trigma.tiktok.presenter;

import android.app.Activity;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.model.MyDoctorsResponse;
import com.trigma.tiktok.model.PrescriptionObject;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenterImp implements MainActivityPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private MainActivityView view;

    class C12471 implements Observer<DocAcceptReject> {
        C12471() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MainActivityPresenterImp.this.view != null) {
                MainActivityPresenterImp.this.view.hideDialog();
                MainActivityPresenterImp.this.view.showError(((Activity) MainActivityPresenterImp.this.view).getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject deDocAcceptReject) {
            if (MainActivityPresenterImp.this.view != null) {
                MainActivityPresenterImp.this.view.hideDialog();
                if (deDocAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    SharedPreff.clearDocPreff();
                    MainActivityPresenterImp.this.view.loginSuccessfull();
                    return;
                }
                MainActivityPresenterImp.this.view.showError(deDocAcceptReject.getError());
            }
        }
    }

    class C12482 implements Observer<PrescriptionObject> {
        C12482() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MainActivityPresenterImp.this.view != null) {
                MainActivityPresenterImp.this.view.hideDialog();
                MainActivityPresenterImp.this.view.showError(((Activity) MainActivityPresenterImp.this.view).getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(PrescriptionObject prescriptionObject) {
            if (MainActivityPresenterImp.this.view != null) {
                MainActivityPresenterImp.this.view.hideDialog();
                if (prescriptionObject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MainActivityPresenterImp.this.view.gotoPrescriptionScreen(prescriptionObject.getUrl(), prescriptionObject.getTransactionErrorsCount());
                } else {
                    MainActivityPresenterImp.this.view.showError(prescriptionObject.getError());
                }
            }
        }
    }

    class C12493 implements Observer<MyDoctorsResponse> {
        C12493() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MainActivityPresenterImp.this.view != null) {
                MainActivityPresenterImp.this.view.hideDialog();
                MainActivityPresenterImp.this.view.showError(((Activity) MainActivityPresenterImp.this.view).getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(MyDoctorsResponse myDoctorsResponse) {
            if (MainActivityPresenterImp.this.view != null) {
                MainActivityPresenterImp.this.view.hideDialog();
                if (!myDoctorsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MainActivityPresenterImp.this.view.showError(myDoctorsResponse.getError());
                } else if (myDoctorsResponse.getData().size() > 0) {
                    for (int z = 0; z < myDoctorsResponse.getData().size(); z++) {
                        if (((MyDoctorsObject) myDoctorsResponse.getData().get(z)).getStatus() == 1) {
                            MainActivityPresenterImp.this.view.isDoctorActive(true);
                            return;
                        }
                    }
                    MainActivityPresenterImp.this.view.isDoctorActive(false);
                } else {
                    MainActivityPresenterImp.this.view.isDoctorActive(false);
                }
            }
        }
    }

    public MainActivityPresenterImp(MainActivityView view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void logout() {
        String id = "";
        if (SharedPreff.getStaffLoginResponse() != null) {
            id = SharedPreff.getStaffLoginResponse().getData().get_id();
        } else {
            id = SharedPreff.getLoginResponce().getData().get_id();
        }
        if (CommonUtils.isConnectedToInternet((Activity) this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().logoutApi(id, SharedPreff.getDeviceToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12471());
        } else if (this.view != null) {
            this.view.showError(((Activity) this.view).getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void callPrescriptionApi() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet((Activity) this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getTransactionErrorsCount(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12482());
        } else if (this.view != null) {
            this.view.showError(((Activity) this.view).getResources().getString(C1020R.string.check_your_network));
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

    public void checkingForDoctors() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet((Activity) this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getPatientDoctorList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12493());
            return;
        }
        this.view.showError(((Activity) this.view).getResources().getString(C1020R.string.check_your_network));
    }
}
