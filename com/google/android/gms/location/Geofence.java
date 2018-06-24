package com.google.android.gms.location;

import android.os.SystemClock;
import com.google.android.gms.internal.zzarz;

public interface Geofence {
    public static final int GEOFENCE_TRANSITION_DWELL = 4;
    public static final int GEOFENCE_TRANSITION_ENTER = 1;
    public static final int GEOFENCE_TRANSITION_EXIT = 2;
    public static final long NEVER_EXPIRE = -1;

    public static final class Builder {
        private String zzOn = null;
        private int zzbja = 0;
        private long zzbjb = Long.MIN_VALUE;
        private short zzbjc = (short) -1;
        private double zzbjd;
        private double zzbje;
        private float zzbjf;
        private int zzbjg = 0;
        private int zzbjh = -1;

        public Geofence build() {
            if (this.zzOn == null) {
                throw new IllegalArgumentException("Request ID not set.");
            } else if (this.zzbja == 0) {
                throw new IllegalArgumentException("Transitions types not set.");
            } else if ((this.zzbja & 4) != 0 && this.zzbjh < 0) {
                throw new IllegalArgumentException("Non-negative loitering delay needs to be set when transition types include GEOFENCE_TRANSITION_DWELLING.");
            } else if (this.zzbjb == Long.MIN_VALUE) {
                throw new IllegalArgumentException("Expiration not set.");
            } else if (this.zzbjc == (short) -1) {
                throw new IllegalArgumentException("Geofence region not set.");
            } else if (this.zzbjg >= 0) {
                return new zzarz(this.zzOn, this.zzbja, (short) 1, this.zzbjd, this.zzbje, this.zzbjf, this.zzbjb, this.zzbjg, this.zzbjh);
            } else {
                throw new IllegalArgumentException("Notification responsiveness should be nonnegative.");
            }
        }

        public Builder setCircularRegion(double d, double d2, float f) {
            this.zzbjc = (short) 1;
            this.zzbjd = d;
            this.zzbje = d2;
            this.zzbjf = f;
            return this;
        }

        public Builder setExpirationDuration(long j) {
            if (j < 0) {
                this.zzbjb = -1;
            } else {
                this.zzbjb = SystemClock.elapsedRealtime() + j;
            }
            return this;
        }

        public Builder setLoiteringDelay(int i) {
            this.zzbjh = i;
            return this;
        }

        public Builder setNotificationResponsiveness(int i) {
            this.zzbjg = i;
            return this;
        }

        public Builder setRequestId(String str) {
            this.zzOn = str;
            return this;
        }

        public Builder setTransitionTypes(int i) {
            this.zzbja = i;
            return this;
        }
    }

    String getRequestId();
}
