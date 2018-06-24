package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.presenter.BasePresenter;

public interface SelfSchedulePresenter extends BasePresenter {
    void openDatePicker();

    void openDoctorPicker();

    void openDurationPicker();

    void openMonthPicker();

    void openTimePicker();

    void openYearPicker();

    void saveButtonAction();
}
