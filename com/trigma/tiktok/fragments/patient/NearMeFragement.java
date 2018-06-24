package com.trigma.tiktok.fragments.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.patient.DoctorBio;
import com.trigma.tiktok.adapter.patient.NearMeFragementAdapter;
import com.trigma.tiktok.fragments.BaseFragment;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.presenter.patient.NearMePresenter;
import com.trigma.tiktok.presenter.patient.NearMePresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.List;

public class NearMeFragement extends BaseFragment implements OnMapReadyCallback {
    private Activity activity;
    private CardView card_view;
    public GoogleMap googleMap;
    private ImageView header;
    private boolean isDataSet = false;
    private boolean isVisible = false;
    private NearMeFragementAdapter nearMeFragementAdapter;
    private NearMePresenter nearMePresenter;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    private TextView tv_no_data;
    private LatLng usLatLng = new LatLng(36.1699d, -115.1398d);

    public static NearMeFragement newInstance(int page) {
        Bundle args = new Bundle();
        NearMeFragement fragment = new NearMeFragement();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.near_me, container, false);
        initViews(screen);
        settingEmptyData(true);
        return screen;
    }

    private void initViews(View screen) {
        this.recyclerView = (RecyclerView) screen.findViewById(C1020R.id.recyclerView);
        this.tv_no_data = (TextView) screen.findViewById(C1020R.id.tv_no_data);
        this.card_view = (CardView) screen.findViewById(C1020R.id.card_view);
        this.header = (ImageView) screen.findViewById(C1020R.id.header);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(C1020R.id.map)).getMapAsync(this);
    }

    private void settingRecyclerView() {
        this.nearMeFragementAdapter = new NearMeFragementAdapter(this, this.nearMePresenter);
        this.recyclerView.setAdapter(this.nearMeFragementAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.nearMePresenter.unSubscribeCallbacks();
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

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.netWorkingService = ((TikTokApp) this.activity.getApplication()).getNetworkService();
        this.nearMePresenter = new NearMePresenterImp(this, this.netWorkingService);
        settingRecyclerView();
    }

    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (!(!this.isVisible || this.isDataSet || this.nearMePresenter == null)) {
            this.nearMePresenter.searchDoctor();
        }
        if (this.nearMeFragementAdapter != null && this.nearMeFragementAdapter.getItemCount() == 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.usLatLng, 10.0f));
        }
    }

    public void settingAdapter(ArrayList<DrSearchNameObject> dataList) {
        this.nearMeFragementAdapter.addingList(dataList);
        if (dataList.size() > 0) {
            this.recyclerView.setVisibility(0);
            this.tv_no_data.setVisibility(8);
            this.header.setVisibility(0);
            this.card_view.setVisibility(0);
            this.isDataSet = true;
            if (this.googleMap != null) {
                this.googleMap.clear();
                ArrayList<LatLng> latLngs = new ArrayList();
                for (int z = 0; z < dataList.size(); z++) {
                    try {
                        double latt = Double.parseDouble(((DrSearchNameObject) dataList.get(z)).getLat());
                        double longg = Double.parseDouble(((DrSearchNameObject) dataList.get(z)).getLong());
                        latLngs.add(new LatLng(latt, longg));
                        Marker marker = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(latt, longg)).title(((DrSearchNameObject) dataList.get(z)).getAddress()).icon(BitmapDescriptorFactory.defaultMarker(0.0f)));
                        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, longg), 15.0f));
                        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, longg), 15.0f));
                        marker.showInfoWindow();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                zoomRoute(this.googleMap, latLngs);
                return;
            }
            return;
        }
        showError(this.activity.getResources().getString(C1020R.string.no_doctors_found_using_this_criteria));
    }

    public void settingEmptyData(boolean hideErrorText) {
        if (hideErrorText) {
            this.tv_no_data.setVisibility(8);
        } else {
            this.tv_no_data.setVisibility(8);
        }
        this.recyclerView.setVisibility(8);
        this.header.setVisibility(0);
        this.card_view.setVisibility(0);
    }

    public void goToPatientDetail(DrSearchNameObject drSearchNameObject) {
        SharedPreff.addingDocSearchDetail(drSearchNameObject);
        startActivity(new Intent(this.activity, DoctorBio.class));
    }

    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {
        if (googleMap != null && lstLatLngRoute != null && !lstLatLngRoute.isEmpty()) {
            Builder boundsBuilder = new Builder();
            for (LatLng latLngPoint : lstLatLngRoute) {
                boundsBuilder.include(latLngPoint);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
        if (isVisibleToUser && this.nearMePresenter != null && !this.isDataSet) {
            this.nearMePresenter.searchDoctor();
        }
    }

    public void showToastError(String error) {
        Toast.makeText(this.activity.getApplicationContext(), error, 0).show();
    }
}
