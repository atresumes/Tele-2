package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzaa;

public final class LocationAvailability extends zza implements ReflectedParcelable {
    public static final Creator<LocationAvailability> CREATOR = new zzn();
    private final int mVersionCode;
    int zzbjs;
    int zzbjt;
    long zzbju;
    int zzbjv;

    LocationAvailability(int i, int i2, int i3, int i4, long j) {
        this.mVersionCode = i;
        this.zzbjv = i2;
        this.zzbjs = i3;
        this.zzbjt = i4;
        this.zzbju = j;
    }

    public static LocationAvailability extractLocationAvailability(Intent intent) {
        return !hasLocationAvailability(intent) ? null : (LocationAvailability) intent.getExtras().getParcelable("com.google.android.gms.location.EXTRA_LOCATION_AVAILABILITY");
    }

    public static boolean hasLocationAvailability(Intent intent) {
        return intent == null ? false : intent.hasExtra("com.google.android.gms.location.EXTRA_LOCATION_AVAILABILITY");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LocationAvailability)) {
            return false;
        }
        LocationAvailability locationAvailability = (LocationAvailability) obj;
        return this.zzbjv == locationAvailability.zzbjv && this.zzbjs == locationAvailability.zzbjs && this.zzbjt == locationAvailability.zzbjt && this.zzbju == locationAvailability.zzbju;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        return zzaa.hashCode(Integer.valueOf(this.zzbjv), Integer.valueOf(this.zzbjs), Integer.valueOf(this.zzbjt), Long.valueOf(this.zzbju));
    }

    public boolean isLocationAvailable() {
        return this.zzbjv < 1000;
    }

    public String toString() {
        return "LocationAvailability[isLocationAvailable: " + isLocationAvailable() + "]";
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzn.zza(this, parcel, i);
    }
}
