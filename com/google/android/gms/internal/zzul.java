package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.RemoteException;
import com.google.android.gms.auth.account.WorkAccount;
import com.google.android.gms.auth.account.WorkAccountApi;
import com.google.android.gms.auth.account.WorkAccountApi.AddAccountResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

public class zzul implements WorkAccountApi {
    private static final Status zzahN = new Status(13);

    static class zza extends com.google.android.gms.auth.account.zza.zza {
        zza() {
        }

        public void zzab(boolean z) {
            throw new UnsupportedOperationException();
        }

        public void zzd(Account account) {
            throw new UnsupportedOperationException();
        }
    }

    static class zzb implements AddAccountResult {
        private final Account zzagg;
        private final Status zzahq;

        public zzb(Status status, Account account) {
            this.zzahq = status;
            this.zzagg = account;
        }

        public Account getAccount() {
            return this.zzagg;
        }

        public Status getStatus() {
            return this.zzahq;
        }
    }

    static class zzc implements Result {
        private final Status zzahq;

        public zzc(Status status) {
            this.zzahq = status;
        }

        public Status getStatus() {
            return this.zzahq;
        }
    }

    public PendingResult<AddAccountResult> addWorkAccount(GoogleApiClient googleApiClient, final String str) {
        return googleApiClient.zzb(new com.google.android.gms.internal.zzzv.zza<AddAccountResult, zzum>(this, WorkAccount.API, googleApiClient) {

            class C08171 extends zza {
                final /* synthetic */ C08182 zzahP;

                C08171(C08182 c08182) {
                    this.zzahP = c08182;
                }

                public void zzd(Account account) {
                    this.zzahP.zzb(new zzb(account != null ? Status.zzayh : zzul.zzahN, account));
                }
            }

            protected void zza(zzum com_google_android_gms_internal_zzum) throws RemoteException {
                ((com.google.android.gms.auth.account.zzb) com_google_android_gms_internal_zzum.zzwW()).zza(new C08171(this), str);
            }

            protected /* synthetic */ Result zzc(Status status) {
                return zzf(status);
            }

            protected AddAccountResult zzf(Status status) {
                return new zzb(status, null);
            }
        });
    }

    public PendingResult<Result> removeWorkAccount(GoogleApiClient googleApiClient, final Account account) {
        return googleApiClient.zzb(new com.google.android.gms.internal.zzzv.zza<Result, zzum>(this, WorkAccount.API, googleApiClient) {

            class C08191 extends zza {
                final /* synthetic */ C08203 zzahQ;

                C08191(C08203 c08203) {
                    this.zzahQ = c08203;
                }

                public void zzab(boolean z) {
                    this.zzahQ.zzb(new zzc(z ? Status.zzayh : zzul.zzahN));
                }
            }

            protected void zza(zzum com_google_android_gms_internal_zzum) throws RemoteException {
                ((com.google.android.gms.auth.account.zzb) com_google_android_gms_internal_zzum.zzwW()).zza(new C08191(this), account);
            }

            protected Result zzc(Status status) {
                return new zzc(status);
            }
        });
    }

    public void setWorkAuthenticatorEnabled(GoogleApiClient googleApiClient, final boolean z) {
        googleApiClient.zzb(new com.google.android.gms.internal.zzzv.zza<Result, zzum>(this, WorkAccount.API, googleApiClient) {
            protected void zza(zzum com_google_android_gms_internal_zzum) throws RemoteException {
                ((com.google.android.gms.auth.account.zzb) com_google_android_gms_internal_zzum.zzwW()).zzac(z);
            }

            protected Result zzc(Status status) {
                return null;
            }
        });
    }
}
