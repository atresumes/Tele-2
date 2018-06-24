package fr.quentinklein.slt;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import fr.quentinklein.aslt.C1433R;

public class LocationUtils {

    static class C14351 implements OnClickListener {
        C14351() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public static boolean isGpsProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, "gps");
    }

    public static boolean isNetworkProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, "network");
    }

    public static boolean isPassiveProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, "passive");
    }

    @UiThread
    public static void askEnableProviders(@NonNull Context context) {
        askEnableProviders(context, C1433R.string.provider_settings_message);
    }

    @UiThread
    public static void askEnableProviders(@NonNull Context context, @StringRes int messageResource) {
        askEnableProviders(context, messageResource, C1433R.string.provider_settings_yes, C1433R.string.provider_settings_yes);
    }

    @UiThread
    public static void askEnableProviders(@NonNull final Context context, @StringRes int messageResource, @StringRes int positiveLabelResource, @StringRes int negativeLabelResource) {
        new Builder(context).setMessage(messageResource).setCancelable(false).setPositiveButton(positiveLabelResource, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        }).setNegativeButton(negativeLabelResource, new C14351()).show();
    }

    private static boolean isProviderEnabled(@NonNull Context context, @NonNull String provider) {
        return ((LocationManager) context.getSystemService("location")).isProviderEnabled(provider);
    }
}
