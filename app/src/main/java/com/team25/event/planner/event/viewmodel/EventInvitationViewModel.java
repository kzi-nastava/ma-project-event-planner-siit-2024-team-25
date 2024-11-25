package com.team25.event.planner.event.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team25.event.planner.event.model.EventCard;

import java.util.ArrayList;
import java.util.List;

public class EventInvitationViewModel {
    private Long eventId;
    private final MutableLiveData<List<String>> _emails = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<String>> emails = _emails;

    public EventInvitationViewModel(Long eventId){
        this.eventId = eventId;
        List<String> newEmails = new ArrayList<>();
        _emails.setValue(newEmails);
    }

    public void addEmail(){

    }

    public void deleteEmail(){

    }

    public void sendEmails(){

    }
}
