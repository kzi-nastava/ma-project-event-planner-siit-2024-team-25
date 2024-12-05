package com.team25.event.planner.event.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.model.ApiError;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventType;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeListViewModel extends ViewModel {
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;

    private final MutableLiveData<List<EventType>> _eventTypes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventType>> eventTypes = _eventTypes;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void fetchEventTypes() {
        eventTypeApi.getEventTypes().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<EventType>> call, @NonNull Response<List<EventType>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _eventTypes.postValue(response.body());
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                            _serverError.postValue(apiError.getMessage());
                            Log.e("EventTypeListViewModel", "Error: " + apiError.getMessage());
                        } else {
                            _serverError.postValue("Unknown error occurred");
                            Log.e("EventTypeListViewModel", "Error response with empty body");
                        }
                    } catch (Exception e) {
                        _serverError.postValue("Error parsing server response");
                        Log.e("EventTypeListViewModel", "Error parsing response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EventType>> call, @NonNull Throwable t) {
                Log.e("EventTypeListViewModel", "Network error on event types fetch: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
    }
}
