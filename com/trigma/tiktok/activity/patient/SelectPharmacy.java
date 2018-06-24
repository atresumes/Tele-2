package com.trigma.tiktok.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.activity.BaseActivity;
import com.trigma.tiktok.activity.DashBoard;
import com.trigma.tiktok.activity.HomeScreen;
import com.trigma.tiktok.adapter.patient.SelectPharmacyAdapter;
import com.trigma.tiktok.model.CreateUserPojo;
import com.trigma.tiktok.model.LoginDocData;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.presenter.patient.SelectPharmacyPresenter;
import com.trigma.tiktok.presenter.patient.SelectPharmacyPresenterImp;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.LocationTrackerClass;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.List;

public class SelectPharmacy extends BaseActivity implements OnMapReadyCallback, OnClickListener {
    private static final int REQUEST_CODE = 100;
    private EditText et_first_name;
    public GoogleMap googleMap;
    private ImageView img_header_back;
    private ImageView img_slider;
    ArrayList<LatLng> latLngs = new ArrayList();
    public LocationTrackerClass locationTrackerClass;
    public NetWorkingService netWorkingService;
    private RecyclerView recyclerView;
    ArrayList<SelectPharmacyObject> selectPharmaListMain = new ArrayList();
    private SelectPharmacyAdapter selectPharmacyAdapter;
    private SelectPharmacyPresenter selectPharmacyPresenter;
    private boolean show_back;
    private TextView title_name;
    private TextView tv_patience;
    private LatLng usLatLng = new LatLng(36.1699d, -115.1398d);

    class C10951 implements AlertCallBackInterface {
        C10951() {
        }

        public void neutralClick() {
        }
    }

    class C10972 implements OnEditorActionListener {

        class C10961 implements AlertCallBackInterface {
            C10961() {
            }

            public void neutralClick() {
                SelectPharmacy.this.et_first_name.setText(null);
            }
        }

