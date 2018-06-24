package com.trigma.tiktok.presenter;

import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.ChatScreen;
import com.trigma.tiktok.model.AsyncMessageSend;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageFromServer;
import com.trigma.tiktok.model.MessageFromServerResponse;
import com.trigma.tiktok.model.UploadingResponse;
import com.trigma.tiktok.realm.model.MessagesListObject;
import com.trigma.tiktok.realm.model.RealmControler;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmList;
import java.util.ArrayList;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatScreenPresenterImp implements ChatScreenPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private ChatScreen view;

    class C12094 implements Observer<UploadingResponse> {
        C12094() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (ChatScreenPresenterImp.this.view != null) {
                ChatScreenPresenterImp.this.view.hideDialog();
                ChatScreenPresenterImp.this.view.showError(ChatScreenPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(UploadingResponse contactUsResponse) {
            if (ChatScreenPresenterImp.this.view != null) {
                ChatScreenPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    ChatScreenPresenterImp.this.view.sendMessage(contactUsResponse.getLink(), true);
                } else {
                    ChatScreenPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    public ChatScreenPresenterImp(ChatScreen view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.loginDocResponce = SharedPreff.getStaffLoginResponse();
        } else {
            this.loginDocResponce = SharedPreff.getLoginResponce();
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscriptionCounter != null) {
            this.mSubscriptionCounter.unsubscribe();
        }
        this.view = null;
    }

    public void fetchChatFromDb(String id) {
        Log.e("unique_id", id);
        try {
            MessagesListObject messagesListObject = (MessagesListObject) RealmControler.getRealm().where(MessagesListObject.class).equalTo("unique_id", id).findFirst();
            ArrayList<ChatMessage> dataList = new ArrayList();
            RealmList realmList = new RealmList();
            RealmList<ChatMessage> results = messagesListObject.getChatList();
            if (results != null && results.size() > 0) {
                for (int a = 0; a < results.size(); a++) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessageText(((ChatMessage) results.get(a)).getMessageText());
                    chatMessage.setName(((ChatMessage) results.get(a)).getName());
                    chatMessage.setType(((ChatMessage) results.get(a)).getType());
                    chatMessage.setUser_type(((ChatMessage) results.get(a)).getUser_type());
                    chatMessage.setLink(((ChatMessage) results.get(a)).getLink());
                    chatMessage.setSameUser(((ChatMessage) results.get(a)).isSameUser());
                    dataList.add(chatMessage);
                }
                this.view.showChatFromDb(dataList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchFromServer(final String groupId, final String userId, final String uniqueID) {
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this.view)) {
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getServerMessage(userId, groupId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<MessageFromServerResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                }

                public void onNext(MessageFromServerResponse messageFromServerResponse) {
                    if (ChatScreenPresenterImp.this.view != null) {
                        Log.e("FromServerResponse", "" + new Gson().toJson((Object) messageFromServerResponse));
                        if (messageFromServerResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && messageFromServerResponse.getData().size() > 0) {
                            final MessagesListObject messagesListObject = (MessagesListObject) RealmControler.getRealm().where(MessagesListObject.class).equalTo("unique_id", ChatScreenPresenterImp.this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + uniqueID).findFirst();
                            if (messagesListObject != null) {
                                RealmControler.getRealm().executeTransaction(new Transaction() {
                                    public void execute(Realm realm) {
                                        messagesListObject.getChatList().clear();
                                        realm.copyToRealm(messagesListObject);
                                    }
                                });
                            }
                            ArrayList<ChatMessage> chatList = new ArrayList();
                            for (int a = 0; a < messageFromServerResponse.getData().size(); a++) {
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setLink(((MessageFromServer) messageFromServerResponse.getData().get(a)).getMessage());
                                chatMessage.setName(((MessageFromServer) messageFromServerResponse.getData().get(a)).getDisplayName());
                                chatMessage.setMessageText(((MessageFromServer) messageFromServerResponse.getData().get(a)).getMessage());
                                if (((MessageFromServer) messageFromServerResponse.getData().get(a)).getFrom().equalsIgnoreCase(userId)) {
                                    chatMessage.setSameUser(true);
                                    chatMessage.setUser_type(ChatScreenPresenterImp.this.loginDocResponce.getData().getUserType());
                                } else {
                                    chatMessage.setSameUser(false);
                                    if (ChatScreenPresenterImp.this.loginDocResponce.getData().getUserType() == 1) {
                                        chatMessage.setUser_type(0);
                                    } else {
                                        chatMessage.setUser_type(1);
                                    }
                                }
                                if (((MessageFromServer) messageFromServerResponse.getData().get(a)).getMessageType().equalsIgnoreCase("link")) {
                                    chatMessage.setType("link");
                                } else {
                                    chatMessage.setType("string");
                                }
                                ChatScreenPresenterImp.this.saveChat(chatMessage, ChatScreenPresenterImp.this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + uniqueID);
                                chatList.add(chatMessage);
                            }
                            ChatScreenPresenterImp.this.view.showChatFromServer(chatList);
                            CommonUtils.chatMessageRead(ChatScreenPresenterImp.this.view, ChatScreenPresenterImp.this.netWorkingService, userId, groupId);
                        }
                    }
                }
            });
        }
    }

    public void saveChat(final ChatMessage chatMessage, final String id) {
        Log.e("unique_id", id);
        final MessagesListObject messagesListObject = (MessagesListObject) RealmControler.getRealm().where(MessagesListObject.class).equalTo("unique_id", id).findFirst();
        if (messagesListObject != null) {
            RealmControler.getRealm().executeTransaction(new Transaction() {
                public void execute(Realm realm) {
                    messagesListObject.getChatList().add(chatMessage);
                    realm.copyToRealm(chatMessage);
                }
            });
            return;
        }
        ChatUserForDb chatUserForDb = SharedPreff.getChatDbDetail();
        if (chatUserForDb != null) {
            final MessagesListObject u = new MessagesListObject();
            u.set_id(chatUserForDb.get_id());
            u.setUnique_id(this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + chatUserForDb.get_id());
            u.setAddress(chatUserForDb.getAddress());
            u.setApiKey(chatUserForDb.getApiKey());
            u.setBio(chatUserForDb.getBio());
            u.setCity(chatUserForDb.getCity());
            u.setCode(chatUserForDb.getCode());
            u.setCount(chatUserForDb.getCount());
            u.setMobile(chatUserForDb.getMobile());
            u.setUserType(chatUserForDb.getUserType());
            u.setDeviceToken(chatUserForDb.getDeviceToken());
            u.setSessionData(chatUserForDb.getSessionData());
            u.setProfilePic(chatUserForDb.getProfilePic());
            u.setGroupId(chatUserForDb.getGroupId());
            u.setDOB(chatUserForDb.getDOB());
            u.setEmail(chatUserForDb.getEmail());
            u.setFirstName(chatUserForDb.getFirstName());
            u.setLastName(chatUserForDb.getLastName());
            u.setQualification(chatUserForDb.getQualification());
            u.setName(chatUserForDb.getName());
            u.setTokenData(chatUserForDb.getTokenData());
            u.setGender(chatUserForDb.getGender());
            u.setSpeciality(chatUserForDb.getSpeciality());
            RealmControler.getRealm().executeTransaction(new Transaction() {
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(u);
                    MessagesListObject messagesListObject2 = (MessagesListObject) realm.where(MessagesListObject.class).equalTo("unique_id", id).findFirst();
                    if (messagesListObject2 != null) {
                        messagesListObject2.getChatList().add(chatMessage);
                        realm.copyToRealm(chatMessage);
                    }
                }
            });
        }
    }

    public void gettingLink(Part p, RequestBody name) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getUploadLink(name, p).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12094());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void sendMsgToServer(AsyncMessageSend asyncMessageSend, final ChatMessage chatMessage, final String userID) {
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this.view)) {
            this.mSubscriptionCounter = this.netWorkingService.getAPI().sendMessageAsync(asyncMessageSend).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                }

                public void onNext(DocAcceptReject contactUsResponse) {
                    if (ChatScreenPresenterImp.this.view != null && contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        ChatScreenPresenterImp.this.view.saveChatMessage(chatMessage, ChatScreenPresenterImp.this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + userID);
                    }
                }
            });
        }
    }
}
