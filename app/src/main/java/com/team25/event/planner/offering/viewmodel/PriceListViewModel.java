package com.team25.event.planner.offering.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.ErrorParse;
import com.team25.event.planner.event.model.BudgetItemResponseDTO;
import com.team25.event.planner.offering.Api.PriceListApi;
import com.team25.event.planner.offering.model.PriceListItemRequestDTO;
import com.team25.event.planner.offering.model.PriceListItemResponseDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListViewModel extends ViewModel {
    private final MutableLiveData<ResponseBody> _pdfData = new MutableLiveData<>();
    public LiveData<ResponseBody> pdfData = _pdfData;

    public final MutableLiveData<Long> ownerId = new MutableLiveData<>();

    private final PriceListApi priceListApi = ConnectionParams.priceListApi;

    private final MutableLiveData<List<PriceListItemResponseDTO>> _priceList = new MutableLiveData<>();
    public final LiveData<List<PriceListItemResponseDTO>> priceListItems = _priceList;
    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;
    private final MutableLiveData<Boolean> _editDone = new MutableLiveData<>();
    public final LiveData<Boolean> editDone = _editDone;
    public final MutableLiveData<String> discountText = new MutableLiveData<>();
    public final MutableLiveData<String> priceText = new MutableLiveData<>();
    public final MutableLiveData<Double> discount = new MutableLiveData<>();
    public final MutableLiveData<Double> price = new MutableLiveData<>();
    public final MutableLiveData<Long> offeringId = new MutableLiveData<>();

    private final MutableLiveData<PriceListViewModel.ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<PriceListViewModel.ErrorUiState> errors = _errors;
    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState {
        private final String price;
        private final String discount;
    }
    public Double parseStringToDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {

            return null;
        }
    }
    public boolean validateForm(){
        PriceListViewModel.ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = PriceListViewModel.ErrorUiState.builder();
        boolean isValid = true;

        try {
            price.setValue(Double.parseDouble(priceText.getValue()));
        } catch (Exception e) {
            errorUiStateBuilder.price("Price must be a valid number");
            isValid = false;
            _errors.setValue(errorUiStateBuilder.build());
            return isValid;
        }

        try {
            discount.setValue(Double.parseDouble(discountText.getValue()));
        } catch (Exception e) {
            errorUiStateBuilder.discount("Discount must be a valid number");
            isValid = false;
            _errors.setValue(errorUiStateBuilder.build());
            return isValid;
        }

        Double price = this.price.getValue();
        Double discount = this.discount.getValue();

        if (price == null || price<=0) {
            errorUiStateBuilder.price("Price must be grater than 0");
            isValid = false;
        }
        if(discount == null || discount <=0 || discount >100){
            errorUiStateBuilder.discount("Discount must be grater than 0 and less than 100");
            isValid = false;
        }
        _errors.setValue(errorUiStateBuilder.build());
        return isValid;

    }
    public void fetchProductsPriceList(){
        priceListApi.getProducts(ownerId.getValue()).enqueue(new Callback<List<PriceListItemResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PriceListItemResponseDTO>> call, Response<List<PriceListItemResponseDTO>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _priceList.postValue(response.body());
                }else{
                    _serverError.postValue(ErrorParse.catchError(response));
                }
            }

            @Override
            public void onFailure(Call<List<PriceListItemResponseDTO>> call, Throwable t) {
                _serverError.postValue("Network problemmm");
            }
        });
    }

    public void fetchServicesPriceList(){
        priceListApi.getServices(ownerId.getValue()).enqueue(new Callback<List<PriceListItemResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PriceListItemResponseDTO>> call, Response<List<PriceListItemResponseDTO>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _priceList.postValue(response.body());
                }else{
                    _serverError.postValue(ErrorParse.catchError(response));
                }
            }

            @Override
            public void onFailure(Call<List<PriceListItemResponseDTO>> call, Throwable t) {
                Log.e("RETROFIT", "Update failed", t);
                _serverError.postValue("Error: " + t.getClass().getSimpleName() + " - " + t.getMessage());

            }
        });
    }

    public void updatePriceListItem(){
        if(validateForm()){
            PriceListItemRequestDTO p = new PriceListItemRequestDTO(price.getValue(),discount.getValue());
            priceListApi.editOfferingCategory(offeringId.getValue(),p).enqueue(new Callback<PriceListItemResponseDTO>() {
                @Override
                public void onResponse(Call<PriceListItemResponseDTO> call, Response<PriceListItemResponseDTO> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        _editDone.postValue(true);
                    }else{
                        _serverError.postValue(ErrorParse.catchError(response));
                    }
                }

                @Override
                public void onFailure(Call<PriceListItemResponseDTO> call, Throwable t) {
                    _serverError.postValue("Network problem update" + t.toString());
                }
            });
        }

    }

    public void generatePDF(boolean isProduct){
        priceListApi.getPriceListReport(ownerId.getValue(), isProduct).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _pdfData.postValue(response.body());
                } else {
                    _serverError.postValue("Network problem");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                _serverError.postValue("Network problem" + t.toString());
            }
        });
    }
    public void fillForm(Double price, Double discount){
        priceText.setValue(String.valueOf(price));
        discountText.setValue(String.valueOf(discount));
    }
    public void clearError() {
        _serverError.setValue(null);
    }
}
