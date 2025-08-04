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

    private long eventId = -1;
    private String eventName;
    private boolean isEditable = false;
    private boolean cameFromDetails = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agenda, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        viewModel = new ViewModelProvider(this).get(AgendaViewModel.class);
        binding.setViewModel(viewModel);

        parseArguments();
        setupObservers();
        setupListeners();
        setupMenu();
        setupBackPress();

        return binding.getRoot();
    }

    private void parseArguments() {
        Bundle args = getArguments();
        if (args == null) return;

        eventId = args.getLong(EventArgumentNames.ID_ARG);
        if (eventId != -1) {
            viewModel.setEventId(eventId);
        }

        eventName = args.getString(EventArgumentNames.NAME_ARG);
        binding.tvEventName.setText(eventName);

        isEditable = args.getBoolean(EventArgumentNames.IS_ORGANIZER_ARG);
        cameFromDetails = args.getBoolean(EventArgumentNames.CAME_FROM_DETAILS_ARG);
    }

    private void setupObservers() {
        ActivityAdapter adapter = new ActivityAdapter(
                new ArrayList<>(),
                activity -> viewModel.removeActivity(activity.getId()),
                isEditable
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
        if (isEditable) {
            binding.fabAddActivity.setOnClickListener(v -> {
                ActivityAddFragment activityAddBottomSheet = new ActivityAddFragment(viewModel);
                activityAddBottomSheet.show(getParentFragmentManager(), "ActivityAddFragment");
            });
        } else {
            binding.fabAddActivity.setVisibility(View.GONE);
        }
    }

    private void setupMenu() {
        if (!isEditable) return;

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
                    if (cameFromDetails) {
                        navController.popBackStack(R.id.eventDetailsFragment, false);
                    } else {
                        Bundle args = new Bundle();
                        args.putLong(EventArgumentNames.ID_ARG, eventId);
                        args.putString(EventArgumentNames.NAME_ARG, eventName);
                        args.putBoolean(EventArgumentNames.CAME_FROM_DETAILS_ARG, false);
                        navController.navigate(R.id.action_agendaFragment_to_budgetItemFragment, args);
                    }
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
                    final int destinationFragment = cameFromDetails
                            ? R.id.eventDetailsFragment
                            : R.id.myEventsFragment;
                    navController.popBackStack(destinationFragment, false);
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }
}