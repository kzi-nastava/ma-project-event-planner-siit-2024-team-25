package com.team25.event.planner.core.dialogs;

import android.app.AlertDialog;
import android.content.Context;

public class DialogHelper {
    public static void showSuccessDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Success")
                .setMessage(message)
                .setIcon(android.R.drawable.checkbox_on_background)
                .setPositiveButton("Okay", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    public static void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton("Try again", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
