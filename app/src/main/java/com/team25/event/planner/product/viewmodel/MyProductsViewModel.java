package com.team25.event.planner.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.ErrorParse;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.FavoruriteOfferingDTO;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.product.api.ProductApi;
import com.team25.event.planner.product.model.MyProductCard;
import com.team25.event.planner.product.model.Product;
import com.team25.event.planner.user.api.UserApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProductsViewModel extends ViewModel {
    private final ProductApi productApi = ConnectionParams.productApi;
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
    private final UserApi userApi = ConnectionParams.userApi;
    private final OfferingCategoryApi offeringCategoryApi = ConnectionParams.offeringCategoryApi;

    private final MutableLiveData<Product> _selectedProduct = new MutableLiveData<>();
    public final LiveData<Product> selectedProduct = _selectedProduct;
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<Double> price = new MutableLiveData<>();
    public final MutableLiveData<Double> discount = new MutableLiveData<>();
    public final MutableLiveData<Boolean> available = new MutableLiveData<>();
    public final MutableLiveData<String> offeringCategoryName = new MutableLiveData<>();
    public final MutableLiveData<List<String>> eventTypeNames = new MutableLiveData<>();
    public final MutableLiveData<String> ownerName = new MutableLiveData<>();
    public final MutableLiveData<List<String>> images = new MutableLiveData<>();

    private final MutableLiveData<List<MyProductCard>> _products = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<MyProductCard>> products = _products;

    private final MutableLiveData<List<EventType>> _eventTypes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventType>> eventTypes = _eventTypes;

    private final MutableLiveData<List<OfferingCategory>> _offeringCategories = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCategory>> offeringCategories = _offeringCategories;

    public final ProductFilter productFilter = new ProductFilter();

    private int currentPage = 0;
    private boolean isEndReached = false;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private MutableLiveData<Boolean> _fav = new MutableLiveData<>();
    public LiveData<Boolean> fav = _fav;

    private final MutableLiveData<Boolean> _refreshSignal = new MutableLiveData<>(false);
    public final LiveData<Boolean> refreshSignal = _refreshSignal;

    public boolean favInd;

    @Setter
    private Long ownerId;


    public void favoriteProduct(Long productId, Long userId) {
        userApi.favoriteProduct(userId, new FavoruriteOfferingDTO(productId)).enqueue(new Callback<ProductCard>() {
            @Override
            public void onResponse(Call<ProductCard> call, Response<ProductCard> response) {
                if (response.isSuccessful()) {
                    _fav.setValue(true);
                    favInd = true;
                } else {
                    _serverError.postValue("Error favorite product");
                }
            }

            @Override
            public void onFailure(Call<ProductCard> call, Throwable t) {
                _serverError.postValue("network problem");
            }
        });
    }

    public void deleteFavoriteProduct(Long productId, Long userId) {
        userApi.deleteFavoriteProduct(userId, productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _fav.setValue(false);
                favInd = false;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _serverError.postValue("network problem");
            }
        });
    }


    public void loadNextPage() {
        if (isLoading() || isEndReached) return;
        _isLoading.setValue(true);

        productApi.getOwnerProducts(ownerId, currentPage, productFilter.buildQueryMap())
                .enqueue(new SideEffectResponseCallback<>(
                        page -> {
                            currentPage++;
                            if (page.isLast()) {
                                isEndReached = true;
                            }
                            List<MyProductCard> products = page.getContent();
                            for (MyProductCard p : products) {
                                p.setThumbnail(ConnectionParams.BASE_URL + "api/products/" + p.getId() + "/images/" + p.getThumbnail());
                            }
                            _products.postValue(products);
                        },
                        () -> _isLoading.postValue(false),
                        _serverError, "MyEventsViewModel"
                ));
    }

    /**
     * Resets page counter and loads the first page again, with the currently applied filters.
     */
    public void reload() {
        currentPage = 0;
        isEndReached = false;
        _products.postValue(new ArrayList<>());
        loadNextPage();
    }

    public boolean isEndReached() {
        return isEndReached;
    }

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }

    public void loadEventTypes() {
        eventTypeApi.getEventTypes().enqueue(new ResponseCallback<>(
                _eventTypes::postValue,
                _serverError, "MyProductsViewModel")
        );
    }

    public void loadOfferingCategories() {
        offeringCategoryApi.getOfferingCategories().enqueue(new ResponseCallback<>(
                _offeringCategories::postValue,
                _serverError, "MyProductsViewModel")
        );
    }

    public void deleteProduct(Long productId) {
        productApi.deleteProduct(productId).enqueue(new ResponseCallback<>(
                ignored -> reload(),
                _serverError, "MyProductsViewModel"
        ));
    }

    public void fetchProduct(Long id) {
        productApi.getProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _selectedProduct.postValue(response.body());
                    fillForm(response.body());
                } else {
                    _serverError.postValue(ErrorParse.catchError(response));
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                _serverError.postValue("Network problem");
            }
        });
    }

    private void fillForm(Product product) {
        name.postValue(product.getName());
        description.postValue(product.getDescription());
        price.postValue(product.getPrice());
        discount.postValue(product.getDiscount());
        offeringCategoryName.postValue(product.getOfferingCategory().getName());
        List<String> res = product.getEventTypes().stream().map(com.team25.event.planner.product.model.EventType::getName).collect(Collectors.toList());
        eventTypeNames.postValue(res);
        ownerName.postValue(product.getOwnerInfo().getName());
        images.setValue(
                product.getImages().stream().map(imageId ->
                        ConnectionParams.BASE_URL + "api/products/" + product.getId() + "/images/" + imageId
                ).collect(Collectors.toList())
        );
        available.postValue(product.isAvailable());
    }

    public void onRefreshHandleComplete() {
        _refreshSignal.setValue(false);
    }
}
