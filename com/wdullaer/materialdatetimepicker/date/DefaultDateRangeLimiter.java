package com.wdullaer.materialdatetimepicker.date;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.wdullaer.materialdatetimepicker.Utils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.TreeSet;

class DefaultDateRangeLimiter implements DateRangeLimiter {
    public static final Creator<DefaultDateRangeLimiter> CREATOR = new C13991();
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_YEAR = 1900;
    private HashSet<Calendar> disabledDays = new HashSet();
    private transient DatePickerController mController;
    private Calendar mMaxDate;
    private int mMaxYear = DEFAULT_END_YEAR;
    private Calendar mMinDate;
    private int mMinYear = DEFAULT_START_YEAR;
    private TreeSet<Calendar> selectableDays = new TreeSet();

    static class C13991 implements Creator<DefaultDateRangeLimiter> {
        C13991() {
        }

        public DefaultDateRangeLimiter createFromParcel(Parcel in) {
            return new DefaultDateRangeLimiter(in);
        }

        public DefaultDateRangeLimiter[] newArray(int size) {
            return new DefaultDateRangeLimiter[size];
        }
    }

    DefaultDateRangeLimiter() {
    }

    public DefaultDateRangeLimiter(Parcel in) {
        this.mMinYear = in.readInt();
        this.mMaxYear = in.readInt();
        this.mMinDate = (Calendar) in.readSerializable();
        this.mMaxDate = (Calendar) in.readSerializable();
        this.selectableDays = (TreeSet) in.readSerializable();
        this.disabledDays = (HashSet) in.readSerializable();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mMinYear);
        out.writeInt(this.mMaxYear);
        out.writeSerializable(this.mMinDate);
        out.writeSerializable(this.mMaxDate);
        out.writeSerializable(this.selectableDays);
        out.writeSerializable(this.disabledDays);
    }

    public int describeContents() {
        return 0;
    }

    void setSelectableDays(@NonNull Calendar[] days) {
        for (Calendar selectableDay : days) {
            Utils.trimToMidnight(selectableDay);
        }
        this.selectableDays.addAll(Arrays.asList(days));
    }

    void setDisabledDays(@NonNull Calendar[] days) {
        for (Calendar disabledDay : days) {
            Utils.trimToMidnight(disabledDay);
        }
        this.disabledDays.addAll(Arrays.asList(days));
    }

    void setMinDate(@NonNull Calendar calendar) {
        this.mMinDate = Utils.trimToMidnight((Calendar) calendar.clone());
    }

    void setMaxDate(@NonNull Calendar calendar) {
        this.mMaxDate = Utils.trimToMidnight((Calendar) calendar.clone());
    }

    void setController(@NonNull DatePickerController controller) {
        this.mController = controller;
    }

    void setYearRange(int startYear, int endYear) {
        if (endYear < startYear) {
            throw new IllegalArgumentException("Year end must be larger than or equal to year start");
        }
        this.mMinYear = startYear;
        this.mMaxYear = endYear;
    }

    @Nullable
    Calendar getMinDate() {
        return this.mMinDate;
    }

    @Nullable
    Calendar getMaxDate() {
        return this.mMaxDate;
    }

    @Nullable
    Calendar[] getSelectableDays() {
        return this.selectableDays.isEmpty() ? null : (Calendar[]) this.selectableDays.toArray(new Calendar[0]);
    }

    @Nullable
    Calendar[] getDisabledDays() {
        return this.disabledDays.isEmpty() ? null : (Calendar[]) this.disabledDays.toArray(new Calendar[0]);
    }

    public int getMinYear() {
        if (this.selectableDays.isEmpty()) {
            return (this.mMinDate == null || this.mMinDate.get(1) <= this.mMinYear) ? this.mMinYear : this.mMinDate.get(1);
        } else {
            return ((Calendar) this.selectableDays.first()).get(1);
        }
    }

    public int getMaxYear() {
        if (this.selectableDays.isEmpty()) {
            return (this.mMaxDate == null || this.mMaxDate.get(1) >= this.mMaxYear) ? this.mMaxYear : this.mMaxDate.get(1);
        } else {
            return ((Calendar) this.selectableDays.last()).get(1);
        }
    }

    @NonNull
    public Calendar getStartDate() {
        if (!this.selectableDays.isEmpty()) {
            return (Calendar) ((Calendar) this.selectableDays.first()).clone();
        }
        if (this.mMinDate != null) {
            return (Calendar) this.mMinDate.clone();
        }
        Calendar output = Calendar.getInstance(this.mController == null ? TimeZone.getDefault() : this.mController.getTimeZone());
        output.set(1, this.mMinYear);
        output.set(5, 1);
        output.set(2, 0);
        return output;
    }

    @NonNull
    public Calendar getEndDate() {
        if (!this.selectableDays.isEmpty()) {
            return (Calendar) ((Calendar) this.selectableDays.last()).clone();
        }
        if (this.mMaxDate != null) {
            return (Calendar) this.mMaxDate.clone();
        }
        Calendar output = Calendar.getInstance(this.mController == null ? TimeZone.getDefault() : this.mController.getTimeZone());
        output.set(1, this.mMaxYear);
        output.set(5, 31);
        output.set(2, 11);
        return output;
    }

    public boolean isOutOfRange(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(1, year);
        date.set(2, month);
        date.set(5, day);
        return isOutOfRange(date);
    }

    private boolean isOutOfRange(@NonNull Calendar calendar) {
        Utils.trimToMidnight(calendar);
        return isDisabled(calendar) || !isSelectable(calendar);
    }

    private boolean isDisabled(@NonNull Calendar c) {
        return this.disabledDays.contains(Utils.trimToMidnight(c)) || isBeforeMin(c) || isAfterMax(c);
    }

    private boolean isSelectable(@NonNull Calendar c) {
        return this.selectableDays.isEmpty() || this.selectableDays.contains(Utils.trimToMidnight(c));
    }

    private boolean isBeforeMin(@NonNull Calendar calendar) {
        return (this.mMinDate != null && calendar.before(this.mMinDate)) || calendar.get(1) < this.mMinYear;
    }

    private boolean isAfterMax(@NonNull Calendar calendar) {
        return (this.mMaxDate != null && calendar.after(this.mMaxDate)) || calendar.get(1) > this.mMaxYear;
    }

    @NonNull
    public Calendar setToNearestDate(@NonNull Calendar calendar) {
        if (this.selectableDays.isEmpty()) {
            if (!this.disabledDays.isEmpty()) {
                Calendar forwardDate = isBeforeMin(calendar) ? getStartDate() : (Calendar) calendar.clone();
                Calendar backwardDate = isAfterMax(calendar) ? getEndDate() : (Calendar) calendar.clone();
                while (isDisabled(forwardDate) && isDisabled(backwardDate)) {
                    forwardDate.add(5, 1);
                    backwardDate.add(5, -1);
                }
                if (!isDisabled(backwardDate)) {
                    return backwardDate;
                }
                if (!isDisabled(forwardDate)) {
                    return forwardDate;
                }
            }
            if (this.mMinDate == null || !isBeforeMin(calendar)) {
                return (this.mMaxDate == null || !isAfterMax(calendar)) ? calendar : (Calendar) this.mMaxDate.clone();
            } else {
                return (Calendar) this.mMinDate.clone();
            }
        }
        Calendar newCalendar = null;
        Calendar higher = (Calendar) this.selectableDays.ceiling(calendar);
        Calendar lower = (Calendar) this.selectableDays.lower(calendar);
        if (higher == null && lower != null) {
            newCalendar = lower;
        } else if (lower == null && higher != null) {
            newCalendar = higher;
        }
        if (newCalendar != null || higher == null) {
            if (newCalendar == null) {
                newCalendar = calendar;
            }
            newCalendar.setTimeZone(this.mController == null ? TimeZone.getDefault() : this.mController.getTimeZone());
            return (Calendar) newCalendar.clone();
        }
        if (Math.abs(calendar.getTimeInMillis() - lower.getTimeInMillis()) < Math.abs(higher.getTimeInMillis() - calendar.getTimeInMillis())) {
            return (Calendar) lower.clone();
        }
        return (Calendar) higher.clone();
    }
}
