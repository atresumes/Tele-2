package com.trigma.tiktok.presenter;

import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.model.LandingPage;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DashBoardPresenterImp implements DashBoardPresenter {
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private DashBoard view;

    class C12261 implements Observer<LandingPage> {
        C12261() {
        }

        public void onCompleted() {
            Log.e("onCompleted", "onCompleted");
        }

        public void onError(Throwable e) {
            if (DashBoardPresenterImp.this.view != null) {
                DashBoardPresenterImp.this.view.hideDialog();
            }
            DashBoardPresenterImp.this.view.showError(DashBoardPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            Log.e("onError", "onError");
        }

        public void onNext(LandingPage landingPage) {
            if (DashBoardPresenterImp.this.view != null) {
                DashBoardPresenterImp.this.view.hideDialog();
            }
            Log.e("onNext", "" + new Gson().toJson((Object) landingPage));
            DashBoardPresenterImp.this.view.apiResponceRecieved(landingPage);
            if (landingPage.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                SharedPreff.saveLandingPageData(landingPage);
            }
        }
    }

    public DashBoardPresenterImp(DashBoard view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void callApiToGetLink() {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.mSubscription = this.netWorkingService.getAPI().getLandingPage().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12261());
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
