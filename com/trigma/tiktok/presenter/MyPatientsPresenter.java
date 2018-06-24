package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.PatientPendingData;

public interface MyPatientsPresenter extends BasePresenter {
    void acceptRequest(String str, String str2, int i, String str3, String str4, String str5, String str6, String str7, String str8);

    void callActivePatient();

    void callConfirm();

    void callDelete();

    void callPendingPatient();

    void chatNowApi(String str, PatientPendingData patientPendingData);

    void deleteRequest(String str, String str2, int i, String str3, String str4, String str5, String str6, String str7, String str8);
}
