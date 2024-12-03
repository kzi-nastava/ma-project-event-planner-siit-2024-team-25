package com.team25.event.planner.event.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.team25.event.planner.core.ConnectiongParams;
import com.team25.event.planner.core.LocalDateTimeDeserializer;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.EventCard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeEventViewModel extends ViewModel {


    private final MutableLiveData<List<EventCard>> _events = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> events = _events;
    private final MutableLiveData<List<EventCard>> _topEvents = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> topEvents = _topEvents;

    private final MutableLiveData<Integer> _currentPage = new MutableLiveData<>();
    public final LiveData<Integer> currentPage = _currentPage;
    private final MutableLiveData<Integer> _totalPage = new MutableLiveData<>();
    public final LiveData<Integer> totalPage = _totalPage;
    public final MutableLiveData<String> country = new MutableLiveData<>();


    public HomeEventViewModel(){
        _currentPage.setValue(0);
    }



    public void getAllEvents(){
        EventApi eventApi = ConnectiongParams.eventApi;
        Call<Page<EventCard>> call = eventApi.getAllEvents(currentPage.getValue());

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

    public void getNextPage(){
        if(_currentPage.getValue()+1<_totalPage.getValue()){
            this._currentPage.setValue(this._currentPage.getValue()+1);
            this.getAllEvents();
        }
    }

    public void getPreviousPage(){
        if(_currentPage.getValue() > 0){
            this._currentPage.setValue(this._currentPage.getValue()-1);
            this.getAllEvents();
        }
    }


    public void getTopEvents(){

        String countryValue = country.getValue() != null ? country.getValue() : "";
        String cityValue = "";

        EventApi eventApi = ConnectiongParams.eventApi;
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
