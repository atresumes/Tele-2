package com.bignerdranch.expandablerecyclerview.ViewHolder;

import android.os.Build.VERSION;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import com.bignerdranch.expandablerecyclerview.ClickListeners.ParentItemClickListener;

public class ParentViewHolder extends ViewHolder implements OnClickListener {
    private static final long DEFAULT_ROTATE_DURATION_MS = 200;
    private static final boolean HONEYCOMB_AND_ABOVE = (VERSION.SDK_INT >= 11);
    private static final float INITIAL_POSITION = 0.0f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final float ROTATED_POSITION = 180.0f;
    private final String TAG = getClass().getSimpleName();
    private View mClickableView;
    private long mDuration = 200;
    private boolean mIsExpanded = false;
    private ParentItemClickListener mParentItemClickListener;
    private float mRotation = 0.0f;
    private boolean mRotationEnabled;

    public ParentViewHolder(View itemView) {
        super(itemView);
    }

    public void setCustomClickableViewOnly(int clickableViewId) {
        this.mClickableView = this.itemView.findViewById(clickableViewId);
        this.itemView.setOnClickListener(null);
        this.mClickableView.setOnClickListener(this);
        if (HONEYCOMB_AND_ABOVE && this.mRotationEnabled) {
            this.mClickableView.setRotation(this.mRotation);
        }
    }

    public void setCustomClickableViewAndItem(int clickableViewId) {
        this.mClickableView = this.itemView.findViewById(clickableViewId);
        this.itemView.setOnClickListener(this);
        this.mClickableView.setOnClickListener(this);
        if (HONEYCOMB_AND_ABOVE && this.mRotationEnabled) {
            this.mClickableView.setRotation(this.mRotation);
        }
    }

    public void setAnimationDuration(long animationDuration) {
        this.mRotationEnabled = true;
        this.mDuration = animationDuration;
        if (HONEYCOMB_AND_ABOVE && this.mRotationEnabled) {
            this.mClickableView.setRotation(this.mRotation);
        }
    }

    public void cancelAnimation() {
        this.mRotationEnabled = false;
        if (HONEYCOMB_AND_ABOVE && this.mRotationEnabled) {
            this.mClickableView.setRotation(this.mRotation);
        }
    }

    public void setMainItemClickToExpand() {
        if (this.mClickableView != null) {
            this.mClickableView.setOnClickListener(null);
        }
        this.itemView.setOnClickListener(this);
        this.mRotationEnabled = false;
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.mIsExpanded = isExpanded;
        if (!this.mRotationEnabled) {
            return;
        }
        if (this.mIsExpanded && this.mClickableView != null && HONEYCOMB_AND_ABOVE) {
            this.mClickableView.setRotation(180.0f);
        } else if (this.mClickableView != null && HONEYCOMB_AND_ABOVE) {
            this.mClickableView.setRotation(0.0f);
        }
    }

    public boolean isRotationEnabled() {
        return this.mRotationEnabled;
    }

    public void setRotation(float rotation) {
        this.mRotationEnabled = true;
        this.mRotation = rotation;
    }

    public ParentItemClickListener getParentItemClickListener() {
        return this.mParentItemClickListener;
    }

    public void setParentItemClickListener(ParentItemClickListener mParentItemClickListener) {
        this.mParentItemClickListener = mParentItemClickListener;
    }

    public void onClick(View v) {
        boolean z = true;
        if (this.mParentItemClickListener != null) {
            if (this.mClickableView != null && this.mRotationEnabled) {
                RotateAnimation rotateAnimation = new RotateAnimation(180.0f, 0.0f, 1, PIVOT_VALUE, 1, PIVOT_VALUE);
                this.mRotation = 0.0f;
                rotateAnimation.setDuration(this.mDuration);
                rotateAnimation.setFillAfter(true);
                this.mClickableView.startAnimation(rotateAnimation);
            }
            if (this.mIsExpanded) {
                z = false;
            }
            setExpanded(z);
            this.mParentItemClickListener.onParentItemClickListener(getLayoutPosition());
        }
    }
}
