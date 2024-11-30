package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentTopEventsBinding;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;


public class TopEventsFragment extends Fragment {

    private HomeEventViewModel _homeEventViewModel;
    private FragmentTopEventsBinding binding;
    int currentSelectedIndex;

    public TopEventsFragment(HomeEventViewModel homeEventViewModel){
        _homeEventViewModel = homeEventViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTopEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.to(TopEventsListFragment.newInstance(_homeEventViewModel), requireActivity(), false, binding.topEventsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}