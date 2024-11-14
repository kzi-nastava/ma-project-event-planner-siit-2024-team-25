package com.team25.event.planner.product_service.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FinishPageCreateingCerviceViewModel extends ViewModel {
    public final MutableLiveData<Boolean> toFinish = new MutableLiveData<>();
    public final MutableLiveData<Boolean> toSecond = new MutableLiveData<>();


    public void Finish(){
        toFinish.setValue(true);
    }

    public void ToSecond(){
        toSecond.setValue(true);
    }
}