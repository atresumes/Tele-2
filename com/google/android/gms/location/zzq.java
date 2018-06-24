package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;

public final class zzq extends zza {
    public static final Creator<zzq> CREATOR = new zzr();
    private final int mVersionCode;
    private final String zzbjD;
    private final String zzbjE;
    private final int zzbjF;

    zzq(int i, String str, String str2, int i2) {
        this.mVersionCode = i;
        this.zzbjD = str;
        this.zzbjE = str2;
        this.zzbjF = i2;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzr.zza(this, parcel, i);
    }

    public String zzHs() {
        return this.zzbjD;
    }

    public String zzHt() {
        return this.zzbjE;
    }

    public int zzHu() {
        return this.zzbjF;
    }
}
