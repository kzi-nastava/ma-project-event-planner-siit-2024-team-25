package com.team25.event.planner.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEditCompanyBinding;
import com.team25.event.planner.user.viewmodels.EditCompanyViewModel;

public class EditCompanyFragment extends Fragment {
    public static final String USER_ID_ARG = "USER_ID";

    private FragmentEditCompanyBinding binding;
    private EditCompanyViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_company, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(EditCompanyViewModel.class);
        binding.setViewModel(viewModel);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getArguments() != null) {
            long userId = getArguments().getLong(USER_ID_ARG);
            if (userId != -1) {
                viewModel.setUserId(userId);
            }
        }

        setupObservers();
        setupListeners();

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.serverError.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
        viewModel.navigateBack.observe(getViewLifecycleOwner(), shouldNavigate -> {
            if (shouldNavigate) {
                navController.popBackStack();
                viewModel.onNavigateBackComplete();
            }
        });
    }

    private void setupListeners() {
        binding.buttonCancel.setOnClickListener(v -> navController.popBackStack());
    }
}