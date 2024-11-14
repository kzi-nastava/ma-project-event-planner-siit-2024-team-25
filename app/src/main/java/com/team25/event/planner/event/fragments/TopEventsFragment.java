package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentTopEventsBinding;
import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TopEventsFragment extends Fragment {

    private ArrayList<EventCard> eventCards = new ArrayList<EventCard>();
    private FragmentTopEventsBinding binding;
    int currentSelectedIndex;

    public static TopEventsFragment newInstance() {
        return new TopEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prepareEventList(eventCards);
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
        FragmentTransition.to(TopEventsListFragment.newInstance(eventCards), requireActivity(), false, binding.topEventsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareEventList(ArrayList<EventCard> eventCards){
        eventCards.clear();
        eventCards.add(new EventCard(1, "Concert", "Stefan", new Date()));
        eventCards.add(new EventCard(2, "Concert", "Petar", new Date()));
        eventCards.add(new EventCard(3, "Concert", "Milos", new Date()));
        eventCards.add(new EventCard(4, "Concert", "Nikola", new Date()));
        eventCards.add(new EventCard(5, "Concert", "Milan", new Date()));
    }
}