package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.MyDoctors;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MyDoctorsResponse;
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

public class MyDoctorsPresenterImp implements MyDoctorsPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    String nameShown = "";
    private NetWorkingService netWorkingService;
    String patient_name = "";
    String userID = "";
    private MyDoctors view;

    class C13391 implements Observer<MyDoctorsResponse> {
        C13391() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MyDoctorsPresenterImp.this.view != null) {
                MyDoctorsPresenterImp.this.view.hideDialog();
                MyDoctorsPresenterImp.this.view.showError(MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(MyDoctorsResponse myDoctorsResponse) {
            if (MyDoctorsPresenterImp.this.view != null) {
                MyDoctorsPresenterImp.this.view.hideDialog();
                if (myDoctorsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MyDoctorsPresenterImp.this.view.setDoctors(myDoctorsResponse.getData());
                } else {
                    MyDoctorsPresenterImp.this.view.showError(myDoctorsResponse.getError());
                }
            }
        }
    }

    public MyDoctorsPresenterImp(MyDoctors view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            this.userID = this.loginDocResponce.getData().get_id();
            try {
                this.patient_name = this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }

    public void loadDoctors() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getPatientDoctorList(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13391());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void addNewDoctor() {
        this.view.goToDoctorSearch();
    }

    public void gotoDocBio(DrSearchNameObject drSearchNameObject) {
        if (this.view != null) {
            this.view.gotoDocBio(drSearchNameObject);
        }
    }

    public void sendRequestToDoc(String DrID, String Email, String DrName, String DeviceToken, String name, String deviceType, final int pos) {
        if (name.contains(this.view.getResources().getString(C1020R.string.dr))) {
            this.nameShown = name;
        } else {
            this.nameShown = this.view.getResources().getString(C1020R.string.dr) + " " + name;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().makeRequestToDoctor(this.userID, DrID, DeviceToken, deviceType, Email, DrName, this.patient_name).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {

                class C13401 implements AlertCallBackInterface {
                    C13401() {
                    }

                    public void neutralClick() {
                        MyDoctorsPresenterImp.this.view.requestSent(pos);
                    }
                }

                class C13412 implements AlertCallBackInterface {
                    C13412() {
                    }

                    public void neutralClick() {
                    }
                }

                class C13423 implements AlertCallBackInterface {
                    C13423() {
                    }

                    public void neutralClick() {
                    }
                }

                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (MyDoctorsPresenterImp.this.view != null) {
                        MyDoctorsPresenterImp.this.view.hideDialog();
                        MyDoctorsPresenterImp.this.view.showError(MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DocAcceptReject contactUsResponse) {
                    if (MyDoctorsPresenterImp.this.view != null) {
                        MyDoctorsPresenterImp.this.view.hideDialog();
                        if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                            DialogPopUps.confirmationPopUp(MyDoctorsPresenterImp.this.view.getActivity(), MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.request_sent), MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.you_can_self_schedule_your_video_visits_with) + " " + MyDoctorsPresenterImp.this.nameShown + " " + MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.once_they_accept_your_request), new C13401());
                        } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_410)) {
                            DialogPopUps.confirmationPopUp(MyDoctorsPresenterImp.this.view.getActivity(), MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.alert), MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.request_has_already_been_sent), new C13412());
                        } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_409)) {
                            DialogPopUps.confirmationPopUp(MyDoctorsPresenterImp.this.view.getActivity(), MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.alert), MyDoctorsPresenterImp.this.view.getResources().getString(C1020R.string.request_has_already_been_sent), new C13423());
                        } else {
                            MyDoctorsPresenterImp.this.view.showError(contactUsResponse.getError());
                        }
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }
}
