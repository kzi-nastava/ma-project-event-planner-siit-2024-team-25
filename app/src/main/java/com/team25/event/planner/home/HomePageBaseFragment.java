package com.team25.event.planner.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.IFilterViewModel;
import com.team25.event.planner.ISortViewModel;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentHomePageBaseBinding;
import com.team25.event.planner.event.fragments.EventsFragment;
import com.team25.event.planner.event.fragments.TopEventsFragment;
import com.team25.event.planner.event.viewmodel.EventFilterViewModel;
import com.team25.event.planner.event.viewmodel.EventSortViewModel;
import com.team25.event.planner.offering.fragments.HomeOfferingsFragment;
import com.team25.event.planner.offering.fragments.TopOfferingsFragment;
import com.team25.event.planner.offering.viewmodel.OfferingFilterViewModel;
import com.team25.event.planner.offering.viewmodel.OfferingSortViewModel;


public class HomePageBaseFragment extends Fragment {

    private  Button _eventButton;
    private Button _psButton;
    public IFilterViewModel homePageFilterViewModel;
    public ISortViewModel homePageSortViewModel;
    private FragmentHomePageBaseBinding _binding;
    public HomePageBaseFragment() {

    }

    public static HomePageBaseFragment newInstance() {
        return new HomePageBaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _binding = FragmentHomePageBaseBinding.inflate(inflater, container, false);
        _binding.setLifecycleOwner(this);

        return _binding.getRoot();
    }

    private void changeButton(Button newButton, Button oldButton){
        newButton.setBackgroundColor(getResources().getColor(R.color.white));
        newButton.setOnClickListener(null);
        newButton.setTextColor(getResources().getColor(R.color.primary));
        oldButton.setBackgroundColor(getResources().getColor(R.color.primary));
        oldButton.setTextColor(getResources().getColor(R.color.white));
    }
    private void eventButtonClick(){
        FragmentTransition.to(new EventsFragment(), requireActivity(), false, _binding.homeContainer.getId());
        changeButton(_eventButton, _psButton);
        _psButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                psButtonClick();

            }
        });
        homePageFilterViewModel = new ViewModelProvider(this).get(EventFilterViewModel.class);
        _binding.setFilterViewModel(homePageFilterViewModel);

        homePageSortViewModel = new ViewModelProvider(this).get(EventSortViewModel.class);
        _binding.setSortViewModel(homePageSortViewModel);

        View dialogView = getLayoutInflater().inflate(R.layout.home_page_event_filter, null);
        homePageFilterViewModel.setFilter(requireActivity(), dialogView);
        View sortView = getLayoutInflater().inflate(R.layout.home_page_event_sort, null);
        homePageSortViewModel.setSort(requireActivity(), sortView);
    }


    private void psButtonClick(){
        FragmentTransition.to(new HomeOfferingsFragment(), requireActivity(), false, _binding.homeContainer.getId());

        changeButton(_psButton, _eventButton);
        _eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventButtonClick();
            }
        });

        homePageFilterViewModel = new ViewModelProvider(this).get(OfferingFilterViewModel.class);
        _binding.setFilterViewModel(homePageFilterViewModel);

        homePageSortViewModel = new ViewModelProvider(this).get(OfferingSortViewModel.class);
        _binding.setSortViewModel(homePageSortViewModel);

        View dialogView = getLayoutInflater().inflate(R.layout.home_page_offering_filter, null);
        homePageFilterViewModel.setFilter(requireActivity(), dialogView);
        View sortView = getLayoutInflater().inflate(R.layout.home_page_offering_sort, null);
        homePageSortViewModel.setSort(requireActivity(), sortView);

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(_binding.homeTopEvents.getId(), new TopEventsFragment())
                    .replace(_binding.homeTopOffers.getId(), new TopOfferingsFragment())
                    .commit();
        }


        _eventButton = _binding.eventButton;
        _psButton = _binding.psButton;

        _eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventButtonClick();
            }
        });

        _psButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psButtonClick();
            }
        });

    }
}