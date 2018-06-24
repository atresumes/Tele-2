package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.presenter.BasePresenter;

public interface DoctorBioPresenter extends BasePresenter {
    void chatNowApi(String str, DrSearchNameObject drSearchNameObject);

    void makeCall(String str, String str2);

    void sendRequestToDoc(String str, String str2, String str3, String str4, String str5, String str6);
}
