package com.team25.event.planner.event.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team25.event.planner.core.ConnectiongParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.core.Validation;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.event.model.Invitation;
import com.team25.event.planner.user.viewmodels.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInvitationViewModel {
    private Long eventId;
    private final MutableLiveData<Boolean> _closeFragmentEvent = new MutableLiveData<>();
    public final LiveData<Boolean> closeFragmentEvent = _closeFragmentEvent;
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    public final LiveData<String> toastMessage = _toastMessage;
    private final MutableLiveData<List<Invitation>> _emails = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Invitation>> emails = _emails;

    private final MutableLiveData<LoginViewModel.ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<LoginViewModel.ErrorUiState> errors = _errors;

    public final MutableLiveData<String> email = new MutableLiveData<>();

    public EventInvitationViewModel(Long eventId){
        this.eventId = eventId;
        List<Invitation> newEmails = new ArrayList<>();
        _emails.setValue(newEmails);
    }

    public void addEmail(){
        if(validateEmail()){
            List<Invitation> currentEmails = new ArrayList<>(_emails.getValue());
            currentEmails.add(new Invitation(email.getValue()));
            _emails.setValue(currentEmails);

            _toastMessage.setValue("You added: " + email.getValue());

            email.setValue(null);
        }
    }

    public void deleteEmail(Invitation email){
        List<Invitation> currentEmails = new ArrayList<>(_emails.getValue());
        currentEmails.remove(email);
        _emails.setValue(currentEmails);

        _toastMessage.setValue("You remove: " + email.getGuestEmail());

        this.email.setValue(null);
    }

    public void sendEmails(){

        EventApi eventApi = ConnectiongParams.eventApi;
        Call<Void> call = eventApi.sendInvitations(eventId, emails.getValue());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    List<Invitation> currentEmails = new ArrayList<>();
                    _emails.setValue(currentEmails);
                    _toastMessage.setValue("Invitations for event was sent");
                    _closeFragmentEvent.setValue(true);
                } else {
                    Log.e("API", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        });


    }

    private boolean validateEmail() {
        boolean isValid = true;
        LoginViewModel.ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = LoginViewModel.ErrorUiState.builder();

        if (!Validation.isValidEmail(email.getValue())) {
            errorUiStateBuilder.email("Please enter a valid email.");
            isValid = false;
        }

        _errors.setValue(errorUiStateBuilder.build());
        return isValid;
    }
}
