package com.team25.event.planner.product_service.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.product_service.api.ServiceApi;
import com.team25.event.planner.product_service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.product_service.dto.ServiceCreateResponseDTO;
import com.team25.event.planner.product_service.enums.ReservationType;
import com.team25.event.planner.product_service.model.Service;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceViewModel extends ViewModel {

    private ServiceApi _serviceApi = ConnectionParams.serviceApi;
    private final MutableLiveData<Service> _currentService = new MutableLiveData<>();
    public LiveData<Service> currentService = _currentService;
    private MutableLiveData<String> _serverError = new MutableLiveData<>();
    public LiveData<String> serverError = _serverError;

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<Double> price = new MutableLiveData<>();
    public final MutableLiveData<Double> discount = new MutableLiveData<>();
    public final MutableLiveData<String> specifics = new MutableLiveData<>();
    public final MutableLiveData<Integer> duration = new MutableLiveData<>();
    public final MutableLiveData<Integer> minimumArrangement = new MutableLiveData<>();
    public final MutableLiveData<Integer> maximumArrangement = new MutableLiveData<>();
    public final MutableLiveData<Integer> reservationDeadline = new MutableLiveData<>();
    public final MutableLiveData<Integer> cancellationDeadline = new MutableLiveData<>();
    public final MutableLiveData<String> reservationTypeService = new MutableLiveData<>();
    public final MutableLiveData<String> offeringCategoryName = new MutableLiveData<>();
    public final MutableLiveData<List<String>> eventTypeNames = new MutableLiveData<>();
    public final MutableLiveData<String> ownerName = new MutableLiveData<>();
    public final MutableLiveData<List<String>> images = new MutableLiveData<>();


    public void getService(Long serviceId) {
        _serviceApi.getService(serviceId).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _currentService.postValue(response.body());
                    fillForm();
                } else {
                    _serverError.postValue("Error fetch service");
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                _serverError.postValue("Error, network problem");
            }
        });
    }

    private void fillForm() {
        Service service = _currentService.getValue();
        name.postValue(service.getName());
        description.postValue(service.getDescription());
        price.postValue(service.getPrice());
        discount.postValue(service.getDiscount());
        specifics.postValue(service.getSpecifics());
        if(service.getDuration()>=1){
            duration.postValue(service.getDuration());
        }else{
            minimumArrangement.postValue(service.getMinimumArrangement());
            maximumArrangement.postValue(service.getMaximumArrangement());
        }
        reservationDeadline.postValue(service.getReservationDeadline());
        cancellationDeadline.postValue(service.getCancellationDeadline());
        if(service.getReservationType() == ReservationType.MANUAL){
            reservationTypeService.postValue("Manual");
        }else{
            reservationTypeService.postValue("Automatic");
        }
        offeringCategoryName.postValue(service.getOfferingCategory().getName());
        List<String> res = service.getEventTypes().stream().map(EventType::getName).collect(Collectors.toList());
        eventTypeNames.postValue(res);
        ownerName.postValue(service.getOwner().getFullName());
        images.postValue(service.getImages());
    }
 }
