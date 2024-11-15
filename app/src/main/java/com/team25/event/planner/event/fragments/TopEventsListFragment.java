package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.team25.event.planner.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.event.adapters.TopEventsListAdapter;
import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;


public class TopEventsListFragment extends ListFragment {

    private TopEventsListAdapter adapter;
    private ArrayList<EventCard> topEventCards;
    private static final String ARG_PARAM = "param";

    public TopEventsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_top_event_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");
        if (getArguments() != null) {
            topEventCards = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new TopEventsListAdapter(getActivity(), topEventCards);
            setListAdapter(adapter);
        }
    }



    public static TopEventsListFragment newInstance(ArrayList<EventCard> eventCards){
        TopEventsListFragment fragment = new TopEventsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, eventCards);
        fragment.setArguments(args);
        return fragment;
    }
}