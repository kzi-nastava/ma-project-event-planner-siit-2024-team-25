package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;

public class HomeEventsListFragment extends ListFragment {
    private HomeEventListAdapter adapter;
    private HomeEventViewModel homeEventViewModel;

    public HomeEventsListFragment() {
    }

    private HomeEventsListFragment(HomeEventViewModel homeEventViewModel) {
        this.homeEventViewModel = homeEventViewModel;
    }

    public static HomeEventsListFragment newInstance(HomeEventViewModel homeEventViewModel) {
        return new HomeEventsListFragment(homeEventViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeEventViewModel.events.observe(getViewLifecycleOwner(), (eventCards -> {
            adapter = new HomeEventListAdapter(getActivity(), eventCards);
            setListAdapter(adapter);
        }));
        return inflater.inflate(R.layout.fragment_home_events_list, container, false);
    }
}