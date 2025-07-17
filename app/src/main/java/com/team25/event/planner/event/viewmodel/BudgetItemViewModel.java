package com.team25.event.planner.event.viewmodel;

import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.airbnb.lottie.L;
import com.google.gson.Gson;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.model.ApiError;
import com.team25.event.planner.event.api.BudgetItemApi;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.BudgetItem;
import com.team25.event.planner.event.model.BudgetItemRequestDTO;
import com.team25.event.planner.event.model.BudgetItemResponseDTO;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetItemViewModel extends ViewModel {
    private BudgetItemApi budgetItemApi = ConnectionParams.budgetItemApi;
    private EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
    private OfferingCategoryApi categoryApi = ConnectionParams.offeringCategoryApi;
    private final MutableLiveData<List<BudgetItem>> _allBudgetItems = new MutableLiveData<>();
    public final LiveData<List<BudgetItem>> allBudgetItem = _allBudgetItems;
    private final MutableLiveData<Boolean> _successAllItems = new MutableLiveData<>();
    public final LiveData<Boolean> successAllItems = _successAllItems;
    public final MutableLiveData<String> overallBudgetString = new MutableLiveData<>();
    public final MutableLiveData<Double> overallBudget = new MutableLiveData<Double>(0.0);


    private final MutableLiveData<Long> _budgetItemId = new MutableLiveData<>();
    public final LiveData<Long> budgetItemId = _budgetItemId;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;
    private final MutableLiveData<Boolean> _success = new MutableLiveData<>();
    public final LiveData<Boolean> success = _success;

    private final MutableLiveData<Boolean> _deleted = new MutableLiveData<>();
    public final LiveData<Boolean> deleted = _deleted;

    public final MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isOfferingCategorySuitable = new MutableLiveData<>(false);

    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState{
        private final String budget;
        private final String offeringCategory;
    }

    public void resetSuccess() {
        _success.setValue(false);
    }
    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    public final MutableLiveData<String> budgetString = new MutableLiveData<>();
    public final MutableLiveData<Double> budget = new MutableLiveData<>();
    public final MutableLiveData<Long> eventId = new MutableLiveData<>();
    public final MutableLiveData<String> eventName = new MutableLiveData<>();
    public final MutableLiveData<Long> eventTypeId = new MutableLiveData<>();

    public final MutableLiveData<Long> offeringCategoryId = new MutableLiveData<>();
    private final MutableLiveData<List<OfferingCategory>> _allCategories = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCategory>> allCategories = _allCategories;

    private void catchError(Response<?> response){
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
    public void fetchSuitableOfferingCategories(){
        eventTypeApi.getOfferingCategoriesByEventType(eventTypeId.getValue()).enqueue(new Callback<List<OfferingCategory>>() {
            @Override
            public void onResponse(Call<List<OfferingCategory>> call, Response<List<OfferingCategory>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _allCategories.postValue(response.body());
                }else{
                    catchError(response);
                }
            }

            @Override
            public void onFailure(Call<List<OfferingCategory>> call, Throwable t) {
                _serverError.postValue("Network error");
            }
        });
    }

    public void saveBudgetItem(){
        if(validateForm()){
            BudgetItemRequestDTO budgetItem = new BudgetItemRequestDTO();
            budgetItem.setBudget(budget.getValue());
            budgetItem.setOfferingCategoryId(offeringCategoryId.getValue());
            budgetItem.setEventId(eventId.getValue());

            Call<BudgetItemResponseDTO> call = Boolean.TRUE.equals(isEditMode.getValue())
                    ? budgetItemApi.updateBudgetItem(budgetItemId.getValue(),budgetItem)
                    : budgetItemApi.createBudgetItem(budgetItem);
            call.enqueue(new Callback<BudgetItemResponseDTO>() {
                @Override
                public void onResponse(Call<BudgetItemResponseDTO> call, Response<BudgetItemResponseDTO> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        _success.postValue(true);
                        _successAllItems.postValue(true);
                    }else{
                        catchError(response);
                    }
                }

                @Override
                public void onFailure(Call<BudgetItemResponseDTO> call, Throwable t) {
                    _serverError.postValue("Network error");
                }
            });

        }
    }

    public void fetchBudgetItem(){
        budgetItemApi.getBudgetItem(budgetItemId.getValue(), eventId.getValue()).enqueue(new Callback<BudgetItemResponseDTO>() {
            @Override
            public void onResponse(Call<BudgetItemResponseDTO> call, Response<BudgetItemResponseDTO> response) {
                if(response.isSuccessful() && response.body()!=null){
                    fillForm(response.body());
                }else{
                    catchError(response);
                }
            }

            @Override
            public void onFailure(Call<BudgetItemResponseDTO> call, Throwable t) {
                _serverError.postValue("Network error");
            }
        });
    }
    public void deleteBudgetItem(Long id){
        budgetItemApi.deleteBudgetItem(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _deleted.setValue(true);
                }else{
                    catchError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                _serverError.postValue("Network error");
            }
        });
    }
    public void fetchBudgetItems() {
        overallBudget.postValue(0.0);
        budgetItemApi.getBudgetItems(eventId.getValue()).enqueue(new Callback<List<BudgetItemResponseDTO>>() {
            @Override
            public void onResponse(Call<List<BudgetItemResponseDTO>> call, Response<List<BudgetItemResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BudgetItem> budgetItems = new ArrayList<>();
                    for (BudgetItemResponseDTO dto : response.body()) {
                        BudgetItem budgetItem = mapToBudgetItem(dto);
                        budgetItems.add(budgetItem);
                        fetchCategoryDetails(budgetItem, dto.getOfferingCategoryId());
                    }
                    _allBudgetItems.postValue(budgetItems);
                    _successAllItems.postValue(true);
                } else {
                    catchError(response);
                }
            }

            @Override
            public void onFailure(Call<List<BudgetItemResponseDTO>> call, Throwable t) {
                _serverError.postValue("Network error");
            }
        });
    }
    private void updateBudgetItemsLiveData(BudgetItem updatedItem) {
        List<BudgetItem> currentItems = _allBudgetItems.getValue();
        if (currentItems != null) {
            for (int i = 0; i < currentItems.size(); i++) {
                if (currentItems.get(i).getId().equals(updatedItem.getId())) {
                    currentItems.set(i, updatedItem);
                    break;
                }
            }
            _allBudgetItems.postValue(currentItems);
        }
    }

    private void fetchCategoryDetails(BudgetItem budgetItem, Long id) {
        categoryApi.getOfferingCategory(id).enqueue(new Callback<OfferingCategory>() {
            @Override
            public void onResponse(Call<OfferingCategory> call, Response<OfferingCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    budgetItem.setOfferingCategory(response.body());
                    updateBudgetItemsLiveData(budgetItem);
                } else {
                    _serverError.postValue("Error fetching category details");
                }
            }

            @Override
            public void onFailure(Call<OfferingCategory> call, Throwable t) {
                _serverError.postValue("Network error while fetching category details");
            }
        });
    }
    private BudgetItem mapToBudgetItem(BudgetItemResponseDTO dto) {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(dto.getId());
        budgetItem.setBudget(dto.getBudget());
        return budgetItem;
    }

    public void setUoBudgetItemId(Long id){
        _budgetItemId.setValue(id);
        if(id != null){
            fetchBudgetItem();
        }else{
            resetForm();
        }
    }
    private boolean validateForm(){
        try {
            budget.setValue(Double.parseDouble(budgetString.getValue()));
        } catch (Exception e) {
            budget.setValue(0.0);
        }
        Double budgetErr = budget.getValue();
        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;
        if(budgetErr==null || budgetErr <= 0){
            errorUiStateBuilder.budget("Budget must be greater than 0");
            isValid = false;
        }
        if(offeringCategoryId.getValue() == null){
            errorUiStateBuilder.offeringCategory("You must choose an offering category");
            isValid = false;
        }else{
            if(Boolean.TRUE.equals(isOfferingCategorySuitable.getValue())){
                errorUiStateBuilder.offeringCategory("You have already have budget for the offering category");
                isValid = false;
            }
        }

        _errors.setValue(errorUiStateBuilder.build());
        return isValid;
    }

    public void isSuitableCategoryForEvent(){
       budgetItemApi.isOfferingCategorySuitableForEvent(offeringCategoryId.getValue(), eventId.getValue()).enqueue(new Callback<Boolean>() {
           @Override
           public void onResponse(Call<Boolean> call, Response<Boolean> response) {
               if(response.isSuccessful() && response.body()!=null){
                   if(response.body()){
                       isOfferingCategorySuitable.setValue(false);
                   }else{
                       isOfferingCategorySuitable.setValue(true);
                   }

               }else{
                   catchError(response);
               }
           }

           @Override
           public void onFailure(Call<Boolean> call, Throwable t) {
               _serverError.postValue("Network problem"+ t.toString());
           }
       });
    }

    public void fillForm(BudgetItemResponseDTO item){
        budgetString.setValue(String.valueOf(item.getBudget()));
        budget.setValue(item.getBudget());
        offeringCategoryId.setValue(item.getOfferingCategoryId());
    }
    public void resetForm(){
        budget.setValue(null);
        budgetString.setValue("");
        offeringCategoryId.setValue(null);
    }
    public void refreshBudget(){
        Double budget = 0.0;
        for (BudgetItem b:
                Objects.requireNonNull(_allBudgetItems.getValue())) {
            budget += b.getBudget();
        }
        budgetString.postValue(String.valueOf(budget) + " $");
    }
}
