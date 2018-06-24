package com.google.android.gms.location;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import java.util.List;

public class zzp implements Creator<LocationResult> {
    static void zza(LocationResult locationResult, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzc(parcel, 1, locationResult.getLocations(), false);
        zzc.zzc(parcel, 1000, locationResult.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgJ(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzkf(i);
    }

    public LocationResult zzgJ(Parcel parcel) {
        int zzaU = zzb.zzaU(parcel);
        int i = 0;
        List list = LocationResult.zzbjB;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    list = zzb.zzc(parcel, zzaT, Location.CREATOR);
                    break;
                case 1000:
                    i = zzb.zzg(parcel, zzaT);
                    break;
                default:
                    zzb.zzb(parcel, zzaT);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaU) {
            return new LocationResult(i, list);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public LocationResult[] zzkf(int i) {
        return new LocationResult[i];
    }
}
