package com.trigma.tiktok.presenter;

import com.trigma.tiktok.fragments.BaseFragment;

public interface MainActivityView {
    int changeFragment(BaseFragment baseFragment);

    void gotoPrescriptionScreen(String str, int i);

    void hideDialog();

    void isDoctorActive(boolean z);

    void loginSuccessfull();

    void logoutClicked();

    void showError(String str);

    void showMessage(String str);

    void showProgressDialog();

    void showToastError(String str);
}
