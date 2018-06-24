package com.trigma.tiktok.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.TikTokApp;
import com.trigma.tiktok.adapter.NavigationAdapter;
import com.trigma.tiktok.adapter.StaffRightSideNavigation;
import com.trigma.tiktok.fragments.BaseFragment;
import com.trigma.tiktok.fragments.ContactUs;
import com.trigma.tiktok.fragments.CreateSchedule;
import com.trigma.tiktok.fragments.HomeFragment;
import com.trigma.tiktok.fragments.MessagesScreen;
import com.trigma.tiktok.fragments.MyAppointments;
import com.trigma.tiktok.fragments.MyPatients;
import com.trigma.tiktok.fragments.MyProfile;
import com.trigma.tiktok.fragments.MyProfile.ProfileImageSetting;
import com.trigma.tiktok.fragments.Notification;
import com.trigma.tiktok.fragments.Prescriptions;
import com.trigma.tiktok.fragments.StaffEditProfile;
import com.trigma.tiktok.presenter.MainActivityPresenter;
import com.trigma.tiktok.presenter.MainActivityPresenterImp;
import com.trigma.tiktok.presenter.MainActivityView;
import com.trigma.tiktok.retrofit.NetWorkingService;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.LocationTrackerClass;
import com.trigma.tiktok.utils.SharedPreff;
import java.io.File;

public class MainActivity extends BaseActivity implements OnClickListener, MainActivityView, ProfileImageSetting {
    public static DrawerLayout drawerLayout;
    BaseFragment currentScreen;
    ActionBarDrawerToggle drawerToggle;
    public LocationTrackerClass locationTrackerClass;
    public MainActivityPresenter mainActivityPresenter;
    TypedArray navIcons;
    TypedArray navStaffIcons;
    String[] navStaffTitles;
    String[] navTitles;
    public NetWorkingService netWorkingService;
    RecyclerView recyclerView;
    NavigationAdapter recyclerViewAdapter;
    StaffRightSideNavigation staffRightSideNavigation;
    int whichFragment = 2;

    class C10551 implements OnBackStackChangedListener {
        C10551() {
        }

        public void onBackStackChanged() {
            MainActivity.this.currentScreen = (BaseFragment) MainActivity.this.getSupportFragmentManager().findFragmentById(C1020R.id.containerView);
        }
    }

    class C10562 implements AlertCallBackWithButtonsInterface {
        C10562() {
        }

        public void positiveClick() {
            MainActivity.this.mainActivityPresenter.logout();
        }

        public void neutralClick() {
        }

