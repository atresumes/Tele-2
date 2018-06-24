package com.trigma.tiktok;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import com.trigma.tiktok.retrofit.NetWorkingService;
import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig.Builder;

public class TikTokApp extends MultiDexApplication {
    private static TikTokApp instance;
    private NetWorkingService netWorkingService;

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        CalligraphyConfig.initDefault(new Builder().setDefaultFontPath("fonts/OpenSans-Regular.ttf").setFontAttrId(C1020R.attr.fontPath).build());
        this.netWorkingService = new NetWorkingService((Application) this);
        Paper.init(this);
        Realm.init(this);
        instance = this;
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().name(getResources().getString(C1020R.string.app_name)).schemaVersion(0).deleteRealmIfMigrationNeeded().build());
    }

    public NetWorkingService getNetworkService() {
        return this.netWorkingService;
    }

    public static TikTokApp getInstance() {
        return instance;
    }
}
