package com.google.android.gms.location;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;
import java.util.Collections;
import java.util.List;

public class zzv extends zza {
    public static final Creator<zzv> CREATOR = new zzw();
    private final PendingIntent mPendingIntent;
    private final String mTag;
    private final int mVersionCode;
    private final List<String> zzbjR;

    zzv(int i, @Nullable List<String> list, @Nullable PendingIntent pendingIntent, String str) {
        this.mVersionCode = i;
        this.zzbjR = list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
        this.mPendingIntent = pendingIntent;
        this.mTag = str;
    }

    private zzv(@Nullable List<String> list, @Nullable PendingIntent pendingIntent, String str) {
        this(1, list, pendingIntent, str);
    }

    public static zzv zzC(List<String> list) {
        zzac.zzb((Object) list, (Object) "geofence can't be null.");
        zzac.zzb(!list.isEmpty(), (Object) "Geofences must contains at least one id.");
        return new zzv(list, null, "");
    }

    public static zzv zzb(PendingIntent pendingIntent) {
        zzac.zzb((Object) pendingIntent, (Object) "PendingIntent can not be null.");
        return new zzv(null, pendingIntent, "");
    }

    public String getTag() {
        return this.mTag;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzw.zza(this, parcel, i);
    }

    public List<String> zzHy() {
        return this.zzbjR;
    }

    public PendingIntent zzuT() {
        return this.mPendingIntent;
    }
}
