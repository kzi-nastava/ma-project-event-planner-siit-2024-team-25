package com.team25.event.planner.offering.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.OfferingCategoryType;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferingCategoryViewModel extends ViewModel {
    private final OfferingCategoryApi offeringCategoryApi = ConnectionParams.offeringCategoryApi;
    private final MutableLiveData<List<OfferingCategory>> _allCategories = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCategory>> allCategories = _allCategories;

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Boolean> _success = new MutableLiveData<>();
    public final LiveData<Boolean> success = _success;

    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState {
        private final String name;
        private final String description;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    public boolean validateForm(){
        String name = this.name.getValue();
        String description = this.description.getValue();

        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if (name == null || name.isBlank()) {
            errorUiStateBuilder.name("Name is required.");
            isValid = false;
        }
        if (description == null || description.isBlank()) {
            errorUiStateBuilder.description("Description is required.");
            isValid = false;
        }

        _errors.setValue(errorUiStateBuilder.build());
        return isValid;

    }

    public void createCategory(){
        if(validateForm()){
            OfferingCategory offeringCategory = new OfferingCategory();
            offeringCategory.setName(name.getValue());
            offeringCategory.setDescription(description.getValue());
            offeringCategory.setStatus(OfferingCategoryType.ACCEPTED);

            offeringCategoryApi.createOfferingCategory(offeringCategory).enqueue(new Callback<OfferingCategory>() {
                @Override
                public void onResponse(Call<OfferingCategory> call, Response<OfferingCategory> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        _success.postValue(true);
                    }
                }

                @Override
                public void onFailure(Call<OfferingCategory> call, Throwable t) {

                }
            });
        }
    }

    public void fetchOfferingCategories(){
        offeringCategoryApi.getOfferingCategories().enqueue(new Callback<List<OfferingCategory>>() {
            @Override
            public void onResponse(Call<List<OfferingCategory>> call, Response<List<OfferingCategory>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _allCategories.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<OfferingCategory>> call, Throwable t) {

            }
        });
    }


}
