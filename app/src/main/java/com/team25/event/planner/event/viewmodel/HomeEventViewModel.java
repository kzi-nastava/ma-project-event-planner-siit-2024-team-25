package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeEventViewModel extends ViewModel {


    private final MutableLiveData<List<EventCard>> _events = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventCard>> events = _events;
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public HomeEventViewModel(){

        List<EventCard> eventCards = new ArrayList<>();
        eventCards.add(new EventCard(1, "Concert", "Stefan", new Date(), "Uzice"));
        eventCards.add(new EventCard(2, "Concert", "Petar", new Date(), "Uzice"));
        eventCards.add(new EventCard(3, "Concert", "Milos", new Date(),"Uzice"));
        eventCards.add(new EventCard(4, "Concert", "Nikola", new Date(),"Uzice"));
        eventCards.add(new EventCard(5, "Concert", "Milan", new Date(),"Uzice"));
        eventCards.add(new EventCard(6, "Concert", "Stefan", new Date(),"Uzice"));
        eventCards.add(new EventCard(7, "Concert", "Petar", new Date(),"Uzice"));
        eventCards.add(new EventCard(8, "Concert", "Milos", new Date(),"Uzice"));
        eventCards.add(new EventCard(9, "Concert", "Nikola", new Date(),"Uzice"));
        eventCards.add(new EventCard(10, "Concert", "Milan", new Date(),"Uzice"));
        _events.setValue(eventCards);
    }

    public void filter() {

    }

    public void sort() {
    }
}
