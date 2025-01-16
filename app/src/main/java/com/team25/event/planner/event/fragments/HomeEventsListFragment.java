package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.event.model.EventCard;
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
            NavController navController = Navigation.findNavController(requireView());
            adapter = new HomeEventListAdapter(requireContext(), eventCards, navController, this::onFavoriteToggle);
            setListAdapter(adapter);
        }));

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        authViewModel.interceptorAdded.observe(getViewLifecycleOwner(), added -> {
            if (added) {
                homeEventViewModel.getAllEvents();
            }
        });
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                homeEventViewModel.setUserId(null);
            } else {
                homeEventViewModel.setUserId(user.getId());
            }
        });

        return inflater.inflate(R.layout.fragment_home_events_list, container, false);
    }

    private void onFavoriteToggle(EventCard event) {
        homeEventViewModel.toggleFavorite(event);
    }
}