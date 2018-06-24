package com.trigma.tiktok.presenter;

import android.text.TextUtils;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.ForgotPassword;
import com.trigma.tiktok.model.ForgotPswdResponce;
import com.trigma.tiktok.model.LoginDetailPojo;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForgotPswdPresenterImp implements ForgotPswdPresenter {
    private LoginDetailPojo loginDetailPojo = new LoginDetailPojo();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private ForgotPassword view;

    class C12321 implements Observer<ForgotPswdResponce> {

        class C12311 implements AlertCallBackInterface {
            C12311() {
            }

            public void neutralClick() {
                ForgotPswdPresenterImp.this.view.succesMessage();
            }
        }

        C12321() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (ForgotPswdPresenterImp.this.view != null) {
                ForgotPswdPresenterImp.this.view.hideDialog();
            }
            ForgotPswdPresenterImp.this.view.showError(ForgotPswdPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
        }

        public void onNext(ForgotPswdResponce forgotPswdResponce) {
            if (ForgotPswdPresenterImp.this.view != null) {
                ForgotPswdPresenterImp.this.view.hideDialog();
            }
            if (forgotPswdResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                DialogPopUps.confirmationPopUp(ForgotPswdPresenterImp.this.view, ForgotPswdPresenterImp.this.view.getResources().getString(C1020R.string.confirm), ForgotPswdPresenterImp.this.view.getResources().getString(C1020R.string.a_new_password_has_been_sent_to_your_email_address), new C12311());
            } else if (forgotPswdResponce.getStatus().equalsIgnoreCase(Constants.STATUS_400)) {
                ForgotPswdPresenterImp.this.view.showError(ForgotPswdPresenterImp.this.view.getResources().getString(C1020R.string.email_id_does_not_exist));
            } else if (forgotPswdResponce.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                ForgotPswdPresenterImp.this.view.showError(ForgotPswdPresenterImp.this.view.getResources().getString(C1020R.string.inactive_user));
            } else if (forgotPswdResponce.getStatus().equalsIgnoreCase(Constants.STATUS_601)) {
                ForgotPswdPresenterImp.this.view.showError(ForgotPswdPresenterImp.this.view.getResources().getString(C1020R.string.email_id_already_exist_with_another_account));
            } else {
                ForgotPswdPresenterImp.this.view.showError(forgotPswdResponce.getError());
            }
        }
    }

    public ForgotPswdPresenterImp(ForgotPassword view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void validateEmailAndApiCall(String email) {
        if (TextUtils.isEmpty(email)) {
            this.view.showError(this.view.getResources().getString(C1020R.string.please_enter_the_email_id));
        } else if (CommonUtils.validateEmail(email).booleanValue()) {
            forgotApicall(email);
        } else {
            this.view.showError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_email_id));
            this.view.setEmailEmpty();
        }
    }

    private void forgotApicall(String email) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().forgotPasswordApi(email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12321());
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
    }
}
