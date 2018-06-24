package com.trigma.tiktok.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.model.GuestUrlObject;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.presenter.GuestLinkScreenPres;
import com.trigma.tiktok.presenter.GuestLinkScreenPresImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;

public class GuestLinkScreen extends BaseActivity implements OnClickListener {
    private EditText et_enter_url;
    private GuestLinkScreenPres guestLinkScreenPres;
    private ImageView img_back;
    private boolean isDocCalling = false;
    private LinearLayout linear_connect;
    public NetWorkingService netWorkingService;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.guest_screen_url);
        if (getIntent().hasExtra("isDocCalling")) {
            this.isDocCalling = getIntent().getBooleanExtra("isDocCalling", false);
        }
        Log.e("isDocCalling", "" + this.isDocCalling);
        initView();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.guestLinkScreenPres = new GuestLinkScreenPresImp(this, this.netWorkingService);
    }

    private void initView() {
        this.et_enter_url = (EditText) findViewById(C1020R.id.et_enter_url);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.linear_connect = (LinearLayout) findViewById(C1020R.id.linear_connect);
        clickListners();
        Uri data = getIntent().getData();
        if (data != null && !TextUtils.isEmpty(data.toString())) {
            this.et_enter_url.setText(data.toString().replace(getResources().getString(C1020R.string.scheme), "http"));
        }
    }

    private void clickListners() {
        this.img_back.setOnClickListener(this);
        this.linear_connect.setOnClickListener(this);
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
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

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.linear_connect:
                if (TextUtils.isEmpty(this.et_enter_url.getText().toString()) || this.et_enter_url.getText().toString().trim().equalsIgnoreCase("")) {
                    showError(getResources().getString(C1020R.string.please_enter_url));
                    return;
                } else if (Patterns.WEB_URL.matcher(this.et_enter_url.getText().toString()).matches()) {
                    this.guestLinkScreenPres.connectNow(this.et_enter_url.getText().toString().trim());
                    return;
                } else {
                    showError(getResources().getString(C1020R.string.please_enter_valid_url));
                    return;
                }
            case C1020R.id.img_back:
                finish();
                return;
            default:
                return;
        }
    }

    public void gotoVideoCallScreen(GuestUrlObject data) {
        VideoCallRequiredFields videoCallRequiredFields = new VideoCallRequiredFields();
        videoCallRequiredFields.setGroupId(data.getGroupId());
        videoCallRequiredFields.setSchedule(CommonUtils.getCurrentDate());
        SharedPreff.saveVideoCallRequireData(videoCallRequiredFields);
        VideoCallScreen.TOKEN = data.getTokenData();
        VideoCallScreen.API_KEY = data.getApiKey();
        VideoCallScreen.SESSION_ID = data.getSessionData();
        VideoCallScreenChanged.TOKEN = data.getTokenData();
        VideoCallScreenChanged.API_KEY = data.getApiKey();
        VideoCallScreenChanged.SESSION_ID = data.getSessionData();
        Intent vidoeScreen = new Intent(this, VideoCallScreenChanged.class);
        vidoeScreen.putExtra("isDocCalling", this.isDocCalling);
        startActivity(vidoeScreen);
    }
}
