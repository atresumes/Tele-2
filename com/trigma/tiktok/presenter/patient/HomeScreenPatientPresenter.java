package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.presenter.BasePresenter;

public interface HomeScreenPatientPresenter extends BasePresenter {
    void callContactUs();

    void checkDeactivate();

    void checkingAppointmentDates();

    void checkingForDoctors(boolean z, boolean z2);

    void logout();

    void startNotificationCountApi();
}
