package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.internal.zzaaz.zzc;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.zzl;
import com.google.android.gms.location.zzm;
import java.util.HashMap;
import java.util.Map;

public class zzart {
    private final Context mContext;
    private final Map<com.google.android.gms.internal.zzaaz.zzb<LocationListener>, zzb> zzaVF = new HashMap();
    private final zzasb<zzarr> zzbjV;
    private ContentProviderClient zzbkg = null;
    private boolean zzbkh = false;
    private final Map<com.google.android.gms.internal.zzaaz.zzb<LocationCallback>, zza> zzbki = new HashMap();

    private static class zza extends com.google.android.gms.location.zzl.zza {
        private final zzaaz<LocationCallback> zzaBG;

        zza(zzaaz<LocationCallback> com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback) {
            this.zzaBG = com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback;
        }

        public void onLocationAvailability(final LocationAvailability locationAvailability) {
            this.zzaBG.zza(new zzc<LocationCallback>(this) {
                public void zza(LocationCallback locationCallback) {
                    locationCallback.onLocationAvailability(locationAvailability);
                }

                public /* synthetic */ void zzs(Object obj) {
                    zza((LocationCallback) obj);
                }

                public void zzvy() {
                }
            });
        }

        public void onLocationResult(final LocationResult locationResult) {
            this.zzaBG.zza(new zzc<LocationCallback>(this) {
                public void zza(LocationCallback locationCallback) {
                    locationCallback.onLocationResult(locationResult);
                }

                public /* synthetic */ void zzs(Object obj) {
                    zza((LocationCallback) obj);
                }

                public void zzvy() {
                }
            });
        }

        public synchronized void release() {
            this.zzaBG.clear();
        }
    }

    private static class zzb extends com.google.android.gms.location.zzm.zza {
        private final zzaaz<LocationListener> zzaBG;

        zzb(zzaaz<LocationListener> com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener) {
            this.zzaBG = com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener;
        }

        public synchronized void onLocationChanged(final Location location) {
            this.zzaBG.zza(new zzc<LocationListener>(this) {
                public void zza(LocationListener locationListener) {
                    locationListener.onLocationChanged(location);
                }

                public /* synthetic */ void zzs(Object obj) {
                    zza((LocationListener) obj);
                }

                public void zzvy() {
                }
            });
        }

        public synchronized void release() {
            this.zzaBG.clear();
        }
    }

    public zzart(Context context, zzasb<zzarr> com_google_android_gms_internal_zzasb_com_google_android_gms_internal_zzarr) {
        this.mContext = context;
        this.zzbjV = com_google_android_gms_internal_zzasb_com_google_android_gms_internal_zzarr;
    }

    private zzb zzf(zzaaz<LocationListener> com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener) {
        zzb com_google_android_gms_internal_zzart_zzb;
        synchronized (this.zzaVF) {
            com_google_android_gms_internal_zzart_zzb = (zzb) this.zzaVF.get(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener.zzwp());
            if (com_google_android_gms_internal_zzart_zzb == null) {
                com_google_android_gms_internal_zzart_zzb = new zzb(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener);
            }
            this.zzaVF.put(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener.zzwp(), com_google_android_gms_internal_zzart_zzb);
        }
        return com_google_android_gms_internal_zzart_zzb;
    }

    private zza zzg(zzaaz<LocationCallback> com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback) {
        zza com_google_android_gms_internal_zzart_zza;
        synchronized (this.zzbki) {
            com_google_android_gms_internal_zzart_zza = (zza) this.zzbki.get(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback.zzwp());
            if (com_google_android_gms_internal_zzart_zza == null) {
                com_google_android_gms_internal_zzart_zza = new zza(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback);
            }
            this.zzbki.put(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback.zzwp(), com_google_android_gms_internal_zzart_zza);
        }
        return com_google_android_gms_internal_zzart_zza;
    }

