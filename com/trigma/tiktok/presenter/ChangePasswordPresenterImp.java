package com.trigma.tiktok.presenter;

import android.text.TextUtils;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.ChangePassword;
import com.trigma.tiktok.model.DocAcceptReject;
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

public class ChangePasswordPresenterImp implements ChangePasswordPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private ChangePassword view;

    class C12041 implements Observer<DocAcceptReject> {

        class C12031 implements AlertCallBackInterface {
            C12031() {
            }

            public void neutralClick() {
                ChangePasswordPresenterImp.this.view.passwordChanged();
            }
        }

        C12041() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (ChangePasswordPresenterImp.this.view != null) {
                ChangePasswordPresenterImp.this.view.hideDialog();
                ChangePasswordPresenterImp.this.view.showError(ChangePasswordPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject contactUsResponse) {
            if (ChangePasswordPresenterImp.this.view != null) {
                ChangePasswordPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    DialogPopUps.confirmationPopUp(ChangePasswordPresenterImp.this.view, ChangePasswordPresenterImp.this.view.getResources().getString(C1020R.string.password_reset), ChangePasswordPresenterImp.this.view.getResources().getString(C1020R.string.you_have_successfully_reset_your_password_please_proceed_to_login), new C12031());
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                    ChangePasswordPresenterImp.this.view.showError(ChangePasswordPresenterImp.this.view.getResources().getString(C1020R.string.please_enter_correct_old_password));
                } else {
                    ChangePasswordPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    public ChangePasswordPresenterImp(ChangePassword view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.loginDocResponce = SharedPreff.getStaffLoginResponse();
        } else {
            this.loginDocResponce = SharedPreff.getLoginResponce();
        }
    }

    public void changePassword(String oldPaswd, String newPaswd, String confirmPaswd) {
        if (TextUtils.isEmpty(oldPaswd)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.enter_your_old_password));
        }
        if (TextUtils.isEmpty(newPaswd)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.enter_your_new_password));
        }
        if (TextUtils.isEmpty(confirmPaswd)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.enter_your_confirm_password));
        } else if (oldPaswd.trim().length() < 6 || oldPaswd.trim().length() > 15) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.password_length_cannot_be_less_than_6_characters));
        } else if (newPaswd.trim().length() < 6 || newPaswd.trim().length() > 15) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.password_length_cannot_be_less_than_6_characters));
        } else if (confirmPaswd.trim().length() < 6 || confirmPaswd.trim().length() > 15) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.password_length_cannot_be_less_than_6_characters));
        } else if (!newPaswd.equalsIgnoreCase(confirmPaswd)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.password_and_confirm_passowrd_must_be_same));
        } else if (this.loginDocResponce == null) {
        } else {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.view.showProgressDialog();
                this.mSubscriptionCounter = this.netWorkingService.getAPI().changePassword(this.loginDocResponce.getData().get_id(), oldPaswd, newPaswd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12041());
            } else if (this.view != null) {
                this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
            }
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
}
