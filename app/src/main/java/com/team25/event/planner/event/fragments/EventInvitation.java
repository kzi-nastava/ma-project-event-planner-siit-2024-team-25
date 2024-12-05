package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventInvitationBinding;
import com.team25.event.planner.event.adapters.EventInvitationsListAdapter;
import com.team25.event.planner.event.viewmodel.EventInvitationViewModel;

public class
EventInvitation extends Fragment {

    private FragmentEventInvitationBinding binding;
    private EventInvitationsListAdapter adapter;
    private EventInvitationViewModel eventInvitationViewModel;
    private ListView listView;
    private String eventName;
    private Long eventId;

    public EventInvitation() {
    }

    public static EventInvitation newInstance(String param1, Long param2) {
        EventInvitation fragment = new EventInvitation();
        Bundle args = new Bundle();
        args.putString("name", param1);
        args.putLong("id", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString("name");
            eventId = getArguments().getLong("id");
            eventInvitationViewModel = new EventInvitationViewModel(eventId);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEventInvitationBinding.inflate(inflater, container, false);
        binding.titleTextView.setText(String.format("Email invitations for %s", eventName));
        binding.setViewModel(eventInvitationViewModel);
        binding.setLifecycleOwner(this);
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
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_eventInvitation_to_homeFragment);
            }
        });
        return binding.getRoot();
    }
}