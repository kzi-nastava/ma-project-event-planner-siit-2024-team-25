package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentFavoriteEventsBinding;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.event.viewmodel.FavoriteEventsViewModel;


public class FavoriteEventsFragment extends Fragment {
    private FragmentFavoriteEventsBinding binding;
    private FavoriteEventsViewModel viewModel;
    private AuthViewModel authViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_events, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(FavoriteEventsViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupObservers();

        return binding.getRoot();
    }

    private void setupObservers() {
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                viewModel.setUserId(user.getUserId());
            }
        });

        viewModel.events.observe(getViewLifecycleOwner(), events -> {
            HomeEventListAdapter adapter = new HomeEventListAdapter(
                    getContext(), events, navController, this::removeFromFavorites
            );
            binding.list.setAdapter(adapter);
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.loadingSpinner.setVisibility(View.VISIBLE);
            } else {
                binding.loadingSpinner.setVisibility(View.GONE);
            }
        });

        viewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void removeFromFavorites(EventCard eventCard) {
        viewModel.removeFromFavorites(eventCard);
    }
}