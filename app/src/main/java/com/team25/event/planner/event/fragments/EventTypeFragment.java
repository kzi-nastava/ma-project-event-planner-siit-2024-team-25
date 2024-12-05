package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class EventTypeFragment extends Fragment {
    public static final String ID_ARG_NAME = "eventTypeId";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            long eventTypeId = getArguments().getLong(ID_ARG_NAME);
            Log.d("DEBUG", "onCreateView: " + eventTypeId);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}