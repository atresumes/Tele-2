package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.ScheduleDetail;
import com.trigma.tiktok.model.DrSchedulePatientListResponce;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScheduleDetailPresntorImp implements ScheduleDetailPresntor {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private ScheduleDetail view;

    class C12871 implements Observer<DrSchedulePatientListResponce> {
        C12871() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (ScheduleDetailPresntorImp.this.view != null) {
                ScheduleDetailPresntorImp.this.view.hideDialog();
            }
            ScheduleDetailPresntorImp.this.view.emptyData();
            ScheduleDetailPresntorImp.this.view.showError(ScheduleDetailPresntorImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
        }

        public void onNext(DrSchedulePatientListResponce drSchedulePatientListResponce) {
            if (ScheduleDetailPresntorImp.this.view != null) {
                ScheduleDetailPresntorImp.this.view.hideDialog();
                if (drSchedulePatientListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    ScheduleDetailPresntorImp.this.view.addingDataToAdapter(drSchedulePatientListResponce.getPatient());
                    return;
                }
                ScheduleDetailPresntorImp.this.view.showError(drSchedulePatientListResponce.getError());
                ScheduleDetailPresntorImp.this.view.emptyData();
            }
        }
    }

    public ScheduleDetailPresntorImp(ScheduleDetail view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void callApi(String Dr_id, String DrScheduleId, String ScheduleId, String From, String To) {
        callApiToFetchDetail(Dr_id, DrScheduleId, ScheduleId, From, To);
    }

    private void callApiToFetchDetail(String dr_id, String drScheduleId, String scheduleId, String from, String to) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().drSchedulePatientList(dr_id, drScheduleId, from, to, scheduleId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12871());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
            this.view = null;
        }
    }
}
