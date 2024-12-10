package com.team25.event.planner.offering.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.offering.Api.OfferingApi;
import com.team25.event.planner.offering.model.SubmittedOfferingCategory;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmittedCategoryViewModel extends ViewModel {
    private final OfferingApi offeringApi = ConnectionParams.offeringApi;

    private final MutableLiveData<List<SubmittedOfferingCategory>> _submittedCategories = new MutableLiveData<>();
    public final LiveData<List<SubmittedOfferingCategory>> submittedCategories = _submittedCategories;

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
    private final MutableLiveData<OfferingCategoryViewModel.ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<OfferingCategoryViewModel.ErrorUiState> errors = _errors;


    public boolean validateForm(){
        String name = this.name.getValue();
        String description = this.description.getValue();

        OfferingCategoryViewModel.ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = OfferingCategoryViewModel.ErrorUiState.builder();
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

    public void fetchSubmittedCategories(){
        offeringApi.getSubmittedCategories().enqueue(new Callback<List<SubmittedOfferingCategory>>() {
            @Override
            public void onResponse(Call<List<SubmittedOfferingCategory>> call, Response<List<SubmittedOfferingCategory>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _submittedCategories.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<SubmittedOfferingCategory>> call, Throwable t) {

            }
        });
    }
}
