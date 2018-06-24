package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.presenter.BasePresenter;

public interface MyDoctorsPresenter extends BasePresenter {
    void addNewDoctor();

    void gotoDocBio(DrSearchNameObject drSearchNameObject);

    void loadDoctors();

    void sendRequestToDoc(String str, String str2, String str3, String str4, String str5, String str6, int i);
}
