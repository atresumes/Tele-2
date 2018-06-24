package com.wdullaer.materialdatetimepicker;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;

public class GravitySnapHelper extends LinearSnapHelper {
    private int gravity;
    private OrientationHelper horizontalHelper;
    private boolean isRtlHorizontal;
    private SnapListener listener;
    private OnScrollListener mScrollListener;
    private boolean snapping;
    private OrientationHelper verticalHelper;

    class C13931 extends OnScrollListener {
        C13931() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 2) {
                GravitySnapHelper.this.snapping = false;
            }
            if (newState == 0 && GravitySnapHelper.this.snapping && GravitySnapHelper.this.listener != null) {
                int position = GravitySnapHelper.this.getSnappedPosition(recyclerView);
                if (position != -1) {
                    GravitySnapHelper.this.listener.onSnap(position);
                }
                GravitySnapHelper.this.snapping = false;
            }
        }
    }

    public interface SnapListener {
        void onSnap(int i);
    }

    public GravitySnapHelper(int gravity) {
        this(gravity, null);
    }

    public GravitySnapHelper(int gravity, SnapListener snapListener) {
        this.mScrollListener = new C13931();
        if (gravity == GravityCompat.START || gravity == GravityCompat.END || gravity == 80 || gravity == 48) {
            this.gravity = gravity;
            this.listener = snapListener;
            return;
        }
        throw new IllegalArgumentException("Invalid gravity value. Use START | END | BOTTOM | TOP constants");
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        boolean z = true;
        if (recyclerView != null) {
            if ((this.gravity == GravityCompat.START || this.gravity == GravityCompat.END) && VERSION.SDK_INT >= 17) {
                if (recyclerView.getContext().getResources().getConfiguration().getLayoutDirection() != 1) {
                    z = false;
                }
                this.isRtlHorizontal = z;
            }
            if (this.listener != null) {
                recyclerView.addOnScrollListener(this.mScrollListener);
            }
        }
        super.attachToRecyclerView(recyclerView);
    }

    public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (!layoutManager.canScrollHorizontally()) {
            out[0] = 0;
        } else if (this.gravity == GravityCompat.START) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager), false);
        } else {
            out[0] = distanceToEnd(targetView, getHorizontalHelper(layoutManager), false);
        }
        if (!layoutManager.canScrollVertically()) {
            out[1] = 0;
        } else if (this.gravity == 48) {
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager), false);
        } else {
            out[1] = distanceToEnd(targetView, getVerticalHelper(layoutManager), false);
        }
        return out;
    }

    public View findSnapView(LayoutManager layoutManager) {
        View snapView = null;
        if (layoutManager instanceof LinearLayoutManager) {
            switch (this.gravity) {
                case 48:
                    snapView = findStartView(layoutManager, getVerticalHelper(layoutManager));
                    break;
                case 80:
                    snapView = findEndView(layoutManager, getVerticalHelper(layoutManager));
                    break;
                case GravityCompat.START /*8388611*/:
                    snapView = findStartView(layoutManager, getHorizontalHelper(layoutManager));
                    break;
                case GravityCompat.END /*8388613*/:
                    snapView = findEndView(layoutManager, getHorizontalHelper(layoutManager));
                    break;
            }
        }
        this.snapping = snapView != null;
        return snapView;
    }

    private int distanceToStart(View targetView, OrientationHelper helper, boolean fromEnd) {
        if (!this.isRtlHorizontal || fromEnd) {
            return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
        }
        return distanceToEnd(targetView, helper, true);
    }

    private int distanceToEnd(View targetView, OrientationHelper helper, boolean fromStart) {
        if (!this.isRtlHorizontal || fromStart) {
            return helper.getDecoratedEnd(targetView) - helper.getEndAfterPadding();
        }
        return distanceToStart(targetView, helper, true);
    }

    private View findStartView(LayoutManager layoutManager, OrientationHelper helper) {
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return null;
        }
        int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        if (firstChild == -1) {
            return null;
        }
        float visibleWidth;
        View child = layoutManager.findViewByPosition(firstChild);
        if (this.isRtlHorizontal) {
            visibleWidth = ((float) (helper.getTotalSpace() - helper.getDecoratedStart(child))) / ((float) helper.getDecoratedMeasurement(child));
        } else {
            visibleWidth = ((float) helper.getDecoratedEnd(child)) / ((float) helper.getDecoratedMeasurement(child));
        }
        boolean endOfList = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() + -1;
        if (visibleWidth > 0.5f && !endOfList) {
            return child;
        }
        if (endOfList) {
            return null;
        }
        return layoutManager.findViewByPosition(firstChild + 1);
    }

    private View findEndView(LayoutManager layoutManager, OrientationHelper helper) {
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return null;
        }
        int lastChild = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        if (lastChild == -1) {
            return null;
        }
        float visibleWidth;
        View child = layoutManager.findViewByPosition(lastChild);
        if (this.isRtlHorizontal) {
            visibleWidth = ((float) helper.getDecoratedEnd(child)) / ((float) helper.getDecoratedMeasurement(child));
        } else {
            visibleWidth = ((float) (helper.getTotalSpace() - helper.getDecoratedStart(child))) / ((float) helper.getDecoratedMeasurement(child));
        }
        boolean startOfList = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() == 0;
        if (visibleWidth > 0.5f && !startOfList) {
            return child;
        }
        if (startOfList) {
            return null;
        }
        return layoutManager.findViewByPosition(lastChild - 1);
    }

    private int getSnappedPosition(RecyclerView recyclerView) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (this.gravity == GravityCompat.START || this.gravity == 48) {
                return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            }
            if (this.gravity == GravityCompat.END || this.gravity == 80) {
                return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
        }
        return -1;
    }

    private OrientationHelper getVerticalHelper(LayoutManager layoutManager) {
        if (this.verticalHelper == null) {
            this.verticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.verticalHelper;
    }

    private OrientationHelper getHorizontalHelper(LayoutManager layoutManager) {
        if (this.horizontalHelper == null) {
            this.horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.horizontalHelper;
    }
}
