package com.trigma.tiktok.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorSearch;
import com.trigma.tiktok.easyimage.DefaultCallback;
import com.trigma.tiktok.easyimage.EasyImageTikTok;
import com.trigma.tiktok.easyimage.EasyImageTikTok.ImageSource;
import com.trigma.tiktok.facebook.SocialLoginData;
import com.trigma.tiktok.model.FcmToken;
import com.trigma.tiktok.presenter.CreateAccountPresenter;
import com.trigma.tiktok.presenter.CreateAccountPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import com.trigma.tiktok.utils.SoftKeyboard;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CreateAccount extends BaseActivity implements OnClickListener, OnFocusChangeListener {
    private CreateAccountPresenter createAccountPresenter;
    private EditText et_address;
    private EditText et_bio;
    private EditText et_city;
    private EditText et_confirm_pwd;
    private EditText et_dob;
    private EditText et_email;
    private EditText et_first_name;
    private EditText et_language;
    private EditText et_last_name;
    private EditText et_phone;
    private EditText et_pwd;
    private EditText et_qualification;
    private EditText et_speciality;
    private TextView et_state;
    private EditText et_zip;
    private String fcmToken = null;
    private CheckBox female_checkbox;
    private String gender;
    private ImageView img_back;
    private RelativeLayout img_header;
    private CircleImageView img_profile;
    private int isDoc = 0;
    private boolean isMediaSignup = false;
    private LinearLayout linear_done;
    private SocialLoginData loginData;
    private CheckBox male_checkbox;
    public NetWorkingService netWorkingService;
    private RelativeLayout parent;
    private String profile_pic;
    private RelativeLayout rel_bio;
    private RelativeLayout rel_female;
    private RelativeLayout rel_languauge;
    private RelativeLayout rel_male;
    private RelativeLayout rel_qaulification;
    private RelativeLayout rel_speciality;
    private RelativeLayout relative_dob;
    private RelativeLayout rv_confirm_passwd;
    private RelativeLayout rv_passwd;
    private ScrollView scrollView;
    SoftKeyboard softKeyboard;
    private String st_address;
    private String st_bio;
    private String st_city;
    private String st_confirm_pwd;
    private String st_country_code;
    private String st_email;
    private String st_first_name;
    private String st_language;
    private String st_last_name;
    private String st_phone;
    private String st_pwd;
    private String st_qualification;
    private String st_speciality;
    private String st_state;
    private String st_zip;
    private EditText tv_country_code;

    class C10321 implements Target {
        C10321() {
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
            CreateAccount.this.img_profile.setImageBitmap(bitmap);
            CreateAccount.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            try {
                CreateAccount.this.convertingFirstImageToString(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onBitmapFailed(Drawable errorDrawable) {
        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }

    class C10332 implements AlertCallBackWithButtonsInterface {
        C10332() {
        }

        public void positiveClick() {
            EasyImageTikTok.openGallery(CreateAccount.this, 0);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            EasyImageTikTok.openCamera(CreateAccount.this, 0);
        }
    }

    class C10343 extends DefaultCallback {
        C10343() {
        }

        public void onImagePickerError(Exception e, ImageSource source, int type) {
            CreateAccount.this.showToastError(CreateAccount.this.getResources().getString(C1020R.string.sorry_unable_to_pick_the_image));
            CreateAccount.this.createAccountPresenter.setBase64("");
        }

        public void onImagePicked(File imageFile, ImageSource source, int type) {
            Picasso.with(CreateAccount.this).load(imageFile).centerCrop().resize(Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION).into(CreateAccount.this.img_profile);
            CreateAccount.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            CreateAccount.this.createAccountPresenter.setBase64(CreateAccount.this.encodeImage(imageFile));
        }

        public void onCanceled(ImageSource source, int type) {
            if (source == ImageSource.CAMERA) {
                File photoFile = EasyImageTikTok.lastlyTakenButCanceledPhoto(CreateAccount.this);
                if (photoFile != null) {
                    photoFile.delete();
                }
            }
        }
    }

    class C10354 implements AlertCallBackInterface {
        C10354() {
        }

        public void neutralClick() {
            Intent signUp = new Intent(CreateAccount.this, DashBoard.class);
            signUp.setFlags(268468224);
            CreateAccount.this.startActivity(signUp);
        }
    }

    class C10375 implements AlertCallBackInterface {

        class C10361 implements AlertCallBackInterface {
            C10361() {
            }

            public void neutralClick() {
                CreateAccount.this.createAccountPresenter.agreeApiCall(SharedPreff.getUerID(), AppEventsConstants.EVENT_PARAM_VALUE_YES);
            }
        }

        C10375() {
        }

        public void neutralClick() {
            if (CreateAccount.this.isMediaSignup) {
                DialogPopUps.agreementPopUp(CreateAccount.this, CreateAccount.this.getResources().getString(C1020R.string.agreement_text), true, new C10361());
                return;
            }
            Intent signUp = new Intent(CreateAccount.this, DoctorSearch.class);
            signUp.setFlags(268468224);
            signUp.putExtra(Constants.SHOW_BACK, false);
            CreateAccount.this.startActivity(signUp);
            SharedPreff.saveFirstTimeDoctorSearch(true);
            SharedPreff.saveFirstTimePharmacySelect(true);
            SharedPreff.saveFirstPharmacySelected(true);
        }
    }

    class C10386 implements AlertCallBackInterface {
        C10386() {
        }

        public void neutralClick() {
            CreateAccount.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }
    }

    public class CustomWatcher implements TextWatcher {
        private int characters;
        private View view;

        class C10401 implements AlertCallBackInterface {
            C10401() {
            }

            public void neutralClick() {
            }
        }

        class C10412 implements AlertCallBackInterface {
            C10412() {
            }

            public void neutralClick() {
            }
        }

        class C10423 implements AlertCallBackInterface {
            C10423() {
            }

            public void neutralClick() {
            }
        }

        class C10434 implements AlertCallBackInterface {
            C10434() {
            }

            public void neutralClick() {
            }
        }

        class C10445 implements AlertCallBackInterface {
            C10445() {
            }

            public void neutralClick() {
            }
        }

        class C10456 implements AlertCallBackInterface {
            C10456() {
            }

            public void neutralClick() {
            }
        }

        class C10467 implements AlertCallBackInterface {
            C10467() {
            }

            public void neutralClick() {
            }
        }

        class C10478 implements AlertCallBackInterface {
            C10478() {
            }

            public void neutralClick() {
            }
        }

        public CustomWatcher(View view, int characters) {
            this.view = view;
            this.characters = characters;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (this.view.getId()) {
                case C1020R.id.et_first_name:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_first_name.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10401());
                        CreateAccount.this.et_first_name.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_first_name.setSelection(CreateAccount.this.et_first_name.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_last_name:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_last_name.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10412());
                        CreateAccount.this.et_last_name.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_last_name.setSelection(CreateAccount.this.et_last_name.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_address:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_address.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10423());
                        CreateAccount.this.et_address.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_address.setSelection(CreateAccount.this.et_address.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_zip:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_zip.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10478());
                        CreateAccount.this.et_zip.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_zip.setSelection(CreateAccount.this.et_zip.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_city:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_city.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10434());
                        CreateAccount.this.et_city.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_city.setSelection(CreateAccount.this.et_city.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_qualification:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_qualification.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10445());
                        CreateAccount.this.et_qualification.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_qualification.setSelection(CreateAccount.this.et_qualification.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_speciality:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_speciality.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10456());
                        CreateAccount.this.et_speciality.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_speciality.setSelection(CreateAccount.this.et_speciality.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_language:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(CreateAccount.this, CommonUtils.capWordCase(CreateAccount.this.et_language.getHint().toString()) + " " + CreateAccount.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + CreateAccount.this.getResources().getString(C1020R.string.characters), CreateAccount.this.getResources().getString(C1020R.string.ok_dialog), new C10467());
                        CreateAccount.this.et_language.setText(text.substring(0, this.characters));
                        CreateAccount.this.et_language.setSelection(CreateAccount.this.et_language.getText().toString().length());
                        break;
                    }
                    break;
                case C1020R.id.et_bio:
                    if (text.length() > this.characters) {
                        break;
                    }
                    break;
            }
            CreateAccount.this.validate();
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.create_account);
        EventBus.getDefault().register(this);
        this.loginData = SharedPreff.getSocialLoginData();
        this.fcmToken = SharedPreff.getDeviceToken();
        try {
            if (this.fcmToken.equalsIgnoreCase("") || this.fcmToken.isEmpty() || this.fcmToken == null) {
                if (FirebaseInstanceId.getInstance().getToken() != null) {
                    this.fcmToken = FirebaseInstanceId.getInstance().getToken();
                    if (!(this.fcmToken.equalsIgnoreCase("") && this.fcmToken.isEmpty())) {
                        SharedPreff.saveDeviceToken(this.fcmToken);
                    }
                    Log.e("fcmToken", "XX");
                } else {
                    Log.e("getToken", "null");
                }
                if (getIntent().hasExtra(Constants.IS_DOC)) {
                    this.isDoc = getIntent().getIntExtra(Constants.IS_DOC, 0);
                }
                if (getIntent().hasExtra(Constants.SOCIAL_MEDIA)) {
                    this.isMediaSignup = getIntent().getBooleanExtra(Constants.SOCIAL_MEDIA, false);
                }
                init();
                this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
                this.createAccountPresenter = new CreateAccountPresenterImp(this, this.netWorkingService, this.isDoc);
                EasyImageTikTok.configuration(this).setImagesFolderName(getResources().getString(C1020R.string.app_name)).saveInAppExternalFilesDir().saveInRootPicturesDirectory();
                if (this.isMediaSignup) {
                    this.rv_confirm_passwd.setVisibility(8);
                    this.rv_passwd.setVisibility(8);
                    settingData();
                }
            }
            Log.e("fcmToken", "preff!null");
            if (getIntent().hasExtra(Constants.IS_DOC)) {
                this.isDoc = getIntent().getIntExtra(Constants.IS_DOC, 0);
            }
            if (getIntent().hasExtra(Constants.SOCIAL_MEDIA)) {
                this.isMediaSignup = getIntent().getBooleanExtra(Constants.SOCIAL_MEDIA, false);
            }
            init();
            this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
            this.createAccountPresenter = new CreateAccountPresenterImp(this, this.netWorkingService, this.isDoc);
            EasyImageTikTok.configuration(this).setImagesFolderName(getResources().getString(C1020R.string.app_name)).saveInAppExternalFilesDir().saveInRootPicturesDirectory();
            if (this.isMediaSignup) {
                this.rv_confirm_passwd.setVisibility(8);
                this.rv_passwd.setVisibility(8);
                settingData();
            }
        } catch (Exception e) {
            this.fcmToken = "";
            Log.e("fcmToken", "Exception");
        }
    }

    private void init() {
        this.et_first_name = (EditText) findViewById(C1020R.id.et_first_name);
        this.et_last_name = (EditText) findViewById(C1020R.id.et_last_name);
        this.et_email = (EditText) findViewById(C1020R.id.et_email);
        this.et_pwd = (EditText) findViewById(C1020R.id.et_pwd);
        this.et_phone = (EditText) findViewById(C1020R.id.et_phone);
        this.et_address = (EditText) findViewById(C1020R.id.et_address);
        this.et_zip = (EditText) findViewById(C1020R.id.et_zip);
        this.et_city = (EditText) findViewById(C1020R.id.et_city);
        this.et_state = (TextView) findViewById(C1020R.id.et_state);
        this.et_qualification = (EditText) findViewById(C1020R.id.et_qualification);
        this.et_speciality = (EditText) findViewById(C1020R.id.et_speciality);
        this.et_language = (EditText) findViewById(C1020R.id.et_language);
        this.et_bio = (EditText) findViewById(C1020R.id.et_bio);
        this.et_confirm_pwd = (EditText) findViewById(C1020R.id.et_confirm_pwd);
        this.et_dob = (EditText) findViewById(C1020R.id.et_dob);
        this.scrollView = (ScrollView) findViewById(C1020R.id.scrollView);
        this.et_first_name.addTextChangedListener(new CustomWatcher(this.et_first_name, 35));
        this.et_last_name.addTextChangedListener(new CustomWatcher(this.et_last_name, 35));
        this.et_email.addTextChangedListener(new CustomWatcher(this.et_email, 35));
        this.et_pwd.addTextChangedListener(new CustomWatcher(this.et_pwd, 35));
        this.et_phone.addTextChangedListener(new CustomWatcher(this.et_phone, 35));
        this.et_address.addTextChangedListener(new CustomWatcher(this.et_address, 35));
        this.et_zip.addTextChangedListener(new CustomWatcher(this.et_zip, 9));
        this.et_city.addTextChangedListener(new CustomWatcher(this.et_city, 35));
        this.et_state.addTextChangedListener(new CustomWatcher(this.et_state, 35));
        this.et_qualification.addTextChangedListener(new CustomWatcher(this.et_qualification, 35));
        this.et_speciality.addTextChangedListener(new CustomWatcher(this.et_speciality, 35));
        this.et_language.addTextChangedListener(new CustomWatcher(this.et_language, 35));
        this.et_bio.addTextChangedListener(new CustomWatcher(this.et_bio, 35));
        this.et_confirm_pwd.addTextChangedListener(new CustomWatcher(this.et_confirm_pwd, 35));
        this.rel_male = (RelativeLayout) findViewById(C1020R.id.rel_male);
        this.rel_female = (RelativeLayout) findViewById(C1020R.id.rel_female);
        this.parent = (RelativeLayout) findViewById(C1020R.id.parent);
        this.img_header = (RelativeLayout) findViewById(C1020R.id.img_header);
        this.rv_passwd = (RelativeLayout) findViewById(C1020R.id.rv_passwd);
        this.rv_confirm_passwd = (RelativeLayout) findViewById(C1020R.id.rv_confirm_passwd);
        this.relative_dob = (RelativeLayout) findViewById(C1020R.id.relative_dob);
        this.rel_qaulification = (RelativeLayout) findViewById(C1020R.id.rel_qaulification);
        this.rel_languauge = (RelativeLayout) findViewById(C1020R.id.rel_languauge);
        this.rel_speciality = (RelativeLayout) findViewById(C1020R.id.rel_speciality);
        this.rel_bio = (RelativeLayout) findViewById(C1020R.id.rel_bio);
        this.tv_country_code = (EditText) findViewById(C1020R.id.tv_country_code);
        this.img_profile = (CircleImageView) findViewById(C1020R.id.img_profile);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.linear_done = (LinearLayout) findViewById(C1020R.id.linear_done);
        this.male_checkbox = (CheckBox) findViewById(C1020R.id.male_checkbox);
        this.female_checkbox = (CheckBox) findViewById(C1020R.id.female_checkbox);
        InputMethodManager im = (InputMethodManager) getSystemService("input_method");
        clickListners();
        hideViewDoc(this.isDoc);
    }

    private void settingData() {
        if (!(this.loginData.getEmail() == null || TextUtils.isEmpty(this.loginData.getEmail()))) {
            this.et_email.setText(this.loginData.getEmail().toLowerCase());
            this.et_email.setEnabled(false);
        }
        if (!(this.loginData.getFirst_name() == null || TextUtils.isEmpty(this.loginData.getFirst_name()))) {
            this.et_first_name.setText(this.loginData.getFirst_name().toLowerCase());
            this.et_first_name.setEnabled(false);
        }
        if (!(this.loginData.getLast_name() == null || TextUtils.isEmpty(this.loginData.getLast_name()))) {
            this.et_last_name.setText(this.loginData.getLast_name().toLowerCase());
            this.et_last_name.setEnabled(false);
        }
        this.et_pwd.setText(this.loginData.getSocialUserID());
        this.et_confirm_pwd.setText(this.loginData.getSocialUserID());
        this.createAccountPresenter.settingMediaId(this.loginData.getSocialUserID());
        this.createAccountPresenter.setUserType(this.loginData.getUserType());
        Picasso.with(this).load(this.loginData.getPic_big()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(new C10321());
    }

    private void hideViewDoc(int isDoc) {
        if (isDoc == 0) {
            this.relative_dob.setVisibility(0);
            this.rel_qaulification.setVisibility(8);
            this.rel_languauge.setVisibility(8);
            this.rel_speciality.setVisibility(8);
            this.rel_bio.setVisibility(8);
            return;
        }
        this.relative_dob.setVisibility(8);
        this.rel_qaulification.setVisibility(0);
        this.rel_languauge.setVisibility(0);
        this.rel_speciality.setVisibility(0);
        this.rel_bio.setVisibility(0);
    }

    private void clickListners() {
        this.rel_male.setOnClickListener(this);
        this.rel_female.setOnClickListener(this);
        this.tv_country_code.setOnClickListener(this);
        this.img_profile.setOnClickListener(this);
        this.linear_done.setOnClickListener(this);
        this.linear_done.setEnabled(false);
        this.parent.setOnClickListener(this);
        this.et_state.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.et_dob.setOnClickListener(this);
        this.et_dob.setOnFocusChangeListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.parent:
                CommonUtils.hideKeyboard(this.et_email, this);
                return;
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.img_profile:
                DialogPopUps.openCamera(this, new C10332());
                return;
            case C1020R.id.linear_done:
                this.st_first_name = this.et_first_name.getText().toString();
                this.st_last_name = this.et_last_name.getText().toString();
                this.st_email = this.et_email.getText().toString();
                this.st_pwd = this.et_pwd.getText().toString();
                this.st_address = this.et_address.getText().toString();
                this.st_zip = this.et_zip.getText().toString();
                this.st_city = this.et_city.getText().toString();
                this.st_state = this.et_state.getText().toString();
                this.st_qualification = this.et_qualification.getText().toString();
                this.st_speciality = this.et_speciality.getText().toString();
                this.st_language = this.et_language.getText().toString();
                this.st_bio = this.et_bio.getText().toString();
                this.st_confirm_pwd = this.et_confirm_pwd.getText().toString();
                this.st_country_code = this.tv_country_code.getText().toString();
                this.st_phone = this.et_phone.getText().toString();
                if (this.fcmToken != null) {
                    this.createAccountPresenter.doneButonFunctionality(this.st_first_name, this.st_last_name, this.st_email, this.st_pwd, this.st_address, this.st_zip, this.st_city, this.st_state, this.st_qualification, this.st_speciality, this.st_language, this.st_bio, this.st_confirm_pwd, this.st_country_code, this.gender, this.st_phone);
                    return;
                } else {
                    showError(getResources().getString(C1020R.string.fcm_token_null));
                    return;
                }
            case C1020R.id.rel_male:
                this.male_checkbox.setChecked(true);
                this.female_checkbox.setChecked(false);
                this.gender = "male";
                validate();
                return;
            case C1020R.id.rel_female:
                this.male_checkbox.setChecked(false);
                this.female_checkbox.setChecked(true);
                this.gender = "female";
                validate();
                return;
            case C1020R.id.et_dob:
                this.createAccountPresenter.openDatePicker();
                return;
            case C1020R.id.et_state:
                CommonUtils.hideKeyboard(this.et_email, this);
                this.createAccountPresenter.showStateDialog(this.et_state.getText().toString());
                return;
            default:
                return;
        }
    }

    private void validate() {
        this.st_first_name = this.et_first_name.getText().toString();
        this.st_last_name = this.et_last_name.getText().toString();
        this.st_email = this.et_email.getText().toString();
        this.st_pwd = this.et_pwd.getText().toString();
        this.st_address = this.et_address.getText().toString();
        this.st_zip = this.et_zip.getText().toString();
        this.st_city = this.et_city.getText().toString();
        this.st_state = this.et_state.getText().toString();
        this.st_qualification = this.et_qualification.getText().toString();
        this.st_speciality = this.et_speciality.getText().toString();
        this.st_language = this.et_language.getText().toString();
        this.st_bio = this.et_bio.getText().toString();
        this.st_confirm_pwd = this.et_confirm_pwd.getText().toString();
        this.st_country_code = this.tv_country_code.getText().toString();
        this.st_phone = this.et_phone.getText().toString();
        this.createAccountPresenter.validate(this.st_first_name, this.st_last_name, this.st_email, this.st_pwd, this.st_address, this.st_zip, this.st_city, this.st_state, this.st_qualification, this.st_speciality, this.st_language, this.st_bio, this.st_confirm_pwd, this.st_country_code, this.gender, this.st_phone);
        Log.e("st_first_name", this.st_first_name);
        Log.e("st_last_name", this.st_last_name);
        Log.e("st_email", this.st_email);
        Log.e("st_pwd", this.st_pwd);
        Log.e("st_address", this.st_address);
        Log.e("st_zip", this.st_zip);
        Log.e("st_city", this.st_city);
        Log.e("st_state", this.st_state);
        Log.e("st_qualification", this.st_qualification);
        Log.e("st_speciality", this.st_speciality);
        Log.e("st_language", this.st_language);
        Log.e("st_bio", this.st_bio);
        Log.e("st_confirm_pwd", this.st_confirm_pwd);
        Log.e("st_country_code", this.st_country_code);
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

    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case C1020R.id.et_dob:
                if (hasFocus) {
                    this.createAccountPresenter.openDatePicker();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setDob(int year, String monthOfYear, int dayOfMonth) {
        this.et_dob.setText(monthOfYear + " " + dayOfMonth + "," + year);
        this.et_email.requestFocus();
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

    protected void onDestroy() {
        super.onDestroy();
        this.createAccountPresenter.unSubscribeCallbacks();
        EasyImageTikTok.clearConfiguration(this);
        EventBus.getDefault().unregister(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImageTikTok.handleActivityResult(requestCode, resultCode, data, this, new C10343());
    }

    private String encodeImage(File imagefile) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }

    public void stateSelecetd(String st_state) {
        this.st_state = st_state;
        this.et_state.setText(st_state);
        validate();
    }

    public void signUpSuccessful() {
        if (this.isDoc == 1) {
            DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.account_created), getResources().getString(C1020R.string.doc_account_created_msg), new C10354());
        } else {
            DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.confirm), getResources().getString(C1020R.string.patient_successfully_create_dialog_text), new C10375());
        }
    }

    public void goToDoctorSearch() {
        Intent signUp = new Intent(this, DoctorSearch.class);
        signUp.setFlags(268468224);
        signUp.putExtra(Constants.SHOW_BACK, false);
        startActivity(signUp);
        SharedPreff.saveFirstTimeDoctorSearch(true);
        SharedPreff.saveFirstTimePharmacySelect(true);
        SharedPreff.saveFirstPharmacySelected(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showCompleteDialog(FcmToken fcmTokenObject) {
        this.fcmToken = fcmTokenObject.getFcmToken();
        Log.e("fcmToken ", " " + this.fcmToken);
    }

    protected void onResume() {
        super.onResume();
        if (!((LocationManager) getSystemService("location")).isProviderEnabled("gps")) {
            DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.this_application_requires_location_services_to_work), new C10386());
        }
    }

    public void convertingFirstImageToString(final Bitmap bm) {
        new AsyncTask() {
            protected Object doInBackground(Object[] params) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(CompressFormat.JPEG, 100, baos);
                CreateAccount.this.createAccountPresenter.setBase64(Base64.encodeToString(baos.toByteArray(), 0));
                return null;
            }
        }.execute(new Object[]{""});
    }
}
