package com.team25.event.planner.user.fragments.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentRegisterSuccessBinding;


public class RegisterSuccessFragment extends Fragment {
    private FragmentRegisterSuccessBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterSuccessBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setupListeners();
    }

    private void setupListeners() {
        binding.btnGoToLogin.setOnClickListener((view) -> {
            navController.navigate(R.id.loginFragment, null, new NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, false).build());
        });
    }
}