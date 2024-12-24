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

    private final MutableLiveData<Long> _budgetItemId = new MutableLiveData<>();
    public final LiveData<Long> budgetItemId = _budgetItemId;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;
    private final MutableLiveData<Boolean> _success = new MutableLiveData<>();
    public final LiveData<Boolean> success = _success;

    public final MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isOfferingCategorySuitable = new MutableLiveData<>(false);

    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState{
        private final String budget;
        private final String offeringCategory;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    public final MutableLiveData<String> budgetString = new MutableLiveData<>();
    public final MutableLiveData<Double> budget = new MutableLiveData<>();
    public final MutableLiveData<Long> eventId = new MutableLiveData<>(2L);
    public final MutableLiveData<Long> eventTypeId = new MutableLiveData<>(2L);
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
    public void fetchBudgetItems(){
        _allBudgetItems.postValue(fetchBudgetItemsTemp());
    }
    private List<BudgetItem> fetchBudgetItemsTemp() {
        List<BudgetItem> budgetItems = new ArrayList<>();

            budgetItemApi.getBudgetItems(eventId.getValue()).enqueue(new Callback<List<BudgetItemResponseDTO>>() {
                @Override
                public void onResponse(Call<List<BudgetItemResponseDTO>> call, Response<List<BudgetItemResponseDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            for (BudgetItemResponseDTO dto : response.body()) {
                                budgetItems.add(fetchFullBudgetItem(dto));
                            }
                        }catch (IOException e){
                            _serverError.postValue("Failed to fetch category");
                        }

                    } else {
                        _serverError.postValue("Failed to fetch budget items");
                    }
                }

                @Override
                public void onFailure(Call<List<BudgetItemResponseDTO>> call, Throwable t) {
                    _serverError.postValue("Network problem");
                }
            });


        return budgetItems;
    }

    private BudgetItem fetchFullBudgetItem(BudgetItemResponseDTO dto) throws IOException {
        Response<OfferingCategory> categoryResponse = categoryApi.getOfferingCategory(dto.getOfferingCategoryId()).execute(); // Dohvat kategorije

        if (categoryResponse.isSuccessful() && categoryResponse.body() != null) {
            OfferingCategory category = categoryResponse.body();
            BudgetItem budgetItem = new BudgetItem();
            budgetItem.setId(dto.getId());
            budgetItem.setOfferingCategory(category);
            budgetItem.setBudget(dto.getBudget());
            return budgetItem;
        } else {
            _serverError.postValue("Failed to fetch category");
            return null;
        }
    }

    public void setUoBudgetItemId(Long id){
        _budgetItemId.setValue(id);
        if(id != null){
            // fetch
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
               _serverError.postValue("Network problem");
           }
       });
    }

    public void fillForm(BudgetItem item){
        budgetString.setValue(String.valueOf(item.getBudget()));
        budget.setValue(item.getBudget());
    }
    public void resetForm(){
        budget.setValue(null);
        budgetString.setValue("");
    }
}
