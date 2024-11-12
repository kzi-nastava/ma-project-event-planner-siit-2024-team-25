package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentServiceContainerBinding;
import com.team25.event.planner.databinding.FragmentServiceListBinding;
import com.team25.event.planner.product_service.model.Service;

import java.util.ArrayList;


public class ServiceContainerFragment extends Fragment {
    private FragmentServiceContainerBinding binding;

   private ArrayList<Service> services = new ArrayList<Service>();


    public ServiceContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prepareServiceList(services);
        binding = FragmentServiceContainerBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentTransition.to(ServiceListFragment.newInstance(services), requireActivity(), false, binding.serviceContainer.getId());
    }

    private void prepareServiceList(ArrayList<Service> services){
        services.clear();
        services.add(new Service(1L, "Music", "Milos",1));
        services.add(new Service(2L, "Music", "Milos",1));
        services.add(new Service(3L, "Music", "Milos",1));
        services.add(new Service(4L, "Music", "Milos",1));

    }
}