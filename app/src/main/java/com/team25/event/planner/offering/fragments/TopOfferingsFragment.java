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
import com.team25.event.planner.offering.model.Offering;
import com.team25.event.planner.offering.model.Product;
import com.team25.event.planner.offering.model.Service;

import java.util.ArrayList;
import java.util.Date;


public class TopOfferingsFragment extends Fragment {

    private ArrayList<Offering> offers = new ArrayList<Offering>();
    private FragmentTopOfferingsBinding binding;
    int currentSelectedIndex;

    public static TopEventsFragment newInstance() {
        return new TopEventsFragment();
    }

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

    private void prepareOffersList(ArrayList<Offering> Offering){
        offers.clear();
        offers.add(new Service(1, "service 1", 1200, "Stefan", "opis 1 "));
        offers.add(new Product(2, "Product 2", 1200, "Stefan","opis 2 "));
        offers.add(new Service(3, "service 3", 1200, "Stefan", "opis 3 "));
        offers.add(new Service(4, "service 4", 1200, "Stefan", "opis 4 "));
        offers.add(new Product(5, "Product 5", 1200, "Stefan", "opis 5 "));
    }
}