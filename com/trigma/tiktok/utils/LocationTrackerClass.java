package com.trigma.tiktok.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class LocationTrackerClass {
    public Location location_;
    public LocationTracker myTracker;

    public LocationTrackerClass(Context context) {
        this.myTracker = new LocationTracker(context, new TrackerSettings().setUseGPS(true).setUseNetwork(true).setUsePassive(false).setTimeBetweenUpdates(1800000).setMetersBetweenUpdates(TrackerSettings.DEFAULT_MIN_METERS_BETWEEN_UPDATES)) {
            public void onLocationFound(Location location) {
                LocationTrackerClass.this.location_ = location;
                if (location != null) {
                    SharedPreff.saveLat("" + location.getLatitude());
                    SharedPreff.saveLng("" + location.getLongitude());
                    Log.e("LAT", "" + location.getLatitude());
                    Log.e("LONG", "" + location.getLongitude());
                }
            }

            public void onTimeout() {
                LocationTrackerClass.this.startLocationTracking();
            }
        };
    }

    public void startLocationTracking() {
        if (this.myTracker != null) {
            this.myTracker.startListening();
        }
    }

    public void stopLocationTracking() {
        if (this.myTracker != null) {
            this.myTracker.stopListening();
        }
    }

    public Location getLocation() {
        return this.location_;
    }
}
