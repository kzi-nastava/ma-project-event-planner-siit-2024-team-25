package com.team25.event.planner.offering.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.model.EventTypePreviewDTO;
import com.team25.event.planner.event.model.OfferingCategoryPreviewDTO;
import com.team25.event.planner.offering.Api.OfferingApi;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.OfferingFilterDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeOfferingViewModel extends ViewModel {

    private final MutableLiveData<List<OfferingCard>> _allOfferings = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> allOfferings = _allOfferings;
    private final MutableLiveData<List<OfferingCard>> _topOfferings = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> topOfferings = _topOfferings;
    private final MutableLiveData<Integer> _currentPage = new MutableLiveData<>();
    public final LiveData<Integer> currentPage = _currentPage;
    private final MutableLiveData<Integer> _totalPage = new MutableLiveData<>();
    public OfferingFilterDTO offeringFilterDTO = new OfferingFilterDTO();
    private final MutableLiveData<List<OfferingCategoryPreviewDTO>> _allOfferingCategories = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCategoryPreviewDTO>> allOfferingCategories = _allOfferingCategories;
    public final MutableLiveData<Integer> selectedFilterId = new MutableLiveData<>();
    public final LiveData<Integer> totalPage = _totalPage;
    public HomeOfferingViewModel(){
        _currentPage.setValue(0);
    }


    public void getAllOfferingCategories(){
        OfferingCategoryApi offeringApi = ConnectionParams.offeringCategoryApi;
        Call<List<OfferingCategoryPreviewDTO>> call = offeringApi.getAllOfferingCategories();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<OfferingCategoryPreviewDTO>> call, Response<List<OfferingCategoryPreviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OfferingCategoryPreviewDTO> result = response.body();
                    result.add(0,new OfferingCategoryPreviewDTO(null,""));
                    _allOfferingCategories.setValue(result);
                } else {
                    Log.e("HomeOfferingViewModel", "Failed to fetch offering categories");
                }
            }

            @Override
            public void onFailure(Call<List<OfferingCategoryPreviewDTO>> call, Throwable t) {
                Log.e("HomeOfferingViewModel", "Error fetching offering categories: " + t.getMessage());
            }
        });
    }
    public void getTopOfferings(){
        String countryValue =  "";
        String cityValue =  "";

        OfferingApi offeringApi = ConnectionParams.offeringApi;
        Call<Page<OfferingCard>> call = offeringApi.getTopOfferings(countryValue, cityValue);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Page<OfferingCard>> call, Response<Page<OfferingCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _topOfferings.setValue(response.body().getContent());
                } else {
                    Log.e("HomeOfferingViewModel", "Failed to fetch top offerings");
                }
            }

            @Override
            public void onFailure(Call<Page<OfferingCard>> call, Throwable t) {
                Log.e("HomeEventViewModel", "Error fetching top offerings: " + t.getMessage());
            }
        });
    }


    public void getAllOfferings(){
        OfferingApi offeringApi = ConnectionParams.offeringApi;
        Call<Page<OfferingCard>> call = offeringApi.getAllOfferings(_currentPage.getValue(), this.offeringFilterDTO.buildQuery());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Page<OfferingCard>> call, Response<Page<OfferingCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _allOfferings.setValue(response.body().getContent());
                    _totalPage.setValue(response.body().getTotalPages());
                } else {
                    Log.e("HomeOfferingViewModel", "Failed to fetch all offerings");
                }
            }
            @Override
            public void onFailure(Call<Page<OfferingCard>> call, Throwable t) {
                Log.e("HomeOfferingViewModel", "Error fetching all offerings: " + t.getMessage());
            }
        });
    }

    public void getAllProducts(){
        OfferingApi offeringApi = ConnectionParams.offeringApi;
        Call<Page<OfferingCard>> call = offeringApi.getAllProducts(_currentPage.getValue(), this.offeringFilterDTO.buildQuery());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Page<OfferingCard>> call, Response<Page<OfferingCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _allOfferings.setValue(response.body().getContent());
                    _totalPage.setValue(response.body().getTotalPages());
                } else {
                    Log.e("HomeOfferingViewModel", "Failed to fetch all offerings");
                }
            }
            @Override
            public void onFailure(Call<Page<OfferingCard>> call, Throwable t) {
                Log.e("HomeOfferingViewModel", "Error fetching all offerings: " + t.getMessage());
            }
        });
    }

    public void getAllServices(){
        OfferingApi offeringApi = ConnectionParams.offeringApi;
        Call<Page<OfferingCard>> call = offeringApi.getAllServices(_currentPage.getValue(), this.offeringFilterDTO.buildQuery());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Page<OfferingCard>> call, Response<Page<OfferingCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _allOfferings.setValue(response.body().getContent());
                    _totalPage.setValue(response.body().getTotalPages());
                } else {
                    Log.e("HomeOfferingViewModel", "Failed to fetch all offerings");
                }
            }
            @Override
            public void onFailure(Call<Page<OfferingCard>> call, Throwable t) {
                Log.e("HomeOfferingViewModel", "Error fetching all offerings: " + t.getMessage());
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

    public void getOfferings() {
        if(selectedFilterId.getValue() == R.id.all_radio_button){
            this.getAllOfferings();
        }else if (selectedFilterId.getValue() == R.id.products_radio_button){
            this.getAllProducts();
        } else if (selectedFilterId.getValue() == R.id.services_radio_button) {
            this.getAllServices();
        }
    }

    public void restartFilter() {
        this.offeringFilterDTO = new OfferingFilterDTO();
        this.getAllOfferings();
    }
}
