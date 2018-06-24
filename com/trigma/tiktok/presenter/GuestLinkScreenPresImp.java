package com.trigma.tiktok.presenter;

import com.facebook.appevents.AppEventsConstants;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.GuestLinkScreen;
import com.trigma.tiktok.model.GuestUrlResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GuestLinkScreenPresImp implements GuestLinkScreenPres {
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private GuestLinkScreen view;

    class C12331 implements Observer<GuestUrlResponse> {
        C12331() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (GuestLinkScreenPresImp.this.view != null) {
                GuestLinkScreenPresImp.this.view.hideDialog();
                GuestLinkScreenPresImp.this.view.showError(GuestLinkScreenPresImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(GuestUrlResponse contactUsResponse) {
            if (GuestLinkScreenPresImp.this.view != null) {
                GuestLinkScreenPresImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    GuestLinkScreenPresImp.this.view.gotoVideoCallScreen(contactUsResponse.getData());
                } else {
                    GuestLinkScreenPresImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    public GuestLinkScreenPresImp(GuestLinkScreen view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void connectNow(String url) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = new NetWorkingService(url + "/").getAPI3().getTokenForGuest(AppEventsConstants.EVENT_PARAM_VALUE_YES).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12331());
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
