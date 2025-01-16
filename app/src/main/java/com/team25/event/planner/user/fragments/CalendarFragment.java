package com.team25.event.planner.user.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentCalendarBinding;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.user.adapters.ServiceReservationListAdapter;
import com.team25.event.planner.user.model.CalendarEvent;
import com.team25.event.planner.user.model.UserRole;
import com.team25.event.planner.user.viewmodels.CalendarViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kotlin.Unit;
import lombok.Getter;

public class CalendarFragment extends Fragment {
    public static final String USER_ID_ARG = "USER_ID";
    public static final String USER_ROLE_ARG = "USER_ROLE";

    private FragmentCalendarBinding binding;
    private CalendarViewModel viewModel;
    private NavController navController;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private UserRole userRole;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);

        if (getArguments() != null) {
            long userId = getArguments().getLong(USER_ID_ARG);
            if (userId != -1L) {
                viewModel.setUserId(userId);
            }
            userRole = UserRole.valueOf(getArguments().getString(USER_ROLE_ARG, ""));
        }

        setupCalendar();
        setupBottomSheet();
        setupObservers();
        setupListeners();

        return binding.getRoot();
    }

    private void setupCalendar() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100);
        YearMonth endMonth = currentMonth.plusMonths(100);

        binding.calendarView.setup(startMonth, endMonth, DayOfWeek.values()[0]);
        binding.calendarView.scrollToMonth(currentMonth);

        binding.calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                container.getTextView().setText(String.valueOf(day.getDate().getDayOfMonth()));

                // Clear previous markers
                container.getMarkersContainer().removeAllViews();

                // Get events for this day
                List<CalendarEvent> events = viewModel.calendarEvents.getValue() != null ?
                        viewModel.calendarEvents.getValue().stream()
                                .filter(event -> !day.getDate().isBefore(event.getStartDate()) &&
                                        !day.getDate().isAfter(event.getEndDate()))
                                .collect(Collectors.toList()) :
                        Collections.emptyList();

                // Add markers for events
                for (CalendarEvent event : events) {
                    View marker = new View(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            dpToPx(8),
                            dpToPx(8)
                    );
                    params.setMargins(dpToPx(2), 0, dpToPx(2), 0);
                    marker.setLayoutParams(params);

                    marker.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.circle_background));

                    int colorRes;
                    switch (event.getEventType()) {
                        case MY_EVENT:
                            colorRes = R.color.organizer_event_color;
                            break;
                        case RESERVATION:
                            colorRes = R.color.service_reservations_color;
                            break;
                        case EVENT:
                        default:
                            colorRes = R.color.attending_event_color;
                    }
                    marker.setBackgroundTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(requireContext(), colorRes)));

                    container.getMarkersContainer().addView(marker);
                }

                container.getView().setOnClickListener(v -> loadEventsForDate(day.getDate()));
            }
        });

        // Set up month header
        binding.calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @NonNull
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthViewContainer container, @NonNull CalendarMonth calendarMonth) {
                container.getTextView().setText(calendarMonth.getYearMonth().format(
                        DateTimeFormatter.ofPattern("MMMM yyyy")));
            }
        });
    }

    private void setupBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        binding.myEventsHeader.setVisibility(
                userRole == UserRole.EVENT_ORGANIZER ? View.VISIBLE : View.GONE);
        binding.myEventsList.setVisibility(
                userRole == UserRole.EVENT_ORGANIZER ? View.VISIBLE : View.GONE);
        binding.reservationsHeader.setVisibility(
                userRole == UserRole.OWNER ? View.VISIBLE : View.GONE);
        binding.reservationsList.setVisibility(
                userRole == UserRole.OWNER ? View.VISIBLE : View.GONE);
    }

    private void loadEventsForDate(LocalDate date) {
        viewModel.loadAttendingEvents(date);

        if (userRole == UserRole.EVENT_ORGANIZER) {
            viewModel.loadMyEvents(date);
        }

        if (userRole == UserRole.OWNER) {
            viewModel.loadServiceReservations(date);
        }

        binding.selectedDateText.setText(
                date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void setupObservers() {
        viewModel.calendarEvents.observe(getViewLifecycleOwner(), events ->
                binding.calendarView.notifyCalendarChanged());

        // Load initial calendar events
        YearMonth currentMonth = YearMonth.now();
        viewModel.loadCalendarEvents(
                currentMonth.atDay(1),
                currentMonth.atEndOfMonth()
        );

        viewModel.serverError.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.attendingEvents.observe(getViewLifecycleOwner(), events -> {
            HomeEventListAdapter adapter = new HomeEventListAdapter(
                    getContext(), events, navController, this::toggleFavoriteEvent
            );
            binding.attendingEventsList.setAdapter(adapter);
        });

        viewModel.myEvents.observe(getViewLifecycleOwner(), events -> {
            HomeEventListAdapter adapter = new HomeEventListAdapter(
                    getContext(), events, navController, this::toggleFavoriteEvent
            );
            binding.myEventsList.setAdapter(adapter);
        });

        viewModel.serviceReservations.observe(getViewLifecycleOwner(), purchases -> {
            ServiceReservationListAdapter adapter = new ServiceReservationListAdapter(
                    getContext(), purchases
            );
            binding.reservationsList.setAdapter(adapter);
        });
    }

    private void setupListeners() {
        binding.calendarView.setMonthScrollListener(calendarMonth -> {
            viewModel.loadCalendarEvents(
                    calendarMonth.getYearMonth().atDay(1),
                    calendarMonth.getYearMonth().atEndOfMonth()
            );
            return Unit.INSTANCE;
        });
    }

    private void toggleFavoriteEvent(EventCard event) {
    }

    @Getter
    private static class DayViewContainer extends ViewContainer {
        private final TextView textView;
        private final LinearLayout markersContainer;

        public DayViewContainer(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.calendarDayText);
            markersContainer = view.findViewById(R.id.eventMarkersContainer);
        }
    }

    @Getter
    private static class MonthViewContainer extends ViewContainer {
        private final TextView textView;

        public MonthViewContainer(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.headerMonthText);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}