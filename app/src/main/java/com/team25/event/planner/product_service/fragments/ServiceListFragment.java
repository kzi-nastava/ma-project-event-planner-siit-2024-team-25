package com.team25.event.planner.product_service.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
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
    private ServiceCardsViewModel serviceCardsViewModel;
    private boolean filter;
    private String nameFilter;
    private String priceFilter;
    private Boolean availableFilter;
    private Long eventTypeId;
    private Long offeringCategoryId;

    public ServiceListFragment(ServiceCardsViewModel vm, boolean f, String n, String p, Boolean a,Long eventTypeId, Long offeringCategoryId) {
        this.serviceCardsViewModel = vm;
        this.filter = f;
        this.nameFilter = n;
        this.priceFilter = p;
        this.availableFilter = a;
        this.eventTypeId = eventTypeId;
        this.offeringCategoryId = offeringCategoryId;
    }

    public static ServiceListFragment newInstance(ServiceCardsViewModel vm, boolean f, String n, String p, Boolean a, Long eventTypeId, Long offeringCategoryId){

        return new ServiceListFragment(vm,f,n,p,a, eventTypeId, offeringCategoryId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        serviceCardsViewModel = new ViewModelProvider(requireActivity()).get(ServiceCardsViewModel.class);
        if(filter){
            serviceCardsViewModel.setupFilter(nameFilter, priceFilter, availableFilter, eventTypeId, offeringCategoryId);
        }else{
            serviceCardsViewModel.filterServices();
        }

        setObserves();
        return inflater.inflate(R.layout.fragment_service_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //serviceCardsViewModel.filterServices();
    }

    public void setObserves(){
        serviceCardsViewModel.services.observe(getViewLifecycleOwner(), (serviceCard ->{
            adapter = new ServiceCardsAdapter(requireContext(), serviceCard);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }));

    }
}