package com.google.android.gms.location;

import android.content.Intent;
import android.location.Location;
import com.google.android.gms.internal.zzarz;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeofencingEvent {
    private final int zzPF;
    private final int zzbji;
    private final List<Geofence> zzbjj;
    private final Location zzbjk;

    private GeofencingEvent(int i, int i2, List<Geofence> list, Location location) {
        this.zzPF = i;
        this.zzbji = i2;
        this.zzbjj = list;
        this.zzbjk = location;
    }

    public static GeofencingEvent fromIntent(Intent intent) {
        return intent == null ? null : new GeofencingEvent(intent.getIntExtra("gms_error_code", -1), zzA(intent), zzB(intent), (Location) intent.getParcelableExtra("com.google.android.location.intent.extra.triggering_location"));
    }

    private static int zzA(Intent intent) {
        int intExtra = intent.getIntExtra("com.google.android.location.intent.extra.transition", -1);
        return intExtra == -1 ? -1 : (intExtra == 1 || intExtra == 2 || intExtra == 4) ? intExtra : -1;
    }

    private static List<Geofence> zzB(Intent intent) {
        ArrayList arrayList = (ArrayList) intent.getSerializableExtra("com.google.android.location.intent.extra.geofence_list");
        if (arrayList == null) {
            return null;
        }
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(zzarz.zzw((byte[]) it.next()));
        }
        return arrayList2;
    }

    public int getErrorCode() {
        return this.zzPF;
    }

    public int getGeofenceTransition() {
        return this.zzbji;
    }

    public List<Geofence> getTriggeringGeofences() {
        return this.zzbjj;
    }

    public Location getTriggeringLocation() {
        return this.zzbjk;
    }

    public boolean hasError() {
        return this.zzPF != -1;
    }
}
