package com.trigma.tiktok.presenter;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.GraphResponse;
import com.facebook.internal.ServerProtocol;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.CreateAccount;
import com.trigma.tiktok.model.CreateUserPojo;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.SignUpResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateAccountPresenterImp implements CreateAccountPresenter, OnDateSetListener {
    public static DatePickerDialog dpd;
    public String base64 = "";
    String[] country_code = new String[]{Constants.STATUS_201, "202", "203", "204", "205", "206", "207", "208", "209", "210", "212", "213", "214", "215", "216", "217", "218", "219", "224", "225", "226", "228", "229", "231", "234", "239", "240", "242", "246", "248", "250", "251", "252", "253", "254", "256", "260", "262", "264", "267", "268", "269", "270", "276", "281", "284", "289", "301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "312", "313", "314", "315", "316", "317", "318", "319", "320", "321", "323", "325", "330", "331", "334", "336", "337", "339", "340", "343", "345", "347", "351", "352", "360", "361", "385", "386", Constants.STATUS_401, "402", "403", "404", "405", "406", "407", "408", Constants.STATUS_409, Constants.STATUS_410, "412", "413", "414", "415", "416", "417", "418", "419", "423", "424", "425", "430", "432", "434", "435", "438", "440", "441", "442", "443", "450", "456", "458", "469", "470", "473", "475", "478", "479", "480", "484", "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "512", "513", "514", "515", "516", "517", "518", "519", "520", "530", "533", "534", "539", "540", "541", "551", "559", "561", "562", "563", "567", "570", "571", "573", "574", "575", "579", "580", "581", "585", "586", "587", "600", Constants.STATUS_601, "602", "603", "604", "605", "606", "607", "608", "609", "610", "612", "613", "614", "615", "616", "617", "618", "619", "620", "623", "626", "630", "631", "636", "641", "646", "647", "649", "650", "651", "657", "660", "661", "662", "664", "670", "671", "678", "681", "682", "684", "669", "700", "701", "702", "703", "704", "705", "706", "707", "708", "709", "710", "712", "713", "714", "715", "716", "717", "718", "719", "720", "724", "727", "731", "732", "734", "740", "747", "754", "757", "758", "760", "762", "763", "765", "767", "769", "770", "772", "773", "774", "775", "778", "779", "780", "781", "784", "785", "786", "787", "800", Constants.STATUS_801, "802", "803", "804", "805", "806", "807", "808", "809", "810", "812", "813", "814", "815", "816", "817", "818", "819", "828", "829", "830", "831", "832", "843", "845", "847", "848", "849", "850", "855", "856", "857", "858", "859", "860", "862", "863", "864", "865", "866", "867", "868", "869", "870", "872", "876", "877", "878", "888", "900", Constants.STATUS_901, "902", "903", "904", "905", "906", "907", "908", "909", "910", "912", "913", "914", "915", "916", "917", "918", "919", "920", "925", "928", "931", "936", "937", "938", "939", "940", "941", "947", "949", "951", "952", "954", "956", "970", "971", "972", "973", "978", "979", "980", "985", "989"};
    String[] country_state = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
    private ArrayList<String> country_state_list = new ArrayList();
    private CreateUserPojo createUserPojo;
    private int isDoc = 0;
    private Subscription mSubscription;
    public String media_id;
    private NetWorkingService netWorkingService;
    public String st_state = "";
    public String strDob = "";
    private String userType = "M";
    private CreateAccount view;
    String[] zip_code_length = new String[]{"5", "9"};

    class C12121 implements Observer<SignUpResponse> {
        C12121() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (CreateAccountPresenterImp.this.view != null) {
                CreateAccountPresenterImp.this.view.hideDialog();
            }
            CreateAccountPresenterImp.this.view.enableDoneButton(true);
            CreateAccountPresenterImp.this.view.showError(CreateAccountPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
        }

        public void onNext(SignUpResponse signUpResponse) {
            if (CreateAccountPresenterImp.this.view != null) {
                CreateAccountPresenterImp.this.view.hideDialog();
            }
            CreateAccountPresenterImp.this.view.enableDoneButton(true);
            if (signUpResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                Log.e("msg", GraphResponse.SUCCESS_KEY);
                if (CreateAccountPresenterImp.this.view != null) {
                    SharedPreff.saveUserId(signUpResponse.getId());
                    CreateAccountPresenterImp.this.view.signUpSuccessful();
                }
            } else if (signUpResponse.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                CreateAccountPresenterImp.this.view.showError(CreateAccountPresenterImp.this.view.getResources().getString(C1020R.string.this_email_id_is_already_taken));
            } else if (CreateAccountPresenterImp.this.view != null) {
                CreateAccountPresenterImp.this.view.showError(CreateAccountPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }
    }

    class C12132 implements OnClickListener {
        C12132() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            String selectedItem = (String) Arrays.asList(CreateAccountPresenterImp.this.country_state).get(i);
            if (CreateAccountPresenterImp.this.view != null) {
                CreateAccountPresenterImp.this.view.stateSelecetd(selectedItem);
            }
        }
    }

    class C12143 implements OnClickListener {
        C12143() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
        }
    }

    class C12154 implements Observer<DocAcceptReject> {
        C12154() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (CreateAccountPresenterImp.this.view != null) {
                CreateAccountPresenterImp.this.view.hideDialog();
            }
            CreateAccountPresenterImp.this.view.showError(CreateAccountPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
        }

        public void onNext(DocAcceptReject docAcceptReject) {
            if (CreateAccountPresenterImp.this.view != null) {
                CreateAccountPresenterImp.this.view.hideDialog();
            }
            if (docAcceptReject.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                CreateAccountPresenterImp.this.view.goToDoctorSearch();
            } else {
                CreateAccountPresenterImp.this.view.showError(docAcceptReject.getError());
            }
        }
    }

    public CreateAccountPresenterImp(CreateAccount view, NetWorkingService netWorkingService, int isDoc) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.createUserPojo = new CreateUserPojo();
        this.isDoc = isDoc;
        this.country_state_list = new ArrayList(Arrays.asList(this.country_state));
    }

    public void validate(String st_first_name, String st_last_name, String st_email, String st_pwd, String st_address, String st_zip, String st_city, String st_state, String st_qualification, String st_speciality, String st_language, String st_bio, String st_confirm_pwd, String st_country_code, String gender, String st_phone) {
        if (this.isDoc == 1) {
            if (TextUtils.isEmpty(st_first_name) || TextUtils.isEmpty(st_last_name) || TextUtils.isEmpty(st_email) || TextUtils.isEmpty(st_pwd) || TextUtils.isEmpty(st_address) || TextUtils.isEmpty(st_zip) || TextUtils.isEmpty(st_city) || TextUtils.isEmpty(st_state) || st_state.equalsIgnoreCase(this.view.getResources().getString(C1020R.string.state)) || TextUtils.isEmpty(st_qualification) || TextUtils.isEmpty(st_speciality) || TextUtils.isEmpty(st_language) || TextUtils.isEmpty(st_bio) || TextUtils.isEmpty(st_confirm_pwd) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(st_phone) || TextUtils.isEmpty(st_country_code)) {
                if (this.view != null) {
                    this.view.enableDoneButton(false);
                }
                Log.e("validate", "false");
                return;
            }
            if (this.view != null) {
                this.view.enableDoneButton(true);
            }
            Log.e("validate", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        } else if (TextUtils.isEmpty(st_first_name) || TextUtils.isEmpty(st_last_name) || TextUtils.isEmpty(st_email) || TextUtils.isEmpty(st_pwd) || TextUtils.isEmpty(st_address) || TextUtils.isEmpty(st_zip) || TextUtils.isEmpty(st_city) || TextUtils.isEmpty(st_state) || st_state.equalsIgnoreCase(this.view.getResources().getString(C1020R.string.state)) || TextUtils.isEmpty(st_confirm_pwd) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(st_phone) || TextUtils.isEmpty(st_country_code) || TextUtils.isEmpty(this.strDob)) {
            if (this.view != null) {
                this.view.enableDoneButton(false);
            }
            Log.e("validate", "false");
        } else {
            if (this.view != null) {
                this.view.enableDoneButton(true);
            }
            Log.e("validate", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        }
    }

    public void doneButonFunctionality(String st_first_name, String st_last_name, String st_email, String st_pwd, String st_address, String st_zip, String st_city, String st_state, String st_qualification, String st_speciality, String st_language, String st_bio, String st_confirm_pwd, String st_country_code, String gender, String st_phone) {
        this.st_state = st_state;
        int phoneLength = st_country_code.trim().length() + st_phone.length();
        int zip_length = st_zip.trim().length();
        Log.e("zip_length", "" + zip_length);
        if (st_country_code.equalsIgnoreCase(this.view.getResources().getString(C1020R.string.ar_code))) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_the_area_code));
        } else if (!st_pwd.equalsIgnoreCase(st_confirm_pwd)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.password_and_confirm_passowrd_must_be_same));
        } else if (!st_pwd.equalsIgnoreCase(st_confirm_pwd)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.password_and_confirm_passowrd_must_be_same));
        } else if (!Arrays.asList(this.zip_code_length).contains("" + zip_length)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_valid_zip_code));
        } else if (st_pwd.trim().length() < 6 || st_pwd.trim().length() > 15) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.password_length_cannot_be_less_than_6_characters));
        } else if (!Arrays.asList(this.country_code).contains(st_country_code)) {
            this.view.showError(this.view.getResources().getString(C1020R.string.please_enter_valid_country_code));
        } else if (phoneLength != 10) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_phone_number));
        } else if (CommonUtils.validateEmail(st_email).booleanValue()) {
            this.createUserPojo.setPassword(st_pwd);
            this.createUserPojo.setDeviceType(Constants.DEVICE_TYPE);
            this.createUserPojo.setDeviceToken(SharedPreff.getDeviceToken());
            this.createUserPojo.setEmail(st_email.toLowerCase());
            this.createUserPojo.setAddress(st_address);
            this.createUserPojo.setBio(st_bio);
            this.createUserPojo.setCity(st_city);
            this.createUserPojo.setFirstName(st_first_name.toLowerCase());
            this.createUserPojo.setLastName(st_last_name.toLowerCase());
            this.createUserPojo.setGender(gender.toLowerCase());
            this.createUserPojo.setLanguages(st_language);
            this.createUserPojo.setProfilePic(this.base64);
            this.createUserPojo.setLat(SharedPreff.getLat());
            this.createUserPojo.setLong(SharedPreff.getLng());
            this.createUserPojo.setMobile(st_phone);
            this.createUserPojo.setCode(st_country_code);
            this.createUserPojo.setQualification(st_qualification);
            this.createUserPojo.setSpeciality(st_speciality);
            this.createUserPojo.setUserType("" + this.isDoc);
            this.createUserPojo.setZipcode(st_zip);
            this.createUserPojo.setState(st_state);
            this.createUserPojo.setDOB(this.strDob);
            this.createUserPojo.setMediaID(this.media_id);
            this.createUserPojo.setMediaType(this.userType);
            Log.e("createUser", new Gson().toJson(this.createUserPojo));
            SharedPreff.saveCreateUserPojo(this.createUserPojo);
            createApiCall(this.createUserPojo);
        } else {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_email_id));
        }
    }

    private void createApiCall(CreateUserPojo createUserPojo) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.view.enableDoneButton(false);
            this.mSubscription = this.netWorkingService.getAPI().signupApi(createUserPojo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12121());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void setBase64(String data) {
        this.base64 = data;
    }

    public void setState(String st_state) {
        this.st_state = st_state;
    }

    public void showStateDialog(String val) {
        int index = -1;
        if (!TextUtils.isEmpty(val) && this.country_state_list.contains(val)) {
            index = this.country_state_list.indexOf(val);
        }
        Builder builder = new Builder(this.view);
        builder.setTitle(this.view.getResources().getString(C1020R.string.choose_state));
        builder.setSingleChoiceItems(this.country_state, index, new C12132());
        builder.setPositiveButton(Constants.OK, new C12143());
        builder.create().show();
    }

    public void setUserType(String type) {
        this.userType = type;
    }

    public void openDatePicker() {
        Calendar now = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        if (dpd != null) {
            if (dpd.isVisible()) {
                dpd.dismiss();
            }
            dpd = null;
        }
        int actualMaxDays = now.getActualMaximum(5);
        int currentDate = now.get(5);
        max.set(1, now.get(1));
        if (currentDate - 1 <= 0) {
            max.set(5, actualMaxDays);
            max.set(2, now.get(2) - 1);
        } else {
            max.set(5, currentDate - 1);
            max.set(2, now.get(2));
        }
        dpd = DatePickerDialog.newInstance(this, now.get(1), now.get(2), now.get(5));
        dpd.setMaxDate(max);
        dpd.setVersion(Version.VERSION_2);
        dpd.show(this.view.getSupportFragmentManager(), "Datepickerdialog");
    }

    public void settingMediaId(String media_id) {
        this.media_id = media_id;
    }

    public void agreeApiCall(String userId, String status) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            if (this.view != null) {
                this.view.showProgressDialog();
            }
            this.mSubscription = this.netWorkingService.getAPI().userAgreement(userId, status).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12154());
            return;
        }
        if (this.view != null) {
            this.view.hideDialog();
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
            this.view = null;
        }
    }

    public void onDateSet(DatePickerDialog vieww, int year, int monthOfYear, int dayOfMonth) {
        this.view.setDob(year, CommonUtils.getMonth("" + (monthOfYear + 1)), dayOfMonth);
        this.strDob = "" + CommonUtils.getMonth("" + (monthOfYear + 1)) + " " + dayOfMonth + "," + year;
        dpd = null;
    }
}
