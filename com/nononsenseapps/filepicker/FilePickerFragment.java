package com.nononsenseapps.filepicker;

import android.net.Uri;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.util.SortedList;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.widget.Toast;
import java.io.File;

public class FilePickerFragment extends AbstractFilePickerFragment<File> {
    protected static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private File mRequestedPath = null;
    protected boolean showHiddenItems = false;

    public void showHiddenItems(boolean showHiddenItems) {
        this.showHiddenItems = showHiddenItems;
    }

    public boolean areHiddenItemsShown() {
        return this.showHiddenItems;
    }

    protected boolean hasPermission(@NonNull File path) {
        return ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    protected void handlePermission(@NonNull File path) {
        this.mRequestedPath = path;
        requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length == 0) {
            if (this.mListener != null) {
                this.mListener.onCancelled();
            }
        } else if (grantResults[0] != 0) {
            Toast.makeText(getContext(), C0955R.string.nnf_permission_external_write_denied, 0).show();
            if (this.mListener != null) {
                this.mListener.onCancelled();
            }
        } else if (this.mRequestedPath != null) {
            refresh(this.mRequestedPath);
        }
    }

    public boolean isDir(@NonNull File path) {
        return path.isDirectory();
    }

    @NonNull
    public String getName(@NonNull File path) {
        return path.getName();
    }

    @NonNull
    public File getParent(@NonNull File from) {
        if (from.getPath().equals(getRoot().getPath()) || from.getParentFile() == null) {
            return from;
        }
        return from.getParentFile();
    }

    @NonNull
    public File getPath(@NonNull String path) {
        return new File(path);
    }

    @NonNull
    public String getFullPath(@NonNull File path) {
        return path.getPath();
    }

    @NonNull
    public File getRoot() {
        return new File("/");
    }

    @NonNull
    public Uri toUri(@NonNull File file) {
        return FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
    }

    @NonNull
    public Loader<SortedList<File>> getLoader() {
        return new AsyncTaskLoader<SortedList<File>>(getActivity()) {
            FileObserver fileObserver;

            public SortedList<File> loadInBackground() {
                int i = 0;
                File[] listFiles = ((File) FilePickerFragment.this.mCurrentPath).listFiles();
                SortedList<File> files = new SortedList(File.class, new SortedListAdapterCallback<File>(FilePickerFragment.this.getDummyAdapter()) {
                    public int compare(File lhs, File rhs) {
                        return FilePickerFragment.this.compareFiles(lhs, rhs);
                    }

                    public boolean areContentsTheSame(File file, File file2) {
                        return file.getAbsolutePath().equals(file2.getAbsolutePath()) && file.isFile() == file2.isFile();
                    }

                    public boolean areItemsTheSame(File file, File file2) {
                        return areContentsTheSame(file, file2);
                    }
                }, listFiles == null ? 0 : listFiles.length);
                files.beginBatchedUpdates();
                if (listFiles != null) {
                    int length = listFiles.length;
                    while (i < length) {
                        File f = listFiles[i];
                        if (FilePickerFragment.this.isItemVisible(f)) {
                            files.add(f);
                        }
                        i++;
                    }
                }
                files.endBatchedUpdates();
                return files;
            }

            protected void onStartLoading() {
                super.onStartLoading();
                if (FilePickerFragment.this.mCurrentPath == null || !((File) FilePickerFragment.this.mCurrentPath).isDirectory()) {
                    FilePickerFragment.this.mCurrentPath = FilePickerFragment.this.getRoot();
                }
                this.fileObserver = new FileObserver(((File) FilePickerFragment.this.mCurrentPath).getPath(), 960) {
                    public void onEvent(int event, String path) {
                        C09501.this.onContentChanged();
                    }
                };
                this.fileObserver.startWatching();
                forceLoad();
            }

            protected void onReset() {
                super.onReset();
                if (this.fileObserver != null) {
                    this.fileObserver.stopWatching();
                    this.fileObserver = null;
                }
            }
        };
    }

    public void onNewFolder(@NonNull String name) {
        File folder = new File((File) this.mCurrentPath, name);
        if (folder.mkdir()) {
            refresh(folder);
        } else {
            Toast.makeText(getActivity(), C0955R.string.nnf_create_folder_error, 0).show();
        }
    }

    protected boolean isItemVisible(File file) {
        if (this.showHiddenItems || !file.isHidden()) {
            return super.isItemVisible(file);
        }
        return false;
    }

    protected int compareFiles(@NonNull File lhs, @NonNull File rhs) {
        if (lhs.isDirectory() && !rhs.isDirectory()) {
            return -1;
        }
        if (!rhs.isDirectory() || lhs.isDirectory()) {
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
        return 1;
    }
}
