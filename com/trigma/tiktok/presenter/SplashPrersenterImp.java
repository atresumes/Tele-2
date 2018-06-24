package com.trigma.tiktok.presenter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.activity.Splash;
import com.trigma.tiktok.model.DecativateResponse;
import com.trigma.tiktok.model.ForceUpdate;
import com.trigma.tiktok.model.LoginDetailPojo;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashPrersenterImp implements SplashPrersenter {
    String Version;
    private int androidVersionCode = 5;
    private int currentVersion = 5;
    private int forceUpdate = 0;
    private LoginDetailPojo loginDetailPojo;
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    PackageInfo packageinfo = null;
    private Splash view;

    class C12891 implements Observer<DecativateResponse> {

        class C12881 implements AlertCallBackInterface {
            C12881() {
            }

            public void neutralClick() {
                SharedPreff.clearDocPreff();
                Intent intent = new Intent(SplashPrersenterImp.this.view, DashBoard.class);
                intent.setFlags(268468224);
                SplashPrersenterImp.this.view.startActivity(intent);
            }
        }

        C12891() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (SplashPrersenterImp.this.view != null) {
                SplashPrersenterImp.this.view.goFurther();
            }
        }

        public void onNext(DecativateResponse decativateResponse) {
            if (SplashPrersenterImp.this.view == null) {
                return;
            }
            if (!decativateResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                SplashPrersenterImp.this.view.goFurther();
            } else if (decativateResponse.getUserStatus() == 0) {
                DialogPopUps.alertPopUp(SplashPrersenterImp.this.view, SplashPrersenterImp.this.view.getResources().getString(C1020R.string.your_account_has_been_deleted_or_deactivated_please_contact_our_support_for_more_information), SplashPrersenterImp.this.view.getResources().getString(C1020R.string.ok), new C12881());
            } else {
                SplashPrersenterImp.this.view.goFurther();
            }
        }
    }

    class C12902 implements Runnable {
        C12902() {
        }

        public void run() {
            SplashPrersenterImp.this.view.showAlertError();
        }
    }

    class C12933 implements Observer<ForceUpdate> {

        class C12911 implements AlertCallBackInterface {
            C12911() {
            }

            public void neutralClick() {
                String appPackageName = SplashPrersenterImp.this.view.getPackageName();
                try {
                    SplashPrersenterImp.this.view.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    SplashPrersenterImp.this.view.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                SplashPrersenterImp.this.view.finish();
            }
        }

        class C12922 implements AlertCallBackWithButtonsInterface {
            C12922() {
            }

            public void positiveClick() {
                SplashPrersenterImp.this.view.goFurther();
            }

            public void neutralClick() {
            }

            public void negativeClick() {
                String appPackageName = SplashPrersenterImp.this.view.getPackageName();
                try {
                    SplashPrersenterImp.this.view.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    SplashPrersenterImp.this.view.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                SplashPrersenterImp.this.view.finish();
            }
        }

        C12933() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (SplashPrersenterImp.this.view != null) {
                SplashPrersenterImp.this.view.goFurther();
            }
        }

        public void onNext(ForceUpdate decativateResponse) {
            if (SplashPrersenterImp.this.view == null) {
                return;
            }
            if (decativateResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) || decativateResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                try {
                    SplashPrersenterImp.this.androidVersionCode = Integer.parseInt(decativateResponse.getAndroidversion());
                    SplashPrersenterImp.this.forceUpdate = Integer.parseInt(decativateResponse.getAndroidFourceUpdate());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (SplashPrersenterImp.this.currentVersion >= SplashPrersenterImp.this.androidVersionCode) {
                    SplashPrersenterImp.this.view.goFurther();
                    return;
                } else if (SplashPrersenterImp.this.forceUpdate == 1) {
                    DialogPopUps.alertPopUp(SplashPrersenterImp.this.view, SplashPrersenterImp.this.view.getResources().getString(C1020R.string.please_update_your_app), SplashPrersenterImp.this.view.getResources().getString(C1020R.string.upgrade), new C12911());
                    return;
                } else {
                    DialogPopUps.showAlertWithButtons(SplashPrersenterImp.this.view, SplashPrersenterImp.this.view.getResources().getString(C1020R.string.app_name), SplashPrersenterImp.this.view.getResources().getString(C1020R.string.please_update_your_app), SplashPrersenterImp.this.view.getResources().getString(C1020R.string.not_now), SplashPrersenterImp.this.view.getResources().getString(C1020R.string.upgrade), "", false, true, new C12922());
                    return;
                }
            }
            SplashPrersenterImp.this.view.goFurther();
        }
    }

    class C12944 implements Runnable {
        C12944() {
        }

        public void run() {
            SplashPrersenterImp.this.view.showAlertError();
        }
    }

    public SplashPrersenterImp(Splash view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDetailPojo = new LoginDetailPojo();
        this.loginDocResponce = SharedPreff.getLoginResponce();
        try {
            this.packageinfo = view.getPackageManager().getPackageInfo(view.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        this.Version = "" + this.packageinfo.versionCode;
        try {
            this.currentVersion = Integer.parseInt(this.Version);
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
        }
        Log.e("packageinfo", "" + this.Version);
    }

    public void checkDeactivate() {
        if (this.loginDocResponce != null) {
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.view.showProgressDialog();
                this.mSubscription = this.netWorkingService.getAPI().userDecativate(this.loginDocResponce.getData().get_id(), SharedPreff.getDeviceToken(), Constants.DEVICE_TYPE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12891());
                return;
            }
            this.view.runOnUiThread(new C12902());
        } else if (this.view == null) {
        }
    }

    public void showForceUpdate() {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            String token = SharedPreff.getDeviceToken();
            if (TextUtils.isEmpty(token) || token.equalsIgnoreCase("")) {
                token = "123";
            }
            this.mSubscription = this.netWorkingService.getAPI().getOnlineAppVersion(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12933());
            return;
        }
        this.view.runOnUiThread(new C12944());
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }
}
