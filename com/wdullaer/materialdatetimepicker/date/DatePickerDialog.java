package com.wdullaer.materialdatetimepicker.date;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.C1395R;
import com.wdullaer.materialdatetimepicker.HapticFeedbackController;
import com.wdullaer.materialdatetimepicker.TypefaceHelper;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter.CalendarDay;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

public class DatePickerDialog extends DialogFragment implements OnClickListener, DatePickerController {
    private static final int ANIMATION_DELAY = 500;
    private static final int ANIMATION_DURATION = 300;
    private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd", Locale.getDefault());
    private static final String KEY_ACCENT = "accent";
    private static final String KEY_AUTO_DISMISS = "auto_dismiss";
    private static final String KEY_CANCEL_COLOR = "cancel_color";
    private static final String KEY_CANCEL_RESID = "cancel_resid";
    private static final String KEY_CANCEL_STRING = "cancel_string";
    private static final String KEY_CURRENT_VIEW = "current_view";
    private static final String KEY_DATERANGELIMITER = "daterangelimiter";
    private static final String KEY_DEFAULT_VIEW = "default_view";
    private static final String KEY_DISMISS = "dismiss";
    private static final String KEY_HIGHLIGHTED_DAYS = "highlighted_days";
    private static final String KEY_LIST_POSITION = "list_position";
    private static final String KEY_LIST_POSITION_OFFSET = "list_position_offset";
    private static final String KEY_OK_COLOR = "ok_color";
    private static final String KEY_OK_RESID = "ok_resid";
    private static final String KEY_OK_STRING = "ok_string";
    private static final String KEY_SELECTED_DAY = "day";
    private static final String KEY_SELECTED_MONTH = "month";
    private static final String KEY_SELECTED_YEAR = "year";
    private static final String KEY_THEME_DARK = "theme_dark";
    private static final String KEY_THEME_DARK_CHANGED = "theme_dark_changed";
    private static final String KEY_TIMEZONE = "timezone";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VERSION = "version";
    private static final String KEY_VIBRATE = "vibrate";
    private static final String KEY_WEEK_START = "week_start";
    private static final int MONTH_AND_DAY_VIEW = 0;
    private static SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMM", Locale.getDefault());
    private static final int UNINITIALIZED = -1;
    private static SimpleDateFormat VERSION_2_FORMAT = null;
    private static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
    private static final int YEAR_VIEW = 1;
    private HashSet<Calendar> highlightedDays = new HashSet();
    private int mAccentColor = -1;
    private AccessibleDateAnimator mAnimator;
    private boolean mAutoDismiss = false;
    private Calendar mCalendar = Utils.trimToMidnight(Calendar.getInstance(getTimeZone()));
    private OnDateSetListener mCallBack;
    private int mCancelColor = -1;
    private int mCancelResid = C1395R.string.mdtp_cancel;
    private String mCancelString;
    private int mCurrentView = -1;
    private TextView mDatePickerHeaderView;
    private DateRangeLimiter mDateRangeLimiter = this.mDefaultLimiter;
    private String mDayPickerDescription;
    private DayPickerView mDayPickerView;
    private DefaultDateRangeLimiter mDefaultLimiter = new DefaultDateRangeLimiter();
    private int mDefaultView = 0;
    private boolean mDelayAnimation = true;
    private boolean mDismissOnPause = false;
    private HapticFeedbackController mHapticFeedbackController;
    private HashSet<OnDateChangedListener> mListeners = new HashSet();
    private LinearLayout mMonthAndDayView;
    private int mOkColor = -1;
    private int mOkResid = C1395R.string.mdtp_ok;
    private String mOkString;
    private OnCancelListener mOnCancelListener;
    private OnDismissListener mOnDismissListener;
    private String mSelectDay;
    private String mSelectYear;
    private TextView mSelectedDayTextView;
    private TextView mSelectedMonthTextView;
    private boolean mThemeDark = false;
    private boolean mThemeDarkChanged = false;
    private TimeZone mTimezone;
    private String mTitle;
    private Version mVersion;
    private boolean mVibrate = true;
    private int mWeekStart = this.mCalendar.getFirstDayOfWeek();
    private String mYearPickerDescription;
    private YearPickerView mYearPickerView;
    private TextView mYearView;

