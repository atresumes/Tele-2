package com.trigma.tiktok.fcm;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.FcmToken;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.SharedPreff;
import org.greenrobot.eventbus.EventBus;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenService extends FirebaseInstanceIdService {
    private LoginDocResponce loginDocResponce;
    public NetWorkingService netWorkingService;

    class C11491 implements Observer<DocAcceptReject> {
        C11491() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(DocAcceptReject contactUsResponse) {
            Log.e("sendRegistration", new Gson().toJson((Object) contactUsResponse));
        }
    }

    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("notificationToken", refreshedToken);
        SharedPreff.saveDeviceToken(refreshedToken);
        FcmToken fcmToken = new FcmToken();
        fcmToken.setFcmToken(refreshedToken);
        EventBus.getDefault().post(fcmToken);
        this.loginDocResponce = SharedPreff.getLoginResponce();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this)) {
            sendRegistrationToServer(refreshedToken);
        }
    }

    private void sendRegistrationToServer(String refreshedToken) {
        this.netWorkingService.getAPI().userUpdateToken(this.loginDocResponce.getData().get_id(), refreshedToken).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C11491());
    }
}
