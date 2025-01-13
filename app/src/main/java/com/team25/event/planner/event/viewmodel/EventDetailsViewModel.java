package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.model.FavoriteEventRequest;

import lombok.Setter;

public class EventDetailsViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;

    private final MutableLiveData<Event> _event = new MutableLiveData<>();
    public final LiveData<Event> event = _event;

    @Setter
    private Long userId;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public final MutableLiveData<Boolean> addToFavoritesSuccessSignal = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> removeFromFavoritesSuccessSignal = new MutableLiveData<>(false);

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

    public void toggleFavorite() {
        Event event = this.event.getValue();
        if (event == null || userId == null) return;

        if (event.getIsFavorite()) {
            eventApi.removeFromFavorites(userId, event.getId()).enqueue(new ResponseCallback<>(
                    ignored -> {
                        event.setIsFavorite(false);
                        _event.postValue(event);
                        removeFromFavoritesSuccessSignal.postValue(true);
                    },
                    _serverError, "EventDetailsViewModel"
            ));
        } else {
            eventApi.addToFavorites(userId, new FavoriteEventRequest(userId, event.getId()))
                    .enqueue(new ResponseCallback<>(
                            ignored -> {
                                event.setIsFavorite(true);
                                _event.postValue(event);
                                addToFavoritesSuccessSignal.postValue(true);
                            },
                            _serverError, "EventDetailsViewModel"
                    ));
        }
    }
}
