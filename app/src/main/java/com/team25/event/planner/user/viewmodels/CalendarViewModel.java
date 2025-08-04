package com.team25.event.planner.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.service.api.PurchaseApi;
import com.team25.event.planner.user.model.CalendarEvent;
import com.team25.event.planner.user.model.PurchaseServiceCard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class CalendarViewModel extends ViewModel {
    private final EventApi eventApi = ConnectionParams.eventApi;
    private final PurchaseApi purchaseApi = ConnectionParams.purchaseApi;

    @Setter
    private Long userId;

    private final MutableLiveData<List<CalendarEvent>> _calendarEvents = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<CalendarEvent>> calendarEvents = _calendarEvents;

    private final MutableLiveData<List<EventCard>> _attendingEvents = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> attendingEvents = _attendingEvents;

    private final MutableLiveData<List<EventCard>> _myEvents = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> myEvents = _myEvents;

    private final MutableLiveData<List<PurchaseServiceCard>> _serviceReservations = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<PurchaseServiceCard>> serviceReservations = _serviceReservations;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void loadCalendarEvents(LocalDate startDate, LocalDate endDate) {
        if (userId == null) return;
        eventApi.getCalendarEvents(userId, startDate, endDate).enqueue(new ResponseCallback<>(
                _calendarEvents::postValue,
                _serverError, "CalendarViewModel"
        ));
    }

    public void loadAttendingEvents(LocalDate date) {
        if (userId == null) return;
        eventApi.getAttendingEvents(userId, date, date).enqueue(new ResponseCallback<>(
                _attendingEvents::postValue,
                _serverError, "CalendarViewModel"
        ));
    }

    public void loadMyEvents(LocalDate date) {
        if (userId == null) return;
        eventApi.getOrganizerEventsOverlappingDateRange(userId, date, date).enqueue(new ResponseCallback<>(
                _myEvents::postValue,
                _serverError, "CalendarViewModel"
        ));
    }

    public void loadServiceReservations(LocalDate date) {
        if (userId == null) return;
        purchaseApi.getOwnerPurchases(userId, date, date).enqueue(new ResponseCallback<>(
                _serviceReservations::postValue,
                _serverError, "CalendarViewModel"
        ));
    }
}