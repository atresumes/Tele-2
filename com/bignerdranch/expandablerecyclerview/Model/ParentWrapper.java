package com.bignerdranch.expandablerecyclerview.Model;

public class ParentWrapper {
    private boolean mIsExpanded = false;
    private Object mParentObject;
    private long mStableId;

    public ParentWrapper(Object parentObject, int stableId) {
        this.mParentObject = parentObject;
        this.mStableId = (long) stableId;
    }

    public Object getParentObject() {
        return this.mParentObject;
    }

    public void setParentObject(Object parentObject) {
        this.mParentObject = parentObject;
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.mIsExpanded = isExpanded;
    }

    public long getStableId() {
        return this.mStableId;
    }

    public void setStableId(long stableId) {
        this.mStableId = stableId;
    }
}
