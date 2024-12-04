package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentTopOfferingsBinding;
import com.team25.event.planner.event.fragments.TopEventsFragment;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.model.ServiceCard;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.util.ArrayList;


public class TopOfferingsFragment extends Fragment {

    private ArrayList<OfferingCard> offers = new ArrayList<OfferingCard>();
    private FragmentTopOfferingsBinding binding;
    private HomeOfferingViewModel _homeOfferingViewModel;
    int currentSelectedIndex;

    public TopOfferingsFragment(HomeOfferingViewModel homeOfferingViewModel){
        _homeOfferingViewModel = homeOfferingViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTopOfferingsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.to(TopOfferingsListFragment.newInstance(_homeOfferingViewModel), requireActivity(), false, binding.topOffersContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}