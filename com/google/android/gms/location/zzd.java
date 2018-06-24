package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzaa;

public class zzd extends zza {
    public static final Creator<zzd> CREATOR = new zze();
    private final int mVersionCode;
    private final int zzaQW;
    private final int zzbiR;

    zzd(int i, int i2, int i3) {
        this.mVersionCode = i;
        this.zzaQW = i2;
        this.zzbiR = i3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzd)) {
            return false;
        }
        zzd com_google_android_gms_location_zzd = (zzd) obj;
        return this.zzaQW == com_google_android_gms_location_zzd.zzaQW && this.zzbiR == com_google_android_gms_location_zzd.zzbiR;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        return zzaa.hashCode(Integer.valueOf(this.zzaQW), Integer.valueOf(this.zzbiR));
    }

    public String toString() {
        int i = this.zzaQW;
        return "ActivityTransition [mActivityType=" + i + ", mTransitionType=" + this.zzbiR + "]";
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }

    public int zzBg() {
        return this.zzaQW;
    }

    public int zzHn() {
        return this.zzbiR;
    }
}
