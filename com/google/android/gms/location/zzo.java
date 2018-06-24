package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzo implements Creator<LocationRequest> {
    static void zza(LocationRequest locationRequest, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzc(parcel, 1, locationRequest.mPriority);
        zzc.zza(parcel, 2, locationRequest.zzbjw);
        zzc.zza(parcel, 3, locationRequest.zzbjx);
        zzc.zza(parcel, 4, locationRequest.zzaVX);
        zzc.zza(parcel, 5, locationRequest.zzbjb);
        zzc.zzc(parcel, 6, locationRequest.zzbjy);
        zzc.zza(parcel, 7, locationRequest.zzbjz);
        zzc.zzc(parcel, 1000, locationRequest.getVersionCode());
        zzc.zza(parcel, 8, locationRequest.zzbjA);
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgI(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzke(i);
    }

    public LocationRequest zzgI(Parcel parcel) {
        int zzaU = zzb.zzaU(parcel);
        int i = 0;
        int i2 = 102;
        long j = 3600000;
        long j2 = 600000;
        boolean z = false;
        long j3 = Long.MAX_VALUE;
        int i3 = Integer.MAX_VALUE;
        float f = 0.0f;
        long j4 = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    i2 = zzb.zzg(parcel, zzaT);
                    break;
                case 2:
                    j = zzb.zzi(parcel, zzaT);
                    break;
                case 3:
                    j2 = zzb.zzi(parcel, zzaT);
                    break;
                case 4:
                    z = zzb.zzc(parcel, zzaT);
                    break;
                case 5:
                    j3 = zzb.zzi(parcel, zzaT);
                    break;
                case 6:
                    i3 = zzb.zzg(parcel, zzaT);
                    break;
                case 7:
                    f = zzb.zzl(parcel, zzaT);
                    break;
                case 8:
                    j4 = zzb.zzi(parcel, zzaT);
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
            return new LocationRequest(i, i2, j, j2, z, j3, i3, f, j4);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public LocationRequest[] zzke(int i) {
        return new LocationRequest[i];
    }
}
