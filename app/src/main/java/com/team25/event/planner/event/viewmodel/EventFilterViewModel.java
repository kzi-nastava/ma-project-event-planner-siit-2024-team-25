package com.team25.event.planner.event.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.IFilterViewModel;
import com.team25.event.planner.R;

public class EventFilterViewModel extends ViewModel implements IFilterViewModel {
    private BottomSheetDialog filterDialog;
    public EventFilterViewModel(){
    }

    @Override
    public void setFilter(Context context, View view){
        filterDialog = new BottomSheetDialog(context, R.style.TransparentBottomSheetDialogTheme);
        filterDialog.setContentView(view);
    }

    @Override
    public void showFilter(){


        String[] options = {"Option 1", "Option 2", "Option 3"};


        Spinner eventTypeSpinner = filterDialog.findViewById(R.id.event_type_filter);
        Spinner PrivacyTypeSpinner = filterDialog.findViewById(R.id.privacy_type_filter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(filterDialog.getContext(), android.R.layout.simple_spinner_item, options){
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((android.widget.TextView) view).setTextColor(filterDialog.getContext().getResources().getColor(R.color.primary));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((android.widget.TextView) view).setTextColor(filterDialog.getContext().getResources().getColor(R.color.white));
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(eventTypeSpinner != null && PrivacyTypeSpinner != null){
            eventTypeSpinner.setAdapter(adapter);
            PrivacyTypeSpinner.setAdapter(adapter);
        }


        filterDialog.show();


        if(filterDialog != null){
            filterDialog.show();
        }
    }

}
