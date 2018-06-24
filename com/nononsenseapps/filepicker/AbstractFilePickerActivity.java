package com.nononsenseapps.filepicker;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment.OnFilePickedListener;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilePickerActivity<T> extends AppCompatActivity implements OnFilePickedListener {
    public static final String EXTRA_ALLOW_CREATE_DIR = "nononsense.intent.ALLOW_CREATE_DIR";
    public static final String EXTRA_ALLOW_EXISTING_FILE = "android.intent.extra.ALLOW_EXISTING_FILE";
    public static final String EXTRA_ALLOW_MULTIPLE = "android.intent.extra.ALLOW_MULTIPLE";
    public static final String EXTRA_MODE = "nononsense.intent.MODE";
    public static final String EXTRA_PATHS = "nononsense.intent.PATHS";
    public static final String EXTRA_SINGLE_CLICK = "nononsense.intent.SINGLE_CLICK";
    public static final String EXTRA_START_PATH = "nononsense.intent.START_PATH";
    public static final int MODE_DIR = 1;
    public static final int MODE_FILE = 0;
    public static final int MODE_FILE_AND_DIR = 2;
    public static final int MODE_NEW_FILE = 3;
    protected static final String TAG = "filepicker_fragment";
    protected boolean allowCreateDir = false;
    private boolean allowExistingFile = true;
    protected boolean allowMultiple = false;
    protected int mode = 0;
    protected boolean singleClick = false;
    protected String startPath = null;

    protected abstract AbstractFilePickerFragment<T> getFragment(@Nullable String str, int i, boolean z, boolean z2, boolean z3, boolean z4);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0955R.layout.nnf_activity_filepicker);
        Intent intent = getIntent();
        if (intent != null) {
            this.startPath = intent.getStringExtra(EXTRA_START_PATH);
            this.mode = intent.getIntExtra(EXTRA_MODE, this.mode);
            this.allowCreateDir = intent.getBooleanExtra(EXTRA_ALLOW_CREATE_DIR, this.allowCreateDir);
            this.allowMultiple = intent.getBooleanExtra(EXTRA_ALLOW_MULTIPLE, this.allowMultiple);
            this.allowExistingFile = intent.getBooleanExtra(EXTRA_ALLOW_EXISTING_FILE, this.allowExistingFile);
            this.singleClick = intent.getBooleanExtra(EXTRA_SINGLE_CLICK, this.singleClick);
        }
        FragmentManager fm = getSupportFragmentManager();
        AbstractFilePickerFragment<T> fragment = (AbstractFilePickerFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = getFragment(this.startPath, this.mode, this.allowMultiple, this.allowCreateDir, this.allowExistingFile, this.singleClick);
        }
        if (fragment != null) {
            fm.beginTransaction().replace(C0955R.id.fragment, fragment, TAG).commit();
        }
        setResult(0);
    }

    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
    }

    public void onFilePicked(@NonNull Uri file) {
        Intent i = new Intent();
        i.setData(file);
        setResult(-1, i);
        finish();
    }

    @TargetApi(16)
    public void onFilesPicked(@NonNull List<Uri> files) {
        Intent i = new Intent();
        i.putExtra(EXTRA_ALLOW_MULTIPLE, true);
        ArrayList<String> paths = new ArrayList();
        for (Uri file : files) {
            paths.add(file.toString());
        }
        i.putStringArrayListExtra(EXTRA_PATHS, paths);
        if (VERSION.SDK_INT >= 16) {
            ClipData clip = null;
            for (Uri file2 : files) {
                if (clip == null) {
                    clip = new ClipData("Paths", new String[0], new Item(file2));
                } else {
                    clip.addItem(new Item(file2));
                }
            }
            i.setClipData(clip);
        }
        setResult(-1, i);
        finish();
    }

    public void onCancelled() {
        setResult(0);
        finish();
    }
}
