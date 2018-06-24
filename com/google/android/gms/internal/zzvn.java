package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzac;

public class zzvn implements ProxyApi {
    public PendingResult<ProxyResult> performProxyRequest(GoogleApiClient googleApiClient, final ProxyRequest proxyRequest) {
        zzac.zzw(googleApiClient);
        zzac.zzw(proxyRequest);
        return googleApiClient.zzb(new zzvm(this, googleApiClient) {

            class C08271 extends zzvi {
                final /* synthetic */ C08281 zzaiT;

                C08271(C08281 c08281) {
                    this.zzaiT = c08281;
                }

                public void zza(ProxyResponse proxyResponse) {
                    this.zzaiT.zzb(new zzvo(proxyResponse));
                }
            }

            protected void zza(Context context, zzvl com_google_android_gms_internal_zzvl) throws RemoteException {
                com_google_android_gms_internal_zzvl.zza(new C08271(this), proxyRequest);
            }
        });
    }
}
