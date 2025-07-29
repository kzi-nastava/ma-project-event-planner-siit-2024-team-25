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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentMyEventsBinding;
import com.team25.event.planner.event.adapters.MyEventAdapter;
import com.team25.event.planner.event.viewmodel.MyEventsViewModel;

import java.util.ArrayList;

public class MyEventsFragment extends Fragment {
    private FragmentMyEventsBinding binding;
    private MyEventsViewModel viewModel;
    private NavController navController;
    private MyEventAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_events, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(MyEventsViewModel.class);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupEventList();
        setupObservers();
        setupListeners();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.clear();
            viewModel.reload();
        }
    }

    private void setupEventList() {
        adapter = new MyEventAdapter(new ArrayList<>(), (event) -> {
            Bundle bundle = new Bundle();
            bundle.putLong(EventArgumentNames.ID_ARG, event.getId());
            navController.navigate(R.id.action_myEventsFragment_to_eventDetailsFragment, bundle);
        });
        binding.recyclerViewEvents.setAdapter(adapter);

        viewModel.events.observe(getViewLifecycleOwner(), adapter::addEvents);

        binding.recyclerViewEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!viewModel.isLoading() && !viewModel.isEndReached()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        viewModel.loadNextPage();
                    }
                }
            }
        });
    }

    private void setupObservers() {
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

    private void setupListeners() {
        binding.createEventButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_myEventsFragment_to_eventFormFragment);
        });
    }
}