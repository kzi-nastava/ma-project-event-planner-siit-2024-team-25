package com.team25.event.planner.product_service.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.product_service.api.ServiceApi;
import com.team25.event.planner.product_service.model.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceViewModel extends ViewModel {

    private ServiceApi _serviceApi = ConnectionParams.serviceApi;
    private final MutableLiveData<Service> _currentService = new MutableLiveData<>();
    public LiveData<Service> currentService = _currentService;



    public void getService(Long serviceId) {
        _serviceApi.getService(serviceId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _currentService.setValue(response.body());
                } else {
                    Log.e("ServiceViewModel", "Failed to fetch event");
                }
            }
            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                Log.e("ServiceViewModel", "Error " + t.getMessage());
            }
        });
    }
 }
