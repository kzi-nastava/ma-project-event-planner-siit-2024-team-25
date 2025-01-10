package com.team25.event.planner.user.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team25.event.planner.databinding.FragmentReportedUsersBinding;
import com.team25.event.planner.databinding.FragmentSuspendedUsersBinding;
import com.team25.event.planner.user.adapters.SuspensionListAdapter;
import com.team25.event.planner.user.viewmodels.ReportViewModel;
import com.team25.event.planner.user.viewmodels.SuspensionViewModel;

import java.util.ArrayList;


public class SuspendedUsers  extends Fragment {
    private FragmentSuspendedUsersBinding _binding;
    private SuspensionListAdapter _adapter;
    private SuspensionViewModel _suspensionViewModel;

    public SuspendedUsers() {
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
        _binding = FragmentSuspendedUsersBinding.inflate(inflater, container, false);
        _binding.setLifecycleOwner(getViewLifecycleOwner());
        _suspensionViewModel = new ViewModelProvider(this).get(SuspensionViewModel.class);

        setupSuspensionsList();
        setupObservers();

        _suspensionViewModel.loadNextPage();

        return _binding.getRoot();
    }

    private void setupSuspensionsList() {
        _adapter = new SuspensionListAdapter(new ArrayList<>(), null);
        _binding.recyclerViewReports.setAdapter(_adapter);

        _suspensionViewModel.suspensions.observe(getViewLifecycleOwner(), userReportResponses -> {
            _adapter.addSuspensions(userReportResponses);
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