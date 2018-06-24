package io.techery.properratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProperRatingBar extends LinearLayout {
    private static final boolean DF_CLICKABLE = false;
    private static final int DF_DEFAULT_TICKS = 3;
    private static final int DF_SYMBOLIC_TEXT_NORMAL_COLOR = -16777216;
    private static final int DF_SYMBOLIC_TEXT_SELECTED_COLOR = -7829368;
    private static final int DF_SYMBOLIC_TEXT_SIZE_RES = C1529R.dimen.prb_symbolic_tick_default_text_size;
    private static final int DF_SYMBOLIC_TEXT_STYLE = 0;
    private static final int DF_SYMBOLIC_TICK_RES = C1529R.string.prb_default_symbolic_string;
    private static final int DF_TICK_SPACING_RES = C1529R.dimen.prb_drawable_tick_default_spacing;
    private static final int DF_TOTAL_TICKS = 5;
    private boolean clickable;
    private int customTextNormalColor;
    private int customTextSelectedColor;
    private int customTextSize;
    private int customTextStyle;
    private int lastSelectedTickIndex;
    private RatingListener listener = null;
    private OnClickListener mTickClickedListener = new C15251();
    private int rating;
    private String symbolicTick;
    private Drawable tickNormalDrawable;
    private Drawable tickSelectedDrawable;
    private int tickSpacing;
    private int totalTicks;
    private boolean useSymbolicTick = false;

    class C15251 implements OnClickListener {
        C15251() {
        }

        public void onClick(View v) {
            ProperRatingBar.this.lastSelectedTickIndex = ((Integer) v.getTag(C1529R.id.prb_tick_tag_id)).intValue();
            ProperRatingBar.this.rating = ProperRatingBar.this.lastSelectedTickIndex + 1;
            ProperRatingBar.this.redrawTicks();
            if (ProperRatingBar.this.listener != null) {
                ProperRatingBar.this.listener.onRatePicked(ProperRatingBar.this);
            }
        }
    }

    private interface TicksIterator {
        void onTick(View view, int i);
    }

    class C15262 implements TicksIterator {
        C15262() {
        }

        public void onTick(View tick, int position) {
            boolean z = true;
            if (ProperRatingBar.this.useSymbolicTick) {
                ProperRatingBar properRatingBar = ProperRatingBar.this;
                TextView textView = (TextView) tick;
                if (position > ProperRatingBar.this.lastSelectedTickIndex) {
                    z = false;
                }
                properRatingBar.redrawTickSelection(textView, z);
                return;
            }
            properRatingBar = ProperRatingBar.this;
            ImageView imageView = (ImageView) tick;
            if (position > ProperRatingBar.this.lastSelectedTickIndex) {
                z = false;
            }
            properRatingBar.redrawTickSelection(imageView, z);
        }
    }

    class C15273 implements TicksIterator {
        C15273() {
        }

        public void onTick(View tick, int position) {
            ProperRatingBar.this.updateTicksClickParameters(tick, position);
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C15281();
        boolean clickable;
        int rating;

        static class C15281 implements Creator<SavedState> {
            C15281() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            boolean z = true;
            super(in);
            this.rating = in.readInt();
            if (in.readByte() != (byte) 1) {
                z = false;
            }
            this.clickable = z;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.rating);
            out.writeByte((byte) (this.clickable ? 1 : 0));
        }
    }

    public ProperRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, C1529R.styleable.ProperRatingBar);
        this.totalTicks = a.getInt(C1529R.styleable.ProperRatingBar_prb_totalTicks, 5);
        this.rating = a.getInt(C1529R.styleable.ProperRatingBar_prb_defaultRating, 3);
        this.clickable = a.getBoolean(C1529R.styleable.ProperRatingBar_prb_clickable, false);
        this.symbolicTick = a.getString(C1529R.styleable.ProperRatingBar_prb_symbolicTick);
        if (this.symbolicTick == null) {
            this.symbolicTick = context.getString(DF_SYMBOLIC_TICK_RES);
        }
        this.customTextSize = a.getDimensionPixelSize(C1529R.styleable.ProperRatingBar_android_textSize, context.getResources().getDimensionPixelOffset(DF_SYMBOLIC_TEXT_SIZE_RES));
        this.customTextStyle = a.getInt(C1529R.styleable.ProperRatingBar_android_textStyle, 0);
        this.customTextNormalColor = a.getColor(C1529R.styleable.ProperRatingBar_prb_symbolicTickNormalColor, -16777216);
        this.customTextSelectedColor = a.getColor(C1529R.styleable.ProperRatingBar_prb_symbolicTickSelectedColor, DF_SYMBOLIC_TEXT_SELECTED_COLOR);
        this.tickNormalDrawable = a.getDrawable(C1529R.styleable.ProperRatingBar_prb_tickNormalDrawable);
        this.tickSelectedDrawable = a.getDrawable(C1529R.styleable.ProperRatingBar_prb_tickSelectedDrawable);
        this.tickSpacing = a.getDimensionPixelOffset(C1529R.styleable.ProperRatingBar_prb_tickSpacing, context.getResources().getDimensionPixelOffset(DF_TICK_SPACING_RES));
        afterInit();
        a.recycle();
    }

    private void afterInit() {
        if (this.rating > this.totalTicks) {
            this.rating = this.totalTicks;
        }
        this.lastSelectedTickIndex = this.rating - 1;
        if (this.tickNormalDrawable == null || this.tickSelectedDrawable == null) {
            this.useSymbolicTick = true;
        }
        addTicks(getContext());
    }

    private void addTicks(Context context) {
        removeAllViews();
        for (int i = 0; i < this.totalTicks; i++) {
            addTick(context, i);
        }
        redrawTicks();
    }

    private void addTick(Context context, int position) {
        if (this.useSymbolicTick) {
            addSymbolicTick(context, position);
        } else {
            addDrawableTick(context, position);
        }
    }

    private void addSymbolicTick(Context context, int position) {
        TextView tv = new TextView(context);
        tv.setText(this.symbolicTick);
        tv.setTextSize(0, (float) this.customTextSize);
        if (this.customTextStyle != 0) {
            tv.setTypeface(Typeface.DEFAULT, this.customTextStyle);
        }
        updateTicksClickParameters(tv, position);
        addView(tv);
    }

    private void addDrawableTick(Context context, int position) {
        ImageView iv = new ImageView(context);
        iv.setPadding(this.tickSpacing, this.tickSpacing, this.tickSpacing, this.tickSpacing);
        updateTicksClickParameters(iv, position);
        addView(iv);
    }

    private void updateTicksClickParameters(View tick, int position) {
        if (this.clickable) {
            tick.setTag(C1529R.id.prb_tick_tag_id, Integer.valueOf(position));
            tick.setOnClickListener(this.mTickClickedListener);
            return;
        }
        tick.setOnClickListener(null);
    }

    private void redrawTicks() {
        iterateTicks(new C15262());
    }

    private void redrawTickSelection(ImageView tick, boolean isSelected) {
        if (isSelected) {
            tick.setImageDrawable(this.tickSelectedDrawable);
        } else {
            tick.setImageDrawable(this.tickNormalDrawable);
        }
    }

    private void redrawTickSelection(TextView tick, boolean isSelected) {
        if (isSelected) {
            tick.setTextColor(this.customTextSelectedColor);
        } else {
            tick.setTextColor(this.customTextNormalColor);
        }
    }

    private void iterateTicks(TicksIterator iterator) {
        if (iterator == null) {
            throw new IllegalArgumentException("Iterator can't be null!");
        }
        for (int i = 0; i < getChildCount(); i++) {
            iterator.onTick(getChildAt(i), i);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.rating = this.rating;
        savedState.clickable = this.clickable;
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            setRating(savedState.rating);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public boolean isClickable() {
        return this.clickable;
    }

    public void toggleClickable() {
        setClickable(!this.clickable);
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        iterateTicks(new C15273());
    }

    public RatingListener getListener() {
        return this.listener;
    }

    public void setListener(RatingListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null!");
        }
        this.listener = listener;
    }

    public void removeRatingListener() {
        this.listener = null;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        if (rating > this.totalTicks) {
            rating = this.totalTicks;
        }
        this.rating = rating;
        this.lastSelectedTickIndex = rating - 1;
        redrawTicks();
    }

    public void setSymbolicTick(String tick) {
        this.symbolicTick = tick;
        afterInit();
    }

    public String getSymbolicTick() {
        return this.symbolicTick;
    }
}
