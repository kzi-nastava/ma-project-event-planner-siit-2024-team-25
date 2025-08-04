package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentEventsBinding;
import com.team25.event.planner.databinding.FragmentHomeOfferingsBinding;
import com.team25.event.planner.event.fragments.HomeEventsListFragment;
import com.team25.event.planner.event.fragments.TopEventsFragment;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.model.ServiceCard;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.util.ArrayList;


public class HomeOfferingsFragment extends Fragment {

    private HomeOfferingViewModel homeOfferingViewModel;
    private FragmentHomeOfferingsBinding binding;
    private Long _eventId;

    public HomeOfferingsFragment(HomeOfferingViewModel homeOfferingViewModel, Long eventId){
        this.homeOfferingViewModel = homeOfferingViewModel;
        this._eventId = eventId;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentHomeOfferingsBinding.inflate(inflater, container, false);
        binding.setViewModel(homeOfferingViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.toRight(HomeOfferingsListFragment.newInstance(homeOfferingViewModel, this._eventId), requireActivity(), false, binding.offeringsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}