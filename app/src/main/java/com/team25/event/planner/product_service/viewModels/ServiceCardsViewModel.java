package com.team25.event.planner.product_service.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectiongParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.product_service.api.ServiceApi;
import com.team25.event.planner.product_service.dto.ServiceFilterDTO;
import com.team25.event.planner.product_service.model.ServiceCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCardsViewModel extends ViewModel {
    public final MutableLiveData<String> name = new MutableLiveData<>();
    //event type
    //category
    public final MutableLiveData<String> priceString = new MutableLiveData<>("");
    public MutableLiveData<Double> price = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();

    private final MutableLiveData<List<ServiceCard>> _services = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<ServiceCard>> services = _services;
    public ServiceFilterDTO filterDTO = new ServiceFilterDTO();



    public ServiceCardsViewModel(){

    }

    public Map<String, String> buildQueryMap(ServiceFilterDTO filter) {
        Map<String, String> queryMap = new HashMap<>();

        if (filter.getName() != null) {
            queryMap.put("name", filter.getName());
        }
        if (filter.getEventTypeId() != null) {
            queryMap.put("eventTypeId", String.valueOf(filter.getEventTypeId()));
        }
        if (filter.getOfferingCategoryId() != null) {
            queryMap.put("offeringCategoryId", String.valueOf(filter.getOfferingCategoryId()));
        }
        if (filter.getPrice() != null) {
            queryMap.put("price", String.valueOf(filter.getPrice()));
        }
        if (filter.getAvailable() != null) {
            queryMap.put("available", String.valueOf(filter.getAvailable()));
        }
        if (filter.getOwnerId() != null) {
            queryMap.put("ownerId", String.valueOf(filter.getOwnerId()));
        }

        return queryMap;
    }

    public boolean validateInputNumber(String text) {
        try {
            Double number = Double.parseDouble(text);
            price.setValue(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void setupFilter(){
        filterDTO.setName("hhh");
        filterDTO.setAvailable(isAvailable.getValue());
        //filterDTO.setOwnerId(11L);
        if(!Objects.equals(priceString.getValue(), "")){
            if(validateInputNumber(priceString.getValue())){
                filterDTO.setPrice(price.getValue());
            }else{
                filterDTO.setPrice(0.0);
            }
        }
        filterServices();
    }
    public void filterServices(){

        Map<String, String> queryMap = buildQueryMap(filterDTO);

        getServices(queryMap);
    }


    public void getServices(Map<String, String> queryMap){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectiongParams.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceApi serviceApi = retrofit.create(ServiceApi.class);
        Call<Page<ServiceCard>> call = serviceApi.getServices(queryMap);

        call.enqueue(new Callback<Page<ServiceCard>>() {
            @Override
            public void onResponse(Call<Page<ServiceCard>> call, Response<Page<ServiceCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _services.setValue(response.body().getContent());
                } else {
                    Log.e("ServiceCardsViewModel", "Error fetching services: ");
                }
            }

            @Override
            public void onFailure(Call<Page<ServiceCard>> call, Throwable t) {
                Log.e("ServiceCardsViewModel", "Error fetching services: " + t.getMessage());
            }
        });
    }
}
