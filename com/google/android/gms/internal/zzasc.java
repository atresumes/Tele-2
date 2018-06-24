package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzzv.zzb;
import com.google.android.gms.location.LocationServices.zza;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsApi;

public class zzasc implements SettingsApi {
    public PendingResult<LocationSettingsResult> checkLocationSettings(GoogleApiClient googleApiClient, LocationSettingsRequest locationSettingsRequest) {
        return zza(googleApiClient, locationSettingsRequest, null);
    }

    public PendingResult<LocationSettingsResult> zza(GoogleApiClient googleApiClient, final LocationSettingsRequest locationSettingsRequest, final String str) {
        return googleApiClient.zza(new zza<LocationSettingsResult>(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(locationSettingsRequest, (zzb) this, str);
            }

            public LocationSettingsResult zzbo(Status status) {
                return new LocationSettingsResult(status);
            }

            public /* synthetic */ Result zzc(Status status) {
                return zzbo(status);
            }
        });
    }
}
