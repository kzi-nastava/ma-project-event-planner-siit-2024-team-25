package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentEventsBinding;
import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;
import java.util.Date;


public class EventsFragment extends Fragment {

    private ArrayList<EventCard> eventCards = new ArrayList<EventCard>();
    private FragmentEventsBinding binding;
    int currentSelectedIndex;

    public static TopEventsFragment newInstance() {
        return new TopEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prepareEventList(eventCards);
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
        FragmentTransition.toRight(HomeEventsListFragment.newInstance(eventCards), requireActivity(), false, binding.EventsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareEventList(ArrayList<EventCard> eventCards) {
        eventCards.clear();
        eventCards.add(new EventCard(1, "Concert", "Stefan", new Date()));
        eventCards.add(new EventCard(2, "Concert", "Petar", new Date()));
        eventCards.add(new EventCard(3, "Concert", "Milos", new Date()));
        eventCards.add(new EventCard(4, "Concert", "Nikola", new Date()));
        eventCards.add(new EventCard(5, "Concert", "Milan", new Date()));
        eventCards.add(new EventCard(11, "Concert", "Stefan", new Date()));
        eventCards.add(new EventCard(21, "Concert", "Petar", new Date()));
        eventCards.add(new EventCard(31, "Concert", "Milos", new Date()));
        eventCards.add(new EventCard(41, "Concert", "Nikola", new Date()));
        eventCards.add(new EventCard(51, "Concert", "Milan", new Date()));
    }
}