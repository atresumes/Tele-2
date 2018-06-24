package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import com.google.android.gms.internal.zzarz;
import java.util.List;

public class zzi implements Creator<GeofencingRequest> {
    static void zza(GeofencingRequest geofencingRequest, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzc(parcel, 1, geofencingRequest.zzHq(), false);
        zzc.zzc(parcel, 2, geofencingRequest.getInitialTrigger());
        zzc.zza(parcel, 3, geofencingRequest.getTag(), false);
        zzc.zzc(parcel, 1000, geofencingRequest.getVersionCode());
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzgF(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzjZ(i);
    }

    public GeofencingRequest zzgF(Parcel parcel) {
        int i = 0;
        int zzaU = zzb.zzaU(parcel);
        List list = null;
        String str = "";
        int i2 = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    list = zzb.zzc(parcel, zzaT, zzarz.CREATOR);
                    break;
                case 2:
                    i = zzb.zzg(parcel, zzaT);
                    break;
                case 3:
                    str = zzb.zzq(parcel, zzaT);
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
            return new GeofencingRequest(i2, list, i, str);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public GeofencingRequest[] zzjZ(int i) {
        return new GeofencingRequest[i];
    }
}
