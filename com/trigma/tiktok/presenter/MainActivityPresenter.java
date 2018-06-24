package com.trigma.tiktok.presenter;

public interface MainActivityPresenter extends BasePresenter {
    void callPrescriptionApi();

    void checkingForDoctors();

    void logout();
}
