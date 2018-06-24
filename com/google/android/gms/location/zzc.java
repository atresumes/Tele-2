package com.google.android.gms.location;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import java.util.List;

public class zzc implements Creator<ActivityRecognitionResult> {
    static void zza(ActivityRecognitionResult activityRecognitionResult, Parcel parcel, int i) {
        int zzaV = com.google.android.gms.common.internal.safeparcel.zzc.zzaV(parcel);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 1, activityRecognitionResult.zzbiN, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 2, activityRecognitionResult.zzbiO);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 3, activityRecognitionResult.zzbiP);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 4, activityRecognitionResult.zzbiQ);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 5, activityRecognitionResult.extras, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 1000, activityRecognitionResult.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgB(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzjS(i);
    }

    public ActivityRecognitionResult zzgB(Parcel parcel) {
        long j = 0;
        Bundle bundle = null;
        int i = 0;
        int zzaU = zzb.zzaU(parcel);
        long j2 = 0;
        List list = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    list = zzb.zzc(parcel, zzaT, DetectedActivity.CREATOR);
                    break;
                case 2:
                    j2 = zzb.zzi(parcel, zzaT);
                    break;
                case 3:
                    j = zzb.zzi(parcel, zzaT);
                    break;
                case 4:
                    i = zzb.zzg(parcel, zzaT);
                    break;
                case 5:
                    bundle = zzb.zzs(parcel, zzaT);
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
            return new ActivityRecognitionResult(i2, list, j2, j, i, bundle);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public ActivityRecognitionResult[] zzjS(int i) {
        return new ActivityRecognitionResult[i];
    }
}
