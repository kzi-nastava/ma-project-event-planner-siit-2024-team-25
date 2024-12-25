package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentMyEventsBinding;

public class MyEventsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMyEventsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_events, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        binding.createEventButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_myEventsFragment_to_eventFormFragment);
        });

        return binding.getRoot();
    }
}