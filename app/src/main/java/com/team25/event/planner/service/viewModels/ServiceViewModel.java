package com.team25.event.planner.service.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.product.model.EventType;
import com.team25.event.planner.product.model.Product;
import com.team25.event.planner.service.api.ServiceApi;
import com.team25.event.planner.service.dto.ServiceCreateResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceViewModel extends ViewModel {

    private ServiceApi _serviceApi = ConnectionParams.serviceApi;
    private final MutableLiveData<ServiceCreateResponseDTO> _currentService = new MutableLiveData<>();
    public LiveData<ServiceCreateResponseDTO> currentService = _currentService;



    public void getService(Long serviceId) {
        _serviceApi.getService(serviceId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ServiceCreateResponseDTO> call, Response<ServiceCreateResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _currentService.setValue(response.body());
                } else {
                    Log.e("ServiceViewModel", "Failed to fetch event");
                }
            }
            @Override
            public void onFailure(Call<ServiceCreateResponseDTO> call, Throwable t) {
                Log.e("ServiceViewModel", "Error " + t.getMessage());
            }
        });
    }

 }
