package com.trigma.tiktok.presenter.patient;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.patient.SelectPharmacy;
import com.trigma.tiktok.model.GeoLocationResponce;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.model.SelectPharmacyResponse;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectPharmacyPresenterImp implements SelectPharmacyPresenter {
    private LoginDocResponce loginDocResponce;
    private Subscription mSubscriptionCounter;
    private NetWorkingService netWorkingService;
    int pos = -1;
    ArrayList<SelectPharmacyObject> selectPharmaList = new ArrayList();
    private String userID;
    private SelectPharmacy view;

    class C13621 implements Observer<SelectPharmacyResponse> {
        C13621() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            if (SelectPharmacyPresenterImp.this.view != null) {
                SelectPharmacyPresenterImp.this.view.hideDialog();
                SelectPharmacyPresenterImp.this.view.showError(SelectPharmacyPresenterImp.this.view.getResources().getString(C1020R.string.something_went_wrong));
                SelectPharmacyPresenterImp.this.view.hidePatienceMessage();
            }
        }

        public void onNext(SelectPharmacyResponse contactUsResponse) {
            if (SelectPharmacyPresenterImp.this.view != null) {
                SelectPharmacyPresenterImp.this.pos = -1;
                SelectPharmacyPresenterImp.this.selectPharmaList.clear();
                if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    ArrayList<SelectPharmacyObject> data = contactUsResponse.getData();
                    if (data.size() > 0) {
                        SelectPharmacyPresenterImp.this.getLatLong(0, data.size(), ((SelectPharmacyObject) data.get(0)).getFullAddress(), data);
                        return;
                    }
                    SelectPharmacyPresenterImp.this.view.hidePatienceMessage();
                    SelectPharmacyPresenterImp.this.view.hideDialog();
                    SelectPharmacyPresenterImp.this.view.showError(SelectPharmacyPresenterImp.this.view.getResources().getString(C1020R.string.no_pharmacy_found));
                } else if (contactUsResponse.getStatus().equalsIgnoreCase(Constants.STATUS_201)) {
                    SelectPharmacyPresenterImp.this.view.hidePatienceMessage();
                    SelectPharmacyPresenterImp.this.view.hideDialog();
                    SelectPharmacyPresenterImp.this.view.showError(SelectPharmacyPresenterImp.this.view.getResources().getString(C1020R.string.no_pharmacy_found));
                } else {
                    SelectPharmacyPresenterImp.this.view.hidePatienceMessage();
                    SelectPharmacyPresenterImp.this.view.hideDialog();
                    SelectPharmacyPresenterImp.this.view.showError(contactUsResponse.getError());
                }
            }
        }
    }

    public SelectPharmacyPresenterImp(SelectPharmacy view, NetWorkingService netWorkingService) {
        this.view = view;
        this.netWorkingService = netWorkingService;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce != null) {
            this.userID = this.loginDocResponce.getData().get_id();
        } else {
            this.userID = SharedPreff.getUerID();
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

    public void getPharmacy(String value) {
        if (CommonUtils.isConnectedToInternet(this.view)) {
            this.view.showProgressDialog();
            this.view.showPatienceMessage();
            this.mSubscriptionCounter = this.netWorkingService.getAPI().getPharmacy(value).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13621());
        } else if (this.view != null) {
            this.view.showError(this.view.getResources().getString(C1020R.string.check_your_network));
        }
    }

    public void gotoPharmacyDetail(SelectPharmacyObject detail) {
        this.view.gotoPharmacyDetail(detail);
    }

    public void getLatLong(final int pos, final int totalSize, String address, final ArrayList<SelectPharmacyObject> data) {
        this.mSubscriptionCounter = this.netWorkingService.getAPI2().getAddress(address, this.view.getString(C1020R.string.geocode)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
                if (pos == totalSize - 1) {
                    Log.e("selectPharmaList", "" + new Gson().toJson(SelectPharmacyPresenterImp.this.selectPharmaList));
                    SelectPharmacyPresenterImp.this.view.hideDialog();
                    SelectPharmacyPresenterImp.this.view.hidePatienceMessage();
                    SelectPharmacyPresenterImp.this.view.settingAdapter(SelectPharmacyPresenterImp.this.selectPharmaList);
                    return;
                }
                SelectPharmacyPresenterImp.this.getLatLong(pos + 1, data.size(), ((SelectPharmacyObject) data.get(pos + 1)).getData().getAddress(), data);
            }

            public void onNext(JsonObject jsonObject) {
                GeoLocationResponce geoLocationResponce = CommonUtils.jsonParsorForGeo(jsonObject);
                if (geoLocationResponce != null) {
                    SelectPharmacyObject selectPharmacyObject = (SelectPharmacyObject) data.get(pos);
                    selectPharmacyObject.setLat("" + geoLocationResponce.getLatitude());
                    selectPharmacyObject.setLong("" + geoLocationResponce.getLongitude());
                    SelectPharmacyPresenterImp.this.selectPharmaList.add(selectPharmacyObject);
                    if (pos == totalSize - 1) {
                        Log.e("selectPharmaList", "" + new Gson().toJson(SelectPharmacyPresenterImp.this.selectPharmaList));
                        SelectPharmacyPresenterImp.this.view.settingAdapter(SelectPharmacyPresenterImp.this.selectPharmaList);
                        SelectPharmacyPresenterImp.this.view.hideDialog();
                        SelectPharmacyPresenterImp.this.view.hidePatienceMessage();
                        return;
                    }
                    SelectPharmacyPresenterImp.this.getLatLong(pos + 1, data.size(), ((SelectPharmacyObject) data.get(pos + 1)).getFullAddress(), data);
                } else if (pos == totalSize - 1) {
                    Log.e("selectPharmaList", "" + new Gson().toJson(SelectPharmacyPresenterImp.this.selectPharmaList));
                    SelectPharmacyPresenterImp.this.view.settingAdapter(SelectPharmacyPresenterImp.this.selectPharmaList);
                    SelectPharmacyPresenterImp.this.view.hideDialog();
                    SelectPharmacyPresenterImp.this.view.hidePatienceMessage();
                } else {
                    SelectPharmacyPresenterImp.this.getLatLong(pos + 1, data.size(), ((SelectPharmacyObject) data.get(pos + 1)).getFullAddress(), data);
                }
            }
        });
    }
}
