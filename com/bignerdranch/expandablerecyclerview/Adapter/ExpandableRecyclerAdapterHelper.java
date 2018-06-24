package com.bignerdranch.expandablerecyclerview.Adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerAdapterHelper {
    private static final int INITIAL_STABLE_ID = 0;
    private static final String TAG = ExpandableRecyclerAdapterHelper.class.getSimpleName();
    private static int sCurrentId;
    private List<Object> mHelperItemList;

    public ExpandableRecyclerAdapterHelper(List<Object> itemList) {
        sCurrentId = 0;
        this.mHelperItemList = generateHelperItemList(itemList);
    }

    public List<Object> getHelperItemList() {
        return this.mHelperItemList;
    }

    public Object getHelperItemAtPosition(int position) {
        if (this.mHelperItemList.get(position) instanceof ParentWrapper) {
            return this.mHelperItemList.get(position);
        }
        return this.mHelperItemList.get(position);
    }

    public List<Object> generateHelperItemList(List<Object> itemList) {
        ArrayList<Object> parentWrapperList = new ArrayList();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i) instanceof ParentObject) {
                ParentWrapper parentWrapper = new ParentWrapper(itemList.get(i), sCurrentId);
                sCurrentId++;
                parentWrapperList.add(parentWrapper);
            } else {
                parentWrapperList.add(itemList.get(i));
            }
        }
        return parentWrapperList;
    }
}
