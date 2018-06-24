package com.nononsenseapps.filepicker;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment.DirViewHolder;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment.HeaderViewHolder;

public interface LogicHandler<T> {
    public static final int VIEWTYPE_CHECKABLE = 2;
    public static final int VIEWTYPE_DIR = 1;
    public static final int VIEWTYPE_HEADER = 0;

    @NonNull
    String getFullPath(@NonNull T t);

    int getItemViewType(int i, @NonNull T t);

    @NonNull
    Loader<SortedList<T>> getLoader();

    @NonNull
    String getName(@NonNull T t);

    @NonNull
    T getParent(@NonNull T t);

    @NonNull
    T getPath(@NonNull String str);

    @NonNull
    T getRoot();

    boolean isDir(@NonNull T t);

    void onBindHeaderViewHolder(@NonNull HeaderViewHolder headerViewHolder);

    void onBindViewHolder(@NonNull DirViewHolder dirViewHolder, int i, @NonNull T t);

    @NonNull
    ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i);

    @NonNull
    Uri toUri(@NonNull T t);
}
