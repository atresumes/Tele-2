package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class zzarl implements FusedLocationProviderApi {

    private static abstract class zza extends com.google.android.gms.location.LocationServices.zza<Status> {
        public zza(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public Status zzb(Status status) {
            return status;
        }

        public /* synthetic */ Result zzc(Status status) {
            return zzb(status);
        }
    }

    private static class zzb extends com.google.android.gms.internal.zzarp.zza {
        private final com.google.android.gms.internal.zzzv.zzb<Status> zzaFq;

        public zzb(com.google.android.gms.internal.zzzv.zzb<Status> com_google_android_gms_internal_zzzv_zzb_com_google_android_gms_common_api_Status) {
            this.zzaFq = com_google_android_gms_internal_zzzv_zzb_com_google_android_gms_common_api_Status;
        }

        public void zza(zzarm com_google_android_gms_internal_zzarm) {
            this.zzaFq.setResult(com_google_android_gms_internal_zzarm.getStatus());
        }
    }

    public PendingResult<Status> flushLocations(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(new zzb(this));
            }
        });
    }

    public Location getLastLocation(GoogleApiClient googleApiClient) {
        try {
            return LocationServices.zzj(googleApiClient).getLastLocation();
        } catch (Exception e) {
            return null;
        }
    }

    public LocationAvailability getLocationAvailability(GoogleApiClient googleApiClient) {
        try {
            return LocationServices.zzj(googleApiClient).zzHB();
        } catch (Exception e) {
            return null;
        }
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, final PendingIntent pendingIntent) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(pendingIntent, new zzb(this));
            }
        });
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, final LocationCallback locationCallback) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zzb(zzaba.zza(locationCallback, LocationCallback.class.getSimpleName()), new zzb(this));
            }
        });
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, final LocationListener locationListener) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(zzaba.zza(locationListener, LocationListener.class.getSimpleName()), new zzb(this));
            }
        });
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, final LocationRequest locationRequest, final PendingIntent pendingIntent) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(locationRequest, pendingIntent, new zzb(this));
            }
        });
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
        final LocationRequest locationRequest2 = locationRequest;
        final LocationCallback locationCallback2 = locationCallback;
        final Looper looper2 = looper;
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(zzarv.zzb(locationRequest2), zzaba.zzb(locationCallback2, zzasn.zzb(looper2), LocationCallback.class.getSimpleName()), new zzb(this));
            }
        });
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, final LocationRequest locationRequest, final LocationListener locationListener) {
        zzac.zzb(Looper.myLooper(), (Object) "Calling thread must be a prepared Looper thread.");
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(locationRequest, zzaba.zzb(locationListener, zzasn.zzIx(), LocationListener.class.getSimpleName()), new zzb(this));
            }
        });
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
        final LocationRequest locationRequest2 = locationRequest;
        final LocationListener locationListener2 = locationListener;
        final Looper looper2 = looper;
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zza(locationRequest2, zzaba.zzb(locationListener2, zzasn.zzb(looper2), LocationListener.class.getSimpleName()), new zzb(this));
            }
        });
    }

    public PendingResult<Status> setMockLocation(GoogleApiClient googleApiClient, final Location location) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zzd(location);
                zzb(Status.zzayh);
            }
        });
    }

    public PendingResult<Status> setMockMode(GoogleApiClient googleApiClient, final boolean z) {
        return googleApiClient.zzb(new zza(this, googleApiClient) {
            protected void zza(zzaru com_google_android_gms_internal_zzaru) throws RemoteException {
                com_google_android_gms_internal_zzaru.zzaC(z);
                zzb(Status.zzayh);
            }
        });
    }
}
