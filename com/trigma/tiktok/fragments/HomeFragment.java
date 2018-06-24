package com.trigma.tiktok.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trigma.tiktok.C1020R;

public class HomeFragment extends BaseFragment {
    TextView mGreeting;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(C1020R.layout.fragment_home, container, false);
        this.mGreeting = (TextView) screen.findViewById(C1020R.id.home_text_greeting);
        return screen;
    }

    public void onStart() {
        super.onStart();
    }
}
