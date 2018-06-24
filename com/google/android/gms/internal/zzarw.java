package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import com.google.android.gms.location.LocationRequest;
import java.util.List;

public class zzarw implements Creator<zzarv> {
    static void zza(zzarv com_google_android_gms_internal_zzarv, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_internal_zzarv.zzaVV, i, false);
        zzc.zza(parcel, 4, com_google_android_gms_internal_zzarv.zzbiI);
        zzc.zzc(parcel, 5, com_google_android_gms_internal_zzarv.zzbiU, false);
        zzc.zza(parcel, 6, com_google_android_gms_internal_zzarv.mTag, false);
        zzc.zza(parcel, 7, com_google_android_gms_internal_zzarv.zzbkp);
        zzc.zzc(parcel, 1000, com_google_android_gms_internal_zzarv.getVersionCode());
        zzc.zza(parcel, 8, com_google_android_gms_internal_zzarv.zzbkq);
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgR(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzkq(i);
    }

    public zzarv zzgR(Parcel parcel) {
        String str = null;
        boolean z = false;
        int zzaU = zzb.zzaU(parcel);
        boolean z2 = true;
        List list = zzarv.zzbko;
        boolean z3 = false;
        LocationRequest locationRequest = null;
        int i = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    locationRequest = (LocationRequest) zzb.zza(parcel, zzaT, LocationRequest.CREATOR);
                    break;
                case 4:
                    z2 = zzb.zzc(parcel, zzaT);
                    break;
                case 5:
                    list = zzb.zzc(parcel, zzaT, zzarj.CREATOR);
                    break;
                case 6:
                    str = zzb.zzq(parcel, zzaT);
                    break;
                case 7:
                    z3 = zzb.zzc(parcel, zzaT);
                    break;
                case 8:
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
            return new zzarv(i, locationRequest, z2, list, str, z3, z);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public zzarv[] zzkq(int i) {
        return new zzarv[i];
    }
}
