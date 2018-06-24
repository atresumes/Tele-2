package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.AsyncMessageSend;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

public interface VideoCallScreenPresenter extends BasePresenter {
    void callDisconnected(VideoCallRequiredFields videoCallRequiredFields);

    void checkSubscriberConnected();

    void fetchChatFromDb(String str);

    void fetchFromServer(String str, String str2, String str3);

    void gettingLink(Part part, RequestBody requestBody);

    void saveChat(ChatMessage chatMessage, String str);

    void sendMsgToServer(AsyncMessageSend asyncMessageSend, ChatMessage chatMessage, String str);

    void setSubscribed(boolean z);
}
