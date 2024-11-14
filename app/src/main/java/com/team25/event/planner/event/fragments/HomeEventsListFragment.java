package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;

public class HomeEventsListFragment extends ListFragment {


    private static final String ARG_PARAM = "param";
    private ArrayList<EventCard> eventCards;
    private HomeEventListAdapter adapter;


    public HomeEventsListFragment() {
        // Required empty public constructor
    }


    public static HomeEventsListFragment newInstance(ArrayList<EventCard> eventCards) {
        HomeEventsListFragment fragment = new HomeEventsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, eventCards);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.eventCards = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new HomeEventListAdapter(getActivity(), eventCards);
            setListAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_events_list, container, false);
    }
}