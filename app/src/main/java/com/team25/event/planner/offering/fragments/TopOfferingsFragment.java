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

import java.util.ArrayList;


public class TopOfferingsFragment extends Fragment {

    private ArrayList<OfferingCard> offers = new ArrayList<OfferingCard>();
    private FragmentTopOfferingsBinding binding;
    int currentSelectedIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prepareOffersList(offers);
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

        Log.i("SADASDASDASD", String.valueOf(currentSelectedIndex));
        FragmentTransition.to(TopOfferingsListFragment.newInstance(offers), requireActivity(), false, binding.topOffersContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareOffersList(ArrayList<OfferingCard> OfferingCard){
        offers.clear();
        offers.add(new ServiceCard(1, "Service 1", 1200, "Stefan", 5));
        offers.add(new ProductCard(2, "Product 2", 1200, "Stefan", 5));
        offers.add(new ServiceCard(3, "Service 3", 1200, "Stefan",5));
        offers.add(new ServiceCard(4, "Service 4", 1200, "Stefan",5));
        offers.add(new ProductCard(5, "Product 5", 1200, "Stefan",5));
    }
}