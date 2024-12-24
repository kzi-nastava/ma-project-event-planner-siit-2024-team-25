package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentServiceDetailsBinding;
import com.team25.event.planner.event.fragments.EventFormFragment;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.viewmodel.EventFormViewModel;
import com.team25.event.planner.event.viewmodel.EventViewModel;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.viewModels.BookServiceViewModel;
import com.team25.event.planner.product_service.viewModels.ServiceCardsViewModel;
import com.team25.event.planner.product_service.viewModels.ServiceViewModel;

public class ServiceDetailsFragment extends Fragment {

    private EventViewModel _eventViewModel;
    private BookServiceViewModel _bookServiceViewModel;
    private ServiceViewModel _serviceViewModel;
    private FragmentServiceDetailsBinding _binding;

    private final String EVENT_ID = "EVENT_ID";
    private final String SERVICE_ID = "SERVICE_ID";
    private final String BOOK_SERVICE = "BOOK_SERVICE";

    private Long _eventId;
    private Long _serviceId;

    private final MutableLiveData<Event> _event = new MutableLiveData<>();
    public LiveData<Event> event = _event;
    private final MutableLiveData<Service> _service = new MutableLiveData<>();
    public  LiveData<Service> service = _service;

    private final MutableLiveData<Boolean> _bookService = new MutableLiveData<>();
    public  LiveData<Boolean> bookService = _bookService;

    public ServiceDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _eventId = getArguments().getLong(EVENT_ID);
            _serviceId = getArguments().getLong(SERVICE_ID);
            _bookService.setValue(getArguments().getBoolean(BOOK_SERVICE));
        }

        _eventViewModel = new EventViewModel();
        _serviceViewModel = new ServiceViewModel();
        _bookServiceViewModel = new BookServiceViewModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _eventViewModel.currentEvent.observe(getViewLifecycleOwner(), this._event::setValue);
        _eventViewModel.getEvent(_eventId);

        _serviceViewModel.currentService.observe(getViewLifecycleOwner(), this._service::setValue);
        _serviceViewModel.getService(_serviceId);


        _bookService.observe(getViewLifecycleOwner(), isBookService -> {
            if (isBookService != null) {
                _binding.bookService.setVisibility(isBookService ? View.VISIBLE : View.GONE);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        _binding = FragmentServiceDetailsBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }
}