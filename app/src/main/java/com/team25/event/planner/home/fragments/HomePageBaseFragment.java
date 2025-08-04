package com.team25.event.planner.home.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
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
    private HomePageEventFilterBinding _homePageEventFilterBinding;
    private HomePageOfferingFilterBinding _homePageOfferingFilterBinding;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

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

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                _homeEventViewModel.setUserId(null);
            } else {
                _homeEventViewModel.setUserId(user.getId());
            }
        });

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
        FragmentTransition.toLeft(new HomeOfferingsFragment(_homeOfferingViewModel, null), requireActivity(), false, _binding.homeContainer.getId());

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

    private void setupEventFilterDateTimePickers() {
        _homePageEventFilterBinding.startDateInput.setOnClickListener(v -> showEventFilterDatePicker(true));
        _homePageEventFilterBinding.endDateInput.setOnClickListener(v -> showEventFilterDatePicker(false));
        _homePageEventFilterBinding.startTimeInput.setOnClickListener(v -> showEventFilterTimePicker(true));
        _homePageEventFilterBinding.endTimeInput.setOnClickListener(v -> showEventFilterTimePicker(false));
    }
    private void showEventFilterDatePicker(boolean isStartDate) {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();

        if(_homeEventViewModel.eventFilterDTO.getSelectedStartDate().getValue() != null && isStartDate){
            year = _homeEventViewModel.eventFilterDTO.getSelectedStartDate().getValue().getYear();
            month = _homeEventViewModel.eventFilterDTO.getSelectedStartDate().getValue().getMonthValue();
            day = _homeEventViewModel.eventFilterDTO.getSelectedStartDate().getValue().getDayOfMonth();
        }else if(_homeEventViewModel.eventFilterDTO.getSelectedEndDate().getValue() != null && !isStartDate){
            year = _homeEventViewModel.eventFilterDTO.getSelectedEndDate().getValue().getYear();
            month = _homeEventViewModel.eventFilterDTO.getSelectedEndDate().getValue().getMonthValue();
            day = _homeEventViewModel.eventFilterDTO.getSelectedEndDate().getValue().getDayOfMonth();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (viewObj, selectedYear, selectedMonth, selectedDay) -> {
                    LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
                    if (isStartDate) {
                        _homeEventViewModel.eventFilterDTO.getSelectedStartDate().setValue(selectedDate);
                        _homePageEventFilterBinding.startDateInput.setText(selectedDate.format(dateFormatter));
                        showEventFilterTimePicker(true);
                    } else {
                        _homeEventViewModel.eventFilterDTO.getSelectedEndDate().setValue(selectedDate);
                        _homePageEventFilterBinding.endDateInput.setText(selectedDate.format(dateFormatter));
                        showEventFilterTimePicker(false);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }
    private void showEventFilterTimePicker(boolean isStartTime) {
        int currentHour = LocalTime.now().getHour();
        int currentMinute = LocalTime.now().getMinute();

        if(_homeEventViewModel.eventFilterDTO.getSelectedStartTime().getValue() != null && isStartTime){
            currentHour = _homeEventViewModel.eventFilterDTO.getSelectedStartTime().getValue().getHour();
            currentMinute = _homeEventViewModel.eventFilterDTO.getSelectedStartTime().getValue().getMinute();
        }else if(_homeEventViewModel.eventFilterDTO.getSelectedEndTime().getValue() != null && !isStartTime){
            currentHour = _homeEventViewModel.eventFilterDTO.getSelectedEndTime().getValue().getHour();
            currentMinute = _homeEventViewModel.eventFilterDTO.getSelectedEndTime().getValue().getMinute();
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                    if (isStartTime) {
                        _homeEventViewModel.eventFilterDTO.getSelectedStartTime().setValue(selectedTime);
                        _homePageEventFilterBinding.startTimeInput.setText(selectedTime.format(timeFormatter));
                    } else {
                        _homeEventViewModel.eventFilterDTO.getSelectedEndTime().setValue(selectedTime);
                        _homePageEventFilterBinding.endTimeInput.setText(selectedTime.format(timeFormatter));
                    }
                },
                currentHour,
                currentMinute,
                true
        );

        timePickerDialog.show();
    }
    public void setEventFilterDialog() {
        _homePageEventFilterBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.home_page_event_filter,
                null,
                false
        );

        View eventView = _homePageEventFilterBinding.getRoot();
        _homePageEventFilterBinding.setViewModel(_homeEventViewModel);
        _filterEventDialog = new BottomSheetDialog(getActivity());
        _filterEventDialog.setContentView(eventView);

        setupEventFilterDateTimePickers();

        Spinner eventTypeSpinner = _filterEventDialog.findViewById(R.id.event_type_filter);

        _homeEventViewModel.allEventTypes.observe(getViewLifecycleOwner(), types -> {
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
                if (newText.isEmpty()) {
                    _homeEventViewModel.getAllEvents();
                }
                return false;
            }
        });


        _filterButton.setOnClickListener(v -> {
            _filterEventDialog.show();
        });

        ImageView filter = _homePageEventFilterBinding.imageView;
        filter.setOnClickListener(v -> {
            _homeEventViewModel.getAllEvents();
            _filterEventDialog.dismiss();
        });

        _homePageEventFilterBinding.imageRestart.setOnClickListener(v -> {
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
                _homeEventViewModel.eventFilterDTO.selectedSortBy.setValue((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        homePageEventSortBinding.eventSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _homeEventViewModel.eventFilterDTO.selectedSortCriteria.setValue((String) parent.getItemAtPosition(position));
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
    private void setupOfferingFilterDateTimePickers() {
        _homePageOfferingFilterBinding.startDateInput.setOnClickListener(v -> showOfferingFilterDatePicker(true));
        _homePageOfferingFilterBinding.endDateInput.setOnClickListener(v -> showOfferingFilterDatePicker(false));
        _homePageOfferingFilterBinding.startTimeInput.setOnClickListener(v -> showOfferingFilterTimePicker(true));
        _homePageOfferingFilterBinding.endTimeInput.setOnClickListener(v -> showOfferingFilterTimePicker(false));
    }
    private void showOfferingFilterDatePicker(boolean isStartDate) {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();

        if(_homeOfferingViewModel.offeringFilterDTO.getSelectedStartDate().getValue() != null && isStartDate){
            year = _homeOfferingViewModel.offeringFilterDTO.getSelectedStartDate().getValue().getYear();
            month = _homeOfferingViewModel.offeringFilterDTO.getSelectedStartDate().getValue().getMonthValue();
            day = _homeOfferingViewModel.offeringFilterDTO.getSelectedStartDate().getValue().getDayOfMonth();
        }else if(_homeOfferingViewModel.offeringFilterDTO.getSelectedEndDate().getValue() != null && !isStartDate){
            year = _homeOfferingViewModel.offeringFilterDTO.getSelectedEndDate().getValue().getYear();
            month = _homeOfferingViewModel.offeringFilterDTO.getSelectedEndDate().getValue().getMonthValue();
            day = _homeOfferingViewModel.offeringFilterDTO.getSelectedEndDate().getValue().getDayOfMonth();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (viewObj, selectedYear, selectedMonth, selectedDay) -> {
                    LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
                    if (isStartDate) {
                        _homeOfferingViewModel.offeringFilterDTO.getSelectedStartDate().setValue(selectedDate);
                        _homePageOfferingFilterBinding.startDateInput.setText(selectedDate.format(dateFormatter));
                        showOfferingFilterTimePicker(true);
                    } else {
                        _homeOfferingViewModel.offeringFilterDTO.getSelectedEndDate().setValue(selectedDate);
                        _homePageOfferingFilterBinding.endDateInput.setText(selectedDate.format(dateFormatter));
                        showOfferingFilterTimePicker(false);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }
    private void showOfferingFilterTimePicker(boolean isStartTime) {
        int currentHour = LocalTime.now().getHour();
        int currentMinute = LocalTime.now().getMinute();

        if(_homeOfferingViewModel.offeringFilterDTO.getSelectedStartTime().getValue() != null && isStartTime){
            currentHour = _homeOfferingViewModel.offeringFilterDTO.getSelectedStartTime().getValue().getHour();
            currentMinute = _homeOfferingViewModel.offeringFilterDTO.getSelectedStartTime().getValue().getMinute();
        }else if(_homeOfferingViewModel.offeringFilterDTO.getSelectedEndTime().getValue() != null && !isStartTime){
            currentHour = _homeOfferingViewModel.offeringFilterDTO.getSelectedEndTime().getValue().getHour();
            currentMinute = _homeOfferingViewModel.offeringFilterDTO.getSelectedEndTime().getValue().getMinute();
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                    if (isStartTime) {
                        _homeOfferingViewModel.offeringFilterDTO.getSelectedStartTime().setValue(selectedTime);
                        _homePageOfferingFilterBinding.startTimeInput.setText(selectedTime.format(timeFormatter));
                    } else {
                        _homeOfferingViewModel.offeringFilterDTO.getSelectedEndTime().setValue(selectedTime);
                        _homePageOfferingFilterBinding.endTimeInput.setText(selectedTime.format(timeFormatter));
                    }
                },
                currentHour,
                currentMinute,
                true
        );

        timePickerDialog.show();
    }
    public void setOfferingFilterDialog() {
        _homePageOfferingFilterBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.home_page_offering_filter,
                null,
                false
        );
        View offeringView = _homePageOfferingFilterBinding.getRoot();
        _homePageOfferingFilterBinding.setViewModel(_homeOfferingViewModel);

        setupOfferingFilterDateTimePickers();

        _homeOfferingViewModel.selectedFilterId.observe(getViewLifecycleOwner(), v -> {
            if (v == R.id.services_radio_button) {
                _homePageOfferingFilterBinding.serviceDateTime.setVisibility(View.VISIBLE);
            } else {
                _homePageOfferingFilterBinding.serviceDateTime.setVisibility(View.GONE);
            }
        });
        _homeOfferingViewModel.selectedFilterId.setValue(R.id.all_radio_button);
        _filterOfferingDialog = new BottomSheetDialog(getActivity());
        _filterOfferingDialog.setContentView(offeringView);


        Spinner offeringEventTypeFilter = _filterOfferingDialog.findViewById(R.id.offering_event_type_filter);
        Spinner offeringCategoryFilter = _filterOfferingDialog.findViewById(R.id.offering_category_filter);


        _homeEventViewModel.allEventTypes.observe(getViewLifecycleOwner(), types -> {
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
        _homeOfferingViewModel.allOfferingCategories.observe(getViewLifecycleOwner(), types -> {
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
                if (newText.isEmpty()) {
                    _homeOfferingViewModel.getAllOfferings();
                }
                return false;
            }
        });

        this._homeOfferingViewModel.selectedFilterId.setValue(R.id.all_radio_button);
        this._homeOfferingViewModel.getOfferings();

        ImageView filter = _homePageOfferingFilterBinding.offeringFilterButton;
        filter.setOnClickListener(v -> {
            _filterOfferingDialog.dismiss();
            _homeOfferingViewModel.getOfferings();
        });

        _homePageOfferingFilterBinding.imageRestart.setOnClickListener(v -> {
            _binding.searchView.setQuery("", false);
            _homeOfferingViewModel.restartFilter();
            _homePageOfferingFilterBinding.radioButtons.clearCheck();
            _homeOfferingViewModel.getAllOfferings();
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
                _homeOfferingViewModel.offeringFilterDTO.selectedSortBy.setValue((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        homePageOfferingSortBinding.offeringSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _homeOfferingViewModel.offeringFilterDTO.selectedSortCriteria.setValue((String) parent.getItemAtPosition(position));
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