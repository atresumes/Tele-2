package com.trigma.tiktok.presenter;

public interface CreateSchedulePresenter extends BasePresenter {
    void checkValidate();

    void openDatePicker();

    void openFromTimePicker();

    void openToTimePicker();

    void saveButtonAction();

    void setTimeSlots(int i);

    void settingCurrentDate(int i, int i2, int i3);

    void settingMaximumSelectdSlot(int i);

    void settingMinimumSelectdSlot(int i);
}
