package com.nononsenseapps.filepicker;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.support.annotation.Nullable;
import java.io.File;

@SuppressLint({"Registered"})
public class FilePickerActivity extends AbstractFilePickerActivity<File> {
    protected AbstractFilePickerFragment<File> getFragment(@Nullable String startPath, int mode, boolean allowMultiple, boolean allowCreateDir, boolean allowExistingFile, boolean singleClick) {
        AbstractFilePickerFragment<File> fragment = new FilePickerFragment();
        fragment.setArgs(startPath != null ? startPath : Environment.getExternalStorageDirectory().getPath(), mode, allowMultiple, allowCreateDir, allowExistingFile, singleClick);
        return fragment;
    }
}
