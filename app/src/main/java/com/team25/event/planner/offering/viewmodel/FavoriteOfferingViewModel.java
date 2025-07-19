package com.team25.event.planner.offering.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.service.model.Offering;
import com.team25.event.planner.user.api.UserApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteOfferingViewModel extends ViewModel {
    private final UserApi userApi = ConnectionParams.userApi;

    private final MutableLiveData<List<OfferingCard>> _allOfferings = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> allOfferings = _allOfferings;
    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void getFavoriteServices(Long userId){
        userApi.getFavoriteService(userId).enqueue(new Callback<List<OfferingCard>>() {
            @Override
            public void onResponse(Call<List<OfferingCard>> call, Response<List<OfferingCard>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _allOfferings.setValue(response.body());
                }else{
                    _serverError.setValue("Error fetching favorite services");
                }
            }

            @Override
            public void onFailure(Call<List<OfferingCard>> call, Throwable t) {
                _serverError.setValue("Network problem");
            }
        });
    }

    public void getFavoriteProducts(Long userId){
        userApi.getFavoriteProducts(userId).enqueue(new Callback<List<OfferingCard>>() {
            @Override
            public void onResponse(Call<List<OfferingCard>> call, Response<List<OfferingCard>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _allOfferings.setValue(response.body());
                }else{
                    _serverError.setValue("Error fetching favorite products");
                }
            }

            @Override
            public void onFailure(Call<List<OfferingCard>> call, Throwable t) {
                _serverError.setValue("Network problem");
            }
        });
    }
}
