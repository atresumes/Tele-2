package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class LocationSettingsRequest extends zza {
    public static final Creator<LocationSettingsRequest> CREATOR = new zzs();
    private final int mVersionCode;
    private final List<LocationRequest> zzaVS;
    private final boolean zzbjG;
    private final boolean zzbjH;
    private zzq zzbjI;

    public static final class Builder {
        private boolean zzbjG = false;
        private boolean zzbjH = false;
        private zzq zzbjI = null;
        private final ArrayList<LocationRequest> zzbjJ = new ArrayList();

        public Builder addAllLocationRequests(Collection<LocationRequest> collection) {
            this.zzbjJ.addAll(collection);
            return this;
        }

        public Builder addLocationRequest(LocationRequest locationRequest) {
            this.zzbjJ.add(locationRequest);
            return this;
        }

        public LocationSettingsRequest build() {
            return new LocationSettingsRequest(this.zzbjJ, this.zzbjG, this.zzbjH, null);
        }

        public Builder setAlwaysShow(boolean z) {
            this.zzbjG = z;
            return this;
        }

        public Builder setNeedBle(boolean z) {
            this.zzbjH = z;
            return this;
        }
    }

    LocationSettingsRequest(int i, List<LocationRequest> list, boolean z, boolean z2, zzq com_google_android_gms_location_zzq) {
        this.mVersionCode = i;
        this.zzaVS = list;
        this.zzbjG = z;
        this.zzbjH = z2;
        this.zzbjI = com_google_android_gms_location_zzq;
    }

    private LocationSettingsRequest(List<LocationRequest> list, boolean z, boolean z2, @Nullable zzq com_google_android_gms_location_zzq) {
        this(3, (List) list, z, z2, com_google_android_gms_location_zzq);
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzs.zza(this, parcel, i);
    }

    public List<LocationRequest> zzCr() {
        return Collections.unmodifiableList(this.zzaVS);
    }

    public boolean zzHv() {
        return this.zzbjG;
    }

    public boolean zzHw() {
        return this.zzbjH;
    }

    @Nullable
    public zzq zzHx() {
        return this.zzbjI;
    }
}
