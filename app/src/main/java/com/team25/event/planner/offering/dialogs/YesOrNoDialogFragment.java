package com.team25.event.planner.offering.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;

public class YesOrNoDialogFragment extends DialogFragment {
    public interface ConfirmDialogListener {
        void onConfirm(); // Poziva se kada korisnik odabere "Yes"
        void onCancel();  // Poziva se kada korisnik odabere "No"
        void refresh();
    }

    private ConfirmDialogListener listener;
    private String name;

    public YesOrNoDialogFragment(ConfirmDialogListener listener, String name) {
        this.listener = listener;
        this.name = name;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Are you sure?")
                .setMessage("Do you want to delete " + this.name + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null) listener.onConfirm();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    if (listener != null) listener.onCancel();
                    dialog.dismiss();
                });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(listener!=null)listener.refresh();
    }

}