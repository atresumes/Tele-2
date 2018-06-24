package com.trigma.tiktok.presenter;

public interface StaffHomePresenter extends BasePresenter {
    void callContactUs();

    void callPrescriptionApi();

    void checkDeactivate(String str);

    void checkStaffExist(String str);

    void checkingAppointmentDates();

    void fetchStaffDocList(String str, boolean z);

    void logout(String str);

    void startNotificationCountApi();
}
