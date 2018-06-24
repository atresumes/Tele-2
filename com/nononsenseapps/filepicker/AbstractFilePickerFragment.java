package com.nononsenseapps.filepicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.nononsenseapps.filepicker.NewItemFragment.OnNewFolderListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractFilePickerFragment<T> extends Fragment implements LoaderCallbacks<SortedList<T>>, OnNewFolderListener, LogicHandler<T> {
    public static final String KEY_ALLOW_DIR_CREATE = "KEY_ALLOW_DIR_CREATE";
    public static final String KEY_ALLOW_EXISTING_FILE = "KEY_ALLOW_EXISTING_FILE";
    public static final String KEY_ALLOW_MULTIPLE = "KEY_ALLOW_MULTIPLE";
    protected static final String KEY_CURRENT_PATH = "KEY_CURRENT_PATH";
    public static final String KEY_MODE = "KEY_MODE";
    public static final String KEY_SINGLE_CLICK = "KEY_SINGLE_CLICK";
    public static final String KEY_START_PATH = "KEY_START_PATH";
    public static final int MODE_DIR = 1;
    public static final int MODE_FILE = 0;
    public static final int MODE_FILE_AND_DIR = 2;
    public static final int MODE_NEW_FILE = 3;
    protected boolean allowCreateDir = false;
    protected boolean allowExistingFile = true;
    protected boolean allowMultiple = false;
    protected boolean isLoading = false;
    protected LinearLayoutManager layoutManager;
    protected FileItemAdapter<T> mAdapter = null;
    protected final HashSet<T> mCheckedItems = new HashSet();
    protected final HashSet<CheckableViewHolder> mCheckedVisibleViewHolders = new HashSet();
    protected TextView mCurrentDirView;
    protected T mCurrentPath = null;
    protected EditText mEditTextFileName;
    protected SortedList<T> mFiles = null;
    protected OnFilePickedListener mListener;
    protected View mNewFileButtonContainer = null;
    protected View mRegularButtonContainer = null;
    protected Toast mToast = null;
    protected int mode = 0;
    protected RecyclerView recyclerView;
    protected boolean singleClick = false;

    public interface OnFilePickedListener {
        void onCancelled();

        void onFilePicked(@NonNull Uri uri);

        void onFilesPicked(@NonNull List<Uri> list);
    }

    class C09431 implements OnClickListener {
        C09431() {
        }

        public void onClick(View v) {
            AbstractFilePickerFragment.this.onClickCancel(v);
        }
    }

    class C09442 implements OnClickListener {
        C09442() {
        }

        public void onClick(View v) {
            AbstractFilePickerFragment.this.onClickOk(v);
        }
    }

    class C09453 implements OnClickListener {
        C09453() {
        }

        public void onClick(View v) {
            AbstractFilePickerFragment.this.onClickOk(v);
        }
    }

    class C09464 implements TextWatcher {
        C09464() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            AbstractFilePickerFragment.this.clearSelections();
        }
    }

    public class DirViewHolder extends ViewHolder implements OnClickListener, OnLongClickListener {
        public T file;
        public View icon;
        public TextView text;

        public DirViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            this.icon = v.findViewById(C0955R.id.item_icon);
            this.text = (TextView) v.findViewById(16908308);
        }

        public void onClick(View v) {
            AbstractFilePickerFragment.this.onClickDir(v, this);
        }

        public boolean onLongClick(View v) {
            return AbstractFilePickerFragment.this.onLongClickDir(v, this);
        }
    }

    public class CheckableViewHolder extends DirViewHolder {
        public CheckBox checkbox;
        final /* synthetic */ AbstractFilePickerFragment this$0;

        public CheckableViewHolder(final AbstractFilePickerFragment this$0, View v) {
            boolean nf;
            int i = 0;
            this.this$0 = this$0;
            super(v);
            if (this$0.mode == 3) {
                nf = true;
            } else {
                nf = false;
            }
            this.checkbox = (CheckBox) v.findViewById(C0955R.id.checkbox);
            CheckBox checkBox = this.checkbox;
            if (nf || this$0.singleClick) {
                i = 8;
            }
            checkBox.setVisibility(i);
            this.checkbox.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CheckableViewHolder.this.this$0.onClickCheckBox(CheckableViewHolder.this);
                }
            });
        }

        public void onClick(View v) {
            this.this$0.onClickCheckable(v, this);
        }

        public boolean onLongClick(View v) {
            return this.this$0.onLongClickCheckable(v, this);
        }
    }

    public class HeaderViewHolder extends ViewHolder implements OnClickListener {
        final TextView text;

        public HeaderViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            this.text = (TextView) v.findViewById(16908308);
        }

        public void onClick(View v) {
            AbstractFilePickerFragment.this.onClickHeader(v, this);
        }
    }

    public AbstractFilePickerFragment() {
        setRetainInstance(true);
    }

    protected FileItemAdapter<T> getAdapter() {
        return this.mAdapter;
    }

    protected FileItemAdapter<T> getDummyAdapter() {
        return new FileItemAdapter(this);
    }

    public void setArgs(@Nullable String startPath, int mode, boolean allowMultiple, boolean allowDirCreate, boolean allowExistingFile, boolean singleClick) {
        if (mode == 3 && allowMultiple) {
            throw new IllegalArgumentException("MODE_NEW_FILE does not support 'allowMultiple'");
        } else if (singleClick && allowMultiple) {
            throw new IllegalArgumentException("'singleClick' can not be used with 'allowMultiple'");
        } else {
            Bundle b = getArguments();
            if (b == null) {
                b = new Bundle();
            }
            if (startPath != null) {
                b.putString(KEY_START_PATH, startPath);
            }
            b.putBoolean(KEY_ALLOW_DIR_CREATE, allowDirCreate);
            b.putBoolean(KEY_ALLOW_MULTIPLE, allowMultiple);
            b.putBoolean(KEY_ALLOW_EXISTING_FILE, allowExistingFile);
            b.putBoolean(KEY_SINGLE_CLICK, singleClick);
            b.putInt(KEY_MODE, mode);
            setArguments(b);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflateRootView(inflater, container);
        Toolbar toolbar = (Toolbar) view.findViewById(C0955R.id.nnf_picker_toolbar);
        if (toolbar != null) {
            setupToolbar(toolbar);
        }
        this.recyclerView = (RecyclerView) view.findViewById(16908298);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        configureItemDecoration(inflater, this.recyclerView);
        this.mAdapter = new FileItemAdapter(this);
        this.recyclerView.setAdapter(this.mAdapter);
        view.findViewById(C0955R.id.nnf_button_cancel).setOnClickListener(new C09431());
        view.findViewById(C0955R.id.nnf_button_ok).setOnClickListener(new C09442());
        view.findViewById(C0955R.id.nnf_button_ok_newfile).setOnClickListener(new C09453());
        this.mNewFileButtonContainer = view.findViewById(C0955R.id.nnf_newfile_button_container);
        this.mRegularButtonContainer = view.findViewById(C0955R.id.nnf_button_container);
        this.mEditTextFileName = (EditText) view.findViewById(C0955R.id.nnf_text_filename);
        this.mEditTextFileName.addTextChangedListener(new C09464());
        this.mCurrentDirView = (TextView) view.findViewById(C0955R.id.nnf_current_dir);
        if (!(this.mCurrentPath == null || this.mCurrentDirView == null)) {
            this.mCurrentDirView.setText(getFullPath(this.mCurrentPath));
        }
        return view;
    }

    protected View inflateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(C0955R.layout.nnf_fragment_filepicker, container, false);
    }

    protected void configureItemDecoration(@NonNull LayoutInflater inflater, @NonNull RecyclerView recyclerView) {
        TypedArray attributes = getActivity().obtainStyledAttributes(new int[]{C0955R.attr.nnf_list_item_divider});
        Drawable divider = attributes.getDrawable(0);
        attributes.recycle();
        if (divider != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(divider));
        }
    }

    public void onClickCancel(@NonNull View view) {
        if (this.mListener != null) {
            this.mListener.onCancelled();
        }
    }

    public void onClickOk(@NonNull View view) {
        if (this.mListener != null) {
            if ((this.allowMultiple || this.mode == 0) && (this.mCheckedItems.isEmpty() || getFirstCheckedItem() == null)) {
                if (this.mToast == null) {
                    this.mToast = Toast.makeText(getActivity(), C0955R.string.nnf_select_something_first, 0);
                }
                this.mToast.show();
            } else if (this.mode == 3) {
                Uri result;
                String filename = getNewFileName();
                if (filename.startsWith("/")) {
                    result = toUri(getPath(filename));
                } else {
                    result = toUri(getPath(Utils.appendPath(getFullPath(this.mCurrentPath), filename)));
                }
                this.mListener.onFilePicked(result);
            } else if (this.allowMultiple) {
                this.mListener.onFilesPicked(toUri(this.mCheckedItems));
            } else if (this.mode == 0) {
                this.mListener.onFilePicked(toUri(getFirstCheckedItem()));
            } else if (this.mode == 1) {
                this.mListener.onFilePicked(toUri(this.mCurrentPath));
            } else if (this.mCheckedItems.isEmpty()) {
                this.mListener.onFilePicked(toUri(this.mCurrentPath));
            } else {
                this.mListener.onFilePicked(toUri(getFirstCheckedItem()));
            }
        }
    }

    @NonNull
    protected String getNewFileName() {
        return this.mEditTextFileName.getText().toString();
    }

    protected void setupToolbar(@NonNull Toolbar toolbar) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Nullable
    public T getFirstCheckedItem() {
        Iterator it = this.mCheckedItems.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    @NonNull
    protected List<Uri> toUri(@NonNull Iterable<T> files) {
        ArrayList<Uri> uris = new ArrayList();
        for (T file : files) {
            uris.add(toUri(file));
        }
        return uris;
    }

    public boolean isCheckable(@NonNull T data) {
        boolean checkable = false;
        if (!isDir(data)) {
            if (this.mode == 0 || this.mode == 2 || this.allowExistingFile) {
                checkable = true;
            }
            return checkable;
        } else if ((this.mode == 1 && this.allowMultiple) || (this.mode == 2 && this.allowMultiple)) {
            return true;
        } else {
            return false;
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnFilePickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFilePickedListener");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mCurrentPath == null) {
            String path;
            if (savedInstanceState != null) {
                this.mode = savedInstanceState.getInt(KEY_MODE, this.mode);
                this.allowCreateDir = savedInstanceState.getBoolean(KEY_ALLOW_DIR_CREATE, this.allowCreateDir);
                this.allowMultiple = savedInstanceState.getBoolean(KEY_ALLOW_MULTIPLE, this.allowMultiple);
                this.allowExistingFile = savedInstanceState.getBoolean(KEY_ALLOW_EXISTING_FILE, this.allowExistingFile);
                this.singleClick = savedInstanceState.getBoolean(KEY_SINGLE_CLICK, this.singleClick);
                path = savedInstanceState.getString(KEY_CURRENT_PATH);
                if (path != null) {
                    this.mCurrentPath = getPath(path.trim());
                }
            } else if (getArguments() != null) {
                this.mode = getArguments().getInt(KEY_MODE, this.mode);
                this.allowCreateDir = getArguments().getBoolean(KEY_ALLOW_DIR_CREATE, this.allowCreateDir);
                this.allowMultiple = getArguments().getBoolean(KEY_ALLOW_MULTIPLE, this.allowMultiple);
                this.allowExistingFile = getArguments().getBoolean(KEY_ALLOW_EXISTING_FILE, this.allowExistingFile);
                this.singleClick = getArguments().getBoolean(KEY_SINGLE_CLICK, this.singleClick);
                if (getArguments().containsKey(KEY_START_PATH)) {
                    path = getArguments().getString(KEY_START_PATH);
                    if (path != null) {
                        T file = getPath(path.trim());
                        if (isDir(file)) {
                            this.mCurrentPath = file;
                        } else {
                            this.mCurrentPath = getParent(file);
                            this.mEditTextFileName.setText(getName(file));
                        }
                    }
                }
            }
        }
        setModeView();
        if (this.mCurrentPath == null) {
            this.mCurrentPath = getRoot();
        }
        refresh(this.mCurrentPath);
    }

    protected void setModeView() {
        boolean nf;
        int i;
        int i2 = 0;
        if (this.mode == 3) {
            nf = true;
        } else {
            nf = false;
        }
        View view = this.mNewFileButtonContainer;
        if (nf) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        View view2 = this.mRegularButtonContainer;
        if (nf) {
            i2 = 8;
        }
        view2.setVisibility(i2);
        if (!nf && this.singleClick) {
            getActivity().findViewById(C0955R.id.nnf_button_ok).setVisibility(8);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0955R.menu.picker_actions, menu);
        menu.findItem(C0955R.id.nnf_action_createdir).setVisible(this.allowCreateDir);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (C0955R.id.nnf_action_createdir != menuItem.getItemId()) {
            return false;
        }
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            NewFolderFragment.showDialog(((AppCompatActivity) activity).getSupportFragmentManager(), this);
        }
        return true;
    }

    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        b.putString(KEY_CURRENT_PATH, this.mCurrentPath.toString());
        b.putBoolean(KEY_ALLOW_MULTIPLE, this.allowMultiple);
        b.putBoolean(KEY_ALLOW_EXISTING_FILE, this.allowExistingFile);
        b.putBoolean(KEY_ALLOW_DIR_CREATE, this.allowCreateDir);
        b.putBoolean(KEY_SINGLE_CLICK, this.singleClick);
        b.putInt(KEY_MODE, this.mode);
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    protected void refresh(@NonNull T nextPath) {
        if (hasPermission(nextPath)) {
            this.mCurrentPath = nextPath;
            this.isLoading = true;
            getLoaderManager().restartLoader(0, null, this);
            return;
        }
        handlePermission(nextPath);
    }

    protected void handlePermission(@NonNull T t) {
    }

    protected boolean hasPermission(@NonNull T t) {
        return true;
    }

    public Loader<SortedList<T>> onCreateLoader(int id, Bundle args) {
        return getLoader();
    }

    public void onLoadFinished(Loader<SortedList<T>> loader, SortedList<T> data) {
        this.isLoading = false;
        this.mCheckedItems.clear();
        this.mCheckedVisibleViewHolders.clear();
        this.mFiles = data;
        this.mAdapter.setList(data);
        if (this.mCurrentDirView != null) {
            this.mCurrentDirView.setText(getFullPath(this.mCurrentPath));
        }
        getLoaderManager().destroyLoader(0);
    }

    public void onLoaderReset(Loader<SortedList<T>> loader) {
        this.isLoading = false;
    }

    public int getItemViewType(int position, @NonNull T data) {
        if (isCheckable(data)) {
            return 2;
        }
        return 1;
    }

    public void onBindHeaderViewHolder(@NonNull HeaderViewHolder viewHolder) {
        viewHolder.text.setText("..");
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new HeaderViewHolder(LayoutInflater.from(getActivity()).inflate(C0955R.layout.nnf_filepicker_listitem_dir, parent, false));
            case 2:
                return new CheckableViewHolder(this, LayoutInflater.from(getActivity()).inflate(C0955R.layout.nnf_filepicker_listitem_checkable, parent, false));
            default:
                return new DirViewHolder(LayoutInflater.from(getActivity()).inflate(C0955R.layout.nnf_filepicker_listitem_dir, parent, false));
        }
    }

    public void onBindViewHolder(@NonNull DirViewHolder vh, int position, @NonNull T data) {
        vh.file = data;
        vh.icon.setVisibility(isDir(data) ? 0 : 8);
        vh.text.setText(getName(data));
        if (!isCheckable(data)) {
            return;
        }
        if (this.mCheckedItems.contains(data)) {
            this.mCheckedVisibleViewHolders.add((CheckableViewHolder) vh);
            ((CheckableViewHolder) vh).checkbox.setChecked(true);
            return;
        }
        this.mCheckedVisibleViewHolders.remove(vh);
        ((CheckableViewHolder) vh).checkbox.setChecked(false);
    }

    public void clearSelections() {
        Iterator it = this.mCheckedVisibleViewHolders.iterator();
        while (it.hasNext()) {
            ((CheckableViewHolder) it.next()).checkbox.setChecked(false);
        }
        this.mCheckedVisibleViewHolders.clear();
        this.mCheckedItems.clear();
    }

    public void onClickHeader(@NonNull View view, @NonNull HeaderViewHolder headerViewHolder) {
        goUp();
    }

    public void goUp() {
        goToDir(getParent(this.mCurrentPath));
    }

    public void onClickDir(@NonNull View view, @NonNull DirViewHolder viewHolder) {
        if (isDir(viewHolder.file)) {
            goToDir(viewHolder.file);
        }
    }

    protected boolean isItemVisible(T file) {
        return isDir(file) || this.mode == 0 || this.mode == 2 || (this.mode == 3 && this.allowExistingFile);
    }

    public void goToDir(@NonNull T file) {
        if (!this.isLoading) {
            this.mCheckedItems.clear();
            this.mCheckedVisibleViewHolders.clear();
            refresh(file);
        }
    }

    public boolean onLongClickDir(@NonNull View view, @NonNull DirViewHolder dirViewHolder) {
        return false;
    }

    public void onClickCheckable(@NonNull View view, @NonNull CheckableViewHolder viewHolder) {
        if (isDir(viewHolder.file)) {
            goToDir(viewHolder.file);
            return;
        }
        onLongClickCheckable(view, viewHolder);
        if (this.singleClick) {
            onClickOk(view);
        }
    }

    public boolean onLongClickCheckable(@NonNull View view, @NonNull CheckableViewHolder viewHolder) {
        if (3 == this.mode) {
            this.mEditTextFileName.setText(getName(viewHolder.file));
        }
        onClickCheckBox(viewHolder);
        return true;
    }

    public void onClickCheckBox(@NonNull CheckableViewHolder viewHolder) {
        if (this.mCheckedItems.contains(viewHolder.file)) {
            viewHolder.checkbox.setChecked(false);
            this.mCheckedItems.remove(viewHolder.file);
            this.mCheckedVisibleViewHolders.remove(viewHolder);
            return;
        }
        if (!this.allowMultiple) {
            clearSelections();
        }
        viewHolder.checkbox.setChecked(true);
        this.mCheckedItems.add(viewHolder.file);
        this.mCheckedVisibleViewHolders.add(viewHolder);
    }
}
