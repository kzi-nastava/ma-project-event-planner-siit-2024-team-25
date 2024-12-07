package com.team25.event.planner.event.model;

import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class EventFilterDTO {
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> maxParticipantsString = new MutableLiveData<>();
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public final MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<LocalDate> selectedStartDate = new MutableLiveData<>();
    public MutableLiveData<LocalDate> selectedEndDate = new MutableLiveData<>();
    public MutableLiveData<LocalTime> selectedStartTime = new MutableLiveData<>();
    public MutableLiveData<LocalTime> selectedEndTime = new MutableLiveData<>();
    public final MutableLiveData<String> selectedSortBy = new MutableLiveData<>();
    public final MutableLiveData<String> selectedSortCriteria = new MutableLiveData<>();

    public final Map<String, String> sortByMap = new HashMap<>();
    public final Map<String, String> sortCriteriaMap = new HashMap<>();

    public EventFilterDTO(){
        sortByMap.put("", "");
        sortByMap.put("Name", "name");
        sortByMap.put("Start date", "startDate");
        sortByMap.put("Organizer", "organizer.firstName");
        sortByMap.put("Country", "location.country");
        sortByMap.put("City", "location.city");

        sortCriteriaMap.put("", "");
        sortCriteriaMap.put("Ascending", "asc");
        sortCriteriaMap.put("Descending", "desc");
    }

    public Map<String, String> buildQuery(){
        Map<String, String> query = new HashMap<>();

        if(this.name.getValue() != null){
            query.put("nameContains", this.name.getValue().trim());
        }
        if(this.maxParticipantsString.getValue()!=null){
            query.put("maxParticipants", this.maxParticipantsString.getValue().trim());
        }
        if(this.country.getValue()!= null){
            query.put("country", this.country.getValue().trim());
        }
        if(this.city.getValue()!= null){
            query.put("city", this.city.getValue().trim());
        }
        if(this.selectedStartDate.getValue()!= null){
            String formattedDate = this.selectedStartDate.getValue().format(DateTimeFormatter.ISO_DATE);
            query.put("startDate", formattedDate);
        }
        if(this.selectedEndDate.getValue()!= null){
            String formattedDate = this.selectedEndDate.getValue().format(DateTimeFormatter.ISO_DATE);
            query.put("endDate", formattedDate);
        }
        if(this.selectedStartTime.getValue()!= null){
            String formattedTime = this.selectedStartTime.getValue().format(DateTimeFormatter.ISO_TIME);
            query.put("startTime", formattedTime);
        }
        if(this.selectedEndTime.getValue()!= null){
            String formattedTime = this.selectedEndTime.getValue().format(DateTimeFormatter.ISO_TIME);
            query.put("endTime", formattedTime);
        }

        if(this.selectedSortBy.getValue()!= null){
            query.put("sortBy", this.sortByMap.get(this.selectedSortBy.getValue().trim()));
        }
        if(this.selectedSortCriteria.getValue()!= null){
            query.put("sortDirection", this.sortCriteriaMap.get(this.selectedSortCriteria.getValue().trim()));
        }

        return query;
    }

}
