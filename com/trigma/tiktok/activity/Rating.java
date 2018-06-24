package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.HomeScreenPatient;
import com.trigma.tiktok.model.FcmDoctorDetail;
import com.trigma.tiktok.presenter.RatingPresenter;
import com.trigma.tiktok.presenter.RatingPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import io.techery.properratingbar.ProperRatingBar;

public class Rating extends BaseActivity {
    private EditText et_feedback;
    private FcmDoctorDetail fcmDoctorDetail;
    private ImageView img_back;
    private ImageView img_profile;
    private LinearLayout linear_done;
    public NetWorkingService netWorkingService;
    private RelativeLayout parent;
    private RatingPresenter ratingPresenter;
    private ProperRatingBar rating_bar;
    private TextView tv_address;
    private TextView tv_name;

    class C10671 implements OnClickListener {
        C10671() {
        }

        public void onClick(View v) {
            if (!TextUtils.isEmpty(Rating.this.et_feedback.getText().toString()) && Rating.this.et_feedback.getText().toString().trim().length() != 0) {
                Rating.this.ratingPresenter.giveRating(Rating.this.et_feedback.getText().toString(), "" + Rating.this.rating_bar.getRating(), Rating.this.fcmDoctorDetail.getId());
            }
        }
    }

    class C10682 implements OnClickListener {
        C10682() {
        }

        public void onClick(View v) {
            CommonUtils.hideKeyboard(Rating.this.et_feedback, Rating.this);
        }
    }

    class C10693 implements AlertCallBackInterface {
        C10693() {
        }

        public void neutralClick() {
            Intent signUp = new Intent(Rating.this, HomeScreenPatient.class);
            signUp.setFlags(268468224);
            Rating.this.startActivity(signUp);
        }
    }

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
            if (TextUtils.isEmpty(editable.toString())) {
                Rating.this.enableDoneButton(false);
            } else {
                Rating.this.enableDoneButton(true);
            }
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.rating_dr);
        this.fcmDoctorDetail = SharedPreff.getFcmDocDetail();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        initViews();
        this.ratingPresenter = new RatingPresenterImp(this, this.netWorkingService);
        settingData();
    }

    private void settingData() {
        try {
            if (this.fcmDoctorDetail != null) {
                Picasso.with(this).load(Constants.HTTP + this.fcmDoctorDetail.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_profile);
                this.tv_name.setText(this.fcmDoctorDetail.getFirstName() + " " + this.fcmDoctorDetail.getLastName());
                this.tv_address.setText(this.fcmDoctorDetail.getSpeciality());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableDoneButton(boolean value) {
        if (value) {
            this.linear_done.setEnabled(true);
            this.linear_done.setBackgroundResource(C1020R.drawable.normal_blue);
            return;
        }
        this.linear_done.setBackgroundResource(C1020R.drawable.blue_pressed);
        this.linear_done.setEnabled(false);
    }

    private void initViews() {
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.img_profile = (ImageView) findViewById(C1020R.id.img_profile);
        this.parent = (RelativeLayout) findViewById(C1020R.id.parent);
        this.img_back.setVisibility(4);
        this.tv_name = (TextView) findViewById(C1020R.id.tv_name);
        this.tv_address = (TextView) findViewById(C1020R.id.tv_address);
        this.et_feedback = (EditText) findViewById(C1020R.id.et_feedback);
        this.et_feedback.addTextChangedListener(new CustomWatcher(this.et_feedback));
        this.linear_done = (LinearLayout) findViewById(C1020R.id.linear_done);
        this.rating_bar = (ProperRatingBar) findViewById(C1020R.id.rating_bar);
        this.linear_done.setOnClickListener(new C10671());
        this.parent.setOnClickListener(new C10682());
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

    protected void onDestroy() {
        super.onDestroy();
        this.ratingPresenter.unSubscribeCallbacks();
    }

    public void ratingDone() {
        DialogPopUps.thanksPopUp(this, "", "", "", "", true, new C10693());
    }

    public void onBackPressed() {
    }
}
