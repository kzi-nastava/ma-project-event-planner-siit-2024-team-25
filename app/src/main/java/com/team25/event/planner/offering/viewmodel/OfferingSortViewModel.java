package com.team25.event.planner.offering.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.ISortViewModel;
import com.team25.event.planner.R;

public class OfferingSortViewModel extends ViewModel implements ISortViewModel {
    private BottomSheetDialog sortDialog;
    public OfferingSortViewModel(){
    }

    public void setSort(Context context, View view){
        sortDialog = new BottomSheetDialog(context, R.style.TransparentBottomSheetDialogTheme);
        sortDialog.setContentView(view);
    }

    public void showSort(){
        if(sortDialog != null){

            String[] options = {"Option 1", "Option 2", "Option 3"};


            Spinner typeSpinner = sortDialog.findViewById(R.id.offering_sort_type);
            Spinner categorySpinner = sortDialog.findViewById(R.id.offering_sort_category);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(sortDialog.getContext(), android.R.layout.simple_spinner_item, options){
                @Override
                public View getView(int position, View convertView, android.view.ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((android.widget.TextView) view).setTextColor(sortDialog.getContext().getResources().getColor(R.color.primary));
                    return view;
                }

                @Override
                public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    ((android.widget.TextView) view).setTextColor(sortDialog.getContext().getResources().getColor(R.color.white));
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if(typeSpinner != null && categorySpinner != null){
                typeSpinner.setAdapter(adapter);
                categorySpinner.setAdapter(adapter);
            }


            sortDialog.show();
        }
    }

}
