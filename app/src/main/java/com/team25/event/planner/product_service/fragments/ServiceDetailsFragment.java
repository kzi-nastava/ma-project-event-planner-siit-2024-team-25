package com.team25.event.planner.product_service.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.DialogBookServiceBinding;
import com.team25.event.planner.databinding.FragmentServiceDetailsBinding;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.viewmodel.EventViewModel;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.viewModels.BookServiceViewModel;
import com.team25.event.planner.product_service.viewModels.ServiceViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ServiceDetailsFragment extends Fragment {

    private EventViewModel _eventViewModel;
    private BookServiceViewModel _bookServiceViewModel;
    private ServiceViewModel _serviceViewModel;
    private FragmentServiceDetailsBinding _binding;

    private BottomSheetDialog _bookServiceDialog;

    private DialogBookServiceBinding _dialogBookServiceBinding;

    private final String EVENT_ID = "EVENT_ID";
    private final String OFFERING_ID = "OFFERING_ID";
    private final String BOOK_SERVICE = "BOOK_SERVICE";

    private Long _eventId;
    private Long _serviceId;

    private final MutableLiveData<Event> _event = new MutableLiveData<>();
    public LiveData<Event> event = _event;
    private final MutableLiveData<Service> _service = new MutableLiveData<>();
    public  LiveData<Service> service = _service;

    private final MutableLiveData<Boolean> _bookService = new MutableLiveData<>();
    public  LiveData<Boolean> bookService = _bookService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ServiceDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _eventId = getArguments().getLong(EVENT_ID);
            _serviceId = getArguments().getLong(OFFERING_ID);
            _bookService.setValue(getArguments().getBoolean(BOOK_SERVICE));
        }

        _eventViewModel = new EventViewModel();
        _serviceViewModel = new ServiceViewModel();
        _bookServiceViewModel = new BookServiceViewModel();
        _dialogBookServiceBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_book_service,
                null,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _eventViewModel.currentEvent.observe(getViewLifecycleOwner(),event -> {
            this._event.setValue(event);
            setUpBookDialog();
        });

        _serviceViewModel.currentService.observe(getViewLifecycleOwner(), service -> {
            this._service.setValue(service);

            Double tootalPrice = this._service.getValue().getPrice();
            if(this._service.getValue().getDiscount() > 0){
                tootalPrice  = tootalPrice*(100-this._service.getValue().getDiscount())/100;
            }
            _bookServiceViewModel.purchase.getPrice().setValue(tootalPrice.toString());
        });
        _serviceViewModel.getService(_serviceId);

        _bookService.observe(getViewLifecycleOwner(), isBookService -> {
            if (isBookService != null) {
                _binding.bookService.setVisibility(isBookService ? View.VISIBLE : View.GONE);
                _eventViewModel.getEvent(_eventId);

                _binding.bookService.setOnClickListener(v -> {
                    _bookServiceDialog.show();
                });
            }
        });

    }

    private void setUpBookDialog(){
        View bookDialog = _dialogBookServiceBinding.getRoot();
        _dialogBookServiceBinding.setViewModel(_bookServiceViewModel);
        _dialogBookServiceBinding.setLifecycleOwner(this);
        _bookServiceDialog = new BottomSheetDialog(getActivity());
        _bookServiceDialog.setContentView(bookDialog);

        long minDate = this._event.getValue().getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long maxDate = this._event.getValue().getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        LocalTime minTime = LocalTime.of(event.getValue().getStartTime().getHour(), event.getValue().getStartTime().getMinute());
        LocalTime maxTime = LocalTime.of(event.getValue().getEndTime().getHour(), event.getValue().getEndTime().getMinute());

        _dialogBookServiceBinding.bookServiceButton.setOnClickListener(v -> {
            _bookServiceViewModel.bookService(this._eventId, this._serviceId);
        });

        _bookServiceViewModel.purchase.selectedEndTime.observe(getViewLifecycleOwner(), localTime -> {
            if(localTime.isAfter(_bookServiceViewModel.purchase.getSelectedStartTime().getValue())
            && !localTime.isAfter(maxTime)){
                _bookServiceViewModel.isServiceAvailable(this._serviceId);
            }else{
                _bookServiceViewModel.errorMessageFromServer.setValue("Invalid start time");
            }
        });

        _bookServiceViewModel.isAvailable.observe(getViewLifecycleOwner(), available -> {
            if(available){
                _bookServiceViewModel.errorMessageFromServer.setValue("Service is available for booking");
            }else {
                _bookServiceViewModel.errorMessageFromServer.setValue("Service isn't available for booking in this period");
            }
        });
    }

    private void showDatePicker(boolean isStartDate) {
        int eventStartYear = this._event.getValue().getStartDate().getYear();
        int eventStartMonth = this._event.getValue().getStartDate().getMonth().getValue()-1;
        int eventStartDay = this._event.getValue().getStartDate().getDayOfMonth();
        Calendar minDate = Calendar.getInstance();
        minDate.set(eventStartYear, eventStartMonth, eventStartDay);

        int eventEndYear = this._event.getValue().getStartDate().getYear();
        int eventEndMonth = this._event.getValue().getStartDate().getMonth().getValue()-1;
        int eventEndDay = this._event.getValue().getStartDate().getDayOfMonth();
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(eventEndYear, eventEndMonth, eventEndDay);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    if (isStartDate) {
                        _bookServiceViewModel.purchase.getSelectedStartDate().setValue(selectedDate);
                        _dialogBookServiceBinding.startDateInput.setText(selectedDate.format(dateFormatter));
                        showTimePicker(true);
                    } else {
                        _bookServiceViewModel.purchase.getSelectedEndDate().setValue(selectedDate);
                        _dialogBookServiceBinding.endDateInput.setText(selectedDate.format(dateFormatter));
                        showTimePicker(false);
                    }
                },
                eventStartYear,
                eventStartMonth,
                eventStartDay
        );
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePicker(boolean isStartTime) {
        int eventStartHour = this._event.getValue().getStartTime().getHour();
        int eventStartMinute = this._event.getValue().getStartTime().getMinute();
        if(_bookServiceViewModel.purchase.getSelectedEndTime().getValue() != null){
            eventStartHour = _bookServiceViewModel.purchase.getSelectedEndTime().getValue().getHour();
            eventStartMinute =_bookServiceViewModel.purchase.getSelectedEndTime().getValue().getMinute();
        }


        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                    if (isStartTime) {
                        _bookServiceViewModel.purchase.getSelectedStartTime().setValue(selectedTime);
                        _dialogBookServiceBinding.startTimeInput.setText(selectedTime.format(timeFormatter));
                    } else {
                        _bookServiceViewModel.purchase.getSelectedEndTime().setValue(selectedTime);
                        _dialogBookServiceBinding.endTimeInput.setText(selectedTime.format(timeFormatter));
                    }
                },
                eventStartHour,
                eventStartMinute,
                true
        );

        timePickerDialog.show();
    }

    private void setupDateTimePickers() {
        _dialogBookServiceBinding.startDateInput.setOnClickListener(v -> showDatePicker(true));
        _dialogBookServiceBinding.endDateInput.setOnClickListener(v -> showDatePicker(false));
        _dialogBookServiceBinding.startTimeInput.setOnClickListener(v -> showTimePicker(true));
        _dialogBookServiceBinding.endTimeInput.setOnClickListener(v -> showTimePicker(false));
    }

    private void setupObservable(){
        _bookServiceViewModel.purchase.getSelectedStartTime().observe(getViewLifecycleOwner(), localTime -> {
        });

        _dialogBookServiceBinding.bookServiceButton.setOnClickListener(v -> {
            _bookServiceViewModel.bookService(this._eventId, this._serviceId);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        _binding = FragmentServiceDetailsBinding.inflate(inflater, container, false);

        setupDateTimePickers();
        setupObservable();
        return _binding.getRoot();
    }
}