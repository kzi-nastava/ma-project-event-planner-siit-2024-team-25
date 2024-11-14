package com.team25.event.planner;

import android.transition.TransitionInflater;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTransition {
    public static void toLeft(Fragment newFragment, FragmentActivity activity, boolean addToBackstack, int layoutViewID)
    {
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_in,
                        R.anim.fade_in,
                        R.anim.fade_in
                )
                .replace(layoutViewID, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);

        transaction.commit();
    }

    public static void toRight(Fragment newFragment, FragmentActivity activity, boolean addToBackstack, int layoutViewID)
    {
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_out,
                        R.anim.slide_out,
                        R.anim.fade_out,
                        R.anim.fade_out
                )
                .replace(layoutViewID, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);

        transaction.commit();
    }

    public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack, int layoutViewID)
    {
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutViewID, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);

        transaction.commit();
    }
}
