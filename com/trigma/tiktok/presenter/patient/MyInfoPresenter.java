package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.model.DocUserDetail;
import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.presenter.BasePresenter;

public interface MyInfoPresenter extends BasePresenter {
    void checkSocialMediaUser();

    void checkingForDoctors();

    void doneButonFunctionality(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, DocUserDetail docUserDetail);

    void fetchDetail();

    void gotoPharmacyDetail(SelectPharmacyObject selectPharmacyObject);

    void openDatePicker();

    void selectPharmacy();

    void setBase64(String str);

    void showStateDialog();
}
