package com.team25.event.planner.event.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.Event;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
    private final MutableLiveData<Event> _currentEvent = new MutableLiveData<>();
    public final LiveData<Event> currentEvent = _currentEvent;

    public void getEvent(Long eventId) {
        eventApi.getEvent(eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _currentEvent.setValue(response.body());
                } else {
                    Log.e("EventFormViewModel", "Failed to fetch event");
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e("EventFormViewModel", "Error " + t.getMessage());
            }
        });
    }
}
