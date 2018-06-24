package com.nononsenseapps.filepicker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import com.nononsenseapps.filepicker.NewItemFragment.OnNewFolderListener;

public class NewFolderFragment extends NewItemFragment {
    private static final String TAG = "new_folder_fragment";

    public static void showDialog(@NonNull FragmentManager fm, @Nullable OnNewFolderListener listener) {
        NewItemFragment d = new NewFolderFragment();
        d.setListener(listener);
        d.show(fm, TAG);
    }

    protected boolean validateName(@Nullable String itemName) {
        return Utils.isValidFileName(itemName);
    }
}
