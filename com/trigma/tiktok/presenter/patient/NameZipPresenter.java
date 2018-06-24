package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.presenter.BasePresenter;

public interface NameZipPresenter extends BasePresenter {
    void goToDetail(DrSearchNameObject drSearchNameObject);

    void gotoDoctorDetail();

    void searchDoctor(String str);
}
