package com.wdullaer.materialdatetimepicker.date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.C1395R;

public class YearPickerView extends ListView implements OnItemClickListener, OnDateChangedListener {
    private static final String TAG = "YearPickerView";
    private YearAdapter mAdapter;
    private int mChildSize;
    private final DatePickerController mController;
    private TextViewWithCircularIndicator mSelectedView;
    private int mViewSize;

    private final class YearAdapter extends BaseAdapter {
        private final int mMaxYear;
        private final int mMinYear;

        YearAdapter(int minYear, int maxYear) {
            if (minYear > maxYear) {
                throw new IllegalArgumentException("minYear > maxYear");
            }
            this.mMinYear = minYear;
            this.mMaxYear = maxYear;
        }

        public int getCount() {
            return (this.mMaxYear - this.mMinYear) + 1;
        }

        public Object getItem(int position) {
            return Integer.valueOf(this.mMinYear + position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextViewWithCircularIndicator v;
            boolean selected = false;
            if (convertView != null) {
                v = (TextViewWithCircularIndicator) convertView;
            } else {
                v = (TextViewWithCircularIndicator) LayoutInflater.from(parent.getContext()).inflate(C1395R.layout.mdtp_year_label_text_view, parent, false);
                v.setAccentColor(YearPickerView.this.mController.getAccentColor(), YearPickerView.this.mController.isThemeDark());
            }
            int year = this.mMinYear + position;
            if (YearPickerView.this.mController.getSelectedDay().year == year) {
                selected = true;
            }
            v.setText(String.valueOf(year));
            v.drawIndicator(selected);
            v.requestLayout();
            if (selected) {
                YearPickerView.this.mSelectedView = v;
            }
            return v;
        }
    }

    public YearPickerView(Context context, DatePickerController controller) {
        super(context);
        this.mController = controller;
        this.mController.registerOnDateChangedListener(this);
        setLayoutParams(new LayoutParams(-1, -2));
        Resources res = context.getResources();
        this.mViewSize = res.getDimensionPixelOffset(C1395R.dimen.mdtp_date_picker_view_animator_height);
        this.mChildSize = res.getDimensionPixelOffset(C1395R.dimen.mdtp_year_label_height);
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(this.mChildSize / 3);
        init();
        setOnItemClickListener(this);
        setSelector(new StateListDrawable());
        setDividerHeight(0);
        onDateChanged();
    }

    private void init() {
        this.mAdapter = new YearAdapter(this.mController.getMinYear(), this.mController.getMaxYear());
        setAdapter(this.mAdapter);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.mController.tryVibrate();
        TextViewWithCircularIndicator clickedView = (TextViewWithCircularIndicator) view;
        if (clickedView != null) {
            if (clickedView != this.mSelectedView) {
                if (this.mSelectedView != null) {
                    this.mSelectedView.drawIndicator(false);
                    this.mSelectedView.requestLayout();
                }
                clickedView.drawIndicator(true);
                clickedView.requestLayout();
                this.mSelectedView = clickedView;
            }
            this.mController.onYearSelected(getYearFromTextView(clickedView));
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private static int getYearFromTextView(TextView view) {
        return Integer.valueOf(view.getText().toString()).intValue();
    }

    public void postSetSelectionCentered(int position) {
        postSetSelectionFromTop(position, (this.mViewSize / 2) - (this.mChildSize / 2));
    }

    public void postSetSelectionFromTop(final int position, final int offset) {
        post(new Runnable() {
            public void run() {
                YearPickerView.this.setSelectionFromTop(position, offset);
                YearPickerView.this.requestLayout();
            }
        });
    }

    public int getFirstPositionOffset() {
        View firstChild = getChildAt(0);
        if (firstChild == null) {
            return 0;
        }
        return firstChild.getTop();
    }

    public void onDateChanged() {
        this.mAdapter.notifyDataSetChanged();
        postSetSelectionCentered(this.mController.getSelectedDay().year - this.mController.getMinYear());
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        if (event.getEventType() == 4096) {
            event.setFromIndex(0);
            event.setToIndex(0);
        }
    }
}
