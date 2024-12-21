package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.Activity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

public class AgendaViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;

    private final MutableLiveData<Long> _eventId = new MutableLiveData<>();
    public final LiveData<Long> eventId = _eventId;

    private final MutableLiveData<List<Activity>> _activities = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Activity>> activities = _activities;

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<LocalDateTime> startTime = new MutableLiveData<>();
    public final MutableLiveData<LocalDateTime> endTime = new MutableLiveData<>();
    public final MutableLiveData<String> location = new MutableLiveData<>();

    @Data
    @Builder
    public static class ErrorUiState {
        private final String name;
        private final String description;
        private final String startTime;
        private final String endTime;
        private final String location;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public final MutableLiveData<Boolean> addSuccessSignal = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> removeSuccessSignal = new MutableLiveData<>(false);

    public void setEventId(Long eventId) {
        _eventId.setValue(eventId);
        fetchAgenda();
    }

    public void fetchAgenda() {
        final Long eventId = this.eventId.getValue();
        if (eventId == null) {
            _activities.postValue(new ArrayList<>());
        } else {
            eventApi.getAgenda(eventId).enqueue(new ResponseCallback<>(
                    _activities::postValue,
                    _serverError, "AgendaViewModel"
            ));
        }
    }

    public void onSubmit() {
        if (validateForm()) {
            addActivity();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();

        if (name.getValue() == null || name.getValue().isEmpty()) {
            errorUiStateBuilder.name("Name is required.");
            isValid = false;
        }

        if (startTime.getValue() == null) {
            errorUiStateBuilder.startTime("Start time is required.");
            isValid = false;
        } else if (endTime.getValue() == null) {
            errorUiStateBuilder.endTime("End time is required.");
            isValid = false;
        } else if (startTime.getValue().isAfter(endTime.getValue())) {
            errorUiStateBuilder.startTime("Start time must be before end time.");
            isValid = false;
        }

        _errors.postValue(errorUiStateBuilder.build());
        return isValid;
    }

    private void addActivity() {
        final Activity activity = new Activity(
                null,
                name.getValue(),
                description.getValue(),
                startTime.getValue(),
                endTime.getValue(),
                location.getValue()
        );

        eventApi.addActivity(eventId.getValue(), activity).enqueue(new ResponseCallback<>(
                (_activity) -> {
                    addSuccessSignal.postValue(true);
                    clearForm();
                    fetchAgenda();
                },
                _serverError, "AgendaViewModel"
        ));
    }

    public void removeActivity(Long activityId) {
        this.eventApi.removeActivity(this.eventId.getValue(), activityId).enqueue(new ResponseCallback<>(
                (ignored) -> {
                    removeSuccessSignal.postValue(true);
                    fetchAgenda();
                },
                _serverError, "AgendaViewModel"
        ));
    }

    public void clearForm() {
        name.setValue(null);
        description.setValue(null);
        startTime.setValue(null);
        endTime.setValue(null);
        location.setValue(null);
    }
}
