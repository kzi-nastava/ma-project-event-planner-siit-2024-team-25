package com.team25.event.planner.service.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.team25.event.planner.databinding.FragmentFilterServiceBinding;

public class FilterServiceFragment extends BottomSheetDialogFragment {

    private FragmentFilterServiceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterServiceBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

}