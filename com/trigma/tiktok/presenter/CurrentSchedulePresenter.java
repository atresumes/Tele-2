package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.SchedulePojo;

public interface CurrentSchedulePresenter extends BasePresenter {
    void deleteDataFromMain(int i, SchedulePojo schedulePojo, String str, String str2, String str3, String str4, String str5, String str6);

    void makeApiCall();

    void openDatePicker();

    void openMonthPicker();

    void openYearPicker();
}
