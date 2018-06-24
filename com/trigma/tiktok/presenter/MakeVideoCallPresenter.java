package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.Upcoming;

public interface MakeVideoCallPresenter extends BasePresenter {
    void cancelAppointment(Upcoming upcoming);

    void makeVideoCall(String str, String str2, String str3);
}
