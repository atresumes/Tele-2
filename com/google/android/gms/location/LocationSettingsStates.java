package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzd;

public final class LocationSettingsStates extends zza {
    public static final Creator<LocationSettingsStates> CREATOR = new zzu();
    private final int mVersionCode;
    private final boolean zzbjL;
    private final boolean zzbjM;
    private final boolean zzbjN;
    private final boolean zzbjO;
    private final boolean zzbjP;
    private final boolean zzbjQ;

    LocationSettingsStates(int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        this.mVersionCode = i;
        this.zzbjL = z;
        this.zzbjM = z2;
        this.zzbjN = z3;
        this.zzbjO = z4;
        this.zzbjP = z5;
        this.zzbjQ = z6;
    }

    public static LocationSettingsStates fromIntent(Intent intent) {
        return (LocationSettingsStates) zzd.zza(intent, "com.google.android.gms.location.LOCATION_SETTINGS_STATES", CREATOR);
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public boolean isBlePresent() {
        return this.zzbjQ;
    }

    public boolean isBleUsable() {
        return this.zzbjN;
    }

    public boolean isGpsPresent() {
        return this.zzbjO;
    }

    public boolean isGpsUsable() {
        return this.zzbjL;
    }

    public boolean isLocationPresent() {
        return this.zzbjO || this.zzbjP;
    }

    public boolean isLocationUsable() {
        return this.zzbjL || this.zzbjM;
    }

    public boolean isNetworkLocationPresent() {
        return this.zzbjP;
    }

    public boolean isNetworkLocationUsable() {
        return this.zzbjM;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzu.zza(this, parcel, i);
    }
}
