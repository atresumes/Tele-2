package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.MessageUserObject;

public interface MessagesScreenPres extends BasePresenter {
    void fetchMessageList(int i);

    void goToChatScreen(MessageUserObject messageUserObject, String str, boolean z);

    void settingAdapterFromDB();
}
