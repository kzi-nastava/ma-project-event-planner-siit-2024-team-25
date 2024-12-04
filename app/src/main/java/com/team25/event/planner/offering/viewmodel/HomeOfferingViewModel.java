package com.team25.event.planner.offering.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectiongParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.offering.Api.OfferingApi;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.model.ServiceCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeOfferingViewModel extends ViewModel {

    private final MutableLiveData<List<OfferingCard>> _offerings = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> offerings = _offerings;

    private final MutableLiveData<List<OfferingCard>> _topOfferings = new MutableLiveData<>(new ArrayList<>());

    public final LiveData<List<OfferingCard>> topOfferings = _topOfferings;
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public final MutableLiveData<String> city = new MutableLiveData<>();
    public HomeOfferingViewModel(){

        List<OfferingCard> offeringCards = new ArrayList<>();
        offeringCards.add(new ProductCard(1, "Product 1", 17500, "Stefan", 5));
        offeringCards.add(new ProductCard(2, "Product 2", 17500, "Stefan", 5));
        offeringCards.add(new ProductCard(3, "Product 3", 17500, "Stefan",5));
        offeringCards.add(new ProductCard(4, "Product 4", 17500, "Stefan", 5));
        offeringCards.add(new ProductCard(5, "Product 5", 17500, "Stefan", 5));
        offeringCards.add(new ServiceCard(6, "Service 1", 17500, "Stefan", 5));
        offeringCards.add(new ServiceCard(7, "Service 2", 17500, "Stefan",5));
        offeringCards.add(new ServiceCard(8, "Service 3", 17500, "Stefan",5));
        offeringCards.add(new ServiceCard(9, "Service 4", 17500, "Stefan",5));
        offeringCards.add(new ServiceCard(10, "Service 5", 17500, "Stefan",5));
        _offerings.setValue(offeringCards);
    }


    public void getTopOfferings(){
        String countryValue = country.getValue() != null ? country.getValue() : "";
        String cityValue = city.getValue() != null ? city.getValue() : "";

        OfferingApi offeringApi = ConnectiongParams.offeringApi;
        Call<Page<OfferingCard>> call = offeringApi.getTopOfferings(countryValue, cityValue);

        call.enqueue(new Callback<Page<OfferingCard>>() {
            @Override
            public void onResponse(Call<Page<OfferingCard>> call, Response<Page<OfferingCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _topOfferings.setValue(response.body().getContent());
                } else {
                    Log.e("HomeEventViewModel", "Failed to fetch top events");
                }
            }
            @Override
            public void onFailure(Call<Page<OfferingCard>> call, Throwable t) {
                Log.e("HomeEventViewModel", "Error fetching top events: " + t.getMessage());
            }
        });
    }
    public void filter() {

    }

    public void sort() {
    }
}
