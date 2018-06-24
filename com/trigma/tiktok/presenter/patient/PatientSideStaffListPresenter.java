package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.presenter.BasePresenter;

public interface PatientSideStaffListPresenter extends BasePresenter {
    void chatNowApi(String str, DrStaffListObject drStaffListObject);

    void fetchMyStaff(String str, String str2, String str3);

    void gotoStaffDetail(DrStaffListObject drStaffListObject);

    void openDoctorPicker();
}
