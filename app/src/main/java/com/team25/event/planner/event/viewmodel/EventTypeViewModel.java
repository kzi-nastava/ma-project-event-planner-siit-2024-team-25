package com.team25.event.planner.event.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.model.ApiError;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.model.EventTypeRequest;
import com.team25.event.planner.event.model.OfferingCategoryPreviewDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        eventTypeApi.getEventType(eventTypeId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventType> call, @NonNull Response<EventType> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _eventTypeId.postValue(response.body().getId());
                    _serverError.postValue(null);
                    populateForm(response.body());
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                            _serverError.postValue(apiError.getMessage());
                            Log.e("EventTypeViewModel", "Error: " + apiError.getMessage());
                        } else {
                            _serverError.postValue("Unknown error occurred");
                            Log.e("EventTypeViewModel", "Error response with empty body");
                        }
                    } catch (Exception e) {
                        _serverError.postValue("Error parsing server response");
                        Log.e("EventTypeViewModel", "Error parsing response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventType> call, @NonNull Throwable t) {
                Log.e("EventTypeViewModel", "Network error on event type fetch: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
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

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventType> call, @NonNull Response<EventType> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _eventTypeId.postValue(response.body().getId());
                    _serverError.postValue(null);
                    populateForm(response.body());
                    successSignal.postValue(true);
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                            _serverError.postValue(apiError.getMessage());
                            Log.e("EventTypeViewModel", "Error: " + apiError.getMessage());
                        } else {
                            _serverError.postValue("Unknown error occurred");
                            Log.e("EventTypeViewModel", "Error response with empty body");
                        }
                    } catch (Exception e) {
                        _serverError.postValue("Error parsing server response");
                        Log.e("EventTypeViewModel", "Error parsing response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventType> call, @NonNull Throwable t) {
                Log.e("EventTypeViewModel", "Network error on event types fetch: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
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
