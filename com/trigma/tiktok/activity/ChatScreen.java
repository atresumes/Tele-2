package com.trigma.tiktok.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.nononsenseapps.filepicker.AbstractFilePickerActivity;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Session.Builder;
import com.opentok.android.Session.SessionListener;
import com.opentok.android.Session.SignalListener;
import com.opentok.android.Stream;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.adapter.ChatAdapter;
import com.trigma.tiktok.easyimage.DefaultCallback;
import com.trigma.tiktok.easyimage.EasyImageTikTok;
import com.trigma.tiktok.easyimage.EasyImageTikTok.ImageSource;
import com.trigma.tiktok.model.AsyncMessageData;
import com.trigma.tiktok.model.AsyncMessageSend;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.presenter.ChatScreenPresenter;
import com.trigma.tiktok.presenter.ChatScreenPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithOptionalInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.io.File;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatScreen extends BaseActivity implements SessionListener, SignalListener, OnClickListener {
    public static String API_KEY = null;
    private static int FILE_CODE = 100;
    private static final String LOG_TAG = ChatScreen.class.getSimpleName();
    public static String REALAM_UNIQUE_ID = null;
    public static String SESSION_ID = null;
    public static final String SIGNAL_TYPE = "chat";
    public static String TOKEN = null;
    public static final String TYPE_LINK = "link";
    public static final String TYPE_STRING = "string";
    public static String USER_ID = null;
    public static boolean isFetchfromServer = false;
    String DOC_TYPE = "application/msword";
    String JPEG = "image/jpeg";
    String PDF = "application/pdf";
    String PNG = "image/png";
    String VIDEO = "video/mp4";
    private ChatAdapter chatAdapter;
    private ChatScreenPresenter chatScreenPresenter;
    private EditText et_chat;
    public File imageFile;
    private ImageView img_attatch;
    private ImageView img_back;
    private ImageView img_send;
    private ImageView img_slider;
    LoginDocResponce loginDocResponce;
    private Session mSession;
    private MessageUserObject messageUserObject;
    public NetWorkingService netWorkingService;
    private RelativeLayout parent;
    private RecyclerView recyclerView;
    private TextView title_name;
    private TextView tv_connect_status;

    class C10291 implements OnTouchListener {
        C10291() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            CommonUtils.hideKeyboard(ChatScreen.this.et_chat, ChatScreen.this);
            return false;
        }
    }

    class C10302 implements AlertCallBackWithOptionalInterface {
        C10302() {
        }

        public void positiveClick() {
            EasyImageTikTok.openGallery(ChatScreen.this, 0);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            EasyImageTikTok.openCamera(ChatScreen.this, 0);
        }

        public void optional() {
            Intent i = new Intent(ChatScreen.this, FilePickerActivity.class);
            i.putExtra(AbstractFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(AbstractFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
            i.putExtra(AbstractFilePickerActivity.EXTRA_MODE, 0);
            i.putExtra(AbstractFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
            ChatScreen.this.startActivityForResult(i, ChatScreen.FILE_CODE);
        }
    }

    class C10313 extends DefaultCallback {
        C10313() {
        }

        public void onImagePickerError(Exception e, ImageSource source, int type) {
            ChatScreen.this.showToastError(ChatScreen.this.getResources().getString(C1020R.string.sorry_unable_to_pick_the_image));
        }

        public void onImagePicked(File imageFilee, ImageSource source, int type) {
            ChatScreen.this.imageFile = imageFilee;
            Log.e("imageFile", "" + ChatScreen.this.imageFile);
            ChatScreen.this.chatScreenPresenter.gettingLink(Part.createFormData("File", ChatScreen.this.imageFile.getName(), RequestBody.create(MediaType.parse("*/*"), ChatScreen.this.imageFile)), RequestBody.create(MediaType.parse("text/plain"), ChatScreen.this.imageFile.getName()));
        }

        public void onCanceled(ImageSource source, int type) {
            if (source == ImageSource.CAMERA) {
                File photoFile = EasyImageTikTok.lastlyTakenButCanceledPhoto(ChatScreen.this);
                if (photoFile != null) {
                    photoFile.delete();
                }
            }
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DEVICE WIDTH", "" + this.deviceWidth);
        Log.e("DEVICE HEIGHT", "" + this.deviceHeight);
        Log.e("TOKEN", "" + TOKEN);
        Constants.CHAT_CONTEXT = this;
        setContentView((int) C1020R.layout.chat_screen);
        this.messageUserObject = SharedPreff.getChatDetailObject();
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.loginDocResponce = SharedPreff.getStaffLoginResponse();
        } else {
            this.loginDocResponce = SharedPreff.getLoginResponce();
        }
        initViews();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.chatScreenPresenter = new ChatScreenPresenterImp(this, this.netWorkingService);
        if (isFetchfromServer) {
            this.chatScreenPresenter.fetchFromServer(this.messageUserObject.getGroupId(), this.loginDocResponce.getData().get_id(), USER_ID);
        } else {
            this.chatScreenPresenter.fetchFromServer(this.messageUserObject.getGroupId(), this.loginDocResponce.getData().get_id(), USER_ID);
        }
        initializeSession(API_KEY, SESSION_ID, TOKEN);
    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        Log.d(LOG_TAG, "Initializing Session");
        this.mSession = new Builder(this, apiKey, sessionId).build();
        this.mSession.setSessionListener(this);
        this.mSession.setSignalListener(this);
        this.mSession.connect(token);
    }

    public void hideKeyBaord() {
        CommonUtils.hideKeyboard(this.et_chat, this);
    }

    private void initViews() {
        this.et_chat = (EditText) findViewById(C1020R.id.et_chat);
        this.et_chat.setHorizontallyScrolling(false);
        this.et_chat.setVerticalScrollBarEnabled(true);
        this.et_chat.setMaxLines(4);
        this.parent = (RelativeLayout) findViewById(C1020R.id.parent);
        this.img_attatch = (ImageView) findViewById(C1020R.id.img_attatch);
        this.img_send = (ImageView) findViewById(C1020R.id.img_send);
        this.img_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.tv_connect_status = (TextView) findViewById(C1020R.id.tv_connect_status);
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_slider.setVisibility(4);
        this.title_name.setText(this.messageUserObject.getName());
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        clickListners();
        enableAllButtons(false);
        settingRecyclerView();
    }

    private void settingRecyclerView() {
        this.recyclerView.setHasFixedSize(true);
        this.chatAdapter = new ChatAdapter(this, this.loginDocResponce.getData().getUserType(), this.deviceWidth);
        this.recyclerView.setAdapter(this.chatAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void clickListners() {
        this.img_send.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.img_attatch.setOnClickListener(this);
        this.recyclerView.setOnTouchListener(new C10291());
    }

    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
        if (this.mSession != null) {
            this.mSession.onPause();
        }
    }

    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if (this.mSession != null) {
            this.mSession.onResume();
        }
    }

    public void onConnected(Session session) {
        enableAllButtons(true);
        this.tv_connect_status.setText(getResources().getString(C1020R.string.connected));
        this.tv_connect_status.setTextColor(ContextCompat.getColor(this, C1020R.color.white));
    }

    protected void onDestroy() {
        super.onDestroy();
        Constants.CHAT_CONTEXT = null;
        if (this.mSession != null) {
            this.mSession.disconnect();
        }
    }

    public void onDisconnected(Session session) {
        enableAllButtons(false);
        this.tv_connect_status.setText(getResources().getString(C1020R.string.connecting));
        this.tv_connect_status.setTextColor(ContextCompat.getColor(this, C1020R.color.disconnecting_color));
        Log.e(LOG_TAG, "Session Disconnected");
    }

    public void onStreamReceived(Session session, Stream stream) {
        Log.e(LOG_TAG, "onStreamReceived");
    }

    public void onStreamDropped(Session session, Stream stream) {
        Log.e(LOG_TAG, "onStreamDropped");
    }

    public void onError(Session session, OpentokError opentokError) {
        logOpenTokError(opentokError);
    }

    public void onSignalReceived(Session session, String type, String data, Connection connection) {
        Log.e("onSignalReceived", data);
        boolean remote = connection.equals(this.mSession.getConnection());
        if ("chat" != null && type.equals("chat")) {
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
                chatMessage.setUser_type(this.loginDocResponce.getData().getUserType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showMessage(chatMessage, remote);
        }
    }

    public void showMessage(ChatMessage chatMessage, boolean remote) {
        if (this.chatAdapter != null) {
            this.chatAdapter.addingMessage(chatMessage);
            this.recyclerView.scrollToPosition(this.chatAdapter.getItemCount() - 1);
            AsyncMessageSend asyncMessageSend = new AsyncMessageSend();
            AsyncMessageData asyncMessageData = new AsyncMessageData();
            asyncMessageData.setGroupId(this.messageUserObject.getGroupId());
            asyncMessageData.setDisplayName(this.loginDocResponce.getData().getFirstName());
            if (remote) {
                asyncMessageData.setFrom(this.loginDocResponce.getData().get_id());
                asyncMessageSend.setPushUserId(this.loginDocResponce.getData().get_id());
            } else {
                asyncMessageData.setFrom(this.messageUserObject.getId());
                asyncMessageSend.setPushUserId(this.messageUserObject.getId());
            }
            asyncMessageData.setMessage(chatMessage.getMessageText());
            asyncMessageData.setMessageId("");
            asyncMessageData.setMessageType(chatMessage.getType());
            asyncMessageSend.setBatchPush(AppEventsConstants.EVENT_PARAM_VALUE_YES);
            asyncMessageSend.setGroupId(this.messageUserObject.getGroupId());
            ArrayList<AsyncMessageData> Data = new ArrayList();
            Data.add(asyncMessageData);
            asyncMessageSend.setData(Data);
            if (remote) {
                this.chatScreenPresenter.sendMsgToServer(asyncMessageSend, chatMessage, USER_ID);
            } else {
                this.chatScreenPresenter.saveChat(chatMessage, REALAM_UNIQUE_ID);
            }
        }
    }

    public void saveChatMessage(ChatMessage chatMessage, String userID) {
        this.chatScreenPresenter.saveChat(chatMessage, userID);
    }

    public void sendMessage(String data, boolean isLink) {
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("msgId", "");
            mainObject.put("groupId", this.messageUserObject.getGroupId());
            mainObject.put("fromId", this.loginDocResponce.getData().get_id());
            if (isLink) {
                mainObject.put("messageType", "link");
            } else {
                mainObject.put("messageType", "text");
                data = this.loginDocResponce.getData().getFirstName() + ":-\n" + data;
            }
            mainObject.put("message", data);
            mainObject.put("displayName", this.loginDocResponce.getData().getFirstName());
            mainObject.put("readStatus", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String messgae_sent = mainObject.toString();
        Log.d("messgae_sent", messgae_sent);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setLink(data);
        chatMessage.setName(this.loginDocResponce.getData().getFirstName());
        chatMessage.setMessageText(data);
        chatMessage.setSameUser(true);
        chatMessage.setUser_type(this.loginDocResponce.getData().getUserType());
        if (isLink) {
            chatMessage.setType("link");
        } else {
            chatMessage.setType("string");
        }
        this.mSession.sendSignal("chat", messgae_sent);
        this.et_chat.setText("");
    }

    private void logOpenTokError(OpentokError opentokError) {
        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.parent:
                CommonUtils.hideKeyboard(this.et_chat, this);
                return;
            case C1020R.id.img_header_back:
                finish();
                return;
            case C1020R.id.img_attatch:
                CommonUtils.hideKeyboard(this.et_chat, this);
                DialogPopUps.openFilePickerDialog(this, new C10302());
                return;
            case C1020R.id.img_send:
                if (!TextUtils.isEmpty(this.et_chat.getText().toString()) && !this.et_chat.getText().toString().trim().equalsIgnoreCase("")) {
                    sendMessage(this.et_chat.getText().toString(), false);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void enableAllButtons(boolean val) {
        this.img_send.setEnabled(val);
        this.img_attatch.setEnabled(val);
        this.et_chat.setEnabled(val);
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
                    this.chatScreenPresenter.gettingLink(Part.createFormData("File", fileee.getName(), RequestBody.create(MediaType.parse("*/*"), fileee)), RequestBody.create(MediaType.parse("text/plain"), fileee.getName()));
                } else {
                    showError(getResources().getString(C1020R.string.format_not_supported_please_choose_another_one));
                }
            }
        }
        EasyImageTikTok.handleActivityResult(requestCode, resultCode, data, this, new C10313());
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
}
