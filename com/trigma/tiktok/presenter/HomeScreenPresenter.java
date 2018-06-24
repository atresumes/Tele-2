package com.trigma.tiktok.presenter;

public interface HomeScreenPresenter extends BasePresenter {
    void callContactUs();

    void callPrescriptionApi();

    void checkDeactivate();

    void checkingAppointmentDates();

    void logout();

    void startNotificationCountApi();
}
