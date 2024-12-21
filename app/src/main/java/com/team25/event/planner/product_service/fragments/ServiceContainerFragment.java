package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentServiceContainerBinding;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.model.ServiceCard;
import com.team25.event.planner.product_service.viewModels.ServiceCardsViewModel;

import java.util.ArrayList;

public class ServiceContainerFragment extends Fragment {
    private FragmentServiceContainerBinding binding;
    private ServiceCardsViewModel serviceCardsViewModel;
    private boolean filter;
    private String searchName;
    private String searchPrice;
    private Boolean searchAvailable;
    private Long eventTypeId;
    private Long offeringCategoryId;

    public ServiceContainerFragment(ServiceCardsViewModel vm, boolean filter, String name, String price, Boolean a,Long eventTypeId, Long offeringCategoryId) {
        serviceCardsViewModel = vm;
        this.filter = filter;
        this.searchName = name;
        this.searchPrice = price;
        this.searchAvailable = a;
        this.eventTypeId = eventTypeId;
        this.offeringCategoryId = offeringCategoryId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceContainerBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.to(ServiceListFragment.newInstance(serviceCardsViewModel,filter, searchName, searchPrice, searchAvailable, eventTypeId, offeringCategoryId), requireActivity(), false, binding.serviceContainer.getId());
    }

}