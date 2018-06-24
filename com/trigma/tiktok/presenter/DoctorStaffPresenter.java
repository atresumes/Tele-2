package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.DrStaffListObject;

public interface DoctorStaffPresenter extends BasePresenter {
    void chatNowApi(String str, DrStaffListObject drStaffListObject);

    void deactivateRequest(String str, String str2, int i, String str3, String str4);

    void fetchMyStaff(String str, String str2);

    void gotoChat(String str, String str2, int i, String str3);

    void gotoStaffDetail(DrStaffListObject drStaffListObject);
}
