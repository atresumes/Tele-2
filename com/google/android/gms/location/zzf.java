package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.internal.zzarj;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class zzf extends zza {
    public static final Creator<zzf> CREATOR = new zzg();
    public static final Comparator<zzd> zzbiS = new C08381();
    @Nullable
    private final String mTag;
    private final int mVersionCode;
    private final List<zzd> zzbiT;
    private final List<zzarj> zzbiU;

    class C08381 implements Comparator<zzd> {
        C08381() {
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return zza((zzd) obj, (zzd) obj2);
        }

        public int zza(zzd com_google_android_gms_location_zzd, zzd com_google_android_gms_location_zzd2) {
            int zzBg = com_google_android_gms_location_zzd.zzBg();
            int zzBg2 = com_google_android_gms_location_zzd2.zzBg();
            if (zzBg != zzBg2) {
                return zzBg < zzBg2 ? -1 : 1;
            } else {
                zzBg = com_google_android_gms_location_zzd.zzHn();
                zzBg2 = com_google_android_gms_location_zzd2.zzHn();
                return zzBg == zzBg2 ? 0 : zzBg >= zzBg2 ? 1 : -1;
            }
        }
    }

    zzf(int i, List<zzd> list, @Nullable String str, @Nullable List<zzarj> list2) {
        zzac.zzb((Object) list, (Object) "transitions can't be null");
        zzac.zzb(list.size() > 0, (Object) "transitions can't be empty.");
        zzB(list);
        this.mVersionCode = i;
        this.zzbiT = Collections.unmodifiableList(list);
        this.mTag = str;
        this.zzbiU = list2 == null ? Collections.emptyList() : Collections.unmodifiableList(list2);
    }

    private static void zzB(List<zzd> list) {
        TreeSet treeSet = new TreeSet(zzbiS);
        for (zzd add : list) {
            zzac.zzb(treeSet.add(add), String.format("Found duplicated transition: %s.", new Object[]{add}));
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzf com_google_android_gms_location_zzf = (zzf) obj;
        return zzaa.equal(this.zzbiT, com_google_android_gms_location_zzf.zzbiT) && zzaa.equal(this.mTag, com_google_android_gms_location_zzf.mTag) && zzaa.equal(this.zzbiU, com_google_android_gms_location_zzf.zzbiU);
    }

    @Nullable
    public String getTag() {
        return this.mTag;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.mTag != null ? this.mTag.hashCode() : 0) + (this.zzbiT.hashCode() * 31)) * 31;
        if (this.zzbiU != null) {
            i = this.zzbiU.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        String valueOf = String.valueOf(this.zzbiT);
        String str = this.mTag;
        String valueOf2 = String.valueOf(this.zzbiU);
        return new StringBuilder(((String.valueOf(valueOf).length() + 61) + String.valueOf(str).length()) + String.valueOf(valueOf2).length()).append("ActivityTransitionRequest [mTransitions=").append(valueOf).append(", mTag='").append(str).append("'").append(", mClients=").append(valueOf2).append("]").toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzg.zza(this, parcel, i);
    }

    public List<zzd> zzHo() {
        return this.zzbiT;
    }

    public List<zzarj> zzHp() {
        return this.zzbiU;
    }
}
