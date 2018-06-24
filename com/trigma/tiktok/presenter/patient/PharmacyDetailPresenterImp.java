package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.patient.PharmacyDetail;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PharmacyDetailPresenterImp implements PharmacyDetailPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private String userID;
    private PharmacyDetail view;

    public PharmacyDetailPresenterImp(PharmacyDetail view, NetWorkingService netWorkingService) {
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

    public void selectPharmacy(String PharmacyId, final String PharmacyName, String PharmacySelect) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().addPharmacy(this.userID, PharmacyId, PharmacyName, PharmacySelect).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                class C13601 implements AlertCallBackInterface {
                    C13601() {
                    }

                    public void neutralClick() {
                        PharmacyDetailPresenterImp.this.view.pharmacySelectedSuccessful(PharmacyName);
                    }
                }

                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (PharmacyDetailPresenterImp.this.view != null) {
                        PharmacyDetailPresenterImp.this.view.hideDialog();
                        PharmacyDetailPresenterImp.this.view.showError(PharmacyDetailPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject drSearchNameResponse) {
                    if (PharmacyDetailPresenterImp.this.view != null) {
                        PharmacyDetailPresenterImp.this.view.hideDialog();
                        if (SharedPreff.getFirstPharmacySelected()) {
                            SharedPreff.saveFirstPharmacySelected(false);
                            DialogPopUps.confirmationPopUp(PharmacyDetailPresenterImp.this.view, PharmacyDetailPresenterImp.this.view.getResources().getString(C1020R.string.pharmacy_selected), PharmacyDetailPresenterImp.this.view.getResources().getString(C1020R.string.you_will_now_be_logged_out_of_tik_tok_doc), new C13601());
                            return;
                        }
                        PharmacyDetailPresenterImp.this.view.pharmacySelectedSuccessful(PharmacyName);
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }
}
