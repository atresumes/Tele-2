package com.trigma.tiktok.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.BaseActivity;
import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.presenter.patient.PharmacyDetailPresenter;
import com.trigma.tiktok.presenter.patient.PharmacyDetailPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;

public class PharmacyDetail extends BaseActivity implements OnClickListener {
    private CardView card_phn;
    private ImageView img_header_back;
    private ImageView img_slider;
    private LinearLayout linear_select_pharmacy;
    public NetWorkingService netWorkingService;
    private PharmacyDetailPresenter pharmacyDetailPresenter;
    private SelectPharmacyObject selectPharmacyObject;
    private boolean show_select_button = true;
    private TextView tv_address_field;
    private TextView tv_certified;
    private TextView tv_city;
    private TextView tv_fax;
    private TextView tv_pharmacy_name;
    private TextView tv_phone;
    private TextView tv_state;
    private TextView tv_timings;
    private TextView tv_title;
    private TextView tv_zip;

    class C10941 implements AlertCallBackWithButtonsInterface {
        C10941() {
        }

        public void positiveClick() {
            CommonUtils.call(PharmacyDetail.this, PharmacyDetail.this.selectPharmacyObject.getData().getPhonePrimary());
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.pharmacy_detial);
        if (getIntent().hasExtra("show_select_button")) {
            this.show_select_button = getIntent().getBooleanExtra("show_select_button", true);
        }
        this.selectPharmacyObject = SharedPreff.getSelectPharmacyObject();
        try {
            Log.e("selectPharmacyObject", "" + new Gson().toJson(this.selectPharmacyObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initViews();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.pharmacyDetailPresenter = new PharmacyDetailPresenterImp(this, this.netWorkingService);
        settingData(this.selectPharmacyObject);
    }

    private void initViews() {
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_slider.setVisibility(4);
        this.tv_address_field = (TextView) findViewById(C1020R.id.tv_address_field);
        this.tv_city = (TextView) findViewById(C1020R.id.tv_city);
        this.tv_title = (TextView) findViewById(C1020R.id.title_name);
        this.tv_title.setText(getResources().getString(C1020R.string.pharmacy_details));
        this.tv_title.setVisibility(0);
        this.tv_state = (TextView) findViewById(C1020R.id.tv_state);
        this.tv_zip = (TextView) findViewById(C1020R.id.tv_zip);
        this.tv_fax = (TextView) findViewById(C1020R.id.tv_fax);
        this.tv_timings = (TextView) findViewById(C1020R.id.tv_timings);
        this.tv_certified = (TextView) findViewById(C1020R.id.tv_certified);
        this.tv_pharmacy_name = (TextView) findViewById(C1020R.id.tv_pharmacy_name);
        this.tv_phone = (TextView) findViewById(C1020R.id.tv_phone);
        this.card_phn = (CardView) findViewById(C1020R.id.card_phn);
        this.linear_select_pharmacy = (LinearLayout) findViewById(C1020R.id.linear_select_pharmacy);
        this.img_header_back.setOnClickListener(this);
        this.linear_select_pharmacy.setOnClickListener(this);
        this.card_phn.setOnClickListener(this);
        if (!this.show_select_button) {
            this.linear_select_pharmacy.setVisibility(8);
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

    public void settingData(SelectPharmacyObject dataObejct) {
        if (dataObejct != null) {
            this.tv_address_field.setText(dataObejct.getData().getAddress());
            this.tv_city.setText(dataObejct.getData().getCity());
            this.tv_state.setText(dataObejct.getData().getState());
            this.tv_zip.setText(dataObejct.getData().getZip());
            this.tv_fax.setText(dataObejct.getData().getFax());
            this.tv_timings.setText(dataObejct.getData().getHours24());
            this.tv_phone.setText(dataObejct.getData().getPhonePrimary());
            if (dataObejct.getData().getIsFromSurescripts().equalsIgnoreCase("True")) {
                this.tv_certified.setText(getResources().getString(C1020R.string.yes));
            } else {
                this.tv_certified.setText(getResources().getString(C1020R.string.no));
            }
            this.tv_pharmacy_name.setText(dataObejct.getData().getStoreName());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_header_back:
                finish();
                return;
            case C1020R.id.linear_select_pharmacy:
                if (this.selectPharmacyObject != null) {
                    this.pharmacyDetailPresenter.selectPharmacy(this.selectPharmacyObject.getData().getDoseSpotPharmacyID(), this.selectPharmacyObject.getData().getStoreName(), this.selectPharmacyObject.getData().getId());
                    return;
                }
                return;
            case C1020R.id.card_phn:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.please_confirm_your_call), getResources().getString(C1020R.string.You_re_about_to_call_the_following) + "\n" + this.tv_phone.getText().toString(), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10941());
                return;
            default:
                return;
        }
    }

    public void pharmacySelectedSuccessful(String PharmacyName) {
        Log.e("PharmacyName", "" + PharmacyName);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("PharmacyName", PharmacyName);
        setResult(-1, returnIntent);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.pharmacyDetailPresenter.unSubscribeCallbacks();
    }
}
