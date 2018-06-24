package com.trigma.tiktok.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {
    public int deviceHeight;
    public int deviceWidth;

    class C10281 implements AlertCallBackInterface {
        C10281() {
        }

        public void neutralClick() {
            SharedPreff.clearDocPreff();
            Intent intent = new Intent(BaseActivity.this, DashBoard.class);
            intent.setFlags(268468224);
            BaseActivity.this.startActivity(intent);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Point size = new Point();
        WindowManager w = getWindowManager();
        if (VERSION.SDK_INT >= 11) {
            w.getDefaultDisplay().getSize(size);
            this.deviceWidth = size.x;
            this.deviceHeight = size.y;
            return;
        }
        Display d = w.getDefaultDisplay();
        this.deviceWidth = d.getWidth();
        this.deviceHeight = d.getHeight();
    }

    public void staffDeactivated(Context activity) {
        DialogPopUps.alertPopUp(activity, getResources().getString(C1020R.string.your_account_has_been_deleted_or_deactivated_please_contact_our_support_for_more_information), getResources().getString(C1020R.string.ok), new C10281());
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onPause() {
        super.onPause();
        Constants.CONTEXT = null;
    }

    protected void onResume() {
        super.onResume();
        Constants.CONTEXT = this;
    }
}
