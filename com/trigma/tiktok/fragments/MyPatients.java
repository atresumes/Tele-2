package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.ChatScreen;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.activity.PatientDetail;
import com.trigma.tiktok.adapter.ActivePatientAdapter;
import com.trigma.tiktok.adapter.PendingPatientAdapter;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.MessageUserDetail;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.presenter.MyPatientsPresenter;
import com.trigma.tiktok.presenter.MyPatientsPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.ArrayList;

public class MyPatients extends BaseFragment implements OnClickListener {
    public ArrayList<PatientPendingData> activeList = new ArrayList();
    private ActivePatientAdapter activePatientAdapter;
    private Activity activity;
    private View appointment_error_msg;
    private int callApi = 3;
    private boolean comeFromDetail = false;
    private EditText et_username;
    private ImageView img_error;
    private ImageView img_header_back;
    private ImageView img_slider;
    private LinearLayout lin_active;
    private LinearLayout lin_pending;
    private LinearLayout ll_search;
    private MyPatientsPresenter myPatientsPresenter;
    public NetWorkingService netWorkingService;
    public ArrayList<PatientPendingData> pendingList = new ArrayList();
    private PendingPatientAdapter pendingPatientAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout rl_parent;
    private View search;
    private TextView title_name;
    private TextView tv_active;
    private TextView tv_error_msg;
    private TextView tv_pending;

