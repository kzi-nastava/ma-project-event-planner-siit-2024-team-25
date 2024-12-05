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
import com.team25.event.planner.core.ConnectionParams;
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

    private final MutableLiveData<List<OfferingCard>> _allOfferings = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> allOfferings = _allOfferings;
    private final MutableLiveData<List<OfferingCard>> _topOfferings = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> topOfferings = _topOfferings;
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public final MutableLiveData<String> city = new MutableLiveData<>();
    private final MutableLiveData<Integer> _currentPage = new MutableLiveData<>();
    public final LiveData<Integer> currentPage = _currentPage;
    private final MutableLiveData<Integer> _totalPage = new MutableLiveData<>();

    public final LiveData<Integer> totalPage = _totalPage;    public HomeOfferingViewModel(){
        _currentPage.setValue(0);
    }


    public void getTopOfferings(){
        String countryValue = country.getValue() != null ? country.getValue() : "";
        String cityValue = city.getValue() != null ? city.getValue() : "";

        OfferingApi offeringApi = ConnectionParams.offeringApi;
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

    public void getAllOfferings(){

        OfferingApi offeringApi = ConnectionParams.offeringApi;
        Call<Page<OfferingCard>> call = offeringApi.getAllOfferings(_currentPage.getValue());

        call.enqueue(new Callback<Page<OfferingCard>>() {
            @Override
            public void onResponse(Call<Page<OfferingCard>> call, Response<Page<OfferingCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _allOfferings.setValue(response.body().getContent());
                    _totalPage.setValue(response.body().getTotalPages());
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


    public void getNextPage(){
        if(this._currentPage.getValue()+1 < this._totalPage.getValue()){
            this._currentPage.setValue(this._currentPage.getValue()+1);
            this.getAllOfferings();
        }
    }

    public void getPreviousPage(){
        if(this._currentPage.getValue() > 0){
            this._currentPage.setValue(this._currentPage.getValue()-1);
            this.getAllOfferings();
        }
    }
    public void filter() {

    }

    public void sort() {
    }
}
