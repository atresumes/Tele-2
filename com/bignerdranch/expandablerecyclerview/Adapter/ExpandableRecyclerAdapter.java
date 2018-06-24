package com.bignerdranch.expandablerecyclerview.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.ViewGroup;
import com.bignerdranch.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ExpandableRecyclerAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder> extends Adapter<ViewHolder> implements ParentItemClickListener {
    public static final long CUSTOM_ANIMATION_DURATION_NOT_SET = -1;
    public static final int CUSTOM_ANIMATION_VIEW_NOT_SET = -1;
    public static final long DEFAULT_ROTATE_DURATION_MS = 200;
    private static final String STABLE_ID_LIST = "ExpandableRecyclerAdapter.StableIdList";
    private static final String STABLE_ID_MAP = "ExpandableRecyclerAdapter.StableIdMap";
    private static final String TAG = ExpandableRecyclerAdapter.class.getClass().getSimpleName();
    private static final int TYPE_CHILD = 1;
    private static final int TYPE_PARENT = 0;
    private long mAnimationDuration = -1;
    protected Context mContext;
    private int mCustomParentAnimationViewId = -1;
    private ExpandableRecyclerAdapterHelper mExpandableRecyclerAdapterHelper;
    protected List<Object> mItemList;
    private boolean mParentAndIconClickable = false;
    protected List<ParentObject> mParentItemList;
    private HashMap<Long, Boolean> mStableIdMap;

    public abstract void onBindChildViewHolder(CVH cvh, int i, Object obj);

    public abstract void onBindParentViewHolder(PVH pvh, int i, Object obj);

    public abstract CVH onCreateChildViewHolder(ViewGroup viewGroup);

    public abstract PVH onCreateParentViewHolder(ViewGroup viewGroup);

    public ExpandableRecyclerAdapter(Context context, List<ParentObject> parentItemList) {
        this.mContext = context;
        this.mParentItemList = parentItemList;
        this.mItemList = generateObjectList(parentItemList);
        this.mExpandableRecyclerAdapterHelper = new ExpandableRecyclerAdapterHelper(this.mItemList);
        this.mStableIdMap = generateStableIdMapFromList(this.mExpandableRecyclerAdapterHelper.getHelperItemList());
    }

    public ExpandableRecyclerAdapter(Context context, List<ParentObject> parentItemList, int customParentAnimationViewId) {
        this.mContext = context;
        this.mParentItemList = parentItemList;
        this.mItemList = generateObjectList(parentItemList);
        this.mExpandableRecyclerAdapterHelper = new ExpandableRecyclerAdapterHelper(this.mItemList);
        this.mStableIdMap = generateStableIdMapFromList(this.mExpandableRecyclerAdapterHelper.getHelperItemList());
        this.mCustomParentAnimationViewId = customParentAnimationViewId;
    }

    public ExpandableRecyclerAdapter(Context context, List<ParentObject> parentItemList, int customParentAnimationViewId, long animationDuration) {
        this.mContext = context;
        this.mParentItemList = parentItemList;
        this.mItemList = generateObjectList(parentItemList);
        this.mExpandableRecyclerAdapterHelper = new ExpandableRecyclerAdapterHelper(this.mItemList);
        this.mStableIdMap = generateStableIdMapFromList(this.mExpandableRecyclerAdapterHelper.getHelperItemList());
        this.mCustomParentAnimationViewId = customParentAnimationViewId;
        this.mAnimationDuration = animationDuration;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            PVH pvh = onCreateParentViewHolder(viewGroup);
            pvh.setParentItemClickListener(this);
            return pvh;
        } else if (viewType == 1) {
            return onCreateChildViewHolder(viewGroup);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this.mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(position) instanceof ParentWrapper) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
            if (this.mParentAndIconClickable) {
                if (this.mCustomParentAnimationViewId != -1 && this.mAnimationDuration != -1) {
                    parentViewHolder.setCustomClickableViewAndItem(this.mCustomParentAnimationViewId);
                    parentViewHolder.setAnimationDuration(this.mAnimationDuration);
                } else if (this.mCustomParentAnimationViewId != -1) {
                    parentViewHolder.setCustomClickableViewAndItem(this.mCustomParentAnimationViewId);
                    parentViewHolder.cancelAnimation();
                } else {
                    parentViewHolder.setMainItemClickToExpand();
                }
            } else if (this.mCustomParentAnimationViewId != -1 && this.mAnimationDuration != -1) {
                parentViewHolder.setCustomClickableViewOnly(this.mCustomParentAnimationViewId);
                parentViewHolder.setAnimationDuration(this.mAnimationDuration);
            } else if (this.mCustomParentAnimationViewId != -1) {
                parentViewHolder.setCustomClickableViewOnly(this.mCustomParentAnimationViewId);
                parentViewHolder.cancelAnimation();
            } else {
                parentViewHolder.setMainItemClickToExpand();
            }
            parentViewHolder.setExpanded(((ParentWrapper) this.mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(position)).isExpanded());
            onBindParentViewHolder(parentViewHolder, position, this.mItemList.get(position));
        } else if (this.mItemList.get(position) == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        } else {
            onBindChildViewHolder((ChildViewHolder) holder, position, this.mItemList.get(position));
        }
    }

    public int getItemCount() {
        return this.mItemList.size();
    }

    public int getItemViewType(int position) {
        if (this.mItemList.get(position) instanceof ParentObject) {
            return 0;
        }
        if (this.mItemList.get(position) != null) {
            return 1;
        }
        throw new IllegalStateException("Null object added");
    }

    public void onParentItemClickListener(int position) {
        if (this.mItemList.get(position) instanceof ParentObject) {
            expandParent((ParentObject) this.mItemList.get(position), position);
        }
    }

    public void setParentClickableViewAnimationDefaultDuration() {
        this.mAnimationDuration = 200;
    }

    public void setParentClickableViewAnimationDuration(long animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    public void setCustomParentAnimationViewId(int customParentAnimationViewId) {
        this.mCustomParentAnimationViewId = customParentAnimationViewId;
    }

    public void setParentAndIconExpandOnClick(boolean parentAndIconClickable) {
        this.mParentAndIconClickable = parentAndIconClickable;
    }

    public void removeAnimation() {
        this.mCustomParentAnimationViewId = -1;
        this.mAnimationDuration = -1;
    }

    private void expandParent(ParentObject parentObject, int position) {
        ParentWrapper parentWrapper = (ParentWrapper) this.mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(position);
        if (parentWrapper != null) {
            List<Object> childObjectList;
            int i;
            if (parentWrapper.isExpanded()) {
                parentWrapper.setExpanded(false);
                this.mStableIdMap.put(Long.valueOf(parentWrapper.getStableId()), Boolean.valueOf(false));
                childObjectList = ((ParentObject) parentWrapper.getParentObject()).getChildObjectList();
                if (childObjectList != null) {
                    for (i = childObjectList.size() - 1; i >= 0; i--) {
                        this.mItemList.remove((position + i) + 1);
                        this.mExpandableRecyclerAdapterHelper.getHelperItemList().remove((position + i) + 1);
                        notifyItemRemoved((position + i) + 1);
                        Log.d(TAG, "Removed " + childObjectList.get(i).toString());
                    }
                    return;
                }
                return;
            }
            parentWrapper.setExpanded(true);
            this.mStableIdMap.put(Long.valueOf(parentWrapper.getStableId()), Boolean.valueOf(true));
            childObjectList = ((ParentObject) parentWrapper.getParentObject()).getChildObjectList();
            if (childObjectList != null) {
                for (i = 0; i < childObjectList.size(); i++) {
                    this.mItemList.add((position + i) + 1, childObjectList.get(i));
                    this.mExpandableRecyclerAdapterHelper.getHelperItemList().add((position + i) + 1, childObjectList.get(i));
                    notifyItemInserted((position + i) + 1);
                }
            }
        }
    }

    private HashMap<Long, Boolean> generateStableIdMapFromList(List<Object> itemList) {
        HashMap<Long, Boolean> parentObjectHashMap = new HashMap();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i) != null) {
                ParentWrapper parentWrapper = (ParentWrapper) this.mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(i);
                parentObjectHashMap.put(Long.valueOf(parentWrapper.getStableId()), Boolean.valueOf(parentWrapper.isExpanded()));
            }
        }
        return parentObjectHashMap;
    }

    private ArrayList<Object> generateObjectList(List<ParentObject> parentObjectList) {
        ArrayList<Object> objectList = new ArrayList();
        for (ParentObject parentObject : parentObjectList) {
            objectList.add(parentObject);
        }
        return objectList;
    }

    public Bundle onSaveInstanceState(Bundle savedInstanceStateBundle) {
        savedInstanceStateBundle.putSerializable(STABLE_ID_MAP, this.mStableIdMap);
        return savedInstanceStateBundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceStateBundle) {
        if (savedInstanceStateBundle != null && savedInstanceStateBundle.containsKey(STABLE_ID_MAP)) {
            this.mStableIdMap = (HashMap) savedInstanceStateBundle.getSerializable(STABLE_ID_MAP);
            int i = 0;
            while (i < this.mExpandableRecyclerAdapterHelper.getHelperItemList().size()) {
                if (this.mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(i) instanceof ParentWrapper) {
                    ParentWrapper parentWrapper = (ParentWrapper) this.mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(i);
                    if (this.mStableIdMap.containsKey(Long.valueOf(parentWrapper.getStableId()))) {
                        parentWrapper.setExpanded(((Boolean) this.mStableIdMap.get(Long.valueOf(parentWrapper.getStableId()))).booleanValue());
                        if (parentWrapper.isExpanded()) {
                            List<Object> childObjectList = ((ParentObject) parentWrapper.getParentObject()).getChildObjectList();
                            if (childObjectList != null) {
                                for (int j = 0; j < childObjectList.size(); j++) {
                                    i++;
                                    this.mItemList.add(i, childObjectList.get(j));
                                    this.mExpandableRecyclerAdapterHelper.getHelperItemList().add(i, childObjectList.get(j));
                                }
                            }
                        }
                    } else {
                        parentWrapper.setExpanded(false);
                    }
                }
                i++;
            }
            notifyDataSetChanged();
        }
    }
}
