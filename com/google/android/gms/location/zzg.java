package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import com.google.android.gms.internal.zzarj;
import java.util.List;

public class zzg implements Creator<zzf> {
    static void zza(zzf com_google_android_gms_location_zzf, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzc(parcel, 1, com_google_android_gms_location_zzf.zzHo(), false);
        zzc.zza(parcel, 2, com_google_android_gms_location_zzf.getTag(), false);
        zzc.zzc(parcel, 3, com_google_android_gms_location_zzf.zzHp(), false);
        zzc.zzc(parcel, 1000, com_google_android_gms_location_zzf.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgD(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzjU(i);
    }

    public zzf zzgD(Parcel parcel) {
        List list = null;
        int zzaU = zzb.zzaU(parcel);
        int i = 0;
        String str = null;
        List list2 = null;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    list2 = zzb.zzc(parcel, zzaT, zzd.CREATOR);
                    break;
                case 2:
                    str = zzb.zzq(parcel, zzaT);
                    break;
                case 3:
                    list = zzb.zzc(parcel, zzaT, zzarj.CREATOR);
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
            return new zzf(i, list2, str, list);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public zzf[] zzjU(int i) {
        return new zzf[i];
    }
}
