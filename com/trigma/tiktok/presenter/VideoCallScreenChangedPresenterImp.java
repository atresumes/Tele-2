package com.trigma.tiktok.presenter;

import android.util.Log;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.VideoCallScreenChanged;
import com.trigma.tiktok.model.AsyncMessageSend;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageFromServer;
import com.trigma.tiktok.model.MessageFromServerResponse;
import com.trigma.tiktok.model.UploadingResponse;
import com.trigma.tiktok.model.VideoCallRequiredFields;
import com.trigma.tiktok.realm.model.MessagesListObject;
import com.trigma.tiktok.realm.model.RealmControler;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoCallScreenChangedPresenterImp implements VideoCallScreenPresenter {
    private boolean isSubscriberConnected = false;
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    Observer observer = new C13197();
    private boolean schduleOverCalled = false;
    private Subscription timerSubscription;
    private VideoCallScreenChanged view;

    class C13101 implements Observer<DocAcceptReject> {
        C13101() {
        }

        public void onCompleted() {
            VideoCallScreenChangedPresenterImp.this.schduleOverCalled = false;
        }

        public void onError(Throwable e) {
            VideoCallScreenChangedPresenterImp.this.schduleOverCalled = false;
            if (VideoCallScreenChangedPresenterImp.this.view != null) {
                VideoCallScreenChangedPresenterImp.this.view.hideDialog();
                VideoCallScreenChangedPresenterImp.this.view.showError(VideoCallScreenChangedPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(DocAcceptReject contactUsResponse) {
            VideoCallScreenChangedPresenterImp.this.schduleOverCalled = false;
            if (VideoCallScreenChangedPresenterImp.this.view != null) {
                VideoCallScreenChangedPresenterImp.this.view.hideDialog();
                if (!contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    VideoCallScreenChangedPresenterImp.this.view.showError(contactUsResponse.getError());
                } else if (VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().getUserType() == 1) {
                    VideoCallScreenChangedPresenterImp.this.view.showPrescriptionDialog(1);
                } else {
                    VideoCallScreenChangedPresenterImp.this.view.gotoRatingScreen();
                }
            }
        }
    }

    class C13155 implements Observer<UploadingResponse> {
        C13155() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (VideoCallScreenChangedPresenterImp.this.view != null) {
                VideoCallScreenChangedPresenterImp.this.view.hideDialog();
                VideoCallScreenChangedPresenterImp.this.view.showError(VideoCallScreenChangedPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(UploadingResponse contactUsResponse) {
            if (VideoCallScreenChangedPresenterImp.this.view != null) {
                VideoCallScreenChangedPresenterImp.this.view.hideDialog();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    VideoCallScreenChangedPresenterImp.this.view.sendMessage(contactUsResponse.getLink(), true);
                } else {
                    VideoCallScreenChangedPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    class C13197 implements Observer {

        class C13171 implements AlertCallBackInterface {
            C13171() {
            }

            public void neutralClick() {
                VideoCallScreenChangedPresenterImp.this.view.gotoHomeScreen(VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().getUserType());
            }
        }

        class C13182 implements AlertCallBackInterface {
            C13182() {
            }

            public void neutralClick() {
                VideoCallScreenChangedPresenterImp.this.view.goToGuestHomeScreen();
            }
        }

        C13197() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object o) {
            if (VideoCallScreenChangedPresenterImp.this.view == null) {
                return;
            }
            String msg;
            if (VideoCallScreenChangedPresenterImp.this.loginDocResponce == null) {
                msg = VideoCallScreenChangedPresenterImp.this.view.getResources().getString(C1020R.string.there_is_some_problem_while_connecting_to_doctor_please_try_after_some_time);
                VideoCallScreenChangedPresenterImp.this.view.hideDialog();
                if (!VideoCallScreenChangedPresenterImp.this.isSubscriberConnected) {
                    DialogPopUps.alertPopUp(VideoCallScreenChangedPresenterImp.this.view, msg, VideoCallScreenChangedPresenterImp.this.view.getResources().getString(C1020R.string.ok_dialog), new C13182());
                }
            } else if (!VideoCallScreenChangedPresenterImp.this.isSubscriberConnected) {
                msg = "";
                if (VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().getUserType() == 1) {
                    msg = VideoCallScreenChangedPresenterImp.this.view.getResources().getString(C1020R.string.patient_did_not_picked_the_call_please_try_after_sometime);
                } else {
                    msg = VideoCallScreenChangedPresenterImp.this.view.getResources().getString(C1020R.string.there_is_some_problem_while_connecting_to_doctor_please_try_after_some_time);
                }
                VideoCallScreenChangedPresenterImp.this.view.hideDialog();
                DialogPopUps.alertPopUp(VideoCallScreenChangedPresenterImp.this.view, msg, VideoCallScreenChangedPresenterImp.this.view.getResources().getString(C1020R.string.ok_dialog), new C13171());
            }
        }
    }

    public VideoCallScreenChangedPresenterImp(VideoCallScreenChanged view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
    }

    public void checkSubscriberConnected() {
        this.timerSubscription = Observable.timer(50, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(this.observer);
    }

    public void setSubscribed(boolean val) {
        this.isSubscriberConnected = val;
    }

    public void callDisconnected(VideoCallRequiredFields videoCallRequiredFields) {
        if (!this.schduleOverCalled) {
            if (this.loginDocResponce != null) {
                if (this.view == null) {
                    return;
                }
                if (CommonUtils.isConnectedToInternet(this.view)) {
                    this.schduleOverCalled = true;
                    this.view.showProgressDialog();
                    this.mSubscription = this.netWorkingService.getAPI().makeSchduleOverCall(videoCallRequiredFields.getBookingId(), videoCallRequiredFields.getDrschedulesetsId(), SharedPreff.getDeviceToken(), videoCallRequiredFields.getSchedule(), videoCallRequiredFields.getPatientEmail(), videoCallRequiredFields.getDrName(), "" + videoCallRequiredFields.getUser(), videoCallRequiredFields.getGroupId(), videoCallRequiredFields.getPatientName()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13101());
                    return;
                }
                if (this.view != null) {
                    this.view.setCallFailed(true);
                }
                this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
            } else if (this.view != null) {
                this.view.showGuestDiconnetedPopUp();
            }
        }
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
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.mSubscription = this.netWorkingService.getAPI().getServerMessage(userId, groupId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<MessageFromServerResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                }

                public void onNext(MessageFromServerResponse messageFromServerResponse) {
                    if (VideoCallScreenChangedPresenterImp.this.view != null && messageFromServerResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && messageFromServerResponse.getData().size() > 0) {
                        final MessagesListObject messagesListObject = (MessagesListObject) RealmControler.getRealm().where(MessagesListObject.class).equalTo("unique_id", VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + uniqueID).findFirst();
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
                                chatMessage.setUser_type(VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().getUserType());
                            } else {
                                chatMessage.setSameUser(false);
                                if (VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().getUserType() == 1) {
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
                            VideoCallScreenChangedPresenterImp.this.saveChat(chatMessage, VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + uniqueID);
                            chatList.add(chatMessage);
                        }
                        VideoCallScreenChangedPresenterImp.this.view.showChatFromServer(chatList);
                        CommonUtils.chatMessageRead(VideoCallScreenChangedPresenterImp.this.view, VideoCallScreenChangedPresenterImp.this.netWorkingService, userId, groupId);
                    }
                }
            });
        } else {
            fetchChatFromDb(this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + uniqueID);
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
            u.setUserType(chatUserForDb.getUserType());
            u.setMobile(chatUserForDb.getMobile());
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
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getUploadLink(name, p).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13155());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void sendMsgToServer(AsyncMessageSend asyncMessageSend, final ChatMessage chatMessage, final String userID) {
        if (this.loginDocResponce != null && CommonUtils.isConnectedToInternet(this.view)) {
            this.mSubscription = this.netWorkingService.getAPI().sendMessageAsync(asyncMessageSend).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                }

                public void onNext(DocAcceptReject contactUsResponse) {
                    if (VideoCallScreenChangedPresenterImp.this.view != null && contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                        VideoCallScreenChangedPresenterImp.this.view.saveChatMessage(chatMessage, VideoCallScreenChangedPresenterImp.this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + userID);
                    }
                }
            });
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        if (this.timerSubscription != null) {
            this.timerSubscription.unsubscribe();
        }
        this.view = null;
    }
}
