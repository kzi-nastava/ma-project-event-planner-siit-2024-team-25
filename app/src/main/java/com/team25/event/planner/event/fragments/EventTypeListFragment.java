package com.team25.event.planner.event.fragments;

import static com.team25.event.planner.event.fragments.EventTypeFragment.ID_ARG_NAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventTypeListBinding;
import com.team25.event.planner.event.adapters.EventTypeAdapter;
import com.team25.event.planner.event.viewmodel.EventTypeListViewModel;

import java.util.ArrayList;

public class EventTypeListFragment extends Fragment {
    private FragmentEventTypeListBinding binding;
    private EventTypeListViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_event_type_list,
                container, false
        );
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(EventTypeListViewModel.class);
        binding.setViewModel(viewModel);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupObservers();
        setupListeners();

        viewModel.fetchEventTypes();

        return binding.getRoot();
    }

    private void setupObservers() {
        EventTypeAdapter adapter = new EventTypeAdapter(new ArrayList<>(), eventType -> {
            Bundle bundle = new Bundle();
            bundle.putLong(ID_ARG_NAME, eventType.getId());
            navController.navigate(R.id.action_eventTypeListFragment_to_eventTypeFragment, bundle);
        });
        binding.recyclerViewEventTypes.setAdapter(adapter);

        viewModel.eventTypes.observe(getViewLifecycleOwner(), adapter::updateEventTypes);
    }

    private void setupListeners() {
        binding.buttonNewEventType.setOnClickListener(v -> {
            navController.navigate(R.id.action_eventTypeListFragment_to_eventTypeFragment);
        });
    }
}