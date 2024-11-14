package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentHomeOfferingsBinding;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.model.ServiceCard;

import java.util.ArrayList;


public class HomeOfferingsFragment extends Fragment {

    private ArrayList<OfferingCard> offeringCards = new ArrayList<OfferingCard>();
    private FragmentHomeOfferingsBinding binding;
    int currentSelectedIndex;

    public static HomeOfferingsFragment newInstance() {
        return new HomeOfferingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prepareOfferingList(offeringCards);
        binding = FragmentHomeOfferingsBinding.inflate(inflater, container, false);
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
        FragmentTransition.toLeft(HomeOfferingsListFragment.newInstance(offeringCards), requireActivity(), false, binding.OfferingsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareOfferingList(ArrayList<OfferingCard> offeringCards) {
        offeringCards.clear();
        offeringCards.add(new ProductCard(1, "Product 1", 17500, "Stefan"));
        offeringCards.add(new ProductCard(2, "Product 2", 17500, "Stefan"));
        offeringCards.add(new ProductCard(3, "Product 3", 17500, "Stefan"));
        offeringCards.add(new ProductCard(4, "Product 4", 17500, "Stefan"));
        offeringCards.add(new ProductCard(5, "Product 5", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(6, "Service 1", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(7, "Service 2", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(8, "Service 3", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(9, "Service 4", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(10, "Service 5", 17500, "Stefan"));
    }
}