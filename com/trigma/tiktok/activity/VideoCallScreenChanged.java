package com.trigma.tiktok.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.nononsenseapps.filepicker.AbstractFilePickerActivity;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.Publisher.Builder;
import com.opentok.android.PublisherKit;
import com.opentok.android.PublisherKit.PublisherListener;
import com.opentok.android.Session;
import com.opentok.android.Session.SessionListener;
import com.opentok.android.Session.SignalListener;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.HomeScreenPatient;
import com.trigma.tiktok.adapter.ChatAdapter;
import com.trigma.tiktok.easyimage.DefaultCallback;
import com.trigma.tiktok.easyimage.EasyImageTikTok;
import com.trigma.tiktok.easyimage.EasyImageTikTok.ImageSource;
import com.trigma.tiktok.model.AsyncMessageData;
import com.trigma.tiktok.model.AsyncMessageSend;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.presenter.VideoCallScreenChangedPresenterImp;
import com.trigma.tiktok.presenter.VideoCallScreenPresenter;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithOptionalInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoCallScreenChanged extends BaseActivity implements SessionListener, PublisherListener, OnClickListener, SignalListener {
    public static String API_KEY = null;
    private static int FILE_CODE = 100;
    public static String GUEST = "Guest";
    public static final String GUEST_ID = "guest_userId";
    public static final int GUEST_TYPE = 2;
    public static String INVITE_URL = null;
    public static String REALAM_UNIQUE_ID = null;
    public static String REDIRECT_URL = null;
    public static String SESSION_ID = null;
    public static final String SIGNAL_TYPE = "chat";
    public static String TOKEN = null;
    public static final String TYPE_LINK = "link";
    public static final String TYPE_STRING = "string";
    public static String USER_ID = null;
    private final int MAX_NUM_SUBSCRIBERS = 2;
    private ChatAdapter chatAdapter;
    private EditText et_chat;
    private FrameLayout guest_container;
    public File imageFile;
    private ImageView img_add_user;
    private ImageView img_attatch;
    private ImageView img_endCall;
    private ImageView img_msg;
    private ImageView img_mute_on;
    private ImageView img_rotate_camera;
    private ImageView img_send;
    private boolean isCallFailed;
    private boolean isDocCalling = false;
    private boolean isSubscriberConnected = false;
    private LinearLayout listViewParent;
    private LoginDocResponce loginDocResponce;
    private Publisher mPublisher;
    private FrameLayout mPublisherViewContainer;
    private Session mSession;
    private HashMap<Stream, Subscriber> mSubscriberStreams = new HashMap();
    private FrameLayout mSubscriberViewContainer;
    private ArrayList<Subscriber> mSubscribers = new ArrayList();
    private RelativeLayout main_container;
    public NetWorkingService netWorkingService;
    private NetworkChangeReceiver networkChangeReceiver;
    private RecyclerView recyclerView;
    private VideoCallRequiredFields videoCallRequiredFields;
    private VideoCallScreenPresenter videoCallScreenPresenter;

    class C10791 implements AlertCallBackInterface {
        C10791() {
        }

        public void neutralClick() {
            if (VideoCallScreenChanged.this.isDocCalling) {
                Intent signUp = new Intent(VideoCallScreenChanged.this, HomeScreen.class);
                signUp.setFlags(268468224);
                VideoCallScreenChanged.this.startActivity(signUp);
                return;
            }
            Intent dashBoard = new Intent(VideoCallScreenChanged.this, DashBoard.class);
            dashBoard.setFlags(268468224);
            VideoCallScreenChanged.this.startActivity(dashBoard);
        }
    }

    class C10802 implements OnGlobalLayoutListener {
        C10802() {
        }

        @SuppressLint({"NewApi"})
        public void onGlobalLayout() {
            int width = VideoCallScreenChanged.this.main_container.getWidth();
            int height = VideoCallScreenChanged.this.main_container.getHeight();
            LayoutParams params1 = (LayoutParams) VideoCallScreenChanged.this.mPublisherViewContainer.getLayoutParams();
            LayoutParams params2 = (LayoutParams) VideoCallScreenChanged.this.mSubscriberViewContainer.getLayoutParams();
            params1.height = height / 2;
            params1.width = width;
            VideoCallScreenChanged.this.mPublisherViewContainer.setLayoutParams(params1);
            params2.width = width;
            params2.height = height / 2;
            VideoCallScreenChanged.this.mSubscriberViewContainer.setLayoutParams(params2);
            if (VERSION.SDK_INT >= 16) {
                VideoCallScreenChanged.this.main_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                VideoCallScreenChanged.this.main_container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
    }

    class C10813 implements AlertCallBackWithButtonsInterface {
        C10813() {
        }

        public void positiveClick() {
            if (VideoCallScreenChanged.this.mSession != null) {
                VideoCallScreenChanged.this.disconnectSession();
            }
            if (SharedPreff.getLoginResponce() == null) {
                Intent dashBoard = new Intent(VideoCallScreenChanged.this, DashBoard.class);
                dashBoard.setFlags(268468224);
                VideoCallScreenChanged.this.startActivity(dashBoard);
            } else if (VideoCallScreenChanged.this.isDocCalling) {
                signUp = new Intent(VideoCallScreenChanged.this, HomeScreen.class);
                signUp.setFlags(268468224);
                VideoCallScreenChanged.this.startActivity(signUp);
            } else if (VideoCallScreenChanged.this.isSubscriberConnected) {
                VideoCallScreenChanged.this.videoCallScreenPresenter.callDisconnected(VideoCallScreenChanged.this.videoCallRequiredFields);
            } else if (SharedPreff.getLoginResponce().getData().getStatus() == 1) {
                signUp = new Intent(VideoCallScreenChanged.this, HomeScreen.class);
                signUp.setFlags(268468224);
                VideoCallScreenChanged.this.startActivity(signUp);
            } else {
                signUp = new Intent(VideoCallScreenChanged.this, HomeScreenPatient.class);
                signUp.setFlags(268468224);
                VideoCallScreenChanged.this.startActivity(signUp);
            }
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C10824 implements AlertCallBackWithOptionalInterface {
        C10824() {
        }

        public void positiveClick() {
            EasyImageTikTok.openGallery(VideoCallScreenChanged.this, 0);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            EasyImageTikTok.openCamera(VideoCallScreenChanged.this, 0);
        }

        public void optional() {
            Intent i = new Intent(VideoCallScreenChanged.this, FilePickerActivity.class);
            i.putExtra(AbstractFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(AbstractFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
            i.putExtra(AbstractFilePickerActivity.EXTRA_MODE, 0);
            i.putExtra(AbstractFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
            VideoCallScreenChanged.this.startActivityForResult(i, VideoCallScreenChanged.FILE_CODE);
        }
    }

    class C10835 extends DefaultCallback {
        C10835() {
        }

        public void onImagePickerError(Exception e, ImageSource source, int type) {
            VideoCallScreenChanged.this.showToastError(VideoCallScreenChanged.this.getResources().getString(C1020R.string.sorry_unable_to_pick_the_image));
        }

        public void onImagePicked(File imageFilee, ImageSource source, int type) {
            VideoCallScreenChanged.this.imageFile = imageFilee;
            VideoCallScreenChanged.this.videoCallScreenPresenter.gettingLink(Part.createFormData("File", VideoCallScreenChanged.this.imageFile.getName(), RequestBody.create(MediaType.parse("*/*"), VideoCallScreenChanged.this.imageFile)), RequestBody.create(MediaType.parse("text/plain"), VideoCallScreenChanged.this.imageFile.getName()));
        }

        public void onCanceled(ImageSource source, int type) {
            if (source == ImageSource.CAMERA) {
                File photoFile = EasyImageTikTok.lastlyTakenButCanceledPhoto(VideoCallScreenChanged.this);
                if (photoFile != null) {
                    photoFile.delete();
                }
            }
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            try {
                if (CommonUtils.isConnectedToInternet(context)) {
                    if (!(VideoCallScreenChanged.this.loginDocResponce == null || VideoCallScreenChanged.this.isDocCalling)) {
                        if (VideoCallScreenChanged.this.mSession == null) {
                            VideoCallScreenChanged.this.initializeSession(VideoCallScreenChanged.API_KEY, VideoCallScreenChanged.SESSION_ID, VideoCallScreenChanged.TOKEN);
                        } else if (VideoCallScreenChanged.this.isCallFailed) {
                            if (VideoCallScreenChanged.this.isSubscriberConnected && VideoCallScreenChanged.this.isDocCalling) {
                                VideoCallScreenChanged.this.videoCallScreenPresenter.callDisconnected(VideoCallScreenChanged.this.videoCallRequiredFields);
                            }
                            if (VideoCallScreenChanged.this.mSession != null) {
                                VideoCallScreenChanged.this.disconnectSession();
                            }
                        }
                    }
                    Log.e("Internet", "Internet");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.video_screen_testing);
        Constants.CHAT_CONTEXT = this;
        if (INVITE_URL != null) {
            Log.e("INVITE_URL", INVITE_URL);
        }
        if (getIntent().hasExtra("isDocCalling")) {
            this.isDocCalling = getIntent().getBooleanExtra("isDocCalling", false);
        }
        Log.e("isDocCalling", "" + this.isDocCalling);
        this.videoCallRequiredFields = SharedPreff.getVideoCallRequireData();
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.isDocCalling && this.loginDocResponce != null) {
            GUEST = this.loginDocResponce.getData().getFirstName();
        }
        initViews();
        initializeSession(API_KEY, SESSION_ID, TOKEN);
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        showProgressDialog();
        this.networkChangeReceiver = new NetworkChangeReceiver();
        this.videoCallScreenPresenter = new VideoCallScreenChangedPresenterImp(this, this.netWorkingService);
        this.videoCallScreenPresenter.checkSubscriberConnected();
        if (!(this.loginDocResponce == null || this.isDocCalling)) {
            this.videoCallScreenPresenter.fetchFromServer(this.videoCallRequiredFields.getGroupId(), this.loginDocResponce.getData().get_id(), USER_ID);
        }
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

    public void showGuestDiconnetedPopUp() {
        DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.guest_call_disconnected_msg), new C10791());
    }

    public void goToGuestHomeScreen() {
        Intent dashBoard = new Intent(this, DashBoard.class);
        dashBoard.setFlags(268468224);
        startActivity(dashBoard);
    }

    private void initViews() {
        this.mPublisherViewContainer = (FrameLayout) findViewById(C1020R.id.publisher_container);
        this.mSubscriberViewContainer = (FrameLayout) findViewById(C1020R.id.subscriber_container);
        this.guest_container = (FrameLayout) findViewById(C1020R.id.guest_container);
        this.img_endCall = (ImageView) findViewById(C1020R.id.img_endCall);
        this.img_mute_on = (ImageView) findViewById(C1020R.id.img_mute_on);
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        this.img_attatch = (ImageView) findViewById(C1020R.id.img_attatch);
        this.main_container = (RelativeLayout) findViewById(C1020R.id.main_container);
        this.img_send = (ImageView) findViewById(C1020R.id.img_send);
        this.img_msg = (ImageView) findViewById(C1020R.id.img_msg);
        this.et_chat = (EditText) findViewById(C1020R.id.et_chat);
        this.et_chat.setHorizontallyScrolling(false);
        this.et_chat.setVerticalScrollBarEnabled(true);
        this.et_chat.setMaxLines(4);
        this.listViewParent = (LinearLayout) findViewById(C1020R.id.listViewParent);
        settingRecyclerView();
        this.img_rotate_camera = (ImageView) findViewById(C1020R.id.img_rotate_camera);
        this.img_add_user = (ImageView) findViewById(C1020R.id.img_add_user);
        if (this.loginDocResponce == null || this.isDocCalling) {
            this.img_add_user.setVisibility(8);
        } else if (this.loginDocResponce.getData().getUserType() == 1) {
            this.img_add_user.setVisibility(0);
        } else {
            this.img_add_user.setVisibility(8);
        }
        enableAllButtons(false);
        clickListners();
    }

    private void settingPublisherView(boolean settingGuest) {
        if (settingGuest) {
            this.guest_container.setVisibility(0);
            this.main_container.getViewTreeObserver().addOnGlobalLayoutListener(new C10802());
            return;
        }
        LayoutParams params1 = (LayoutParams) this.mPublisherViewContainer.getLayoutParams();
        LayoutParams params2 = (LayoutParams) this.mSubscriberViewContainer.getLayoutParams();
        params1.height = (this.deviceWidth * 45) / 100;
        params1.width = (this.deviceWidth * 45) / 100;
        this.mPublisherViewContainer.setLayoutParams(params1);
        params2.width = -1;
        params2.height = -1;
        this.mSubscriberViewContainer.setLayoutParams(params2);
        this.guest_container.setVisibility(8);
    }

    private void settingRecyclerView() {
        this.recyclerView.setHasFixedSize(true);
        if (this.loginDocResponce == null || this.isDocCalling) {
            this.chatAdapter = new ChatAdapter(this, 2, this.deviceWidth);
        } else {
            this.chatAdapter = new ChatAdapter(this, this.loginDocResponce.getData().getUserType(), this.deviceWidth);
        }
        this.recyclerView.setAdapter(this.chatAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void clickListners() {
        this.img_endCall.setOnClickListener(this);
        this.img_mute_on.setOnClickListener(this);
        this.img_rotate_camera.setOnClickListener(this);
        this.img_add_user.setOnClickListener(this);
        this.img_attatch.setOnClickListener(this);
        this.img_send.setOnClickListener(this);
        this.img_msg.setOnClickListener(this);
    }

    public void enableAllButtons(boolean val) {
        this.img_send.setEnabled(val);
        this.img_attatch.setEnabled(val);
        this.et_chat.setEnabled(val);
    }

    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.e("xxxx", "onStreamCreated PublisherKit ");
    }

    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.e("xxxx", "onStreamDropped" + stream.getStreamId() + " in session: ");
    }

    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
    }

    public void onConnected(Session session) {
        Log.e("VideoCall", "onConnected: Connected to session: " + session.getSessionId());
        hideDialog();
        enableAllButtons(true);
        this.mPublisher = new Builder(this).build();
        this.mPublisher.setPublisherListener(this);
        this.mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        this.mPublisherViewContainer.addView(this.mPublisher.getView());
        this.mSession.publish(this.mPublisher);
        if (!this.isSubscriberConnected) {
            return;
        }
        if (this.isDocCalling) {
            showGuestDiconnetedPopUp();
        } else {
            this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        }
    }

    public void onDisconnected(Session session) {
        Log.e("xxxx", "onDisconnected");
        enableAllButtons(false);
        this.mSession = null;
        if (!this.isSubscriberConnected || this.isDocCalling) {
            showGuestDiconnetedPopUp();
        } else {
            this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Constants.CHAT_CONTEXT = null;
        hideDialog();
        if (this.mSession != null) {
            disconnectSession();
        }
        unRegisteringBroadCast();
        this.videoCallScreenPresenter.unSubscribeCallbacks();
    }

    public void onBackPressed() {
    }

    public void onStreamReceived(Session session, Stream stream) {
        Log.d("", "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
        if (this.mSubscribers.size() + 1 <= 2) {
            Log.e("mSubscribers.size()", "" + this.mSubscribers.size());
            Subscriber subscriber = new Subscriber.Builder(this, stream).build();
            this.mSession.subscribe(subscriber);
            this.mSubscribers.add(subscriber);
            this.mSubscriberStreams.put(stream, subscriber);
            this.isSubscriberConnected = true;
            this.img_endCall.setEnabled(true);
            this.img_add_user.setEnabled(true);
            this.videoCallScreenPresenter.setSubscribed(true);
            if (this.mSubscribers.size() == 1) {
                subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
                this.mSubscriberViewContainer.addView(subscriber.getView());
                this.guest_container.setVisibility(8);
                settingPublisherView(false);
            } else if (this.mSubscribers.size() == 2) {
                subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
                this.guest_container.addView(subscriber.getView());
                this.guest_container.setVisibility(0);
                settingPublisherView(true);
            }
        }
    }

    public void onStreamDropped(Session session, Stream stream) {
        Log.d("", "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());
        Subscriber subscriber = (Subscriber) this.mSubscriberStreams.get(stream);
        if (subscriber != null) {
            int position = this.mSubscribers.indexOf(subscriber);
            if (position == 0) {
                try {
                    this.mSubscribers.remove(subscriber);
                    this.mSubscriberStreams.remove(stream);
                    this.mSubscriberViewContainer.removeView(subscriber.getView());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!this.isSubscriberConnected || this.isDocCalling) {
                    showGuestDiconnetedPopUp();
                } else {
                    this.videoCallScreenPresenter.callDisconnected(this.videoCallRequiredFields);
                }
            } else if (position == 1) {
                try {
                    settingPublisherView(false);
                    this.mSubscribers.remove(subscriber);
                    this.mSubscriberStreams.remove(stream);
                    this.guest_container.removeView(subscriber.getView());
                    this.guest_container.setVisibility(8);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void onError(Session session, OpentokError opentokError) {
        Log.e("11", "onError: " + opentokError.getErrorDomain() + " : " + opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());
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
        this.mSession.setSignalListener(this);
        this.mSession.connect(token);
    }

    public void onClick(View v) {
        boolean z = true;
        switch (v.getId()) {
            case C1020R.id.img_attatch:
                CommonUtils.hideKeyboard(this.et_chat, this);
                DialogPopUps.openFilePickerDialog(this, new C10824());
                return;
            case C1020R.id.img_send:
                if (!TextUtils.isEmpty(this.et_chat.getText().toString()) && !this.et_chat.getText().toString().trim().equalsIgnoreCase("")) {
                    sendMessage(this.et_chat.getText().toString(), false);
                    return;
                }
                return;
            case C1020R.id.img_rotate_camera:
                if (this.mPublisher != null) {
                    this.mPublisher.cycleCamera();
                    return;
                }
                return;
            case C1020R.id.img_endCall:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_end_the_call), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10813());
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
            case C1020R.id.img_add_user:
                shareItem(INVITE_URL);
                return;
            case C1020R.id.img_msg:
                if (this.img_msg.getContentDescription().equals(getResources().getString(C1020R.string.selected))) {
                    this.img_msg.setContentDescription(getResources().getString(C1020R.string.normal));
                    this.img_msg.setImageResource(C1020R.drawable.chat_on);
                    this.listViewParent.setVisibility(8);
                    return;
                }
                this.img_msg.setContentDescription(getResources().getString(C1020R.string.selected));
                this.img_msg.setImageResource(C1020R.drawable.chat_off);
                this.listViewParent.setVisibility(0);
                return;
            default:
                return;
        }
    }

    public void onSignalReceived(Session session, String type, String data, Connection connection) {
        Log.e("onSignalReceived", data);
        boolean remote = connection.equals(this.mSession.getConnection());
        if ("chat" == null) {
            return;
        }
        if (type.equals("chat") || type.equals("msg")) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSameUser(remote);
            try {
                JSONObject mainObject = new JSONObject(data);
                if (mainObject.has("message")) {
                    chatMessage.setLink(mainObject.getString("message"));
                    chatMessage.setMessageText(mainObject.getString("message"));
                }
                if (mainObject.has("displayName")) {
                    chatMessage.setName(mainObject.getString("displayName"));
                }
                if (mainObject.has("messageType")) {
                    chatMessage.setType(mainObject.getString("messageType"));
                }
                if (mainObject.has("displayName")) {
                    chatMessage.setName(mainObject.getString("displayName"));
                }
                if (this.loginDocResponce == null || this.isDocCalling) {
                    chatMessage.setUser_type(2);
                    showMessage(chatMessage, remote);
                }
                chatMessage.setUser_type(this.loginDocResponce.getData().getUserType());
                showMessage(chatMessage, remote);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showMessage(ChatMessage chatMessage, boolean remote) {
        if (this.chatAdapter != null) {
            this.chatAdapter.addingMessage(chatMessage);
            this.recyclerView.scrollToPosition(this.chatAdapter.getItemCount() - 1);
            AsyncMessageSend asyncMessageSend = new AsyncMessageSend();
            AsyncMessageData asyncMessageData = new AsyncMessageData();
            asyncMessageData.setGroupId(this.videoCallRequiredFields.getGroupId());
            asyncMessageData.setMessage(chatMessage.getMessageText());
            asyncMessageData.setMessageId("");
            asyncMessageData.setMessageType(chatMessage.getType());
            asyncMessageSend.setBatchPush(AppEventsConstants.EVENT_PARAM_VALUE_YES);
            asyncMessageSend.setGroupId(this.videoCallRequiredFields.getGroupId());
            if (this.loginDocResponce == null || this.isDocCalling) {
                asyncMessageData.setDisplayName(GUEST);
                asyncMessageData.setFrom(GUEST_ID);
                asyncMessageSend.setPushUserId(GUEST_ID);
            } else {
                asyncMessageData.setDisplayName(this.loginDocResponce.getData().getFirstName());
                asyncMessageData.setFrom(this.loginDocResponce.getData().get_id());
                asyncMessageSend.setPushUserId(this.loginDocResponce.getData().get_id());
                if (remote) {
                    asyncMessageData.setFrom(this.loginDocResponce.getData().get_id());
                    asyncMessageSend.setPushUserId(this.loginDocResponce.getData().get_id());
                } else {
                    asyncMessageData.setFrom(USER_ID);
                    asyncMessageSend.setPushUserId(USER_ID);
                }
            }
            ArrayList<AsyncMessageData> Data = new ArrayList();
            Data.add(asyncMessageData);
            asyncMessageSend.setData(Data);
            if (remote) {
                this.videoCallScreenPresenter.sendMsgToServer(asyncMessageSend, chatMessage, USER_ID);
            } else if (this.loginDocResponce != null && !this.isDocCalling) {
                this.videoCallScreenPresenter.saveChat(chatMessage, REALAM_UNIQUE_ID);
            }
        }
    }

    public void saveChatMessage(ChatMessage chatMessage, String userID) {
        if (this.loginDocResponce != null && !this.isDocCalling) {
            this.videoCallScreenPresenter.saveChat(chatMessage, userID);
        }
    }

    public void sendMessage(String data, boolean isLink) {
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("msgId", "");
            mainObject.put("groupId", this.videoCallRequiredFields.getGroupId());
            mainObject.put("readStatus", false);
            if (this.loginDocResponce == null || this.isDocCalling) {
                mainObject.put("fromId", GUEST_ID);
                if (isLink) {
                    mainObject.put("messageType", "link");
                } else {
                    mainObject.put("messageType", "text");
                    data = GUEST + ":-\n" + data;
                }
                mainObject.put("displayName", GUEST);
            } else {
                mainObject.put("fromId", this.loginDocResponce.getData().get_id());
                if (isLink) {
                    mainObject.put("messageType", "link");
                } else {
                    mainObject.put("messageType", "text");
                    data = this.loginDocResponce.getData().getFirstName() + ":-\n" + data;
                }
                mainObject.put("displayName", this.loginDocResponce.getData().getFirstName());
            }
            mainObject.put("message", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String messgae_sent = mainObject.toString();
        Log.d("messgae_sent", messgae_sent);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setLink(data);
        chatMessage.setMessageText(data);
        chatMessage.setSameUser(true);
        if (this.loginDocResponce == null || this.isDocCalling) {
            chatMessage.setName(GUEST);
            chatMessage.setUser_type(2);
        } else {
            chatMessage.setName(this.loginDocResponce.getData().getFirstName());
            chatMessage.setUser_type(this.loginDocResponce.getData().getUserType());
        }
        if (isLink) {
            chatMessage.setType("link");
        } else {
            chatMessage.setType("string");
        }
        this.mSession.sendSignal("chat", messgae_sent);
        this.et_chat.setText("");
    }

    public void showChatFromDb(ArrayList<ChatMessage> messageList) {
        if (this.chatAdapter != null) {
            this.chatAdapter.addingWholeList(messageList);
            this.recyclerView.scrollToPosition(this.chatAdapter.getItemCount() - 1);
        }
    }

    public void showChatFromServer(ArrayList<ChatMessage> messageList) {
        if (this.chatAdapter != null) {
            this.chatAdapter.addingWholeList(messageList);
            this.recyclerView.scrollToPosition(this.chatAdapter.getItemCount() - 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CODE && resultCode == -1) {
            for (Uri uri : Utils.getSelectedFilesFromResult(data)) {
                File file = Utils.getFileForUri(uri);
                Log.e("FIle", "" + file);
                Log.e("selected_img_path", "" + new File(uri.getPath()));
                Log.e("mime_type", "" + CommonUtils.getMimeType(uri.toString()));
                if (CommonUtils.isFormatSupported(file.toString())) {
                    File fileee = Utils.getFileForUri(uri);
                    this.videoCallScreenPresenter.gettingLink(Part.createFormData("File", fileee.getName(), RequestBody.create(MediaType.parse("*/*"), fileee)), RequestBody.create(MediaType.parse("text/plain"), fileee.getName()));
                } else {
                    showError(getResources().getString(C1020R.string.format_not_supported_please_choose_another_one));
                }
            }
        }
        EasyImageTikTok.handleActivityResult(requestCode, resultCode, data, this, new C10835());
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
        disconnectSession();
        Intent signUp = new Intent(this, Rating.class);
        signUp.setFlags(268468224);
        startActivity(signUp);
    }

    public void showPrescriptionDialog(final int status) {
        DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.do_you_need_to_write_a_prescription_for_your_patient), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new AlertCallBackWithButtonsInterface() {
            public void positiveClick() {
                VideoCallScreenChanged.this.disconnectSession();
                Constants.PRESCRIPTION_URL = VideoCallScreenChanged.this.videoCallRequiredFields.getUrl();
                Intent signUp = new Intent(VideoCallScreenChanged.this, PatientPrescription.class);
                signUp.setFlags(268468224);
                VideoCallScreenChanged.this.startActivity(signUp);
            }

            public void neutralClick() {
            }

            public void negativeClick() {
                VideoCallScreenChanged.this.gotoHomeScreen(status);
            }
        });
    }

    public void setCallFailed(boolean v) {
        this.isCallFailed = v;
    }

    private void shareItem(String link) {
        String msg1 = getResources().getString(C1020R.string.you_have_been_invited_to_join_a_tik_tok_doc_concierge_telemedicine_video_call) + "\n\n" + getResources().getString(C1020R.string.please_follow_these_instructions) + "\n1. " + getResources().getString(C1020R.string.Copy_this_link) + "\n" + REDIRECT_URL + "\n2. " + getResources().getString(C1020R.string.open_the_tik_tok_doc_app) + "\n\n" + getResources().getString(C1020R.string.please_follow_these_instructions_for_joining_the_video_call_on_a_computer) + "\n1. " + getResources().getString(C1020R.string.Copy_this_link) + "\n" + link + "\n2. " + getResources().getString(C1020R.string.you_will_be_automatically_connected_into_this_video_call) + "\n\n" + getResources().getString(C1020R.string.if_you_do_not_have_the_tik_tok_doc_app_on_your_mobile_device_you_can_download_it_here);
        String final_string = "";
        final_string = msg1 + "\n\n" + getResources().getString(C1020R.string.ios_device) + " " + getResources().getString(C1020R.string.iphone_app_link) + "\n\n" + getResources().getString(C1020R.string.android_device) + " " + getResources().getString(C1020R.string.android_app_link) + "\n\n\n\n" + getResources().getString(C1020R.string.sincerely_health4life_admin);
        Intent share = new Intent("android.intent.action.SEND");
        share.setType("text/plain");
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList();
            for (ResolveInfo resolveInfo : resInfo) {
                String packageName = resolveInfo.activityInfo.packageName;
                Intent targetedShareIntent = new Intent("android.intent.action.SEND");
                targetedShareIntent.setType("text/plain");
                if ("com.twitter.android".equals(packageName)) {
                    targetedShareIntent.putExtra("android.intent.extra.TEXT", final_string);
                } else if ("com.google.android.gm".equals(packageName)) {
                    targetedShareIntent.putExtra("android.intent.extra.SUBJECT", getResources().getString(C1020R.string.please_join_my_secure_video_visit_through_tik_tok_doc));
                    targetedShareIntent.putExtra("android.intent.extra.TEXT", final_string);
                } else if ("com.android.email".equals(packageName)) {
                    targetedShareIntent.putExtra("android.intent.extra.SUBJECT", getResources().getString(C1020R.string.please_join_my_secure_video_visit_through_tik_tok_doc));
                    targetedShareIntent.putExtra("android.intent.extra.TEXT", final_string);
                } else {
                    targetedShareIntent.putExtra("android.intent.extra.TEXT", final_string);
                }
                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);
            }
            Intent chooserIntent = Intent.createChooser((Intent) targetedShareIntents.remove(0), "Share text to...");
            chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) targetedShareIntents.toArray(new Parcelable[0]));
            startActivityForResult(chooserIntent, 0);
        }
    }

    private void disconnectSession() {
        if (this.mSession != null) {
            if (this.mSubscribers.size() > 0) {
                Iterator it = this.mSubscribers.iterator();
                while (it.hasNext()) {
                    Subscriber subscriber = (Subscriber) it.next();
                    if (subscriber != null) {
                        this.mSession.unsubscribe(subscriber);
                        subscriber.destroy();
                    }
                }
            }
            if (this.mPublisher != null) {
                this.mPublisherViewContainer.removeView(this.mPublisher.getView());
                this.mSession.unpublish(this.mPublisher);
                this.mPublisher.destroy();
                this.mPublisher = null;
            }
            this.mSession.disconnect();
            this.mPublisher = null;
        }
    }
}
