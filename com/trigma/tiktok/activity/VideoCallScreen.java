package com.trigma.tiktok.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.Publisher.Builder;
import com.opentok.android.PublisherKit;
import com.opentok.android.PublisherKit.PublisherListener;
import com.opentok.android.Session;
import com.opentok.android.Session.SessionListener;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.HomeScreenPatient;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.presenter.VideoCallScreenPresenter;
import com.trigma.tiktok.presenter.VideoCallScreenPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;

public class VideoCallScreen extends BaseActivity implements SessionListener, PublisherListener, OnClickListener {
    public static String API_KEY = null;
    public static String SESSION_ID = null;
    public static String TOKEN = null;
    private ImageView img_endCall;
    private ImageView img_mute_on;
    private ImageView img_rotate_camera;
    private boolean isCallFailed;
    private boolean isSubscriberConnected = false;
    private Publisher mPublisher;
    private FrameLayout mPublisherViewContainer;
    private Session mSession;
    private Subscriber mSubscriber;
    private FrameLayout mSubscriberViewContainer;
    public NetWorkingService netWorkingService;
    private NetworkChangeReceiver networkChangeReceiver;
    private VideoCallRequiredFields videoCallRequiredFields;
    private VideoCallScreenPresenter videoCallScreenPresenter;

    class C10771 implements AlertCallBackWithButtonsInterface {
        C10771() {
        }

        public void positiveClick() {
            if (VideoCallScreen.this.mSession != null) {
                VideoCallScreen.this.mSession.disconnect();
            }
            if (VideoCallScreen.this.isSubscriberConnected) {
                VideoCallScreen.this.videoCallScreenPresenter.callDisconnected(VideoCallScreen.this.videoCallRequiredFields);
            } else if (SharedPreff.getLoginResponce().getData().getStatus() == 1) {
                signUp = new Intent(VideoCallScreen.this, HomeScreen.class);
                signUp.setFlags(268468224);
                VideoCallScreen.this.startActivity(signUp);
            } else {
                signUp = new Intent(VideoCallScreen.this, HomeScreenPatient.class);
                signUp.setFlags(268468224);
                VideoCallScreen.this.startActivity(signUp);
            }
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            try {
                if (CommonUtils.isConnectedToInternet(context)) {
                    if (VideoCallScreen.this.mSession == null) {
                        VideoCallScreen.this.initializeSession(VideoCallScreen.API_KEY, VideoCallScreen.SESSION_ID, VideoCallScreen.TOKEN);
                    } else if (VideoCallScreen.this.isCallFailed) {
                        if (VideoCallScreen.this.isSubscriberConnected) {
                            VideoCallScreen.this.videoCallScreenPresenter.callDisconnected(VideoCallScreen.this.videoCallRequiredFields);
                        }
                        if (VideoCallScreen.this.mSession != null) {
                            VideoCallScreen.this.mSession.disconnect();
                        }
                    }
                    Log.e("Internet", "Internet");
                } else if (VideoCallScreen.this.mSession != null) {
                    VideoCallScreen.this.mSession.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.doc_video_call_screen);
        this.videoCallRequiredFields = SharedPreff.getVideoCallRequireData();
        initViews();
        initializeSession(API_KEY, SESSION_ID, TOKEN);
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        showProgressDialog();
        this.networkChangeReceiver = new NetworkChangeReceiver();
        this.videoCallScreenPresenter = new VideoCallScreenPresenterImp(this, this.netWorkingService);
        this.videoCallScreenPresenter.checkSubscriberConnected();
        registerBroadCast();
    }

    private void registerBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(this.networkChangeReceiver, intentFilter);
    }

    private void unRegisteringBroadCast() {
        try {
            unregisterReceiver(this.networkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        this.mPublisherViewContainer = (FrameLayout) findViewById(C1020R.id.publisher_container);
        this.mSubscriberViewContainer = (FrameLayout) findViewById(C1020R.id.subscriber_container);
        this.img_endCall = (ImageView) findViewById(C1020R.id.img_endCall);
        this.img_mute_on = (ImageView) findViewById(C1020R.id.img_mute_on);
        this.img_rotate_camera = (ImageView) findViewById(C1020R.id.img_rotate_camera);
        clickListners();
    }

    private void clickListners() {
        this.img_endCall.setOnClickListener(this);
        this.img_mute_on.setOnClickListener(this);
        this.img_rotate_camera.setOnClickListener(this);
    }

    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
    }

    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.e("xxxx", "onStreamDropped" + stream.getStreamId() + " in session: ");
        if (this.isSubscriberConnected) {
            this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        }
    }

    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        showToastError(opentokError.getMessage());
        if (this.isSubscriberConnected) {
            this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        }
    }

