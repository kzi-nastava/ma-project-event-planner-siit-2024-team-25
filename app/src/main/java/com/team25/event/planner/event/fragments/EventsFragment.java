package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventsBinding;
import com.team25.event.planner.databinding.FragmentTopEventsBinding;
import com.team25.event.planner.event.model.Event;

import java.util.ArrayList;
import java.util.Date;


public class EventsFragment extends Fragment {

    private ArrayList<Event> events = new ArrayList<Event>();
    private FragmentEventsBinding binding;
    int currentSelectedIndex;

    public static TopEventsFragment newInstance() {
        return new TopEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prepareEventList(events);
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

        Log.i("SADASDASDASD", String.valueOf(currentSelectedIndex));
        FragmentTransition.to(HomeEventsListFragment.newInstance(events), requireActivity(), false, binding.EventsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareEventList(ArrayList<Event> events) {
        events.clear();
        events.add(new Event(1, "Concert", "Stefan", new Date()));
        events.add(new Event(2, "Concert", "Petar", new Date()));
        events.add(new Event(3, "Concert", "Milos", new Date()));
        events.add(new Event(4, "Concert", "Nikola", new Date()));
        events.add(new Event(5, "Concert", "Milan", new Date()));
        events.add(new Event(11, "Concert", "Stefan", new Date()));
        events.add(new Event(21, "Concert", "Petar", new Date()));
        events.add(new Event(31, "Concert", "Milos", new Date()));
        events.add(new Event(41, "Concert", "Nikola", new Date()));
        events.add(new Event(51, "Concert", "Milan", new Date()));
    }
}