package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzd implements Creator<HintRequest> {
    static void zza(HintRequest hintRequest, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zza(parcel, 1, hintRequest.getHintPickerConfig(), i, false);
        zzc.zza(parcel, 2, hintRequest.isEmailAddressIdentifierSupported());
        zzc.zza(parcel, 3, hintRequest.zzqx());
        zzc.zza(parcel, 4, hintRequest.getAccountTypes(), false);
        zzc.zzc(parcel, 1000, hintRequest.mVersionCode);
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzM(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzba(i);
    }

    public HintRequest zzM(Parcel parcel) {
        String[] strArr = null;
        boolean z = false;
        int zzaU = zzb.zzaU(parcel);
        boolean z2 = false;
        CredentialPickerConfig credentialPickerConfig = null;
        int i = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case 1:
                    credentialPickerConfig = (CredentialPickerConfig) zzb.zza(parcel, zzaT, CredentialPickerConfig.CREATOR);
                    break;
                case 2:
                    z2 = zzb.zzc(parcel, zzaT);
                    break;
                case 3:
                    z = zzb.zzc(parcel, zzaT);
                    break;
                case 4:
                    strArr = zzb.zzC(parcel, zzaT);
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
            return new HintRequest(i, credentialPickerConfig, z2, z, strArr);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public HintRequest[] zzba(int i) {
        return new HintRequest[i];
    }
}
