package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentPriceListBaseBinding;


public class PriceListBaseFragment extends Fragment {

    FragmentPriceListBaseBinding binding;
    public PriceListBaseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPriceListBaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.toRight(new ProductPriceListFragment(), requireActivity(), false, binding.fragmentContainer.getId());

    }

    private void setUpListeners() {
        binding.buttonProductPriceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.toRight(new ProductPriceListFragment(), requireActivity(), false, binding.fragmentContainer.getId());
            }
        });
    }
}