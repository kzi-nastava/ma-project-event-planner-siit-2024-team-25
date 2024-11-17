package com.team25.event.planner.product_service.viewModels;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.R;
import com.team25.event.planner.product_service.enums.ReservationType;
import com.team25.event.planner.product_service.model.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

public class ServiceAddFormViewModel extends ViewModel {

    private ArrayList<Service> services = new ArrayList<Service>();

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> specifics = new MutableLiveData<>();
    public final MutableLiveData<Integer> price = new MutableLiveData<>();
    public final MutableLiveData<Integer> discount = new MutableLiveData<>();
    //public final MutableLiveData<String> images = new MutableLiveData<>();
    public final MutableLiveData<String> eventTypes = new MutableLiveData<>();
    public final MutableLiveData<Boolean> option1 = new MutableLiveData<>();
    public final MutableLiveData<Boolean> option2 = new MutableLiveData<>();
    public final MutableLiveData<Boolean> option3 = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();
    public final MutableLiveData<String> reservationDate = new MutableLiveData<>(null);
    public final MutableLiveData<String> cancelDate = new MutableLiveData<>(null);
    public final MutableLiveData<ReservationType> confirmationType = new MutableLiveData<>();
    public final MutableLiveData<Boolean> confirmationTypeToggle = new MutableLiveData<>();
    public final MutableLiveData<String> categoryInput = new MutableLiveData<>();
    public final MutableLiveData<Boolean> categoryInputEnabled = new MutableLiveData<>();
    public final MutableLiveData<Integer> duration = new MutableLiveData<>();
    public final MutableLiveData<Integer> minArrangement = new MutableLiveData<>();
    public final MutableLiveData<Integer> maxArrangement = new MutableLiveData<>();

    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState {
        private final String name;
        private final String reservationDate;
        private final String cancelDate;
        private final int discount;
        private final int price;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;
    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;
    private final MutableLiveData<Boolean> _addedService = new MutableLiveData<>(false);
    public final LiveData<Boolean> addedService = _addedService;

    public final MutableLiveData<Boolean> secondToThird = new MutableLiveData<>();
    public final MutableLiveData<Boolean> secondToFirst = new MutableLiveData<>();
    public final MutableLiveData<Boolean> firstToSecond = new MutableLiveData<>();
    public final MutableLiveData<Boolean> cancelClicked = new MutableLiveData<>();
    public final MutableLiveData<Boolean> toFinish = new MutableLiveData<>();
    public final MutableLiveData<Boolean> toSecond = new MutableLiveData<>();

    public void FirstToSecond() {
        firstToSecond.setValue(true);
    }
    public void cancel() {
        cancelClicked.setValue(true);
    }
    public void SecondToThird() {
        secondToThird.setValue(true);
    }
    public void SecondToFirst() {
        secondToFirst.setValue(true);
    }
    public void Finish() {
        toFinish.setValue(true);
    }
    public void ToSecond() {
        toSecond.setValue(true);
    }

    public void onSeeking(SeekBar seekBar, Integer seekParams) {
        if (seekBar.getId() == R.id.durationValue) {
            duration.setValue(seekParams);
        } else if (seekBar.getId() == R.id.minArrValue) {
            minArrangement.setValue(seekParams);
        } else if(seekBar.getId() == R.id.maxArrValue) {
            maxArrangement.setValue(seekParams);
        }else if(seekBar.getId() == R.id.priceSeekBar2){
            discount.setValue(seekParams);
        }
    }

    public void onChooseCancellationDate(View v) {
        showDatePicker(v.getContext(), cancelDate);
    }

    public void onChooseReservationDate(View v) {
        showDatePicker(v.getContext(), reservationDate);
    }

    private void showDatePicker(final Context context, final MutableLiveData<String> dateLiveData) {
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int dayOfMonth;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("TAG", "showDatePicker: " + dateLiveData.getValue().isEmpty());
        if(!dateLiveData.getValue().isEmpty()){
            try{
                Date date = dateFormat.parse(dateLiveData.getValue());
                calendar.setTime(date);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH); // Meseci su 0-indeksirani
                year = calendar.get(Calendar.YEAR);
            }catch (Exception e){
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }

        }else{
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year1, month1, dayOfMonth1) -> {
            String date = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
            dateLiveData.setValue(date);  // Update the LiveData
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    public void findService(Integer idService){
        name.setValue("Name 1");
        description.setValue("Description 1");
        specifics.setValue("Specifics 1");
        price.setValue(100);
        discount.setValue(15);
        cancelDate.setValue("20/12/2024");
        reservationDate.setValue("10/12/2024");
        confirmationType.setValue(ReservationType.AUTOMATIC);
        isVisible.setValue(true);
        isAvailable.setValue(true);
        syncFront();
    }

    public void fillTheForm(){
        duration.setValue(1);
        minArrangement.setValue(1);
        maxArrangement.setValue(5);
        categoryInput.setValue("Category 10");
        categoryInputEnabled.setValue(false);
        option1.setValue(true);
        option3.setValue(true);
    }

    private void syncFront(){
        if(confirmationType.getValue() == ReservationType.AUTOMATIC){
            confirmationTypeToggle.setValue(true);
        }else{
            confirmationTypeToggle.setValue(false);
        }
    }

    public void restart(){
        categoryInputEnabled.setValue(true);
    }
}