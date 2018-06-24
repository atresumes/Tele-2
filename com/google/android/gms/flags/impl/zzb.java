package com.google.android.gms.flags.impl;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.internal.zzaps;
import java.util.concurrent.Callable;

public class zzb {
    private static SharedPreferences zzaWS = null;

    class C07031 implements Callable<SharedPreferences> {
        final /* synthetic */ Context zztd;

        C07031(Context context) {
            this.zztd = context;
        }

        public /* synthetic */ Object call() throws Exception {
            return zzCU();
        }

        public SharedPreferences zzCU() {
            return this.zztd.getSharedPreferences("google_sdk_flags", 1);
        }
    }

    public static SharedPreferences zzm(Context context) {
        SharedPreferences sharedPreferences;
        synchronized (SharedPreferences.class) {
            if (zzaWS == null) {
                zzaWS = (SharedPreferences) zzaps.zzb(new C07031(context));
            }
            sharedPreferences = zzaWS;
        }
        return sharedPreferences;
    }
}
