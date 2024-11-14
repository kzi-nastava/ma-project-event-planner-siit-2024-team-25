package com.team25.event.planner;

import android.transition.TransitionInflater;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTransition {
    public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack, int layoutViewID)
    {
        System.out.println("pogdjena metoda");


        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                )
                .replace(layoutViewID, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);

        transaction.commit();
    }
}
