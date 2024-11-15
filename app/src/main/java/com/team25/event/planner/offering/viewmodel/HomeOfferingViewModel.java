package com.team25.event.planner.offering.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.model.ServiceCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeOfferingViewModel extends ViewModel {

    private final MutableLiveData<List<OfferingCard>> _offerings = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCard>> offerings = _offerings;
    public final MutableLiveData<String> country = new MutableLiveData<>();
    public HomeOfferingViewModel(){

        List<OfferingCard> offeringCards = new ArrayList<>();
        offeringCards.add(new ProductCard(1, "Product 1", 17500, "Stefan"));
        offeringCards.add(new ProductCard(2, "Product 2", 17500, "Stefan"));
        offeringCards.add(new ProductCard(3, "Product 3", 17500, "Stefan"));
        offeringCards.add(new ProductCard(4, "Product 4", 17500, "Stefan"));
        offeringCards.add(new ProductCard(5, "Product 5", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(6, "Service 1", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(7, "Service 2", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(8, "Service 3", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(9, "Service 4", 17500, "Stefan"));
        offeringCards.add(new ServiceCard(10, "Service 5", 17500, "Stefan"));
        _offerings.setValue(offeringCards);
    }

    public void filter() {

    }

    public void sort() {
    }
}
