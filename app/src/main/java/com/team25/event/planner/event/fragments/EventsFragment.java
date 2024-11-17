package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentEventsBinding;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;


public class EventsFragment extends Fragment {
    private HomeEventViewModel homeEventViewModel;
    private FragmentEventsBinding binding;

    public static TopEventsFragment newInstance() {
        return new TopEventsFragment();
    }

    public EventsFragment(HomeEventViewModel homeEventViewModel){
        this.homeEventViewModel = homeEventViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentEventsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.toRight(HomeEventsListFragment.newInstance(homeEventViewModel), requireActivity(), false, binding.EventsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}