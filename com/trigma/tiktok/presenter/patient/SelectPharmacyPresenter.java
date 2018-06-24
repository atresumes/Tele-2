package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.presenter.BasePresenter;

public interface SelectPharmacyPresenter extends BasePresenter {
    void getPharmacy(String str);

    void gotoPharmacyDetail(SelectPharmacyObject selectPharmacyObject);
}
