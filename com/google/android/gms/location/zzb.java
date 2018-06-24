package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.WorkSource;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzb implements Creator<zza> {
    static void zza(zza com_google_android_gms_location_zza, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_location_zza.getIntervalMillis());
        zzc.zza(parcel, 2, com_google_android_gms_location_zza.zzHi());
        zzc.zza(parcel, 3, com_google_android_gms_location_zza.zzHj(), i, false);
        zzc.zza(parcel, 4, com_google_android_gms_location_zza.getTag(), false);
        zzc.zza(parcel, 5, com_google_android_gms_location_zza.zzHk(), false);
        zzc.zza(parcel, 6, com_google_android_gms_location_zza.zzHl());
        zzc.zza(parcel, 7, com_google_android_gms_location_zza.getAccountName(), false);
        zzc.zzc(parcel, 1000, com_google_android_gms_location_zza.getVersionCode());
        zzc.zza(parcel, 8, com_google_android_gms_location_zza.zzHm());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgA(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzjR(i);
    }

    public zza zzgA(Parcel parcel) {
        long j = 0;
        boolean z = false;
        String str = null;
        int zzaU = com.google.android.gms.common.internal.safeparcel.zzb.zzaU(parcel);
        int[] iArr = null;
        String str2 = null;
        WorkSource workSource = null;
        boolean z2 = false;
        long j2 = 0;
        int i = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = com.google.android.gms.common.internal.safeparcel.zzb.zzaT(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zzb.zzcW(zzaT)) {
                case 1:
                    j2 = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, zzaT);
                    break;
                case 2:
                    z2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, zzaT);
                    break;
                case 3:
                    workSource = (WorkSource) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, zzaT, WorkSource.CREATOR);
                    break;
                case 4:
                    str2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, zzaT);
                    break;
                case 5:
                    iArr = com.google.android.gms.common.internal.safeparcel.zzb.zzw(parcel, zzaT);
                    break;
                case 6:
                    z = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, zzaT);
                    break;
                case 7:
                    str = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, zzaT);
                    break;
                case 8:
                    j = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, zzaT);
                    break;
                case 1000:
                    i = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, zzaT);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, zzaT);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaU) {
            return new zza(i, j2, z2, workSource, str2, iArr, z, str, j);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public zza[] zzjR(int i) {
        return new zza[i];
    }
}
