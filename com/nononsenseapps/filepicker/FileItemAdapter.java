package com.nononsenseapps.filepicker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment.DirViewHolder;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment.HeaderViewHolder;

public class FileItemAdapter<T> extends Adapter<ViewHolder> {
    protected SortedList<T> mList = null;
    protected final LogicHandler<T> mLogic;

    public FileItemAdapter(@NonNull LogicHandler<T> logic) {
        this.mLogic = logic;
    }

    public void setList(@Nullable SortedList<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.mLogic.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int headerPosition) {
        if (headerPosition == 0) {
            this.mLogic.onBindHeaderViewHolder((HeaderViewHolder) viewHolder);
            return;
        }
        int pos = headerPosition - 1;
        this.mLogic.onBindViewHolder((DirViewHolder) viewHolder, pos, this.mList.get(pos));
    }

    public int getItemViewType(int headerPosition) {
        if (headerPosition == 0) {
            return 0;
        }
        int pos = headerPosition - 1;
        return this.mLogic.getItemViewType(pos, this.mList.get(pos));
    }

    public int getItemCount() {
        if (this.mList == null) {
            return 0;
        }
        return this.mList.size() + 1;
    }

    @Nullable
    protected T getItem(int position) {
        if (position == 0) {
            return null;
        }
        return this.mList.get(position - 1);
    }
}
