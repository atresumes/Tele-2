package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzn implements Creator<LocationAvailability> {
    static void zza(LocationAvailability locationAvailability, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzc(parcel, 1, locationAvailability.zzbjs);
        zzc.zzc(parcel, 2, locationAvailability.zzbjt);
        zzc.zza(parcel, 3, locationAvailability.zzbju);
        zzc.zzc(parcel, 4, locationAvailability.zzbjv);
        zzc.zzc(parcel, 1000, locationAvailability.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgH(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzkb(i);
    }

    public LocationAvailability zzgH(Parcel parcel) {
        int i = 1;
        int zzaU = zzb.zzaU(parcel);
        int i2 = 0;
        int i3 = 1000;
        long j = 0;
        int i4 = 1;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    i4 = zzb.zzg(parcel, zzaT);
                    break;
                case 2:
                    i = zzb.zzg(parcel, zzaT);
                    break;
                case 3:
                    j = zzb.zzi(parcel, zzaT);
                    break;
                case 4:
                    i3 = zzb.zzg(parcel, zzaT);
                    break;
                case 1000:
                    i2 = zzb.zzg(parcel, zzaT);
                    break;
                default:
                    zzb.zzb(parcel, zzaT);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaU) {
            return new LocationAvailability(i2, i3, i4, i, j);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public LocationAvailability[] zzkb(int i) {
        return new LocationAvailability[i];
    }
}
