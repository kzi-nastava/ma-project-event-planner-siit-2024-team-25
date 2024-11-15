package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentFilterServiceBinding;


public class FilterServiceFragment extends BottomSheetDialogFragment {

    private FragmentFilterServiceBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFilterServiceBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}