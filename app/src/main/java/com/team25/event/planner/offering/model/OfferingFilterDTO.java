package com.team25.event.planner.offering.model;

import androidx.lifecycle.MutableLiveData;

import com.team25.event.planner.event.model.EventTypePreviewDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class OfferingFilterDTO {

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<EventTypePreviewDTO> selectedEventType = new MutableLiveData<>();
    public final MutableLiveData<EventTypePreviewDTO> selectedCategoryType = new MutableLiveData<>();
    public final MutableLiveData<String> minPrice = new MutableLiveData<>();
    public final MutableLiveData<String> maxPrice = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();
    public final MutableLiveData<String> selectedSortBy = new MutableLiveData<>();
    public final MutableLiveData<String> selectedSortCriteria = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();

    public final Map<String, String> sortByMap = new HashMap<>();
    public final Map<String, String> sortCriteriaMap = new HashMap<>();

    public OfferingFilterDTO(){
        sortByMap.put("", "");
        sortByMap.put("Name", "name");
        sortByMap.put("Availability", "isAvailable");
        sortByMap.put("Price", "price");

        sortCriteriaMap.put("", "");
        sortCriteriaMap.put("Ascending", "asc");
        sortCriteriaMap.put("Descending", "desc");
    }
    public Map<String, Object> buildQuery(){
        Map<String, Object> query = new HashMap<>();

        if(this.name.getValue() != null){
            query.put("name", this.name.getValue().trim());
        }
        if(this.selectedCategoryType.getValue()!= null){
            query.put("categoryId", this.selectedCategoryType.getValue().getName());
        }
        if(this.minPrice.getValue()!= null){
            query.put("minPrice", this.minPrice.getValue());
        }
        if(this.maxPrice.getValue()!= null){
            query.put("maxPrice", this.maxPrice.getValue());
        }
        if(this.isAvailable.getValue()!= null){
            query.put("isAvailable", this.isAvailable.getValue());
        }
        if(this.selectedEventType.getValue()!= null){
            if(this.selectedEventType.getValue().getId()!=null){
                query.put("eventTypeId", this.selectedEventType.getValue().id.toString());
            }
        }
        if(this.selectedSortBy.getValue()!= null){
            query.put("sortBy", this.sortByMap.get(this.selectedSortBy.getValue().trim()));
        }
        if(this.selectedSortCriteria.getValue()!= null){
            query.put("sortDirection", this.sortCriteriaMap.get(this.selectedSortCriteria.getValue().trim()));
        }
        if(this.description.getValue()!= null){
            query.put("description", this.description.getValue().trim());
        }
        return query;
    }
}
