package com.team25.event.planner.event.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentActivityAddBinding;
import com.team25.event.planner.event.viewmodel.AgendaViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ActivityAddFragment extends BottomSheetDialogFragment {

    FragmentActivityAddBinding binding;
    private final AgendaViewModel viewModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // For partial selections (only date or only time)
    private LocalDate tempStartDate;
    private LocalTime tempStartTime;
    private LocalDate tempEndDate;
    private LocalTime tempEndTime;

    public ActivityAddFragment(AgendaViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_add, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.setViewModel(viewModel);

        setupTimePickers();
        setupListeners();
        setupObservers();

        return binding.getRoot();
    }

    private void setupTimePickers() {
        binding.startDateInput.setOnClickListener(v -> showDatePicker(true));
        binding.endDateInput.setOnClickListener(v -> showDatePicker(false));
        binding.startTimeInput.setOnClickListener(v -> showTimePicker(true));
        binding.endTimeInput.setOnClickListener(v -> showTimePicker(false));
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    if (isStartDate) {
                        tempStartDate = selectedDate;
                        binding.startDateInput.setText(selectedDate.format(dateFormatter));
                        showTimePicker(true); // Show time picker right after date selection
                    } else {
                        tempEndDate = selectedDate;
                        binding.endDateInput.setText(selectedDate.format(dateFormatter));
                        showTimePicker(false); // Show time picker right after date selection
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
                        tempStartTime = selectedTime;
                        binding.startTimeInput.setText(selectedTime.format(timeFormatter));
                        if (tempStartDate != null) {
                            viewModel.startTime.setValue(
                                    LocalDateTime.of(tempStartDate, tempStartTime)
                            );
                        }
                    } else {
                        tempEndTime = selectedTime;
                        binding.endTimeInput.setText(selectedTime.format(timeFormatter));
                        if (tempEndDate != null) {
                            viewModel.endTime.setValue(
                                    LocalDateTime.of(tempEndDate, tempEndTime)
                            );
                        }
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void setupListeners() {
        binding.buttonCancel.setOnClickListener(v -> dismiss());
    }

    private void setupObservers() {
        viewModel.addSuccessSignal.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), R.string.activity_add_success, Toast.LENGTH_SHORT).show();
                dismiss();
                viewModel.addSuccessSignal.setValue(false);
            }
        });

        viewModel.startTime.observe(getViewLifecycleOwner(), dateTime -> {
            if (dateTime != null) {
                tempStartDate = dateTime.toLocalDate();
                tempStartTime = dateTime.toLocalTime();
                binding.startDateInput.setText(tempStartDate.format(dateFormatter));
                binding.startTimeInput.setText(tempStartTime.format(timeFormatter));
            }
        });

        viewModel.endTime.observe(getViewLifecycleOwner(), dateTime -> {
            if (dateTime != null) {
                tempEndDate = dateTime.toLocalDate();
                tempEndTime = dateTime.toLocalTime();
                binding.endDateInput.setText(tempEndDate.format(dateFormatter));
                binding.endTimeInput.setText(tempEndTime.format(timeFormatter));
            }
        });
    }
}