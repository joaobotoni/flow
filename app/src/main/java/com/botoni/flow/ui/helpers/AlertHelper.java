package com.botoni.flow.ui.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
public class AlertHelper {

    public static void showSnackBarSucesso(View view, String message) {
        if (view != null && message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(
                            ContextCompat.getColor(view.getContext(), android.R.color.holo_green_dark)
                    )
                    .setTextColor(Color.WHITE)
                    .show();
        }
    }

    public static void showSnackBarErro(View view, String message) {
        if (view != null && message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(
                            ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark)
                    )
                    .setTextColor(Color.WHITE)
                    .show();
        }
    }

    public static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener) {
        if (context != null && title != null && message != null) {
            new MaterialAlertDialogBuilder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", positiveListener)
                    .setNegativeButton("Não", negativeListener)
                    .show();
        }
    }
}
