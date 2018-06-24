package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.presenter.AddStaffIdPresenter;
import com.trigma.tiktok.presenter.AddStaffIdPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;

public class AddStaffId extends BaseActivity implements OnClickListener {
    private AddStaffIdPresenter addStaffIdPresenter;
    private EditText et_enter_url;
    private ImageView img_back;
    private LinearLayout linear_connect;
    public NetWorkingService netWorkingService;
    private String staff_user_id;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.add_staff_id_screen);
        if (getIntent().hasExtra(Constants.STAFF_UDER_ID)) {
            this.staff_user_id = getIntent().getStringExtra(Constants.STAFF_UDER_ID);
        }
        initView();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.addStaffIdPresenter = new AddStaffIdPresenterImp(this, this.netWorkingService);
    }

    private void initView() {
        this.et_enter_url = (EditText) findViewById(C1020R.id.et_enter_url);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.linear_connect = (LinearLayout) findViewById(C1020R.id.linear_connect);
        clickListners();
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

    public void showErrorWithCallBack(final String error) {
        DialogPopUps.alertPopUp(this, error, getResources().getString(C1020R.string.ok_dialog), new AlertCallBackInterface() {
            public void neutralClick() {
                if (error.equalsIgnoreCase(AddStaffId.this.getResources().getString(C1020R.string.staff_member_already_exist_with_this_email_id))) {
                    AddStaffId.this.setResult(-1, new Intent());
                    AddStaffId.this.finish();
                }
            }
        });
    }

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.linear_connect:
                break;
            case C1020R.id.img_back:
                finish();
                break;
            default:
                return;
        }
        if (TextUtils.isEmpty(this.et_enter_url.getText().toString()) || this.et_enter_url.getText().toString().trim().equalsIgnoreCase("")) {
            showError(getResources().getString(C1020R.string.please_enter_staff_id));
        } else {
            this.addStaffIdPresenter.addStaffWithID(this.et_enter_url.getText().toString(), this.staff_user_id, "", "");
        }
    }

    public void staffAdded(String id) {
        setResult(-1, new Intent());
        finish();
    }
}
