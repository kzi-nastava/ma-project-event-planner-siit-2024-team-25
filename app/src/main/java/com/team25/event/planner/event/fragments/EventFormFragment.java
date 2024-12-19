package com.team25.event.planner.event.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventFormBinding;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.model.PrivacyType;
import com.team25.event.planner.event.viewmodel.EventFormViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class EventFormFragment extends Fragment {
    private FragmentEventFormBinding binding;
    private EventFormViewModel viewModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_form, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(EventFormViewModel.class);
        binding.setViewModel(viewModel);

        setupDateTimePickers();
        setupPrivacyTypeRadioButtons();
        setupObservers();

        viewModel.fetchEventTypes();

        return binding.getRoot();
    }

    private void setupDateTimePickers() {
        binding.startDateInput.setOnClickListener(v -> showDatePicker(true));
        binding.endDateInput.setOnClickListener(v -> showDatePicker(false));
        binding.startTimeInput.setOnClickListener(v -> showTimePicker(true));
        binding.endTimeInput.setOnClickListener(v -> showTimePicker(false));
    }

    private void setupPrivacyTypeRadioButtons() {
        binding.privateRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewModel.privacyType.setValue(PrivacyType.PRIVATE);
            }
        });

        binding.publicRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewModel.privacyType.setValue(PrivacyType.PUBLIC);
            }
        });
    }

    private void setupObservers() {
        viewModel.eventTypes.observe(getViewLifecycleOwner(), this::setupEventTypeSpinner);

        viewModel.serverError.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.event.observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                Toast.makeText(getContext(), R.string.event_created_success, Toast.LENGTH_SHORT).show();
                // Optionally navigate back or to event details
            }
        });
    }

    private void setupEventTypeSpinner(List<EventType> eventTypes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                eventTypes.stream()
                        .map(EventType::getName)
                        .collect(Collectors.toList())
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.eventTypeSpinner.setAdapter(adapter);

        binding.eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.selectedEventTypeId.setValue(eventTypes.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.selectedEventTypeId.setValue(null);
            }
        });
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    if (isStartDate) {
                        viewModel.startDate.setValue(selectedDate);
                        binding.startDateInput.setText(selectedDate.format(dateFormatter));
                    } else {
                        viewModel.endDate.setValue(selectedDate);
                        binding.endDateInput.setText(selectedDate.format(dateFormatter));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                    if (isStartTime) {
                        viewModel.startTime.setValue(selectedTime);
                        binding.startTimeInput.setText(selectedTime.format(timeFormatter));
                    } else {
                        viewModel.endTime.setValue(selectedTime);
                        binding.endTimeInput.setText(selectedTime.format(timeFormatter));
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
}