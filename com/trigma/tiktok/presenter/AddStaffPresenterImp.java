package com.trigma.tiktok.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.facebook.internal.ServerProtocol;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.AddStaff;
import com.trigma.tiktok.model.AddStaffResponse;
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

public class AddStaffPresenterImp implements AddStaffPresenter {
    String[] country_code = new String[]{Constants.STATUS_201, "202", "203", "204", "205", "206", "207", "208", "209", "210", "212", "213", "214", "215", "216", "217", "218", "219", "224", "225", "226", "228", "229", "231", "234", "239", "240", "242", "246", "248", "250", "251", "252", "253", "254", "256", "260", "262", "264", "267", "268", "269", "270", "276", "281", "284", "289", "301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "312", "313", "314", "315", "316", "317", "318", "319", "320", "321", "323", "325", "330", "331", "334", "336", "337", "339", "340", "343", "345", "347", "351", "352", "360", "361", "385", "386", Constants.STATUS_401, "402", "403", "404", "405", "406", "407", "408", Constants.STATUS_409, Constants.STATUS_410, "412", "413", "414", "415", "416", "417", "418", "419", "423", "424", "425", "430", "432", "434", "435", "438", "440", "441", "442", "443", "450", "456", "458", "469", "470", "473", "475", "478", "479", "480", "484", "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "512", "513", "514", "515", "516", "517", "518", "519", "520", "530", "533", "534", "539", "540", "541", "551", "559", "561", "562", "563", "567", "570", "571", "573", "574", "575", "579", "580", "581", "585", "586", "587", "600", Constants.STATUS_601, "602", "603", "604", "605", "606", "607", "608", "609", "610", "612", "613", "614", "615", "616", "617", "618", "619", "620", "623", "626", "630", "631", "636", "641", "646", "647", "649", "650", "651", "657", "660", "661", "662", "664", "670", "671", "678", "681", "682", "684", "669", "700", "701", "702", "703", "704", "705", "706", "707", "708", "709", "710", "712", "713", "714", "715", "716", "717", "718", "719", "720", "724", "727", "731", "732", "734", "740", "747", "754", "757", "758", "760", "762", "763", "765", "767", "769", "770", "772", "773", "774", "775", "778", "779", "780", "781", "784", "785", "786", "787", "800", Constants.STATUS_801, "802", "803", "804", "805", "806", "807", "808", "809", "810", "812", "813", "814", "815", "816", "817", "818", "819", "828", "829", "830", "831", "832", "843", "845", "847", "848", "849", "850", "855", "856", "857", "858", "859", "860", "862", "863", "864", "865", "866", "867", "868", "869", "870", "872", "876", "877", "878", "888", "900", Constants.STATUS_901, "902", "903", "904", "905", "906", "907", "908", "909", "910", "912", "913", "914", "915", "916", "917", "918", "919", "920", "925", "928", "931", "936", "937", "938", "939", "940", "941", "947", "949", "951", "952", "954", "956", "970", "971", "972", "973", "978", "979", "980", "985", "989"};
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private String media_type = "M";
    private NetWorkingService netWorkingService;
    private String setBase64 = "";
    private String user_type = "3";
    private AddStaff view;

    class C12021 implements Observer<AddStaffResponse> {
        C12021() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (AddStaffPresenterImp.this.view != null) {
                AddStaffPresenterImp.this.view.hideDialog();
                AddStaffPresenterImp.this.view.showError(AddStaffPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(final AddStaffResponse drStaffListResponce) {
            if (AddStaffPresenterImp.this.view != null) {
                AddStaffPresenterImp.this.view.hideDialog();
                if (drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    DialogPopUps.confirmationPopUp(AddStaffPresenterImp.this.view, AddStaffPresenterImp.this.view.getResources().getString(C1020R.string.confirm), AddStaffPresenterImp.this.view.getResources().getString(C1020R.string.staff_member_added_successfully), new AlertCallBackInterface() {
                        public void neutralClick() {
                            AddStaffPresenterImp.this.view.staffAdded(drStaffListResponce.getUserId());
                        }
                    });
                } else if (!drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                    AddStaffPresenterImp.this.view.showError(AddStaffPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                } else if (drStaffListResponce.getUserType() == 3) {
                    AddStaffPresenterImp.this.view.gotoStaffIDScreen(drStaffListResponce.getUserId());
                } else {
                    AddStaffPresenterImp.this.view.showError(AddStaffPresenterImp.this.view.getResources().getString(C1020R.string.email_id_already_exist_with_another_account));
                }
            }
        }
    }

    public AddStaffPresenterImp(AddStaff view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
    }

    public void validate(String st_first_name, String st_last_name, String st_email, String st_country_code, String gender, String st_phone, String designation) {
        if (TextUtils.isEmpty(st_first_name) || st_first_name.trim().length() == 0 || TextUtils.isEmpty(st_last_name) || st_last_name.trim().length() == 0 || TextUtils.isEmpty(st_email) || st_email.trim().length() == 0 || TextUtils.isEmpty(gender) || st_first_name.trim().length() == 0 || TextUtils.isEmpty(st_phone) || st_phone.trim().length() == 0 || TextUtils.isEmpty(st_country_code) || st_country_code.trim().length() == 0 || TextUtils.isEmpty(designation) || designation.trim().length() == 0) {
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
    }

    public void doneButonFunctionality(String st_first_name, String st_last_name, String st_email, String st_country_code, String gender, String st_phone, String designation) {
        int phoneLength = st_country_code.trim().length() + st_phone.length();
        if (st_country_code.equalsIgnoreCase(this.view.getResources().getString(C1020R.string.ar_code))) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_select_the_area_code));
        } else if (!Arrays.asList(this.country_code).contains(st_country_code)) {
            this.view.showError(this.view.getResources().getString(C1020R.string.please_enter_valid_country_code));
        } else if (phoneLength != 10) {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_phone_number));
        } else if (CommonUtils.validateEmail(st_email).booleanValue()) {
            Log.e("st_first_name", st_first_name);
            Log.e("st_last_name", st_last_name);
            Log.e("st_email", st_email);
            Log.e("st_country_code", st_country_code);
            Log.e("gender", gender);
            Log.e("st_phone", st_phone);
            Log.e("designation", designation);
            if (this.loginDocResponce == null) {
                return;
            }
            if (CommonUtils.isConnectedToInternet(this.view)) {
                this.view.showProgressDialog();
                String trim = designation.trim();
                String str = this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName();
                this.mSubscription = this.netWorkingService.getAPI().drMemberAdd(st_first_name.toLowerCase().trim(), st_last_name.toLowerCase().trim(), gender, st_phone, st_country_code, this.setBase64, this.user_type, st_email.toLowerCase(), this.media_type, this.loginDocResponce.getData().get_id(), "106", trim, str).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12021());
                return;
            }
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        } else {
            this.view.showToastError(this.view.getResources().getString(C1020R.string.please_enter_a_valid_email_id));
        }
    }

    public void setBase64(String data) {
        this.setBase64 = data;
    }

    public void setUserType(String type) {
    }

    public void settingMediaId(String media_id) {
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }
}
