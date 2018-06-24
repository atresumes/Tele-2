package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzaa;

public final class LocationRequest extends zza implements ReflectedParcelable {
    public static final Creator<LocationRequest> CREATOR = new zzo();
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    int mPriority;
    private final int mVersionCode;
    boolean zzaVX;
    long zzbjA;
    long zzbjb;
    long zzbjw;
    long zzbjx;
    int zzbjy;
    float zzbjz;

    public LocationRequest() {
        this.mVersionCode = 1;
        this.mPriority = 102;
        this.zzbjw = 3600000;
        this.zzbjx = 600000;
        this.zzaVX = false;
        this.zzbjb = Long.MAX_VALUE;
        this.zzbjy = Integer.MAX_VALUE;
        this.zzbjz = 0.0f;
        this.zzbjA = 0;
    }

    LocationRequest(int i, int i2, long j, long j2, boolean z, long j3, int i3, float f, long j4) {
        this.mVersionCode = i;
        this.mPriority = i2;
        this.zzbjw = j;
        this.zzbjx = j2;
        this.zzaVX = z;
        this.zzbjb = j3;
        this.zzbjy = i3;
        this.zzbjz = f;
        this.zzbjA = j4;
    }

    public static LocationRequest create() {
        return new LocationRequest();
    }

    private static void zzU(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("invalid interval: " + j);
        }
    }

    private static void zze(float f) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("invalid displacement: " + f);
        }
    }

    private static void zzkc(int i) {
        switch (i) {
            case 100:
            case 102:
            case 104:
            case 105:
                return;
            default:
                throw new IllegalArgumentException("invalid quality: " + i);
        }
    }

    public static String zzkd(int i) {
        switch (i) {
            case 100:
                return "PRIORITY_HIGH_ACCURACY";
            case 102:
                return "PRIORITY_BALANCED_POWER_ACCURACY";
            case 104:
                return "PRIORITY_LOW_POWER";
            case 105:
                return "PRIORITY_NO_POWER";
            default:
                return "???";
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocationRequest)) {
            return false;
        }
        LocationRequest locationRequest = (LocationRequest) obj;
        return this.mPriority == locationRequest.mPriority && this.zzbjw == locationRequest.zzbjw && this.zzbjx == locationRequest.zzbjx && this.zzaVX == locationRequest.zzaVX && this.zzbjb == locationRequest.zzbjb && this.zzbjy == locationRequest.zzbjy && this.zzbjz == locationRequest.zzbjz;
    }

    public long getExpirationTime() {
        return this.zzbjb;
    }

    public long getFastestInterval() {
        return this.zzbjx;
    }

    public long getInterval() {
        return this.zzbjw;
    }

    public long getMaxWaitTime() {
        long j = this.zzbjA;
        return j < this.zzbjw ? this.zzbjw : j;
    }

    public int getNumUpdates() {
        return this.zzbjy;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public float getSmallestDisplacement() {
        return this.zzbjz;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        return zzaa.hashCode(Integer.valueOf(this.mPriority), Long.valueOf(this.zzbjw), Long.valueOf(this.zzbjx), Boolean.valueOf(this.zzaVX), Long.valueOf(this.zzbjb), Integer.valueOf(this.zzbjy), Float.valueOf(this.zzbjz));
    }

    public LocationRequest setExpirationDuration(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (j > Long.MAX_VALUE - elapsedRealtime) {
            this.zzbjb = Long.MAX_VALUE;
        } else {
            this.zzbjb = elapsedRealtime + j;
        }
        if (this.zzbjb < 0) {
            this.zzbjb = 0;
        }
        return this;
    }

    public LocationRequest setExpirationTime(long j) {
        this.zzbjb = j;
        if (this.zzbjb < 0) {
            this.zzbjb = 0;
        }
        return this;
    }

    public LocationRequest setFastestInterval(long j) {
        zzU(j);
        this.zzaVX = true;
        this.zzbjx = j;
        return this;
    }

    public LocationRequest setInterval(long j) {
        zzU(j);
        this.zzbjw = j;
        if (!this.zzaVX) {
            this.zzbjx = (long) (((double) this.zzbjw) / 6.0d);
        }
        return this;
    }

    public LocationRequest setMaxWaitTime(long j) {
        zzU(j);
        this.zzbjA = j;
        return this;
    }

    public LocationRequest setNumUpdates(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("invalid numUpdates: " + i);
        }
        this.zzbjy = i;
        return this;
    }

    public LocationRequest setPriority(int i) {
        zzkc(i);
        this.mPriority = i;
        return this;
    }

    public LocationRequest setSmallestDisplacement(float f) {
        zze(f);
        this.zzbjz = f;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request[").append(zzkd(this.mPriority));
        if (this.mPriority != 105) {
            stringBuilder.append(" requested=");
            stringBuilder.append(this.zzbjw).append("ms");
        }
        stringBuilder.append(" fastest=");
        stringBuilder.append(this.zzbjx).append("ms");
        if (this.zzbjA > this.zzbjw) {
            stringBuilder.append(" maxWait=");
            stringBuilder.append(this.zzbjA).append("ms");
        }
        if (this.zzbjb != Long.MAX_VALUE) {
            long elapsedRealtime = this.zzbjb - SystemClock.elapsedRealtime();
            stringBuilder.append(" expireIn=");
            stringBuilder.append(elapsedRealtime).append("ms");
        }
        if (this.zzbjy != Integer.MAX_VALUE) {
            stringBuilder.append(" num=").append(this.zzbjy);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzo.zza(this, parcel, i);
    }
}
