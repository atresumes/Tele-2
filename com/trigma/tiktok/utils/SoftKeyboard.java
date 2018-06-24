package com.trigma.tiktok.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoftKeyboard implements OnFocusChangeListener {
    private static final int CLEAR_FOCUS = 0;
    private int[] coords;
    private List<EditText> editTextList;
    private InputMethodManager im;
    private boolean isKeyboardShow;
    private ViewGroup layout;
    private int layoutBottom;
    private final Handler mHandler = new C13891();
    private SoftKeyboardChangesThread softKeyboardThread;
    private View tempView;

    class C13891 extends Handler {
        C13891() {
        }

        public void handleMessage(Message m) {
            switch (m.what) {
                case 0:
                    if (SoftKeyboard.this.tempView != null) {
                        SoftKeyboard.this.tempView.clearFocus();
                        SoftKeyboard.this.tempView = null;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public interface SoftKeyboardChanged {
        void onSoftKeyboardHide();

        void onSoftKeyboardShow();
    }

    private class SoftKeyboardChangesThread extends Thread {
        private SoftKeyboardChanged mCallback;
        private AtomicBoolean started = new AtomicBoolean(true);

        public void setCallback(SoftKeyboardChanged mCallback) {
            this.mCallback = mCallback;
        }

        public void run() {
            while (this.started.get()) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int currentBottomLocation = SoftKeyboard.this.getLayoutCoordinates();
                while (currentBottomLocation == SoftKeyboard.this.layoutBottom && this.started.get()) {
                    currentBottomLocation = SoftKeyboard.this.getLayoutCoordinates();
                }
                if (this.started.get()) {
                    this.mCallback.onSoftKeyboardShow();
                }
                while (currentBottomLocation >= SoftKeyboard.this.layoutBottom && this.started.get()) {
                    currentBottomLocation = SoftKeyboard.this.getLayoutCoordinates();
                }
                while (currentBottomLocation != SoftKeyboard.this.layoutBottom && this.started.get()) {
                    synchronized (this) {
                        try {
                            wait(500);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                    currentBottomLocation = SoftKeyboard.this.getLayoutCoordinates();
                }
                if (this.started.get()) {
                    this.mCallback.onSoftKeyboardHide();
                }
                if (SoftKeyboard.this.isKeyboardShow && this.started.get()) {
                    SoftKeyboard.this.isKeyboardShow = false;
                }
                if (this.started.get()) {
                    SoftKeyboard.this.mHandler.obtainMessage(0).sendToTarget();
                }
            }
        }

        public void keyboardOpened() {
            synchronized (this) {
                notify();
            }
        }

        public void stopThread() {
            synchronized (this) {
                this.started.set(false);
                notify();
            }
        }
    }

    public SoftKeyboard(ViewGroup layout, InputMethodManager im) {
        this.layout = layout;
        keyboardHideByDefault();
        initEditTexts(layout);
        this.im = im;
        this.coords = new int[2];
        this.isKeyboardShow = false;
        this.softKeyboardThread = new SoftKeyboardChangesThread();
        this.softKeyboardThread.start();
    }

    public void openSoftKeyboard() {
        if (!this.isKeyboardShow) {
            this.layoutBottom = getLayoutCoordinates();
            this.im.toggleSoftInput(0, 1);
            this.softKeyboardThread.keyboardOpened();
            this.isKeyboardShow = true;
        }
    }

    public void closeSoftKeyboard() {
        if (this.isKeyboardShow) {
            this.im.toggleSoftInput(1, 0);
            this.isKeyboardShow = false;
        }
    }

    public void setSoftKeyboardCallback(SoftKeyboardChanged mCallback) {
        this.softKeyboardThread.setCallback(mCallback);
    }

    public void unRegisterSoftKeyboardCallback() {
        this.softKeyboardThread.stopThread();
    }

    private int getLayoutCoordinates() {
        this.layout.getLocationOnScreen(this.coords);
        return this.coords[1] + this.layout.getHeight();
    }

    private void keyboardHideByDefault() {
        this.layout.setFocusable(true);
        this.layout.setFocusableInTouchMode(true);
    }

    private void initEditTexts(ViewGroup viewgroup) {
        if (this.editTextList == null) {
            this.editTextList = new ArrayList();
        }
        int childCount = viewgroup.getChildCount();
        for (int i = 0; i <= childCount - 1; i++) {
            View v = viewgroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                initEditTexts((ViewGroup) v);
            }
            if (v instanceof EditText) {
                EditText editText = (EditText) v;
                editText.setOnFocusChangeListener(this);
                editText.setCursorVisible(true);
                this.editTextList.add(editText);
            }
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            this.tempView = v;
            if (!this.isKeyboardShow) {
                this.layoutBottom = getLayoutCoordinates();
                this.softKeyboardThread.keyboardOpened();
                this.isKeyboardShow = true;
            }
        }
    }
}
