package com.trigma.tiktok.fragments.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorBio;
import com.trigma.tiktok.adapter.patient.NameZipAdapter;
import com.trigma.tiktok.fragments.BaseFragment;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.presenter.patient.NameZipPresenter;
import com.trigma.tiktok.presenter.patient.NameZipPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;

public class NameZipFragment extends BaseFragment implements OnClickListener {
    private Activity activity;
    private EditText et_username;
    private NameZipAdapter nameZipAdapter;
    private NameZipPresenter nameZipPresenter;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    private View search;
    private TextView tv_no_data;

    class C11961 implements AlertCallBackInterface {
        C11961() {
        }

        public void neutralClick() {
        }
    }

    public static NameZipFragment newInstance(int page) {
        Bundle args = new Bundle();
        NameZipFragment fragment = new NameZipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.name_zip_fragment, container, false);
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
        this.nameZipAdapter = new NameZipAdapter(this, this.nameZipPresenter);
        this.recyclerView.setAdapter(this.nameZipAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.nameZipPresenter = new NameZipPresenterImp(this, this.netWorkingService);
        settingRecyclerView();
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
                    DialogPopUps.confirmationPopUp(this.activity, this.activity.getResources().getString(C1020R.string.alert), this.activity.getResources().getString(C1020R.string.please_enter_name_zipcode), new C11961());
                    return;
                } else {
                    this.nameZipPresenter.searchDoctor(this.et_username.getText().toString().trim());
                    return;
                }
            default:
                return;
        }
    }

    public void settingAdapter(ArrayList<DrSearchNameObject> dataList) {
        this.et_username.setText(null);
        this.nameZipAdapter.addingList(dataList);
        this.recyclerView.setVisibility(0);
        this.tv_no_data.setVisibility(8);
    }

    public void settingEmptyData() {
        this.recyclerView.setVisibility(8);
        this.tv_no_data.setVisibility(8);
    }

    public void goToPatientDetail(DrSearchNameObject drSearchNameObject) {
        SharedPreff.addingDocSearchDetail(drSearchNameObject);
        startActivity(new Intent(this.activity, DoctorBio.class));
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.nameZipAdapter != null) {
            this.et_username.setText(null);
            this.nameZipAdapter.addingList(new ArrayList());
        }
    }
}