        C10972() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {
                if (TextUtils.isEmpty(SelectPharmacy.this.et_first_name.getText().toString()) || SelectPharmacy.this.et_first_name.getText().toString().trim().equalsIgnoreCase("")) {
                    DialogPopUps.alertPopUp(SelectPharmacy.this, SelectPharmacy.this.getResources().getString(C1020R.string.please_enter_zipcode_error), SelectPharmacy.this.getResources().getString(C1020R.string.ok_dialog), new C10961());
                } else {
                    SelectPharmacy.this.selectPharmacyPresenter.getPharmacy(SelectPharmacy.this.et_first_name.getText().toString());
                }
            }
            return false;
        }
    }

    class C10983 implements TextWatcher {
        C10983() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString()) || s.toString().equalsIgnoreCase("")) {
                SelectPharmacy.this.settingAdapter(new ArrayList());
                if (SelectPharmacy.this.selectPharmacyAdapter != null && SelectPharmacy.this.selectPharmacyAdapter.getItemCount() == 0 && SelectPharmacy.this.googleMap != null) {
                    SelectPharmacy.this.googleMap.clear();
                    SelectPharmacy.this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(SelectPharmacy.this.usLatLng, 10.0f));
                }
            }
        }
    }

    class C10994 implements OnInfoWindowClickListener {
        C10994() {
        }

        public void onInfoWindowClick(Marker marker) {
            LatLng latLon = marker.getPosition();
            for (int f = 0; f < SelectPharmacy.this.latLngs.size(); f++) {
                if (latLon.equals((LatLng) SelectPharmacy.this.latLngs.get(f))) {
                    SelectPharmacy.this.gotoPharmacyDetail((SelectPharmacyObject) SelectPharmacy.this.selectPharmaListMain.get(f));
                }
            }
        }
    }

    class C11005 implements OnCameraMoveListener {
        C11005() {
        }

        public void onCameraMove() {
        }
    }

    class C11016 implements AlertCallBackInterface {
        C11016() {
        }

        public void neutralClick() {
            SelectPharmacy.this.et_first_name.setText(null);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.select_pharmacy);
        if (getIntent().hasExtra(Constants.SHOW_BACK)) {
            this.show_back = getIntent().getBooleanExtra(Constants.SHOW_BACK, false);
        }
        this.locationTrackerClass = new LocationTrackerClass(this);
        this.locationTrackerClass.startLocationTracking();
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.selectPharmacyPresenter = new SelectPharmacyPresenterImp(this, this.netWorkingService);
        initViews();
    }

    private void initViews() {
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.tv_patience = (TextView) findViewById(C1020R.id.tv_patience);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_slider.setVisibility(4);
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.et_first_name = (EditText) findViewById(C1020R.id.et_first_name);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(C1020R.id.map)).getMapAsync(this);
        settingTitle(getResources().getString(C1020R.string.select_pharmacy));
        settingRecyclerView();
        settingClickListner();
        if (this.show_back) {
            this.img_header_back.setVisibility(0);
        } else {
            this.img_header_back.setVisibility(4);
        }
        if (SharedPreff.isFirstTimePharmacySelect()) {
            SharedPreff.saveFirstTimePharmacySelect(false);
            DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.message), getResources().getString(C1020R.string.you_will_now_be_prompted_to_select_a_pharmacy), new C10951());
        }
    }

    private void settingClickListner() {
        this.img_header_back.setOnClickListener(this);
        this.et_first_name.setOnEditorActionListener(new C10972());
        this.et_first_name.addTextChangedListener(new C10983());
    }

    public void showPatienceMessage() {
        this.tv_patience.setVisibility(0);
    }

    public void hidePatienceMessage() {
        this.tv_patience.setVisibility(8);
    }

    private void settingRecyclerView() {
        this.selectPharmacyAdapter = new SelectPharmacyAdapter(this, this.selectPharmacyPresenter);
        this.recyclerView.setAdapter(this.selectPharmacyAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void settingTitle(String name) {
        this.title_name.setText(name);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.locationTrackerClass.stopLocationTracking();
        this.selectPharmacyPresenter.unSubscribeCallbacks();
    }

    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnInfoWindowClickListener(new C10994());
        if (this.selectPharmacyAdapter != null && this.selectPharmacyAdapter.getItemCount() == 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.usLatLng, 10.0f));
        }
        googleMap.setOnCameraMoveListener(new C11005());
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error, getResources().getString(C1020R.string.ok_dialog), new C11016());
    }

    public void showToastError(String error) {
        Toast.makeText(getApplicationContext(), error, 0).show();
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void settingAdapter(ArrayList<SelectPharmacyObject> selectPharmaList) {
        if (this.selectPharmacyAdapter == null) {
            return;
        }
        if (selectPharmaList.size() > 0) {
            this.selectPharmaListMain = selectPharmaList;
            this.selectPharmacyAdapter.addingList(selectPharmaList);
            if (this.googleMap != null) {
                this.googleMap.clear();
                this.latLngs.clear();
                for (int z = 0; z < selectPharmaList.size(); z++) {
                    try {
                        double latt = Double.parseDouble(((SelectPharmacyObject) selectPharmaList.get(z)).getLat());
                        double longg = Double.parseDouble(((SelectPharmacyObject) selectPharmaList.get(z)).getLong());
                        this.latLngs.add(new LatLng(latt, longg));
                        Marker marker = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(latt, longg)).title(((SelectPharmacyObject) selectPharmaList.get(z)).getData().getStoreName()).icon(BitmapDescriptorFactory.defaultMarker(0.0f)));
                        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, longg), 15.0f));
                        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, longg), 15.0f));
                        marker.showInfoWindow();
                        marker.hideInfoWindow();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                zoomRoute(this.googleMap, this.latLngs);
                this.googleMap.setMinZoomPreference(10.0f);
                return;
            }
            return;
        }
        this.selectPharmacyAdapter.addingList(selectPharmaList);
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

    public void gotoPharmacyDetail(SelectPharmacyObject detail) {
        SharedPreff.saveSelectPharmacyObject(detail);
        startActivityForResult(new Intent(this, PharmacyDetail.class), 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 100 || resultCode != -1 || data == null) {
            return;
        }
        LoginDocData loginDocData;
        Intent signUp;
        if (SharedPreff.getLoginResponce() != null) {
            Intent returnIntent;
            if (this.show_back) {
                returnIntent = new Intent();
                if (data.hasExtra("PharmacyName")) {
                    returnIntent.putExtra("PharmacyName", data.getStringExtra("PharmacyName"));
                }
                setResult(-1, returnIntent);
                finish();
                return;
            }
            LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
            loginDocData = loginDocResponce.getData();
            loginDocData.setDrRequest(1);
            returnIntent = new Intent();
            if (data.hasExtra("PharmacyName")) {
                loginDocData.setPharmacyName(data.getStringExtra("PharmacyName"));
            } else {
                loginDocData.setPharmacyName("");
            }
            loginDocResponce.setData(loginDocData);
            loginDocResponce.setStatus(Constants.STATUS_OK);
            SharedPreff.saveLoginResponce(loginDocResponce);
            signUp = new Intent(this, HomeScreenPatient.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
        } else if (SharedPreff.getSocialLoginData() != null) {
            CreateUserPojo createUserPojo = SharedPreff.getCreateUserPojo();
            Object loginDocResponce2 = new LoginDocResponce();
            loginDocData = new LoginDocData();
            loginDocData.setDrRequest(1);
            loginDocData.setDrCode(null);
            loginDocData.setEmail(createUserPojo.getEmail());
            loginDocData.set_id(SharedPreff.getUerID());
            loginDocData.setAddress(createUserPojo.getAddress());
            loginDocData.setBio(createUserPojo.getBio());
            loginDocData.setCity(createUserPojo.getCity());
            loginDocData.setUserType(Integer.parseInt(createUserPojo.getUserType()));
            loginDocData.setAdminStatus(1);
            loginDocData.setCode(Integer.parseInt(createUserPojo.getCode()));
            loginDocData.setDeviceType(Constants.DEVICE_TYPE);
            loginDocData.setFirstName(createUserPojo.getFirstName());
            loginDocData.setLastName(createUserPojo.getLastName());
            loginDocData.setQualification(createUserPojo.getQualification());
            loginDocData.setState(createUserPojo.getState());
            loginDocData.setMobile(createUserPojo.getMobile());
            loginDocData.setGender(createUserPojo.getGender());
            loginDocData.setMediaType(createUserPojo.getMediaType());
            loginDocData.setLanguages(createUserPojo.getLanguages());
            loginDocData.setLat(SharedPreff.getLat());
            loginDocData.setLong(SharedPreff.getLng());
            loginDocData.setZipcode(createUserPojo.getZipcode());
            if (data.hasExtra("PharmacyName")) {
                loginDocData.setPharmacyName(data.getStringExtra("PharmacyName"));
            }
            loginDocData.setLoginAllready(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            loginDocData.setProfilePic(SharedPreff.getSocialLoginData().getPic_big());
            loginDocResponce2.setData(loginDocData);
            loginDocResponce2.setStatus(Constants.STATUS_OK);
            SharedPreff.saveLoginResponce(loginDocResponce2);
            Log.e("SOCIAL_LOGIn", new Gson().toJson(loginDocResponce2));
            if (createUserPojo.getUserType().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
                signUp = new Intent(this, HomeScreen.class);
                signUp.setFlags(268468224);
                startActivity(signUp);
                return;
            }
            signUp = new Intent(this, HomeScreenPatient.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
        } else {
            signUp = new Intent(this, DashBoard.class);
            signUp.setFlags(268468224);
            startActivity(signUp);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_header_back:
                finish();
                return;
            default:
                return;
        }
    }
}
