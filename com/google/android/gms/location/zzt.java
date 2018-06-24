package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzt implements Creator<LocationSettingsResult> {
    static void zza(LocationSettingsResult locationSettingsResult, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zza(parcel, 1, locationSettingsResult.getStatus(), i, false);
        zzc.zza(parcel, 2, locationSettingsResult.getLocationSettingsStates(), i, false);
        zzc.zzc(parcel, 1000, locationSettingsResult.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgM(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzki(i);
    }

    public LocationSettingsResult zzgM(Parcel parcel) {
        LocationSettingsStates locationSettingsStates = null;
        int zzaU = zzb.zzaU(parcel);
        int i = 0;
        Status status = null;
        while (parcel.dataPosition() < zzaU) {
            int i2;
            LocationSettingsStates locationSettingsStates2;
            Status status2;
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    i2 = i;
                    Status status3 = (Status) zzb.zza(parcel, zzaT, Status.CREATOR);
                    locationSettingsStates2 = locationSettingsStates;
                    status2 = status3;
                    break;
                case 2:
                    locationSettingsStates2 = (LocationSettingsStates) zzb.zza(parcel, zzaT, LocationSettingsStates.CREATOR);
                    status2 = status;
                    i2 = i;
                    break;
                case 1000:
                    LocationSettingsStates locationSettingsStates3 = locationSettingsStates;
                    status2 = status;
                    i2 = zzb.zzg(parcel, zzaT);
                    locationSettingsStates2 = locationSettingsStates3;
                    break;
                default:
                    zzb.zzb(parcel, zzaT);
                    locationSettingsStates2 = locationSettingsStates;
                    status2 = status;
                    i2 = i;
                    break;
            }
            i = i2;
            status = status2;
            locationSettingsStates = locationSettingsStates2;
        }
        if (parcel.dataPosition() == zzaU) {
            return new LocationSettingsResult(i, status, locationSettingsStates);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public LocationSettingsResult[] zzki(int i) {
        return new LocationSettingsResult[i];
    }
}
