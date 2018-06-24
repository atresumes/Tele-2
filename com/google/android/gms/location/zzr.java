package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzr implements Creator<zzq> {
    static void zza(zzq com_google_android_gms_location_zzq, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_location_zzq.zzHs(), false);
        zzc.zza(parcel, 2, com_google_android_gms_location_zzq.zzHt(), false);
        zzc.zzc(parcel, 3, com_google_android_gms_location_zzq.zzHu());
        zzc.zzc(parcel, 1000, com_google_android_gms_location_zzq.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgK(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzkg(i);
    }

    public zzq zzgK(Parcel parcel) {
        int i = 0;
        int zzaU = zzb.zzaU(parcel);
        String str = "";
        String str2 = "";
        int i2 = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    str = zzb.zzq(parcel, zzaT);
                    break;
                case 2:
                    str2 = zzb.zzq(parcel, zzaT);
                    break;
                case 3:
                    i = zzb.zzg(parcel, zzaT);
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
            return new zzq(i2, str, str2, i);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public zzq[] zzkg(int i) {
        return new zzq[i];
    }
}
