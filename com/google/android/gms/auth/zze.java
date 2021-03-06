package com.google.android.gms.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.internal.zzace;
import com.google.android.gms.internal.zzvp;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class zze {
    public static final int CHANGE_TYPE_ACCOUNT_ADDED = 1;
    public static final int CHANGE_TYPE_ACCOUNT_REMOVED = 2;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_FROM = 3;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_TO = 4;
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    public static final String KEY_ANDROID_PACKAGE_NAME = (VERSION.SDK_INT >= 14 ? "androidPackageName" : "androidPackageName");
    public static final String KEY_CALLER_UID = (VERSION.SDK_INT >= 11 ? "callerUid" : "callerUid");
    public static final String KEY_SUPPRESS_PROGRESS_SCREEN = "suppressProgressScreen";
    public static final String WORK_ACCOUNT_TYPE = "com.google.work";
    private static final zzace zzahA = zzd.zzb("GoogleAuthUtil");
    private static final String[] zzahx = new String[]{"com.google", "com.google.work", "cn.google"};
    private static final ComponentName zzahy = new ComponentName("com.google.android.gms", "com.google.android.gms.auth.GetToken");
    private static final ComponentName zzahz = new ComponentName("com.google.android.gms", "com.google.android.gms.recovery.RecoveryService");

    private interface zza<T> {
        T zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException;
    }

    class C06641 implements zza<TokenData> {
        final /* synthetic */ Bundle val$options;
        final /* synthetic */ Account zzahB;
        final /* synthetic */ String zzahC;

        C06641(Account account, String str, Bundle bundle) {
            this.zzahB = account;
            this.zzahC = str;
            this.val$options = bundle;
        }

        public TokenData zzat(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            Bundle bundle = (Bundle) zze.zzn(com.google.android.gms.internal.zzbz.zza.zza(iBinder).zza(this.zzahB, this.zzahC, this.val$options));
            TokenData zzd = TokenData.zzd(bundle, "tokenDetails");
            if (zzd != null) {
                return zzd;
            }
            String string = bundle.getString("Error");
            Intent intent = (Intent) bundle.getParcelable("userRecoveryIntent");
            zzvp zzcD = zzvp.zzcD(string);
            if (zzvp.zza(zzcD)) {
                Object[] objArr = new Object[1];
                String valueOf = String.valueOf(zzcD);
                objArr[0] = new StringBuilder(String.valueOf(valueOf).length() + 31).append("isUserRecoverableError status: ").append(valueOf).toString();
                zze.zzahA.zzf("GoogleAuthUtil", objArr);
                throw new UserRecoverableAuthException(string, intent);
            } else if (zzvp.zzb(zzcD)) {
                throw new IOException(string);
            } else {
                throw new GoogleAuthException(string);
            }
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzat(iBinder);
        }
    }

    class C06652 implements zza<Void> {
        final /* synthetic */ String zzahD;
        final /* synthetic */ Bundle zzahE;

        C06652(String str, Bundle bundle) {
            this.zzahD = str;
            this.zzahE = bundle;
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzav(iBinder);
        }

        public Void zzav(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            Bundle bundle = (Bundle) zze.zzn(com.google.android.gms.internal.zzbz.zza.zza(iBinder).zza(this.zzahD, this.zzahE));
            String string = bundle.getString("Error");
            if (bundle.getBoolean("booleanResult")) {
                return null;
            }
            throw new GoogleAuthException(string);
        }
    }

    class C06663 implements zza<List<AccountChangeEvent>> {
        final /* synthetic */ String zzahF;
        final /* synthetic */ int zzahG;

        C06663(String str, int i) {
            this.zzahF = str;
            this.zzahG = i;
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzaw(iBinder);
        }

        public List<AccountChangeEvent> zzaw(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return ((AccountChangeEventsResponse) zze.zzn(com.google.android.gms.internal.zzbz.zza.zza(iBinder).zza(new AccountChangeEventsRequest().setAccountName(this.zzahF).setEventIndex(this.zzahG)))).getEvents();
        }
    }

    class C06674 implements zza<Bundle> {
        final /* synthetic */ Account zzahB;

        C06674(Account account) {
            this.zzahB = account;
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzax(iBinder);
        }

        public Bundle zzax(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return (Bundle) zze.zzn(com.google.android.gms.internal.zzbz.zza.zza(iBinder).zza(this.zzahB));
        }
    }

    zze() {
    }

    public static void clearToken(Context context, String str) throws GooglePlayServicesAvailabilityException, GoogleAuthException, IOException {
        zzac.zzdo("Calling this from your main thread can lead to deadlock");
        zzZ(context);
        Bundle bundle = new Bundle();
        String str2 = context.getApplicationInfo().packageName;
        bundle.putString("clientPackageName", str2);
        if (!bundle.containsKey(KEY_ANDROID_PACKAGE_NAME)) {
            bundle.putString(KEY_ANDROID_PACKAGE_NAME, str2);
        }
        zza(context, zzahy, new C06652(str, bundle));
    }

    public static List<AccountChangeEvent> getAccountChangeEvents(Context context, int i, String str) throws GoogleAuthException, IOException {
        zzac.zzh(str, "accountName must be provided");
        zzac.zzdo("Calling this from your main thread can lead to deadlock");
        zzZ(context);
        return (List) zza(context, zzahy, new C06663(str, i));
    }

    public static String getAccountId(Context context, String str) throws GoogleAuthException, IOException {
        zzac.zzh(str, "accountName must be provided");
        zzac.zzdo("Calling this from your main thread can lead to deadlock");
        zzZ(context);
        return getToken(context, str, "^^_account_id_^^", new Bundle());
    }

    public static String getToken(Context context, Account account, String str) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, account, str, new Bundle());
    }

    public static String getToken(Context context, Account account, String str, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        zzc(account);
        return zzc(context, account, str, bundle).getToken();
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(str, "com.google"), str2);
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(str, "com.google"), str2, bundle);
    }

    @RequiresPermission("android.permission.MANAGE_ACCOUNTS")
    @Deprecated
    public static void invalidateToken(Context context, String str) {
        AccountManager.get(context).invalidateAuthToken("com.google", str);
    }

    @TargetApi(23)
    public static Bundle removeAccount(Context context, Account account) throws GoogleAuthException, IOException {
        zzac.zzw(context);
        zzc(account);
        zzZ(context);
        return (Bundle) zza(context, zzahy, new C06674(account));
    }

    private static void zzZ(Context context) throws GoogleAuthException {
        try {
            com.google.android.gms.common.zze.zzZ(context.getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            throw new GooglePlayServicesAvailabilityException(e.getConnectionStatusCode(), e.getMessage(), e.getIntent());
        } catch (GooglePlayServicesNotAvailableException e2) {
            throw new GoogleAuthException(e2.getMessage());
        }
    }

    private static <T> T zza(Context context, ComponentName componentName, zza<T> com_google_android_gms_auth_zze_zza_T) throws IOException, GoogleAuthException {
        Throwable e;
        ServiceConnection com_google_android_gms_common_zza = new com.google.android.gms.common.zza();
        zzn zzaC = zzn.zzaC(context);
        if (zzaC.zza(componentName, com_google_android_gms_common_zza, "GoogleAuthUtil")) {
            try {
                T zzau = com_google_android_gms_auth_zze_zza_T.zzau(com_google_android_gms_common_zza.zzuy());
                zzaC.zzb(componentName, com_google_android_gms_common_zza, "GoogleAuthUtil");
                return zzau;
            } catch (RemoteException e2) {
                e = e2;
                try {
                    zzahA.zze("GoogleAuthUtil", "Error on service connection.", e);
                    throw new IOException("Error on service connection.", e);
                } catch (Throwable th) {
                    zzaC.zzb(componentName, com_google_android_gms_common_zza, "GoogleAuthUtil");
                }
            } catch (InterruptedException e3) {
                e = e3;
                zzahA.zze("GoogleAuthUtil", "Error on service connection.", e);
                throw new IOException("Error on service connection.", e);
            }
        }
        throw new IOException("Could not bind to service.");
    }

    public static TokenData zzc(Context context, Account account, String str, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        zzac.zzdo("Calling this from your main thread can lead to deadlock");
        zzac.zzh(str, "Scope cannot be empty or null.");
        zzc(account);
        zzZ(context);
        Bundle bundle2 = bundle == null ? new Bundle() : new Bundle(bundle);
        String str2 = context.getApplicationInfo().packageName;
        bundle2.putString("clientPackageName", str2);
        if (TextUtils.isEmpty(bundle2.getString(KEY_ANDROID_PACKAGE_NAME))) {
            bundle2.putString(KEY_ANDROID_PACKAGE_NAME, str2);
        }
        bundle2.putLong("service_connection_start_time_millis", SystemClock.elapsedRealtime());
        return (TokenData) zza(context, zzahy, new C06641(account, str, bundle2));
    }

    private static void zzc(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        } else if (TextUtils.isEmpty(account.name)) {
            throw new IllegalArgumentException("Account name cannot be empty!");
        } else {
            String[] strArr = zzahx;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                if (!strArr[i].equals(account.type)) {
                    i++;
                } else {
                    return;
                }
            }
            throw new IllegalArgumentException("Account type not supported");
        }
    }

    static void zzi(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("Callback cannot be null.");
        }
        try {
            Intent.parseUri(intent.toUri(1), 1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
        }
    }

    private static <T> T zzn(T t) throws IOException {
        if (t != null) {
            return t;
        }
        zzahA.zzf("GoogleAuthUtil", "Binder call returned null.");
        throw new IOException("Service unavailable.");
    }
}
