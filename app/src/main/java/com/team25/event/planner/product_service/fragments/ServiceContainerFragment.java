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

    private ArrayList<Service> services = new ArrayList<Service>();

    public ServiceContainerFragment(ServiceCardsViewModel vm) {
        serviceCardsViewModel = vm;
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
        FragmentTransition.to(ServiceListFragment.newInstance(serviceCardsViewModel), requireActivity(), false, binding.serviceContainer.getId());
    }

}