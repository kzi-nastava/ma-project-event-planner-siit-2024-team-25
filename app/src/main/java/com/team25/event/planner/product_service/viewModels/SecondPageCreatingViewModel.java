package com.team25.event.planner.product_service.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SecondPageCreatingViewModel extends ViewModel {
    public final MutableLiveData<Boolean> secondToThird = new MutableLiveData<>();

    public final MutableLiveData<Boolean> secondToFirst = new MutableLiveData<>();

    public void SecondToThird(){
        secondToThird.setValue(true);
    }

    public void SecondToFirst(){
        secondToFirst.setValue(true);
    }
}