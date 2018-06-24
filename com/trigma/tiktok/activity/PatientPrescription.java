package com.trigma.tiktok.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;

public class PatientPrescription extends BaseActivity implements OnClickListener {
    private ImageView img_header_back;
    private ImageView img_slider;
    private LinearLayout linear_done;
    LoginDocResponce loginDocResponce;
    public NetWorkingService netWorkingService;
    private TextView title_name;
    private WebView webView;

    class C10641 extends WebViewClient {
        C10641() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            PatientPrescription.this.showProgressDialog();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            PatientPrescription.this.webView.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            PatientPrescription.this.hideDialog();
        }

        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            PatientPrescription.this.hideDialog();
        }
    }

    class C10652 implements AlertCallBackWithButtonsInterface {
        C10652() {
        }

        public void positiveClick() {
            Intent signUp = new Intent(PatientPrescription.this, HomeScreen.class);
            signUp.setFlags(268468224);
            PatientPrescription.this.startActivity(signUp);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C10663 implements AlertCallBackWithButtonsInterface {
        C10663() {
        }

        public void positiveClick() {
            Intent signUp = new Intent(PatientPrescription.this, HomeScreen.class);
            signUp.setFlags(268468224);
            PatientPrescription.this.startActivity(signUp);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            this.context = context;
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.pateint_prescription);
        initViews();
    }

    private void initViews() {
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.title_name.setText(getResources().getString(C1020R.string.prescription));
        this.title_name.setVisibility(4);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_slider.setVisibility(4);
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.img_header_back.setVisibility(4);
        this.linear_done = (LinearLayout) findViewById(C1020R.id.linear_done);
        this.webView = (WebView) findViewById(C1020R.id.webView);
        this.webView.setWebChromeClient(new MyWebChromeClient(this));
        this.webView.setWebViewClient(new C10641());
        this.webView.clearCache(true);
        this.webView.clearHistory();
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.loadUrl(Constants.PRESCRIPTION_URL);
        clickListners();
    }

    private void clickListners() {
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
        this.linear_done.setOnClickListener(this);
    }

    public void onBackPressed() {
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_header_back:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_exit), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10652());
                return;
            case C1020R.id.linear_done:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_exit), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10663());
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

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }
}
