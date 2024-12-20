package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentAgendaBinding;
import com.team25.event.planner.event.adapters.ActivityAdapter;
import com.team25.event.planner.event.viewmodel.AgendaViewModel;

import java.util.ArrayList;

public class AgendaFragment extends Fragment {
    private FragmentAgendaBinding binding;
    private AgendaViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agenda, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        viewModel = new ViewModelProvider(this).get(AgendaViewModel.class);
        binding.setViewModel(viewModel);

        setupObservers();
        setupListeners();
        setupMenu();
        setupBackPress();

        if (getArguments() != null) {
            long eventId = getArguments().getLong(EventArgumentNames.ID_ARG);
            if (eventId != -1) {
                viewModel.setEventId(eventId);
            }

            String eventName = getArguments().getString(EventArgumentNames.NAME_ARG);
            binding.tvEventName.setText(eventName);
        }

        return binding.getRoot();
    }

    private void setupObservers() {
        ActivityAdapter adapter = new ActivityAdapter(
                new ArrayList<>(),
                activity -> viewModel.removeActivity(activity.getId())
        );
        binding.recyclerViewActivities.setAdapter(adapter);
        viewModel.activities.observe(getViewLifecycleOwner(), adapter::refreshActivities);

        viewModel.removeSuccessSignal.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), R.string.activity_remove_success, Toast.LENGTH_SHORT).show();
                viewModel.addSuccessSignal.setValue(false);
            }
        });
    }

    private void setupListeners() {
        binding.fabAddActivity.setOnClickListener(v -> {
            ActivityAddFragment activityAddBottomSheet = new ActivityAddFragment(viewModel);
            activityAddBottomSheet.show(getParentFragmentManager(), "ActivityAddFragment");
        });
    }

    private void setupMenu() {
        MenuHost menuHost = requireActivity();

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.agenda_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_finish) {
                    Toast.makeText(getContext(), R.string.agenda_finish_message, Toast.LENGTH_SHORT).show();
                    navController.popBackStack(R.id.myEventsFragment, false);
                    // TODO: replace with navController.navigate(R.id.ACTION_AGENDA_FRAGMENT_TO_BUDGET_PLANNING_FRAGMENT)
                    // TODO: if editing navigate to event details instead of budget planning
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void setupBackPress() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController.getCurrentDestination() != null &&
                        navController.getCurrentDestination().getId() == R.id.agendaFragment) {
                    navController.popBackStack(R.id.myEventsFragment, false);
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }
}