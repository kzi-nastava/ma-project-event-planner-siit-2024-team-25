package com.team25.event.planner.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(getLayoutInflater());

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        binding.loginBtn.setOnClickListener((button) -> {
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        });

        return binding.getRoot();
    }
}