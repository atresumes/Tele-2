package com.trigma.tiktok.presenter.patient;

import android.util.Log;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.patient.PatientSideStaffList;
import com.trigma.tiktok.model.ChatUserForDb;
import com.trigma.tiktok.model.DoctorChatResponse;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.DrStaffListResponce;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface2;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PatientSideStaffListPresenterImp implements PatientSideStaffListPresenter {
    private ArrayList<MyDoctorsObject> dataDoctorMainList = new ArrayList();
    private String docDeviceToken = "";
    private ArrayList<String> doctorStringList = new ArrayList();
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscription;
    private NetWorkingService netWorkingService;
    private String selected_doc;
    private String selected_doc_email;
    private String selected_doc_id;
    private PatientSideStaffList view;

    class C13583 implements AlertCallBackWithButtonsInterface2 {
        C13583() {
        }

        public void positiveClick(int selected) {
            PatientSideStaffListPresenterImp.this.selected_doc = (String) PatientSideStaffListPresenterImp.this.doctorStringList.get(selected);
            PatientSideStaffListPresenterImp.this.selected_doc_id = ((MyDoctorsObject) PatientSideStaffListPresenterImp.this.dataDoctorMainList.get(selected)).getDrId();
            PatientSideStaffListPresenterImp.this.selected_doc_email = ((MyDoctorsObject) PatientSideStaffListPresenterImp.this.dataDoctorMainList.get(selected)).getEmail();
            PatientSideStaffListPresenterImp.this.docDeviceToken = ((MyDoctorsObject) PatientSideStaffListPresenterImp.this.dataDoctorMainList.get(selected)).getDeviceToken();
            Log.e("selectedItem", PatientSideStaffListPresenterImp.this.selected_doc);
            Log.e("selected_doc_id", PatientSideStaffListPresenterImp.this.selected_doc_id);
            PatientSideStaffListPresenterImp.this.fetchMyStaff(PatientSideStaffListPresenterImp.this.selected_doc_id, "abc", PatientSideStaffListPresenterImp.this.selected_doc);
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    public PatientSideStaffListPresenterImp(PatientSideStaffList view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        gettingDocStringList();
    }

    public void gettingDocStringList() {
        ArrayList arrayList = new ArrayList();
        ArrayList<MyDoctorsObject> dataDoctorMainListTemp = SharedPreff.getDoctorList();
        for (int s = 0; s < dataDoctorMainListTemp.size(); s++) {
            if (((MyDoctorsObject) dataDoctorMainListTemp.get(s)).getStatus() == 1) {
                this.dataDoctorMainList.add(dataDoctorMainListTemp.get(s));
            }
        }
        for (int j = 0; j < this.dataDoctorMainList.size(); j++) {
            this.doctorStringList.add(((MyDoctorsObject) this.dataDoctorMainList.get(j)).getName());
        }
    }

    public void subscribeCallbacks() {
    }

    public void unSubscribeCallbacks() {
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
        this.view = null;
    }

    public void fetchMyStaff(String drId, String staffId, final String selected_doc) {
        if (this.loginDocResponce == null) {
            return;
        }
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.mSubscription = this.netWorkingService.getAPI().getDrStaffList(drId, staffId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DrStaffListResponce>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (PatientSideStaffListPresenterImp.this.view != null) {
                        PatientSideStaffListPresenterImp.this.view.hideDialog();
                        PatientSideStaffListPresenterImp.this.view.showError(PatientSideStaffListPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DrStaffListResponce drStaffListResponce) {
                    if (PatientSideStaffListPresenterImp.this.view != null) {
                        PatientSideStaffListPresenterImp.this.view.hideDialog();
                        if (drStaffListResponce.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                            PatientSideStaffListPresenterImp.this.view.settingDoctor(selected_doc);
                            try {
                                if (drStaffListResponce.getData() != null) {
                                    PatientSideStaffListPresenterImp.this.view.addingDataToList(drStaffListResponce.getData());
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            return;
        }
        this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
    }

    public void chatNowApi(final String drID, final DrStaffListObject drStaffListObject) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            String from_id = "";
            this.mSubscription = this.netWorkingService.getAPI().drDrcreatetoken(SharedPreff.getLoginResponce().getData().get_id(), drID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DoctorChatResponse>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    if (PatientSideStaffListPresenterImp.this.view != null) {
                        PatientSideStaffListPresenterImp.this.view.hideDialog();
                        PatientSideStaffListPresenterImp.this.view.showError(PatientSideStaffListPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                    }
                }

                public void onNext(DoctorChatResponse contactUsResponse) {
                    if (PatientSideStaffListPresenterImp.this.view != null) {
                        PatientSideStaffListPresenterImp.this.view.hideDialog();
                        ChatUserForDb chatUserForDb = new ChatUserForDb();
                        chatUserForDb.set_id(drID);
                        chatUserForDb.setAddress("");
                        chatUserForDb.setApiKey(contactUsResponse.getData().getApiKey());
                        chatUserForDb.setBio("");
                        chatUserForDb.setCity("");
                        chatUserForDb.setCode(drStaffListObject.getCode());
                        chatUserForDb.setCount(0);
                        chatUserForDb.setUserType(3);
                        chatUserForDb.setMobile(drStaffListObject.getMobile());
                        chatUserForDb.setDeviceToken(drStaffListObject.getDeviceToken());
                        chatUserForDb.setSessionData(contactUsResponse.getData().getSessionData());
                        chatUserForDb.setProfilePic(drStaffListObject.getProfilePic());
                        chatUserForDb.setGroupId(contactUsResponse.getData().getGroupId());
                        chatUserForDb.setDOB("");
                        chatUserForDb.setEmail(drStaffListObject.getEmail());
                        chatUserForDb.setFirstName("");
                        chatUserForDb.setLastName("");
                        chatUserForDb.setQualification("");
                        chatUserForDb.setSpeciality(drStaffListObject.getDesignation());
                        chatUserForDb.setName(drStaffListObject.getName());
                        chatUserForDb.setTokenData(contactUsResponse.getData().getTokenData());
                        chatUserForDb.setGender(drStaffListObject.getGender());
                        SharedPreff.saveChatDbDetail(chatUserForDb);
                        PatientSideStaffListPresenterImp.this.view.gotoChatScreen(chatUserForDb);
                    }
                }
            });
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void openDoctorPicker() {
        DialogPopUps.showTimePicker(this.view, CommonUtils.getArrayFromList(this.doctorStringList), new C13583());
    }

    public void gotoStaffDetail(DrStaffListObject drStaffListObject) {
        if (this.view != null) {
            this.view.gotoStaffDetail(drStaffListObject);
        }
    }
}
