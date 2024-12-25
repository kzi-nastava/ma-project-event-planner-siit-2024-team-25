package com.team25.event.planner.offering.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.model.ApiError;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.OfferingCategoryType;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferingCategoryViewModel extends ViewModel {
    private final OfferingCategoryApi offeringCategoryApi = ConnectionParams.offeringCategoryApi;
    private final MutableLiveData<List<OfferingCategory>> _allCategories = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCategory>> allCategories = _allCategories;
    private final MutableLiveData<Long> _categoryId = new MutableLiveData<>();
    public final LiveData<Long> categoryId = _categoryId;

    public final MutableLiveData<Long> id = new MutableLiveData<>();
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

    public void setUpCategoryId(Long id){
        _categoryId.setValue(id);
        if(id != null){
            fetchOfferingCategory(id);
        }else{
            resetForm();
        }
    }

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

    public void deleteOfferingCategory(Long id){
        offeringCategoryApi.deleteOfferingCategory(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _success.postValue(true);
                }//TODO
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void saveCategory(Boolean isEdit){
        if(validateForm()){
            OfferingCategory offeringCategory = new OfferingCategory();
            offeringCategory.setName(name.getValue());
            offeringCategory.setDescription(description.getValue());
            offeringCategory.setStatus(OfferingCategoryType.ACCEPTED);

            Call<OfferingCategory> call = isEdit
                    ? offeringCategoryApi.editOfferingCategory(categoryId.getValue(), offeringCategory)
                    : offeringCategoryApi.createOfferingCategory(offeringCategory);

            call.enqueue(new Callback<OfferingCategory>() {
                @Override
                public void onResponse(Call<OfferingCategory> call, Response<OfferingCategory> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        _success.postValue(true);
                    }else{
                        try (ResponseBody errorBody = response.errorBody()) {
                            if (errorBody != null) {
                                Gson gson = new Gson();
                                ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                                _serverError.postValue(apiError.getMessage());
                            } else {
                                _serverError.postValue("Unknown error occurred");
                            }
                        } catch (Exception e) {
                            _serverError.postValue("Error parsing server response");
                        }
                    }
                }

                @Override
                public void onFailure(Call<OfferingCategory> call, Throwable t) {
                    _serverError.postValue("Network error");
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
                    //_success.postValue(true);
                }
                else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                            _serverError.postValue(apiError.getMessage());
                            Log.e("OfferingCategoryViewModel", "Error: " + apiError.getMessage());
                        } else {
                            _serverError.postValue("Unknown error occurred");
                            Log.e("OfferingCategoryViewModel", "Error response with empty body");
                        }
                    } catch (Exception e) {
                        _serverError.postValue("Error parsing server response");
                        Log.e("OfferingCategoryViewModel", "Error parsing response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OfferingCategory>> call, Throwable t) {
                Log.e("OfferingCategoryViewModel", "Network error on offering category fetch: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
    }

    public void fetchOfferingCategory(Long id){
        offeringCategoryApi.getOfferingCategory(id).enqueue(new Callback<OfferingCategory>() {
            @Override
            public void onResponse(Call<OfferingCategory> call, Response<OfferingCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _categoryId.postValue(response.body().getId());
                    fillForm(response.body());
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                            _serverError.postValue(apiError.getMessage());
                            Log.e("OfferingCategoryViewModel", "Error: " + apiError.getMessage());
                        } else {
                            _serverError.postValue("Unknown error occurred");
                            Log.e("OfferingCategoryViewModel", "Error response with empty body");
                        }
                    } catch (Exception e) {
                        _serverError.postValue("Error parsing server response");
                        Log.e("OfferingCategoryViewModel", "Error parsing response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<OfferingCategory> call, Throwable t) {
                Log.e("OfferingCategoryViewModel", "Network error on offering category fetch: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
    }

    public void fillForm(OfferingCategory offeringCategory){
        name.setValue(offeringCategory.getName());
        description.setValue(offeringCategory.getDescription());
    }

    public void resetForm(){
        name.setValue("");
        description.setValue("");
    }
}
