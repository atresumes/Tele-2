package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.DrStaffListObject;
import java.util.ArrayList;

public interface StaffDetailPresenter extends BasePresenter {
    void chatNowApi(String str, DrStaffListObject drStaffListObject);

    void fetchStaffDocList(String str);

    void showDrList(ArrayList<String> arrayList);
}
