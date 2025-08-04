package com.team25.event.planner.event.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.ErrorParse;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.fragments.PurchaseListFragment;
import com.team25.event.planner.event.model.PurchaseResponseDTO;
import com.team25.event.planner.offering.Api.OfferingApi;
import com.team25.event.planner.offering.model.OfferingFilterDTO;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.model.ProductPurchaseRequestDTO;
import com.team25.event.planner.offering.model.ProductPurchaseResponseDTO;
import com.team25.event.planner.service.api.PurchaseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseViewModel extends ViewModel {

    public final MutableLiveData<List<ProductCard>> products = new MutableLiveData<>();
    private final OfferingApi offeringApi = ConnectionParams.offeringApi;
    private final PurchaseApi purchaseApi = ConnectionParams.purchaseApi;

    private final MutableLiveData<List<PurchaseResponseDTO>> _purchaseList = new MutableLiveData<>();
    public final LiveData<List<PurchaseResponseDTO>> purchaseList = _purchaseList;
    private final MutableLiveData<Integer> _currentPage = new MutableLiveData<>(0);
    public final LiveData<Integer> currentPage = _currentPage;
    private final MutableLiveData<Integer> _totalPage = new MutableLiveData<>();
    public final LiveData<Integer> totalPage = _totalPage;
    public OfferingFilterDTO offeringFilterDTO = new OfferingFilterDTO();
    public MutableLiveData<Long> eventId = new MutableLiveData<>();
    public MutableLiveData<Boolean> purchaseResponse = new MutableLiveData<>();

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public PurchaseViewModel(){
        _currentPage.postValue(0);
    }

    public void getAllProducts(){
        Call<Page<ProductCard>> call = offeringApi.getProductsPurchase(_currentPage.getValue(), this.offeringFilterDTO.buildQuery());
        call.enqueue(new Callback<Page<ProductCard>>() {
            @Override
            public void onResponse(Call<Page<ProductCard>> call, Response<Page<ProductCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products.postValue(response.body().getContent());
                    _totalPage.setValue(response.body().getTotalPages());
                }
            }

            @Override
            public void onFailure(Call<Page<ProductCard>> call, Throwable t) {

            }
        });
    }

    public void purchaseProduct(Long productId){
        ProductPurchaseRequestDTO dto = new ProductPurchaseRequestDTO(productId);
        purchaseApi.purchaseProduct(eventId.getValue(),dto).enqueue(new Callback<ProductPurchaseResponseDTO>() {
            @Override
            public void onResponse(Call<ProductPurchaseResponseDTO> call, Response<ProductPurchaseResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    purchaseResponse.postValue(true);
                }else{
                    _serverError.setValue(ErrorParse.catchError(response));
                }
            }

            @Override
            public void onFailure(Call<ProductPurchaseResponseDTO> call, Throwable t) {
                purchaseResponse.postValue(false);
            }
        });
    }
    public void getNextPage(){
        if(this._currentPage.getValue()+1 < this._totalPage.getValue()){
            this._currentPage.setValue(this._currentPage.getValue()+1);
            getAllProducts();
        }
    }

    public void getPreviousPage(){
        if(this._currentPage.getValue() > 0){
            this._currentPage.setValue(this._currentPage.getValue()-1);
            getAllProducts();
        }
    }

    public void getPurchaseByEvent(Long eventId){
        purchaseApi.getPurchaseByEvent(eventId).enqueue(new Callback<List<PurchaseResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PurchaseResponseDTO>> call, Response<List<PurchaseResponseDTO>> response) {
                Log.d("API_CALL", "Response Body: " + response.body());
                if(response.isSuccessful() && response.body()!=null){
                    _purchaseList.postValue(response.body());
                }else{
                    _serverError.setValue(ErrorParse.catchError(response));
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseResponseDTO>> call, Throwable t) {
                purchaseResponse.postValue(false);
                Log.e("API_CALL", "Network failure: " + t.getMessage());
            }
        });
    }

    public void getPurchaseByOffering(Long offeringId){
        purchaseApi.getPurchaseByOffering(offeringId).enqueue(new Callback<List<PurchaseResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PurchaseResponseDTO>> call, Response<List<PurchaseResponseDTO>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _purchaseList.postValue(response.body());
                }else{
                    ErrorParse.catchError(response);
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseResponseDTO>> call, Throwable t) {

            }
        });
    }
}
