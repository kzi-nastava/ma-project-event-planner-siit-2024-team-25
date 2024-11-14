package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventsBinding;
import com.team25.event.planner.databinding.FragmentHomeOfferingsBinding;
import com.team25.event.planner.event.fragments.HomeEventsListFragment;
import com.team25.event.planner.event.fragments.TopEventsFragment;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.offering.model.Offering;
import com.team25.event.planner.offering.model.Product;
import com.team25.event.planner.offering.model.Service;

import java.util.ArrayList;
import java.util.Date;


public class HomeOfferingsFragment extends Fragment {

    private ArrayList<Offering> offerings = new ArrayList<Offering>();
    private FragmentHomeOfferingsBinding binding;
    int currentSelectedIndex;

    public static HomeOfferingsFragment newInstance() {
        return new HomeOfferingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prepareOfferingList(offerings);
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
        FragmentTransition.to(HomeOfferingsListFragment.newInstance(offerings), requireActivity(), false, binding.OfferingsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareOfferingList(ArrayList<Offering> offerings) {
        offerings.clear();
        offerings.add(new Product(1, "Product 1", 17500, "Stefan"));
        offerings.add(new Product(2, "Product 2", 17500, "Stefan"));
        offerings.add(new Product(3, "Product 3", 17500, "Stefan"));
        offerings.add(new Product(4, "Product 4", 17500, "Stefan"));
        offerings.add(new Product(5, "Product 5", 17500, "Stefan"));
        offerings.add(new Service(6, "Service 1", 17500, "Stefan"));
        offerings.add(new Service(7, "Service 2", 17500, "Stefan"));
        offerings.add(new Service(8, "Service 3", 17500, "Stefan"));
        offerings.add(new Service(9, "Service 4", 17500, "Stefan"));
        offerings.add(new Service(10, "Service 5", 17500, "Stefan"));
    }
}