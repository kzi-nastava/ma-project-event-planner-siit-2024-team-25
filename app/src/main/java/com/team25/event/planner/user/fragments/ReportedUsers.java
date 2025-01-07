package com.team25.event.planner.user.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.adapters.NotificationsListAdapter;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.communication.model.NotificationCategory;
import com.team25.event.planner.communication.viewmodel.MyNotificationViewModel;
import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentNotificationBinding;
import com.team25.event.planner.databinding.FragmentReportedUsersBinding;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.user.adapters.ReportListAdapter;
import com.team25.event.planner.user.model.UserReportResponse;
import com.team25.event.planner.user.model.UserRole;
import com.team25.event.planner.user.viewmodels.SuspensionViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ReportedUsers extends Fragment {
    private FragmentReportedUsersBinding _binding;
    private ReportListAdapter _adapter;
    private SuspensionViewModel _suspensionViewModel;

    private boolean _isUpdate;

    public ReportedUsers() {
        _isUpdate = false;
    }


    public static ReportedUsers newInstance() {
        ReportedUsers fragment = new ReportedUsers();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentReportedUsersBinding.inflate(inflater, container, false);
        _binding.setLifecycleOwner(getViewLifecycleOwner());
        _suspensionViewModel = new ViewModelProvider(this).get(SuspensionViewModel.class);

        setupNotificationList();
        setupObservers();

        _suspensionViewModel.loadNextPage();

        return _binding.getRoot();
    }

    private void setupNotificationList() {
        _adapter = new ReportListAdapter(new ArrayList<>(), new ReportListAdapter.OnItemClickListener() {
            @Override
            public void suspendUser(UserReportResponse report) {

            }

            @Override
            public void markReportAsViewed(UserReportResponse report, int position) {
                _suspensionViewModel.currentReport.observe(getViewLifecycleOwner(), response -> {
                    if(response!=null && Objects.equals(response.getId(), report.getId())){
                        Toast.makeText(getContext(), "You marked report as viewed", Toast.LENGTH_LONG).show();
                        _adapter.removeReport(position);
                        _isUpdate = true;
                        _suspensionViewModel.loadCurrentPage();
                    }
                });
                _suspensionViewModel.updateReport(report);
            }
        });
        _binding.recyclerViewReports.setAdapter(_adapter);

        _suspensionViewModel.reports.observe(getViewLifecycleOwner(), userReportResponses -> {
            _adapter.addReports(userReportResponses, _isUpdate);
            _isUpdate = !_isUpdate;
        });

        _binding.recyclerViewReports.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!_suspensionViewModel.isLoading() && !_suspensionViewModel.isEndReached()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        _suspensionViewModel.loadNextPage();
                    }
                }
            }
        });
    }

    private void setupObservers() {
        _suspensionViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                _binding.loadingSpinner.setVisibility(View.VISIBLE);
            } else {
                _binding.loadingSpinner.setVisibility(View.GONE);
            }
        });

        _suspensionViewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}