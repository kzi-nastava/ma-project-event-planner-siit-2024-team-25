package com.team25.event.planner.event.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.IFilterViewModel;
import com.team25.event.planner.ISortViewModel;
import com.team25.event.planner.R;

public class EventSortViewModel extends ViewModel implements ISortViewModel {
    private BottomSheetDialog sortDialog;
    public EventSortViewModel(){
    }

    @Override
    public void setSort(Context context, View view){
        sortDialog = new BottomSheetDialog(context);
        sortDialog.setContentView(view);
    }

    @Override
    public void showSort(){
        if(sortDialog != null){

            String[] options = {"Option 1", "Option 2", "Option 3"};


            Spinner typeSpinner = sortDialog.findViewById(R.id.sort_type);
            Spinner categorySpinner = sortDialog.findViewById(R.id.sort_category);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(sortDialog.getContext(), android.R.layout.simple_spinner_item, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            typeSpinner.setAdapter(adapter);
            categorySpinner.setAdapter(adapter);


            sortDialog.show();
        }
    }
}
