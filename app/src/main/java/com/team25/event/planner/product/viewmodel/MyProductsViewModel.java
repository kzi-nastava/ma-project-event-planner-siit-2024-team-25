package com.team25.event.planner.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.product.api.ProductApi;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class MyProductsViewModel extends ViewModel {
    private final ProductApi productApi = ConnectionParams.productApi;
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
    private final OfferingCategoryApi offeringCategoryApi = ConnectionParams.offeringCategoryApi;

    private final MutableLiveData<List<OfferingCard>> _products = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> products = _products;

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

    @Setter
    private Long ownerId;

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
                            _products.postValue(page.getContent());
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
}
