package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventStatsBinding;
import com.team25.event.planner.event.adapters.AttendeeListAdapter;
import com.team25.event.planner.event.model.ReviewStats;
import com.team25.event.planner.event.viewmodel.EventStatsViewModel;
import com.team25.event.planner.user.fragments.PublicProfileFragment;

import java.util.ArrayList;

public class EventStatsFragment extends Fragment {
    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 5;

    private FragmentEventStatsBinding binding;
    private EventStatsViewModel viewModel;
    private NavController navController;
    private AttendeeListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_stats, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        viewModel = new ViewModelProvider(this).get(EventStatsViewModel.class);
        binding.setViewModel(viewModel);

        parseArguments();
        setupAttendeeList();
        setupObservers();

        return binding.getRoot();
    }

    private void parseArguments() {
        Bundle args = getArguments();
        if (args == null) return;

        long eventId = args.getLong(EventArgumentNames.ID_ARG);
        if (eventId != -1) {
            viewModel.setEventId(eventId);
        } else {
            viewModel.setEventId(null);
        }

        String eventName = args.getString(EventArgumentNames.NAME_ARG);
        viewModel.setEventName(eventName);
    }

    private void setupAttendeeList() {
        adapter = new AttendeeListAdapter(new ArrayList<>(), attendee -> {
            Bundle bundle = new Bundle();
            bundle.putLong(PublicProfileFragment.USER_ID_ARG, attendee.getUserId());
            navController.navigate(R.id.action_eventStatsFragment_to_publicProfileFragment, bundle);
        });
        binding.rvAttendees.setAdapter(adapter);

        binding.rvAttendees.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!viewModel.isLoading() && !viewModel.isEndReached()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        viewModel.loadAttendeesNextPage();
                    }
                }
            }
        });
    }

    private void setupObservers() {
        viewModel.reviewStats.observe(getViewLifecycleOwner(), this::setupRatingChart);

        viewModel.attendees.observe(getViewLifecycleOwner(), adapter::addAttendees);

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.loadingSpinner.setVisibility(View.VISIBLE);
            } else {
                binding.loadingSpinner.setVisibility(View.GONE);
            }
        });

        viewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRatingChart(ReviewStats stats) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = MIN_RATING; i <= MAX_RATING; i++) {
            Integer count = stats.getReviewCounts().get(i);
            float value = count != null ? count.floatValue() : 0f;
            entries.add(new BarEntry(i, value));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Ratings");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.primary));

        BarData data = new BarData(dataSet);

        BarChart chart = binding.ratingChart;
        chart.setData(data);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(6);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);

        chart.animateY(1000);
        chart.invalidate();
    }
}