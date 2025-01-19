package com.team25.event.planner.review.dialogs;

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
import com.team25.event.planner.offering.dialogs.YesOrNoDialogFragment;

public class ApproveReviewDialog extends DialogFragment {

    public interface ApproveReviewDialogListener {
        void onApprove(); // Poziva se kada korisnik odabere "Yes"
        void onReject();  // Poziva se kada korisnik odabere "No"
        void refresh();
    }

    private ApproveReviewDialog.ApproveReviewDialogListener listener;

    public ApproveReviewDialog(ApproveReviewDialog.ApproveReviewDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Are you sure?")
                .setMessage("Approve this review?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null) listener.onApprove();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    if (listener != null) listener.onReject();
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