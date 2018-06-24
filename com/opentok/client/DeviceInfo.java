package com.opentok.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import java.util.UUID;

public class DeviceInfo {
    private Context context;

    public DeviceInfo(Context applicationContext) {
        this.context = applicationContext;
    }

    public Context getApplicationContext() {
        return this.context;
    }

    public void setApplicationContext(Context context) {
        this.context = context;
    }

    public String getApplicationIdentifier() {
        return this.context.getPackageName();
    }

    public String getApplicationVersion() {
        try {
            return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "unknown";
        }
    }

    public String getSystemVersion() {
        return Integer.toString(VERSION.SDK_INT);
    }

    public String getSystemName() {
        return "Android OS";
    }

    public String getDeviceModel() {
        return String.format("mfr=%s,model=%s,abi=%s", new Object[]{Build.MANUFACTURER, Build.MODEL, Build.CPU_ABI});
    }

    public String getSdkVersion() {
        return String.format(".%s-android", new Object[]{"e0064a9d1509d474ce90b86000204feaab6bfd1a".substring(0, 8)});
    }

    public String getCarrierName() {
        return Build.BRAND;
    }

    public String getNetworkStatus() {
        if (VERSION.SDK_INT >= 14) {
            return Build.getRadioVersion();
        }
        return Build.RADIO;
    }

    public String getOpenTokDeviceIdentifier() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("opentok", 0);
        if (prefs.getString("guid", null) == null) {
            prefs.edit().putString("guid", UUID.randomUUID().toString()).apply();
        }
        return prefs.getString("guid", null);
    }
}
