package com.team25.event.planner.event.viewmodel;

import android.util.Log;
import android.widget.DatePicker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> maxParticipantsString = new MutableLiveData<>();
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public final MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> selectedStartDate = new MutableLiveData<>();
    public MutableLiveData<String> selectedEndDate = new MutableLiveData<>();


    public final Map<String, String> sortByMap = new HashMap<>();
    public final Map<String, String> sortCriteriaMap = new HashMap<>();

    public final MutableLiveData<String> selectedSortBy = new MutableLiveData<>();
    public final MutableLiveData<String> selectedSortCriteria = new MutableLiveData<>();


    public HomeEventViewModel() {
        _currentPage.setValue(0);

        sortByMap.put("", "");
        sortByMap.put("Name", "name");
        sortByMap.put("Start date", "startDate");
        sortByMap.put("Organizer", "organizer.firstName");
        sortByMap.put("Country", "location.country");
        sortByMap.put("City", "location.city");

        sortCriteriaMap.put("", "");
        sortCriteriaMap.put("Ascending", "asc");
        sortCriteriaMap.put("Descending", "desc");

    }

    private Map<String, String> buildQuery(){
        Map<String, String> query = new HashMap<>();

        if(this.name.getValue() != null){
            query.put("nameContains", this.name.getValue().trim());
        }
        if(this.maxParticipantsString.getValue()!=null){
            query.put("maxParticipants", this.maxParticipantsString.getValue().trim());
        }
        if(this.country.getValue()!= null){
            query.put("country", this.country.getValue().trim());
        }
        if(this.city.getValue()!= null){
            query.put("city", this.city.getValue().trim());
        }
        if(this.selectedStartDate.getValue()!= null){
            query.put("startDate", this.selectedStartDate.getValue().trim());
        }
        if(this.selectedEndDate.getValue()!= null){
            query.put("endDate", this.selectedEndDate.getValue().trim());
        }

        if(this.selectedSortBy.getValue()!= null){
            query.put("sortBy", this.sortByMap.get(this.selectedSortBy.getValue().trim()));
        }
        if(this.selectedSortCriteria.getValue()!= null){
            query.put("sortDirection", this.sortCriteriaMap.get(this.selectedSortCriteria.getValue().trim()));
        }

        return query;
    }

    public void getAllEvents() {

        Map<String, String> query = this.buildQuery();

        EventApi eventApi = ConnectionParams.eventApi;
        Call<Page<EventCard>> call = eventApi.getAllEvents(currentPage.getValue(), query);

        call.enqueue(new Callback<Page<EventCard>>() {
            @Override
            public void onResponse(Call<Page<EventCard>> call, Response<Page<EventCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _events.setValue(response.body().getContent());
                    _totalPage.setValue(response.body().getTotalPages());
                } else {
                    Log.e("HomeEventViewModel", "Failed to fetch top events");
                }
            }
            @Override
            public void onFailure(Call<Page<EventCard>> call, Throwable t) {
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

        String countryValue = country.getValue() != null ? country.getValue() : "";
        String cityValue = "";

        EventApi eventApi = ConnectionParams.eventApi;
        Call<Page<EventCard>> call = eventApi.getTopEvents(countryValue, cityValue);

        call.enqueue(new Callback<Page<EventCard>>() {
            @Override
            public void onResponse(Call<Page<EventCard>> call, Response<Page<EventCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _topEvents.setValue(response.body().getContent());
                } else {
                    Log.e("HomeEventViewModel", "Failed to fetch top events");
                }
            }

            @Override
            public void onFailure(Call<Page<EventCard>> call, Throwable t) {
                Log.e("HomeEventViewModel", "Error fetching top events: " + t.getMessage());
            }
        });
    }

    public void filter() {

    }

    public void sort() {
    }
}
