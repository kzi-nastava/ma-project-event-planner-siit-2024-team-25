package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;
import java.util.List;

public class MyEventsViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;

    // Holds the current page events
    private final MutableLiveData<List<EventCard>> _events = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> events = _events;

    private int currentPage = 0;
    private boolean isEndReached = false;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void loadNextPage() {
        if (isLoading()) return;
        _isLoading.setValue(true);

        if (isEndReached) return;

        eventApi.getOrganizerEvents(currentPage).enqueue(new SideEffectResponseCallback<>(
                page -> {
                    currentPage++;
                    if (page.isLast()) {
                        isEndReached = true;
                    }
                    _events.postValue(page.getContent());
                },
                () -> _isLoading.postValue(false),
                _serverError, "MyEventsViewModel")
        );
    }

    public boolean isEndReached() {
        return isEndReached;
    }

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }
}
