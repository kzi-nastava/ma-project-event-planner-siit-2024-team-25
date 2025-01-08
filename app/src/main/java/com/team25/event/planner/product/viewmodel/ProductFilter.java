package com.team25.event.planner.product.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilter {
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<Long> eventTypeId = new MutableLiveData<>();
    public final MutableLiveData<Long> categoryId = new MutableLiveData<>();
    public final MutableLiveData<Double> minPrice = new MutableLiveData<>();
    public final MutableLiveData<Double> maxPrice = new MutableLiveData<>();

    public Map<String, Object> buildQueryMap() {
        Map<String, Object> queryMap = new HashMap<>();

        String name = this.name.getValue();
        if (name != null && !name.isBlank()) {
            queryMap.put("name", name);
        }

        Long eventTypeId = this.eventTypeId.getValue();
        if (eventTypeId != null) {
            queryMap.put("eventTypeId", eventTypeId);
        }

        Long categoryId = this.categoryId.getValue();
        if (categoryId != null) {
            queryMap.put("categoryId", categoryId);
        }

        Double minPrice = this.minPrice.getValue();
        if (minPrice != null) {
            queryMap.put("minPrice", minPrice);
        }

        Double maxPrice = this.maxPrice.getValue();
        if (maxPrice != null) {
            queryMap.put("maxPrice", maxPrice);
        }

        return queryMap;
    }
}
