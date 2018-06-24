package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.PatientPendingData;

public interface PatientDetailPresenter extends BasePresenter {
    void acceptRequest(String str, String str2, int i, String str3, String str4, String str5, String str6, String str7, String str8);

    void cacelSubscription(String str, String str2, int i, String str3);

    void chatNowApi(String str, PatientPendingData patientPendingData);

    void checkChatButtonVisibility(String str, String str2);

    void deleteRequest(String str, String str2, int i, String str3, String str4, String str5, String str6, String str7);

    void makeVideoCall(String str, String str2, String str3);
}
