package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.zza;

public final class LocationSettingsResult extends zza implements Result {
    public static final Creator<LocationSettingsResult> CREATOR = new zzt();
    private final int mVersionCode;
    private final Status zzahq;
    private final LocationSettingsStates zzbjK;

    LocationSettingsResult(int i, Status status, LocationSettingsStates locationSettingsStates) {
        this.mVersionCode = i;
        this.zzahq = status;
        this.zzbjK = locationSettingsStates;
    }

    public LocationSettingsResult(Status status) {
        this(1, status, null);
    }

    public LocationSettingsStates getLocationSettingsStates() {
        return this.zzbjK;
    }

    public Status getStatus() {
        return this.zzahq;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzt.zza(this, parcel, i);
    }
}
