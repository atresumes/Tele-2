package com.trigma.tiktok.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.MyProfile;
import com.trigma.tiktok.model.CreateUserPojo;
import com.trigma.tiktok.model.DocUserDetail;
import com.trigma.tiktok.model.DocUserDetailResponse;
import com.trigma.tiktok.model.LoginDocData;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.Arrays;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyProfilePresenterImp implements MyProfilePresenter {
    public String base64 = "";
    String[] country_code = new String[]{Constants.STATUS_201, "202", "203", "204", "205", "206", "207", "208", "209", "210", "212", "213", "214", "215", "216", "217", "218", "219", "224", "225", "226", "228", "229", "231", "234", "239", "240", "242", "246", "248", "250", "251", "252", "253", "254", "256", "260", "262", "264", "267", "268", "269", "270", "276", "281", "284", "289", "301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "312", "313", "314", "315", "316", "317", "318", "319", "320", "321", "323", "325", "330", "331", "334", "336", "337", "339", "340", "343", "345", "347", "351", "352", "360", "361", "385", "386", Constants.STATUS_401, "402", "403", "404", "405", "406", "407", "408", Constants.STATUS_409, Constants.STATUS_410, "412", "413", "414", "415", "416", "417", "418", "419", "423", "424", "425", "430", "432", "434", "435", "438", "440", "441", "442", "443", "450", "456", "458", "469", "470", "473", "475", "478", "479", "480", "484", "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "512", "513", "514", "515", "516", "517", "518", "519", "520", "530", "533", "534", "539", "540", "541", "551", "559", "561", "562", "563", "567", "570", "571", "573", "574", "575", "579", "580", "581", "585", "586", "587", "600", Constants.STATUS_601, "602", "603", "604", "605", "606", "607", "608", "609", "610", "612", "613", "614", "615", "616", "617", "618", "619", "620", "623", "626", "630", "631", "636", "641", "646", "647", "649", "650", "651", "657", "660", "661", "662", "664", "670", "671", "678", "681", "682", "684", "669", "700", "701", "702", "703", "704", "705", "706", "707", "708", "709", "710", "712", "713", "714", "715", "716", "717", "718", "719", "720", "724", "727", "731", "732", "734", "740", "747", "754", "757", "758", "760", "762", "763", "765", "767", "769", "770", "772", "773", "774", "775", "778", "779", "780", "781", "784", "785", "786", "787", "800", Constants.STATUS_801, "802", "803", "804", "805", "806", "807", "808", "809", "810", "812", "813", "814", "815", "816", "817", "818", "819", "828", "829", "830", "831", "832", "843", "845", "847", "848", "849", "850", "855", "856", "857", "858", "859", "860", "862", "863", "864", "865", "866", "867", "868", "869", "870", "872", "876", "877", "878", "888", "900", Constants.STATUS_901, "902", "903", "904", "905", "906", "907", "908", "909", "910", "912", "913", "914", "915", "916", "917", "918", "919", "920", "925", "928", "931", "936", "937", "938", "939", "940", "941", "947", "949", "951", "952", "954", "956", "970", "971", "972", "973", "978", "979", "980", "985", "989"};
    String[] country_state = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
    private CreateUserPojo createUserPojo;
    DocUserDetail data;
    private int isDoc = 0;
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    public String st_state = "";
    private MyProfile view;
    String[] zip_code_length = new String[]{"5", "9"};

    class C12691 implements Observer<LoginDocResponce> {

        class C12681 implements AlertCallBackInterface {
            C12681() {
            }

            public void neutralClick() {
                MyProfilePresenterImp.this.view.setData(MyProfilePresenterImp.this.data);
            }
        }

        C12691() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MyProfilePresenterImp.this.view != null) {
                MyProfilePresenterImp.this.view.hideDialog();
                MyProfilePresenterImp.this.view.showError(MyProfilePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(LoginDocResponce docUserDetailResponse) {
            if (docUserDetailResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                LoginDocData dataa = docUserDetailResponse.getData();
                dataa.setDrRequest(MyProfilePresenterImp.this.loginDocResponce.getData().getDrRequest());
                docUserDetailResponse.setData(dataa);
                SharedPreff.saveLoginResponce(docUserDetailResponse);
                DialogPopUps.confirmationPopUp(MyProfilePresenterImp.this.view.getActivity(), "", MyProfilePresenterImp.this.view.getResources().getString(C1020R.string.your_has_been_updated_successfully), new C12681());
                return;
            }
            MyProfilePresenterImp.this.view.showError(docUserDetailResponse.getError());
        }
    }

    class C12702 implements Observer<DocUserDetailResponse> {
        C12702() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MyProfilePresenterImp.this.view != null) {
                MyProfilePresenterImp.this.view.hideDialog();
                MyProfilePresenterImp.this.view.showError(MyProfilePresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocUserDetailResponse docUserDetailResponse) {
            if (MyProfilePresenterImp.this.view != null) {
                MyProfilePresenterImp.this.view.hideDialog();
                if (docUserDetailResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    MyProfilePresenterImp.this.view.setUserData(docUserDetailResponse.getData());
                } else {
                    MyProfilePresenterImp.this.view.showError(docUserDetailResponse.getError());
                }
            }
        }
    }

    public MyProfilePresenterImp(MyProfile view, NetWorkingService netWorkingService, int isDoc) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.createUserPojo = new CreateUserPojo();
        this.isDoc = isDoc;
        this.loginDocResponce = SharedPreff.getLoginResponce();
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
            this.view = null;
        }
    }

    public void doneButonFunctionality(String st_first_name, String st_last_name, String st_email, String st_address, String st_zip, String st_qualification, String st_speciality, String st_language, String st_bio, String st_country_code, String st_phone, DocUserDetail dataa) {
        int phoneLength = st_country_code.trim().length() + st_phone.length();
        int zip_length = st_zip.trim().length();
        Log.e("zip_length", "" + zip_length);
        this.data = dataa;
        this.data.setFirstName(st_first_name);
        this.data.setLastName(st_last_name);
        this.data.setName(st_first_name + " " + st_first_name);
        this.data.setEmail(st_email);
        this.data.setAddress(st_address);
        this.data.setZipcode(st_zip);
        this.data.setQualification(st_qualification);
        this.data.setSpeciality(st_speciality);
        this.data.setLanguages(st_language);
        this.data.setBio(st_bio);
        this.data.setCode(st_country_code);
        this.data.setMobile(st_phone);
        Log.e("Data", "" + new Gson().toJson(this.data));
        if (st_country_code.equalsIgnoreCase(this.view.getResources().getString(C1020R.string.ar_code))) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_the_area_code));
        } else if (!Arrays.asList(this.zip_code_length).contains("" + zip_length)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_valid_zip_code));
        } else if (!Arrays.asList(this.country_code).contains(st_country_code)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_valid_country_code));
        } else if (phoneLength != 10) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_phone_number));
        } else if (!CommonUtils.validateEmail(st_email).booleanValue()) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_email_id));
        } else if (TextUtils.isEmpty(st_first_name)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_name));
        } else if (TextUtils.isEmpty(st_last_name)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_last_name));
        } else if (TextUtils.isEmpty(st_speciality)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_speciality));
        } else if (TextUtils.isEmpty(st_language)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_language_spoken));
        } else if (TextUtils.isEmpty(st_bio)) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_bio));
        } else if (this.loginDocResponce == null) {
        } else {
            if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
                this.view.showProgressDialog();
                this.mSubscription = this.netWorkingService.getAPI().updateDocProfile(st_first_name.toLowerCase(), st_last_name.toLowerCase(), st_phone, st_country_code, SharedPreff.getLat(), SharedPreff.getLng(), this.loginDocResponce.getData().get_id(), st_zip, this.base64, st_qualification, st_speciality, st_language, st_bio).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12691());
                return;
            }
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void setBase64(String data) {
        this.base64 = data;
    }

    public void showStateDialog() {
    }

    public void fetchDetail() {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getDoctorProfile(this.loginDocResponce.getData().get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12702());
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void checkSocialMediaUser() {
        if (this.loginDocResponce != null && !this.loginDocResponce.getData().getMediaType().equalsIgnoreCase("M")) {
            this.view.hideChangePassword();
        }
    }

    public void doneButonFunctionality(String st_first_name, String st_last_name, String st_email, String st_designation, String st_country_code, String st_phone, DocUserDetail dataa) {
    }
}
