package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.model.EventRequest;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.model.PrivacyType;
import com.team25.event.planner.user.model.Location;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

public class EventFormViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;

    private final MutableLiveData<List<EventType>> _eventTypes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventType>> eventTypes = _eventTypes;

    public final MutableLiveData<Long> selectedEventTypeId = new MutableLiveData<>();
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<PrivacyType> privacyType = new MutableLiveData<>();
    public final MutableLiveData<LocalDate> startDate = new MutableLiveData<>();
    public final MutableLiveData<LocalDate> endDate = new MutableLiveData<>();
    public final MutableLiveData<LocalTime> startTime = new MutableLiveData<>();
    public final MutableLiveData<LocalTime> endTime = new MutableLiveData<>();
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public final MutableLiveData<String> city = new MutableLiveData<>();
    public final MutableLiveData<String> address = new MutableLiveData<>();

    @Data
    @Builder
    public static class ErrorUiState {
        private final String name;
        private final String description;
        private final String privacyType;
        private final String startDate;
        private final String endDate;
        private final String startTime;
        private final String endTime;
        private final String country;
        private final String city;
        private final String address;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Event> _event = new MutableLiveData<>();
    public final LiveData<Event> event = _event;

    public void fetchEventTypes() {
        eventTypeApi.getEventTypes().enqueue(new ResponseCallback<>(
                this._eventTypes::postValue,
                _serverError,
                "EventFormViewModel"
        ));
    }

    public void onSubmit() {
        if (validateForm()) {
            createEvent();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();

        if (name.getValue() == null || name.getValue().trim().isEmpty()) {
            errorUiStateBuilder.name("Name is required.");
            isValid = false;
        }

        if (privacyType.getValue() == null) {
            errorUiStateBuilder.privacyType("Privacy type is required.");
            isValid = false;
        }

        if (startDate.getValue() == null) {
            errorUiStateBuilder.startDate("Start date is required.");
            isValid = false;
        } else if (endDate.getValue() == null) {
            errorUiStateBuilder.endDate("End date is required.");
            isValid = false;
        } else {
            if (endDate.getValue().isBefore(LocalDate.now())) {
                errorUiStateBuilder.endDate("Event must be scheduled in the future.");
                isValid = false;
            }

            if (startDate.getValue().isAfter(endDate.getValue())) {
                errorUiStateBuilder.startDate("Start date must be before end date.");
                isValid = false;
            }
        }

        if (startTime.getValue() == null) {
            errorUiStateBuilder.startTime("Start time is required.");
            isValid = false;
        } else if (endTime.getValue() == null) {
            errorUiStateBuilder.endTime("End time is required.");
            isValid = false;
        } else if (startDate.getValue() != null && endDate.getValue() != null &&
                startDate.getValue().equals(endDate.getValue()) &&
                startTime.getValue().isAfter(endTime.getValue())) {
            errorUiStateBuilder.startTime("Start time must be before end time.");
            isValid = false;
        }

        if (country.getValue() == null || country.getValue().trim().isEmpty()) {
            errorUiStateBuilder.country("Country is required.");
            isValid = false;
        }
        if (city.getValue() == null || city.getValue().trim().isEmpty()) {
            errorUiStateBuilder.city("City is required.");
            isValid = false;
        }
        if (address.getValue() == null || address.getValue().trim().isEmpty()) {
            errorUiStateBuilder.address("Address is required.");
            isValid = false;
        }

        _errors.postValue(errorUiStateBuilder.build());
        return isValid;
    }

    private void createEvent() {
        final EventRequest eventRequest = new EventRequest(
                selectedEventTypeId.getValue(),
                name.getValue(),
                description.getValue(),
                privacyType.getValue(),
                startDate.getValue(),
                endDate.getValue(),
                startTime.getValue(),
                endTime.getValue(),
                new Location(
                        country.getValue(),
                        city.getValue(),
                        address.getValue()
                )
        );

        eventApi.createEvent(eventRequest).enqueue(new ResponseCallback<>(
                event -> {
                    _event.postValue(event);
                    resetForm();
                },
                _serverError, "EventFormViewModel"
        ));
    }

    private void resetForm() {
        selectedEventTypeId.setValue(null);
        name.setValue(null);
        description.setValue(null);
        privacyType.setValue(null);
        startDate.setValue(null);
        endDate.setValue(null);
        startTime.setValue(null);
        endTime.setValue(null);
        country.setValue(null);
        city.setValue(null);
        address.setValue(null);
        _errors.postValue(null);
    }
}
