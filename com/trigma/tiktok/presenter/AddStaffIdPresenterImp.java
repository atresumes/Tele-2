package com.trigma.tiktok.presenter;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.AddStaffId;
import com.trigma.tiktok.model.AddStaffResponse;
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

public class AddStaffIdPresenterImp implements AddStaffIdPresenter {
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private AddStaffId view;

    class C12001 implements Observer<AddStaffResponse> {
        C12001() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (AddStaffIdPresenterImp.this.view != null) {
                AddStaffIdPresenterImp.this.view.hideDialog();
                AddStaffIdPresenterImp.this.view.showError(AddStaffIdPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(final AddStaffResponse drStaffListResponce) {
            if (AddStaffIdPresenterImp.this.view != null) {
                AddStaffIdPresenterImp.this.view.hideDialog();
                if (drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    DialogPopUps.confirmationPopUp(AddStaffIdPresenterImp.this.view, AddStaffIdPresenterImp.this.view.getResources().getString(C1020R.string.confirm), AddStaffIdPresenterImp.this.view.getResources().getString(C1020R.string.staff_member_added_successfully), new AlertCallBackInterface() {
                        public void neutralClick() {
                            AddStaffIdPresenterImp.this.view.staffAdded(drStaffListResponce.getUserId());
                        }
                    });
                } else if (drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                    AddStaffIdPresenterImp.this.view.showError(AddStaffIdPresenterImp.this.view.getResources().getString(C1020R.string.please_enter_a_valid_staffid));
                } else if (drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_400)) {
                    AddStaffIdPresenterImp.this.view.showErrorWithCallBack(AddStaffIdPresenterImp.this.view.getResources().getString(C1020R.string.staff_member_already_exist_with_this_email_id));
                } else {
                    AddStaffIdPresenterImp.this.view.showError(AddStaffIdPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                }
            }
        }
    }

    public AddStaffIdPresenterImp(AddStaffId view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
    }

    public void addStaffWithID(String staffID, String staffUserId, String DrName, String DrID) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().staffWithID(staffID, this.loginDocResponce.getData().get_id(), staffUserId, this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12001());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }
}
