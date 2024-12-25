package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.Event;

public class EventDetailsViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;

    private final MutableLiveData<Event> _event = new MutableLiveData<>();
    public final LiveData<Event> event = _event;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void loadEvent(Long eventId, String invitationCode) {
        if (eventId == null) {
            _event.setValue(null);
        } else {
            eventApi.getEvent(eventId, invitationCode).enqueue(new ResponseCallback<>(
                    _event::postValue,
                    _serverError,
                    "EventDetailsViewModel"
            ));
        }
    }

    public void addToFavorites() {
        // TODO: Implement
    }
}
