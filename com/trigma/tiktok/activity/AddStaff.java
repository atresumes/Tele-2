package com.trigma.tiktok.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
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
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.easyimage.DefaultCallback;
import com.trigma.tiktok.easyimage.EasyImageTikTok;
import com.trigma.tiktok.easyimage.EasyImageTikTok.ImageSource;
import com.trigma.tiktok.presenter.AddStaffPresenter;
import com.trigma.tiktok.presenter.AddStaffPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
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

public class AddStaff extends BaseActivity implements OnClickListener {
    private static final int REQUEST_CODE = 101;
    private AddStaffPresenter addStaffPresenter;
    private int comming_from_id = 0;
    private EditText et_designation;
    private EditText et_email;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_phone;
    private CheckBox female_checkbox;
    private String gender;
    private ImageView img_back;
    private RelativeLayout img_header;
    private CircleImageView img_profile;
    private LinearLayout linear_done;
    private CheckBox male_checkbox;
    public NetWorkingService netWorkingService;
    private String profile_pic;
    private RelativeLayout rel_female;
    private RelativeLayout rel_male;
    private String st_country_code;
    private String st_designation;
    private String st_email;
    private String st_first_name;
    private String st_last_name;
    private String st_phone;
    private EditText tv_country_code;
    private TextView tv_name_text;

    class C10211 implements AlertCallBackWithButtonsInterface {
        C10211() {
        }

        public void positiveClick() {
            EasyImageTikTok.openGallery(AddStaff.this, 0);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            EasyImageTikTok.openCamera(AddStaff.this, 0);
        }
    }

    class C10222 extends DefaultCallback {
        C10222() {
        }

        public void onImagePickerError(Exception e, ImageSource source, int type) {
            AddStaff.this.showToastError(AddStaff.this.getResources().getString(C1020R.string.sorry_unable_to_pick_the_image));
            AddStaff.this.addStaffPresenter.setBase64("");
        }

        public void onImagePicked(File imageFile, ImageSource source, int type) {
            Picasso.with(AddStaff.this).load(imageFile).into(AddStaff.this.img_profile);
            AddStaff.this.img_header.setBackgroundResource(C1020R.drawable.profile_circle);
            AddStaff.this.addStaffPresenter.setBase64(AddStaff.this.encodeImage(imageFile));
        }

        public void onCanceled(ImageSource source, int type) {
            if (source == ImageSource.CAMERA) {
                File photoFile = EasyImageTikTok.lastlyTakenButCanceledPhoto(AddStaff.this);
                if (photoFile != null) {
                    photoFile.delete();
                }
            }
        }
    }

    public class CustomWatcher implements TextWatcher {
        private int characters;
        private View view;

        class C10241 implements AlertCallBackInterface {
            C10241() {
            }

            public void neutralClick() {
            }
        }

        class C10252 implements AlertCallBackInterface {
            C10252() {
            }

            public void neutralClick() {
            }
        }

        class C10263 implements AlertCallBackInterface {
            C10263() {
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
                    if (text.length() <= this.characters) {
                        if (text.length() != 1) {
                            if (text.length() == 0) {
                                AddStaff.this.settingStaffName();
                                break;
                            }
                        }
                        AddStaff.this.settingStaffName();
                        break;
                    }
                    DialogPopUps.alertPopUp(AddStaff.this, CommonUtils.capWordCase(AddStaff.this.et_first_name.getHint().toString()) + " " + AddStaff.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + AddStaff.this.getResources().getString(C1020R.string.characters), AddStaff.this.getResources().getString(C1020R.string.ok_dialog), new C10241());
                    AddStaff.this.et_first_name.setText(text.substring(0, this.characters));
                    AddStaff.this.et_first_name.setSelection(AddStaff.this.et_first_name.getText().toString().length());
                    break;
                    break;
                case C1020R.id.et_last_name:
                    if (text.length() <= this.characters) {
                        if (text.length() != 1) {
                            if (text.length() == 0) {
                                AddStaff.this.settingStaffName();
                                break;
                            }
                        }
                        AddStaff.this.settingStaffName();
                        break;
                    }
                    DialogPopUps.alertPopUp(AddStaff.this, CommonUtils.capWordCase(AddStaff.this.et_last_name.getHint().toString()) + " " + AddStaff.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + AddStaff.this.getResources().getString(C1020R.string.characters), AddStaff.this.getResources().getString(C1020R.string.ok_dialog), new C10252());
                    AddStaff.this.et_last_name.setText(text.substring(0, this.characters));
                    AddStaff.this.et_last_name.setSelection(AddStaff.this.et_last_name.getText().toString().length());
                    break;
                    break;
                case C1020R.id.et_designation:
                    if (text.length() > this.characters) {
                        DialogPopUps.alertPopUp(AddStaff.this, CommonUtils.capWordCase(AddStaff.this.et_designation.getHint().toString()) + " " + AddStaff.this.getResources().getString(C1020R.string.does_not_contain_more_than) + " " + this.characters + " " + AddStaff.this.getResources().getString(C1020R.string.characters), AddStaff.this.getResources().getString(C1020R.string.ok_dialog), new C10263());
                        AddStaff.this.et_designation.setText(text.substring(0, this.characters));
                        AddStaff.this.et_designation.setSelection(AddStaff.this.et_designation.getText().toString().length());
                        break;
                    }
                    break;
            }
            AddStaff.this.validate();
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.create_staff);
        initializationView();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.addStaffPresenter = new AddStaffPresenterImp(this, this.netWorkingService);
        EasyImageTikTok.configuration(this).setImagesFolderName(getResources().getString(C1020R.string.app_name)).saveInAppExternalFilesDir().saveInRootPicturesDirectory();
    }

