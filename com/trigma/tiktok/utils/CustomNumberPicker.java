package com.trigma.tiktok.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

public class CustomNumberPicker extends NumberPicker {
    public CustomNumberPicker(Context context) {
        super(context);
    }

    public void addView(View child) {
        super.addView(child);
        if (child instanceof EditText) {
            ((EditText) child).setTextSize(16.0f);
        }
    }
}
