package com.trigma.tiktok.presenter;

public interface NotificationFromPushPresenter extends BasePresenter {
    void getNotification();

    void getPatientNotification();

    void notificationRead();
}
