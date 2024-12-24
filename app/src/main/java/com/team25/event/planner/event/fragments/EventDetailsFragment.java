package com.team25.event.planner.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventDetailsBinding;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.viewmodel.EventFormViewModel;


public class EventDetailsFragment extends Fragment {

    private final String EVENT_ID = "EVENT_ID";
    private final String INVITATION_CODE = "invitationCode";
    private Long _eventId;
    private String _invitationCode;
    private NavController navController;
    private FragmentEventDetailsBinding _binding;

    public EventDetailsFragment() {
    }

    public static EventDetailsFragment newInstance() {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _eventId = getArguments().getLong(EVENT_ID);
            _invitationCode = getArguments().getString(INVITATION_CODE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        _binding.eventPurchase.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong(EVENT_ID, _eventId);
            navController.navigate(R.id.eventPurchaseFragment, bundle);
        });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false);

        return _binding.getRoot();
    }
}