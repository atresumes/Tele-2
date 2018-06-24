package com.google.android.gms.location;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Api.zzf;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.internal.zzarl;
import com.google.android.gms.internal.zzaro;
import com.google.android.gms.internal.zzaru;
import com.google.android.gms.internal.zzasc;

public class LocationServices {
    public static final Api<NoOptions> API = new Api("LocationServices.API", zzahd, zzahc);
    public static final FusedLocationProviderApi FusedLocationApi = new zzarl();
    public static final GeofencingApi GeofencingApi = new zzaro();
    public static final SettingsApi SettingsApi = new zzasc();
    private static final zzf<zzaru> zzahc = new zzf();
    private static final com.google.android.gms.common.api.Api.zza<zzaru, NoOptions> zzahd = new C08351();

    public static abstract class zza<R extends Result> extends com.google.android.gms.internal.zzzv.zza<R, zzaru> {
        public zza(GoogleApiClient googleApiClient) {
            super(LocationServices.API, googleApiClient);
        }
    }

    class C08351 extends com.google.android.gms.common.api.Api.zza<zzaru, NoOptions> {
        C08351() {
        }

        public /* synthetic */ zze zza(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return zzq(context, looper, com_google_android_gms_common_internal_zzg, (NoOptions) obj, connectionCallbacks, onConnectionFailedListener);
        }

        public zzaru zzq(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzaru(context, looper, connectionCallbacks, onConnectionFailedListener, "locationServices", com_google_android_gms_common_internal_zzg);
        }
    }

    private LocationServices() {
    }

    public static zzaru zzj(GoogleApiClient googleApiClient) {
        boolean z = true;
        zzac.zzb(googleApiClient != null, (Object) "GoogleApiClient parameter is required.");
        zzaru com_google_android_gms_internal_zzaru = (zzaru) googleApiClient.zza(zzahc);
        if (com_google_android_gms_internal_zzaru == null) {
            z = false;
        }
        zzac.zza(z, (Object) "GoogleApiClient is not configured to use the LocationServices.API Api. Pass thisinto GoogleApiClient.Builder#addApi() to use this feature.");
        return com_google_android_gms_internal_zzaru;
    }
}