        public void negativeClick() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.activity_main);
        this.locationTrackerClass = new LocationTrackerClass(this);
        this.locationTrackerClass.startLocationTracking();
        if (getIntent().hasExtra(Constants.WHICH_FRAGMENT)) {
            this.whichFragment = getIntent().getIntExtra(Constants.WHICH_FRAGMENT, 2);
        }
        this.netWorkingService = ((TikTokApp) getApplication()).getNetworkService();
        this.mainActivityPresenter = new MainActivityPresenterImp(this, this.netWorkingService);
        initUi();
        implementingClickListner();
        if (this.currentScreen == null) {
            this.currentScreen = getFragment(this.whichFragment);
        }
        getSupportFragmentManager().beginTransaction().replace(C1020R.id.containerView, this.currentScreen).commit();
        onTransition(null, this.currentScreen);
        getSupportFragmentManager().addOnBackStackChangedListener(new C10551());
    }

    private BaseFragment getFragment(int whichFragment) {
        switch (whichFragment) {
            case 1:
                return new ContactUs();
            case 3:
                return new CreateSchedule();
            case 4:
                return new MyPatients();
            case 5:
                return new MyAppointments();
            case 6:
                return new MyProfile();
            case 7:
                return new Notification();
            case 11:
                return new Prescriptions();
            case 12:
                return new MessagesScreen();
            case 13:
                return new StaffEditProfile();
            default:
                return new HomeFragment();
        }
    }

    private void implementingClickListner() {
    }

    private void initUi() {
        this.recyclerView = (RecyclerView) findViewById(C1020R.id.recyclerView);
        drawerLayout = (DrawerLayout) findViewById(C1020R.id.drawerMainActivity);
        this.navTitles = getResources().getStringArray(C1020R.array.navDrawerItems);
        this.navIcons = getResources().obtainTypedArray(C1020R.array.navDrawerIcons);
        this.navStaffTitles = getResources().getStringArray(C1020R.array.navDrawerStaffItems);
        this.navStaffIcons = getResources().obtainTypedArray(C1020R.array.navDrawerStaffIcons);
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.staffRightSideNavigation = new StaffRightSideNavigation(this.navStaffTitles, this.navStaffIcons, this, null, this.mainActivityPresenter);
            this.recyclerView.setAdapter(this.staffRightSideNavigation);
        } else {
            this.recyclerViewAdapter = new NavigationAdapter(this.navTitles, this.navIcons, this, null, this.mainActivityPresenter);
            this.recyclerView.setAdapter(this.recyclerViewAdapter);
        }
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.getLayoutParams().width = (int) (((double) this.deviceWidth) / 1.2d);
        this.recyclerView.requestLayout();
        setupDrawerToggle();
    }

    protected void onTransition(BaseFragment oldFragment, BaseFragment newFragment) {
        this.currentScreen = newFragment;
    }

    void setupDrawerToggle() {
        this.drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, C1020R.string.app_name, C1020R.string.app_name);
        this.drawerToggle.syncState();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C1020R.id.img_slider:
                drawerLayout.openDrawer(5);
                return;
            default:
                return;
        }
    }

    public static void openDrawer() {
        drawerLayout.openDrawer(5);
    }

    public int changeFragment(BaseFragment newFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, 0).replace(C1020R.id.containerView, newFragment, newFragment.getClass().getSimpleName());
        int id = ft.commit();
        onTransition(this.currentScreen, newFragment);
        return id;
    }

    public void showMessage(String Message) {
    }

    public void showProgressDialog() {
        DialogPopUps.showProgressDialog(this, getResources().getString(C1020R.string.please_wait));
    }

    public void showError(String error) {
        DialogPopUps.alertPopUp(this, error);
    }

    public void showToastError(String error) {
    }

    public void hideDialog() {
        DialogPopUps.hideDialog();
    }

    public void loginSuccessfull() {
        Intent intent = new Intent(this, DashBoard.class);
        intent.setFlags(268468224);
        startActivity(intent);
    }

    public void logoutClicked() {
        DialogPopUps.showAlertWithButtons(this, getResources().getString(C1020R.string.alert), getResources().getString(C1020R.string.are_you_sure_you_want_to_logout), getResources().getString(C1020R.string.yes_dialog), getResources().getString(C1020R.string.no_dialog), "", false, true, new C10562());
    }

    public void gotoPrescriptionScreen(String url, int count) {
        Constants.PRESCRIPTION_COUNT = count;
        Constants.PRESCRIPTION_URL = url;
        this.whichFragment = 11;
        this.currentScreen = getFragment(this.whichFragment);
        getSupportFragmentManager().beginTransaction().replace(C1020R.id.containerView, this.currentScreen).commit();
        onTransition(null, this.currentScreen);
    }

    public void isDoctorActive(boolean v) {
    }

    public void settingImage(File file) {
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.staffRightSideNavigation = new StaffRightSideNavigation(this.navStaffTitles, this.navStaffIcons, this, null, this.mainActivityPresenter);
            this.recyclerView.setAdapter(this.staffRightSideNavigation);
            this.staffRightSideNavigation.notifyDataSetChanged();
            return;
        }
        this.recyclerViewAdapter = new NavigationAdapter(this.navTitles, this.navIcons, this, file, this.mainActivityPresenter);
        this.recyclerView.setAdapter(this.recyclerViewAdapter);
        this.recyclerViewAdapter.notifyDataSetChanged();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.locationTrackerClass.stopLocationTracking();
    }
}