    class C11541 implements OnFocusChangeListener {
        C11541() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(MyPatients.this.et_username, MyPatients.this.activity);
            }
        }
    }

    class C11552 implements TextWatcher {
        C11552() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (MyPatients.this.activePatientAdapter == null) {
                return;
            }
            if (MyPatients.this.comeFromDetail) {
                MyPatients.this.comeFromDetail = false;
            } else {
                MyPatients.this.activePatientAdapter.getFilter().filter(editable);
            }
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.my_patients, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.recyclerView = (RecyclerView) screen.findViewById(C1020R.id.recyclerView);
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.tv_active = (TextView) screen.findViewById(C1020R.id.tv_active);
        this.tv_pending = (TextView) screen.findViewById(C1020R.id.tv_pending);
        this.tv_error_msg = (TextView) screen.findViewById(C1020R.id.tv_error_msg);
        this.tv_error_msg.setVisibility(8);
        this.lin_pending = (LinearLayout) screen.findViewById(C1020R.id.lin_pending);
        this.lin_active = (LinearLayout) screen.findViewById(C1020R.id.lin_active);
        this.search = screen.findViewById(C1020R.id.search);
        this.ll_search = (LinearLayout) screen.findViewById(C1020R.id.ll_search);
        this.et_username = (EditText) screen.findViewById(C1020R.id.et_username);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.img_error = (ImageView) screen.findViewById(C1020R.id.img_error);
        this.img_error.setImageResource(C1020R.drawable.no_active_patient);
        this.appointment_error_msg = screen.findViewById(C1020R.id.appointment_error_msg);
        this.rl_parent = (RelativeLayout) screen.findViewById(C1020R.id.rl_parent);
        clickListners();
    }

    private void clickListners() {
        this.tv_active.setOnClickListener(this);
        this.tv_pending.setOnClickListener(this);
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
        this.search.setOnClickListener(this);
        this.rl_parent.setOnClickListener(this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.my_patients));
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.myPatientsPresenter = new MyPatientsPresenterImp(this, this.netWorkingService);
        settingRecyclerView();
        setActiveAdapter(this.activeList);
        this.et_username.setOnFocusChangeListener(new C11541());
    }

    private void settingRecyclerView() {
        this.pendingPatientAdapter = new PendingPatientAdapter(this.myPatientsPresenter, this);
        this.activePatientAdapter = new ActivePatientAdapter(this.myPatientsPresenter, this);
        this.recyclerView.setAdapter(this.activePatientAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setVisibility(8);
        this.ll_search.setVisibility(8);
        this.myPatientsPresenter.callActivePatient();
        this.et_username.addTextChangedListener(new C11552());
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

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                MainActivity.openDrawer();
                CommonUtils.hideKeyboard(this.et_username, this.activity);
                return;
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            case C1020R.id.tv_active:
                this.appointment_error_msg.setVisibility(0);
                this.img_error.setImageResource(C1020R.drawable.no_active_patient);
                this.lin_active.setBackgroundResource(C1020R.drawable.while_left_selecetd);
                this.lin_pending.setBackground(null);
                this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
                this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
                this.myPatientsPresenter.callActivePatient();
                this.recyclerView.setVisibility(8);
                this.ll_search.setVisibility(8);
                return;
            case C1020R.id.tv_pending:
                this.img_error.setImageResource(C1020R.drawable.no_pending_patient);
                this.lin_pending.setBackgroundResource(C1020R.drawable.white_right_selected);
                this.lin_active.setBackground(null);
                this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
                this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
                this.myPatientsPresenter.callPendingPatient();
                this.recyclerView.setVisibility(8);
                this.ll_search.setVisibility(8);
                this.appointment_error_msg.setVisibility(0);
                return;
            case C1020R.id.rl_parent:
                CommonUtils.hideKeyboard(this.et_username, this.activity);
                return;
            default:
                return;
        }
    }

    public void setPendingAdapter(ArrayList<PatientPendingData> pendingList) {
        this.activeList = pendingList;
        if (pendingList.size() > 0) {
            this.appointment_error_msg.setVisibility(8);
            this.recyclerView.setVisibility(0);
            this.recyclerView.setAdapter(this.pendingPatientAdapter);
            this.pendingPatientAdapter.addingList(pendingList);
            this.pendingPatientAdapter.notifyDataSetChanged();
            return;
        }
        this.recyclerView.setVisibility(8);
        this.ll_search.setVisibility(8);
        this.appointment_error_msg.setVisibility(0);
        this.img_error.setImageResource(C1020R.drawable.no_pending_patient);
    }

    public void noSearchFound() {
        showError(this.activity.getResources().getString(C1020R.string.no_search_found));
    }

    public void setActiveAdapter(ArrayList<PatientPendingData> activeList) {
        if (activeList.size() > 0) {
            this.appointment_error_msg.setVisibility(8);
            this.recyclerView.setVisibility(0);
            this.ll_search.setVisibility(0);
            this.recyclerView.setAdapter(this.activePatientAdapter);
            this.activePatientAdapter.addingList(activeList);
            this.activePatientAdapter.notifyDataSetChanged();
            return;
        }
        this.recyclerView.setVisibility(8);
        this.ll_search.setVisibility(8);
        this.appointment_error_msg.setVisibility(0);
        this.img_error.setImageResource(C1020R.drawable.no_active_patient);
    }

    public void successfullyDeleted(int pos) {
        this.pendingPatientAdapter.deleted(pos);
        if (this.pendingPatientAdapter.getItemCount() <= 0) {
            this.recyclerView.setVisibility(8);
            this.ll_search.setVisibility(8);
            this.appointment_error_msg.setVisibility(0);
        }
    }

    public void successfullyAccepted(int pos) {
        this.pendingPatientAdapter.accepted(pos);
        if (this.pendingPatientAdapter.getItemCount() <= 0) {
            this.recyclerView.setVisibility(8);
            this.ll_search.setVisibility(8);
            this.appointment_error_msg.setVisibility(0);
        }
    }

    public void gotoDetailPage(int isFromActive) {
        this.callApi = isFromActive;
        this.comeFromDetail = true;
        Intent scDeatil = new Intent(this.activity, PatientDetail.class);
        scDeatil.putExtra("isFromActive", isFromActive);
        startActivity(scDeatil);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.myPatientsPresenter.unSubscribeCallbacks();
    }

    public void onResume() {
        super.onResume();
        if (this.callApi == 0) {
            this.callApi = 3;
            this.appointment_error_msg.setVisibility(0);
            this.img_error.setImageResource(C1020R.drawable.no_pending_patient);
            this.lin_pending.setBackgroundResource(C1020R.drawable.white_right_selected);
            this.lin_active.setBackground(null);
            this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
            this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.myPatientsPresenter.callPendingPatient();
            this.recyclerView.setVisibility(8);
            this.ll_search.setVisibility(8);
        } else if (this.callApi == 1) {
            this.callApi = 3;
            this.appointment_error_msg.setVisibility(0);
            this.img_error.setImageResource(C1020R.drawable.no_active_patient);
            this.lin_active.setBackgroundResource(C1020R.drawable.while_left_selecetd);
            this.lin_pending.setBackground(null);
            this.tv_active.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.dash_baord_pat));
            this.tv_pending.setTextColor(ContextCompat.getColor(this.activity, C1020R.color.white));
            this.myPatientsPresenter.callActivePatient();
            this.recyclerView.setVisibility(8);
            this.ll_search.setVisibility(8);
        } else {
            this.callApi = 3;
        }
        if (this.comeFromDetail) {
            this.et_username.setText(null);
        }
    }

    public void gotoChatScreen(ChatUserForDb chatUserForDb) {
        MessageUserObject messageUserObject = new MessageUserObject();
        MessageUserDetail messageUserDetail = new MessageUserDetail();
        messageUserObject.setGender(chatUserForDb.getGender());
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
        startActivity(new Intent(this.activity, ChatScreen.class));
    }
}