    public interface OnDateSetListener {
        void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3);
    }

    class C13961 implements OnClickListener {
        C13961() {
        }

        public void onClick(View v) {
            DatePickerDialog.this.tryVibrate();
            DatePickerDialog.this.notifyOnDateListener();
            DatePickerDialog.this.dismiss();
        }
    }

    class C13972 implements OnClickListener {
        C13972() {
        }

        public void onClick(View v) {
            DatePickerDialog.this.tryVibrate();
            if (DatePickerDialog.this.getDialog() != null) {
                DatePickerDialog.this.getDialog().cancel();
            }
        }
    }

    protected interface OnDateChangedListener {
        void onDateChanged();
    }

    public enum Version {
        VERSION_1,
        VERSION_2
    }

    public static DatePickerDialog newInstance(OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        DatePickerDialog ret = new DatePickerDialog();
        ret.initialize(callBack, year, monthOfYear, dayOfMonth);
        return ret;
    }

    public static DatePickerDialog newInstance(OnDateSetListener callback) {
        Calendar now = Calendar.getInstance();
        return newInstance(callback, now.get(1), now.get(2), now.get(5));
    }

    public void initialize(OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        this.mCallBack = callBack;
        this.mCalendar.set(1, year);
        this.mCalendar.set(2, monthOfYear);
        this.mCalendar.set(5, dayOfMonth);
        this.mVersion = VERSION.SDK_INT < 23 ? Version.VERSION_1 : Version.VERSION_2;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.getWindow().setSoftInputMode(3);
        this.mCurrentView = -1;
        if (savedInstanceState != null) {
            this.mCalendar.set(1, savedInstanceState.getInt(KEY_SELECTED_YEAR));
            this.mCalendar.set(2, savedInstanceState.getInt(KEY_SELECTED_MONTH));
            this.mCalendar.set(5, savedInstanceState.getInt(KEY_SELECTED_DAY));
            this.mDefaultView = savedInstanceState.getInt(KEY_DEFAULT_VIEW);
        }
        if (VERSION.SDK_INT < 18) {
            VERSION_2_FORMAT = new SimpleDateFormat(activity.getResources().getString(C1395R.string.mdtp_date_v2_daymonthyear), Locale.getDefault());
        } else {
            VERSION_2_FORMAT = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEMMMdd"), Locale.getDefault());
        }
        VERSION_2_FORMAT.setTimeZone(getTimeZone());
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_YEAR, this.mCalendar.get(1));
        outState.putInt(KEY_SELECTED_MONTH, this.mCalendar.get(2));
        outState.putInt(KEY_SELECTED_DAY, this.mCalendar.get(5));
        outState.putInt(KEY_WEEK_START, this.mWeekStart);
        outState.putInt(KEY_CURRENT_VIEW, this.mCurrentView);
        int listPosition = -1;
        if (this.mCurrentView == 0) {
            listPosition = this.mDayPickerView.getMostVisiblePosition();
        } else if (this.mCurrentView == 1) {
            listPosition = this.mYearPickerView.getFirstVisiblePosition();
            outState.putInt(KEY_LIST_POSITION_OFFSET, this.mYearPickerView.getFirstPositionOffset());
        }
        outState.putInt(KEY_LIST_POSITION, listPosition);
        outState.putSerializable(KEY_HIGHLIGHTED_DAYS, this.highlightedDays);
        outState.putBoolean(KEY_THEME_DARK, this.mThemeDark);
        outState.putBoolean(KEY_THEME_DARK_CHANGED, this.mThemeDarkChanged);
        outState.putInt(KEY_ACCENT, this.mAccentColor);
        outState.putBoolean(KEY_VIBRATE, this.mVibrate);
        outState.putBoolean(KEY_DISMISS, this.mDismissOnPause);
        outState.putBoolean(KEY_AUTO_DISMISS, this.mAutoDismiss);
        outState.putInt(KEY_DEFAULT_VIEW, this.mDefaultView);
        outState.putString("title", this.mTitle);
        outState.putInt(KEY_OK_RESID, this.mOkResid);
        outState.putString(KEY_OK_STRING, this.mOkString);
        outState.putInt(KEY_OK_COLOR, this.mOkColor);
        outState.putInt(KEY_CANCEL_RESID, this.mCancelResid);
        outState.putString(KEY_CANCEL_STRING, this.mCancelString);
        outState.putInt(KEY_CANCEL_COLOR, this.mCancelColor);
        outState.putSerializable("version", this.mVersion);
        outState.putSerializable(KEY_TIMEZONE, this.mTimezone);
        outState.putParcelable(KEY_DATERANGELIMITER, this.mDateRangeLimiter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int listPosition = -1;
        int listPositionOffset = 0;
        int currentView = this.mDefaultView;
        if (savedInstanceState != null) {
            this.mWeekStart = savedInstanceState.getInt(KEY_WEEK_START);
            currentView = savedInstanceState.getInt(KEY_CURRENT_VIEW);
            listPosition = savedInstanceState.getInt(KEY_LIST_POSITION);
            listPositionOffset = savedInstanceState.getInt(KEY_LIST_POSITION_OFFSET);
            this.highlightedDays = (HashSet) savedInstanceState.getSerializable(KEY_HIGHLIGHTED_DAYS);
            this.mThemeDark = savedInstanceState.getBoolean(KEY_THEME_DARK);
            this.mThemeDarkChanged = savedInstanceState.getBoolean(KEY_THEME_DARK_CHANGED);
            this.mAccentColor = savedInstanceState.getInt(KEY_ACCENT);
            this.mVibrate = savedInstanceState.getBoolean(KEY_VIBRATE);
            this.mDismissOnPause = savedInstanceState.getBoolean(KEY_DISMISS);
            this.mAutoDismiss = savedInstanceState.getBoolean(KEY_AUTO_DISMISS);
            this.mTitle = savedInstanceState.getString("title");
            this.mOkResid = savedInstanceState.getInt(KEY_OK_RESID);
            this.mOkString = savedInstanceState.getString(KEY_OK_STRING);
            this.mOkColor = savedInstanceState.getInt(KEY_OK_COLOR);
            this.mCancelResid = savedInstanceState.getInt(KEY_CANCEL_RESID);
            this.mCancelString = savedInstanceState.getString(KEY_CANCEL_STRING);
            this.mCancelColor = savedInstanceState.getInt(KEY_CANCEL_COLOR);
            this.mVersion = (Version) savedInstanceState.getSerializable("version");
            this.mTimezone = (TimeZone) savedInstanceState.getSerializable(KEY_TIMEZONE);
            this.mDateRangeLimiter = (DateRangeLimiter) savedInstanceState.getParcelable(KEY_DATERANGELIMITER);
            if (this.mDateRangeLimiter instanceof DefaultDateRangeLimiter) {
                this.mDefaultLimiter = (DefaultDateRangeLimiter) this.mDateRangeLimiter;
            } else {
                this.mDefaultLimiter = new DefaultDateRangeLimiter();
            }
        }
        this.mDefaultLimiter.setController(this);
        View view = inflater.inflate(this.mVersion == Version.VERSION_1 ? C1395R.layout.mdtp_date_picker_dialog : C1395R.layout.mdtp_date_picker_dialog_v2, container, false);
        this.mCalendar = this.mDateRangeLimiter.setToNearestDate(this.mCalendar);
        this.mDatePickerHeaderView = (TextView) view.findViewById(C1395R.id.mdtp_date_picker_header);
        this.mMonthAndDayView = (LinearLayout) view.findViewById(C1395R.id.mdtp_date_picker_month_and_day);
        this.mMonthAndDayView.setOnClickListener(this);
        this.mSelectedMonthTextView = (TextView) view.findViewById(C1395R.id.mdtp_date_picker_month);
        this.mSelectedDayTextView = (TextView) view.findViewById(C1395R.id.mdtp_date_picker_day);
        this.mYearView = (TextView) view.findViewById(C1395R.id.mdtp_date_picker_year);
        this.mYearView.setOnClickListener(this);
        Context activity = getActivity();
        this.mDayPickerView = new SimpleDayPickerView(activity, (DatePickerController) this);
        this.mYearPickerView = new YearPickerView(activity, this);
        if (!this.mThemeDarkChanged) {
            this.mThemeDark = Utils.isDarkTheme(activity, this.mThemeDark);
        }
        Resources res = getResources();
        this.mDayPickerDescription = res.getString(C1395R.string.mdtp_day_picker_description);
        this.mSelectDay = res.getString(C1395R.string.mdtp_select_day);
        this.mYearPickerDescription = res.getString(C1395R.string.mdtp_year_picker_description);
        this.mSelectYear = res.getString(C1395R.string.mdtp_select_year);
        view.setBackgroundColor(ContextCompat.getColor(activity, this.mThemeDark ? C1395R.color.mdtp_date_picker_view_animator_dark_theme : C1395R.color.mdtp_date_picker_view_animator));
        this.mAnimator = (AccessibleDateAnimator) view.findViewById(C1395R.id.mdtp_animator);
        this.mAnimator.addView(this.mDayPickerView);
        this.mAnimator.addView(this.mYearPickerView);
        this.mAnimator.setDateMillis(this.mCalendar.getTimeInMillis());
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        this.mAnimator.setInAnimation(animation);
        Animation animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(300);
        this.mAnimator.setOutAnimation(animation2);
        Button okButton = (Button) view.findViewById(C1395R.id.mdtp_ok);
        okButton.setOnClickListener(new C13961());
        okButton.setTypeface(TypefaceHelper.get(activity, "Roboto-Medium"));
        if (this.mOkString != null) {
            okButton.setText(this.mOkString);
        } else {
            okButton.setText(this.mOkResid);
        }
        Button cancelButton = (Button) view.findViewById(C1395R.id.mdtp_cancel);
        cancelButton.setOnClickListener(new C13972());
        cancelButton.setTypeface(TypefaceHelper.get(activity, "Roboto-Medium"));
        if (this.mCancelString != null) {
            cancelButton.setText(this.mCancelString);
        } else {
            cancelButton.setText(this.mCancelResid);
        }
        cancelButton.setVisibility(isCancelable() ? 0 : 8);
        if (this.mAccentColor == -1) {
            this.mAccentColor = Utils.getAccentColorFromThemeIfAvailable(getActivity());
        }
        if (this.mDatePickerHeaderView != null) {
            this.mDatePickerHeaderView.setBackgroundColor(Utils.darkenColor(this.mAccentColor));
        }
        view.findViewById(C1395R.id.mdtp_day_picker_selected_date_layout).setBackgroundColor(this.mAccentColor);
        if (this.mOkColor != -1) {
            okButton.setTextColor(this.mOkColor);
        } else {
            okButton.setTextColor(this.mAccentColor);
        }
        if (this.mCancelColor != -1) {
            cancelButton.setTextColor(this.mCancelColor);
        } else {
            cancelButton.setTextColor(this.mAccentColor);
        }
        if (getDialog() == null) {
            view.findViewById(C1395R.id.mdtp_done_background).setVisibility(8);
        }
        updateDisplay(false);
        setCurrentView(currentView);
        if (listPosition != -1) {
            if (currentView == 0) {
                this.mDayPickerView.postSetSelection(listPosition);
            } else if (currentView == 1) {
                this.mYearPickerView.postSetSelectionFromTop(listPosition, listPositionOffset);
            }
        }
        this.mHapticFeedbackController = new HapticFeedbackController(activity);
        return view;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup viewGroup = (ViewGroup) getView();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
            viewGroup.addView(onCreateView(getActivity().getLayoutInflater(), viewGroup, null));
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(1);
        return dialog;
    }

    public void onResume() {
        super.onResume();
        this.mHapticFeedbackController.start();
    }

    public void onPause() {
        super.onPause();
        this.mHapticFeedbackController.stop();
        if (this.mDismissOnPause) {
            dismiss();
        }
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (this.mOnCancelListener != null) {
            this.mOnCancelListener.onCancel(dialog);
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss(dialog);
        }
    }

    private void setCurrentView(int viewIndex) {
        long millis = this.mCalendar.getTimeInMillis();
        ObjectAnimator pulseAnimator;
        switch (viewIndex) {
            case 0:
                if (this.mVersion == Version.VERSION_1) {
                    pulseAnimator = Utils.getPulseAnimator(this.mMonthAndDayView, 0.9f, 1.05f);
                    if (this.mDelayAnimation) {
                        pulseAnimator.setStartDelay(500);
                        this.mDelayAnimation = false;
                    }
                    this.mDayPickerView.onDateChanged();
                    if (this.mCurrentView != viewIndex) {
                        this.mMonthAndDayView.setSelected(true);
                        this.mYearView.setSelected(false);
                        this.mAnimator.setDisplayedChild(0);
                        this.mCurrentView = viewIndex;
                    }
                    pulseAnimator.start();
                } else {
                    this.mDayPickerView.onDateChanged();
                    if (this.mCurrentView != viewIndex) {
                        this.mMonthAndDayView.setSelected(true);
                        this.mYearView.setSelected(false);
                        this.mAnimator.setDisplayedChild(0);
                        this.mCurrentView = viewIndex;
                    }
                }
                this.mAnimator.setContentDescription(this.mDayPickerDescription + ": " + DateUtils.formatDateTime(getActivity(), millis, 16));
                Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectDay);
                return;
            case 1:
                if (this.mVersion == Version.VERSION_1) {
                    pulseAnimator = Utils.getPulseAnimator(this.mYearView, 0.85f, 1.1f);
                    if (this.mDelayAnimation) {
                        pulseAnimator.setStartDelay(500);
                        this.mDelayAnimation = false;
                    }
                    this.mYearPickerView.onDateChanged();
                    if (this.mCurrentView != viewIndex) {
                        this.mMonthAndDayView.setSelected(false);
                        this.mYearView.setSelected(true);
                        this.mAnimator.setDisplayedChild(1);
                        this.mCurrentView = viewIndex;
                    }
                    pulseAnimator.start();
                } else {
                    this.mYearPickerView.onDateChanged();
                    if (this.mCurrentView != viewIndex) {
                        this.mMonthAndDayView.setSelected(false);
                        this.mYearView.setSelected(true);
                        this.mAnimator.setDisplayedChild(1);
                        this.mCurrentView = viewIndex;
                    }
                }
                this.mAnimator.setContentDescription(this.mYearPickerDescription + ": " + YEAR_FORMAT.format(Long.valueOf(millis)));
                Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectYear);
                return;
            default:
                return;
        }
    }

    private void updateDisplay(boolean announce) {
        this.mYearView.setText(YEAR_FORMAT.format(this.mCalendar.getTime()));
        if (this.mVersion == Version.VERSION_1) {
            if (this.mDatePickerHeaderView != null) {
                if (this.mTitle != null) {
                    this.mDatePickerHeaderView.setText(this.mTitle.toUpperCase(Locale.getDefault()));
                } else {
                    this.mDatePickerHeaderView.setText(this.mCalendar.getDisplayName(7, 2, Locale.getDefault()).toUpperCase(Locale.getDefault()));
                }
            }
            this.mSelectedMonthTextView.setText(MONTH_FORMAT.format(this.mCalendar.getTime()));
            this.mSelectedDayTextView.setText(DAY_FORMAT.format(this.mCalendar.getTime()));
        }
        if (this.mVersion == Version.VERSION_2) {
            this.mSelectedDayTextView.setText(VERSION_2_FORMAT.format(this.mCalendar.getTime()));
            if (this.mTitle != null) {
                this.mDatePickerHeaderView.setText(this.mTitle.toUpperCase(Locale.getDefault()));
            } else {
                this.mDatePickerHeaderView.setVisibility(8);
            }
        }
        long millis = this.mCalendar.getTimeInMillis();
        this.mAnimator.setDateMillis(millis);
        this.mMonthAndDayView.setContentDescription(DateUtils.formatDateTime(getActivity(), millis, 24));
        if (announce) {
            Utils.tryAccessibilityAnnounce(this.mAnimator, DateUtils.formatDateTime(getActivity(), millis, 20));
        }
    }

    public void vibrate(boolean vibrate) {
        this.mVibrate = vibrate;
    }

    public void dismissOnPause(boolean dismissOnPause) {
        this.mDismissOnPause = dismissOnPause;
    }

    public void autoDismiss(boolean autoDismiss) {
        this.mAutoDismiss = autoDismiss;
    }

    public void setThemeDark(boolean themeDark) {
        this.mThemeDark = themeDark;
        this.mThemeDarkChanged = true;
    }

    public boolean isThemeDark() {
        return this.mThemeDark;
    }

    public void setAccentColor(String color) {
        this.mAccentColor = Color.parseColor(color);
    }

    public void setAccentColor(@ColorInt int color) {
        this.mAccentColor = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
    }

    public void setOkColor(String color) {
        this.mOkColor = Color.parseColor(color);
    }

    public void setOkColor(@ColorInt int color) {
        this.mOkColor = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
    }

    public void setCancelColor(String color) {
        this.mCancelColor = Color.parseColor(color);
    }

    public void setCancelColor(@ColorInt int color) {
        this.mCancelColor = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
    }

    public int getAccentColor() {
        return this.mAccentColor;
    }

    public void showYearPickerFirst(boolean yearPicker) {
        this.mDefaultView = yearPicker ? 1 : 0;
    }

    public void setFirstDayOfWeek(int startOfWeek) {
        if (startOfWeek < 1 || startOfWeek > 7) {
            throw new IllegalArgumentException("Value must be between Calendar.SUNDAY and Calendar.SATURDAY");
        }
        this.mWeekStart = startOfWeek;
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public void setYearRange(int startYear, int endYear) {
        this.mDefaultLimiter.setYearRange(startYear, endYear);
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public void setMinDate(Calendar calendar) {
        this.mDefaultLimiter.setMinDate(calendar);
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public Calendar getMinDate() {
        return this.mDefaultLimiter.getMinDate();
    }

    public void setMaxDate(Calendar calendar) {
        this.mDefaultLimiter.setMaxDate(calendar);
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public Calendar getMaxDate() {
        return this.mDefaultLimiter.getMaxDate();
    }

    public void setHighlightedDays(Calendar[] highlightedDays) {
        for (Calendar highlightedDay : highlightedDays) {
            Utils.trimToMidnight(highlightedDay);
        }
        this.highlightedDays.addAll(Arrays.asList(highlightedDays));
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public Calendar[] getHighlightedDays() {
        if (this.highlightedDays.isEmpty()) {
            return null;
        }
        Calendar[] output = (Calendar[]) this.highlightedDays.toArray(new Calendar[0]);
        Arrays.sort(output);
        return output;
    }

    public boolean isHighlighted(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(1, year);
        date.set(2, month);
        date.set(5, day);
        Utils.trimToMidnight(date);
        return this.highlightedDays.contains(date);
    }

    public void setSelectableDays(Calendar[] selectableDays) {
        this.mDefaultLimiter.setSelectableDays(selectableDays);
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public Calendar[] getSelectableDays() {
        return this.mDefaultLimiter.getSelectableDays();
    }

    public void setDisabledDays(Calendar[] disabledDays) {
        this.mDefaultLimiter.setDisabledDays(disabledDays);
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public Calendar[] getDisabledDays() {
        return this.mDefaultLimiter.getDisabledDays();
    }

    public void setDateRangeLimiter(DateRangeLimiter dateRangeLimiter) {
        this.mDateRangeLimiter = dateRangeLimiter;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setOkText(String okString) {
        this.mOkString = okString;
    }

    public void setOkText(@StringRes int okResid) {
        this.mOkString = null;
        this.mOkResid = okResid;
    }

    public void setCancelText(String cancelString) {
        this.mCancelString = cancelString;
    }

    public void setCancelText(@StringRes int cancelResid) {
        this.mCancelString = null;
        this.mCancelResid = cancelResid;
    }

    public void setVersion(Version version) {
        this.mVersion = version;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.mTimezone = timeZone;
        this.mCalendar.setTimeZone(timeZone);
        YEAR_FORMAT.setTimeZone(timeZone);
        MONTH_FORMAT.setTimeZone(timeZone);
        DAY_FORMAT.setTimeZone(timeZone);
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        this.mCallBack = listener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    private Calendar adjustDayInMonthIfNeeded(Calendar calendar) {
        int day = calendar.get(5);
        int daysInMonth = calendar.getActualMaximum(5);
        if (day > daysInMonth) {
            calendar.set(5, daysInMonth);
        }
        return this.mDateRangeLimiter.setToNearestDate(calendar);
    }

    public void onClick(View v) {
        tryVibrate();
        if (v.getId() == C1395R.id.mdtp_date_picker_year) {
            setCurrentView(1);
        } else if (v.getId() == C1395R.id.mdtp_date_picker_month_and_day) {
            setCurrentView(0);
        }
    }

    public void onYearSelected(int year) {
        this.mCalendar.set(1, year);
        this.mCalendar = adjustDayInMonthIfNeeded(this.mCalendar);
        updatePickers();
        setCurrentView(0);
        updateDisplay(true);
    }

    public void onDayOfMonthSelected(int year, int month, int day) {
        this.mCalendar.set(1, year);
        this.mCalendar.set(2, month);
        this.mCalendar.set(5, day);
        updatePickers();
        updateDisplay(true);
        if (this.mAutoDismiss) {
            notifyOnDateListener();
            dismiss();
        }
    }

    private void updatePickers() {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((OnDateChangedListener) it.next()).onDateChanged();
        }
    }

    public CalendarDay getSelectedDay() {
        return new CalendarDay(this.mCalendar, getTimeZone());
    }

    public Calendar getStartDate() {
        return this.mDateRangeLimiter.getStartDate();
    }

    public Calendar getEndDate() {
        return this.mDateRangeLimiter.getEndDate();
    }

    public int getMinYear() {
        return this.mDateRangeLimiter.getMinYear();
    }

    public int getMaxYear() {
        return this.mDateRangeLimiter.getMaxYear();
    }

    public boolean isOutOfRange(int year, int month, int day) {
        return this.mDateRangeLimiter.isOutOfRange(year, month, day);
    }

    public int getFirstDayOfWeek() {
        return this.mWeekStart;
    }

    public void registerOnDateChangedListener(OnDateChangedListener listener) {
        this.mListeners.add(listener);
    }

    public void unregisterOnDateChangedListener(OnDateChangedListener listener) {
        this.mListeners.remove(listener);
    }

    public void tryVibrate() {
        if (this.mVibrate) {
            this.mHapticFeedbackController.tryVibrate();
        }
    }

    public TimeZone getTimeZone() {
        return this.mTimezone == null ? TimeZone.getDefault() : this.mTimezone;
    }

    public void notifyOnDateListener() {
        if (this.mCallBack != null) {
            this.mCallBack.onDateSet(this, this.mCalendar.get(1), this.mCalendar.get(2), this.mCalendar.get(5));
        }
    }
}
