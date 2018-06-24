package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import java.util.List;

public class zzk implements Creator<zzj> {
    static void zza(zzj com_google_android_gms_location_zzj, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_location_zzj.zzHr(), false);
        zzc.zzc(parcel, 1000, com_google_android_gms_location_zzj.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgG(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzka(i);
    }

    public zzj zzgG(Parcel parcel) {
        int zzaU = zzb.zzaU(parcel);
        int i = 0;
        List list = null;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    list = zzb.zzD(parcel, zzaT);
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
            return new zzj(i, list);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public zzj[] zzka(int i) {
        return new zzj[i];
    }
}
