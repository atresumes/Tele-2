package fr.quentinklein.slt;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import com.google.android.gms.maps.model.GroundOverlayOptions;

public class TrackerSettings {
    public static final TrackerSettings DEFAULT = new TrackerSettings();
    public static final float DEFAULT_MIN_METERS_BETWEEN_UPDATES = 100.0f;
    public static final long DEFAULT_MIN_TIME_BETWEEN_UPDATES = 300000;
    public static final int DEFAULT_TIMEOUT = 60000;
    private float mMetersBetweenUpdates = GroundOverlayOptions.NO_DIMENSION;
    private long mTimeBetweenUpdates = -1;
    private int mTimeout = -1;
    private boolean mUseGPS = true;
    private boolean mUseNetwork = true;
    private boolean mUsePassive = true;

    public TrackerSettings setTimeBetweenUpdates(@FloatRange(from = 1.0d) long timeBetweenUpdates) {
        if (timeBetweenUpdates > 0) {
            this.mTimeBetweenUpdates = timeBetweenUpdates;
        }
        return this;
    }

    public long getTimeBetweenUpdates() {
        return this.mTimeBetweenUpdates <= 0 ? DEFAULT_MIN_TIME_BETWEEN_UPDATES : this.mTimeBetweenUpdates;
    }

    public TrackerSettings setMetersBetweenUpdates(@FloatRange(from = 1.0d) float metersBetweenUpdates) {
        if (metersBetweenUpdates > 0.0f) {
            this.mMetersBetweenUpdates = metersBetweenUpdates;
        }
        return this;
    }

    public float getMetersBetweenUpdates() {
        return this.mMetersBetweenUpdates <= 0.0f ? DEFAULT_MIN_METERS_BETWEEN_UPDATES : this.mMetersBetweenUpdates;
    }

    public TrackerSettings setTimeout(@IntRange(from = 1) int timeout) {
        if (timeout > 0) {
            this.mTimeout = timeout;
        }
        return this;
    }

    public int getTimeout() {
        return this.mTimeout <= -1 ? DEFAULT_TIMEOUT : this.mTimeout;
    }

    public TrackerSettings setUseGPS(boolean useGPS) {
        this.mUseGPS = useGPS;
        return this;
    }

    public boolean shouldUseGPS() {
        return this.mUseGPS;
    }

    public TrackerSettings setUseNetwork(boolean useNetwork) {
        this.mUseNetwork = useNetwork;
        return this;
    }

    public boolean shouldUseNetwork() {
        return this.mUseNetwork;
    }

    public TrackerSettings setUsePassive(boolean usePassive) {
        this.mUsePassive = usePassive;
        return this;
    }

    public boolean shouldUsePassive() {
        return this.mUsePassive;
    }
}
