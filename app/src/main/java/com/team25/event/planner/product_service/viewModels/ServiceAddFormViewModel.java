package com.team25.event.planner.product_service.viewModels;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.R;
import com.team25.event.planner.product_service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.product_service.enums.ReservationType;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.model.ServiceCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;

public class ServiceAddFormViewModel extends ViewModel {

    private ArrayList<Service> services = new ArrayList<Service>();
    private ServiceCreateRequestDTO serviceCreateRequestDTO;

    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public String nameService;
    public final MutableLiveData<String> description = new MutableLiveData<>("");
    public final MutableLiveData<String> specifics = new MutableLiveData<>();
    private final MutableLiveData<String> priceString = new MutableLiveData<>();
    public final MutableLiveData<Integer> price = new MutableLiveData<>();
    public final MutableLiveData<Integer> discount = new MutableLiveData<>(0);
    //public final MutableLiveData<String> images = new MutableLiveData<>();
    public final MutableLiveData<String> eventTypes = new MutableLiveData<>();
    public final MutableLiveData<Boolean> option1 = new MutableLiveData<>();
    public final MutableLiveData<Boolean> option2 = new MutableLiveData<>();
    public final MutableLiveData<Boolean> option3 = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();
    public final MutableLiveData<Integer> reservationDeadline = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> cancelDeadline = new MutableLiveData<>(0);
    public final MutableLiveData<ReservationType> confirmationType = new MutableLiveData<>();
    public final MutableLiveData<Boolean> confirmationTypeToggle = new MutableLiveData<>();
    public final MutableLiveData<String> categoryInput = new MutableLiveData<>();
    public final MutableLiveData<Boolean> categoryInputEnabled = new MutableLiveData<>();
    public final MutableLiveData<Integer> duration = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> minArrangement = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> maxArrangement = new MutableLiveData<>(0);

    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState {
        private final String name;
        private final String description;
        private final Double price;
        private final Integer discount;
        private final String specifics;
        /* visible, available, confirmation type
        private final Integer duration;
        private final Integer minArr;
        private final Integer maxArr;
        private final Integer reservationDeadline;
        private final Integer cancellationDeadline;*/
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;
    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;
    private final MutableLiveData<Boolean> _addedService = new MutableLiveData<>(false);
    public final LiveData<Boolean> addedService = _addedService;

    public final MutableLiveData<Boolean> secondToThird = new MutableLiveData<>();
    public final MutableLiveData<Boolean> secondToFirst = new MutableLiveData<>();
    public final MutableLiveData<Boolean> firstToSecond = new MutableLiveData<>();
    public final MutableLiveData<Boolean> cancelClicked = new MutableLiveData<>();
    public final MutableLiveData<Boolean> toFinish = new MutableLiveData<>();
    public final MutableLiveData<Boolean> toSecond = new MutableLiveData<>();

    public void FirstToSecond() {
        firstToSecond.setValue(true);
    }
    public void cancel() {
        cancelClicked.setValue(true);
    }
    public void SecondToThird() {
        secondToThird.setValue(true);
    }
    public void SecondToFirst() {
        secondToFirst.setValue(true);
    }
    public void Finish() {
        toFinish.setValue(true);
    }
    public void ToSecond() {
        toSecond.setValue(true);
    }

    public void onSeeking(SeekBar seekBar, Integer seekParams) {
        if (seekBar.getId() == R.id.durationValue) {
            duration.setValue(seekParams);
        } else if (seekBar.getId() == R.id.minArrValue) {
            minArrangement.setValue(seekParams);
        } else if(seekBar.getId() == R.id.maxArrValue) {
            maxArrangement.setValue(seekParams);
        }else if(seekBar.getId() == R.id.priceSeekBar2){
            discount.setValue(seekParams);
        }else if(seekBar.getId() == R.id.cancellationSeekbar){
            cancelDeadline.setValue(seekParams);
        }else if(seekBar.getId() == R.id.reservationSeekbar){
            reservationDeadline.setValue(seekParams);
        }
    }

    public boolean validateInputNumber(String text) {
        if (text != null && !text.isEmpty()) {
            try {
                int number = Integer.parseInt(text);
                price.setValue(number);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean validateForm(){
        String name = this.name.getValue();
        String description = this.description.getValue();
        //Double price = Double.valueOf(this.price.getValue());

        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if (name == null || name.isBlank()) {
            errorUiStateBuilder.name("Name is required.");
            isValid = false;
        }
        if (description == null || description.isBlank()) {
            errorUiStateBuilder.description("Description is required.");
            isValid = false;
        }

        _errors.setValue(errorUiStateBuilder.build());
        return isValid;

    }

    public void findService(Integer idService){
        name.postValue("sss");
        description.setValue("Description 1");
        specifics.setValue("Specifics 1");
        price.setValue(100);
        discount.setValue(15);
        confirmationType.setValue(ReservationType.AUTOMATIC);
        isVisible.setValue(true);
        isAvailable.setValue(true);
        syncFront();
    }

    public void fillTheForm(){
        duration.setValue(1);
        minArrangement.setValue(1);
        maxArrangement.setValue(5);
        categoryInput.setValue("Category 10");
        categoryInputEnabled.setValue(false);
        option1.setValue(true);
        option3.setValue(true);
    }

    private void syncFront(){
        if(confirmationType.getValue() == ReservationType.AUTOMATIC){
            confirmationTypeToggle.setValue(true);
        }else{
            confirmationTypeToggle.setValue(false);
        }
    }

    public void restart(){
        categoryInputEnabled.setValue(true);
    }
    public void createService(){

        serviceCreateRequestDTO = new ServiceCreateRequestDTO();
        serviceCreateRequestDTO.setName(name.getValue());
        serviceCreateRequestDTO.setDescription(description.getValue());
        serviceCreateRequestDTO.setPrice(12);
        serviceCreateRequestDTO.setDiscount(5.0);
        serviceCreateRequestDTO.setImages(List.of("https://example.com/images/party1.jpg", "https://example.com/images/party2.jpg"));
        serviceCreateRequestDTO.setVisible(true);
        serviceCreateRequestDTO.setAvailable(true);
        serviceCreateRequestDTO.setSpecifics("Includes DJ services, lighting setup, and custom decorations.");
        serviceCreateRequestDTO.setReservationType(ReservationType.MANUAL); // Pretpostavka da je `ReservationType` enum
        serviceCreateRequestDTO.setDuration(180);
        serviceCreateRequestDTO.setReservationDeadline(48);
        serviceCreateRequestDTO.setCancellationDeadline(24);
        serviceCreateRequestDTO.setEventTypesIDs(List.of(1L, 2L, 3L)); // Pretpostavka da su ovo ID-jevi event tipova
        serviceCreateRequestDTO.setOfferingCategoryID(1L); // Pretpostavka da je ovo ID kategorije
        serviceCreateRequestDTO.setOwnerId(11L);
    }
}