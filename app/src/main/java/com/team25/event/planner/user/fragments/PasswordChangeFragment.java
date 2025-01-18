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
import com.team25.event.planner.databinding.FragmentPasswordChangeBinding;
import com.team25.event.planner.user.viewmodels.PasswordChangeViewModel;

public class PasswordChangeFragment extends Fragment {
    private FragmentPasswordChangeBinding binding;
    private PasswordChangeViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_password_change, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(PasswordChangeViewModel.class);
        binding.setViewModel(viewModel);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

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
        viewModel.successSignal.observe(getViewLifecycleOwner(), succeeded -> {
            if (succeeded) {
                Toast.makeText(getContext(), "Successfully changed the password!", Toast.LENGTH_SHORT).show();
                navController.popBackStack();
                viewModel.onSuccessHandleComplete();
            }
        });
    }

    private void setupListeners() {
        binding.buttonCancel.setOnClickListener(v -> navController.popBackStack());
    }

}