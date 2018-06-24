package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorSearch;
import com.trigma.tiktok.activity.patient.HomeScreenPatient;
import com.trigma.tiktok.activity.patient.SelectPharmacy;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.presenter.SplashPrersenter;
import com.trigma.tiktok.presenter.SplashPrersenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.LocationTrackerClass;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class Splash extends AppCompatActivity {
    public LocationTrackerClass locationTrackerClass;
    private LoginDocResponce loginDocResponce;
    public NetWorkingService netWorkingService;
    Observer observer = new C10701();
    public SplashPrersenter splashPrersenter;
    private Subscription subscription;

    class C10701 implements Observer {
        C10701() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object o) {
            String token = SharedPreff.getDeviceToken();
            if (TextUtils.isEmpty(token) || token.equalsIgnoreCase("")) {
                Splash.this.goFurther();
                return;
            }
            Log.e("token", token);
            Splash.this.splashPrersenter.showForceUpdate();
        }
    }

    class C10712 implements AlertCallBackInterface {
        C10712() {
        }

        public void neutralClick() {
            Splash.this.finish();
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.splash);
        this.loginDocResponce = SharedPreff.getLoginResponce();
        this.locationTrackerClass = new LocationTrackerClass(this);
        this.locationTrackerClass.startLocationTracking();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.splashPrersenter = new SplashPrersenterImp(this, this.netWorkingService);
    }

    public void onBackPressed() {
        this.subscription.unsubscribe();
        super.onBackPressed();
    }

    protected void onPause() {
        this.subscription.unsubscribe();
        super.onPause();
    }

    protected void onResume() {
        this.subscription = Observable.timer(3, TimeUnit.SECONDS).subscribe(this.observer);
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.locationTrackerClass.stopLocationTracking();
        this.splashPrersenter.unSubscribeCallbacks();
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error);
    }

    public void showAlertError() {
        DialogPopUps.alertPopUp(this, getResources().getString(C1020R.string.check_your_network), getResources().getString(C1020R.string.ok), new C10712());
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void goFurther() {
        this.loginDocResponce = SharedPreff.getLoginResponce();
        Intent signUp;
        if (SharedPreff.getStaffLoginResponse() != null) {
            CommonUtils.refreshingOpenTokToken(this, this.netWorkingService, SharedPreff.getStaffLoginResponse().getData().get_id());
            signUp = new Intent(this, StaffHomeScreen.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
        } else if (this.loginDocResponce != null) {
            Log.e("loginDocResponce", new Gson().toJson(this.loginDocResponce));
            CommonUtils.refreshingOpenTokToken(this, this.netWorkingService, this.loginDocResponce.getData().get_id());
            if (this.loginDocResponce.getData().getUserType() == 1) {
                signUp = new Intent(this, HomeScreen.class);
                signUp.setFlags(268468224);
                startActivity(signUp);
            } else if (this.loginDocResponce.getData().getUserType() == 3) {
                signUp = new Intent(this, StaffHomeScreen.class);
                signUp.setFlags(268468224);
                startActivity(signUp);
            } else if (this.loginDocResponce.getData().getDrRequest() == 0) {
                signUp = new Intent(this, DoctorSearch.class);
                signUp.setFlags(268468224);
                signUp.putExtra(Constants.SHOW_BACK, false);
                startActivity(signUp);
                SharedPreff.saveFirstTimePharmacySelect(true);
                SharedPreff.saveFirstPharmacySelected(true);
            } else if (this.loginDocResponce.getData().getPharmacyName().equalsIgnoreCase("") || TextUtils.isEmpty(this.loginDocResponce.getData().getPharmacyName())) {
                Intent selectPharmacy = new Intent(this, SelectPharmacy.class);
                selectPharmacy.setFlags(268468224);
                selectPharmacy.putExtra(Constants.SHOW_BACK, false);
                startActivity(selectPharmacy);
            } else {
                signUp = new Intent(this, HomeScreenPatient.class);
                signUp.setFlags(268468224);
                startActivity(signUp);
            }
        } else {
            startActivity(new Intent(this, DashBoard.class));
            finish();
        }
    }
}
