package com.trigma.tiktok.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.LoginActivity;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDetailPojo;
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

public class LoginActivityPresenterImp implements LoginActivityPresenter {
    public int isDoc = 0;
    private LoginDetailPojo loginDetailPojo;
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private LoginActivity view;

    public LoginActivityPresenterImp(LoginActivity view, NetWorkingService netWorkingService, int isDoc) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDetailPojo = new LoginDetailPojo();
        this.isDoc = isDoc;
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }

    public void checkValidation(String email, String password, String loginType) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (this.view != null) {
                this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_all_details));
            }
        } else if (CommonUtils.validateEmail(email.trim()).booleanValue()) {
            if (password.length() >= 6 && (password.length() <= 15 || !loginType.equalsIgnoreCase("M"))) {
                this.loginDetailPojo.setDeviceToken(SharedPreff.getDeviceToken());
                this.loginDetailPojo.setDeviceType(Constants.DEVICE_TYPE);
                this.loginDetailPojo.setEmail(email);
                this.loginDetailPojo.setLoginKey(password);
                this.loginDetailPojo.setUserType("" + this.isDoc);
                this.loginDetailPojo.setLoginType(loginType);
                if (this.isDoc == 3) {
                    lgoinApiCallStaff(this.loginDetailPojo, loginType);
                } else {
                    lgoinApiCall(this.loginDetailPojo, loginType);
                }
            } else if (this.view != null) {
                this.view.showToastError(this.view.getResources().getString(C1020R.string.password_length_cannot_be_less_than_6_characters));
            }
        } else if (this.view != null) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_email_id));
        }
    }

    private void lgoinApiCall(final LoginDetailPojo loginDetailPojo, final String loginType) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.mSubscription = this.netWorkingService.getAPI().loginApi(loginDetailPojo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LoginDocResponce>() {
                public void onCompleted() {
                    Log.e("lgoinApiCall", "onCompleted");
                }

                public void onError(Throwable e) {
                    Log.e("lgoinApiCall", "onError");
                    if (LoginActivityPresenterImp.this.view != null) {
                        LoginActivityPresenterImp.this.view.hideDialog();
                    }
                    LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                }

                public void onNext(final LoginDocResponce loginDocResponce) {
                    Log.e("lgoinApiCall", "onNext");
                    Log.e("lgoinApiCall", "" + new Gson().toJson((Object) loginDocResponce));
                    Log.e("msg", "" + loginDocResponce.getError());
                    if (LoginActivityPresenterImp.this.view != null) {
                        LoginActivityPresenterImp.this.view.hideDialog();
                    }
                    if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        CommonUtils.refreshingOpenTokToken(LoginActivityPresenterImp.this.view, LoginActivityPresenterImp.this.netWorkingService, loginDocResponce.getData().get_id());
                        if (loginDocResponce.getData().getLoginAllready().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
                            SharedPreff.saveLoginResponce(loginDocResponce);
                            SharedPreff.clearStaffLoginResponse();
                            Log.e("msg", GraphResponse.SUCCESS_KEY);
                            if (loginDocResponce.getData().getUserType() == 1) {
                                SharedPreff.saveDocLogin(loginDetailPojo);
                            } else if (loginDocResponce.getData().getUserType() == 3) {
                                SharedPreff.saveStaffLogin(loginDetailPojo);
                            } else {
                                SharedPreff.savePatientLogin(loginDetailPojo);
                            }
                            if (LoginActivityPresenterImp.this.view != null) {
                                LoginActivityPresenterImp.this.view.loginSuccessFull(loginDocResponce);
                                return;
                            }
                            return;
                        }
                        DialogPopUps.agreementPopUp(LoginActivityPresenterImp.this.view, LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.agreement_text), false, new AlertCallBackInterface() {
                            public void neutralClick() {
                                LoginActivityPresenterImp.this.view.showProgressDialog();
                                LoginActivityPresenterImp.this.agreeApiCall(loginDocResponce.getData().get_id(), AppEventsConstants.EVENT_PARAM_VALUE_YES, loginDocResponce);
                            }
                        });
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_901)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.please_verify_your_email_before_logging_into_tik_tok_doc));
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_601)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.email_id_already_exist_with_another_account));
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_400)) {
                        if (loginType.equalsIgnoreCase("M")) {
                            LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.email_id_does_not_exist));
                        } else {
                            LoginActivityPresenterImp.this.view.socialLogin();
                        }
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.incorrect_password));
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_801)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.tik_tok_doc_has_not_yet_verified_your_account_please_call_them_at));
                    } else {
                        Log.e("msg", "" + loginDocResponce.getError());
                        if (LoginActivityPresenterImp.this.view != null) {
                            LoginActivityPresenterImp.this.view.showError(loginDocResponce.getError());
                        }
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    private void lgoinApiCallStaff(final LoginDetailPojo loginDetailPojo, final String loginType) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.mSubscription = this.netWorkingService.getAPI().loginStaffApi(loginDetailPojo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LoginDocResponce>() {
                public void onCompleted() {
                    Log.e("lgoinApiCall", "onCompleted");
                }

                public void onError(Throwable e) {
                    Log.e("lgoinApiCall", "onError");
                    if (LoginActivityPresenterImp.this.view != null) {
                        LoginActivityPresenterImp.this.view.hideDialog();
                    }
                    LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                }

                public void onNext(final LoginDocResponce loginDocResponce) {
                    Log.e("lgoinApiCall", "onNext");
                    Log.e("lgoinApiCall", "" + new Gson().toJson((Object) loginDocResponce));
                    Log.e("msg", "" + loginDocResponce.getError());
                    if (LoginActivityPresenterImp.this.view != null) {
                        LoginActivityPresenterImp.this.view.hideDialog();
                    }
                    if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        if (loginDocResponce.getData().getLoginAllready().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
                            SharedPreff.saveStaffLoginResponse(loginDocResponce);
                            SharedPreff.clearLoginResponce();
                            Log.e("msg", GraphResponse.SUCCESS_KEY);
                            if (loginDocResponce.getData().getUserType() == 1) {
                                SharedPreff.saveDocLogin(loginDetailPojo);
                            } else if (loginDocResponce.getData().getUserType() == 3) {
                                SharedPreff.saveStaffLogin(loginDetailPojo);
                            } else {
                                SharedPreff.savePatientLogin(loginDetailPojo);
                            }
                            if (LoginActivityPresenterImp.this.view != null) {
                                LoginActivityPresenterImp.this.view.loginSuccessFull(loginDocResponce);
                                return;
                            }
                            return;
                        }
                        DialogPopUps.agreementPopUp(LoginActivityPresenterImp.this.view, LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.agreement_text), false, new AlertCallBackInterface() {
                            public void neutralClick() {
                                LoginActivityPresenterImp.this.view.showProgressDialog();
                                LoginActivityPresenterImp.this.agreeApiCall(loginDocResponce.getData().get_id(), AppEventsConstants.EVENT_PARAM_VALUE_YES, loginDocResponce);
                            }
                        });
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_901)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.please_verify_your_email_before_logging_into_tik_tok_doc));
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_601)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.email_id_already_exist_with_another_account));
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_400)) {
                        if (loginType.equalsIgnoreCase("M")) {
                            LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.email_id_does_not_exist));
                        } else {
                            LoginActivityPresenterImp.this.view.socialLogin();
                        }
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.incorrect_password));
                    } else if (loginDocResponce.getStatus().equalsIgnoreCase(Constants.STATUS_801)) {
                        LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.tik_tok_doc_has_not_yet_verified_your_account_please_call_them_at));
                    } else {
                        Log.e("msg", "" + loginDocResponce.getError());
                        if (LoginActivityPresenterImp.this.view != null) {
                            LoginActivityPresenterImp.this.view.showError(loginDocResponce.getError());
                        }
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void agreeApiCall(String userId, String status, final LoginDocResponce loginDocResponce) {
        if (!CommonUtils.isConnectedToInternet(this.view)) {
            if (this.view != null) {
                this.view.hideDialog();
            }
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        } else if (this.view != null) {
            this.mSubscription = this.netWorkingService.getAPI().userAgreement(userId, status).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (LoginActivityPresenterImp.this.view != null) {
                        LoginActivityPresenterImp.this.view.hideDialog();
                    }
                    LoginActivityPresenterImp.this.view.showError(LoginActivityPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                }

                public void onNext(DocAcceptReject docAcceptReject) {
                    if (LoginActivityPresenterImp.this.view != null) {
                        LoginActivityPresenterImp.this.view.hideDialog();
                    }
                    if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        if (loginDocResponce.getData().getUserType() == 3) {
                            SharedPreff.saveStaffLoginResponse(loginDocResponce);
                        } else {
                            SharedPreff.saveLoginResponce(loginDocResponce);
                        }
                        Log.e("msg", GraphResponse.SUCCESS_KEY);
                        if (loginDocResponce.getData().getUserType() == 1) {
                            SharedPreff.saveDocLogin(LoginActivityPresenterImp.this.loginDetailPojo);
                        } else if (loginDocResponce.getData().getUserType() == 3) {
                            SharedPreff.saveStaffLogin(LoginActivityPresenterImp.this.loginDetailPojo);
                        } else {
                            SharedPreff.savePatientLogin(LoginActivityPresenterImp.this.loginDetailPojo);
                        }
                        if (LoginActivityPresenterImp.this.view != null) {
                            LoginActivityPresenterImp.this.view.loginSuccessFull(loginDocResponce);
                            return;
                        }
                        return;
                    }
                    LoginActivityPresenterImp.this.view.showError(docAcceptReject.getError());
                }
            });
        }
    }
}
