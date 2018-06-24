package com.trigma.tiktok.adapter.patient;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.DoctorFragment;
import com.trigma.tiktok.fragments.patient.NameZipFragment;
import com.trigma.tiktok.fragments.patient.NearMeFragement;

public class SearchFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;

    public SearchFragmentPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.tabTitles = c.getResources().getStringArray(C1020R.array.tabsArray);
    }

    public int getCount() {
        return this.tabTitles.length;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DoctorFragment.newInstance(position + 1);
            case 1:
                return NameZipFragment.newInstance(position + 1);
            case 2:
                return NearMeFragement.newInstance(position + 1);
            default:
                return null;
        }
    }

    public CharSequence getPageTitle(int position) {
        return this.tabTitles[position];
    }
}
