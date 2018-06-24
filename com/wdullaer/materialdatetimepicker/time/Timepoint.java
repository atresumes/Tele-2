package com.wdullaer.materialdatetimepicker.time;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public class Timepoint implements Parcelable, Comparable<Timepoint> {
    public static final Creator<Timepoint> CREATOR = new C14141();
    private int hour;
    private int minute;
    private int second;

    static class C14141 implements Creator<Timepoint> {
        C14141() {
        }

        public Timepoint createFromParcel(Parcel in) {
            return new Timepoint(in);
        }

        public Timepoint[] newArray(int size) {
            return new Timepoint[size];
        }
    }

    public enum TYPE {
        HOUR,
        MINUTE,
        SECOND
    }

    public Timepoint(Timepoint time) {
        this(time.hour, time.minute, time.second);
    }

    public Timepoint(@IntRange(from = 0, to = 23) int hour, @IntRange(from = 0, to = 59) int minute, @IntRange(from = 0, to = 59) int second) {
        this.hour = hour % 24;
        this.minute = minute % 60;
        this.second = second % 60;
    }

    public Timepoint(@IntRange(from = 0, to = 23) int hour, @IntRange(from = 0, to = 59) int minute) {
        this(hour, minute, 0);
    }

    public Timepoint(@IntRange(from = 0, to = 23) int hour) {
        this(hour, 0);
    }

    public Timepoint(Parcel in) {
        this.hour = in.readInt();
        this.minute = in.readInt();
        this.second = in.readInt();
    }

    @IntRange(from = 0, to = 23)
    public int getHour() {
        return this.hour;
    }

    @IntRange(from = 0, to = 59)
    public int getMinute() {
        return this.minute;
    }

    @IntRange(from = 0, to = 59)
    public int getSecond() {
        return this.second;
    }

    public boolean isAM() {
        return this.hour < 12;
    }

    public boolean isPM() {
        return this.hour >= 12 && this.hour < 24;
    }

    public void setAM() {
        if (this.hour >= 12) {
            this.hour %= 12;
        }
    }

    public void setPM() {
        if (this.hour < 12) {
            this.hour = (this.hour + 12) % 24;
        }
    }

    public boolean equals(Object o) {
        try {
            Timepoint other = (Timepoint) o;
            if (other.getHour() == this.hour && other.getMinute() == this.minute && other.getSecond() == this.second) {
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int compareTo(@NonNull Timepoint t) {
        return (((this.hour - t.hour) * 3600) + ((this.minute - t.minute) * 60)) + (this.second - t.second);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.hour);
        out.writeInt(this.minute);
        out.writeInt(this.second);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "" + this.hour + "h " + this.minute + "m " + this.second + "s";
    }
}
