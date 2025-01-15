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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentProfileBinding;
import com.team25.event.planner.user.model.EventOrganizer;
import com.team25.event.planner.user.model.Location;
import com.team25.event.planner.user.model.RegularUser;
import com.team25.event.planner.user.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private AuthViewModel authViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setViewModel(viewModel);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupObservers();
        setupListeners();

        return binding.getRoot();
    }

    private void setupObservers() {
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                viewModel.setEmail(user.getEmail());
                viewModel.setUserId(user.getUserId());

                final String profilePicUrl = ConnectionParams.BASE_URL + "api/users/" + user.getUserId() + "/profile-picture";
                Glide.with(this)
                        .load(profilePicUrl)
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .circleCrop()
                        .into(binding.ivProfilePic);
            } else {
                viewModel.setEmail(null);
                viewModel.setUserId(null);
            }
        });

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user instanceof EventOrganizer) {
                Location address = ((EventOrganizer) user).getLivingAddress();
                binding.tvAddress.setText(getResources().getString(R.string.full_address, address.getAddress(), address.getCity(), address.getCountry()));
                binding.tvPhone.setText(((EventOrganizer) user).getPhoneNumber());
            }
        });

        viewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnEditProfile.setOnClickListener(v -> handleEditProfile());
        binding.btnCompanyInfo.setOnClickListener(v -> handleCompanyInfo());
        binding.btnCalendar.setOnClickListener(v -> handleCalendar());
        binding.btnFavoriteEvents.setOnClickListener(v -> handleFavoriteEvents());
        binding.btnFavoriteProducts.setOnClickListener(v -> handleFavoriteProducts());
        binding.btnChangePassword.setOnClickListener(v -> handleChangePassword());
        binding.btnLogout.setOnClickListener(v -> handleLogOut());
        binding.btnDeactivate.setOnClickListener(v -> handleDeactivateAccount());
    }

    private void handleEditProfile() {
        RegularUser user = viewModel.user.getValue();
        if (user != null && user.getId() != null) {
            Bundle args = new Bundle();
            args.putLong(EditProfileFragment.USER_ID_ARG, viewModel.user.getValue().getId());
            navController.navigate(R.id.action_profileFragment_to_editProfileFragment, args);
        }
    }

    private void handleCompanyInfo() {
        // Handle company info action
    }

    private void handleCalendar() {
        // Handle calendar action
    }

    private void handleFavoriteEvents() {
        // Handle favorite events action
    }

    private void handleFavoriteProducts() {
        // Handle favorite products action
    }

    private void handleChangePassword() {
        // Handle change password action
    }

    private void handleLogOut() {
        authViewModel.clearUser();
        authViewModel.clearJwt();
        navController.popBackStack();
    }

    private void handleDeactivateAccount() {
        // Handle deactivate account action
    }

}