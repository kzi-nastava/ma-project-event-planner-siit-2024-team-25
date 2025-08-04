package com.team25.event.planner.service.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;

import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.model.EventType;


import com.team25.event.planner.offering.model.FavoruriteOfferingDTO;
import com.team25.event.planner.product.model.Product;
import com.team25.event.planner.service.api.ServiceApi;
import com.team25.event.planner.service.dto.ServiceCreateResponseDTO;
import com.team25.event.planner.service.enums.ReservationType;
import com.team25.event.planner.service.model.Offering;
import com.team25.event.planner.service.model.Service;
import com.team25.event.planner.service.model.ServiceCard;
import com.team25.event.planner.user.api.UserApi;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceViewModel extends ViewModel {

    private ServiceApi _serviceApi = ConnectionParams.serviceApi;
    private UserApi _userApi = ConnectionParams.userApi;
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
    public final MutableLiveData<Boolean> showDuration = new MutableLiveData<>();

    private MutableLiveData<Boolean> _fav = new MutableLiveData<>();
    public LiveData<Boolean> fav = _fav;

    public boolean favInd;


    public void favoriteService(Long serviceId, Long userId){
        _userApi.favoriteService(userId, new FavoruriteOfferingDTO(serviceId)).enqueue(new Callback<ServiceCard>() {
            @Override
            public void onResponse(Call<ServiceCard> call, Response<ServiceCard> response) {

                if (response.isSuccessful()) {
                        _fav.setValue(true);
                        favInd = true;
                } else {
                    _serverError.postValue("Error favorite service");
                }
            }

            @Override
            public void onFailure(Call<ServiceCard> call, Throwable t) {
                _serverError.postValue("Error, network problem");
            }
        });
    }
    public void deleteFavoriteService(Long serviceId, Long userId){
        _userApi.deleteFavoriteService(userId, serviceId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _fav.setValue(false);
                favInd = false;

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _serverError.postValue("Error, network problem");
            }
        });
    }
    public void getService(Long serviceId) {
        _serviceApi.getService(serviceId).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful() && response.body() != null) {

                    fillForm(response.body());
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

    private void fillForm(Service service) {
        name.postValue(service.getName());
        description.postValue(service.getDescription());
        price.postValue(service.getPrice());
        discount.postValue(service.getDiscount());
        specifics.postValue(service.getSpecifics());
        if(service.getDuration()>=1){
            duration.postValue(service.getDuration());
            showDuration.postValue(true);
        }else{
            minimumArrangement.postValue(service.getMinimumArrangement());
            maximumArrangement.postValue(service.getMaximumArrangement());
            showDuration.postValue(false);
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
        ownerName.postValue(service.getOwner().getName());
        images.setValue(
                service.getImages().stream().map(imageId ->
                        ConnectionParams.BASE_URL + "api/services/" + service.getId() + "/images/" + imageId
                ).collect(Collectors.toList())
        );
        _currentService.postValue(service);
    }
 }
