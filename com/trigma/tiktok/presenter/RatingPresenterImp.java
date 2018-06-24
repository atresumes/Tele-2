package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.Rating;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RatingPresenterImp implements RatingPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private Rating view;

    class C12861 implements Observer<DocAcceptReject> {
        C12861() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (RatingPresenterImp.this.view != null) {
                RatingPresenterImp.this.view.hideDialog();
                RatingPresenterImp.this.view.showError(RatingPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject docAcceptReject) {
            if (RatingPresenterImp.this.view != null) {
                RatingPresenterImp.this.view.hideDialog();
                if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    RatingPresenterImp.this.view.ratingDone();
                } else {
                    RatingPresenterImp.this.view.showError(docAcceptReject.getError());
                }
            }
        }
    }

    public RatingPresenterImp(Rating view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void giveRating(String comments, String rating, String docId) {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.view.showProgressDialog();
                this.mSubscriptionCounter = this.netWorkingService.getAPI().userRating(this.loginDocResponce.getData().get_id(), docId, rating, comments).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12861());
            }
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
