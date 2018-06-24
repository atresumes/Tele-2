package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.ChatScreen;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.adapter.MessagesAdapter;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.presenter.MessagesScreenPres;
import com.trigma.tiktok.presenter.MessagesScreenPresImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MessagesScreen extends BaseFragment implements OnClickListener {
    private Activity activity;
    private View appointment_error_msg;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    LoginDocResponce loginDocResponce;
    private MessagesAdapter messagesAdapter;
    private MessagesScreenPres messagesScreenPres;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    private TextView title_name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.messages_screen, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.recyclerView = (RecyclerView) screen.findViewById(C1020R.id.recyclerView);
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.appointment_error_msg = screen.findViewById(C1020R.id.appointment_error_msg);
        this.img_error = (ImageView) screen.findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.currently_no_chat);
        clickListners();
    }

    private void settingRecyclerView() {
        this.messagesAdapter = new MessagesAdapter(this.messagesScreenPres, this, this.loginDocResponce.getData().getUserType());
        this.recyclerView.setAdapter(this.messagesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setVisibility(8);
    }

    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        this.messagesScreenPres.fetchMessageList(this.loginDocResponce.getData().getUserType());
    }

    private void clickListners() {
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
    }

    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if (Constants.REFRESH_MESSAGE_USER.equalsIgnoreCase(event)) {
            this.messagesScreenPres.fetchMessageList(this.loginDocResponce.getData().getUserType());
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.loginDocResponce = SharedPreff.getStaffLoginResponse();
        } else {
            this.loginDocResponce = SharedPreff.getLoginResponce();
        }
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.messagesScreenPres = new MessagesScreenPresImp(this, this.netWorkingService);
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.messagings));
        settingRecyclerView();
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this.activity, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this.activity, error);
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.messagesScreenPres.unSubscribeCallbacks();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                if (this.loginDocResponce == null) {
                    return;
                }
                if (this.loginDocResponce.getData().getUserType() == 0) {
                    PatientMainActivity.openDrawer();
                    return;
                } else {
                    MainActivity.openDrawer();
                    return;
                }
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            default:
                return;
        }
    }

    public void settingAdapter(ArrayList<MessageUserObject> user) {
        if (user.size() > 0) {
            this.appointment_error_msg.setVisibility(8);
            this.recyclerView.setVisibility(0);
            this.messagesAdapter.addingList(user);
            return;
        }
        this.recyclerView.setVisibility(8);
        this.appointment_error_msg.setVisibility(0);
        this.img_error.setImageResource(C1020R.drawable.currently_no_chat);
    }

    public void goToChatScreen(MessageUserObject messageUserObject, String uniqueId, boolean isFetchFromServer) {
        ChatScreen.TOKEN = messageUserObject.getTokenData();
        ChatScreen.API_KEY = messageUserObject.getApiKey();
        ChatScreen.SESSION_ID = messageUserObject.getSessionData();
        ChatScreen.USER_ID = uniqueId;
        if (SharedPreff.getStaffLoginResponse() != null) {
            ChatScreen.REALAM_UNIQUE_ID = SharedPreff.getStaffLoginResponse().getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + uniqueId;
        } else {
            ChatScreen.REALAM_UNIQUE_ID = SharedPreff.getLoginResponce().getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + uniqueId;
        }
        ChatScreen.isFetchfromServer = isFetchFromServer;
        SharedPreff.savingChatDetailObject(messageUserObject);
        startActivity(new Intent(this.activity, ChatScreen.class));
    }
}
