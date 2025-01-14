package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.event.adapters.TopEventsListAdapter;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;

import java.util.ArrayList;


public class TopEventsListFragment extends ListFragment {

    private TopEventsListAdapter adapter;
    private ArrayList<EventCard> topEventCards;
    private HomeEventViewModel homeEventViewModel;
    private static final String ARG_PARAM = "param";

    private TopEventsListFragment(HomeEventViewModel homeEventViewModel) {
        this.homeEventViewModel = homeEventViewModel;
    }

    public static TopEventsListFragment newInstance(HomeEventViewModel homeEventViewModel) {
        return new TopEventsListFragment(homeEventViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeEventViewModel.topEvents.observe(getViewLifecycleOwner(), (eventCards -> {
            NavController navController = Navigation.findNavController(requireView());
            adapter = new TopEventsListAdapter(requireContext(), eventCards, navController, this::onFavoriteToggle);
            setListAdapter(adapter);
        }));
        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        authViewModel.interceptorAdded.observe(getViewLifecycleOwner(), added -> {
            if (added) {
                homeEventViewModel.getTopEvents();
            }
        });
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                homeEventViewModel.setUserId(null);
            } else {
                homeEventViewModel.setUserId(user.getId());
            }
        });
        return inflater.inflate(R.layout.fragment_top_event_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        homeEventViewModel = new ViewModelProvider(requireActivity()).get(HomeEventViewModel.class);
        if (getArguments() != null) {
            NavController navController = Navigation.findNavController(requireView());
            topEventCards = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new TopEventsListAdapter(getActivity(), topEventCards, navController, this::onFavoriteToggle);
            setListAdapter(adapter);
        }
    }

    private void onFavoriteToggle(EventCard event) {
        homeEventViewModel.toggleFavorite(event);
    }
}