    private void initializationView() {
        this.et_first_name = (EditText) findViewById(C1020R.id.et_first_name);
        this.et_last_name = (EditText) findViewById(C1020R.id.et_last_name);
        this.et_email = (EditText) findViewById(C1020R.id.et_email);
        this.et_phone = (EditText) findViewById(C1020R.id.et_phone);
        this.et_designation = (EditText) findViewById(C1020R.id.et_designation);
        this.tv_country_code = (EditText) findViewById(C1020R.id.tv_country_code);
        this.tv_name_text = (TextView) findViewById(C1020R.id.tv_name_text);
        this.img_header = (RelativeLayout) findViewById(C1020R.id.img_header);
        this.et_first_name.addTextChangedListener(new CustomWatcher(this.et_first_name, 35));
        this.et_last_name.addTextChangedListener(new CustomWatcher(this.et_last_name, 35));
        this.rel_male = (RelativeLayout) findViewById(C1020R.id.rel_male);
        this.rel_female = (RelativeLayout) findViewById(C1020R.id.rel_female);
        this.img_profile = (CircleImageView) findViewById(C1020R.id.img_profile);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.linear_done = (LinearLayout) findViewById(C1020R.id.linear_done);
        this.male_checkbox = (CheckBox) findViewById(C1020R.id.male_checkbox);
        this.female_checkbox = (CheckBox) findViewById(C1020R.id.female_checkbox);
        clickListners();
    }

    private void clickListners() {
        this.rel_male.setOnClickListener(this);
        this.rel_female.setOnClickListener(this);
        this.tv_country_code.setOnClickListener(this);
        this.linear_done.setOnClickListener(this);
        this.linear_done.setEnabled(false);
        this.img_back.setOnClickListener(this);
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
                DialogPopUps.openCamera(this, new C10211());
                return;
            case C1020R.id.linear_done:
                this.st_first_name = this.et_first_name.getText().toString();
                this.st_last_name = this.et_last_name.getText().toString();
                this.st_email = this.et_email.getText().toString();
                this.st_country_code = this.tv_country_code.getText().toString();
                this.st_phone = this.et_phone.getText().toString();
                this.st_designation = this.et_designation.getText().toString();
                this.addStaffPresenter.doneButonFunctionality(this.st_first_name, this.st_last_name, this.st_email, this.st_country_code, this.gender, this.st_phone, this.st_designation);
                return;
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
            default:
                return;
        }
    }

    private void validate() {
        this.st_first_name = this.et_first_name.getText().toString();
        this.st_last_name = this.et_last_name.getText().toString();
        this.st_email = this.et_email.getText().toString();
        this.st_country_code = this.tv_country_code.getText().toString();
        this.st_phone = this.et_phone.getText().toString();
        this.st_designation = this.et_designation.getText().toString();
        this.addStaffPresenter.validate(this.st_first_name, this.st_last_name, this.st_email, this.st_country_code, this.gender, this.st_phone, this.st_designation);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImageTikTok.handleActivityResult(requestCode, resultCode, data, this, new C10222());
        if (requestCode == 101 && resultCode == -1 && data != null) {
            setResult(-1, new Intent());
            finish();
        }
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

    public static Bitmap loadBitmapFromView(View shareLayout) {
        shareLayout.setDrawingCacheEnabled(true);
        shareLayout.buildDrawingCache();
        Bitmap bm = shareLayout.getDrawingCache();
        bm.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return bm;
    }

    public void settingStaffName() {
        String first = "";
        String second = "";
        first = this.et_first_name.getText().toString();
        second = this.et_last_name.getText().toString();
        if (TextUtils.isEmpty(first) && first.equalsIgnoreCase("")) {
            first = "";
        } else {
            first = ("" + first.charAt(0)).toUpperCase();
        }
        if (TextUtils.isEmpty(second) && second.equalsIgnoreCase("")) {
            second = "";
        } else {
            second = ("" + second.charAt(0)).toUpperCase();
        }
        this.tv_name_text.setText(first + second);
        convertingFirstImageToString(loadBitmapFromView(this.img_header));
    }

    public void convertingFirstImageToString(final Bitmap bm) {
        new AsyncTask() {
            protected Object doInBackground(Object[] params) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(CompressFormat.PNG, 100, baos);
                String encodedImage = Base64.encodeToString(baos.toByteArray(), 0);
                Log.e("encoded", encodedImage);
                AddStaff.this.addStaffPresenter.setBase64(encodedImage);
                return null;
            }
        }.execute(new Object[]{""});
    }

    public void gotoStaffIDScreen(String id) {
        Intent addStaff = new Intent(this, AddStaffId.class);
        addStaff.putExtra(Constants.STAFF_UDER_ID, id);
        startActivityForResult(addStaff, 101);
        this.comming_from_id = 1;
    }

    public void staffAdded(String id) {
        setResult(-1, new Intent());
        finish();
    }

    protected void onResume() {
        super.onResume();
        if (this.comming_from_id == 1) {
            this.comming_from_id = 0;
            this.et_first_name.setText(null);
            this.et_last_name.setText(null);
            this.et_email.setText(null);
            this.et_phone.setText(null);
            this.et_designation.setText(null);
            this.tv_country_code.setText(null);
            this.st_first_name = null;
            this.st_last_name = null;
            this.st_email = null;
            this.st_phone = null;
            this.st_designation = null;
            this.gender = null;
            this.profile_pic = null;
            this.st_country_code = null;
            this.male_checkbox.setChecked(false);
            this.female_checkbox.setChecked(false);
            this.tv_name_text.setText(null);
        }
    }
}
