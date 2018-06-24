package com.trigma.tiktok.fragments.patient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.BaseActivity;
import com.trigma.tiktok.adapter.patient.SearchFragmentPagerAdapter;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.LocationTrackerClass;
import com.trigma.tiktok.utils.SharedPreff;

public class DoctorSearch extends BaseActivity {
    public static int pagerSelectedPosition = 0;
    public static PagerSlidingTabStrip tabsStrip;
    public static ViewPager viewPager;
    private ImageView img_header_back;
    private ImageView img_slider;
    public LocationTrackerClass locationTrackerClass;
    private LinearLayout mTabsLinearLayout;
    public SearchFragmentPagerAdapter searchFragmentPagerAdapter;
    private boolean show_back;
    private TextView title_name;

    class C11811 implements OnClickListener {
        C11811() {
        }

        public void onClick(View v) {
            DoctorSearch.this.finish();
        }
    }

    class C11822 implements AlertCallBackInterface {
        C11822() {
        }

        public void neutralClick() {
        }
    }

    class C11833 implements OnPageChangeListener {
        C11833() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            CommonUtils.hideKeyboard(DoctorSearch.this.title_name, DoctorSearch.this);
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.doctor_search);
        if (getIntent().hasExtra(Constants.SHOW_BACK)) {
            this.show_back = getIntent().getBooleanExtra(Constants.SHOW_BACK, false);
        }
        this.locationTrackerClass = new LocationTrackerClass(this);
        this.locationTrackerClass.startLocationTracking();
        initViews();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(C1020R.id.viewpager);
        this.searchFragmentPagerAdapter = new SearchFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(this.searchFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabsStrip = (PagerSlidingTabStrip) findViewById(C1020R.id.tabs);
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setTextColor(ContextCompat.getColor(this, C1020R.color.white));
        tabsStrip.setIndicatorColor(ContextCompat.getColor(this, C1020R.color.white));
        if (CommonUtils.getWindowDimension(this, true) == 1080 && CommonUtils.getWindowDimension(this, false) == 1920) {
            tabsStrip.setTextSize(CommonUtils.dpToPx(14, this));
        } else {
            tabsStrip.setTextSize(CommonUtils.dpToPx(16, this));
        }
        tabsStrip.setTypeface(CommonUtils.opensansLightFont(this), 0);
        this.mTabsLinearLayout = (LinearLayout) tabsStrip.getChildAt(0);
        ((TextView) this.mTabsLinearLayout.getChildAt(0)).setTextColor(ContextCompat.getColor(this, C1020R.color.white));
        this.title_name = (TextView) findViewById(C1020R.id.title_name);
        this.img_slider = (ImageView) findViewById(C1020R.id.img_slider);
        this.img_slider.setVisibility(4);
        this.img_header_back = (ImageView) findViewById(C1020R.id.img_header_back);
        this.img_header_back.setOnClickListener(new C11811());
        settingTitle(getResources().getString(C1020R.string.doctor_search));
        if (this.show_back) {
            this.img_header_back.setVisibility(0);
        } else {
            this.img_header_back.setVisibility(4);
        }
        if (SharedPreff.isFirstTimeDoctorSearch()) {
            SharedPreff.saveFirstTimeDoctorSearch(false);
            DialogPopUps.confirmationPopUp(this, getResources().getString(C1020R.string.message), getResources().getString(C1020R.string.you_will_be_now_be_prompted_to_select_a_doctor), new C11822());
        }
        viewPager.addOnPageChangeListener(new C11833());
    }

    public void settingTitle(String name) {
        this.title_name.setText(name);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.locationTrackerClass.stopLocationTracking();
    }
}
