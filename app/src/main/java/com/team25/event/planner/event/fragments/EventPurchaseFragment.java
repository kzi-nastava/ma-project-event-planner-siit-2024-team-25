package com.team25.event.planner.event.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventPurchaseBinding;
import com.team25.event.planner.databinding.HomePageOfferingFilterBinding;
import com.team25.event.planner.databinding.HomePageOfferingSortBinding;
import com.team25.event.planner.event.model.EventType;
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
import java.util.Collection;
import java.util.Objects;


public class EventPurchaseFragment extends Fragment {

    private Long _eventId;
    private final String PRODUCTS = "PRODUCTS";
    private final String SERVICES = "SERVICES";

    private Button _productsButton;
    private Button _servicesButton;
    private Button _filterButton;
    private Button _sortButton;
    private BottomSheetDialog _filterOfferingDialog;
    private BottomSheetDialog _sortOfferingDialog;
    private HomeOfferingViewModel _homeOfferingViewModel;
    private HomeEventViewModel _homeEventViewModel;
    private FragmentEventPurchaseBinding _binding;
    private HomePageOfferingFilterBinding _homePageOfferingFilterBinding;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public EventPurchaseFragment() {

    }

    public static EventPurchaseFragment newInstance() {
        return new EventPurchaseFragment();
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
        _binding = FragmentEventPurchaseBinding.inflate(inflater, container, false);
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

    private void productsButtonClick() {
        FragmentTransition.toLeft(new ProductPurchaseListFragment(this._eventId), requireActivity(), false, _binding.homeContainer.getId());

        setOfferingFilterDialog(this.PRODUCTS);
        setOfferingSortDialog();

        changeButton(_productsButton, _servicesButton);
        _servicesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                servicesButtonClick();
            }
        });
    }


    private void servicesButtonClick() {
        FragmentTransition.toLeft(new HomeOfferingsFragment(_homeOfferingViewModel,this._eventId), requireActivity(), false, _binding.homeContainer.getId());

        setOfferingFilterDialog(this.SERVICES);
        setOfferingSortDialog();

        changeButton(_servicesButton, _productsButton);

        _productsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsButtonClick();
            }
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
    public void setOfferingFilterDialog(String selectedType) {
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
            if(v == R.id.services_radio_button){
                _homePageOfferingFilterBinding.serviceDateTime.setVisibility(View.VISIBLE);
            }else {
                _homePageOfferingFilterBinding.serviceDateTime.setVisibility(View.GONE);
            }
        });
        _homeOfferingViewModel.selectedFilterId.setValue(R.id.all_radio_button);
        _filterOfferingDialog = new BottomSheetDialog(getActivity());
        _filterOfferingDialog.setContentView(offeringView);



        Spinner offeringEventTypeFilter = _filterOfferingDialog.findViewById(R.id.offering_event_type_filter);
        Spinner offeringCategoryFilter = _filterOfferingDialog.findViewById(R.id.offering_category_filter);


        _homeEventViewModel.currentEventType.observe(getViewLifecycleOwner(),type ->{
            EventTypePreviewDTO eventTypePreviewDTO = new EventTypePreviewDTO();
            eventTypePreviewDTO.setId(type.getId());
            eventTypePreviewDTO.setName(type.getName());
            _homeOfferingViewModel.offeringFilterDTO.selectedEventType.setValue(eventTypePreviewDTO);
            ArrayList<EventTypePreviewDTO>list=new ArrayList<>();
            list.add(eventTypePreviewDTO);
            ArrayAdapter<EventTypePreviewDTO> adapter = new ArrayAdapter<>(_filterOfferingDialog.getContext(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            offeringEventTypeFilter.setAdapter(adapter);

            ArrayList<OfferingCategoryPreviewDTO> categoryPreviewDTOS = new ArrayList<>(type.getCategories());
            categoryPreviewDTOS.add(0,new OfferingCategoryPreviewDTO(null, null));
            ArrayAdapter<OfferingCategoryPreviewDTO> offeringCategoriesAdapter = new ArrayAdapter<>(_filterOfferingDialog.getContext(), android.R.layout.simple_spinner_item, categoryPreviewDTOS);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            offeringCategoryFilter.setAdapter(offeringCategoriesAdapter);


            _homeOfferingViewModel.getOfferings();
        });
        _homeEventViewModel.getEventTypeByEvent(this._eventId);

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

        offeringCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OfferingCategoryPreviewDTO selectedType = (OfferingCategoryPreviewDTO) parent.getItemAtPosition(position);
                _homeOfferingViewModel.offeringFilterDTO.selectedCategoryType.setValue(selectedType);
                if(selectedType.getId() != null){
                    _homeOfferingViewModel.getLeftMoneyForBudgetItem(_eventId, selectedType.getId());
                }
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


        ImageView filter = _homePageOfferingFilterBinding.offeringFilterButton;
        filter.setOnClickListener(v -> {
            setFilterCriteria(selectedType);
            _filterOfferingDialog.dismiss();
            _homeOfferingViewModel.getOfferings();
        });

        _homePageOfferingFilterBinding.imageRestart.setOnClickListener(v -> {
            _binding.searchView.setQuery("", false);
            _homeOfferingViewModel.restartFilter();
            setFilterCriteria(selectedType);
            _homeOfferingViewModel.getOfferings();
            _filterOfferingDialog.dismiss();
        });

        _filterButton.setOnClickListener(v -> {
            _filterOfferingDialog.show();
        });

        setFilterCriteria(selectedType);
    }

    private void setFilterCriteria(String selectedType){
        if(selectedType.equals(PRODUCTS)){
            _homeOfferingViewModel.selectedFilterId.setValue(R.id.products_radio_button);
        }
        else{
            _homeOfferingViewModel.selectedFilterId.setValue(R.id.services_radio_button);
        }
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

        Bundle arguments = getArguments();
        if (arguments != null) {
            _eventId = arguments.getLong(EventArgumentNames.ID_ARG);
        }
        _productsButton = _binding.productsButton;
        _servicesButton = _binding.servicesButton;
        _filterButton = _binding.filterButton;
        _sortButton = _binding.sortButton;

        _productsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsButtonClick();
            }
        });

        _servicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesButtonClick();
            }
        });

        _productsButton.callOnClick();
    }
}