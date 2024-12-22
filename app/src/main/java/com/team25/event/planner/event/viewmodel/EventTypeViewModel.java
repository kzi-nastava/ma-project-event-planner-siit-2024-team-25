package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.model.EventTypeRequest;
import com.team25.event.planner.event.model.OfferingCategoryPreviewDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;
import retrofit2.Call;

public class EventTypeViewModel extends ViewModel {
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;

    private final MutableLiveData<List<OfferingCategoryPreviewDTO>> _offeringCategories = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCategoryPreviewDTO>> offeringCategories = _offeringCategories;

    private final MutableLiveData<Long> _eventTypeId = new MutableLiveData<>();
    public final LiveData<Long> eventTypeId = _eventTypeId;
    public final LiveData<Boolean> isEditing = Transformations.map(_eventTypeId, Objects::nonNull);

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isActive = new MutableLiveData<>(true);
    public final MutableLiveData<List<Long>> selectedCategoryIds = new MutableLiveData<>(new ArrayList<>());

    @Data
    @Builder
    public static class ErrorUiState {
        private final String name;
        private final String description;
        private final String categories;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public final MutableLiveData<Boolean> successSignal = new MutableLiveData<>(false);

    public void fetchOfferings() {
        List<OfferingCategoryPreviewDTO> mockOfferings = new ArrayList<>();
        mockOfferings.add(new OfferingCategoryPreviewDTO(1L, "Training"));
        mockOfferings.add(new OfferingCategoryPreviewDTO(2L, "Consultation"));
        mockOfferings.add(new OfferingCategoryPreviewDTO(3L, "Product Demo"));
        mockOfferings.add(new OfferingCategoryPreviewDTO(4L, "Mentorship"));
        mockOfferings.add(new OfferingCategoryPreviewDTO(5L, "Certification"));
        _offeringCategories.setValue(mockOfferings);
    }

    public void setEventTypeId(Long eventTypeId) {
        _eventTypeId.setValue(eventTypeId);
        if (eventTypeId != null) {
            fetchEventType(eventTypeId);
        } else {
            resetForm();
        }
    }

    private void fetchEventType(Long eventTypeId) {
        eventTypeApi.getEventType(eventTypeId).enqueue(new ResponseCallback<>(
                (eventType) -> {
                    _eventTypeId.postValue(eventType.getId());
                    _serverError.postValue(null);
                    populateForm(eventType);
                },
                _serverError, "EventTypeViewModel"
        ));
    }

    private void populateForm(EventType eventType) {
        name.setValue(eventType.getName());
        description.setValue(eventType.getDescription());
        isActive.setValue(eventType.getIsActive());
        List<Long> categoryIds = new ArrayList<>();
        if (eventType.getCategories() != null) {
            for (OfferingCategoryPreviewDTO category : eventType.getCategories()) {
                categoryIds.add(category.getId());
            }
        }
        selectedCategoryIds.setValue(categoryIds);
    }

    public void onSubmit() {
        if (validateForm()) {
            saveEventType();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();

        if (name.getValue() == null || name.getValue().trim().isEmpty()) {
            errorUiStateBuilder.name("Name is required.");
            isValid = false;
        }

        if (description.getValue() == null || description.getValue().trim().isEmpty()) {
            errorUiStateBuilder.description("Description is required.");
            isValid = false;
        }

        if (selectedCategoryIds.getValue() == null || selectedCategoryIds.getValue().isEmpty()) {
            errorUiStateBuilder.categories("At least one category must be selected.");
            isValid = false;
        }

        _errors.setValue(errorUiStateBuilder.build());
        _serverError.setValue(null);

        return isValid;
    }

    private void saveEventType() {
        EventTypeRequest requestBody = new EventTypeRequest(
                name.getValue(),
                description.getValue(),
                isActive.getValue(),
                selectedCategoryIds.getValue()
        );

        Call<EventType> call = isEditing.getValue() != null && isEditing.getValue()
                ? eventTypeApi.updateEventType(eventTypeId.getValue(), requestBody)
                : eventTypeApi.createEventType(requestBody);

        call.enqueue(new ResponseCallback<>(
                (eventType) -> {
                    _eventTypeId.postValue(eventType.getId());
                    _serverError.postValue(null);
                    populateForm(eventType);
                    successSignal.postValue(true);
                },
                _serverError, "EventTypeViewModel"
        ));
    }

    private void resetForm() {
        name.setValue("");
        description.setValue("");
        isActive.setValue(true);
        selectedCategoryIds.setValue(new ArrayList<>());
        _errors.setValue(null);
        _serverError.setValue(null);
    }
}
