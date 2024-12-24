package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.offering.adapters.HomeOfferingListAdapter;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.util.ArrayList;

public class HomeOfferingsListFragment extends ListFragment {
    private HomeOfferingListAdapter adapter;
    private HomeOfferingViewModel _homeOfferingViewModel;
    private Long _eventId;

    private HomeOfferingsListFragment(HomeOfferingViewModel homeOfferingViewModel, Long eventId) {
        this._homeOfferingViewModel = homeOfferingViewModel;
        this._eventId = eventId;
    }


    public static HomeOfferingsListFragment newInstance(HomeOfferingViewModel homeOfferingViewModel, Long eventId) {
        return new HomeOfferingsListFragment(homeOfferingViewModel,eventId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _homeOfferingViewModel.allOfferings.observe(getViewLifecycleOwner(), (offerings -> {
            NavController navController = Navigation.findNavController(requireView());
            adapter = new HomeOfferingListAdapter(getActivity(), offerings, navController, this._eventId);
            setListAdapter(adapter);
        }));
        return inflater.inflate(R.layout.fragment_home_offerings_list, container, false);
    }
}