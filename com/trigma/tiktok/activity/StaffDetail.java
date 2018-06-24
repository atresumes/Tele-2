package com.trigma.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.MessageUserDetail;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.presenter.StaffDetailPresenter;
import com.trigma.tiktok.presenter.StaffDetailPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.ArrayList;
import org.objectweb.asm.Opcodes;

public class StaffDetail extends BaseActivity implements OnClickListener {
    private LinearLayout call_linear;
    private CardView card_associated_docs;
    private ArrayList<String> doc_list = new ArrayList();
    private DrStaffListObject drStaffListObject;
    private ImageView img_back;
    private CircleImageView img_profile;
    private LinearLayout linear_confirm;
    private NetWorkingService netWorkingService;
    private RelativeLayout rl_view_doc_list;
    private StaffDetailPresenter staffDetailPresenter;
    private TextView tv_associated_doc;
    private TextView tv_designation;
    private TextView tv_email_field;
    private TextView tv_gender_field;
    private TextView tv_name_field;
    private TextView tv_phone_field;

    class C10721 implements AlertCallBackWithButtonsInterface {
        C10721() {
        }

        public void positiveClick() {
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            CommonUtils.call(StaffDetail.this, StaffDetail.this.drStaffListObject.getCode() + StaffDetail.this.drStaffListObject.getMobile());
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.staff_detail);
        this.drStaffListObject = SharedPreff.getStaffDetailListObject();
        initView();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.staffDetailPresenter = new StaffDetailPresenterImp(this, this.netWorkingService);
        settingData();
    }

    private void settingData() {
        if (this.drStaffListObject != null) {
            Log.e("patientPendingData", new Gson().toJson(this.drStaffListObject));
            this.tv_name_field.setText(this.drStaffListObject.getName());
            this.tv_gender_field.setText(this.drStaffListObject.getGender());
            this.tv_phone_field.setText("+1(" + this.drStaffListObject.getCode() + ")" + CommonUtils.phoneFormatter(this.drStaffListObject.getMobile()));
            this.tv_designation.setText(this.drStaffListObject.getDesignation());
            this.tv_email_field.setText(this.drStaffListObject.getEmail());
            Picasso.with(this).load(Constants.HTTP + this.drStaffListObject.getProfilePic()).transform(new CircleTransform()).resize(Opcodes.ISHL, Opcodes.ISHL).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(this.img_profile);
            this.staffDetailPresenter.fetchStaffDocList(this.drStaffListObject.getStaffId());
        }
    }

    private void initView() {
        this.tv_name_field = (TextView) findViewById(C1020R.id.tv_name_field);
        this.tv_gender_field = (TextView) findViewById(C1020R.id.tv_gender_field);
        this.tv_phone_field = (TextView) findViewById(C1020R.id.tv_phone_field);
        this.tv_email_field = (TextView) findViewById(C1020R.id.tv_email_field);
        this.tv_designation = (TextView) findViewById(C1020R.id.tv_designation);
        this.tv_associated_doc = (TextView) findViewById(C1020R.id.tv_associated_doc);
        this.call_linear = (LinearLayout) findViewById(C1020R.id.call_linear);
        this.card_associated_docs = (CardView) findViewById(C1020R.id.card_associated_docs);
        this.img_back = (ImageView) findViewById(C1020R.id.img_back);
        this.img_profile = (CircleImageView) findViewById(C1020R.id.img_profile);
        this.rl_view_doc_list = (RelativeLayout) findViewById(C1020R.id.rl_view_doc_list);
        this.linear_confirm = (LinearLayout) findViewById(C1020R.id.linear_confirm);
        if (this.drStaffListObject != null && this.drStaffListObject.getStaffStatus() == 0) {
            this.linear_confirm.setVisibility(8);
        }
        clickListners();
    }

