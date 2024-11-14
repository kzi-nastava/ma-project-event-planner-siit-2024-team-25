package com.team25.event.planner.user.fragments.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentRegisterVendorBinding;
import com.team25.event.planner.user.viewmodels.RegisterViewModel;

public class RegisterVendorFragment extends Fragment {
    private FragmentRegisterVendorBinding binding;
    private RegisterViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_vendor, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        viewModel = new ViewModelProvider(
                navController.getViewModelStoreOwner(R.id.nav_graph_registration)
        ).get(RegisterViewModel.class);

        binding.setViewModel(viewModel);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.formStep.observe(getViewLifecycleOwner(), formStep -> {
            if (formStep == null) {
                return;
            }
            if (formStep.equals(RegisterViewModel.RegisterStep.Success)) {
                navController.navigate(R.id.action_registerVendorFragment_to_registerSuccessFragment);
            } else if (formStep.equals(RegisterViewModel.RegisterStep.GeneralInfo)) {
                navController.navigateUp();
            } else {
                return;
            }
            viewModel.clearFormStepNavigation();
        });
    }
}