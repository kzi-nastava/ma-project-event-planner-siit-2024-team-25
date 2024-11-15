package com.team25.event.planner.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentHomePageBaseBinding;
import com.team25.event.planner.databinding.HomePageEventFilterBinding;
import com.team25.event.planner.databinding.HomePageEventSortBinding;
import com.team25.event.planner.databinding.HomePageOfferingFilterBinding;
import com.team25.event.planner.databinding.HomePageOfferingSortBinding;
import com.team25.event.planner.event.fragments.EventsFragment;
import com.team25.event.planner.event.fragments.TopEventsFragment;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;
import com.team25.event.planner.offering.fragments.HomeOfferingsFragment;
import com.team25.event.planner.offering.fragments.TopOfferingsFragment;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;


public class HomePageBaseFragment extends Fragment {

    private Button _eventButton;
    private Button _psButton;

    private Button _filterButton;
    private Button _sortButton;

    private BottomSheetDialog _filterEventDialog;
    private BottomSheetDialog _sortEventDialog;
    private HomeEventViewModel _homeEventViewModel;

    private BottomSheetDialog _filterOfferingDialog;
    private BottomSheetDialog _sortOfferingDialog;
    private HomeOfferingViewModel _homeOfferingViewModel;
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
        _homeEventViewModel = new HomeEventViewModel();
        _homeOfferingViewModel = new HomeOfferingViewModel();
        return _binding.getRoot();
    }

    private void changeButton(Button newButton, Button oldButton) {
        newButton.setBackgroundColor(getResources().getColor(R.color.white));
        newButton.setOnClickListener(null);
        newButton.setTextColor(getResources().getColor(R.color.primary));
        oldButton.setBackgroundColor(getResources().getColor(R.color.primary));
        oldButton.setTextColor(getResources().getColor(R.color.white));
    }

    private void eventButtonClick() {
        FragmentTransition.toRight(new EventsFragment(_homeEventViewModel), requireActivity(), false, _binding.homeContainer.getId());
        changeButton(_eventButton, _psButton);
        _psButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                psButtonClick();
            }
        });

        setEventFilterDialog();
        setEventSortDialog();
    }


    private void psButtonClick() {
        FragmentTransition.toLeft(new HomeOfferingsFragment(_homeOfferingViewModel), requireActivity(), false, _binding.homeContainer.getId());

        setOfferingFilterDialog();
        setOfferingSortDialog();

        changeButton(_psButton, _eventButton);

        _eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventButtonClick();
            }
        });

    }

    public void setEventFilterDialog() {
        HomePageEventFilterBinding homePageEventFilterBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.home_page_event_filter,
                null,
                false
        );
        View eventView = homePageEventFilterBinding.getRoot();
        _filterEventDialog = new BottomSheetDialog(getActivity());
        _filterEventDialog.setContentView(eventView);

        String[] options = {"Option 1", "Option 2", "Option 3"};
        Spinner eventTypeSpinner = _filterEventDialog.findViewById(R.id.event_type_filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(_filterEventDialog.getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(adapter);

        _filterButton.setOnClickListener(v -> {
            _filterEventDialog.show();
        });

        ImageView filter = homePageEventFilterBinding.imageView;
        filter.setOnClickListener(v -> {
            _homeEventViewModel.filter();
            _filterEventDialog.dismiss();
        });
    }


    private void setEventSortDialog() {
        HomePageEventSortBinding homePageEventSortBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.home_page_event_sort,
                null,
                false
        );
        View eventView = homePageEventSortBinding.getRoot();
        _sortEventDialog = new BottomSheetDialog(getActivity());
        _sortEventDialog.setContentView(eventView);

        String[] options = {"Option 1", "Option 2", "Option 3"};
        Spinner eventSortCategory = _sortEventDialog.findViewById(R.id.event_sort_category);
        Spinner eventSortType = _sortEventDialog.findViewById(R.id.event_sort_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(_sortEventDialog.getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSortCategory.setAdapter(adapter);
        eventSortType.setAdapter(adapter);

        _sortButton.setOnClickListener(v -> {
            _sortEventDialog.show();

        });

        Button sortEvent = homePageEventSortBinding.eventSortButton;
        sortEvent.setOnClickListener(v -> {
            _homeEventViewModel.sort();
            _sortEventDialog.dismiss();
        });
    }

    public void setOfferingFilterDialog() {
        HomePageOfferingFilterBinding homePageOfferingFilterBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.home_page_offering_filter,
                null,
                false
        );
        View offeringView = homePageOfferingFilterBinding.getRoot();

        _filterOfferingDialog = new BottomSheetDialog(getActivity());
        _filterOfferingDialog.setContentView(offeringView);

        String[] options = {"Option 1", "Option 2", "Option 3"};
        Spinner offeringEventTypeFilter = _filterOfferingDialog.findViewById(R.id.offering_event_type_filter);
        Spinner offeringCategoryFilter = _filterOfferingDialog.findViewById(R.id.offering_category_filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(_filterOfferingDialog.getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offeringEventTypeFilter.setAdapter(adapter);
        offeringCategoryFilter.setAdapter(adapter);

        _filterButton.setOnClickListener(v -> {
            _filterOfferingDialog.show();
        });

        ImageView filter = homePageOfferingFilterBinding.offeringFilterButton;
        filter.setOnClickListener(v -> {
            _homeOfferingViewModel.filter();
            _filterOfferingDialog.dismiss();
        });
    }


    private void setOfferingSortDialog() {
        HomePageOfferingSortBinding homePageOfferingSortBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.home_page_offering_sort,
                null,
                false
        );
        View offeringView = homePageOfferingSortBinding.getRoot();
        _sortOfferingDialog = new BottomSheetDialog(getActivity());
        _sortOfferingDialog.setContentView(offeringView);

        String[] options = {"Option 1", "Option 2", "Option 3"};
        Spinner offeringSortCategory = _sortOfferingDialog.findViewById(R.id.offering_sort_category);
        Spinner offeringSortType = _sortOfferingDialog.findViewById(R.id.offering_sort_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(_sortOfferingDialog.getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offeringSortCategory.setAdapter(adapter);
        offeringSortType.setAdapter(adapter);

        _sortButton.setOnClickListener(v -> {
            _sortOfferingDialog.show();

        });

        Button sort = homePageOfferingSortBinding.offeringSortButton;
        sort.setOnClickListener(v -> {
            _homeOfferingViewModel.sort();
            _sortOfferingDialog.dismiss();
        });
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
        _filterButton = _binding.filterButton;
        _sortButton = _binding.sortButton;

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