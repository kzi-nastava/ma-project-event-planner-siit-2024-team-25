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
import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentRegisterRoleBinding;
import com.team25.event.planner.user.model.RegularUser;
import com.team25.event.planner.user.model.User;
import com.team25.event.planner.user.viewmodels.RegisterViewModel;

public class RegisterRoleFragment extends Fragment {
    FragmentRegisterRoleBinding binding;
    private RegisterViewModel viewModel;
    private NavController navController;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_role, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        SharedPrefService sharedPrefService = new SharedPrefService(getActivity());
        authViewModel.initialize(sharedPrefService);

        if (getArguments() != null) {
            authViewModel.user.observe(getViewLifecycleOwner(), user -> {
                Long userId = getArguments().getLong("userId");
                String firstName = getArguments().getString("firstName");
                String lastName = getArguments().getString("lastName");
                if(userId.equals(user.getId())){
                    viewModel.isUpgrade.setValue(true);
                    viewModel.email.setValue(user.getEmail());
                    viewModel.lastName.setValue(lastName);
                    viewModel.firstName.setValue(firstName);
                }
            });
        }

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
            if (formStep.equals(RegisterViewModel.RegisterStep.GeneralInfo)) {
                navController.navigate(R.id.action_registerRoleFragment_to_registerGeneralInfoFragment);
                viewModel.clearFormStepNavigation();
            }
        });

        viewModel.goToLogin.observe(getViewLifecycleOwner(), goToLogin -> {
            if (goToLogin) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.loginFragment);
                viewModel.goToLogin.setValue(false);
            }
        });
    }
}