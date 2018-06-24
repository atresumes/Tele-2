package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.internal.zzarz;
import java.util.ArrayList;
import java.util.List;

public class GeofencingRequest extends zza {
    public static final Creator<GeofencingRequest> CREATOR = new zzi();
    public static final int INITIAL_TRIGGER_DWELL = 4;
    public static final int INITIAL_TRIGGER_ENTER = 1;
    public static final int INITIAL_TRIGGER_EXIT = 2;
    private final String mTag;
    private final int mVersionCode;
    private final List<zzarz> zzbjl;
    private final int zzbjm;

    public static final class Builder {
        private String mTag = "";
        private final List<zzarz> zzbjl = new ArrayList();
        private int zzbjm = 5;

        public static int zzjY(int i) {
            return i & 7;
        }

        public Builder addGeofence(Geofence geofence) {
            zzac.zzb((Object) geofence, (Object) "geofence can't be null.");
            zzac.zzb(geofence instanceof zzarz, (Object) "Geofence must be created using Geofence.Builder.");
            this.zzbjl.add((zzarz) geofence);
            return this;
        }

        public Builder addGeofences(List<Geofence> list) {
            if (!(list == null || list.isEmpty())) {
                for (Geofence geofence : list) {
                    if (geofence != null) {
                        addGeofence(geofence);
                    }
                }
            }
            return this;
        }

        public GeofencingRequest build() {
            zzac.zzb(!this.zzbjl.isEmpty(), (Object) "No geofence has been added to this request.");
            return new GeofencingRequest(this.zzbjl, this.zzbjm, this.mTag);
        }

        public Builder setInitialTrigger(int i) {
            this.zzbjm = zzjY(i);
            return this;
        }
    }

    GeofencingRequest(int i, List<zzarz> list, int i2, String str) {
        this.mVersionCode = i;
        this.zzbjl = list;
        this.zzbjm = i2;
        this.mTag = str;
    }

    private GeofencingRequest(List<zzarz> list, int i, String str) {
        this(1, (List) list, i, str);
    }

    public List<Geofence> getGeofences() {
        List<Geofence> arrayList = new ArrayList();
        arrayList.addAll(this.zzbjl);
        return arrayList;
    }

    public int getInitialTrigger() {
        return this.zzbjm;
    }

    public String getTag() {
        return this.mTag;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzi.zza(this, parcel, i);
    }

    public List<zzarz> zzHq() {
        return this.zzbjl;
    }
}
