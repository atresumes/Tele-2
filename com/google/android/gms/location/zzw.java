package com.google.android.gms.location;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import java.util.List;

public class zzw implements Creator<zzv> {
    static void zza(zzv com_google_android_gms_location_zzv, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzb(parcel, 1, com_google_android_gms_location_zzv.zzHy(), false);
        zzc.zza(parcel, 2, com_google_android_gms_location_zzv.zzuT(), i, false);
        zzc.zza(parcel, 3, com_google_android_gms_location_zzv.getTag(), false);
        zzc.zzc(parcel, 1000, com_google_android_gms_location_zzv.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgO(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzkm(i);
    }

    public zzv zzgO(Parcel parcel) {
        PendingIntent pendingIntent = null;
        int zzaU = zzb.zzaU(parcel);
        int i = 0;
        String str = "";
        List list = null;
        while (parcel.dataPosition() < zzaU) {
            int i2;
            String str2;
            PendingIntent pendingIntent2;
            List list2;
            int zzaT = zzb.zzaT(parcel);
            String str3;
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    i2 = i;
                    PendingIntent pendingIntent3 = pendingIntent;
                    Object zzE = zzb.zzE(parcel, zzaT);
                    str2 = str;
                    pendingIntent2 = pendingIntent3;
                    break;
                case 2:
                    list2 = list;
                    i2 = i;
                    str3 = str;
                    pendingIntent2 = (PendingIntent) zzb.zza(parcel, zzaT, PendingIntent.CREATOR);
                    str2 = str3;
                    break;
                case 3:
                    str2 = zzb.zzq(parcel, zzaT);
                    pendingIntent2 = pendingIntent;
                    list2 = list;
                    i2 = i;
                    break;
                case 1000:
                    str3 = str;
                    pendingIntent2 = pendingIntent;
                    list2 = list;
                    i2 = zzb.zzg(parcel, zzaT);
                    str2 = str3;
                    break;
                default:
                    zzb.zzb(parcel, zzaT);
                    str2 = str;
                    pendingIntent2 = pendingIntent;
                    list2 = list;
                    i2 = i;
                    break;
            }
            i = i2;
            list = list2;
            pendingIntent = pendingIntent2;
            str = str2;
        }
        if (parcel.dataPosition() == zzaU) {
            return new zzv(i, list, pendingIntent, str);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public zzv[] zzkm(int i) {
        return new zzv[i];
    }
}
