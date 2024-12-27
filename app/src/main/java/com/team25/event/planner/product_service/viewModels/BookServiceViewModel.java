package com.team25.event.planner.product_service.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.product_service.api.PurchaseApi;
import com.team25.event.planner.product_service.dto.ServicePurchaseRequestDTO;
import com.team25.event.planner.product_service.dto.ServicePurchaseResponseDTO;
import com.team25.event.planner.product_service.model.Purchase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookServiceViewModel {

    public MutableLiveData<String> errorMessageFromServer = new MutableLiveData<>();
    private PurchaseApi purchaseApi = ConnectionParams.purchaseApi;
    public Purchase purchase = new Purchase();
    public MutableLiveData<Double> leftMoney = new MutableLiveData<>();
    public MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();

    public MutableLiveData<ServicePurchaseResponseDTO> responseDTO = new MutableLiveData<>();

    public BookServiceViewModel(){}

    public void bookService(Long eventId, Long serviceId){
        ServicePurchaseRequestDTO servicePurchaseDTO = new ServicePurchaseRequestDTO(
                purchase.getSelectedStartDate().getValue(),
                purchase.getSelectedStartTime().getValue(),
                purchase.getSelectedEndDate().getValue(),
                purchase.getSelectedEndTime().getValue(),
                Double.parseDouble(purchase.getPrice().getValue())
        );
        Call<ServicePurchaseResponseDTO> call = purchaseApi.bookService(eventId,serviceId,servicePurchaseDTO);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ServicePurchaseResponseDTO> call, @NonNull Response<ServicePurchaseResponseDTO> response) {
                if (response.isSuccessful()) {
                    responseDTO.setValue(response.body());
                    errorMessageFromServer.setValue("You've successfully booked this service for your event!");
                } else {
                    if (Boolean.TRUE.equals(isAvailable.getValue())) {
                        errorMessageFromServer.setValue("Sorry, you haven't enough money to book this service");
                    } else {
                        errorMessageFromServer.setValue("Service isn't available for booking in this period");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServicePurchaseResponseDTO> call, @NonNull Throwable t) {
                Log.e("Retrofit", "Network error: " + t.getMessage());
            }

        });
    }

    public void isServiceAvailable(Long serviceId){
        purchaseApi.isServiceAvailable(serviceId, this.purchase.buildQuery()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body().booleanValue()){
                    isAvailable.setValue(true);
                }else{
                    isAvailable.setValue(false);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                isAvailable.setValue(false);
            }
        });
    }
}
