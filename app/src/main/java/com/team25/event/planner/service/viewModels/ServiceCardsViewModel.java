package com.team25.event.planner.service.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.service.api.ServiceApi;
import com.team25.event.planner.service.dto.ServiceFilterDTO;
import com.team25.event.planner.service.model.Service;
import com.team25.event.planner.service.model.ServiceCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Getter
    private int currentPage = 0;
    @Getter
    private int totalPages;
    public MutableLiveData<Boolean> paginationChanged = new MutableLiveData<>();
    public ServiceCardsViewModel() {

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

    public void setupFilter(String nameF, String pf, Boolean availableF, Long eventTypeId, Long offeringCategoryId) {
        if (Objects.equals(nameF, "")) {
            filterDTO.setName(null);
        } else {
            filterDTO.setName(nameF);
        }

        filterDTO.setAvailable(availableF);
        if (pf != null) {
            if (!Objects.equals(pf, "")) {
                if (validateInputNumber(pf)) {
                    filterDTO.setPrice(Double.parseDouble(pf));
                } else {
                    filterDTO.setPrice(0.0);
                }
            }
        }
        if(eventTypeId != null){
            filterDTO.setEventTypeId(eventTypeId);
        }
        if(offeringCategoryId != null){
            filterDTO.setOfferingCategoryId(offeringCategoryId);
        }

        filterServices();
    }

    public void     filterServices() {

        Map<String, String> queryMap = buildQueryMap(filterDTO);

        getServices(queryMap);

    }


    public void getServices(Map<String, String> queryMap) {
        ServiceApi serviceApi = ConnectionParams.serviceApi;
        Call<Page<Service>> call = serviceApi.getServices(queryMap, currentPage);

        call.enqueue(new Callback<Page<Service>>() {
            @Override
            public void onResponse(Call<Page<Service>> call, Response<Page<Service>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchService(response.body().getContent());
                    filterDTO.setName(null);
                    filterDTO.setAvailable(null);
                    filterDTO.setPrice(null);
                    filterDTO.setEventTypeId(null);
                    filterDTO.setOfferingCategoryId(null);
                    totalPages = response.body().getTotalPages();

                    paginationChanged.postValue(false);
                    paginationChanged.postValue(true);
                } else {
                    Log.e("ServiceCardsViewModel", "Error fetching services: ");
                }
            }

            @Override
            public void onFailure(Call<Page<Service>> call, Throwable t) {
                Log.e("ServiceCardsViewModel", "Error fetching services: " + t.getMessage());
            }
        });
    }

    private void fetchService(List<Service> originalServices){

        List<ServiceCard> modifiedServices = new ArrayList<>();

        for (Service service : originalServices) {
            ServiceCard modifiedService = new ServiceCard();

            modifiedService.setId(service.getId());
            modifiedService.setName(service.getName());
            modifiedService.setDescription(service.getDescription());
            modifiedService.setPrice(service.getPrice());

            String image = service.getImage();

            if (image != null && !image.isEmpty()) {
                modifiedService.setImage(image);
            } else {
                modifiedService.setImage(null);
            }

            modifiedServices.add(modifiedService);
        }

        _services.setValue(modifiedServices);

    }

    public void getNextPage(){
        if(this.currentPage+1 < this.totalPages){
            this.currentPage++;
            paginationChanged.postValue(false);
            paginationChanged.postValue(true);
            getServices(buildQueryMap(filterDTO));
        }
    }

    public void getPreviousPage(){
        if(this.currentPage > 0){
            this.currentPage--;
            paginationChanged.postValue(false);
            paginationChanged.postValue(true);
            getServices(buildQueryMap(filterDTO));
        }
    }
}
