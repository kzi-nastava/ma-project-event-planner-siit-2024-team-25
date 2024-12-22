package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventInvitationBinding;
import com.team25.event.planner.event.adapters.EventInvitationsListAdapter;
import com.team25.event.planner.event.viewmodel.EventInvitationViewModel;

public class EventInvitation extends Fragment {
    private FragmentEventInvitationBinding binding;
    private EventInvitationsListAdapter adapter;
    private EventInvitationViewModel eventInvitationViewModel;
    private ListView listView;
    private String eventName;
    private Long eventId;
    private NavController navController;

    public EventInvitation() {
    }

    public static EventInvitation newInstance(String param1, Long param2) {
        EventInvitation fragment = new EventInvitation();
        Bundle args = new Bundle();
        args.putString(EventArgumentNames.NAME_ARG, param1);
        args.putLong(EventArgumentNames.ID_ARG, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString(EventArgumentNames.NAME_ARG);
            eventId = getArguments().getLong(EventArgumentNames.ID_ARG);
            eventInvitationViewModel = new EventInvitationViewModel(eventId);

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEventInvitationBinding.inflate(inflater, container, false);
        binding.titleTextView.setText(String.format("Email invitations for %s", eventName));
        binding.setViewModel(eventInvitationViewModel);
        binding.setLifecycleOwner(this);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        listView = binding.emailListView;
        eventInvitationViewModel.emails.observe(getViewLifecycleOwner(), (emails -> {
            adapter = new EventInvitationsListAdapter(requireContext(), emails);
            listView.setAdapter(adapter);

            adapter.deleteEmail.observe(getViewLifecycleOwner(), email -> {
                eventInvitationViewModel.deleteEmail(email);
            });
        }));

        eventInvitationViewModel.toastMessage.observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        });

        eventInvitationViewModel.closeFragmentEvent.observe(getViewLifecycleOwner(), shouldClose -> {
            if (shouldClose != null && shouldClose) {
                Bundle args = new Bundle();
                args.putLong(EventArgumentNames.ID_ARG, eventId);
                args.putString(EventArgumentNames.NAME_ARG, eventName);
                navController.navigate(R.id.action_eventInvitation_to_agendaFragment, args);
            }
        });

        setupBackPress();

        return binding.getRoot();
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