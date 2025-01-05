package com.team25.event.planner.offering.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.offering.Api.PriceListApi;
import com.team25.event.planner.offering.model.PriceListItemResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListViewModel extends ViewModel {
    public final MutableLiveData<Long> ownerId = new MutableLiveData<>();

    private final PriceListApi priceListApi = ConnectionParams.priceListApi;

    private final MutableLiveData<List<PriceListItemResponseDTO>> _priceList = new MutableLiveData<>();
    public final LiveData<List<PriceListItemResponseDTO>> priceListItems = _priceList;

    public void fetchProductsPriceList(){
        priceListApi.getProducts(ownerId.getValue()).enqueue(new Callback<List<PriceListItemResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PriceListItemResponseDTO>> call, Response<List<PriceListItemResponseDTO>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _priceList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PriceListItemResponseDTO>> call, Throwable t) {

            }
        });
    }

    public void fetchServicesPriceList(){
        priceListApi.getServices(ownerId.getValue()).enqueue(new Callback<List<PriceListItemResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PriceListItemResponseDTO>> call, Response<List<PriceListItemResponseDTO>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _priceList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PriceListItemResponseDTO>> call, Throwable t) {

            }
        });
    }
}
