package com.team25.event.planner.product_service.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.MainActivity;
import com.team25.event.planner.R;
import com.team25.event.planner.product_service.fragments.OwnerHomePage;
import com.team25.event.planner.product_service.fragments.ProfilFragment;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

public class ServiceAddFormViewModel extends ViewModel {
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> specifics = new MutableLiveData<>();
    //public final MutableLiveData<String> serviceCategory = new MutableLiveData<>();
    public final MutableLiveData<Integer> price = new MutableLiveData<>();
    public final MutableLiveData<Integer> discount = new MutableLiveData<>();
    //public final MutableLiveData<String> images = new MutableLiveData<>();
    //public final MutableLiveData<String> eventTypes = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();
    public final MutableLiveData<Integer> duration = new MutableLiveData<>();
    public final MutableLiveData<Integer> minArr = new MutableLiveData<>();
    public final MutableLiveData<Integer> maxArr = new MutableLiveData<>();
    public final MutableLiveData<Date> reservationDate = new MutableLiveData<>();
    public final MutableLiveData<Date> cancelDate = new MutableLiveData<>();
    public final MutableLiveData<Boolean> confirmationType = new MutableLiveData<>();


    @Data
    @Builder()
    public static class ErrorUiState {
        //private final String name;
    }
    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Boolean> _addedService = new MutableLiveData<>(false);
    public final LiveData<Boolean> addedService = _addedService;


    public final MutableLiveData<Boolean> firstToSecond = new MutableLiveData<>();
    public final MutableLiveData<Boolean> cancelClicked = new MutableLiveData<>();


    public void FirstToSecond(){
        firstToSecond.setValue(true);
    }


    public void cancel(){
        cancelClicked.setValue(true);
    }


}