package com.trigma.tiktok.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

public class CommonListeners {

    public interface AlertCallBackWithButtonsInterface {
        void negativeClick();

        void neutralClick();

        void positiveClick();
    }

    public interface AlertCallBackInterface {
        void neutralClick();
    }

    public interface AlertCallBackWithOptionalInterface {
        void negativeClick();

        void neutralClick();

        void optional();

        void positiveClick();
    }

    public interface AlertCallBackWithButtonsInterface2 {
        void negativeClick();

        void neutralClick();

        void positiveClick(int i);
    }

    public interface AlertCallBackWithReturnButtonsInterface {
        void negativeClick(Dialog dialog);

        void neutralClick();

        void positiveClick(Dialog dialog);
    }

    public interface AlertInputCallBackInterface {
        void negativeClick();

        void positiveClick(EditText editText);
    }

    public interface AlertInputInterface {
        void positiveClick(String str);
    }

    public interface ChangeFragmentListener {
        void initiateFragment(int i, String str, Bundle bundle);
    }

    public interface ChangeLayoutRegister {
        void doSomething(long j, int i);
    }

    public interface CloseDrawerListener {
        void closeDrawer();
    }

    public interface ToggleClicked {
        void toggleClicked(boolean z);
    }

    public interface onRequestCpVideoCalling {
        void showCpConfirmationDialog(int i, @NonNull String[] strArr, @NonNull int[] iArr);
    }

    public interface onRequestSpVideoCalling {
        void showCpConfirmationDialog(int i, @NonNull String[] strArr, @NonNull int[] iArr);
    }
}