    private void clickListners() {
        this.linear_confirm.setOnClickListener(this);
        this.img_back.setOnClickListener(this);
        this.call_linear.setOnClickListener(this);
        this.tv_associated_doc.setOnClickListener(this);
        this.rl_view_doc_list.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_back:
                finish();
                return;
            case C1020R.id.linear_confirm:
                this.staffDetailPresenter.chatNowApi(this.drStaffListObject.getStaffId(), this.drStaffListObject);
                return;
            case C1020R.id.call_linear:
                DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.please_confirm_your_call), getResources().getString(C1020R.string.You_re_about_to_call_the_following) + "\n" + this.tv_phone_field.getText().toString(), getResources().getString(C1020R.string.no_dialog), getResources().getString(C1020R.string.yes_dialog), "", false, true, new C10721());
                return;
            case C1020R.id.rl_view_doc_list:
            case C1020R.id.tv_associated_doc:
                this.staffDetailPresenter.showDrList(this.doc_list);
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

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void gotoChatScreen(ChatUserForDb chatUserForDb) {
        MessageUserObject messageUserObject = new MessageUserObject();
        MessageUserDetail messageUserDetail = new MessageUserDetail();
        messageUserObject.setGender("");
        messageUserObject.setTokenData(chatUserForDb.getTokenData());
        messageUserObject.setDOB(chatUserForDb.getDOB());
        messageUserObject.setName(chatUserForDb.getName());
        messageUserObject.setApiKey(chatUserForDb.getApiKey());
        messageUserObject.setCount(chatUserForDb.getCount());
        messageUserObject.setGroupId(chatUserForDb.getGroupId());
        messageUserObject.setDeviceToken(chatUserForDb.getDeviceToken());
        messageUserObject.setId(chatUserForDb.get_id());
        messageUserObject.setSessionData(chatUserForDb.getSessionData());
        messageUserObject.setProfilePic(chatUserForDb.getProfilePic());
        messageUserObject.setSpeciality(chatUserForDb.getSpeciality());
        messageUserObject.setType(chatUserForDb.getType());
        messageUserObject.setUserType(chatUserForDb.getUserType());
        messageUserDetail.setSpeciality(chatUserForDb.getSpeciality());
        messageUserDetail.setProfilePic(chatUserForDb.getProfilePic());
        messageUserDetail.setAddress(chatUserForDb.getAddress());
        messageUserDetail.setBio(chatUserForDb.getBio());
        messageUserDetail.setCity(chatUserForDb.getCity());
        messageUserDetail.setDOB(chatUserForDb.getDOB());
        messageUserDetail.setFirstName(chatUserForDb.getFirstName());
        messageUserDetail.setLastName(chatUserForDb.getLastName());
        messageUserDetail.setQualification(chatUserForDb.getQualification());
        messageUserDetail.setCode(chatUserForDb.getCode());
        messageUserDetail.setMobile(chatUserForDb.getMobile());
        messageUserObject.setUser(messageUserDetail);
        ChatScreen.TOKEN = messageUserObject.getTokenData();
        ChatScreen.API_KEY = messageUserObject.getApiKey();
        ChatScreen.SESSION_ID = messageUserObject.getSessionData();
        ChatScreen.USER_ID = chatUserForDb.get_id();
        if (SharedPreff.getStaffLoginResponse() != null) {
            ChatScreen.REALAM_UNIQUE_ID = SharedPreff.getStaffLoginResponse().getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + chatUserForDb.get_id();
        } else {
            ChatScreen.REALAM_UNIQUE_ID = SharedPreff.getLoginResponce().getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + chatUserForDb.get_id();
        }
        ChatScreen.isFetchfromServer = true;
        SharedPreff.savingChatDetailObject(messageUserObject);
        startActivity(new Intent(this, ChatScreen.class));
    }

    public void settingDocList(ArrayList<String> doc_list) {
        if (doc_list.size() > 0) {
            this.card_associated_docs.setVisibility(0);
            this.tv_associated_doc.setText(TextUtils.join(",", doc_list));
            this.doc_list = doc_list;
            return;
        }
        this.card_associated_docs.setVisibility(8);
    }

    public void showDrList(ArrayList<String> doc_list) {
        CharSequence[] drList = (CharSequence[]) doc_list.toArray(new String[doc_list.size()]);
        Builder dialogBuilder = new Builder(this);
        dialogBuilder.setTitle(getResources().getString(C1020R.string.associated_doctors));
        dialogBuilder.setItems(drList, null);
        dialogBuilder.create().show();
    }
}
