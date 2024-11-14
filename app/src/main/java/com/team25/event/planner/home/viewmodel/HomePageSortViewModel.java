package com.team25.event.planner.home.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;

public class HomePageSortViewModel extends ViewModel {

    private BottomSheetDialog sortDialog;
    public HomePageSortViewModel(){
    }

    public void setEventSort(Context context, View view){
        sortDialog = new BottomSheetDialog(context, R.style.TransparentBottomSheetDialogTheme);
        sortDialog.setContentView(view);
    }

    public void showSort(){
        if(sortDialog != null){
            sortDialog.show();
        }
    }


}
