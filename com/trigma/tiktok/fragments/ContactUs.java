package com.trigma.tiktok.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.model.ContactUsResponse;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.presenter.ContactUsPresenter;
import com.trigma.tiktok.presenter.ContactUsPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;

public class ContactUs extends BaseFragment implements OnMapReadyCallback, OnClickListener {
    private Activity activity;
    private ContactUsPresenter contactUsPresenter;
    private ContactUsResponse contactUsResponse = null;
    public GoogleMap googleMap;
    private ImageView img_header_back;
    private ImageView img_slider;
    private boolean isInfoWindowShown = false;
    private RelativeLayout loading_layout;
    LoginDocResponce loginDocResponce;
    private LinearLayout main_layout;
    Marker marker;
    public NetWorkingService netWorkingService;
    private TextView title_name;
    private TextView tv_address;
    private TextView tv_email;
    private TextView tv_error;
    private TextView tv_phone;
    private TextView where_to_find_us;

    class C11501 implements OnMarkerClickListener {
        C11501() {
        }

        public boolean onMarkerClick(Marker marker) {
            if (marker != null) {
                if (ContactUs.this.isInfoWindowShown) {
                    ContactUs.this.isInfoWindowShown = false;
                    marker.hideInfoWindow();
                } else {
                    ContactUs.this.isInfoWindowShown = true;
                    marker.showInfoWindow();
                }
            }
            return false;
        }
    }

    class C11512 implements AlertCallBackWithButtonsInterface {
        C11512() {
        }

        public void positiveClick() {
        }

        public void neutralClick() {
        }

        public void negativeClick() {
            CommonUtils.call(ContactUs.this.activity, ContactUs.this.tv_phone.getText().toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.contact_us, container, false);
        initViews(screen);
        return screen;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.loginDocResponce = SharedPreff.getLoginResponce();
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.contactUsPresenter = new ContactUsPresenterImp(this, this.netWorkingService);
        this.contactUsPresenter.callApi();
        this.title_name.setText(this.activity.getResources().getString(C1020R.string.contact_us));
    }

    private void initViews(View screen) {
        this.tv_address = (TextView) screen.findViewById(C1020R.id.tv_address);
        this.tv_phone = (TextView) screen.findViewById(C1020R.id.tv_phone);
        this.tv_email = (TextView) screen.findViewById(C1020R.id.tv_email);
        this.tv_error = (TextView) screen.findViewById(C1020R.id.tv_error);
        this.where_to_find_us = (TextView) screen.findViewById(C1020R.id.where_to_find_us);
        this.img_slider = (ImageView) screen.findViewById(C1020R.id.img_slider);
        this.img_header_back = (ImageView) screen.findViewById(C1020R.id.img_header_back);
        this.title_name = (TextView) screen.findViewById(C1020R.id.title_name);
        this.main_layout = (LinearLayout) screen.findViewById(C1020R.id.main_layout);
        this.loading_layout = (RelativeLayout) screen.findViewById(C1020R.id.loading_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(C1020R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);
        clickListners();
        showProgressLayout(true, "");
    }

    private void clickListners() {
        this.tv_email.setOnClickListener(this);
        this.tv_phone.setOnClickListener(this);
        this.img_slider.setOnClickListener(this);
        this.img_header_back.setOnClickListener(this);
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this.activity, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this.activity, error);
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void showProgressLayout(boolean value, String error) {
        if (value) {
            this.loading_layout.setVisibility(0);
            this.where_to_find_us.setVisibility(8);
            this.main_layout.setVisibility(8);
            this.tv_error.setText(error);
            return;
        }
        this.loading_layout.setVisibility(8);
        this.where_to_find_us.setVisibility(0);
        this.main_layout.setVisibility(0);
        this.tv_error.setText(error);
    }

    public void apiResponseSuccess(ContactUsResponse contactUsResponse) {
        this.contactUsResponse = contactUsResponse;
        settingValues(contactUsResponse);
    }

    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (this.contactUsResponse != null) {
            settingValues(this.contactUsResponse);
        }
        googleMap.setOnMarkerClickListener(new C11501());
    }

    public void settingValues(ContactUsResponse contactUsRespons) {
        this.tv_address.setText(contactUsRespons.getData().getAddress());
        this.tv_phone.setText(contactUsRespons.getData().getPhone());
        this.tv_email.setText(contactUsRespons.getData().getEmail());
        if (this.googleMap != null) {
            this.googleMap.clear();
            try {
                double latt = Double.parseDouble(contactUsRespons.getData().getLat());
                double longg = Double.parseDouble(contactUsRespons.getData().getLong());
                this.marker = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(latt, longg)).title(contactUsRespons.getData().getAddress()).icon(BitmapDescriptorFactory.defaultMarker(0.0f)));
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, longg), 8.0f));
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, longg), 8.0f));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                if (this.loginDocResponce == null) {
                    return;
                }
                if (this.loginDocResponce.getData().getUserType() == 0) {
                    PatientMainActivity.openDrawer();
                    return;
                } else {
                    MainActivity.openDrawer();
                    return;
                }
            case C1020R.id.img_header_back:
                getActivity().finish();
                return;
            case C1020R.id.tv_phone:
                DialogPopUps.showAlertWithButtons(this.activity, this.activity.getResources().getString(C1020R.string.please_confirm_your_call), this.activity.getResources().getString(C1020R.string.You_re_about_to_call_the_following) + "\n" + this.tv_phone.getText().toString(), this.activity.getResources().getString(C1020R.string.no_dialog), this.activity.getResources().getString(C1020R.string.yes_dialog), "", false, true, new C11512());
                return;
            case C1020R.id.tv_email:
                CommonUtils.sendEmailTo(this.activity, "", this.tv_email.getText().toString());
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.contactUsPresenter.unSubscribeCallbacks();
        this.marker = null;
        this.googleMap = null;
    }
}
