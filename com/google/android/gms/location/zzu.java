package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzu implements Creator<LocationSettingsStates> {
    static void zza(LocationSettingsStates locationSettingsStates, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zza(parcel, 1, locationSettingsStates.isGpsUsable());
        zzc.zza(parcel, 2, locationSettingsStates.isNetworkLocationUsable());
        zzc.zza(parcel, 3, locationSettingsStates.isBleUsable());
        zzc.zza(parcel, 4, locationSettingsStates.isGpsPresent());
        zzc.zza(parcel, 5, locationSettingsStates.isNetworkLocationPresent());
        zzc.zza(parcel, 6, locationSettingsStates.isBlePresent());
        zzc.zzc(parcel, 1000, locationSettingsStates.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgN(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzkj(i);
    }

    public LocationSettingsStates zzgN(Parcel parcel) {
        boolean z = false;
        int zzaU = zzb.zzaU(parcel);
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        boolean z6 = false;
        int i = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    z6 = zzb.zzc(parcel, zzaT);
                    break;
                case 2:
                    z5 = zzb.zzc(parcel, zzaT);
                    break;
                case 3:
                    z4 = zzb.zzc(parcel, zzaT);
                    break;
                case 4:
                    z3 = zzb.zzc(parcel, zzaT);
                    break;
                case 5:
                    z2 = zzb.zzc(parcel, zzaT);
                    break;
                case 6:
                    z = zzb.zzc(parcel, zzaT);
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
            return new LocationSettingsStates(i, z6, z5, z4, z3, z2, z);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public LocationSettingsStates[] zzkj(int i) {
        return new LocationSettingsStates[i];
    }
}
