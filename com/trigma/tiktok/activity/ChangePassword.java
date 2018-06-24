package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.presenter.ChangePasswordPresenter;
import com.trigma.tiktok.presenter.ChangePasswordPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;

public class ChangePassword extends BaseActivity implements OnClickListener {
    private ChangePasswordPresenter changePasswordPresenter;
    private EditText et_confirm_passowrd;
    private EditText et_new_passowrd;
    private EditText et_old_passowrd;
    private ImageView img_back;
    private ImageView img_confirm_pwd;
    private ImageView img_new_pwd;
    private ImageView img_old_pwd;
    private LinearLayout linear_change_password;
    public NetWorkingService netWorkingService;

    public class CustomWatcher implements TextWatcher {
        private View view;

        public CustomWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(ChangePassword.this.et_old_passowrd.getText().toString().trim()) || TextUtils.isEmpty(ChangePassword.this.et_new_passowrd.getText().toString().trim()) || TextUtils.isEmpty(ChangePassword.this.et_confirm_passowrd.getText().toString().trim())) {
                ChangePassword.this.linear_change_password.setEnabled(false);
                ChangePassword.this.linear_change_password.setBackgroundResource(C1020R.drawable.blue_pressed);
                return;
            }
            ChangePassword.this.linear_change_password.setEnabled(true);
            ChangePassword.this.linear_change_password.setBackgroundResource(C1020R.drawable.normal_blue);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.change_password);
        initView();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.changePasswordPresenter = new ChangePasswordPresenterImp(this, this.netWorkingService);
    }

    private void initView() {
        this.et_old_passowrd = (EditText) findViewById(C1020R.id.et_old_passowrd);
        this.et_new_passowrd = (EditText) findViewById(C1020R.id.et_new_passowrd);
        this.et_confirm_passowrd = (EditText) findViewById(C1020R.id.et_confirm_passowrd);
        this.et_old_passowrd.addTextChangedListener(new CustomWatcher(this.et_old_passowrd));
        this.et_new_passowrd.addTextChangedListener(new CustomWatcher(this.et_new_passowrd));
        this.et_confirm_passowrd.addTextChangedListener(new CustomWatcher(this.et_confirm_passowrd));
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.img_old_pwd = (ImageView) findViewById(C1020R.id.img_old_pwd);
        this.img_new_pwd = (ImageView) findViewById(C1020R.id.img_new_pwd);
        this.img_confirm_pwd = (ImageView) findViewById(C1020R.id.img_confirm_pwd);
        this.linear_change_password = (LinearLayout) findViewById(C1020R.id.linear_change_password);
        clickListners();
    }

    private void clickListners() {
        this.img_old_pwd.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.img_new_pwd.setOnClickListener(this);
        this.img_confirm_pwd.setOnClickListener(this);
        this.linear_change_password.setOnClickListener(this);
        this.linear_change_password.setEnabled(false);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.img_old_pwd:
                if (this.img_old_pwd.getTag().toString().equalsIgnoreCase(getResources().getString(C1020R.string.hide))) {
                    this.img_old_pwd.setTag(getResources().getString(C1020R.string.show));
                    this.img_old_pwd.setImageResource(C1020R.drawable.private_change);
                    this.et_old_passowrd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    return;
                }
                this.img_old_pwd.setTag(getResources().getString(C1020R.string.hide));
                this.img_old_pwd.setImageResource(C1020R.drawable.publi_change);
                this.et_old_passowrd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                return;
            case C1020R.id.img_new_pwd:
                if (this.img_new_pwd.getTag().toString().equalsIgnoreCase(getResources().getString(C1020R.string.hide))) {
                    this.img_new_pwd.setTag(getResources().getString(C1020R.string.show));
                    this.img_new_pwd.setImageResource(C1020R.drawable.private_change);
                    this.et_new_passowrd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    return;
                }
                this.img_new_pwd.setTag(getResources().getString(C1020R.string.hide));
                this.img_new_pwd.setImageResource(C1020R.drawable.publi_change);
                this.et_new_passowrd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                return;
            case C1020R.id.img_confirm_pwd:
                if (this.img_confirm_pwd.getTag().toString().equalsIgnoreCase(getResources().getString(C1020R.string.hide))) {
                    this.img_confirm_pwd.setTag(getResources().getString(C1020R.string.show));
                    this.img_confirm_pwd.setImageResource(C1020R.drawable.private_change);
                    this.et_confirm_passowrd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    return;
                }
                this.img_confirm_pwd.setTag(getResources().getString(C1020R.string.hide));
                this.img_confirm_pwd.setImageResource(C1020R.drawable.publi_change);
                this.et_confirm_passowrd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                return;
            case C1020R.id.linear_change_password:
                this.changePasswordPresenter.changePassword(this.et_old_passowrd.getText().toString(), this.et_new_passowrd.getText().toString(), this.et_confirm_passowrd.getText().toString());
                return;
            default:
                return;
        }
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

    protected void onDestroy() {
        super.onDestroy();
        this.changePasswordPresenter.unSubscribeCallbacks();
    }

    public void passwordChanged() {
        this.et_confirm_passowrd.setText(null);
        this.et_new_passowrd.setText(null);
        this.et_old_passowrd.setText(null);
        SharedPreff.clearDocPreff();
        Intent intent = new Intent(this, DashBoard.class);
        intent.setFlags(268468224);
        startActivity(intent);
    }
}
