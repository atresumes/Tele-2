package com.trigma.tiktok.presenter;

import android.util.Log;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.MessagesScreen;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageUserDetail;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.model.MessagesResponse;
import com.trigma.tiktok.realm.model.MessagesListObject;
import com.trigma.tiktok.realm.model.RealmControler;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmResults;
import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MessagesScreenPresImp implements MessagesScreenPres {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    private MessagesScreen view;

    class C12521 implements Observer<MessagesResponse> {
        C12521() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (MessagesScreenPresImp.this.view != null) {
                MessagesScreenPresImp.this.view.hideDialog();
                MessagesScreenPresImp.this.view.showError(MessagesScreenPresImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
            }
        }

        public void onNext(MessagesResponse messagesResponse) {
            if (MessagesScreenPresImp.this.view != null) {
                Log.e("messagesResponse", new Gson().toJson((Object) messagesResponse));
                MessagesScreenPresImp.this.view.hideDialog();
                if (messagesResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    if (messagesResponse.getUser().size() > 0) {
                        MessagesScreenPresImp.this.addingDataToDatabase(messagesResponse.getUser());
                    }
                } else if (messagesResponse.getStatus().equalsIgnoreCase(Constants.STATUS_401)) {
                    MessagesScreenPresImp.this.view.settingAdapter(new ArrayList());
                } else {
                    MessagesScreenPresImp.this.view.showError(messagesResponse.getError());
                }
            }
        }
    }

    public MessagesScreenPresImp(MessagesScreen view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.loginDocResponce = SharedPreff.getStaffLoginResponse();
        } else {
            this.loginDocResponce = SharedPreff.getLoginResponce();
        }
    }

    public void fetchMessageList(int isDoc) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view.getActivity())) {
            this.view.showProgressDialog();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getMessagesList(this.loginDocResponce.getData().get_id(), "" + isDoc).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C12521());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void goToChatScreen(MessageUserObject messageUserObject, String uniqueId, boolean isFetchFromServer) {
        this.view.goToChatScreen(messageUserObject, uniqueId, isFetchFromServer);
    }

    private void addingDataToDatabase(ArrayList<MessageUserObject> user) {
        RealmResults<MessagesListObject> results = RealmControler.getRealm().where(MessagesListObject.class).findAll();
        for (int q = 0; q < results.size(); q++) {
            boolean isMatched = false;
            for (int w = 0; w < user.size(); w++) {
                if (((MessagesListObject) results.get(q)).getUnique_id().equalsIgnoreCase(this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + ((MessageUserObject) user.get(w)).getId())) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                final MessagesListObject messagesListObject = (MessagesListObject) RealmControler.getRealm().where(MessagesListObject.class).equalTo("unique_id", ((MessagesListObject) results.get(q)).getUnique_id()).findFirst();
                if (messagesListObject != null) {
                    RealmControler.getRealm().executeTransaction(new Transaction() {
                        public void execute(Realm realm) {
                            messagesListObject.deleteFromRealm();
                        }
                    });
                }
            }
        }
        addingNewDataInList(user);
    }

    private void addingNewDataInList(ArrayList<MessageUserObject> user) {
        RealmResults<MessagesListObject> results = RealmControler.getRealm().where(MessagesListObject.class).findAll();
        for (int q = 0; q < user.size(); q++) {
            boolean isMatched = false;
            for (int w = 0; w < results.size(); w++) {
                if ((this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + ((MessageUserObject) user.get(q)).getId()).equalsIgnoreCase(((MessagesListObject) results.get(w)).getUnique_id())) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                final MessagesListObject u = new MessagesListObject();
                u.set_id(((MessageUserObject) user.get(q)).getId());
                u.setUnique_id(this.loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + ((MessageUserObject) user.get(q)).getId());
                u.setAddress(((MessageUserObject) user.get(q)).getUser().getAddress());
                u.setApiKey(((MessageUserObject) user.get(q)).getApiKey());
                u.setBio(((MessageUserObject) user.get(q)).getUser().getBio());
                u.setCity(((MessageUserObject) user.get(q)).getUser().getCity());
                u.setCode(((MessageUserObject) user.get(q)).getUser().getCode());
                u.setCount(((MessageUserObject) user.get(q)).getCount());
                u.setMobile(((MessageUserObject) user.get(q)).getUser().getMobile());
                u.setDeviceToken(((MessageUserObject) user.get(q)).getDeviceToken());
                u.setUserType(((MessageUserObject) user.get(q)).getUserType());
                u.setSessionData(((MessageUserObject) user.get(q)).getSessionData());
                u.setProfilePic(((MessageUserObject) user.get(q)).getProfilePic());
                u.setGroupId(((MessageUserObject) user.get(q)).getGroupId());
                u.setDOB(((MessageUserObject) user.get(q)).getDOB());
                u.setEmail(((MessageUserObject) user.get(q)).getUser().getEmail());
                u.setFirstName(((MessageUserObject) user.get(q)).getUser().getFirstName());
                u.setLastName(((MessageUserObject) user.get(q)).getUser().getLastName());
                u.setQualification(((MessageUserObject) user.get(q)).getUser().getQualification());
                u.setName(((MessageUserObject) user.get(q)).getName());
                u.setTokenData(((MessageUserObject) user.get(q)).getTokenData());
                u.setGender(((MessageUserObject) user.get(q)).getGender());
                RealmControler.getRealm().executeTransaction(new Transaction() {
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(u);
                    }
                });
            }
        }
        if (this.view != null) {
            ArrayList<MessageUserObject> readList = new ArrayList();
            ArrayList<MessageUserObject> unReadList = new ArrayList();
            ArrayList<MessageUserObject> finalList = new ArrayList();
            for (int d = 0; d < user.size(); d++) {
                if (((MessageUserObject) user.get(d)).getMessagedotshow() == 0) {
                    unReadList.add(user.get(d));
                } else {
                    readList.add(user.get(d));
                }
            }
            finalList.addAll(unReadList);
            finalList.addAll(readList);
            if (finalList.size() > 0) {
                this.view.settingAdapter(finalList);
            } else {
                this.view.settingAdapter(user);
            }
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
    }

    public void settingAdapterFromDB() {
        ArrayList<MessageUserObject> dataList = new ArrayList();
        RealmResults<MessagesListObject> results = RealmControler.getRealm().where(MessagesListObject.class).findAll();
        if (results.size() > 0) {
            for (int a = 0; a < results.size(); a++) {
                MessageUserObject messageUserObject = new MessageUserObject();
                MessageUserDetail messageUserDetail = new MessageUserDetail();
                messageUserObject.setGender(((MessagesListObject) results.get(a)).getGender());
                messageUserObject.setTokenData(((MessagesListObject) results.get(a)).getTokenData());
                messageUserObject.setDOB(((MessagesListObject) results.get(a)).getDOB());
                messageUserObject.setName(((MessagesListObject) results.get(a)).getName());
                messageUserObject.setApiKey(((MessagesListObject) results.get(a)).getApiKey());
                messageUserObject.setCount(((MessagesListObject) results.get(a)).getCount());
                messageUserObject.setGroupId(((MessagesListObject) results.get(a)).getGroupId());
                messageUserObject.setDeviceToken(((MessagesListObject) results.get(a)).getDeviceToken());
                messageUserObject.setId(((MessagesListObject) results.get(a)).get_id());
                messageUserObject.setSessionData(((MessagesListObject) results.get(a)).getSessionData());
                messageUserObject.setProfilePic(((MessagesListObject) results.get(a)).getProfilePic());
                messageUserObject.setSpeciality(((MessagesListObject) results.get(a)).getSpeciality());
                messageUserObject.setType(((MessagesListObject) results.get(a)).getType());
                messageUserObject.setUserType(((MessagesListObject) results.get(a)).getUserType());
                messageUserDetail.setUserType(((MessagesListObject) results.get(a)).getUserType());
                messageUserDetail.setSpeciality(((MessagesListObject) results.get(a)).getSpeciality());
                messageUserDetail.setProfilePic(((MessagesListObject) results.get(a)).getProfilePic());
                messageUserDetail.setAddress(((MessagesListObject) results.get(a)).getAddress());
                messageUserDetail.setBio(((MessagesListObject) results.get(a)).getBio());
                messageUserDetail.setCity(((MessagesListObject) results.get(a)).getCity());
                messageUserDetail.setDOB(((MessagesListObject) results.get(a)).getDOB());
                messageUserDetail.setFirstName(((MessagesListObject) results.get(a)).getFirstName());
                messageUserDetail.setLastName(((MessagesListObject) results.get(a)).getLastName());
                messageUserDetail.setQualification(((MessagesListObject) results.get(a)).getQualification());
                messageUserDetail.setCode(((MessagesListObject) results.get(a)).getCode());
                messageUserDetail.setMobile(((MessagesListObject) results.get(a)).getMobile());
                messageUserObject.setUser(messageUserDetail);
                dataList.add(messageUserObject);
            }
        }
        if (this.view != null) {
            this.view.settingAdapter(dataList);
        }
    }
}
