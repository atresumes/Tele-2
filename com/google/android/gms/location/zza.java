package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.WorkSource;
import android.support.annotation.Nullable;

public class zza extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Creator<zza> CREATOR = new zzb();
    @Nullable
    private String mTag;
    private final int mVersionCode;
    @Nullable
    private String zzaht;
    private long zzbiH;
    private boolean zzbiI;
    @Nullable
    private WorkSource zzbiJ;
    @Nullable
    private int[] zzbiK;
    @Nullable
    private boolean zzbiL;
    private final long zzbiM;

    zza(int i, long j, boolean z, @Nullable WorkSource workSource, @Nullable String str, @Nullable int[] iArr, boolean z2, @Nullable String str2, long j2) {
        this.mVersionCode = i;
        this.zzbiH = j;
        this.zzbiI = z;
        this.zzbiJ = workSource;
        this.mTag = str;
        this.zzbiK = iArr;
        this.zzbiL = z2;
        this.zzaht = str2;
        this.zzbiM = j2;
    }

    @Nullable
    public String getAccountName() {
        return this.zzaht;
    }

    public long getIntervalMillis() {
        return this.zzbiH;
    }

    @Nullable
    public String getTag() {
        return this.mTag;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzb.zza(this, parcel, i);
    }

    public boolean zzHi() {
        return this.zzbiI;
    }

    @Nullable
    public WorkSource zzHj() {
        return this.zzbiJ;
    }

    @Nullable
    public int[] zzHk() {
        return this.zzbiK;
    }

    public boolean zzHl() {
        return this.zzbiL;
    }

    public long zzHm() {
        return this.zzbiM;
    }
}
