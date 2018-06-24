package com.trigma.tiktok.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.internal.WebDialog;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface2;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithOptionalInterface;
import java.lang.reflect.Field;

public class DialogPopUps {
    static Dialog dialog;
    public static int selected_value = 0;

    public static void showCustomDialogWithButtons(Context activity, String message, String positive, String negative, String neutral, final AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface) {
        try {
            hideDialog();
            final Dialog dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.custom_dialog_layout);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            ((TextView) dialog.findViewById(C1020R.id.dialog_message)).setText(message);
            TextView actionPositive = (TextView) dialog.findViewById(C1020R.id.action_positive);
            TextView actionNegative = (TextView) dialog.findViewById(C1020R.id.action_negative);
            TextView actionNeutral = (TextView) dialog.findViewById(C1020R.id.action_neutral);
            if ((positive.isEmpty() || negative.isEmpty()) && !neutral.isEmpty()) {
                actionNegative.setVisibility(8);
                actionPositive.setVisibility(8);
                actionNeutral.setVisibility(0);
                actionNeutral.setText(neutral);
            } else if (!(positive.isEmpty() && negative.isEmpty()) && neutral.isEmpty()) {
                actionNegative.setVisibility(0);
                actionPositive.setVisibility(0);
                actionNeutral.setVisibility(8);
                actionNegative.setText(negative);
                actionPositive.setText(positive);
            } else {
                actionNegative.setVisibility(0);
                actionPositive.setVisibility(0);
                actionNeutral.setVisibility(0);
                actionNegative.setText(negative);
                actionPositive.setText(positive);
                actionNeutral.setText(neutral);
            }
            actionPositive.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface.positiveClick();
                    dialog.dismiss();
                }
            });
            actionNeutral.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface.neutralClick();
                    dialog.dismiss();
                }
            });
            actionNegative.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface.negativeClick();
                    dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void showAlertWithButtons(Context activity, String titleText, String message, String positive, String negative, String neutral, boolean isConfirm, boolean hideCancelIcon, AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface) {
        try {
            hideDialog();
            final Dialog dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.alert_new_popup);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView title = (TextView) dialog.findViewById(C1020R.id.title);
            ((TextView) dialog.findViewById(C1020R.id.dialog_message)).setText(message);
            TextView action_ok = (TextView) dialog.findViewById(C1020R.id.action_ok);
            TextView action_no = (TextView) dialog.findViewById(C1020R.id.action_no);
            ImageView img_cancel = (ImageView) dialog.findViewById(C1020R.id.img_cancel);
            ImageView img_alert = (ImageView) dialog.findViewById(C1020R.id.img_alert);
            action_ok.setVisibility(0);
            action_no.setVisibility(0);
            if (!TextUtils.isEmpty(titleText)) {
                title.setText(titleText);
            }
            if (!TextUtils.isEmpty(positive)) {
                action_ok.setText(positive);
            }
            if (!TextUtils.isEmpty(negative)) {
                action_no.setText(negative);
            }
            if (isConfirm) {
                img_alert.setImageResource(C1020R.drawable.confirmation_icon);
                img_cancel.setVisibility(8);
            } else if (hideCancelIcon) {
                img_cancel.setVisibility(8);
            }
            final AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            action_ok.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.positiveClick();
                    dialog.dismiss();
                }
            });
            alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            action_no.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.negativeClick();
                    dialog.dismiss();
                }
            });
            alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            img_cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.negativeClick();
                    dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void showSChedulePatientAlert(Context activity, String titleText, String message, String positive, String negative, String neutral, boolean isConfirm, AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface) {
        try {
            hideDialog();
            final Dialog dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.patient_schedule_dialog);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView title = (TextView) dialog.findViewById(C1020R.id.title);
            ((TextView) dialog.findViewById(C1020R.id.dialog_message)).setText(message);
            TextView action_ok = (TextView) dialog.findViewById(C1020R.id.action_ok);
            TextView action_no = (TextView) dialog.findViewById(C1020R.id.action_no);
            ImageView img_cancel = (ImageView) dialog.findViewById(C1020R.id.img_cancel);
            ImageView img_alert = (ImageView) dialog.findViewById(C1020R.id.img_alert);
            action_ok.setVisibility(0);
            action_no.setVisibility(0);
            if (!TextUtils.isEmpty(titleText)) {
                title.setText(titleText);
            }
            if (!TextUtils.isEmpty(positive)) {
                action_ok.setText(positive);
            }
            if (!TextUtils.isEmpty(negative)) {
                action_no.setText(negative);
            }
            if (isConfirm) {
                img_alert.setImageResource(C1020R.drawable.confirmation_icon);
                img_cancel.setVisibility(8);
            }
            final AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            action_ok.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.positiveClick();
                    dialog.dismiss();
                }
            });
            alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            action_no.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.negativeClick();
                    dialog.dismiss();
                }
            });
            alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            img_cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.negativeClick();
                    dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void showStatusAlert(Context activity, String titleText, String message, String positive, String negative, String neutral, boolean isConfirm, AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface) {
        try {
            hideDialog();
            final Dialog dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.status_alert);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView title = (TextView) dialog.findViewById(C1020R.id.title);
            ((TextView) dialog.findViewById(C1020R.id.dialog_message)).setText(message);
            LinearLayout lin_action_ok = (LinearLayout) dialog.findViewById(C1020R.id.lin_action_ok);
            LinearLayout lin_action_no = (LinearLayout) dialog.findViewById(C1020R.id.lin_action_no);
            TextView action_ok = (TextView) dialog.findViewById(C1020R.id.action_ok);
            TextView action_no = (TextView) dialog.findViewById(C1020R.id.action_no);
            ImageView img_cancel = (ImageView) dialog.findViewById(C1020R.id.img_cancel);
            ImageView img_alert = (ImageView) dialog.findViewById(C1020R.id.img_alert);
            action_ok.setVisibility(0);
            action_no.setVisibility(0);
            if (!TextUtils.isEmpty(titleText)) {
                title.setText(titleText);
            }
            if (!TextUtils.isEmpty(positive)) {
                action_ok.setText(positive);
            }
            if (!TextUtils.isEmpty(negative)) {
                action_no.setText(negative);
            }
            if (isConfirm) {
                img_alert.setImageResource(C1020R.drawable.confirmation_icon);
                img_cancel.setVisibility(8);
            }
            final AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            lin_action_ok.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.positiveClick();
                    dialog.dismiss();
                }
            });
            alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            lin_action_no.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.negativeClick();
                    dialog.dismiss();
                }
            });
            alertCallBackWithButtonsInterface2 = alertCallBackWithButtonsInterface;
            img_cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface2.neutralClick();
                    dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void showTimePicker(Context activity, String[] values, final AlertCallBackWithButtonsInterface2 alertCallBackWithButtonsInterface) {
        try {
            selected_value = 0;
            hideDialog();
            final Dialog dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.dialog_time_picker);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            NumberPicker npPicker = (NumberPicker) dialog.findViewById(C1020R.id.npPicker);
            setNumberPickerTextColor(npPicker, ContextCompat.getColor(activity, C1020R.color.black));
            npPicker.setMinValue(0);
            npPicker.setMaxValue(values.length - 1);
            npPicker.setDisplayedValues(values);
            npPicker.setWrapSelectorWheel(true);
            Button actionNegative = (Button) dialog.findViewById(C1020R.id.action_negative);
            ((Button) dialog.findViewById(C1020R.id.action_positive)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface.positiveClick(DialogPopUps.selected_value);
                    dialog.dismiss();
                }
            });
            actionNegative.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackWithButtonsInterface.negativeClick();
                    dialog.dismiss();
                }
            });
            npPicker.setOnValueChangedListener(new OnValueChangeListener() {
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    DialogPopUps.selected_value = newVal;
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        int count = numberPicker.getChildCount();
        int i = 0;
        while (i < count) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    Log.w("setNumberPi", e);
                } catch (IllegalAccessException e2) {
                    Log.w("setNumberPi", e2);
                } catch (IllegalArgumentException e3) {
                    Log.w("setNumr", e3);
                }
            } else {
                i++;
            }
        }
        return false;
    }

    public static void alertPopUp(Context activity, String message, String neutral, final AlertCallBackInterface alertCallBackInterface) {
        try {
            hideDialog();
            dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.custom_dialog_neutral_layout);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            ((TextView) dialog.findViewById(C1020R.id.dialog_message)).setText(message);
            TextView title = (TextView) dialog.findViewById(C1020R.id.title);
            if (message.equalsIgnoreCase(activity.getResources().getString(C1020R.string.your_account_has_been_deleted_or_deactivated_please_contact_our_support_for_more_information))) {
                title.setText(activity.getResources().getString(C1020R.string.app_name));
            } else if (message.equalsIgnoreCase(activity.getResources().getString(C1020R.string.please_update_your_app))) {
                title.setText(activity.getResources().getString(C1020R.string.app_name));
            } else if (message.equalsIgnoreCase(activity.getResources().getString(C1020R.string.your_prescription_has_been_changed_by_admin_please_log_in_into_the_app_again))) {
                title.setText(activity.getResources().getString(C1020R.string.app_name));
            }
            TextView actionNeutral = (TextView) dialog.findViewById(C1020R.id.action_neutral);
            actionNeutral.setText(neutral);
            actionNeutral.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackInterface.neutralClick();
                    DialogPopUps.dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("CPS_Dialog_crash", e2.getMessage());
        }
    }

    public static void confirmationPopUp(Context activity, String title, String message, final AlertCallBackInterface alertCallBackInterface) {
        try {
            hideDialog();
            final Dialog dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.confirmation_popup);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView dialogMessage = (TextView) dialog.findViewById(C1020R.id.dialog_message);
            TextView titleText = (TextView) dialog.findViewById(C1020R.id.title);
            TextView actionNeutral = (TextView) dialog.findViewById(C1020R.id.action_neutral);
            ImageView img = (ImageView) dialog.findViewById(C1020R.id.img);
            if (!TextUtils.isEmpty(message)) {
                dialogMessage.setText(message);
                if (message.equalsIgnoreCase(activity.getResources().getString(C1020R.string.this_application_requires_location_services_to_work))) {
                    actionNeutral.setText(activity.getResources().getString(C1020R.string.go_to_settings));
                }
            }
            if (!TextUtils.isEmpty(title)) {
                titleText.setText(title);
                if (title.equalsIgnoreCase(activity.getResources().getString(C1020R.string.alert))) {
                    img.setImageResource(C1020R.drawable.alert_icon);
                }
            }
            actionNeutral.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackInterface.neutralClick();
                    dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.v("CPS_Dialog_crash", e2.getMessage());
        }
    }

    public static void thanksPopUp(Context activity, String title, String message, String message2, String buttonText, boolean showText2, final AlertCallBackInterface alertCallBackInterface) {
        try {
            hideDialog();
            dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.thanks_popup);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView dialogMessage = (TextView) dialog.findViewById(C1020R.id.dialog_message);
            TextView titleText = (TextView) dialog.findViewById(C1020R.id.title);
            TextView text2 = (TextView) dialog.findViewById(C1020R.id.text2);
            if (showText2) {
                text2.setVisibility(0);
            } else {
                text2.setVisibility(8);
            }
            TextView actionNeutral = (TextView) dialog.findViewById(C1020R.id.action_neutral);
            if (!TextUtils.isEmpty(message)) {
                dialogMessage.setText(message);
            }
            if (!TextUtils.isEmpty(message2)) {
                text2.setText(message);
            }
            if (!TextUtils.isEmpty(title)) {
                titleText.setText(title);
            }
            if (!TextUtils.isEmpty(buttonText)) {
                actionNeutral.setText(title);
            }
            actionNeutral.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackInterface.neutralClick();
                    DialogPopUps.dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.v("CPS_Dialog_crash", e2.getMessage());
        }
    }

    public static void alertPopUpWithNoButton(Context activity, String message, String neutral) {
        try {
            hideDialog();
            dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.info_dialog);
            LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = 17;
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            ((TextView) dialog.findViewById(C1020R.id.dialog_message)).setText(message);
            TextView data = (TextView) dialog.findViewById(C1020R.id.data);
            RelativeLayout parent = (RelativeLayout) dialog.findViewById(C1020R.id.parent);
            data.setText(neutral);
            CommonUtils.textScrollable(data);
            parent.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.hideDialog();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void alertPopUp(Context activity, String message) {
        try {
            hideDialog();
            dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.custom_dialog_neutral_layout);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            ((TextView) dialog.findViewById(C1020R.id.dialog_message)).setText(message);
            TextView actionNeutral = (TextView) dialog.findViewById(C1020R.id.action_neutral);
            actionNeutral.setText(C1020R.string.ok);
            actionNeutral.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void showProgressDialog(Context activity, String message) {
        try {
            hideDialog();
            dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.progress_layout);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            if (VERSION.SDK_INT < 21) {
                ((ProgressBar) dialog.findViewById(C1020R.id.circular_progress)).setIndeterminateDrawable(activity.getResources().getDrawable(C1020R.drawable.progress));
            }
            if (!message.isEmpty()) {
                ((TextView) dialog.findViewById(C1020R.id.progress_msg)).setText(message);
            }
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void hideDialog() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openCamera(Context activity, final AlertCallBackWithButtonsInterface alertCallBackInterface) {
        try {
            hideDialog();
            dialog = new Dialog(activity, WebDialog.DEFAULT_THEME);
            dialog.setContentView(C1020R.layout.camera_gallery_popup);
            dialog.show();
            TextView camera_text = (TextView) dialog.findViewById(C1020R.id.camera_text);
            TextView open_gallery_text = (TextView) dialog.findViewById(C1020R.id.open_gallery_text);
            ((TextView) dialog.findViewById(C1020R.id.cancel_text)).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.neutralClick();
                }
            });
            camera_text.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.negativeClick();
                }
            });
            open_gallery_text.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.positiveClick();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.v("CPS_Dialog_crash", e2.getMessage());
        }
    }

    public static void openFilePickerDialog(Context activity, final AlertCallBackWithOptionalInterface alertCallBackInterface) {
        try {
            hideDialog();
            dialog = new Dialog(activity, WebDialog.DEFAULT_THEME);
            dialog.setContentView(C1020R.layout.video_attachment_dialog);
            dialog.show();
            TextView camera_text = (TextView) dialog.findViewById(C1020R.id.camera_text);
            TextView open_gallery_text = (TextView) dialog.findViewById(C1020R.id.open_gallery_text);
            TextView open_file_picker = (TextView) dialog.findViewById(C1020R.id.open_file_picker);
            ((TextView) dialog.findViewById(C1020R.id.cancel_text)).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.neutralClick();
                }
            });
            camera_text.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.negativeClick();
                }
            });
            open_gallery_text.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.positiveClick();
                }
            });
            open_file_picker.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.optional();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.v("CPS_Dialog_crash", e2.getMessage());
        }
    }

    public static void agreementPopUp(Context activity, String dataText, boolean hideCancelButton, final AlertCallBackInterface alertCallBackInterface) {
        try {
            hideDialog();
            final Dialog dialog = new Dialog(activity, C1020R.style.Theme.AppCompat.Translucent);
            dialog.setContentView(C1020R.layout.agreement_popup);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            ImageView img_cancel = (ImageView) dialog.findViewById(C1020R.id.img_cancel);
            if (hideCancelButton) {
                img_cancel.setVisibility(8);
            }
            ((TextView) dialog.findViewById(C1020R.id.data)).setText(Html.fromHtml(dataText));
            ((LinearLayout) dialog.findViewById(C1020R.id.linear_save)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertCallBackInterface.neutralClick();
                    dialog.dismiss();
                }
            });
            img_cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.v("CPS_Dialog_crash", e2.getMessage());
        }
    }

    public static void staffPromptLoginDialog(Context activity, final AlertCallBackWithButtonsInterface alertCallBackInterface) {
        try {
            hideDialog();
            dialog = new Dialog(activity, WebDialog.DEFAULT_THEME);
            dialog.setContentView(C1020R.layout.login_as_staff_dialog);
            dialog.getWindow().getAttributes().dimAmount = 0.6f;
            dialog.getWindow().addFlags(2);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            ImageView img_docstaff = (ImageView) dialog.findViewById(C1020R.id.img_docstaff);
            ((ImageView) dialog.findViewById(C1020R.id.img_doc)).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.positiveClick();
                }
            });
            img_docstaff.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DialogPopUps.dialog.dismiss();
                    alertCallBackInterface.negativeClick();
                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.v("CPS_Dialog_crash", e2.getMessage());
        }
    }
}
