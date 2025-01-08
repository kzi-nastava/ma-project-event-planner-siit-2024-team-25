package com.team25.event.planner.event.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.event.model.EventFilterDTO;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.model.EventTypePreviewDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeEventViewModel extends ViewModel {


    private final MutableLiveData<List<EventCard>> _events = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> events = _events;
    private final MutableLiveData<List<EventCard>> _topEvents = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> topEvents = _topEvents;
    private final MutableLiveData<Integer> _currentPage = new MutableLiveData<>();
    public final LiveData<Integer> currentPage = _currentPage;
    private final MutableLiveData<Integer> _totalPage = new MutableLiveData<>();
    public final LiveData<Integer> totalPage = _totalPage;
    public EventFilterDTO eventFilterDTO = new EventFilterDTO();
    private final MutableLiveData<List<EventTypePreviewDTO>> _allEventTypes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventTypePreviewDTO>> allEventTypes = _allEventTypes;

    private final MutableLiveData<EventType> _currentEventType = new MutableLiveData<>();
    public final LiveData<EventType> currentEventType = _currentEventType;


    public HomeEventViewModel() {
        _currentPage.setValue(0);
    }

    public void getAllEvents() {
        EventApi eventApi = ConnectionParams.eventApi;
        Call<Page<EventCard>> call = eventApi.getAllEvents(currentPage.getValue(), this.eventFilterDTO.buildQuery());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Page<EventCard>> call, @NonNull Response<Page<EventCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _events.setValue(response.body().getContent());
                    _totalPage.setValue(response.body().getTotalPages());
                } else {
                    Log.e("HomeEventViewModel", "Failed to fetch top events");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Page<EventCard>> call, @NonNull Throwable t) {
                Log.e("HomeEventViewModel", "Error fetching top events: " + t.getMessage());
            }
        });
    }

    public void getNextPage() {
        if (_currentPage.getValue() + 1 < _totalPage.getValue()) {
            this._currentPage.setValue(this._currentPage.getValue() + 1);
            this.getAllEvents();
        }
    }

    public void getPreviousPage() {
        if (_currentPage.getValue() > 0) {
            this._currentPage.setValue(this._currentPage.getValue() - 1);
            this.getAllEvents();
        }
    }

    public void getTopEvents() {

        EventApi eventApi = ConnectionParams.eventApi;
        Call<Page<EventCard>> call = eventApi.getTopEvents();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Page<EventCard>> call, @NonNull Response<Page<EventCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _topEvents.setValue(response.body().getContent());
                } else {
                    Log.e("HomeEventViewModel", "Failed to fetch top events");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Page<EventCard>> call, @NonNull Throwable t) {
                Log.e("HomeEventViewModel", "Error fetching top events: " + t.getMessage());
            }
        });
    }

    public void restartFilter(){
        this.eventFilterDTO = new EventFilterDTO();
        this.getAllEvents();
    }

    public void getEventTypes() {
        EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
        Call<List<EventTypePreviewDTO>> call = eventTypeApi.getAllEventTypes();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<EventTypePreviewDTO>> call, @NonNull Response<List<EventTypePreviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<EventTypePreviewDTO> list = response.body();
                    list.add(0,new EventTypePreviewDTO());
                    _allEventTypes.setValue(list);
                } else {
                    Log.e("HomeEventViewModel", "Failed to fetch top events");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EventTypePreviewDTO>> call, @NonNull Throwable t) {
                Log.e("HomeEventViewModel", "Error fetching top events: " + t.getMessage());
            }
        });
    }

    public void getEventTypeByEvent(Long eventId) {
        EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
        Call<EventType> call = eventTypeApi.getEventTypeByEvent(eventId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventType> call, @NonNull Response<EventType> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _currentEventType.setValue(response.body());
                } else {
                    Log.e("HomeEventViewModel", "Failed to fetch top events");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventType> call, @NonNull Throwable t) {
                Log.e("HomeEventViewModel", "Error fetching top events: " + t.getMessage());
            }
        });
    }
}
