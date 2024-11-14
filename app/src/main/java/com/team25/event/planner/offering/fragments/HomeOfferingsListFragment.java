package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.offering.adapters.HomeOfferingListAdapter;
import com.team25.event.planner.offering.model.OfferingCard;

import java.util.ArrayList;

public class HomeOfferingsListFragment extends ListFragment {


    private static final String ARG_PARAM = "param";
    private ArrayList<OfferingCard> offeringCards;
    private HomeOfferingListAdapter adapter;


    public HomeOfferingsListFragment() {
        // Required empty public constructor
    }


    public static HomeOfferingsListFragment newInstance(ArrayList<OfferingCard> offeringCards) {
        HomeOfferingsListFragment fragment = new HomeOfferingsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, offeringCards);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.offeringCards = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new HomeOfferingListAdapter(getActivity(), offeringCards);
            setListAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_offerings_list, container, false);
    }
}