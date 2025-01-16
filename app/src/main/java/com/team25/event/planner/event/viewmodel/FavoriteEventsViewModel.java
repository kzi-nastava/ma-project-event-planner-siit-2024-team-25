package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;
import java.util.List;

public class FavoriteEventsViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;

    private Long userId;

    private final MutableLiveData<List<EventCard>> _events = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> events = _events;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void setUserId(Long userId) {
        this.userId = userId;
        if (userId == null) {
            _events.postValue(new ArrayList<>());
        } else {
            if (isLoading()) return;
            _isLoading.setValue(true);

            eventApi.getFavoriteEvents(userId).enqueue(new SideEffectResponseCallback<>(
                    _events::postValue,
                    () -> _isLoading.postValue(false),
                    _serverError, "MyEventsViewModel")
            );
        }
    }

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }

    public void removeFromFavorites(EventCard event) {
        if (userId == null || event == null) return;
        eventApi.removeFromFavorites(userId, event.getId()).enqueue(new ResponseCallback<>(
                ignored -> {
                    List<EventCard> currEvents = events.getValue();
                    if (currEvents == null) currEvents = new ArrayList<>();
                    currEvents.remove(event);
                    _events.postValue(currEvents);
                },
                _serverError, "FavoriteEventsViewModel"
        ));
    }
}
