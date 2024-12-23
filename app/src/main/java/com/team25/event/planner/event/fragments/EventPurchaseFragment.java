package com.team25.event.planner.event.fragments;

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
import com.team25.event.planner.event.model.EventTypePreviewDTO;
import com.team25.event.planner.event.model.OfferingCategoryPreviewDTO;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;
import com.team25.event.planner.offering.fragments.HomeOfferingsFragment;
import com.team25.event.planner.offering.fragments.TopOfferingsFragment;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class EventPurchaseFragment extends Fragment {

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
        FragmentTransition.toLeft(new HomeOfferingsFragment(_homeOfferingViewModel), requireActivity(), false, _binding.homeContainer.getId());

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
        FragmentTransition.toLeft(new HomeOfferingsFragment(_homeOfferingViewModel), requireActivity(), false, _binding.homeContainer.getId());

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

    public void setOfferingFilterDialog(String selectedType) {
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

        if(selectedType.equals(PRODUCTS)){
            _homeOfferingViewModel.selectedFilterId.setValue(R.id.products_radio_button);
        }
        else{
            _homeOfferingViewModel.selectedFilterId.setValue(R.id.services_radio_button);
        }

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

        if(Objects.equals(selectedType, PRODUCTS)){
            _homeOfferingViewModel.selectedFilterId.setValue(R.id.products_radio_button);
            filter.callOnClick();
        }
        else{
            _homeOfferingViewModel.selectedFilterId.setValue(R.id.services_radio_button);
            filter.callOnClick();
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