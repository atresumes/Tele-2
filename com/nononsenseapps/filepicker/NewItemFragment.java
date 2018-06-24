package com.nononsenseapps.filepicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public abstract class NewItemFragment extends DialogFragment {
    private OnNewFolderListener listener = null;

    public interface OnNewFolderListener {
        void onNewFolder(@NonNull String str);
    }

    class C09541 implements OnShowListener {
        C09541() {
        }

        public void onShow(DialogInterface dialog1) {
            final AlertDialog dialog = (AlertDialog) dialog1;
            final EditText editText = (EditText) dialog.findViewById(C0955R.id.edit_text);
            if (editText == null) {
                throw new NullPointerException("Could not find an edit text in the dialog");
            }
            dialog.getButton(-2).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            final Button ok = dialog.getButton(-1);
            ok.setEnabled(false);
            ok.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    String itemName = editText.getText().toString();
                    if (NewItemFragment.this.validateName(itemName)) {
                        if (NewItemFragment.this.listener != null) {
                            NewItemFragment.this.listener.onNewFolder(itemName);
                        }
                        dialog.dismiss();
                    }
                }
            });
            editText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void afterTextChanged(Editable s) {
                    ok.setEnabled(NewItemFragment.this.validateName(s.toString()));
                }
            });
        }
    }

    protected abstract boolean validateName(String str);

    public void setListener(@Nullable OnNewFolderListener listener) {
        this.listener = listener;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());
        builder.setView(C0955R.layout.nnf_dialog_folder_name).setTitle(C0955R.string.nnf_new_folder).setNegativeButton(17039360, null).setPositiveButton(17039370, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new C09541());
        return dialog;
    }
}
