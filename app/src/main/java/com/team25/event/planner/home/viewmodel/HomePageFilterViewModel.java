package com.team25.event.planner.home.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;


public class HomePageFilterViewModel extends ViewModel {

    private BottomSheetDialog filterDialog;
    public HomePageFilterViewModel(){
    }

    public void setEventFilter(Context context, View view){
        filterDialog = new BottomSheetDialog(context, R.style.TransparentBottomSheetDialogTheme);
        filterDialog.setContentView(view);
    }

    public void showFilter(){
        if(filterDialog != null){
            filterDialog.show();
        }
    }


}
