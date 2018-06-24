package com.trigma.tiktok.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.trigma.tiktok.BuildConfig;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorSearch;
import com.trigma.tiktok.activity.patient.HomeScreenPatient;
import com.trigma.tiktok.activity.patient.SelectPharmacy;
import com.trigma.tiktok.facebook.OnSocialLoginListener;
import com.trigma.tiktok.facebook.SocialLogin;
import com.trigma.tiktok.facebook.SocialLoginData;
import com.trigma.tiktok.model.FcmToken;
import com.trigma.tiktok.model.LoginDetailPojo;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.presenter.LoginActivityPresenter;
import com.trigma.tiktok.presenter.LoginActivityPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.LocationTrackerClass;
import com.trigma.tiktok.utils.SharedPreff;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity implements OnClickListener, OnSocialLoginListener, OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginInActivity";
    private CallbackManager callbackManager;
    ClickableSpan clickableSpan;
    private EditText et_passowrd;
    private EditText et_username;
    private String fcmToken = null;
    private TextView forget_pwd;
    public GoogleSignInOptions googleSignInOptions;
    private ImageView img_back;
    private ImageView img_fb;
    private ImageView img_google;
    private ImageView img_h;
    private ImageView img_show_pwd;
    private ImageView img_t;
    private int isDoc = 0;
    private RelativeLayout lin_parent;
    private LinearLayout linear_login;
    public LocationTrackerClass locationTrackerClass;
    private LoginActivityPresenter loginActivityPresenter;
    private LoginDetailPojo loginDetailPojo;
    public String loginType = "M";
    public GoogleApiClient mGoogleApiClient;
    public NetWorkingService netWorkingService;
    private CheckBox remember_me;
    private RelativeLayout rl_social_login_layout;
    private SignInButton signInButton;
    public boolean socail_login = false;
    private TextView tv_register;

    class C10521 extends ClickableSpan {
        C10521() {
        }

        public void onClick(View textView) {
            SharedPreff.clearSocialLoginData();
            Intent createAcc = new Intent(LoginActivity.this, CreateAccount.class);
            createAcc.putExtra(Constants.IS_DOC, LoginActivity.this.isDoc);
            LoginActivity.this.startActivity(createAcc);
        }

        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }
    }

    class C10532 implements ResultCallback<Status> {
        C10532() {
        }

        public void onResult(Status status) {
        }
    }

    class C10543 implements AlertCallBackInterface {
        C10543() {
        }

        public void neutralClick() {
            SharedPreff.clearDocPreff();
            Intent intent = new Intent(LoginActivity.this, DashBoard.class);
            intent.setFlags(268468224);
            LoginActivity.this.startActivity(intent);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.login_activity);
        EventBus.getDefault().register(this);
        this.socail_login = false;
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.fcmToken = SharedPreff.getDeviceToken();
        this.locationTrackerClass = new LocationTrackerClass(this);
        this.locationTrackerClass.startLocationTracking();
        try {
            if ((this.fcmToken.equalsIgnoreCase("") || this.fcmToken.isEmpty() || this.fcmToken == null) && FirebaseInstanceId.getInstance().getToken() != null) {
                this.fcmToken = FirebaseInstanceId.getInstance().getToken();
                if (!(this.fcmToken.equalsIgnoreCase("") && this.fcmToken.isEmpty())) {
                    SharedPreff.saveDeviceToken(this.fcmToken);
                }
            }
        } catch (Exception e) {
            this.fcmToken = "";
        }
        Log.e("fcmToken ", " " + this.fcmToken);
        Log.e("lat ", " " + SharedPreff.getLat());
        Log.e("long ", " " + SharedPreff.getLng());
        if (getIntent().hasExtra(Constants.IS_DOC)) {
            this.isDoc = getIntent().getIntExtra(Constants.IS_DOC, 0);
        }
        if (this.isDoc == 1) {
            this.loginDetailPojo = SharedPreff.getDocLogin();
        } else if (this.isDoc == 3) {
            this.loginDetailPojo = SharedPreff.getStaffLogin();
        } else {
            this.loginDetailPojo = SharedPreff.getPatientLogin();
        }
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        init();
        this.loginActivityPresenter = new LoginActivityPresenterImp(this, this.netWorkingService, this.isDoc);
        settingClickableSpan();
        initCallbackManager();
        initGoogleAccount();
    }

    private void initGoogleAccount() {
        this.mGoogleApiClient = new Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()).build();
    }

    private void initCallbackManager() {
        this.callbackManager = new SocialLogin(this).loginViaFacebook(C1020R.id.img_fb);
    }

    private void settingClickableSpan() {
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(C1020R.string.to_create_an_account_click_here));
        SpannableString ss = new SpannableString(sb);
        this.clickableSpan = new C10521();
        ss.setSpan(this.clickableSpan, 3, 20, 33);
        StyleSpan bss = new StyleSpan(1);
        if (BuildConfig.FLAVOR.equals("Health4Life")) {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#5e5e5e")), 3, 20, 33);
        } else {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 3, 20, 33);
        }
        ss.setSpan(bss, 3, 20, 33);
        this.tv_register.setText(ss);
        this.tv_register.setMovementMethod(LinkMovementMethod.getInstance());
        this.tv_register.setHighlightColor(0);
    }

    private void init() {
        this.et_passowrd = (EditText) findViewById(C1020R.id.et_passowrd);
        this.et_username = (EditText) findViewById(C1020R.id.et_username);
        this.img_show_pwd = (ImageView) findViewById(C1020R.id.img_show_pwd);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.signInButton = (SignInButton) findViewById(C1020R.id.signInButton);
        this.img_fb = (ImageView) findViewById(C1020R.id.img_fb);
        this.img_google = (ImageView) findViewById(C1020R.id.img_google);
        this.img_h = (ImageView) findViewById(C1020R.id.img_h);
        this.img_t = (ImageView) findViewById(C1020R.id.img_t);
        if (BuildConfig.FLAVOR.equals("Health4Life")) {
            this.img_h.setVisibility(0);
            this.img_t.setVisibility(8);
        } else {
            this.img_t.setVisibility(0);
            this.img_h.setVisibility(8);
        }
        this.forget_pwd = (TextView) findViewById(C1020R.id.forget_pwd);
        this.linear_login = (LinearLayout) findViewById(C1020R.id.linear_login);
        this.tv_register = (TextView) findViewById(C1020R.id.tv_register);
        this.remember_me = (CheckBox) findViewById(C1020R.id.remember_me);
        this.lin_parent = (RelativeLayout) findViewById(C1020R.id.lin_parent);
        this.rl_social_login_layout = (RelativeLayout) findViewById(C1020R.id.rl_social_login_layout);
        if (this.isDoc == 3) {
            this.rl_social_login_layout.setVisibility(8);
            this.tv_register.setVisibility(8);
        } else {
            this.rl_social_login_layout.setVisibility(0);
            this.tv_register.setVisibility(0);
        }
        clickListners();
        checkingRememberMe();
    }

    private void checkingRememberMe() {
        if (SharedPreff.isRememberMe()) {
            this.remember_me.setChecked(true);
            if (this.loginDetailPojo != null && this.loginDetailPojo.getLoginType().equalsIgnoreCase("M")) {
                this.et_username.setText(this.loginDetailPojo.getEmail());
                this.et_passowrd.setText(this.loginDetailPojo.getLoginKey());
                return;
            }
            return;
        }
        this.remember_me.setChecked(false);
    }

    private void clickListners() {
        this.forget_pwd.setOnClickListener(this);
        this.linear_login.setOnClickListener(this);
        this.tv_register.setOnClickListener(this);
        this.img_show_pwd.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.remember_me.setOnClickListener(this);
        this.lin_parent.setOnClickListener(this);
        this.signInButton.setOnClickListener(this);
        this.img_google.setOnClickListener(this);
    }

    private void signIn() {
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(this.mGoogleApiClient), RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(this.mGoogleApiClient).setResultCallback(new C10532());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.forget_pwd:
                startActivity(new Intent(this, ForgotPassword.class));
                return;
            case C1020R.id.img_show_pwd:
                if (this.img_show_pwd.getTag().toString().equalsIgnoreCase(getResources().getString(C1020R.string.hide))) {
                    this.img_show_pwd.setTag(getResources().getString(C1020R.string.show));
                    this.img_show_pwd.setImageResource(C1020R.drawable.private_icon_login);
                    this.et_passowrd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    return;
                }
                this.img_show_pwd.setTag(getResources().getString(C1020R.string.hide));
                this.img_show_pwd.setImageResource(C1020R.drawable.public_icon_login);
                this.et_passowrd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                return;
            case C1020R.id.lin_parent:
                CommonUtils.hideKeyboard(this.et_username, this);
                return;
            case C1020R.id.img_google:
                this.signInButton.performClick();
                signIn();
                return;
            case C1020R.id.remember_me:
                if (this.remember_me.isChecked()) {
                    SharedPreff.rememberMe(true);
                    checkingRememberMe();
                    return;
                }
                SharedPreff.rememberMe(false);
                return;
            case C1020R.id.linear_login:
                if (this.fcmToken != null) {
                    this.loginActivityPresenter.checkValidation(this.et_username.getText().toString().toLowerCase(), this.et_passowrd.getText().toString(), this.loginType);
                    return;
                } else {
                    showError(getResources().getString(C1020R.string.fcm_token_null));
                    return;
                }
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

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void loginSuccessFull(LoginDocResponce loginDocResponce) {
        SharedPreff.clearSocialLoginData();
        Intent signUp;
        if (loginDocResponce.getData().getUserType() == 1) {
            signUp = new Intent(this, HomeScreen.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
        } else if (loginDocResponce.getData().getUserType() == 3) {
            if (loginDocResponce.getData().getStaffDrStatus() == 0) {
                DialogPopUps.alertPopUp(this, getResources().getString(C1020R.string.your_doctor_has_suspended_your_account_please_contact_your_doctor_to_get_your_account_reinstated), getResources().getString(C1020R.string.ok), new C10543());
                return;
            }
            signUp = new Intent(this, StaffHomeScreen.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
        } else if (loginDocResponce.getData().getDrRequest() == 0) {
            signUp = new Intent(this, DoctorSearch.class);
            signUp.setFlags(268468224);
            signUp.putExtra(Constants.SHOW_BACK, false);
            startActivity(signUp);
            SharedPreff.saveFirstTimePharmacySelect(true);
            SharedPreff.saveFirstPharmacySelected(true);
        } else if (loginDocResponce.getData().getPharmacyName().equalsIgnoreCase("") || TextUtils.isEmpty(loginDocResponce.getData().getPharmacyName())) {
            Intent selectPharmacy = new Intent(this, SelectPharmacy.class);
            selectPharmacy.setFlags(268468224);
            selectPharmacy.putExtra(Constants.SHOW_BACK, false);
            startActivity(selectPharmacy);
        } else {
            signUp = new Intent(this, HomeScreenPatient.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.loginActivityPresenter.unSubscribeCallbacks();
        EventBus.getDefault().unregister(this);
        this.locationTrackerClass.stopLocationTracking();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showCompleteDialog(FcmToken fcmTokenObject) {
        this.fcmToken = fcmTokenObject.getFcmToken();
        Log.e("fcmToken ", " " + this.fcmToken);
    }

    public void onSocialLogin(int type, SocialLoginData loginData) {
        try {
            Log.e("loginData", new Gson().toJson((Object) loginData));
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
            if (this.fcmToken == null) {
                showError(getResources().getString(C1020R.string.fcm_token_null));
            } else if (loginData.getEmail() == null || TextUtils.isEmpty(loginData.getEmail())) {
                showError(getResources().getString(C1020R.string.We_are_unable_to_get_your_email_id_from_login_Pleaseuse_Google_or_manual_Login_to_access_the_app));
            } else {
                SharedPreff.saveSocialLoginData(loginData);
                this.loginActivityPresenter.checkValidation(loginData.getEmail().toLowerCase(), loginData.getSocialUserID(), loginData.getUserType());
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void socialLogin() {
        Intent createAcc = new Intent(this, CreateAccount.class);
        createAcc.putExtra(Constants.IS_DOC, this.isDoc);
        createAcc.putExtra(Constants.SOCIAL_MEDIA, true);
        startActivity(createAcc);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            this.callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (requestCode == RC_SIGN_IN) {
            handleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }
    }

    protected void onStart() {
        super.onStart();
        this.mGoogleApiClient.connect();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.getStatus().toString());
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("acct", "" + acct);
            Object loginData = new SocialLoginData();
            loginData.setUserType("G");
            loginData.setSocialUserID(acct.getId());
            loginData.setEmail(acct.getEmail());
            Log.e("acct", "" + new Gson().toJson(loginData));
            try {
                if (acct.getDisplayName() != null) {
                    loginData.setFirst_name(getFirstNameAndLastName(acct.getDisplayName(), true));
                    loginData.setLast_name(getFirstNameAndLastName(acct.getDisplayName(), false));
                } else {
                    loginData.setFirst_name("");
                    loginData.setLast_name("");
                }
            } catch (Exception e) {
                e.printStackTrace();
                loginData.setFirst_name(acct.getDisplayName());
                loginData.setLast_name("");
            }
            if (acct.getPhotoUrl() != null) {
                loginData.setPic_big(acct.getPhotoUrl().toString());
            }
            signOut();
            if (this.fcmToken != null) {
                SharedPreff.saveSocialLoginData(loginData);
                this.loginActivityPresenter.checkValidation(loginData.getEmail().toLowerCase(), loginData.getSocialUserID(), loginData.getUserType());
                return;
            }
            showError(getResources().getString(C1020R.string.fcm_token_null));
        }
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public String getFirstNameAndLastName(String name, boolean isFirstName) {
        String result = name;
        String[] splited = name.split("\\s+");
        if (splited.length == 1) {
            if (isFirstName) {
                return splited[0];
            }
            return "";
        } else if (splited.length == 2) {
            if (isFirstName) {
                return splited[0];
            }
            return splited[1];
        } else if (isFirstName) {
            return splited[0];
        } else {
            return splited[1];
        }
    }
}
