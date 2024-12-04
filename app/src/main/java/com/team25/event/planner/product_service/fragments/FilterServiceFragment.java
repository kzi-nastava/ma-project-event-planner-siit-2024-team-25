package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.team25.event.planner.databinding.FragmentFilterServiceBinding;
import com.team25.event.planner.product_service.viewModels.ServiceCardsViewModel;

public class FilterServiceFragment extends BottomSheetDialogFragment {

    private FragmentFilterServiceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterServiceBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

}