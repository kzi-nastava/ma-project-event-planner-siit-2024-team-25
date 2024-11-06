package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.team25.event.planner.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team25.event.planner.databinding.FragmentTopEventListBinding;
import com.team25.event.planner.event.adapters.TopEventsListAdapter;
import com.team25.event.planner.model.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TopEventsListFragment extends ListFragment {

    private RecyclerView recyclerView;
    private TopEventsListAdapter adapter;
    private ArrayList<Event> topEvents;
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
            topEvents = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new TopEventsListAdapter(getActivity(), topEvents);
            setListAdapter(adapter);
        }
    }



    public static TopEventsListFragment newInstance(ArrayList<Event> events){
        TopEventsListFragment fragment = new TopEventsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, events);
        fragment.setArguments(args);
        return fragment;
    }
}