package com.wdullaer.materialdatetimepicker.date;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import com.wdullaer.materialdatetimepicker.date.MonthView.OnDayClickListener;
import java.util.Calendar;
import java.util.TimeZone;

public abstract class MonthAdapter extends Adapter<MonthViewHolder> implements OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    protected static int WEEK_7_OVERHANG_HEIGHT = 7;
    protected final DatePickerController mController;
    private CalendarDay mSelectedDay;

    public static class CalendarDay {
        private Calendar calendar;
        int day;
        TimeZone mTimeZone;
        int month;
        int year;

        public CalendarDay(TimeZone timeZone) {
            this.mTimeZone = timeZone;
            setTime(System.currentTimeMillis());
        }

        public CalendarDay(long timeInMillis, TimeZone timeZone) {
            this.mTimeZone = timeZone;
            setTime(timeInMillis);
        }

        public CalendarDay(Calendar calendar, TimeZone timeZone) {
            this.mTimeZone = timeZone;
            this.year = calendar.get(1);
            this.month = calendar.get(2);
            this.day = calendar.get(5);
        }

        public CalendarDay(int year, int month, int day) {
            setDay(year, month, day);
        }

        public void set(CalendarDay date) {
            this.year = date.year;
            this.month = date.month;
            this.day = date.day;
        }

        public void setDay(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        private void setTime(long timeInMillis) {
            if (this.calendar == null) {
                this.calendar = Calendar.getInstance(this.mTimeZone);
            }
            this.calendar.setTimeInMillis(timeInMillis);
            this.month = this.calendar.get(2);
            this.year = this.calendar.get(1);
            this.day = this.calendar.get(5);
        }

        public int getYear() {
            return this.year;
        }

        public int getMonth() {
            return this.month;
        }

        public int getDay() {
            return this.day;
        }
    }

    static class MonthViewHolder extends ViewHolder {
        public MonthViewHolder(MonthView itemView) {
            super(itemView);
        }

        void bind(int position, DatePickerController mController, CalendarDay selectedCalendarDay) {
            int month = (mController.getStartDate().get(2) + position) % 12;
            int year = ((mController.getStartDate().get(2) + position) / 12) + mController.getMinYear();
            int selectedDay = -1;
            if (isSelectedDayInMonth(selectedCalendarDay, year, month)) {
                selectedDay = selectedCalendarDay.day;
            }
            ((MonthView) this.itemView).setMonthParams(selectedDay, year, month, mController.getFirstDayOfWeek());
            this.itemView.invalidate();
        }

        private boolean isSelectedDayInMonth(CalendarDay selectedDay, int year, int month) {
            return selectedDay.year == year && selectedDay.month == month;
        }
    }

    public abstract MonthView createMonthView(Context context);

    public MonthAdapter(DatePickerController controller) {
        this.mController = controller;
        init();
        setSelectedDay(this.mController.getSelectedDay());
        setHasStableIds(true);
    }

    public void setSelectedDay(CalendarDay day) {
        this.mSelectedDay = day;
        notifyDataSetChanged();
    }

    public CalendarDay getSelectedDay() {
        return this.mSelectedDay;
    }

    protected void init() {
        this.mSelectedDay = new CalendarDay(System.currentTimeMillis(), this.mController.getTimeZone());
    }

    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MonthView v = createMonthView(parent.getContext());
        v.setLayoutParams(new LayoutParams(-1, -1));
        v.setClickable(true);
        v.setOnDayClickListener(this);
        return new MonthViewHolder(v);
    }

    public void onBindViewHolder(MonthViewHolder holder, int position) {
        holder.bind(position, this.mController, this.mSelectedDay);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        Calendar endDate = this.mController.getEndDate();
        Calendar startDate = this.mController.getStartDate();
        return (((endDate.get(1) * 12) + endDate.get(2)) - ((startDate.get(1) * 12) + startDate.get(2))) + 1;
    }

    public void onDayClick(MonthView view, CalendarDay day) {
        if (day != null) {
            onDayTapped(day);
        }
    }

    protected void onDayTapped(CalendarDay day) {
        this.mController.tryVibrate();
        this.mController.onDayOfMonthSelected(day.year, day.month, day.day);
        setSelectedDay(day);
    }
}
