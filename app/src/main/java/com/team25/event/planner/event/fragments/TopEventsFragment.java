package com.team25.event.planner.event.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.databinding.FragmentHomePageBaseBinding;
import com.team25.event.planner.databinding.FragmentTopEventListBinding;
import com.team25.event.planner.databinding.FragmentTopEventsBinding;
import com.team25.event.planner.model.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TopEventsFragment extends Fragment {

    private ArrayList<Event> events = new ArrayList<Event>();
    private FragmentTopEventsBinding binding;
    int currentSelectedIndex;

    public static TopEventsFragment newInstance() {
        return new TopEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prepareEventList(events);
        binding = FragmentTopEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("SADASDASDASD", String.valueOf(currentSelectedIndex));
        FragmentTransition.to(TopEventsListFragment.newInstance(events), requireActivity(), false, binding.topEventsContainer.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareEventList(ArrayList<Event> events){
        events.clear();
        events.add(new Event(1, "Concert", "Stefan", new Date()));
        events.add(new Event(2, "Concert", "Petar", new Date()));
        events.add(new Event(3, "Concert", "Milos", new Date()));
        events.add(new Event(4, "Concert", "Nikola", new Date()));
        events.add(new Event(5, "Concert", "Milan", new Date()));
    }
}