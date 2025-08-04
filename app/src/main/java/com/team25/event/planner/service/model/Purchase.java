package com.team25.event.planner.service.model;

import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Purchase {
    public MutableLiveData<LocalDate> selectedStartDate = new MutableLiveData<>();
    public MutableLiveData<LocalDate> selectedEndDate = new MutableLiveData<>();
    public MutableLiveData<LocalTime> selectedStartTime = new MutableLiveData<>();
    public MutableLiveData<LocalTime> selectedEndTime = new MutableLiveData<>();
    public MutableLiveData<String> price = new MutableLiveData<>();


    public Map<String, String> buildQuery(){
        Map<String, String> query = new HashMap<>();

        if(this.price.getValue() != null){
            query.put("price", this.price.getValue());
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
        return query;
    }
}
