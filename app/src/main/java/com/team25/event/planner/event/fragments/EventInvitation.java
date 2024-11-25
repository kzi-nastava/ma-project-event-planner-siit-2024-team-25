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
        listView = binding.emailListView;

        eventInvitationViewModel.emails.observe(getViewLifecycleOwner(), (emails -> {
            NavController navController = Navigation.findNavController(requireView());
            adapter = new EventInvitationsListAdapter(requireContext(), emails, navController);
            listView.setAdapter(adapter);
        }));
        return binding.getRoot();
    }
}