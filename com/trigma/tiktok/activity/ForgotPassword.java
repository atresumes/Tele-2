package com.trigma.tiktok.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.presenter.ForgotPswdPresenter;
import com.trigma.tiktok.presenter.ForgotPswdPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;

public class ForgotPassword extends BaseActivity implements OnClickListener {
    private EditText et_email;
    private ForgotPswdPresenter forgotPswdPresenter;
    private ImageView img_back;
    private LinearLayout linear_reset_password;
    public NetWorkingService netWorkingService;
    private RelativeLayout parent;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.forgot_password);
        initView();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.forgotPswdPresenter = new ForgotPswdPresenterImp(this, this.netWorkingService);
    }

    private void initView() {
        this.et_email = (EditText) findViewById(C1020R.id.et_email);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.parent = (RelativeLayout) findViewById(C1020R.id.parent);
        this.linear_reset_password = (LinearLayout) findViewById(C1020R.id.linear_reset_password);
        clickListners();
    }

    private void clickListners() {
        this.linear_reset_password.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.parent.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.parent:
                CommonUtils.hideKeyboard(this.et_email, this);
                return;
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.linear_reset_password:
                this.forgotPswdPresenter.validateEmailAndApiCall(this.et_email.getText().toString());
                return;
            default:
                return;
        }
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error);
    }

    public void setEmailEmpty() {
        this.et_email.setText(null);
    }

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void succesMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, 0).show();
        this.et_email.setText(null);
    }

    public void succesMessage() {
        finish();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.forgotPswdPresenter.unSubscribeCallbacks();
    }
}
