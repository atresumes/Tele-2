package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.ContactUs;
import com.trigma.tiktok.model.ContactUsResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactUsPresenterImp implements ContactUsPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private ContactUs view;

    class C12111 implements Observer<ContactUsResponse> {
        C12111() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (ContactUsPresenterImp.this.view != null) {
                ContactUsPresenterImp.this.view.hideDialog();
                ContactUsPresenterImp.this.view.showProgressLayout(true, ContactUsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                ContactUsPresenterImp.this.view.showError(ContactUsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(ContactUsResponse contactUsResponse) {
            if (ContactUsPresenterImp.this.view != null) {
                ContactUsPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    ContactUsPresenterImp.this.view.showProgressLayout(false, "");
                    ContactUsPresenterImp.this.view.apiResponseSuccess(contactUsResponse);
                    return;
                }
                ContactUsPresenterImp.this.view.showProgressLayout(true, contactUsResponse.getError());
                ContactUsPresenterImp.this.view.showError(contactUsResponse.getError());
            }
        }
    }

    public ContactUsPresenterImp(ContactUs view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void callApi() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.view.showProgressLayout(true, "");
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getContactUsDeatil(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12111());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
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
