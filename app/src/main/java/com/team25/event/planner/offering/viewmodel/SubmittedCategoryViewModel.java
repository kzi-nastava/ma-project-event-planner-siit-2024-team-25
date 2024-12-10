package com.team25.event.planner.offering.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.offering.Api.OfferingApi;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.OfferingCategoryType;
import com.team25.event.planner.offering.model.SubmittedOfferingCategory;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmittedCategoryViewModel extends ViewModel {
    private final OfferingApi offeringApi = ConnectionParams.offeringApi;
    private final OfferingCategoryApi offeringCategoryApi = ConnectionParams.offeringCategoryApi;

    private final MutableLiveData<List<SubmittedOfferingCategory>> _submittedCategories = new MutableLiveData<>();
    public final LiveData<List<SubmittedOfferingCategory>> submittedCategories = _submittedCategories;

    public final MutableLiveData<Long> offeringId = new MutableLiveData<>();
    public final MutableLiveData<Long> categoryId = new MutableLiveData<>();
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
        private final String createdCategory;
    }
    private final MutableLiveData<SubmittedCategoryViewModel.ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<SubmittedCategoryViewModel.ErrorUiState> errors = _errors;


    public boolean validateForm(){
        String name = this.name.getValue();
        String description = this.description.getValue();

        SubmittedCategoryViewModel.ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = SubmittedCategoryViewModel.ErrorUiState.builder();
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
    public boolean validSpinner(Integer size){
        SubmittedCategoryViewModel.ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = SubmittedCategoryViewModel.ErrorUiState.builder();
        boolean isValid = true;
        if(size <= 0){
            errorUiStateBuilder.createdCategory("There is no created category, you are not able to choose.");
            isValid = false;
        }
        _errors.setValue(errorUiStateBuilder.build());
        return isValid;
    }

    // id for offering, id submitted category, id already existed category = choose
    public void changeOfferingsCategory(Long newCategoryId, Integer size){
        if(validSpinner(size)){
            offeringApi.updateOfferingsCategory(offeringId.getValue(),categoryId.getValue(),newCategoryId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        _success.postValue(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }

    public void updateOfferingCategory(){
        if(validateForm()){
            OfferingCategory offeringCategory = new OfferingCategory();
            offeringCategory.setName(name.getValue());
            offeringCategory.setDescription(description.getValue());
            offeringCategory.setStatus(OfferingCategoryType.ACCEPTED);
            offeringCategoryApi.editOfferingCategory(categoryId.getValue(),offeringCategory).enqueue(new Callback<OfferingCategory>() {
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

    public void fetchSubmittedCategory(Long id){
        offeringCategoryApi.getSubmittedOfferingCategory(id).enqueue(new Callback<OfferingCategory>() {
            @Override
            public void onResponse(Call<OfferingCategory> call, Response<OfferingCategory> response) {
                if(response.isSuccessful() && response.body()!=null){
                    fillForm(response.body());
                }
            }

            @Override
            public void onFailure(Call<OfferingCategory> call, Throwable t) {

            }
        });
    }

    public void fillForm(OfferingCategory body){
        name.setValue(body.getName());
        description.setValue(body.getDescription());
    }
}
