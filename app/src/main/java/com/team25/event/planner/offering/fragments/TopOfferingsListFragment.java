package com.team25.event.planner.offering.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentTopOfferingsListBinding;
import com.team25.event.planner.offering.adapters.TopOfferingsListAdapter;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.util.ArrayList;

public class TopOfferingsListFragment extends ListFragment {

    private FragmentTopOfferingsListBinding binding;
    private TopOfferingsListAdapter adapter;
    private ArrayList<OfferingCard> _topOfferings;
    private HomeOfferingViewModel _homeOfferingViewModel;
    private static final String ARG_PARAM = "param";

    public TopOfferingsListFragment(HomeOfferingViewModel homeOfferingViewModel) {
        this._homeOfferingViewModel = homeOfferingViewModel;
    }

    public static TopOfferingsListFragment newInstance(HomeOfferingViewModel homeOfferingViewModel) {
        return new TopOfferingsListFragment(homeOfferingViewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        _homeOfferingViewModel.topOfferings.observe(getViewLifecycleOwner(), offeringCards -> {
            adapter = new TopOfferingsListAdapter(getContext(), offeringCards);
            setListAdapter(adapter);
        });
        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        authViewModel.interceptorAdded.observe(getViewLifecycleOwner(), added -> {
            if (added) {
                _homeOfferingViewModel.getTopOfferings();
            }
        });
        binding = FragmentTopOfferingsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _homeOfferingViewModel = new ViewModelProvider(requireActivity()).get(HomeOfferingViewModel.class);
        if (getArguments() != null) {
            _topOfferings = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new TopOfferingsListAdapter(getActivity(), _topOfferings);
            setListAdapter(adapter);
        }
    }


}
