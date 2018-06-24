package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.presenter.PrescriptionsPresenter;
import com.trigma.tiktok.presenter.PrescriptionsPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;

public class Prescriptions extends BaseFragment implements OnClickListener {
    private Activity activity;
    private CardView card_view;
    private TextView done_button;
    private ImageView img_header_back;
    private ImageView img_slider;
    private LinearLayout linear_done;
    LoginDocResponce loginDocResponce;
    public NetWorkingService netWorkingService;
    PrescriptionsPresenter prescriptionsPresenter;
    private TextView title_name;
    private TextView tv_transmission_cout;
    private WebView webView;

    class C11691 implements AlertCallBackWithButtonsInterface {
        C11691() {
        }

        public void positiveClick() {
            Prescriptions.this.activity.finish();
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    class C11702 extends WebViewClient {
        C11702() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Prescriptions.this.showProgressDialog();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Prescriptions.this.webView.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Prescriptions.this.hideDialog();
        }

        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Prescriptions.this.hideDialog();
        }
    }

    class C11713 implements AlertCallBackWithButtonsInterface {
        C11713() {
        }

        public void positiveClick() {
            Prescriptions.this.activity.finish();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.prescription, container, false);
        this.loginDocResponce = SharedPreff.getLoginResponce();
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_transmission_cout = (TextView) screen.findViewById(C1020R.id.tv_transmission_cout);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.linear_done = (LinearLayout) screen.findViewById(C1020R.id.linear_done);
        this.img_header_back.setVisibility(0);
        this.img_slider.setVisibility(0);
        this.title_name.setVisibility(8);
        this.card_view = (CardView) screen.findViewById(C1020R.id.card_view);
        this.webView = (WebView) screen.findViewById(C1020R.id.webView);
        this.done_button = (TextView) screen.findViewById(C1020R.id.done_button);
        clickListners();
        inializingWebView();
    }

    private void inializingWebView() {
        this.tv_transmission_cout.setText("" + Constants.PRESCRIPTION_COUNT);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.prescriptionsPresenter = new PrescriptionsPresenterImp(this, this.netWorkingService);
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.prescription));
        this.title_name.setVisibility(0);
    }

    private void clickListners() {
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
        this.linear_done.setOnClickListener(this);
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
                if (this.done_button.getTag().toString().equalsIgnoreCase(this.activity.getResources().getString(C1020R.string.resolve))) {
                    this.activity.finish();
                    return;
                } else {
                    DialogPopUps.showAlertWithButtons(this.activity, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_exit), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C11691());
                    return;
                }
            case C1020R.id.linear_done:
                if (this.done_button.getTag().toString().equalsIgnoreCase(this.activity.getResources().getString(C1020R.string.resolve))) {
                    this.img_header_back.setVisibility(8);
                    this.img_slider.setVisibility(8);
                    this.title_name.setVisibility(8);
                    this.done_button.setText(this.activity.getResources().getString(C1020R.string.exit_pharmacy));
                    this.done_button.setTag(this.activity.getResources().getString(C1020R.string.done_button));
                    this.card_view.setVisibility(8);
                    this.webView.setVisibility(0);
                    this.webView.setWebChromeClient(new MyWebChromeClient(this.activity));
                    this.webView.setWebViewClient(new C11702());
                    this.webView.clearCache(true);
                    this.webView.clearHistory();
                    this.webView.getSettings().setJavaScriptEnabled(true);
                    this.webView.setHorizontalScrollBarEnabled(false);
                    this.webView.loadUrl(Constants.PRESCRIPTION_URL);
                    return;
                }
                DialogPopUps.showAlertWithButtons(this.activity, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_exit), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C11713());
                return;
            default:
                return;
        }
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
}