    public void onConnected(Session session) {
        Log.e("VideoCall", "onConnected: Connected to session: " + session.getSessionId());
        hideDialog();
        this.mPublisher = new Builder(this).build();
        this.mPublisher.setPublisherListener(this);
        this.mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        this.mPublisherViewContainer.addView(this.mPublisher.getView());
        this.mSession.publish(this.mPublisher);
        if (this.isSubscriberConnected) {
            this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        }
    }

    public void onDisconnected(Session session) {
        this.mPublisher = null;
        this.mSubscriber = null;
        if (this.mSession != null) {
            this.mSession.disconnect();
        }
        this.mSession = null;
        if (this.isSubscriberConnected) {
            this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        hideDialog();
        if (this.mSession != null) {
            this.mSession.disconnect();
        }
        unRegisteringBroadCast();
        this.videoCallScreenPresenter.unSubscribeCallbacks();
    }

    public void onBackPressed() {
    }

    public void onStreamReceived(Session session, Stream stream) {
        Log.d("", "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
        if (this.mSubscriber == null) {
            this.mSubscriber = new Subscriber.Builder(this, stream).build();
            this.mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            this.mSession.subscribe(this.mSubscriber);
            this.mSubscriberViewContainer.addView(this.mSubscriber.getView());
            this.isSubscriberConnected = true;
            this.img_endCall.setEnabled(true);
            this.videoCallScreenPresenter.setSubscribed(true);
        }
    }

    public void onStreamDropped(Session session, Stream stream) {
        Log.d("", "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());
        if (this.mSubscriber != null) {
            this.mSubscriber = null;
            this.mSubscriberViewContainer.removeAllViews();
        }
        this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        if (this.mSession != null) {
            this.mSession.disconnect();
        }
    }

    public void onError(Session session, OpentokError opentokError) {
        Log.e("", "onError: " + opentokError.getErrorDomain() + " : " + opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());
        showToastError(opentokError.getMessage());
        if (this.isSubscriberConnected) {
            this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        }
        if (this.mSession != null) {
            this.mSession.disconnect();
        }
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error);
    }

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    protected void onPause() {
        super.onPause();
        if (this.mSession != null) {
            this.mSession.onPause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.mSession != null) {
            this.mSession.onResume();
        }
    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        this.mSession = new Session.Builder(this, apiKey, sessionId).build();
        this.mSession.setSessionListener(this);
        this.mSession.connect(token);
    }

    public void onClick(View v) {
        boolean z = true;
        switch (v.getId()) {
            case C1020R.id.img_rotate_camera:
                if (this.mPublisher != null) {
                    this.mPublisher.cycleCamera();
                    return;
                }
                return;
            case C1020R.id.img_endCall:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_end_the_call), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10771());
                return;
            case C1020R.id.img_mute_on:
                if (this.mPublisher != null) {
                    if (this.img_mute_on.getContentDescription().equals(getResources().getString(C1020R.string.normal))) {
                        this.img_mute_on.setContentDescription(getResources().getString(C1020R.string.selected));
                        this.img_mute_on.setImageResource(C1020R.drawable.mute_icon);
                    } else {
                        this.img_mute_on.setContentDescription(getResources().getString(C1020R.string.normal));
                        this.img_mute_on.setImageResource(C1020R.drawable.mute_off);
                    }
                    Publisher publisher = this.mPublisher;
                    if (this.mPublisher.getPublishAudio()) {
                        z = false;
                    }
                    publisher.setPublishAudio(z);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void gotoHomeScreen(int status) {
        if (status == 1) {
            Intent signUp = new Intent(this, HomeScreen.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
            return;
        }
        signUp = new Intent(this, HomeScreenPatient.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    public void gotoRatingScreen() {
        Intent signUp = new Intent(this, Rating.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    public void showPrescriptionDialog(final int status) {
        DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.do_you_need_to_write_a_prescription_for_your_patient), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new AlertCallBackWithButtonsInterface() {
            public void positiveClick() {
                Constants.PRESCRIPTION_URL = VideoCallScreen.this.videoCallRequiredFields.getUrl();
                Intent signUp = new Intent(VideoCallScreen.this, PatientPrescription.class);
                signUp.setFlags(268468224);
                VideoCallScreen.this.startActivity(signUp);
            }

            public void neutralClick() {
            }

            public void negativeClick() {
                VideoCallScreen.this.gotoHomeScreen(status);
            }
        });
    }

    public void setCallFailed(boolean v) {
        this.isCallFailed = v;
    }
}
