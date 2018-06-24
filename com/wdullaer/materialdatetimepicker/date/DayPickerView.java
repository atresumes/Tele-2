package com.wdullaer.materialdatetimepicker.date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import com.wdullaer.materialdatetimepicker.GravitySnapHelper;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter.CalendarDay;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class DayPickerView extends RecyclerView implements OnDateChangedListener {
    public static final int DAYS_PER_WEEK = 7;
    protected static final int SCROLL_HYST_WEEKS = 2;
    private static final String TAG = "MonthFragment";
    private static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
    private LinearLayoutManager linearLayoutManager;
    protected MonthAdapter mAdapter;
    protected Context mContext;
    private DatePickerController mController;
    protected int mCurrentMonthDisplayed;
    protected int mDaysPerWeek = 7;
    protected int mFirstDayOfWeek;
    protected Handler mHandler;
    protected int mNumWeeks = 6;
    protected CharSequence mPrevMonthName;
    protected long mPreviousScrollPosition;
    protected int mPreviousScrollState = 0;
    protected CalendarDay mSelectedDay;
    protected boolean mShowWeekNumber = false;
    protected CalendarDay mTempDay;

    public abstract MonthAdapter createMonthAdapter(DatePickerController datePickerController);

    public DayPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DayPickerView(Context context, DatePickerController controller) {
        super(context);
        init(context);
        setController(controller);
    }

    public void setController(DatePickerController controller) {
        this.mController = controller;
        this.mController.registerOnDateChangedListener(this);
        this.mSelectedDay = new CalendarDay(this.mController.getTimeZone());
        this.mTempDay = new CalendarDay(this.mController.getTimeZone());
        refreshAdapter();
        onDateChanged();
    }

    public void init(Context context) {
        this.linearLayoutManager = new LinearLayoutManager(context, 1, false);
        setLayoutManager(this.linearLayoutManager);
        this.mHandler = new Handler();
        setLayoutParams(new LayoutParams(-1, -1));
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        this.mContext = context;
        setUpRecyclerView();
    }

    public void setScrollOrientation(int orientation) {
        this.linearLayoutManager.setOrientation(orientation);
    }

    protected void setUpRecyclerView() {
        setVerticalScrollBarEnabled(false);
        setFadingEdgeLength(0);
        new GravitySnapHelper(48).attachToRecyclerView(this);
    }

    public void onChange() {
        refreshAdapter();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        restoreAccessibilityFocus(findAccessibilityFocus());
    }

    protected void refreshAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = createMonthAdapter(this.mController);
        } else {
            this.mAdapter.setSelectedDay(this.mSelectedDay);
        }
        setAdapter(this.mAdapter);
    }

    public boolean goTo(CalendarDay day, boolean animate, boolean setSelected, boolean forceScroll) {
        View child;
        if (setSelected) {
            this.mSelectedDay.set(day);
        }
        this.mTempDay.set(day);
        int position = (((day.year - this.mController.getMinYear()) * 12) + day.month) - this.mController.getStartDate().get(2);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            child = getChildAt(i);
            if (child != null) {
                int top = child.getTop();
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "child at " + (i2 - 1) + " has top " + top);
                }
                if (top >= 0) {
                    break;
                }
                i = i2;
            } else {
                break;
            }
        }
        int selectedPosition = child != null ? getChildAdapterPosition(child) : 0;
        if (setSelected) {
            this.mAdapter.setSelectedDay(this.mSelectedDay);
        }
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "GoTo position " + position);
        }
        if (position != selectedPosition || forceScroll) {
            setMonthDisplayed(this.mTempDay);
            this.mPreviousScrollState = 1;
            if (animate) {
                smoothScrollToPosition(position);
                return true;
            }
            postSetSelection(position);
        } else if (setSelected) {
            setMonthDisplayed(this.mSelectedDay);
        }
        return false;
    }

    public void postSetSelection(final int position) {
        clearFocus();
        post(new Runnable() {
            public void run() {
                ((LinearLayoutManager) DayPickerView.this.getLayoutManager()).scrollToPositionWithOffset(position, 0);
            }
        });
    }

    protected void setMonthDisplayed(CalendarDay date) {
        this.mCurrentMonthDisplayed = date.month;
    }

    public int getMostVisiblePosition() {
        return getChildAdapterPosition(getMostVisibleMonth());
    }

    public MonthView getMostVisibleMonth() {
        boolean verticalScroll = true;
        if (((LinearLayoutManager) getLayoutManager()).getOrientation() != 1) {
            verticalScroll = false;
        }
        int maxSize = verticalScroll ? getHeight() : getWidth();
        int maxDisplayedSize = 0;
        int i = 0;
        int size = 0;
        MonthView mostVisibleMonth = null;
        while (size < maxSize) {
            View child = getChildAt(i);
            if (child == null) {
                break;
            }
            size = verticalScroll ? child.getBottom() : getRight();
            int displayedSize = Math.min(size, maxSize) - Math.max(0, child.getTop());
            if (displayedSize > maxDisplayedSize) {
                mostVisibleMonth = (MonthView) child;
                maxDisplayedSize = displayedSize;
            }
            i++;
        }
        return mostVisibleMonth;
    }

    public void onDateChanged() {
        goTo(this.mController.getSelectedDay(), false, true, true);
    }

    private CalendarDay findAccessibilityFocus() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof MonthView) {
                CalendarDay focus = ((MonthView) child).getAccessibilityFocus();
                if (focus != null) {
                    if (VERSION.SDK_INT != 17) {
                        return focus;
                    }
                    ((MonthView) child).clearAccessibilityFocus();
                    return focus;
                }
            }
        }
        return null;
    }

    private boolean restoreAccessibilityFocus(CalendarDay day) {
        if (day == null) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if ((child instanceof MonthView) && ((MonthView) child).restoreAccessibilityFocus(day)) {
                return true;
            }
        }
        return false;
    }

    public void onInitializeAccessibilityEvent(@NonNull AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setItemCount(-1);
    }

    private static String getMonthAndYearString(CalendarDay day) {
        Calendar cal = Calendar.getInstance();
        cal.set(day.year, day.month, day.day);
        return (("" + cal.getDisplayName(2, 2, Locale.getDefault())) + " ") + YEAR_FORMAT.format(cal.getTime());
    }

    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (VERSION.SDK_INT >= 21) {
            info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
            info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
            return;
        }
        info.addAction(4096);
        info.addAction(8192);
    }

    @SuppressLint({"NewApi"})
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if (action != 4096 && action != 8192) {
            return super.performAccessibilityAction(action, arguments);
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int minMonth = this.mController.getStartDate().get(2);
        CalendarDay day = new CalendarDay(((firstVisiblePosition + minMonth) / 12) + this.mController.getMinYear(), (firstVisiblePosition + minMonth) % 12, 1);
        if (action == 4096) {
            day.month++;
            if (day.month == 12) {
                day.month = 0;
                day.year++;
            }
        } else if (action == 8192) {
            View firstVisibleView = getChildAt(0);
            if (firstVisibleView != null && firstVisibleView.getTop() >= -1) {
                day.month--;
                if (day.month == -1) {
                    day.month = 11;
                    day.year--;
                }
            }
        }
        Utils.tryAccessibilityAnnounce(this, getMonthAndYearString(day));
        goTo(day, true, false, true);
        return true;
    }

    private int getFirstVisiblePosition() {
        return getChildAdapterPosition(getChildAt(0));
    }
}
