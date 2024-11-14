package com.team25.event.planner.offering.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.IFilterViewModel;
import com.team25.event.planner.R;

public class OfferingFilterViewModel extends ViewModel implements IFilterViewModel {

    private BottomSheetDialog filterDialog;
    public OfferingFilterViewModel(){
    }

    @Override
    public void setFilter(Context context, View view) {
        filterDialog = new BottomSheetDialog(context);
        filterDialog.setContentView(view);
    }

    @Override
    public void showFilter(){

        if(filterDialog != null){

            String[] options = {"Option 1", "Option 2", "Option 3"};

            Spinner eventTypeSpinner = filterDialog.findViewById(R.id.offering_event_type_filter);
            Spinner offeringCategorySpinner = filterDialog.findViewById(R.id.offering_category_filter);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(filterDialog.getContext(), android.R.layout.simple_spinner_item, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if(eventTypeSpinner != null && offeringCategorySpinner != null){
                eventTypeSpinner.setAdapter(adapter);
                offeringCategorySpinner.setAdapter(adapter);
            }


            filterDialog.show();
        }
    }
}