    public Location getLastLocation() {
        this.zzbjV.zzwV();
        try {
            return ((zzarr) this.zzbjV.zzwW()).zzeV(this.mContext.getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeAllListeners() {
        try {
            synchronized (this.zzaVF) {
                for (zzm com_google_android_gms_location_zzm : this.zzaVF.values()) {
                    if (com_google_android_gms_location_zzm != null) {
                        ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zza(com_google_android_gms_location_zzm, null));
                    }
                }
                this.zzaVF.clear();
            }
            synchronized (this.zzbki) {
                for (zzl com_google_android_gms_location_zzl : this.zzbki.values()) {
                    if (com_google_android_gms_location_zzl != null) {
                        ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zza(com_google_android_gms_location_zzl, null));
                    }
                }
                this.zzbki.clear();
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public LocationAvailability zzHB() {
        this.zzbjV.zzwV();
        try {
            return ((zzarr) this.zzbjV.zzwW()).zzeW(this.mContext.getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void zzHC() {
        if (this.zzbkh) {
            try {
                zzaC(false);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void zza(PendingIntent pendingIntent, zzarp com_google_android_gms_internal_zzarp) throws RemoteException {
        this.zzbjV.zzwV();
        ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zzb(pendingIntent, com_google_android_gms_internal_zzarp));
    }

    public void zza(com.google.android.gms.internal.zzaaz.zzb<LocationListener> com_google_android_gms_internal_zzaaz_zzb_com_google_android_gms_location_LocationListener, zzarp com_google_android_gms_internal_zzarp) throws RemoteException {
        this.zzbjV.zzwV();
        zzac.zzb((Object) com_google_android_gms_internal_zzaaz_zzb_com_google_android_gms_location_LocationListener, (Object) "Invalid null listener key");
        synchronized (this.zzaVF) {
            zzm com_google_android_gms_location_zzm = (zzb) this.zzaVF.remove(com_google_android_gms_internal_zzaaz_zzb_com_google_android_gms_location_LocationListener);
            if (com_google_android_gms_location_zzm != null) {
                com_google_android_gms_location_zzm.release();
                ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zza(com_google_android_gms_location_zzm, com_google_android_gms_internal_zzarp));
            }
        }
    }

    public void zza(zzarp com_google_android_gms_internal_zzarp) throws RemoteException {
        this.zzbjV.zzwV();
        ((zzarr) this.zzbjV.zzwW()).zza(com_google_android_gms_internal_zzarp);
    }

    public void zza(zzarv com_google_android_gms_internal_zzarv, zzaaz<LocationCallback> com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback, zzarp com_google_android_gms_internal_zzarp) throws RemoteException {
        this.zzbjV.zzwV();
        ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zza(com_google_android_gms_internal_zzarv, zzg(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationCallback), com_google_android_gms_internal_zzarp));
    }

    public void zza(LocationRequest locationRequest, PendingIntent pendingIntent, zzarp com_google_android_gms_internal_zzarp) throws RemoteException {
        this.zzbjV.zzwV();
        ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zza(zzarv.zzb(locationRequest), pendingIntent, com_google_android_gms_internal_zzarp));
    }

    public void zza(LocationRequest locationRequest, zzaaz<LocationListener> com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener, zzarp com_google_android_gms_internal_zzarp) throws RemoteException {
        this.zzbjV.zzwV();
        ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zza(zzarv.zzb(locationRequest), zzf(com_google_android_gms_internal_zzaaz_com_google_android_gms_location_LocationListener), com_google_android_gms_internal_zzarp));
    }

    public void zzaC(boolean z) throws RemoteException {
        this.zzbjV.zzwV();
        ((zzarr) this.zzbjV.zzwW()).zzaC(z);
        this.zzbkh = z;
    }

    public void zzb(com.google.android.gms.internal.zzaaz.zzb<LocationCallback> com_google_android_gms_internal_zzaaz_zzb_com_google_android_gms_location_LocationCallback, zzarp com_google_android_gms_internal_zzarp) throws RemoteException {
        this.zzbjV.zzwV();
        zzac.zzb((Object) com_google_android_gms_internal_zzaaz_zzb_com_google_android_gms_location_LocationCallback, (Object) "Invalid null listener key");
        synchronized (this.zzbki) {
            zzl com_google_android_gms_location_zzl = (zza) this.zzbki.remove(com_google_android_gms_internal_zzaaz_zzb_com_google_android_gms_location_LocationCallback);
            if (com_google_android_gms_location_zzl != null) {
                com_google_android_gms_location_zzl.release();
                ((zzarr) this.zzbjV.zzwW()).zza(zzarx.zza(com_google_android_gms_location_zzl, com_google_android_gms_internal_zzarp));
            }
        }
    }

    public void zzd(Location location) throws RemoteException {
        this.zzbjV.zzwV();
        ((zzarr) this.zzbjV.zzwW()).zzd(location);
    }
}
