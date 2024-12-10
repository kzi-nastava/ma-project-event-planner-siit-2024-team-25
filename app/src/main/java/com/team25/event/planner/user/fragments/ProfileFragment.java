package com.team25.event.planner.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        binding.logoutButton.setOnClickListener(v -> {
            authViewModel.clearUser();
            navController.popBackStack();
        });

        return binding.getRoot();
    }
}