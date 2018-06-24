package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.DocUserDetail;

public interface MyProfilePresenter extends BasePresenter {
    void checkSocialMediaUser();

    void doneButonFunctionality(String str, String str2, String str3, String str4, String str5, String str6, DocUserDetail docUserDetail);

    void doneButonFunctionality(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, DocUserDetail docUserDetail);

    void fetchDetail();

    void setBase64(String str);

    void showStateDialog();
}
