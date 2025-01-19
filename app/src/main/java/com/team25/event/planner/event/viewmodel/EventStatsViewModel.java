package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.Attendee;
import com.team25.event.planner.event.model.ReviewStats;

import java.util.ArrayList;
import java.util.List;

public class EventStatsViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;

    private Long eventId;

    private final MutableLiveData<String> _eventName = new MutableLiveData<>();
    public final LiveData<String> eventName = _eventName;

    private final MutableLiveData<ReviewStats> _reviewStats = new MutableLiveData<>();
    public final LiveData<ReviewStats> reviewStats = _reviewStats;

    private final MutableLiveData<Long> _numAttendees = new MutableLiveData<>();
    public final LiveData<Long> numAttendees = _numAttendees;
    private final MutableLiveData<List<Attendee>> _attendees = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Attendee>> attendees = _attendees;
    private int currentPage = 0;
    private boolean isEndReached = false;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void setEventId(Long eventId) {
        this.eventId = eventId;
        if (eventId != null) {
            loadReviewStats();
            loadAttendeesNextPage();
        }
    }

    public void setEventName(String eventName) {
        _eventName.postValue(eventName);
    }

    private void loadReviewStats() {
        eventApi.getEventReviewStats(eventId).enqueue(new ResponseCallback<>(
                _reviewStats::postValue,
                _serverError, "EventStatsViewModel"
        ));
    }

    public void loadAttendeesNextPage() {
        if (isLoading() || isEndReached) return;
        _isLoading.setValue(true);

        eventApi.getEventAttendees(eventId, currentPage).enqueue(new SideEffectResponseCallback<>(
                page -> {
                    currentPage++;
                    if (page.isLast()) {
                        isEndReached = true;
                    }
                    _attendees.postValue(page.getContent());
                    if (numAttendees.getValue() == null || numAttendees.getValue() != page.getTotalElements()) {
                        _numAttendees.postValue(page.getTotalElements());
                    }
                },
                () -> _isLoading.postValue(false),
                _serverError, "MyEventsViewModel"
        ));
    }

    public boolean isEndReached() {
        return isEndReached;
    }

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }
}