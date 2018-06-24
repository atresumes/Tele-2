package com.trigma.tiktok.presenter.patient;

import com.facebook.appevents.AppEventsConstants;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.NearMeFragement;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.DrSearchNameResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NearMePresenterImp implements NearMePresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private String userID;
    private NearMeFragement view;

    class C13491 implements Observer<DrSearchNameResponse> {
        C13491() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (NearMePresenterImp.this.view != null) {
                NearMePresenterImp.this.view.hideDialog();
                NearMePresenterImp.this.view.showError(NearMePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DrSearchNameResponse drSearchNameResponse) {
            if (NearMePresenterImp.this.view != null) {
                NearMePresenterImp.this.view.hideDialog();
                if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    try {
                        if (drSearchNameResponse.getData().size() > 0) {
                            ArrayList<DrSearchNameObject> data = new ArrayList();
                            int z = 0;
                            while (z < drSearchNameResponse.getData().size()) {
                                if (!(((DrSearchNameObject) drSearchNameResponse.getData().get(z)).getLat().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO) || ((DrSearchNameObject) drSearchNameResponse.getData().get(z)).getLat().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO) || ((DrSearchNameObject) drSearchNameResponse.getData().get(z)).getLat().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES) || ((DrSearchNameObject) drSearchNameResponse.getData().get(z)).getLat().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES))) {
                                    data.add(drSearchNameResponse.getData().get(z));
                                }
                                z++;
                            }
                            NearMePresenterImp.this.view.settingAdapter(data);
                            return;
                        }
                        NearMePresenterImp.this.view.settingEmptyData(false);
                        NearMePresenterImp.this.view.showError(NearMePresenterImp.this.view.getActivity().getString(C1020R.string.no_doctors_found_using_this_criteria));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (drSearchNameResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    NearMePresenterImp.this.view.showError(NearMePresenterImp.this.view.getActivity().getString(C1020R.string.no_doctors_found_using_this_criteria));
                    NearMePresenterImp.this.view.settingEmptyData(false);
                } else {
                    NearMePresenterImp.this.view.showError(drSearchNameResponse.getError());
                    NearMePresenterImp.this.view.settingEmptyData(false);
                }
            }
        }
    }

    public NearMePresenterImp(NearMeFragement view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        if (this.loginDocResponce != null) {
            this.userID = this.loginDocResponce.getData().get_id();
        } else {
            this.userID = SharedPreff.getUerID();
        }
    }

    public void searchDoctor() {
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.settingEmptyData(true);
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().searchByNear(SharedPreff.getLat(), SharedPreff.getLng(), this.userID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13491());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void gotoDoctorDetail() {
    }

    public void goToDetail(DrSearchNameObject drSearchNameObject) {
        this.view.goToPatientDetail(drSearchNameObject);
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
