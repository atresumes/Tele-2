package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.trigma.tiktok.BuildConfig;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.model.LandingPage;
import com.trigma.tiktok.presenter.DashBoardPresenter;
import com.trigma.tiktok.presenter.DashBoardPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.LocationTrackerClass;
import com.trigma.tiktok.utils.SharedPreff;

public class DashBoard extends BaseActivity implements OnClickListener {
    private DashBoardPresenter dashBoardPresenter;
    private CardView doc_faq;
    private LinearLayout doc_linear;
    private LinearLayout guest_linear;
    private ImageView img_play_video;
    private ImageView img_vid_icon;
    public LocationTrackerClass locationTrackerClass;
    public NetWorkingService netWorkingService;
    private CardView pat_faq;
    private LinearLayout pat_linear;
    private CardView telemedicine;
    private CardView who_we;

    class C10481 implements AlertCallBackWithButtonsInterface {
        C10481() {
        }

        public void positiveClick() {
            Intent login = new Intent(DashBoard.this, LoginActivity.class);
            login.putExtra(Constants.IS_DOC, 1);
            DashBoard.this.startActivity(login);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            Intent login = new Intent(DashBoard.this, LoginActivity.class);
            login.putExtra(Constants.IS_DOC, 3);
            DashBoard.this.startActivity(login);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.activity_dash_board);
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.dashBoardPresenter = new DashBoardPresenterImp(this, this.netWorkingService);
        initViews();
        this.dashBoardPresenter.callApiToGetLink();
        this.locationTrackerClass = new LocationTrackerClass(this);
        this.locationTrackerClass.startLocationTracking();
    }

    private void initViews() {
        this.img_play_video = (ImageView) findViewById(C1020R.id.img_play_video);
        this.img_vid_icon = (ImageView) findViewById(C1020R.id.img_vid_icon);
        this.doc_faq = (CardView) findViewById(C1020R.id.doc_faq);
        this.telemedicine = (CardView) findViewById(C1020R.id.telemedicine);
        this.who_we = (CardView) findViewById(C1020R.id.who_we);
        this.pat_faq = (CardView) findViewById(C1020R.id.pat_faq);
        this.doc_linear = (LinearLayout) findViewById(C1020R.id.doc_linear);
        this.pat_linear = (LinearLayout) findViewById(C1020R.id.pat_linear);
        this.guest_linear = (LinearLayout) findViewById(C1020R.id.guest_linear);
        settingClickListner();
        if (BuildConfig.FLAVOR.equals("Health4Life")) {
            this.img_vid_icon.setScaleType(ScaleType.FIT_XY);
            this.img_play_video.setVisibility(8);
            return;
        }
        this.img_vid_icon.setScaleType(ScaleType.CENTER_CROP);
    }

    private void settingClickListner() {
        this.img_play_video.setOnClickListener(this);
        this.doc_faq.setOnClickListener(this);
        this.telemedicine.setOnClickListener(this);
        this.who_we.setOnClickListener(this);
        this.pat_faq.setOnClickListener(this);
        this.doc_linear.setOnClickListener(this);
        this.pat_linear.setOnClickListener(this);
        this.guest_linear.setOnClickListener(this);
    }

    protected void onPause() {
        super.onPause();
    }

    public void onClick(View v) {
        LandingPage landingPage = SharedPreff.getLandingPageData();
        switch (v.getId()) {
            case C1020R.id.img_play_video:
                startActivity(new Intent(this, VideoPlayerActivity.class));
                return;
            case C1020R.id.doc_faq:
                if (landingPage != null) {
                    CommonUtils.openLink(this, landingPage.getData().getFaqDr());
                    return;
                }
                return;
            case C1020R.id.pat_faq:
                if (landingPage != null) {
                    CommonUtils.openLink(this, landingPage.getData().getFaqPatient());
                    return;
                }
                return;
            case C1020R.id.who_we:
                if (landingPage != null) {
                    CommonUtils.openLink(this, landingPage.getData().getWhoAreWe());
                    return;
                }
                return;
            case C1020R.id.telemedicine:
                if (landingPage != null) {
                    CommonUtils.openLink(this, landingPage.getData().getTeliMedicine());
                    return;
                }
                return;
            case C1020R.id.doc_linear:
                DialogPopUps.staffPromptLoginDialog(this, new C10481());
                return;
            case C1020R.id.pat_linear:
                Intent loginPat = new Intent(this, LoginActivity.class);
                loginPat.putExtra(Constants.IS_DOC, 0);
                startActivity(loginPat);
                return;
            case C1020R.id.guest_linear:
                startActivity(new Intent(this, GuestLinkScreen.class));
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

    public void apiResponceRecieved(LandingPage landingPage) {
        DialogPopUps.hideDialog();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.dashBoardPresenter.unSubscribeCallbacks();
        this.locationTrackerClass.stopLocationTracking();
    }
}
