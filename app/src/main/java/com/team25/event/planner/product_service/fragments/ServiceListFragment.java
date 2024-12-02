package com.team25.event.planner.product_service.fragments;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team25.event.planner.R;
import com.team25.event.planner.event.adapters.TopEventsListAdapter;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;
import com.team25.event.planner.product_service.adapters.ServiceCardsAdapter;
import com.team25.event.planner.product_service.adapters.ServiceListAdapter;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.model.ServiceCard;
import com.team25.event.planner.product_service.viewModels.ServiceCardsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ServiceListFragment extends ListFragment {
    private ServiceCardsAdapter adapter;
    private List<ServiceCard> services;
    private ServiceCardsViewModel serviceCardsViewModel;
    private static final String ARG_PARAM = "param";

    public ServiceListFragment(ServiceCardsViewModel vm) {
        this.serviceCardsViewModel = vm;
    }
    public static ServiceListFragment newInstance(ServiceCardsViewModel vm){
        ServiceListFragment fragment = new ServiceListFragment(vm);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        serviceCardsViewModel.services.observe(getViewLifecycleOwner(), (serviceCard ->{
            adapter = new ServiceCardsAdapter(requireContext(), serviceCard);
            setListAdapter(adapter);
        }));
        return inflater.inflate(R.layout.fragment_service_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceCardsViewModel = new ViewModelProvider(requireActivity()).get(ServiceCardsViewModel.class);
        if (getArguments() != null) {
            services = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ServiceCardsAdapter(getActivity(), services);
            setListAdapter(adapter);
        }
    }


}