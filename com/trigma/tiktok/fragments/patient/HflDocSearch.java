package com.trigma.tiktok.fragments.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorBio;
import com.trigma.tiktok.adapter.patient.HflDocAdapter;
import com.trigma.tiktok.fragments.BaseFragment;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.presenter.patient.HflDocSearchPresenter;
import com.trigma.tiktok.presenter.patient.HflDocSearchPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;

public class HflDocSearch extends BaseFragment implements OnClickListener {
    private Activity activity;
    private boolean comeFromDetail = false;
    private EditText et_username;
    private HflDocAdapter nameZipAdapter;
    private HflDocSearchPresenter nameZipPresenter;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    private View search;
    private TextView tv_no_data;

    class C11841 implements TextWatcher {
        C11841() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (HflDocSearch.this.nameZipAdapter != null) {
                HflDocSearch.this.nameZipAdapter.getFilter().filter(editable);
            }
        }
    }

    class C11852 implements AlertCallBackInterface {
        C11852() {
        }

        public void neutralClick() {
        }
    }

    public static HflDocSearch newInstance(int page) {
        Bundle args = new Bundle();
        HflDocSearch fragment = new HflDocSearch();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.hfl_search_screen, container, false);
        initViews(screen);
        return screen;
    }

    private void initViews(View screen) {
        this.recyclerView = (RecyclerView) screen.findViewById(C1020R.id.recyclerView);
        this.et_username = (EditText) screen.findViewById(C1020R.id.et_username);
        this.tv_no_data = (TextView) screen.findViewById(C1020R.id.tv_no_data);
        this.search = screen.findViewById(C1020R.id.search);
        clickListners();
    }

    private void clickListners() {
        this.search.setOnClickListener(this);
    }

    private void settingRecyclerView() {
        this.nameZipAdapter = new HflDocAdapter(this, this.nameZipPresenter);
        this.recyclerView.setAdapter(this.nameZipAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.et_username.addTextChangedListener(new C11841());
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.nameZipPresenter = new HflDocSearchPresenterImp(this, this.netWorkingService);
        settingRecyclerView();
        this.nameZipPresenter.searchDoctor("");
    }

    public void emptyField() {
        this.et_username.setText(null);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.nameZipPresenter.unSubscribeCallbacks();
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this.activity, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this.activity, error);
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.search:
                CommonUtils.closingKeyboard(this.activity);
                if (TextUtils.isEmpty(this.et_username.getText().toString()) || this.et_username.getText().toString().trim().equalsIgnoreCase("")) {
                    DialogPopUps.confirmationPopUp(this.activity, this.activity.getResources().getString(C1020R.string.alert), this.activity.getResources().getString(C1020R.string.please_enter_doctor_name), new C11852());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void settingAdapter(ArrayList<DrSearchNameObject> dataList) {
        this.nameZipAdapter.addingList(dataList);
        this.recyclerView.setVisibility(0);
        this.tv_no_data.setVisibility(8);
    }

    public void settingEmptyData() {
        this.recyclerView.setVisibility(8);
        this.tv_no_data.setVisibility(8);
    }

    public void onResume() {
        super.onResume();
        if (this.comeFromDetail) {
            this.comeFromDetail = false;
            this.et_username.setText(null);
        }
    }

    public void goToPatientDetail(DrSearchNameObject drSearchNameObject) {
        this.comeFromDetail = true;
        SharedPreff.addingDocSearchDetail(drSearchNameObject);
        startActivity(new Intent(this.activity, DoctorBio.class));
    }

    public void filteredDataShown(boolean val) {
        if (val) {
            this.recyclerView.setVisibility(0);
            this.tv_no_data.setVisibility(8);
            return;
        }
        this.recyclerView.setVisibility(8);
        this.tv_no_data.setVisibility(0);
    }
}
