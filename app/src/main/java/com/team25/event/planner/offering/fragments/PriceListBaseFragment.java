package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentPriceListBaseBinding;


public class PriceListBaseFragment extends Fragment {

    private AuthViewModel authViewModel;
    private Long ownerId;
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
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        ownerId = authViewModel.getUserId();
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
        binding.buttonProductPriceList.setScaleX(1.2f);
        binding.buttonProductPriceList.setScaleY(1.2f);

        binding.buttonServicePriceList.setScaleX(1f);
        binding.buttonServicePriceList.setScaleY(1f);
        FragmentTransition.to(ProductPriceListFragment.newInstance(true,ownerId), requireActivity(), false, binding.fragmentContainer.getId());

    }

    private void setUpListeners() {
        binding.buttonProductPriceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.buttonProductPriceList.setScaleX(1.2f);
                binding.buttonProductPriceList.setScaleY(1.2f);

                binding.buttonServicePriceList.setScaleX(1f);
                binding.buttonServicePriceList.setScaleY(1f);
                FragmentTransition.to(ProductPriceListFragment.newInstance(true,ownerId), requireActivity(), false, binding.fragmentContainer.getId());
            }
        });
        binding.buttonServicePriceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.buttonServicePriceList.setScaleX(1.2f);
                binding.buttonServicePriceList.setScaleY(1.2f);

                binding.buttonProductPriceList.setScaleX(1f);
                binding.buttonProductPriceList.setScaleY(1f);
                FragmentTransition.to(ProductPriceListFragment.newInstance(false,ownerId), requireActivity(), false, binding.fragmentContainer.getId());

            }
        });
    }
}