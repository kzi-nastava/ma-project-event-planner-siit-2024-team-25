package com.team25.event.planner.product_service.viewModels;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectiongParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.product_service.api.ServiceApi;
import com.team25.event.planner.product_service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.product_service.enums.ReservationType;
import com.team25.event.planner.product_service.model.ErrorResponse;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.model.ServiceCard;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceAddFormViewModel extends ViewModel {
    public   MutableLiveData<String> errorMessageFromServer = new MutableLiveData<>();

    private ArrayList<Service> services = new ArrayList<Service>();
    private ServiceCreateRequestDTO serviceCreateRequestDTO;

    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public String nameService;
    public final MutableLiveData<String> description = new MutableLiveData<>("");
    public final MutableLiveData<String> specifics = new MutableLiveData<>();
    public final MutableLiveData<String> priceString = new MutableLiveData<>();
    public final MutableLiveData<Integer> price = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> discount = new MutableLiveData<>(0);
    public final MutableLiveData<List<String>> images = new MutableLiveData<>(List.of());
    public final MutableLiveData<String> eventTypes = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>(false);
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
        private final String price;
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

            try {
                int number = Integer.parseInt(text);
                price.setValue(number);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }

    }
    public boolean validateForm(){
        String name = this.name.getValue();
        String description = this.description.getValue();
        String priceStringValue = this.priceString.getValue();

        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if (priceStringValue == null || priceStringValue.isBlank()) {
            errorUiStateBuilder.price("Price is required");
            isValid = false;
        }else{
            if(validateInputNumber(priceStringValue)){
                price.setValue(Integer.valueOf(priceStringValue));
            }else{
                errorUiStateBuilder.price("Price must be a number");
                isValid = false;
            }
        }

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

    private ReservationType getReservationType(){
        if(Boolean.TRUE.equals(confirmationTypeToggle.getValue())){
            return ReservationType.AUTOMATIC;
        }else{
            return ReservationType.MANUAL;
        }
    }
    public void createService(){

        serviceCreateRequestDTO = new ServiceCreateRequestDTO();
        serviceCreateRequestDTO.setName(name.getValue());
        serviceCreateRequestDTO.setDescription(description.getValue());
        serviceCreateRequestDTO.setPrice(price.getValue());
        serviceCreateRequestDTO.setDiscount(discount.getValue());
        serviceCreateRequestDTO.setVisible(isVisible.getValue());
        serviceCreateRequestDTO.setAvailable(isAvailable.getValue());
        serviceCreateRequestDTO.setSpecifics(specifics.getValue());
        serviceCreateRequestDTO.setReservationType(getReservationType());
        serviceCreateRequestDTO.setReservationDeadline(reservationDeadline.getValue());
        serviceCreateRequestDTO.setCancellationDeadline(cancelDeadline.getValue());

        serviceCreateRequestDTO.setDuration(duration.getValue());
        // min and max

        serviceCreateRequestDTO.setEventTypesIDs(List.of(1L, 2L, 3L));
        serviceCreateRequestDTO.setOfferingCategoryID(3L);
        serviceCreateRequestDTO.setOwnerId(11L);

        serviceCreateRequestDTO.setImages(images.getValue());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectiongParams.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceApi serviceApi = retrofit.create(ServiceApi.class);
        Call<ResponseBody> call = serviceApi.createService(serviceCreateRequestDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    errorMessageFromServer.setValue("You successfully created new service");
                } else {
                    errorMessageFromServer.setValue(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessageFromServer.setValue("Networking problem" + t.toString());
            }
        });

    }
}