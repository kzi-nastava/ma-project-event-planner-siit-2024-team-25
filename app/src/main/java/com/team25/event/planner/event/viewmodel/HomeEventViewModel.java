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
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public HomeEventViewModel(){

        List<EventCard> eventCards = new ArrayList<>();
//        eventCards.add(new EventCard(1, "Concert", "Stefan", new Date(), "Uzice"));
//        eventCards.add(new EventCard(2, "Concert", "Petar", new Date(), "Uzice"));
//        eventCards.add(new EventCard(3, "Concert", "Milos", new Date(),"Uzice"));
//        eventCards.add(new EventCard(4, "Concert", "Nikola", new Date(),"Uzice"));
//        eventCards.add(new EventCard(5, "Concert", "Milan", new Date(),"Uzice"));
//        eventCards.add(new EventCard(6, "Concert", "Stefan", new Date(),"Uzice"));
//        eventCards.add(new EventCard(7, "Concert", "Petar", new Date(),"Uzice"));
//        eventCards.add(new EventCard(8, "Concert", "Milos", new Date(),"Uzice"));
//        eventCards.add(new EventCard(9, "Concert", "Nikola", new Date(),"Uzice"));
//        eventCards.add(new EventCard(10, "Concert", "Milan", new Date(),"Uzice"));
        _events.setValue(eventCards);

        getTopEvents();
    }

    public void getTopEvents(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectiongParams.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        String countryValue = country.getValue() != null ? country.getValue() : "";
        String cityValue = ""; // Replace with your logic to get the city

        EventApi eventApi = retrofit.create(EventApi.class);
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
