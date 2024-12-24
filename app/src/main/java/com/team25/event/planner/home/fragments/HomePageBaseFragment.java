package com.team25.event.planner.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.ViewSwitcher;

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
import com.team25.event.planner.event.model.EventTypePreviewDTO;
import com.team25.event.planner.event.model.OfferingCategoryPreviewDTO;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;
import com.team25.event.planner.offering.fragments.HomeOfferingsFragment;
import com.team25.event.planner.offering.fragments.TopOfferingsFragment;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;


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
        _homeEventViewModel = new HomeEventViewModel();
        _homeOfferingViewModel = new HomeOfferingViewModel();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentHomePageBaseBinding.inflate(inflater, container, false);
        _binding.setLifecycleOwner(this);
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
        FragmentTransition.toLeft(new HomeOfferingsFragment(_homeOfferingViewModel,null), requireActivity(), false, _binding.homeContainer.getId());

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
        homePageEventFilterBinding.setViewModel(_homeEventViewModel);
        _filterEventDialog = new BottomSheetDialog(getActivity());
        _filterEventDialog.setContentView(eventView);

        Spinner eventTypeSpinner = _filterEventDialog.findViewById(R.id.event_type_filter);

        _homeEventViewModel.allEventTypes.observe(getViewLifecycleOwner(),types ->{
            ArrayAdapter<EventTypePreviewDTO> adapter = new ArrayAdapter<>(_filterEventDialog.getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(types));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eventTypeSpinner.setAdapter(adapter);
        });
        _homeEventViewModel.getEventTypes();

        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventTypePreviewDTO selectedType = (EventTypePreviewDTO) parent.getItemAtPosition(position);
                _homeEventViewModel.eventFilterDTO.selectedEventType.setValue(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Calendar calendar = Calendar.getInstance();
        long minDate = calendar.getTimeInMillis();
        homePageEventFilterBinding.eventStartDate.setMinDate(minDate);
        homePageEventFilterBinding.eventEndDate.setMinDate(minDate);

        homePageEventFilterBinding.eventStartDate.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            LocalDate localDate = LocalDate.of(year, monthOfYear+1, dayOfMonth);
            _homeEventViewModel.eventFilterDTO.selectedStartDate.setValue(localDate);
        });

        homePageEventFilterBinding.eventEndDate.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            LocalDate localDate = LocalDate.of(year, monthOfYear+1, dayOfMonth);
            _homeEventViewModel.eventFilterDTO.selectedEndDate.setValue(localDate);
        });

        homePageEventFilterBinding.eventStartTime.setIs24HourView(true);
        homePageEventFilterBinding.eventStartTime.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
            _homeEventViewModel.eventFilterDTO.selectedStartTime.setValue(selectedTime);
        });

        homePageEventFilterBinding.eventEndTime.setIs24HourView(true);
        homePageEventFilterBinding.eventEndTime.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
            _homeEventViewModel.eventFilterDTO.selectedEndTime.setValue(selectedTime);
        });

        _binding.searchView.setQueryHint("Event name");
        _binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _homeEventViewModel.getAllEvents();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _homeEventViewModel.eventFilterDTO.name.setValue(newText);
                if(newText.isEmpty()){
                    _homeEventViewModel.getAllEvents();
                }
                return false;
            }
        });


        _filterButton.setOnClickListener(v -> {
            _filterEventDialog.show();
        });

        ImageView filter = homePageEventFilterBinding.imageView;
        filter.setOnClickListener(v -> {
            _homeEventViewModel.getAllEvents();
            _filterEventDialog.dismiss();
        });

        homePageEventFilterBinding.imageRestart.setOnClickListener(v -> {
            _binding.searchView.setQuery("", false);
            _homeEventViewModel.restartFilter();
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

        Spinner eventSortCategory = _sortEventDialog.findViewById(R.id.event_sort_category);
        Spinner eventSortType = _sortEventDialog.findViewById(R.id.event_sort_type);
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<>(_sortEventDialog.getContext(), android.R.layout.simple_spinner_item, _homeEventViewModel.eventFilterDTO.sortByMap.keySet().toArray(new String[0]));
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSortCategory.setAdapter(sortByAdapter);

        ArrayAdapter<String> sortCriteriaAdapter = new ArrayAdapter<>(_sortEventDialog.getContext(), android.R.layout.simple_spinner_item, _homeEventViewModel.eventFilterDTO.sortCriteriaMap.keySet().toArray(new String[0]));
        sortCriteriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSortType.setAdapter(sortCriteriaAdapter);

        _sortButton.setOnClickListener(v -> {
            _sortEventDialog.show();
        });

        homePageEventSortBinding.eventSortCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _homeEventViewModel.eventFilterDTO.selectedSortBy.setValue((String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        homePageEventSortBinding.eventSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _homeEventViewModel.eventFilterDTO.selectedSortCriteria.setValue((String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button sortEvent = homePageEventSortBinding.eventSortButton;
        sortEvent.setOnClickListener(v -> {
            _homeEventViewModel.getAllEvents();
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
        homePageOfferingFilterBinding.setViewModel(_homeOfferingViewModel);
        _homeOfferingViewModel.selectedFilterId.observe(getViewLifecycleOwner(), v -> {
            if(v == R.id.services_radio_button){
                homePageOfferingFilterBinding.serviceDateTime.setVisibility(View.VISIBLE);
            }else {
                homePageOfferingFilterBinding.serviceDateTime.setVisibility(View.GONE);
            }
        });
        _homeOfferingViewModel.selectedFilterId.setValue(R.id.all_radio_button);
        _filterOfferingDialog = new BottomSheetDialog(getActivity());
        _filterOfferingDialog.setContentView(offeringView);



        Spinner offeringEventTypeFilter = _filterOfferingDialog.findViewById(R.id.offering_event_type_filter);
        Spinner offeringCategoryFilter = _filterOfferingDialog.findViewById(R.id.offering_category_filter);


        _homeEventViewModel.allEventTypes.observe(getViewLifecycleOwner(),types ->{
            ArrayAdapter<EventTypePreviewDTO> adapter = new ArrayAdapter<>(_filterOfferingDialog.getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(types));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            offeringEventTypeFilter.setAdapter(adapter);
        });
        _homeEventViewModel.getEventTypes();

        offeringEventTypeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventTypePreviewDTO selectedType = (EventTypePreviewDTO) parent.getItemAtPosition(position);
                _homeOfferingViewModel.offeringFilterDTO.selectedEventType.setValue(selectedType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        _homeOfferingViewModel.allOfferingCategories.observe(getViewLifecycleOwner(),types ->{
            ArrayAdapter<OfferingCategoryPreviewDTO> adapter = new ArrayAdapter<>(_filterOfferingDialog.getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(types));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            offeringCategoryFilter.setAdapter(adapter);
        });
        _homeOfferingViewModel.getAllOfferingCategories();

        offeringCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OfferingCategoryPreviewDTO selectedType = (OfferingCategoryPreviewDTO) parent.getItemAtPosition(position);
                _homeOfferingViewModel.offeringFilterDTO.selectedCategoryType.setValue(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        _binding.searchView.setQueryHint("Name");
        _binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _homeOfferingViewModel.getAllOfferings();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _homeOfferingViewModel.offeringFilterDTO.name.setValue(newText);
                if(newText.isEmpty()){
                    _homeOfferingViewModel.getAllOfferings();
                }
                return false;
            }
        });

        Calendar calendar = Calendar.getInstance();
        long minDate = calendar.getTimeInMillis();
        homePageOfferingFilterBinding.eventStartDate.setMinDate(minDate);
        homePageOfferingFilterBinding.eventEndDate.setMinDate(minDate);

        homePageOfferingFilterBinding.eventStartDate.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            LocalDate localDate = LocalDate.of(year, monthOfYear+1, dayOfMonth);
            _homeOfferingViewModel.offeringFilterDTO.selectedStartDate.setValue(localDate);
        });

        homePageOfferingFilterBinding.eventEndDate.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            LocalDate localDate = LocalDate.of(year, monthOfYear+1, dayOfMonth);
            _homeOfferingViewModel.offeringFilterDTO.selectedEndDate.setValue(localDate);
        });

        homePageOfferingFilterBinding.eventStartTime.setIs24HourView(true);
        homePageOfferingFilterBinding.eventStartTime.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
            _homeOfferingViewModel.offeringFilterDTO.selectedStartTime.setValue(selectedTime);
        });

        homePageOfferingFilterBinding.eventEndTime.setIs24HourView(true);
        homePageOfferingFilterBinding.eventEndTime.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
            _homeOfferingViewModel.offeringFilterDTO.selectedEndTime.setValue(selectedTime);
        });

        this._homeOfferingViewModel.selectedFilterId.setValue(R.id.all_radio_button);
        this._homeOfferingViewModel.getOfferings();

        ImageView filter = homePageOfferingFilterBinding.offeringFilterButton;
        filter.setOnClickListener(v -> {
            _filterOfferingDialog.dismiss();
            _homeOfferingViewModel.getOfferings();
        });

        homePageOfferingFilterBinding.imageRestart.setOnClickListener(v -> {
            _binding.searchView.setQuery("", false);
            _homeOfferingViewModel.restartFilter();
            homePageOfferingFilterBinding.radioButtons.clearCheck();
            _filterOfferingDialog.dismiss();
        });

        _filterButton.setOnClickListener(v -> {
            _filterOfferingDialog.show();
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

        Spinner offeringSortCategory = _sortOfferingDialog.findViewById(R.id.offering_sort_category);
        Spinner offeringSortType = _sortOfferingDialog.findViewById(R.id.offering_sort_type);
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<>(_sortOfferingDialog.getContext(), android.R.layout.simple_spinner_item, _homeOfferingViewModel.offeringFilterDTO.sortByMap.keySet().toArray(new String[0]));
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offeringSortCategory.setAdapter(sortByAdapter);

        ArrayAdapter<String> sortCriteriaAdapter = new ArrayAdapter<>(_sortOfferingDialog.getContext(), android.R.layout.simple_spinner_item, _homeOfferingViewModel.offeringFilterDTO.sortCriteriaMap.keySet().toArray(new String[0]));
        sortCriteriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offeringSortType.setAdapter(sortCriteriaAdapter);

        _sortButton.setOnClickListener(v -> {
            _sortOfferingDialog.show();

        });


        homePageOfferingSortBinding.offeringSortCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _homeOfferingViewModel.offeringFilterDTO.selectedSortBy.setValue((String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        homePageOfferingSortBinding.offeringSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _homeOfferingViewModel.offeringFilterDTO.selectedSortCriteria.setValue((String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Button sort = homePageOfferingSortBinding.offeringSortButton;
        sort.setOnClickListener(v -> {
            _homeOfferingViewModel.getOfferings();
            _sortOfferingDialog.dismiss();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(_binding.homeTopEvents.getId(), new TopEventsFragment(_homeEventViewModel))
                    .replace(_binding.homeTopOffers.getId(), new TopOfferingsFragment(_homeOfferingViewModel))
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