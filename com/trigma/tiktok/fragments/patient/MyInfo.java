package com.trigma.tiktok.fragments.patient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.ChangePassword;
import com.trigma.tiktok.activity.patient.PharmacyDetail;
import com.trigma.tiktok.activity.patient.SelectPharmacy;
import com.trigma.tiktok.easyimage.DefaultCallback;
import com.trigma.tiktok.easyimage.EasyImageTikTok;
import com.trigma.tiktok.easyimage.EasyImageTikTok.ImageSource;
import com.trigma.tiktok.fragments.BaseFragment;
import com.trigma.tiktok.model.DocUserDetail;
import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.presenter.patient.MyInfoPresenter;
import com.trigma.tiktok.presenter.patient.MyInfoPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MyInfo extends BaseFragment implements OnClickListener, OnFocusChangeListener {
    private static final int REQUEST_CODE = 100;
    private String PharmacyId;
    private String PharmacyName;
    private Activity activity;
    private TextView changePassword;
    private DocUserDetail docUserDetail;
    private EditText et_address;
    private EditText et_dob;
    private EditText et_email;
    private EditText et_first_name;
    private EditText et_last_name;
    private TextView et_pharmacy;
    private EditText et_phone;
    private EditText et_zip;
    public File imageFile;
    private ImageView img_back;
    private ImageView img_edit;
    private RelativeLayout img_header;
    private CircleImageView img_profile;
    private boolean isEditable = false;
    private LinearLayout l_1;
    private LinearLayout l_2;
    private LinearLayout l_3;
    private LinearLayout l_4;
    private LinearLayout l_5;
    private LinearLayout l_55;
    private LinearLayout l_6;
    private LinearLayout l_7;
    private LinearLayout l_8;
    private CardView last_name_card;
    private LinearLayout linear_done;
    private MyInfoPresenter myProfilePresenter;
    public NetWorkingService netWorkingService;
    private RelativeLayout parent;
    private ScrollView scrollView;
    private String st_Pharmacy;
    private String st_address;
    private String st_country_code;
    private String st_dob;
    private String st_email;
    private String st_first_name;
    private String st_last_name;
    private String st_phone;
    private String st_zip;
    private String title;
    private TextView tv_confirm;
    private EditText tv_country_code;
    private TextView tv_last_name;
    private TextView tv_name;
    private TextView tv_pharmacy_head;
    private TextView tv_title;

    class C11861 implements Target {
        C11861() {
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
            MyInfo.this.img_profile.setImageBitmap(bitmap);
            MyInfo.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            try {
                MyInfo.this.convertingFirstImageToString(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onBitmapFailed(Drawable errorDrawable) {
        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }

    class C11872 implements Target {
        C11872() {
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
            MyInfo.this.img_profile.setImageBitmap(bitmap);
            MyInfo.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            try {
                MyInfo.this.convertingFirstImageToString(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onBitmapFailed(Drawable errorDrawable) {
        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }

    class C11883 implements AlertCallBackWithButtonsInterface {
        C11883() {
        }

        public void positiveClick() {
            EasyImageTikTok.openGallery(MyInfo.this, 0);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            EasyImageTikTok.openCamera(MyInfo.this, 0);
        }
    }

    class C11894 extends DefaultCallback {
        C11894() {
        }

        public void onImagePickerError(Exception e, ImageSource source, int type) {
            MyInfo.this.showToastError(MyInfo.this.getResources().getString(C1020R.string.sorry_unable_to_pick_the_image));
            MyInfo.this.myProfilePresenter.setBase64("");
        }

        public void onImagePicked(File imageFilee, ImageSource source, int type) {
            MyInfo.this.imageFile = imageFilee;
            Picasso.with(MyInfo.this.activity).load(imageFilee).centerCrop().resize(Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION).into(MyInfo.this.img_profile);
            MyInfo.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            MyInfo.this.encodeImage(imageFilee);
        }

        public void onCanceled(ImageSource source, int type) {
            if (source == ImageSource.CAMERA) {
                File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MyInfo.this.activity);
                if (photoFile != null) {
                    photoFile.delete();
                }
            }
        }
    }

    class C11916 implements AlertCallBackInterface {
        C11916() {
        }

        public void neutralClick() {
        }
    }

    public class CustomWatcher implements TextWatcher {
        private int characters;
        private View view;

        class C11921 implements AlertCallBackInterface {
            C11921() {
            }

            public void neutralClick() {
            }
        }

        class C11932 implements AlertCallBackInterface {
            C11932() {
            }

            public void neutralClick() {
            }
        }

        class C11943 implements AlertCallBackInterface {
            C11943() {
            }

            public void neutralClick() {
            }
        }

        class C11954 implements AlertCallBackInterface {
            C11954() {
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
                        DialogPopUps.alertPopUp(MyInfo.this.activity, MyInfo.this.activity.getResources().getString(C1020R.string.first_name_c) + " " + MyInfo.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + MyInfo.this.getResources().getString(C1020R.string.characters), MyInfo.this.getResources().getString(C1020R.string.ok_dialog), new C11921());
                        MyInfo.this.et_first_name.setText(text.substring(0, this.characters - 1));
                        MyInfo.this.et_first_name.setSelection(MyInfo.this.et_first_name.getText().toString().length());
                        return;
                    }
                    return;
                case C1020R.id.et_last_name:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(MyInfo.this.activity, MyInfo.this.activity.getResources().getString(C1020R.string.last_name_c) + " " + MyInfo.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + MyInfo.this.getResources().getString(C1020R.string.characters), MyInfo.this.getResources().getString(C1020R.string.ok_dialog), new C11932());
                        MyInfo.this.et_last_name.setText(text.substring(0, this.characters - 1));
                        MyInfo.this.et_last_name.setSelection(MyInfo.this.et_last_name.getText().toString().length());
                        return;
                    }
                    return;
                case C1020R.id.et_address:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(MyInfo.this.activity, MyInfo.this.activity.getResources().getString(C1020R.string.address_field_c) + " " + MyInfo.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + MyInfo.this.getResources().getString(C1020R.string.characters), MyInfo.this.getResources().getString(C1020R.string.ok_dialog), new C11943());
                        MyInfo.this.et_address.setText(text.substring(0, this.characters - 1));
                        MyInfo.this.et_address.setSelection(MyInfo.this.et_address.getText().toString().length());
                        return;
                    }
                    return;
                case C1020R.id.et_zip:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(MyInfo.this.activity, MyInfo.this.activity.getResources().getString(C1020R.string.zipcode_c) + " " + MyInfo.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + MyInfo.this.getResources().getString(C1020R.string.characters), MyInfo.this.getResources().getString(C1020R.string.ok_dialog), new C11954());
                        MyInfo.this.et_zip.setText(text.substring(0, this.characters - 1));
                        MyInfo.this.et_zip.setSelection(MyInfo.this.et_zip.getText().toString().length());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public interface ProfileImageSetting {
        void settingImage(File file);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.my_information, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.scrollView = (ScrollView) screen.findViewById(C1020R.id.scrollView);
        this.et_first_name = (EditText) screen.findViewById(C1020R.id.et_first_name);
        this.et_last_name = (EditText) screen.findViewById(C1020R.id.et_last_name);
        this.et_email = (EditText) screen.findViewById(C1020R.id.et_email);
        this.et_phone = (EditText) screen.findViewById(C1020R.id.et_phone);
        this.et_dob = (EditText) screen.findViewById(C1020R.id.et_dob);
        this.tv_country_code = (EditText) screen.findViewById(C1020R.id.tv_country_code);
        this.et_address = (EditText) screen.findViewById(C1020R.id.et_address);
        this.et_zip = (EditText) screen.findViewById(C1020R.id.et_zip);
        this.et_pharmacy = (TextView) screen.findViewById(C1020R.id.et_pharmacy);
        this.img_back = (ImageView) screen.findViewById(C1020R.id.img_back);
        this.img_edit = (ImageView) screen.findViewById(C1020R.id.img_edit);
        this.tv_last_name = (TextView) screen.findViewById(C1020R.id.tv_last_name);
        this.tv_name = (TextView) screen.findViewById(C1020R.id.tv_name);
        this.tv_title = (TextView) screen.findViewById(C1020R.id.tv_title);
        this.tv_confirm = (TextView) screen.findViewById(C1020R.id.tv_confirm);
        this.tv_pharmacy_head = (TextView) screen.findViewById(C1020R.id.tv_pharmacy_head);
        this.changePassword = (TextView) screen.findViewById(C1020R.id.changePassword);
        this.parent = (RelativeLayout) screen.findViewById(C1020R.id.parent);
        this.img_header = (RelativeLayout) screen.findViewById(C1020R.id.img_header);
        this.img_edit.setEnabled(false);
        this.img_profile = (CircleImageView) screen.findViewById(C1020R.id.img_profile);
        this.linear_done = (LinearLayout) screen.findViewById(C1020R.id.linear_done);
        this.l_1 = (LinearLayout) screen.findViewById(C1020R.id.l_1);
        this.l_2 = (LinearLayout) screen.findViewById(C1020R.id.l_2);
        this.l_3 = (LinearLayout) screen.findViewById(C1020R.id.l_3);
        this.l_4 = (LinearLayout) screen.findViewById(C1020R.id.l_4);
        this.l_55 = (LinearLayout) screen.findViewById(C1020R.id.l_55);
        this.l_5 = (LinearLayout) screen.findViewById(C1020R.id.l_5);
        this.l_6 = (LinearLayout) screen.findViewById(C1020R.id.l_6);
        this.l_7 = (LinearLayout) screen.findViewById(C1020R.id.l_7);
        this.l_8 = (LinearLayout) screen.findViewById(C1020R.id.l_8);
        this.last_name_card = (CardView) screen.findViewById(C1020R.id.last_name_card);
        clickListners();
    }

    public void setUserData(DocUserDetail dataa) {
        this.docUserDetail = dataa;
        if (this.docUserDetail != null) {
            Log.e("MyINfo", new Gson().toJson(this.docUserDetail));
            this.PharmacyId = this.docUserDetail.getPharmacyId();
            this.PharmacyName = this.docUserDetail.getPharmacyName();
            this.img_edit.setEnabled(true);
            if (this.isEditable) {
                this.et_first_name.setText(this.docUserDetail.getFirstName());
            } else {
                this.et_first_name.setText(this.docUserDetail.getName());
            }
            this.et_last_name.setText(this.docUserDetail.getLastName());
            this.et_email.setText(this.docUserDetail.getEmail());
            this.et_phone.setText(this.docUserDetail.getMobile());
            this.et_dob.setText(this.docUserDetail.getDOB());
            this.tv_country_code.setText(this.docUserDetail.getCode());
            this.et_address.setText(this.docUserDetail.getAddress());
            this.et_zip.setText(this.docUserDetail.getZipcode());
            this.et_pharmacy.setText(this.docUserDetail.getPharmacyName());
            if (this.docUserDetail.getProfilePic().contains("http") || this.docUserDetail.getProfilePic().contains("https")) {
                Picasso.with(this.activity).load(this.docUserDetail.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(new C11861());
            } else {
                Picasso.with(this.activity).load(Constants.HTTP + this.docUserDetail.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(new C11872());
            }
        }
        this.et_first_name.addTextChangedListener(new CustomWatcher(this.et_first_name, 35));
        this.et_last_name.addTextChangedListener(new CustomWatcher(this.et_last_name, 35));
        this.et_address.addTextChangedListener(new CustomWatcher(this.et_last_name, 35));
        this.et_zip.addTextChangedListener(new CustomWatcher(this.et_last_name, 35));
    }

    public void hideChangePassword() {
        this.changePassword.setVisibility(8);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        CommonUtils.closingKeyboard(this.activity);
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.myProfilePresenter = new MyInfoPresenterImp(this, this.netWorkingService, 1);
        this.myProfilePresenter.fetchDetail();
        enableFields(false);
        if (getArguments() != null && getArguments().containsKey("title")) {
            this.title = getArguments().getString("title");
            this.tv_title.setText(this.title);
            enableFields(true);
        }
        this.myProfilePresenter.checkSocialMediaUser();
    }

    public void onResume() {
        super.onResume();
        if (this.myProfilePresenter != null) {
            this.myProfilePresenter.checkSocialMediaUser();
        }
    }

    public void enableFields(boolean enable) {
        this.isEditable = enable;
        this.et_first_name.setEnabled(enable);
        this.et_last_name.setEnabled(enable);
        this.et_first_name.requestFocus();
        this.et_email.setEnabled(false);
        this.et_phone.setEnabled(enable);
        this.et_dob.setEnabled(enable);
        this.tv_country_code.setEnabled(enable);
        this.et_address.setEnabled(enable);
        this.et_zip.setEnabled(enable);
        this.img_profile.setEnabled(enable);
        if (enable) {
            this.tv_title.setText(this.activity.getResources().getString(C1020R.string.edit_information));
            this.tv_pharmacy_head.setText(this.activity.getResources().getString(C1020R.string.edit_pharmacy));
            this.img_edit.setVisibility(8);
            this.tv_name.setText(this.activity.getResources().getString(C1020R.string.first_name_c));
            CommonUtils.showKeyboard(this.activity);
            this.last_name_card.setVisibility(0);
            try {
                if (this.docUserDetail != null) {
                    this.et_first_name.setText(this.docUserDetail.getFirstName());
                }
                this.et_first_name.setSelection(this.et_first_name.getText().toString().length());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.l_1.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.l_2.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
            this.l_3.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.l_4.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
            this.l_5.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.l_55.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.l_6.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
            this.l_7.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.l_8.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
            this.tv_confirm.setText(getResources().getString(C1020R.string.save));
            this.changePassword.setVisibility(8);
            return;
        }
        this.tv_pharmacy_head.setText(this.activity.getResources().getString(C1020R.string.pharmacy));
        this.changePassword.setVisibility(0);
        this.tv_confirm.setText(getResources().getString(C1020R.string.self_schedule_appointment));
        this.tv_title.setText(this.activity.getResources().getString(C1020R.string.my_information));
        this.img_edit.setVisibility(0);
        this.last_name_card.setVisibility(8);
        this.tv_name.setText(this.activity.getResources().getString(C1020R.string.name));
        try {
            if (this.docUserDetail != null) {
                this.et_first_name.setText(this.docUserDetail.getFirstName() + " " + this.docUserDetail.getLastName());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.l_1.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
        this.l_2.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
        this.l_3.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
        this.l_4.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
        this.l_5.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
        this.l_55.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
        this.l_6.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
        this.l_7.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
        this.l_8.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
    }

    private void clickListners() {
        this.img_back.setOnClickListener(this);
        this.img_edit.setOnClickListener(this);
        this.img_profile.setOnClickListener(this);
        this.linear_done.setOnClickListener(this);
        this.changePassword.setOnClickListener(this);
        this.l_8.setOnClickListener(this);
        this.et_pharmacy.setOnClickListener(this);
        this.parent.setOnClickListener(this);
        this.et_dob.setOnFocusChangeListener(this);
        this.et_dob.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.parent:
                CommonUtils.hideKeyboard(this.et_email, this.activity);
                return;
            case C1020R.id.img_back:
                this.activity.finish();
                CommonUtils.hideKeyboard(this.et_first_name, this.activity);
                return;
            case C1020R.id.img_profile:
                CommonUtils.hideKeyboard(this.et_email, this.activity);
                DialogPopUps.openCamera(this.activity, new C11883());
                return;
            case C1020R.id.linear_done:
                if (this.isEditable) {
                    this.st_first_name = this.et_first_name.getText().toString();
                    this.st_last_name = this.et_last_name.getText().toString();
                    this.st_email = this.et_email.getText().toString();
                    this.st_phone = this.et_phone.getText().toString();
                    this.st_country_code = this.tv_country_code.getText().toString();
                    this.st_address = this.et_address.getText().toString();
                    this.st_zip = this.et_zip.getText().toString();
                    this.st_dob = this.et_dob.getText().toString();
                    this.st_Pharmacy = this.et_pharmacy.getText().toString();
                    this.myProfilePresenter.doneButonFunctionality(this.st_first_name, this.st_last_name, this.st_email, this.st_address, this.st_zip, this.st_country_code, this.st_phone, this.st_Pharmacy, this.PharmacyId, this.PharmacyName, this.st_dob, this.docUserDetail);
                    return;
                }
                this.myProfilePresenter.checkingForDoctors();
                return;
            case C1020R.id.et_dob:
                CommonUtils.hideKeyboard(this.et_first_name, this.activity);
                this.myProfilePresenter.openDatePicker();
                return;
            case C1020R.id.img_edit:
                if (this.docUserDetail != null) {
                    enableFields(true);
                    return;
                }
                return;
            case C1020R.id.l_8:
            case C1020R.id.et_pharmacy:
                if (this.isEditable) {
                    this.myProfilePresenter.selectPharmacy();
                    return;
                } else if (this.docUserDetail != null) {
                    SelectPharmacyObject selectPharmacyObject = new SelectPharmacyObject();
                    selectPharmacyObject.setLong(this.docUserDetail.getLong());
                    selectPharmacyObject.setFullAddress(this.docUserDetail.getAddress());
                    selectPharmacyObject.setLat(this.docUserDetail.getLat());
                    selectPharmacyObject.setData(this.docUserDetail.getPharmacyDetails());
                    this.myProfilePresenter.gotoPharmacyDetail(selectPharmacyObject);
                    return;
                } else {
                    return;
                }
            case C1020R.id.changePassword:
                this.activity.startActivity(new Intent(this.activity, ChangePassword.class));
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.myProfilePresenter.unSubscribeCallbacks();
        CommonUtils.hideKeyboard(this.et_first_name, this.activity);
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this.activity, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this.activity, error);
    }

    public void showToastError(String error) {
        Toast.makeText(this.activity.getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void setData(DocUserDetail data) {
        if (this.imageFile != null) {
            ((com.trigma.tiktok.fragments.MyProfile.ProfileImageSetting) getActivity()).settingImage(this.imageFile);
        }
        this.docUserDetail = data;
        enableFields(false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImageTikTok.handleActivityResult(requestCode, resultCode, data, this.activity, new C11894());
        if (requestCode == 100) {
            Activity activity = this.activity;
            if (resultCode == -1 && data != null && data.hasExtra("PharmacyName")) {
                SelectPharmacyObject selectPharmacyObject = SharedPreff.getSelectPharmacyObject();
                this.docUserDetail.setLat(selectPharmacyObject.getLat());
                this.docUserDetail.setLong(selectPharmacyObject.getLong());
                this.docUserDetail.setAddress(selectPharmacyObject.getFullAddress());
                this.docUserDetail.setPharmacyDetails(selectPharmacyObject.getData());
                this.et_pharmacy.setText(data.getStringExtra("PharmacyName"));
            }
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case C1020R.id.et_dob:
                if (hasFocus) {
                    this.myProfilePresenter.openDatePicker();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void gotoPharmacyDetail(SelectPharmacyObject detail) {
        SharedPreff.saveFirstTimePharmacySelect(false);
        SharedPreff.saveSelectPharmacyObject(detail);
        Intent scDeatil = new Intent(this.activity, PharmacyDetail.class);
        scDeatil.putExtra("show_select_button", false);
        startActivityForResult(scDeatil, 100);
    }

    public void gotoSelectPharmacy() {
        SharedPreff.saveFirstTimePharmacySelect(false);
        Intent selectPharmacy = new Intent(this.activity, SelectPharmacy.class);
        selectPharmacy.putExtra(Constants.SHOW_BACK, true);
        startActivityForResult(selectPharmacy, 100);
    }

    public void setDob(int year, String monthOfYear, int dayOfMonth) {
        this.et_dob.setText(monthOfYear + " " + dayOfMonth + "," + year);
        this.et_email.requestFocus();
    }

    public void convertingFirstImageToString(final Bitmap bm) {
        new AsyncTask() {
            protected Object doInBackground(Object[] params) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(CompressFormat.JPEG, 100, baos);
                MyInfo.this.myProfilePresenter.setBase64(Base64.encodeToString(baos.toByteArray(), 0));
                return null;
            }
        }.execute(new Object[]{""});
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
        String encImage = Base64.encodeToString(baos.toByteArray(), 0);
        this.myProfilePresenter.setBase64(encImage);
        return encImage;
    }

    public void isDoctorActive(boolean status) {
        if (status) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(C1020R.id.containerView, new SelfSchedule());
            fragmentTransaction.commit();
            return;
        }
        DialogPopUps.confirmationPopUp(this.activity, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.your_doctor_has_not_verified_your_account_yet), new C11916());
    }
}
