package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import java.util.List;

public class zzs implements Creator<LocationSettingsRequest> {
    static void zza(LocationSettingsRequest locationSettingsRequest, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzc(parcel, 1, locationSettingsRequest.zzCr(), false);
        zzc.zza(parcel, 2, locationSettingsRequest.zzHv());
        zzc.zza(parcel, 3, locationSettingsRequest.zzHw());
        zzc.zza(parcel, 5, locationSettingsRequest.zzHx(), i, false);
        zzc.zzc(parcel, 1000, locationSettingsRequest.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgL(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzkh(i);
    }

    public LocationSettingsRequest zzgL(Parcel parcel) {
        zzq com_google_android_gms_location_zzq = null;
        boolean z = false;
        int zzaU = zzb.zzaU(parcel);
        boolean z2 = false;
        List list = null;
        int i = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    list = zzb.zzc(parcel, zzaT, LocationRequest.CREATOR);
                    break;
                case 2:
                    z2 = zzb.zzc(parcel, zzaT);
                    break;
                case 3:
                    z = zzb.zzc(parcel, zzaT);
                    break;
                case 5:
                    com_google_android_gms_location_zzq = (zzq) zzb.zza(parcel, zzaT, zzq.CREATOR);
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
            return new LocationSettingsRequest(i, list, z2, z, com_google_android_gms_location_zzq);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public LocationSettingsRequest[] zzkh(int i) {
        return new LocationSettingsRequest[i];
    }
}
