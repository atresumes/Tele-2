package com.wdullaer.materialdatetimepicker.time;

import android.animation.ObjectAnimator;
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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.C1395R;
import com.wdullaer.materialdatetimepicker.HapticFeedbackController;
import com.wdullaer.materialdatetimepicker.TypefaceHelper;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout.OnValueSelectedListener;
import com.wdullaer.materialdatetimepicker.time.Timepoint.TYPE;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TimePickerDialog extends DialogFragment implements OnValueSelectedListener, TimePickerController {
    public static final int AM = 0;
    public static final int HOUR_INDEX = 0;
    private static final String KEY_ACCENT = "accent";
    private static final String KEY_CANCEL_COLOR = "cancel_color";
    private static final String KEY_CANCEL_RESID = "cancel_resid";
    private static final String KEY_CANCEL_STRING = "cancel_string";
    private static final String KEY_CURRENT_ITEM_SHOWING = "current_item_showing";
    private static final String KEY_DISMISS = "dismiss";
    private static final String KEY_ENABLE_MINUTES = "enable_minutes";
    private static final String KEY_ENABLE_SECONDS = "enable_seconds";
    private static final String KEY_INITIAL_TIME = "initial_time";
    private static final String KEY_IN_KB_MODE = "in_kb_mode";
    private static final String KEY_IS_24_HOUR_VIEW = "is_24_hour_view";
    private static final String KEY_MAX_TIME = "max_time";
    private static final String KEY_MIN_TIME = "min_time";
    private static final String KEY_OK_COLOR = "ok_color";
    private static final String KEY_OK_RESID = "ok_resid";
    private static final String KEY_OK_STRING = "ok_string";
    private static final String KEY_SELECTABLE_TIMES = "selectable_times";
    private static final String KEY_THEME_DARK = "theme_dark";
    private static final String KEY_THEME_DARK_CHANGED = "theme_dark_changed";
    private static final String KEY_TITLE = "dialog_title";
    private static final String KEY_TYPED_TIMES = "typed_times";
    private static final String KEY_VERSION = "version";
    private static final String KEY_VIBRATE = "vibrate";
    public static final int MINUTE_INDEX = 1;
    public static final int PM = 1;
    private static final int PULSE_ANIMATOR_DELAY = 300;
    public static final int SECOND_INDEX = 2;
    private static final String TAG = "TimePickerDialog";
    private int mAccentColor = -1;
    private boolean mAllowAutoAdvance;
    private int mAmKeyCode;
    private View mAmPmLayout;
    private String mAmText;
    private TextView mAmTextView;
    private OnTimeSetListener mCallback;
    private Button mCancelButton;
    private int mCancelColor;
    private int mCancelResid;
    private String mCancelString;
    private String mDeletedKeyFormat;
    private boolean mDismissOnPause;
    private String mDoublePlaceholderText;
    private boolean mEnableMinutes;
    private boolean mEnableSeconds;
    private HapticFeedbackController mHapticFeedbackController;
    private String mHourPickerDescription;
    private TextView mHourSpaceView;
    private TextView mHourView;
    private boolean mInKbMode;
    private Timepoint mInitialTime;
    private boolean mIs24HourMode;
    private Node mLegalTimesTree;
    private Timepoint mMaxTime;
    private Timepoint mMinTime;
    private String mMinutePickerDescription;
    private TextView mMinuteSpaceView;
    private TextView mMinuteView;
    private Button mOkButton;
    private int mOkColor;
    private int mOkResid;
    private String mOkString;
    private OnCancelListener mOnCancelListener;
    private OnDismissListener mOnDismissListener;
    private char mPlaceholderText;
    private int mPmKeyCode;
    private String mPmText;
    private TextView mPmTextView;
    private String mSecondPickerDescription;
    private TextView mSecondSpaceView;
    private TextView mSecondView;
    private String mSelectHours;
    private String mSelectMinutes;
    private String mSelectSeconds;
    private Timepoint[] mSelectableTimes;
    private int mSelectedColor;
    private boolean mThemeDark;
    private boolean mThemeDarkChanged;
    private RadialPickerLayout mTimePicker;
    private String mTitle;
    private ArrayList<Integer> mTypedTimes;
    private int mUnselectedColor;
    private Version mVersion;
    private boolean mVibrate;

    public interface OnTimeSetListener {
        void onTimeSet(TimePickerDialog timePickerDialog, int i, int i2, int i3);
    }

    class C14081 implements OnClickListener {
        C14081() {
        }

        public void onClick(View v) {
            TimePickerDialog.this.setCurrentItemShowing(0, true, false, true);
            TimePickerDialog.this.tryVibrate();
        }
    }

    class C14092 implements OnClickListener {
        C14092() {
        }

        public void onClick(View v) {
            TimePickerDialog.this.setCurrentItemShowing(1, true, false, true);
            TimePickerDialog.this.tryVibrate();
        }
    }

    class C14103 implements OnClickListener {
        C14103() {
        }

        public void onClick(View view) {
            TimePickerDialog.this.setCurrentItemShowing(2, true, false, true);
            TimePickerDialog.this.tryVibrate();
        }
    }

    class C14114 implements OnClickListener {
        C14114() {
        }

        public void onClick(View v) {
            if (TimePickerDialog.this.mInKbMode && TimePickerDialog.this.isTypedTimeFullyLegal()) {
                TimePickerDialog.this.finishKbMode(false);
            } else {
                TimePickerDialog.this.tryVibrate();
            }
            TimePickerDialog.this.notifyOnDateListener();
            TimePickerDialog.this.dismiss();
        }
    }

    class C14125 implements OnClickListener {
        C14125() {
        }

        public void onClick(View v) {
            TimePickerDialog.this.tryVibrate();
            if (TimePickerDialog.this.getDialog() != null) {
                TimePickerDialog.this.getDialog().cancel();
            }
        }
    }

    class C14136 implements OnClickListener {
        C14136() {
        }

        public void onClick(View v) {
            if (!TimePickerDialog.this.isAmDisabled() && !TimePickerDialog.this.isPmDisabled()) {
                TimePickerDialog.this.tryVibrate();
                int amOrPm = TimePickerDialog.this.mTimePicker.getIsCurrentlyAmOrPm();
                if (amOrPm == 0) {
                    amOrPm = 1;
                } else if (amOrPm == 1) {
                    amOrPm = 0;
                }
                TimePickerDialog.this.mTimePicker.setAmOrPm(amOrPm);
            }
        }
    }

    private class KeyboardListener implements OnKeyListener {
        private KeyboardListener() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == 1) {
                return TimePickerDialog.this.processKeyUp(keyCode);
            }
            return false;
        }
    }

    private static class Node {
        private ArrayList<Node> mChildren = new ArrayList();
        private int[] mLegalKeys;

        public Node(int... legalKeys) {
            this.mLegalKeys = legalKeys;
        }

        public void addChild(Node child) {
            this.mChildren.add(child);
        }

        public boolean containsKey(int key) {
            for (int legalKey : this.mLegalKeys) {
                if (legalKey == key) {
                    return true;
                }
            }
            return false;
        }

        public Node canReach(int key) {
            if (this.mChildren == null) {
                return null;
            }
            Iterator it = this.mChildren.iterator();
            while (it.hasNext()) {
                Node child = (Node) it.next();
                if (child.containsKey(key)) {
                    return child;
                }
            }
            return null;
        }
    }

    public enum Version {
        VERSION_1,
        VERSION_2
    }

    public static TimePickerDialog newInstance(OnTimeSetListener callback, int hourOfDay, int minute, int second, boolean is24HourMode) {
        TimePickerDialog ret = new TimePickerDialog();
        ret.initialize(callback, hourOfDay, minute, second, is24HourMode);
        return ret;
    }

    public static TimePickerDialog newInstance(OnTimeSetListener callback, int hourOfDay, int minute, boolean is24HourMode) {
        return newInstance(callback, hourOfDay, minute, 0, is24HourMode);
    }

    public static TimePickerDialog newInstance(OnTimeSetListener callback, boolean is24HourMode) {
        Calendar now = Calendar.getInstance();
        return newInstance(callback, now.get(11), now.get(12), is24HourMode);
    }

    public void initialize(OnTimeSetListener callback, int hourOfDay, int minute, int second, boolean is24HourMode) {
        this.mCallback = callback;
        this.mInitialTime = new Timepoint(hourOfDay, minute, second);
        this.mIs24HourMode = is24HourMode;
        this.mInKbMode = false;
        this.mTitle = "";
        this.mThemeDark = false;
        this.mThemeDarkChanged = false;
        this.mAccentColor = -1;
        this.mVibrate = true;
        this.mDismissOnPause = false;
        this.mEnableSeconds = false;
        this.mEnableMinutes = true;
        this.mOkResid = C1395R.string.mdtp_ok;
        this.mOkColor = -1;
        this.mCancelResid = C1395R.string.mdtp_cancel;
        this.mCancelColor = -1;
        this.mVersion = VERSION.SDK_INT < 23 ? Version.VERSION_1 : Version.VERSION_2;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setThemeDark(boolean dark) {
        this.mThemeDark = dark;
        this.mThemeDarkChanged = true;
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

    public boolean isThemeDark() {
        return this.mThemeDark;
    }

    public boolean is24HourMode() {
        return this.mIs24HourMode;
    }

    public int getAccentColor() {
        return this.mAccentColor;
    }

    public void vibrate(boolean vibrate) {
        this.mVibrate = vibrate;
    }

    public void dismissOnPause(boolean dismissOnPause) {
        this.mDismissOnPause = dismissOnPause;
    }

    public void enableSeconds(boolean enableSeconds) {
        if (enableSeconds) {
            this.mEnableMinutes = true;
        }
        this.mEnableSeconds = enableSeconds;
    }

    public void enableMinutes(boolean enableMinutes) {
        if (!enableMinutes) {
            this.mEnableSeconds = false;
        }
        this.mEnableMinutes = enableMinutes;
    }

    public void setMinTime(int hour, int minute, int second) {
        setMinTime(new Timepoint(hour, minute, second));
    }

    public void setMinTime(Timepoint minTime) {
        if (this.mMaxTime == null || minTime.compareTo(this.mMaxTime) <= 0) {
            this.mMinTime = minTime;
            return;
        }
        throw new IllegalArgumentException("Minimum time must be smaller than the maximum time");
    }

    public void setMaxTime(int hour, int minute, int second) {
        setMaxTime(new Timepoint(hour, minute, second));
    }

    public void setMaxTime(Timepoint maxTime) {
        if (this.mMinTime == null || maxTime.compareTo(this.mMinTime) >= 0) {
            this.mMaxTime = maxTime;
            return;
        }
        throw new IllegalArgumentException("Maximum time must be greater than the minimum time");
    }

    public void setSelectableTimes(Timepoint[] selectableTimes) {
        this.mSelectableTimes = selectableTimes;
        Arrays.sort(this.mSelectableTimes);
    }

    public void setTimeInterval(@IntRange(from = 1, to = 24) int hourInterval, @IntRange(from = 1, to = 60) int minuteInterval, @IntRange(from = 1, to = 60) int secondInterval) {
        List<Timepoint> timepoints = new ArrayList();
        int hour = 0;
        while (hour < 24) {
            int minute = 0;
            while (minute < 60) {
                int second = 0;
                while (second < 60) {
                    timepoints.add(new Timepoint(hour, minute, second));
                    second += secondInterval;
                }
                minute += minuteInterval;
            }
            hour += hourInterval;
        }
        setSelectableTimes((Timepoint[]) timepoints.toArray(new Timepoint[timepoints.size()]));
    }

    public void setTimeInterval(@IntRange(from = 1, to = 24) int hourInterval, @IntRange(from = 1, to = 60) int minuteInterval) {
        setTimeInterval(hourInterval, minuteInterval, 1);
    }

    public void setTimeInterval(@IntRange(from = 1, to = 24) int hourInterval) {
        setTimeInterval(hourInterval, 1);
    }

    public void setOnTimeSetListener(OnTimeSetListener callback) {
        this.mCallback = callback;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setStartTime(int hourOfDay, int minute, int second) {
        this.mInitialTime = roundToNearest(new Timepoint(hourOfDay, minute, second));
        this.mInKbMode = false;
    }

    public void setStartTime(int hourOfDay, int minute) {
        setStartTime(hourOfDay, minute, 0);
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

    public Version getVersion() {
        return this.mVersion;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_INITIAL_TIME) && savedInstanceState.containsKey(KEY_IS_24_HOUR_VIEW)) {
            this.mInitialTime = (Timepoint) savedInstanceState.getParcelable(KEY_INITIAL_TIME);
            this.mIs24HourMode = savedInstanceState.getBoolean(KEY_IS_24_HOUR_VIEW);
            this.mInKbMode = savedInstanceState.getBoolean(KEY_IN_KB_MODE);
            this.mTitle = savedInstanceState.getString(KEY_TITLE);
            this.mThemeDark = savedInstanceState.getBoolean(KEY_THEME_DARK);
            this.mThemeDarkChanged = savedInstanceState.getBoolean(KEY_THEME_DARK_CHANGED);
            this.mAccentColor = savedInstanceState.getInt(KEY_ACCENT);
            this.mVibrate = savedInstanceState.getBoolean(KEY_VIBRATE);
            this.mDismissOnPause = savedInstanceState.getBoolean(KEY_DISMISS);
            this.mSelectableTimes = (Timepoint[]) savedInstanceState.getParcelableArray(KEY_SELECTABLE_TIMES);
            this.mMinTime = (Timepoint) savedInstanceState.getParcelable(KEY_MIN_TIME);
            this.mMaxTime = (Timepoint) savedInstanceState.getParcelable(KEY_MAX_TIME);
            this.mEnableSeconds = savedInstanceState.getBoolean(KEY_ENABLE_SECONDS);
            this.mEnableMinutes = savedInstanceState.getBoolean(KEY_ENABLE_MINUTES);
            this.mOkResid = savedInstanceState.getInt(KEY_OK_RESID);
            this.mOkString = savedInstanceState.getString(KEY_OK_STRING);
            this.mOkColor = savedInstanceState.getInt(KEY_OK_COLOR);
            this.mCancelResid = savedInstanceState.getInt(KEY_CANCEL_RESID);
            this.mCancelString = savedInstanceState.getString(KEY_CANCEL_STRING);
            this.mCancelColor = savedInstanceState.getInt(KEY_CANCEL_COLOR);
            this.mVersion = (Version) savedInstanceState.getSerializable("version");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.mVersion == Version.VERSION_1 ? C1395R.layout.mdtp_time_picker_dialog : C1395R.layout.mdtp_time_picker_dialog_v2, container, false);
        KeyboardListener keyboardListener = new KeyboardListener();
        view.findViewById(C1395R.id.mdtp_time_picker_dialog).setOnKeyListener(keyboardListener);
        if (this.mAccentColor == -1) {
            this.mAccentColor = Utils.getAccentColorFromThemeIfAvailable(getActivity());
        }
        if (!this.mThemeDarkChanged) {
            this.mThemeDark = Utils.isDarkTheme(getActivity(), this.mThemeDark);
        }
        Resources res = getResources();
        Context context = getActivity();
        this.mHourPickerDescription = res.getString(C1395R.string.mdtp_hour_picker_description);
        this.mSelectHours = res.getString(C1395R.string.mdtp_select_hours);
        this.mMinutePickerDescription = res.getString(C1395R.string.mdtp_minute_picker_description);
        this.mSelectMinutes = res.getString(C1395R.string.mdtp_select_minutes);
        this.mSecondPickerDescription = res.getString(C1395R.string.mdtp_second_picker_description);
        this.mSelectSeconds = res.getString(C1395R.string.mdtp_select_seconds);
        this.mSelectedColor = ContextCompat.getColor(context, C1395R.color.mdtp_white);
        this.mUnselectedColor = ContextCompat.getColor(context, C1395R.color.mdtp_accent_color_focused);
        this.mHourView = (TextView) view.findViewById(C1395R.id.mdtp_hours);
        this.mHourView.setOnKeyListener(keyboardListener);
        this.mHourSpaceView = (TextView) view.findViewById(C1395R.id.mdtp_hour_space);
        this.mMinuteSpaceView = (TextView) view.findViewById(C1395R.id.mdtp_minutes_space);
        this.mMinuteView = (TextView) view.findViewById(C1395R.id.mdtp_minutes);
        this.mMinuteView.setOnKeyListener(keyboardListener);
        this.mSecondSpaceView = (TextView) view.findViewById(C1395R.id.mdtp_seconds_space);
        this.mSecondView = (TextView) view.findViewById(C1395R.id.mdtp_seconds);
        this.mSecondView.setOnKeyListener(keyboardListener);
        this.mAmTextView = (TextView) view.findViewById(C1395R.id.mdtp_am_label);
        this.mAmTextView.setOnKeyListener(keyboardListener);
        this.mPmTextView = (TextView) view.findViewById(C1395R.id.mdtp_pm_label);
        this.mPmTextView.setOnKeyListener(keyboardListener);
        this.mAmPmLayout = view.findViewById(C1395R.id.mdtp_ampm_layout);
        String[] amPmTexts = new DateFormatSymbols().getAmPmStrings();
        this.mAmText = amPmTexts[0];
        this.mPmText = amPmTexts[1];
        this.mHapticFeedbackController = new HapticFeedbackController(getActivity());
        if (this.mTimePicker != null) {
            this.mInitialTime = new Timepoint(this.mTimePicker.getHours(), this.mTimePicker.getMinutes(), this.mTimePicker.getSeconds());
        }
        this.mInitialTime = roundToNearest(this.mInitialTime);
        this.mTimePicker = (RadialPickerLayout) view.findViewById(C1395R.id.mdtp_time_picker);
        this.mTimePicker.setOnValueSelectedListener(this);
        this.mTimePicker.setOnKeyListener(keyboardListener);
        this.mTimePicker.initialize(getActivity(), this, this.mInitialTime, this.mIs24HourMode);
        int currentItemShowing = 0;
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_ITEM_SHOWING)) {
            currentItemShowing = savedInstanceState.getInt(KEY_CURRENT_ITEM_SHOWING);
        }
        setCurrentItemShowing(currentItemShowing, false, true, true);
        this.mTimePicker.invalidate();
        this.mHourView.setOnClickListener(new C14081());
        this.mMinuteView.setOnClickListener(new C14092());
        this.mSecondView.setOnClickListener(new C14103());
        this.mOkButton = (Button) view.findViewById(C1395R.id.mdtp_ok);
        this.mOkButton.setOnClickListener(new C14114());
        this.mOkButton.setOnKeyListener(keyboardListener);
        this.mOkButton.setTypeface(TypefaceHelper.get(context, "Roboto-Medium"));
        if (this.mOkString != null) {
            this.mOkButton.setText(this.mOkString);
        } else {
            this.mOkButton.setText(this.mOkResid);
        }
        this.mCancelButton = (Button) view.findViewById(C1395R.id.mdtp_cancel);
        this.mCancelButton.setOnClickListener(new C14125());
        this.mCancelButton.setTypeface(TypefaceHelper.get(context, "Roboto-Medium"));
        if (this.mCancelString != null) {
            this.mCancelButton.setText(this.mCancelString);
        } else {
            this.mCancelButton.setText(this.mCancelResid);
        }
        this.mCancelButton.setVisibility(isCancelable() ? 0 : 8);
        if (this.mIs24HourMode) {
            this.mAmPmLayout.setVisibility(8);
        } else {
            OnClickListener listener = new C14136();
            this.mAmTextView.setVisibility(8);
            this.mPmTextView.setVisibility(0);
            this.mAmPmLayout.setOnClickListener(listener);
            if (this.mVersion == Version.VERSION_2) {
                this.mAmTextView.setText(this.mAmText);
                this.mPmTextView.setText(this.mPmText);
                this.mAmTextView.setVisibility(0);
            }
            updateAmPmDisplay(this.mInitialTime.isAM() ? 0 : 1);
        }
        if (!this.mEnableSeconds) {
            this.mSecondView.setVisibility(8);
            view.findViewById(C1395R.id.mdtp_separator_seconds).setVisibility(8);
        }
        if (!this.mEnableMinutes) {
            this.mMinuteSpaceView.setVisibility(8);
            view.findViewById(C1395R.id.mdtp_separator).setVisibility(8);
        }
        LayoutParams layoutParams;
        RelativeLayout.LayoutParams paramsAmPm;
        if (getResources().getConfiguration().orientation == 2) {
            if (!this.mEnableMinutes && !this.mEnableSeconds) {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(2, C1395R.id.mdtp_center_view);
                layoutParams.addRule(14);
                this.mHourSpaceView.setLayoutParams(layoutParams);
                if (this.mIs24HourMode) {
                    paramsAmPm = new RelativeLayout.LayoutParams(-2, -2);
                    paramsAmPm.addRule(1, C1395R.id.mdtp_hour_space);
                    this.mAmPmLayout.setLayoutParams(paramsAmPm);
                }
            } else if (!this.mEnableSeconds && this.mIs24HourMode) {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(14);
                layoutParams.addRule(2, C1395R.id.mdtp_center_view);
                ((TextView) view.findViewById(C1395R.id.mdtp_separator)).setLayoutParams(layoutParams);
            } else if (!this.mEnableSeconds) {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(14);
                layoutParams.addRule(2, C1395R.id.mdtp_center_view);
                ((TextView) view.findViewById(C1395R.id.mdtp_separator)).setLayoutParams(layoutParams);
                paramsAmPm = new RelativeLayout.LayoutParams(-2, -2);
                paramsAmPm.addRule(13);
                paramsAmPm.addRule(3, C1395R.id.mdtp_center_view);
                this.mAmPmLayout.setLayoutParams(paramsAmPm);
            } else if (this.mIs24HourMode) {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(14);
                layoutParams.addRule(2, C1395R.id.mdtp_seconds_space);
                ((TextView) view.findViewById(C1395R.id.mdtp_separator)).setLayoutParams(layoutParams);
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13);
                this.mSecondSpaceView.setLayoutParams(layoutParams);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13);
                this.mSecondSpaceView.setLayoutParams(layoutParams);
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(14);
                layoutParams.addRule(2, C1395R.id.mdtp_seconds_space);
                ((TextView) view.findViewById(C1395R.id.mdtp_separator)).setLayoutParams(layoutParams);
                paramsAmPm = new RelativeLayout.LayoutParams(-2, -2);
                paramsAmPm.addRule(14);
                paramsAmPm.addRule(3, C1395R.id.mdtp_seconds_space);
                this.mAmPmLayout.setLayoutParams(paramsAmPm);
            }
        } else if (this.mIs24HourMode && !this.mEnableSeconds && this.mEnableMinutes) {
            layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            ((TextView) view.findViewById(C1395R.id.mdtp_separator)).setLayoutParams(layoutParams);
        } else if (!this.mEnableMinutes && !this.mEnableSeconds) {
            layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            this.mHourSpaceView.setLayoutParams(layoutParams);
            if (!this.mIs24HourMode) {
                paramsAmPm = new RelativeLayout.LayoutParams(-2, -2);
                paramsAmPm.addRule(1, C1395R.id.mdtp_hour_space);
                paramsAmPm.addRule(4, C1395R.id.mdtp_hour_space);
                this.mAmPmLayout.setLayoutParams(paramsAmPm);
            }
        } else if (this.mEnableSeconds) {
            View separator = view.findViewById(C1395R.id.mdtp_separator);
            layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(0, C1395R.id.mdtp_minutes_space);
            layoutParams.addRule(15, -1);
            separator.setLayoutParams(layoutParams);
            if (this.mIs24HourMode) {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(1, C1395R.id.mdtp_center_view);
                this.mMinuteSpaceView.setLayoutParams(layoutParams);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13);
                this.mMinuteSpaceView.setLayoutParams(layoutParams);
            }
        }
        this.mAllowAutoAdvance = true;
        setHour(this.mInitialTime.getHour(), true);
        setMinute(this.mInitialTime.getMinute());
        setSecond(this.mInitialTime.getSecond());
        this.mDoublePlaceholderText = res.getString(C1395R.string.mdtp_time_placeholder);
        this.mDeletedKeyFormat = res.getString(C1395R.string.mdtp_deleted_key);
        this.mPlaceholderText = this.mDoublePlaceholderText.charAt(0);
        this.mPmKeyCode = -1;
        this.mAmKeyCode = -1;
        generateLegalTimesTree();
        if (this.mInKbMode) {
            this.mTypedTimes = savedInstanceState.getIntegerArrayList(KEY_TYPED_TIMES);
            tryStartingKbMode(-1);
            this.mHourView.invalidate();
        } else if (this.mTypedTimes == null) {
            this.mTypedTimes = new ArrayList();
        }
        TextView timePickerHeader = (TextView) view.findViewById(C1395R.id.mdtp_time_picker_header);
        if (!this.mTitle.isEmpty()) {
            timePickerHeader.setVisibility(0);
            timePickerHeader.setText(this.mTitle.toUpperCase(Locale.getDefault()));
        }
        timePickerHeader.setBackgroundColor(Utils.darkenColor(this.mAccentColor));
        view.findViewById(C1395R.id.mdtp_time_display_background).setBackgroundColor(this.mAccentColor);
        view.findViewById(C1395R.id.mdtp_time_display).setBackgroundColor(this.mAccentColor);
        if (this.mOkColor != -1) {
            this.mOkButton.setTextColor(this.mOkColor);
        } else {
            this.mOkButton.setTextColor(this.mAccentColor);
        }
        if (this.mCancelColor != -1) {
            this.mCancelButton.setTextColor(this.mCancelColor);
        } else {
            this.mCancelButton.setTextColor(this.mAccentColor);
        }
        if (getDialog() == null) {
            view.findViewById(C1395R.id.mdtp_done_background).setVisibility(8);
        }
        int circleBackground = ContextCompat.getColor(context, C1395R.color.mdtp_circle_background);
        int backgroundColor = ContextCompat.getColor(context, C1395R.color.mdtp_background_color);
        int darkBackgroundColor = ContextCompat.getColor(context, C1395R.color.mdtp_light_gray);
        int lightGray = ContextCompat.getColor(context, C1395R.color.mdtp_light_gray);
        RadialPickerLayout radialPickerLayout = this.mTimePicker;
        if (!this.mThemeDark) {
            lightGray = circleBackground;
        }
        radialPickerLayout.setBackgroundColor(lightGray);
        View findViewById = view.findViewById(C1395R.id.mdtp_time_picker_dialog);
        if (!this.mThemeDark) {
            darkBackgroundColor = backgroundColor;
        }
        findViewById.setBackgroundColor(darkBackgroundColor);
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

    public void tryVibrate() {
        if (this.mVibrate) {
            this.mHapticFeedbackController.tryVibrate();
        }
    }

    private void updateAmPmDisplay(int amOrPm) {
        if (this.mVersion == Version.VERSION_2) {
            if (amOrPm == 0) {
                this.mAmTextView.setTextColor(this.mSelectedColor);
                this.mPmTextView.setTextColor(this.mUnselectedColor);
                Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mAmText);
                return;
            }
            this.mAmTextView.setTextColor(this.mUnselectedColor);
            this.mPmTextView.setTextColor(this.mSelectedColor);
            Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mPmText);
        } else if (amOrPm == 0) {
            this.mPmTextView.setText(this.mAmText);
            Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mAmText);
            this.mPmTextView.setContentDescription(this.mAmText);
        } else if (amOrPm == 1) {
            this.mPmTextView.setText(this.mPmText);
            Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mPmText);
            this.mPmTextView.setContentDescription(this.mPmText);
        } else {
            this.mPmTextView.setText(this.mDoublePlaceholderText);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (this.mTimePicker != null) {
            outState.putParcelable(KEY_INITIAL_TIME, this.mTimePicker.getTime());
            outState.putBoolean(KEY_IS_24_HOUR_VIEW, this.mIs24HourMode);
            outState.putInt(KEY_CURRENT_ITEM_SHOWING, this.mTimePicker.getCurrentItemShowing());
            outState.putBoolean(KEY_IN_KB_MODE, this.mInKbMode);
            if (this.mInKbMode) {
                outState.putIntegerArrayList(KEY_TYPED_TIMES, this.mTypedTimes);
            }
            outState.putString(KEY_TITLE, this.mTitle);
            outState.putBoolean(KEY_THEME_DARK, this.mThemeDark);
            outState.putBoolean(KEY_THEME_DARK_CHANGED, this.mThemeDarkChanged);
            outState.putInt(KEY_ACCENT, this.mAccentColor);
            outState.putBoolean(KEY_VIBRATE, this.mVibrate);
            outState.putBoolean(KEY_DISMISS, this.mDismissOnPause);
            outState.putParcelableArray(KEY_SELECTABLE_TIMES, this.mSelectableTimes);
            outState.putParcelable(KEY_MIN_TIME, this.mMinTime);
            outState.putParcelable(KEY_MAX_TIME, this.mMaxTime);
            outState.putBoolean(KEY_ENABLE_SECONDS, this.mEnableSeconds);
            outState.putBoolean(KEY_ENABLE_MINUTES, this.mEnableMinutes);
            outState.putInt(KEY_OK_RESID, this.mOkResid);
            outState.putString(KEY_OK_STRING, this.mOkString);
            outState.putInt(KEY_OK_COLOR, this.mOkColor);
            outState.putInt(KEY_CANCEL_RESID, this.mCancelResid);
            outState.putString(KEY_CANCEL_STRING, this.mCancelString);
            outState.putInt(KEY_CANCEL_COLOR, this.mCancelColor);
            outState.putSerializable("version", this.mVersion);
        }
    }

    public void onValueSelected(Timepoint newValue) {
        int i = 0;
        setHour(newValue.getHour(), false);
        this.mTimePicker.setContentDescription(this.mHourPickerDescription + ": " + newValue.getHour());
        setMinute(newValue.getMinute());
        this.mTimePicker.setContentDescription(this.mMinutePickerDescription + ": " + newValue.getMinute());
        setSecond(newValue.getSecond());
        this.mTimePicker.setContentDescription(this.mSecondPickerDescription + ": " + newValue.getSecond());
        if (!this.mIs24HourMode) {
            if (!newValue.isAM()) {
                i = 1;
            }
            updateAmPmDisplay(i);
        }
    }

    public void advancePicker(int index) {
        if (!this.mAllowAutoAdvance) {
            return;
        }
        if (index == 0 && this.mEnableMinutes) {
            setCurrentItemShowing(1, true, true, false);
            Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectHours + ". " + this.mTimePicker.getMinutes());
        } else if (index == 1 && this.mEnableSeconds) {
            setCurrentItemShowing(2, true, true, false);
            Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectMinutes + ". " + this.mTimePicker.getSeconds());
        }
    }

    public void enablePicker() {
        if (!isTypedTimeFullyLegal()) {
            this.mTypedTimes.clear();
        }
        finishKbMode(true);
    }

    public boolean isOutOfRange(Timepoint current) {
        if (this.mMinTime != null && this.mMinTime.compareTo(current) > 0) {
            return true;
        }
        if (this.mMaxTime != null && this.mMaxTime.compareTo(current) < 0) {
            return true;
        }
        if (this.mSelectableTimes == null) {
            return false;
        }
        if (Arrays.asList(this.mSelectableTimes).contains(current)) {
            return false;
        }
        return true;
    }

    public boolean isOutOfRange(Timepoint current, int index) {
        if (current == null) {
            return false;
        }
        if (index == 0) {
            if (this.mMinTime != null && this.mMinTime.getHour() > current.getHour()) {
                return true;
            }
            if (this.mMaxTime != null && this.mMaxTime.getHour() + 1 <= current.getHour()) {
                return true;
            }
            if (this.mSelectableTimes == null) {
                return false;
            }
            for (Timepoint t : this.mSelectableTimes) {
                if (t.getHour() == current.getHour()) {
                    return false;
                }
            }
            return true;
        } else if (index != 1) {
            return isOutOfRange(current);
        } else {
            if (this.mMinTime != null && new Timepoint(this.mMinTime.getHour(), this.mMinTime.getMinute()).compareTo(current) > 0) {
                return true;
            }
            if (this.mMaxTime != null && new Timepoint(this.mMaxTime.getHour(), this.mMaxTime.getMinute(), 59).compareTo(current) < 0) {
                return true;
            }
            if (this.mSelectableTimes == null) {
                return false;
            }
            for (Timepoint t2 : this.mSelectableTimes) {
                if (t2.getHour() == current.getHour() && t2.getMinute() == current.getMinute()) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isAmDisabled() {
        Timepoint midday = new Timepoint(12);
        if (this.mMinTime != null && this.mMinTime.compareTo(midday) > 0) {
            return true;
        }
        if (this.mSelectableTimes == null) {
            return false;
        }
        for (Timepoint t : this.mSelectableTimes) {
            if (t.compareTo(midday) < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isPmDisabled() {
        Timepoint midday = new Timepoint(12);
        if (this.mMaxTime != null && this.mMaxTime.compareTo(midday) < 0) {
            return true;
        }
        if (this.mSelectableTimes == null) {
            return false;
        }
        for (Timepoint t : this.mSelectableTimes) {
            if (t.compareTo(midday) >= 0) {
                return false;
            }
        }
        return true;
    }

    private Timepoint roundToNearest(@NonNull Timepoint time) {
        return roundToNearest(time, null);
    }

    public Timepoint roundToNearest(@NonNull Timepoint time, @Nullable TYPE type) {
        if (this.mMinTime != null && this.mMinTime.compareTo(time) > 0) {
            return this.mMinTime;
        }
        if (this.mMaxTime != null && this.mMaxTime.compareTo(time) < 0) {
            return this.mMaxTime;
        }
        if (this.mSelectableTimes == null) {
            return time;
        }
        int currentDistance = Integer.MAX_VALUE;
        Timepoint output = time;
        for (Timepoint t : this.mSelectableTimes) {
            if ((type != TYPE.HOUR || t.getHour() == time.getHour()) && (type != TYPE.MINUTE || t.getHour() == time.getHour() || t.getMinute() == time.getMinute())) {
                if (type != TYPE.SECOND) {
                    int newDistance = Math.abs(t.compareTo(time));
                    if (newDistance >= currentDistance) {
                        break;
                    }
                    currentDistance = newDistance;
                    output = t;
                } else {
                    return time;
                }
            }
        }
        return output;
    }

    private void setHour(int value, boolean announce) {
        String format;
        if (this.mIs24HourMode) {
            format = "%02d";
        } else {
            format = "%d";
            value %= 12;
            if (value == 0) {
                value = 12;
            }
        }
        CharSequence text = String.format(format, new Object[]{Integer.valueOf(value)});
        this.mHourView.setText(text);
        this.mHourSpaceView.setText(text);
        if (announce) {
            Utils.tryAccessibilityAnnounce(this.mTimePicker, text);
        }
    }

    private void setMinute(int value) {
        if (value == 60) {
            value = 0;
        }
        CharSequence text = String.format(Locale.getDefault(), "%02d", new Object[]{Integer.valueOf(value)});
        Utils.tryAccessibilityAnnounce(this.mTimePicker, text);
        this.mMinuteView.setText(text);
        this.mMinuteSpaceView.setText(text);
    }

    private void setSecond(int value) {
        if (value == 60) {
            value = 0;
        }
        CharSequence text = String.format(Locale.getDefault(), "%02d", new Object[]{Integer.valueOf(value)});
        Utils.tryAccessibilityAnnounce(this.mTimePicker, text);
        this.mSecondView.setText(text);
        this.mSecondSpaceView.setText(text);
    }

    private void setCurrentItemShowing(int index, boolean animateCircle, boolean delayLabelAnimate, boolean announce) {
        TextView labelToAnimate;
        this.mTimePicker.setCurrentItemShowing(index, animateCircle);
        switch (index) {
            case 0:
                int hours = this.mTimePicker.getHours();
                if (!this.mIs24HourMode) {
                    hours %= 12;
                }
                this.mTimePicker.setContentDescription(this.mHourPickerDescription + ": " + hours);
                if (announce) {
                    Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectHours);
                }
                labelToAnimate = this.mHourView;
                break;
            case 1:
                this.mTimePicker.setContentDescription(this.mMinutePickerDescription + ": " + this.mTimePicker.getMinutes());
                if (announce) {
                    Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectMinutes);
                }
                labelToAnimate = this.mMinuteView;
                break;
            default:
                this.mTimePicker.setContentDescription(this.mSecondPickerDescription + ": " + this.mTimePicker.getSeconds());
                if (announce) {
                    Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectSeconds);
                }
                labelToAnimate = this.mSecondView;
                break;
        }
        int hourColor = index == 0 ? this.mSelectedColor : this.mUnselectedColor;
        int minuteColor = index == 1 ? this.mSelectedColor : this.mUnselectedColor;
        int secondColor = index == 2 ? this.mSelectedColor : this.mUnselectedColor;
        this.mHourView.setTextColor(hourColor);
        this.mMinuteView.setTextColor(minuteColor);
        this.mSecondView.setTextColor(secondColor);
        ObjectAnimator pulseAnimator = Utils.getPulseAnimator(labelToAnimate, 0.85f, 1.1f);
        if (delayLabelAnimate) {
            pulseAnimator.setStartDelay(300);
        }
        pulseAnimator.start();
    }

    private boolean processKeyUp(int keyCode) {
        if (keyCode != 111 && keyCode != 4) {
            if (keyCode == 61) {
                if (this.mInKbMode) {
                    if (!isTypedTimeFullyLegal()) {
                        return true;
                    }
                    finishKbMode(true);
                    return true;
                }
            } else if (keyCode == 66) {
                if (this.mInKbMode) {
                    if (!isTypedTimeFullyLegal()) {
                        return true;
                    }
                    finishKbMode(false);
                }
                if (this.mCallback != null) {
                    this.mCallback.onTimeSet(this, this.mTimePicker.getHours(), this.mTimePicker.getMinutes(), this.mTimePicker.getSeconds());
                }
                dismiss();
                return true;
            } else if (keyCode == 67) {
                if (this.mInKbMode && !this.mTypedTimes.isEmpty()) {
                    String deletedKeyStr;
                    int deleted = deleteLastTypedKey();
                    if (deleted == getAmOrPmKeyCode(0)) {
                        deletedKeyStr = this.mAmText;
                    } else if (deleted == getAmOrPmKeyCode(1)) {
                        deletedKeyStr = this.mPmText;
                    } else {
                        deletedKeyStr = String.format("%d", new Object[]{Integer.valueOf(getValFromKeyCode(deleted))});
                    }
                    Utils.tryAccessibilityAnnounce(this.mTimePicker, String.format(this.mDeletedKeyFormat, new Object[]{deletedKeyStr}));
                    updateDisplay(true);
                }
            } else if (keyCode == 7 || keyCode == 8 || keyCode == 9 || keyCode == 10 || keyCode == 11 || keyCode == 12 || keyCode == 13 || keyCode == 14 || keyCode == 15 || keyCode == 16 || (!this.mIs24HourMode && (keyCode == getAmOrPmKeyCode(0) || keyCode == getAmOrPmKeyCode(1)))) {
                if (this.mInKbMode) {
                    if (!addKeyIfLegal(keyCode)) {
                        return true;
                    }
                    updateDisplay(false);
                    return true;
                } else if (this.mTimePicker == null) {
                    Log.e(TAG, "Unable to initiate keyboard mode, TimePicker was null.");
                    return true;
                } else {
                    this.mTypedTimes.clear();
                    tryStartingKbMode(keyCode);
                    return true;
                }
            }
            return false;
        } else if (!isCancelable()) {
            return true;
        } else {
            dismiss();
            return true;
        }
    }

    private void tryStartingKbMode(int keyCode) {
        if (!this.mTimePicker.trySettingInputEnabled(false)) {
            return;
        }
        if (keyCode == -1 || addKeyIfLegal(keyCode)) {
            this.mInKbMode = true;
            this.mOkButton.setEnabled(false);
            updateDisplay(false);
        }
    }

    private boolean addKeyIfLegal(int keyCode) {
        int textSize = 6;
        if (this.mEnableMinutes && !this.mEnableSeconds) {
            textSize = 4;
        }
        if (!(this.mEnableMinutes || this.mEnableSeconds)) {
            textSize = 2;
        }
        if (this.mIs24HourMode && this.mTypedTimes.size() == textSize) {
            return false;
        }
        if (!this.mIs24HourMode && isTypedTimeFullyLegal()) {
            return false;
        }
        this.mTypedTimes.add(Integer.valueOf(keyCode));
        if (isTypedTimeLegalSoFar()) {
            int val = getValFromKeyCode(keyCode);
            Utils.tryAccessibilityAnnounce(this.mTimePicker, String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(val)}));
            if (isTypedTimeFullyLegal()) {
                if (!this.mIs24HourMode && this.mTypedTimes.size() <= textSize - 1) {
                    this.mTypedTimes.add(this.mTypedTimes.size() - 1, Integer.valueOf(7));
                    this.mTypedTimes.add(this.mTypedTimes.size() - 1, Integer.valueOf(7));
                }
                this.mOkButton.setEnabled(true);
            }
            return true;
        }
        deleteLastTypedKey();
        return false;
    }

    private boolean isTypedTimeLegalSoFar() {
        Node node = this.mLegalTimesTree;
        Iterator it = this.mTypedTimes.iterator();
        while (it.hasNext()) {
            node = node.canReach(((Integer) it.next()).intValue());
            if (node == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isTypedTimeFullyLegal() {
        boolean z = false;
        if (this.mIs24HourMode) {
            int[] values = getEnteredTime(null);
            if (values[0] < 0 || values[1] < 0 || values[1] >= 60 || values[2] < 0 || values[2] >= 60) {
                return false;
            }
            return true;
        }
        if (this.mTypedTimes.contains(Integer.valueOf(getAmOrPmKeyCode(0))) || this.mTypedTimes.contains(Integer.valueOf(getAmOrPmKeyCode(1)))) {
            z = true;
        }
        return z;
    }

    private int deleteLastTypedKey() {
        int deleted = ((Integer) this.mTypedTimes.remove(this.mTypedTimes.size() - 1)).intValue();
        if (!isTypedTimeFullyLegal()) {
            this.mOkButton.setEnabled(false);
        }
        return deleted;
    }

    private void finishKbMode(boolean updateDisplays) {
        this.mInKbMode = false;
        if (!this.mTypedTimes.isEmpty()) {
            int[] values = getEnteredTime(null);
            this.mTimePicker.setTime(new Timepoint(values[0], values[1], values[2]));
            if (!this.mIs24HourMode) {
                this.mTimePicker.setAmOrPm(values[3]);
            }
            this.mTypedTimes.clear();
        }
        if (updateDisplays) {
            updateDisplay(false);
            this.mTimePicker.trySettingInputEnabled(true);
        }
    }

    private void updateDisplay(boolean allowEmptyDisplay) {
        if (allowEmptyDisplay || !this.mTypedTimes.isEmpty()) {
            String hourStr;
            String minuteStr;
            String secondStr;
            Boolean[] enteredZeros = new Boolean[]{Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false)};
            int[] values = getEnteredTime(enteredZeros);
            String hourFormat = enteredZeros[0].booleanValue() ? "%02d" : "%2d";
            String minuteFormat = enteredZeros[1].booleanValue() ? "%02d" : "%2d";
            String secondFormat = enteredZeros[1].booleanValue() ? "%02d" : "%2d";
            if (values[0] == -1) {
                hourStr = this.mDoublePlaceholderText;
            } else {
                hourStr = String.format(hourFormat, new Object[]{Integer.valueOf(values[0])}).replace(' ', this.mPlaceholderText);
            }
            if (values[1] == -1) {
                minuteStr = this.mDoublePlaceholderText;
            } else {
                minuteStr = String.format(minuteFormat, new Object[]{Integer.valueOf(values[1])}).replace(' ', this.mPlaceholderText);
            }
            if (values[2] == -1) {
                secondStr = this.mDoublePlaceholderText;
            } else {
                secondStr = String.format(secondFormat, new Object[]{Integer.valueOf(values[1])}).replace(' ', this.mPlaceholderText);
            }
            this.mHourView.setText(hourStr);
            this.mHourSpaceView.setText(hourStr);
            this.mHourView.setTextColor(this.mUnselectedColor);
            this.mMinuteView.setText(minuteStr);
            this.mMinuteSpaceView.setText(minuteStr);
            this.mMinuteView.setTextColor(this.mUnselectedColor);
            this.mSecondView.setText(secondStr);
            this.mSecondSpaceView.setText(secondStr);
            this.mSecondView.setTextColor(this.mUnselectedColor);
            if (!this.mIs24HourMode) {
                updateAmPmDisplay(values[3]);
                return;
            }
            return;
        }
        int hour = this.mTimePicker.getHours();
        int minute = this.mTimePicker.getMinutes();
        int second = this.mTimePicker.getSeconds();
        setHour(hour, true);
        setMinute(minute);
        setSecond(second);
        if (!this.mIs24HourMode) {
            updateAmPmDisplay(hour < 12 ? 0 : 1);
        }
        setCurrentItemShowing(this.mTimePicker.getCurrentItemShowing(), true, true, true);
        this.mOkButton.setEnabled(true);
    }

    private static int getValFromKeyCode(int keyCode) {
        switch (keyCode) {
            case 7:
                return 0;
            case 8:
                return 1;
            case 9:
                return 2;
            case 10:
                return 3;
            case 11:
                return 4;
            case 12:
                return 5;
            case 13:
                return 6;
            case 14:
                return 7;
            case 15:
                return 8;
            case 16:
                return 9;
            default:
                return -1;
        }
    }

    private int[] getEnteredTime(Boolean[] enteredZeros) {
        int shift;
        int amOrPm = -1;
        int startIndex = 1;
        if (!this.mIs24HourMode && isTypedTimeFullyLegal()) {
            int keyCode = ((Integer) this.mTypedTimes.get(this.mTypedTimes.size() - 1)).intValue();
            if (keyCode == getAmOrPmKeyCode(0)) {
                amOrPm = 0;
            } else if (keyCode == getAmOrPmKeyCode(1)) {
                amOrPm = 1;
            }
            startIndex = 2;
        }
        int minute = -1;
        int hour = -1;
        int second = 0;
        if (this.mEnableSeconds) {
            shift = 2;
        } else {
            shift = 0;
        }
        for (int i = startIndex; i <= this.mTypedTimes.size(); i++) {
            int val = getValFromKeyCode(((Integer) this.mTypedTimes.get(this.mTypedTimes.size() - i)).intValue());
            if (this.mEnableSeconds) {
                if (i == startIndex) {
                    second = val;
                } else if (i == startIndex + 1) {
                    second += val * 10;
                    if (enteredZeros != null && val == 0) {
                        enteredZeros[2] = Boolean.valueOf(true);
                    }
                }
            }
            if (this.mEnableMinutes) {
                if (i == startIndex + shift) {
                    minute = val;
                } else if (i == (startIndex + shift) + 1) {
                    minute += val * 10;
                    if (enteredZeros != null && val == 0) {
                        enteredZeros[1] = Boolean.valueOf(true);
                    }
                } else if (i == (startIndex + shift) + 2) {
                    hour = val;
                } else if (i == (startIndex + shift) + 3) {
                    hour += val * 10;
                    if (enteredZeros != null && val == 0) {
                        enteredZeros[0] = Boolean.valueOf(true);
                    }
                }
            } else if (i == startIndex + shift) {
                hour = val;
            } else if (i == (startIndex + shift) + 1) {
                hour += val * 10;
                if (enteredZeros != null && val == 0) {
                    enteredZeros[0] = Boolean.valueOf(true);
                }
            }
        }
        return new int[]{hour, minute, second, amOrPm};
    }

    private int getAmOrPmKeyCode(int amOrPm) {
        if (this.mAmKeyCode == -1 || this.mPmKeyCode == -1) {
            KeyCharacterMap kcm = KeyCharacterMap.load(-1);
            int i = 0;
            while (i < Math.max(this.mAmText.length(), this.mPmText.length())) {
                if (this.mAmText.toLowerCase(Locale.getDefault()).charAt(i) != this.mPmText.toLowerCase(Locale.getDefault()).charAt(i)) {
                    KeyEvent[] events = kcm.getEvents(new char[]{this.mAmText.toLowerCase(Locale.getDefault()).charAt(i), this.mPmText.toLowerCase(Locale.getDefault()).charAt(i)});
                    if (events == null || events.length != 4) {
                        Log.e(TAG, "Unable to find keycodes for AM and PM.");
                    } else {
                        this.mAmKeyCode = events[0].getKeyCode();
                        this.mPmKeyCode = events[2].getKeyCode();
                    }
                } else {
                    i++;
                }
            }
        }
        if (amOrPm == 0) {
            return this.mAmKeyCode;
        }
        if (amOrPm == 1) {
            return this.mPmKeyCode;
        }
        return -1;
    }

    private void generateLegalTimesTree() {
        this.mLegalTimesTree = new Node(new int[0]);
        Node firstDigit;
        if (!this.mEnableMinutes && this.mIs24HourMode) {
            firstDigit = new Node(7, 8);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16));
            firstDigit = new Node(9);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(new Node(7, 8, 9, 10));
        } else if (!this.mEnableMinutes && !this.mIs24HourMode) {
            ampm = new Node(getAmOrPmKeyCode(0), getAmOrPmKeyCode(1));
            firstDigit = new Node(8);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(ampm);
            r0 = new Node(7, 8, 9);
            firstDigit.addChild(r0);
            r0.addChild(ampm);
            firstDigit = new Node(9, 10, 11, 12, 13, 14, 15, 16);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(ampm);
        } else if (this.mIs24HourMode) {
            Node minuteFirstDigit = new Node(7, 8, 9, 10, 11, 12);
            r0 = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
            minuteFirstDigit.addChild(r0);
            if (this.mEnableSeconds) {
                r0 = new Node(7, 8, 9, 10, 11, 12);
                r0.addChild(new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16));
                r0.addChild(r0);
            }
            firstDigit = new Node(7, 8);
            this.mLegalTimesTree.addChild(firstDigit);
            r0 = new Node(7, 8, 9, 10, 11, 12);
            firstDigit.addChild(r0);
            r0.addChild(minuteFirstDigit);
            r0.addChild(new Node(13, 14, 15, 16));
            r0 = new Node(13, 14, 15, 16);
            firstDigit.addChild(r0);
            r0.addChild(minuteFirstDigit);
            firstDigit = new Node(9);
            this.mLegalTimesTree.addChild(firstDigit);
            r0 = new Node(7, 8, 9, 10);
            firstDigit.addChild(r0);
            r0.addChild(minuteFirstDigit);
            r0 = new Node(11, 12);
            firstDigit.addChild(r0);
            r0.addChild(r0);
            firstDigit = new Node(10, 11, 12, 13, 14, 15, 16);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(minuteFirstDigit);
        } else {
            ampm = new Node(getAmOrPmKeyCode(0), getAmOrPmKeyCode(1));
            r0 = new Node(7, 8, 9, 10, 11, 12);
            r0 = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
            r0.addChild(ampm);
            r0.addChild(r0);
            firstDigit = new Node(8);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(ampm);
            r0 = new Node(7, 8, 9);
            firstDigit.addChild(r0);
            r0.addChild(ampm);
            Node thirdDigit = new Node(7, 8, 9, 10, 11, 12);
            r0.addChild(thirdDigit);
            thirdDigit.addChild(ampm);
            Node fourthDigit = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
            thirdDigit.addChild(fourthDigit);
            fourthDigit.addChild(ampm);
            if (this.mEnableSeconds) {
                fourthDigit.addChild(r0);
            }
            thirdDigit = new Node(13, 14, 15, 16);
            r0.addChild(thirdDigit);
            thirdDigit.addChild(ampm);
            if (this.mEnableSeconds) {
                thirdDigit.addChild(r0);
            }
            r0 = new Node(10, 11, 12);
            firstDigit.addChild(r0);
            thirdDigit = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
            r0.addChild(thirdDigit);
            thirdDigit.addChild(ampm);
            if (this.mEnableSeconds) {
                thirdDigit.addChild(r0);
            }
            firstDigit = new Node(9, 10, 11, 12, 13, 14, 15, 16);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(ampm);
            r0 = new Node(7, 8, 9, 10, 11, 12);
            firstDigit.addChild(r0);
            thirdDigit = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
            r0.addChild(thirdDigit);
            thirdDigit.addChild(ampm);
            if (this.mEnableSeconds) {
                thirdDigit.addChild(r0);
            }
        }
    }

    public void notifyOnDateListener() {
        if (this.mCallback != null) {
            this.mCallback.onTimeSet(this, this.mTimePicker.getHours(), this.mTimePicker.getMinutes(), this.mTimePicker.getSeconds());
        }
    }

    public Timepoint getSelectedTime() {
        return this.mTimePicker.getTime();
    }
}
