package com.wdullaer.materialdatetimepicker.date;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.wdullaer.materialdatetimepicker.C1395R;

public class TextViewWithCircularIndicator extends AppCompatTextView {
    private static final int SELECTED_CIRCLE_ALPHA = 255;
    private int mCircleColor;
    Paint mCirclePaint = new Paint();
    private boolean mDrawCircle;
    private final String mItemIsSelectedText;

    public TextViewWithCircularIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCircleColor = ContextCompat.getColor(context, C1395R.color.mdtp_accent_color);
        this.mItemIsSelectedText = context.getResources().getString(C1395R.string.mdtp_item_is_selected);
        init();
    }

    private void init() {
        this.mCirclePaint.setFakeBoldText(true);
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setColor(this.mCircleColor);
        this.mCirclePaint.setTextAlign(Align.CENTER);
        this.mCirclePaint.setStyle(Style.FILL);
        this.mCirclePaint.setAlpha(255);
    }

    public void setAccentColor(int color, boolean darkMode) {
        this.mCircleColor = color;
        this.mCirclePaint.setColor(this.mCircleColor);
        setTextColor(createTextColor(color, darkMode));
    }

    private ColorStateList createTextColor(int accentColor, boolean darkMode) {
        int i = -1;
        states = new int[3][];
        states[0] = new int[]{16842919};
        states[1] = new int[]{16842913};
        states[2] = new int[0];
        int[] colors = new int[3];
        colors[0] = accentColor;
        colors[1] = -1;
        if (!darkMode) {
            i = -16777216;
        }
        colors[2] = i;
        return new ColorStateList(states, colors);
    }

    public void drawIndicator(boolean drawCircle) {
        this.mDrawCircle = drawCircle;
    }

    public void onDraw(@NonNull Canvas canvas) {
        if (this.mDrawCircle) {
            int width = getWidth();
            int height = getHeight();
            canvas.drawCircle((float) (width / 2), (float) (height / 2), (float) (Math.min(width, height) / 2), this.mCirclePaint);
        }
        setSelected(this.mDrawCircle);
        super.onDraw(canvas);
    }

    public CharSequence getContentDescription() {
        CharSequence itemText = getText();
        if (!this.mDrawCircle) {
            return itemText;
        }
        return String.format(this.mItemIsSelectedText, new Object[]{itemText});
    }
}
