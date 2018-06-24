package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.ChangePassword;
import com.trigma.tiktok.easyimage.DefaultCallback;
import com.trigma.tiktok.easyimage.EasyImageTikTok;
import com.trigma.tiktok.easyimage.EasyImageTikTok.ImageSource;
import com.trigma.tiktok.model.DocUserDetail;
import com.trigma.tiktok.presenter.MyProfilePresenter;
import com.trigma.tiktok.presenter.StaffEditProfilePresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StaffEditProfile extends BaseFragment implements OnClickListener {
    private Activity activity;
    private TextView changePassword;
    private DocUserDetail data;
    private EditText et_designation;
    private EditText et_email;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_phone;
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
    private LinearLayout l_44;
    private LinearLayout l_5;
    private CardView last_name_card;
    private LinearLayout linear_done;
    private MyProfilePresenter myProfilePresenter;
    public NetWorkingService netWorkingService;
    private RelativeLayout parent;
    private ScrollView scrollView;
    private String st_country_code;
    private String st_designation;
    private String st_email;
    private String st_first_name;
    private String st_last_name;
    private String st_phone;
    private String title;
    private EditText tv_country_code;
    private TextView tv_last_name;
    private TextView tv_name;
    private TextView tv_title;
    private TextView tv_user_code;

    class C11721 implements Target {
        C11721() {
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
            StaffEditProfile.this.img_profile.setImageBitmap(bitmap);
            StaffEditProfile.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            try {
                StaffEditProfile.this.convertingFirstImageToString(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onBitmapFailed(Drawable errorDrawable) {
        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }

    class C11732 implements Target {
        C11732() {
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
            StaffEditProfile.this.img_profile.setImageBitmap(bitmap);
            StaffEditProfile.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            try {
                StaffEditProfile.this.convertingFirstImageToString(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onBitmapFailed(Drawable errorDrawable) {
        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }

    class C11754 implements AlertCallBackWithButtonsInterface {
        C11754() {
        }

        public void positiveClick() {
            EasyImageTikTok.openGallery(StaffEditProfile.this, 0);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            EasyImageTikTok.openCamera(StaffEditProfile.this, 0);
        }
    }

    class C11765 extends DefaultCallback {
        C11765() {
        }

        public void onImagePickerError(Exception e, ImageSource source, int type) {
            StaffEditProfile.this.showToastError(StaffEditProfile.this.getResources().getString(C1020R.string.sorry_unable_to_pick_the_image));
            StaffEditProfile.this.myProfilePresenter.setBase64("");
        }

        public void onImagePicked(File imageFilee, ImageSource source, int type) {
            StaffEditProfile.this.imageFile = imageFilee;
            Picasso.with(StaffEditProfile.this.activity).load(imageFilee).into(StaffEditProfile.this.img_profile);
            StaffEditProfile.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            StaffEditProfile.this.myProfilePresenter.setBase64(StaffEditProfile.this.encodeImage(imageFilee));
        }

        public void onCanceled(ImageSource source, int type) {
            if (source == ImageSource.CAMERA) {
                File photoFile = EasyImageTikTok.lastlyTakenButCanceledPhoto(StaffEditProfile.this.activity);
                if (photoFile != null) {
                    photoFile.delete();
                }
            }
        }
    }

    public class CustomWatcher implements TextWatcher {
        private int characters;
        private View view;

        class C11771 implements AlertCallBackInterface {
            C11771() {
            }

            public void neutralClick() {
            }
        }

        class C11782 implements AlertCallBackInterface {
            C11782() {
            }

            public void neutralClick() {
            }
        }

        class C11793 implements AlertCallBackInterface {
            C11793() {
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
            try {
                switch (this.view.getId()) {
                    case C1020R.id.et_first_name:
                        if (text.length() > this.characters) {
                            DialogPopUps.alertPopUp(StaffEditProfile.this.activity, StaffEditProfile.this.activity.getResources().getString(C1020R.string.first_name_c) + " " + StaffEditProfile.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + StaffEditProfile.this.getResources().getString(C1020R.string.characters), StaffEditProfile.this.getResources().getString(C1020R.string.ok_dialog), new C11771());
                            StaffEditProfile.this.et_first_name.setText(text.substring(0, this.characters - 1));
                            StaffEditProfile.this.et_first_name.setSelection(StaffEditProfile.this.et_first_name.getText().toString().length());
                            return;
                        }
                        return;
                    case C1020R.id.et_last_name:
                        if (text.length() > this.characters) {
                            DialogPopUps.alertPopUp(StaffEditProfile.this.activity, StaffEditProfile.this.activity.getResources().getString(C1020R.string.last_name_c) + " " + StaffEditProfile.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + StaffEditProfile.this.getResources().getString(C1020R.string.characters), StaffEditProfile.this.getResources().getString(C1020R.string.ok_dialog), new C11782());
                            StaffEditProfile.this.et_last_name.setText(text.substring(0, this.characters - 1));
                            StaffEditProfile.this.et_last_name.setSelection(StaffEditProfile.this.et_last_name.getText().toString().length());
                            return;
                        }
                        return;
                    case C1020R.id.et_designation:
                        if (text.length() > this.characters) {
                            DialogPopUps.alertPopUp(StaffEditProfile.this.activity, StaffEditProfile.this.activity.getResources().getString(C1020R.string.designation) + " " + StaffEditProfile.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + StaffEditProfile.this.getResources().getString(C1020R.string.characters), StaffEditProfile.this.getResources().getString(C1020R.string.ok_dialog), new C11793());
                            StaffEditProfile.this.et_designation.setText(text.substring(0, this.characters - 1));
                            StaffEditProfile.this.et_designation.setSelection(StaffEditProfile.this.et_designation.getText().toString().length());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public interface ProfileImageSetting {
        void settingImage(File file);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.staff_edit_profile, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.scrollView = (ScrollView) screen.findViewById(C1020R.id.scrollView);
        this.et_first_name = (EditText) screen.findViewById(C1020R.id.et_first_name);
        this.et_last_name = (EditText) screen.findViewById(C1020R.id.et_last_name);
        this.et_email = (EditText) screen.findViewById(C1020R.id.et_email);
        this.et_phone = (EditText) screen.findViewById(C1020R.id.et_phone);
        this.tv_country_code = (EditText) screen.findViewById(C1020R.id.tv_country_code);
        this.et_designation = (EditText) screen.findViewById(C1020R.id.et_designation);
        this.img_back = (ImageView) screen.findViewById(C1020R.id.img_back);
        this.img_edit = (ImageView) screen.findViewById(C1020R.id.img_edit);
        this.img_header = (RelativeLayout) screen.findViewById(C1020R.id.img_header);
        this.tv_last_name = (TextView) screen.findViewById(C1020R.id.tv_last_name);
        this.tv_name = (TextView) screen.findViewById(C1020R.id.tv_name);
        this.tv_title = (TextView) screen.findViewById(C1020R.id.tv_title);
        this.changePassword = (TextView) screen.findViewById(C1020R.id.changePassword);
        this.tv_user_code = (TextView) screen.findViewById(C1020R.id.tv_user_code);
        this.parent = (RelativeLayout) screen.findViewById(C1020R.id.parent);
        this.img_edit.setEnabled(false);
        this.img_profile = (CircleImageView) screen.findViewById(C1020R.id.img_profile);
        this.linear_done = (LinearLayout) screen.findViewById(C1020R.id.linear_done);
        this.l_1 = (LinearLayout) screen.findViewById(C1020R.id.l_1);
        this.l_2 = (LinearLayout) screen.findViewById(C1020R.id.l_2);
        this.l_3 = (LinearLayout) screen.findViewById(C1020R.id.l_3);
        this.l_4 = (LinearLayout) screen.findViewById(C1020R.id.l_4);
        this.l_44 = (LinearLayout) screen.findViewById(C1020R.id.l_44);
        this.l_5 = (LinearLayout) screen.findViewById(C1020R.id.l_5);
        this.last_name_card = (CardView) screen.findViewById(C1020R.id.last_name_card);
        clickListners();
    }

    public void setUserData(DocUserDetail data) {
        this.data = data;
        if (data != null) {
            this.img_edit.setEnabled(true);
            if (this.isEditable) {
                this.et_first_name.setText(data.getFirstName());
            } else {
                this.et_first_name.setText(data.getName());
            }
            if (data.getStaffCode() != null) {
                this.tv_user_code.setText(this.activity.getResources().getString(C1020R.string.staff_code_profile) + "- " + data.getStaffCode());
                this.tv_user_code.setVisibility(0);
            } else {
                this.tv_user_code.setVisibility(8);
            }
            this.et_last_name.setText(data.getLastName());
            this.et_email.setText(data.getEmail());
            this.et_phone.setText(data.getMobile());
            this.tv_country_code.setText(data.getCode());
            this.et_designation.setText(data.getDesignation());
            if (data.getProfilePic().contains("http") || data.getProfilePic().contains("https")) {
                Picasso.with(this.activity).load(data.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(new C11721());
            } else {
                Picasso.with(this.activity).load(Constants.HTTP + data.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(new C11732());
            }
        }
        this.et_first_name.addTextChangedListener(new CustomWatcher(this.et_first_name, 35));
        this.et_last_name.addTextChangedListener(new CustomWatcher(this.et_last_name, 35));
    }

    public void convertingFirstImageToString(final Bitmap bm) {
        new AsyncTask() {
            protected Object doInBackground(Object[] params) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(CompressFormat.JPEG, 100, baos);
                StaffEditProfile.this.myProfilePresenter.setBase64(Base64.encodeToString(baos.toByteArray(), 0));
                return null;
            }
        }.execute(new Object[]{""});
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        CommonUtils.closingKeyboard(this.activity);
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.myProfilePresenter = new StaffEditProfilePresenterImp(this, this.netWorkingService);
        this.myProfilePresenter.fetchDetail();
        enableFields(false);
        if (getArguments() != null && getArguments().containsKey("title")) {
            this.title = getArguments().getString("title");
            this.tv_title.setText(this.title);
            enableFields(true);
        }
    }

    public void onResume() {
        super.onResume();
        if (this.myProfilePresenter != null) {
            this.myProfilePresenter.checkSocialMediaUser();
        }
    }

    public void hideChangePassword() {
        this.changePassword.setVisibility(8);
    }

    public void enableFields(boolean enable) {
        this.isEditable = enable;
        this.et_first_name.setEnabled(enable);
        this.et_last_name.setEnabled(enable);
        this.et_first_name.requestFocus();
        this.et_email.setEnabled(false);
        this.et_phone.setEnabled(enable);
        this.tv_country_code.setEnabled(enable);
        this.et_designation.setEnabled(enable);
        this.linear_done.setEnabled(enable);
        this.img_profile.setEnabled(enable);
        if (enable) {
            this.linear_done.setVisibility(0);
            this.tv_title.setText(this.activity.getResources().getString(C1020R.string.edit_profile));
            this.img_edit.setVisibility(8);
            this.tv_name.setText(this.activity.getResources().getString(C1020R.string.first_name_c));
            CommonUtils.showKeyboard(this.activity);
            this.last_name_card.setVisibility(0);
            try {
                if (this.data != null) {
                    this.et_first_name.setText(this.data.getFirstName());
                }
                this.et_first_name.setSelection(this.et_first_name.getText().toString().length());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.l_1.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.l_2.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
            this.l_3.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.l_4.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
            this.l_44.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
            this.l_5.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.changePassword.setVisibility(8);
            return;
        }
        this.changePassword.setVisibility(0);
        this.linear_done.setVisibility(8);
        this.tv_title.setText(this.activity.getResources().getString(C1020R.string.my_profile_title));
        this.img_edit.setVisibility(0);
        this.last_name_card.setVisibility(8);
        this.tv_name.setText(this.activity.getResources().getString(C1020R.string.name));
        try {
            if (this.data != null) {
                this.et_first_name.setText(this.data.getFirstName() + " " + this.data.getLastName());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.l_1.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
        this.l_2.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
        this.l_3.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
        this.l_4.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
        this.l_44.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.white));
        this.l_5.setBackgroundColor(ContextCompat.getColor(this.activity, C1020R.color.light_white));
    }

    private void clickListners() {
        this.img_back.setOnClickListener(this);
        this.tv_user_code.setOnClickListener(this);
        this.img_edit.setOnClickListener(this);
        this.img_profile.setOnClickListener(this);
        this.linear_done.setOnClickListener(this);
        this.changePassword.setOnClickListener(this);
        this.parent.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.parent:
                CommonUtils.hideKeyboard(this.et_first_name, this.activity);
                return;
            case C1020R.id.img_back:
                this.activity.finish();
                CommonUtils.hideKeyboard(this.et_first_name, this.activity);
                return;
            case C1020R.id.img_profile:
                CommonUtils.hideKeyboard(this.et_first_name, this.activity);
                DialogPopUps.openCamera(this.activity, new C11754());
                return;
            case C1020R.id.linear_done:
                this.st_first_name = this.et_first_name.getText().toString();
                this.st_last_name = this.et_last_name.getText().toString();
                this.st_email = this.et_email.getText().toString();
                this.st_phone = this.et_phone.getText().toString();
                this.st_country_code = this.tv_country_code.getText().toString();
                this.st_designation = this.et_designation.getText().toString();
                this.myProfilePresenter.doneButonFunctionality(this.st_first_name, this.st_last_name, this.st_email, this.st_designation, this.st_country_code, this.st_phone, this.data);
                return;
            case C1020R.id.img_edit:
                enableFields(true);
                return;
            case C1020R.id.changePassword:
                this.activity.startActivity(new Intent(this.activity, ChangePassword.class));
                return;
            case C1020R.id.tv_user_code:
                CommonUtils.copyText(this.data.getStaffCode(), this.activity, this.activity.getResources().getString(C1020R.string.staff_member_code_copied));
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
        this.data = data;
        enableFields(false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImageTikTok.handleActivityResult(requestCode, resultCode, data, this.activity, new C11765());
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
}
