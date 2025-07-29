package com.team25.event.planner.user.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.fragments.MapFragment;
import com.team25.event.planner.databinding.FragmentPublicProfileBinding;
import com.team25.event.planner.databinding.ReportDialogBinding;
import com.team25.event.planner.user.adapters.CompanyPicturesAdapter;
import com.team25.event.planner.user.model.EventOrganizer;
import com.team25.event.planner.user.model.Location;
import com.team25.event.planner.user.model.Owner;
import com.team25.event.planner.user.model.RegularUser;
import com.team25.event.planner.user.viewmodels.PublicProfileViewModel;
import com.team25.event.planner.user.viewmodels.UserReportViewModel;

public class PublicProfileFragment extends Fragment {
    public static final String USER_ID_ARG = "USER_ID";

    private FragmentPublicProfileBinding binding;
    private PublicProfileViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_public_profile, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(PublicProfileViewModel.class);
        binding.setViewModel(viewModel);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getArguments() != null) {
            long userId = getArguments().getLong(USER_ID_ARG);
            if (userId != -1) {
                viewModel.setUserId(userId);
                setupProfilePicture(userId);
            }
        }

        setupObservers();
        setupListeners();

        return binding.getRoot();
    }

    private void setupProfilePicture(long userId) {
        final String profilePicUrl = ConnectionParams.BASE_URL + "api/users/" + userId + "/profile-picture";
        Glide.with(this)
                .load(profilePicUrl)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .circleCrop()
                .into(binding.ivProfilePic);
    }

    private void setupObservers() {
        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user instanceof EventOrganizer) {
                Location address = ((EventOrganizer) user).getLivingAddress();
                binding.tvAddress.setText(getResources().getString(R.string.full_address,
                        address.getAddress(), address.getCity(), address.getCountry()));
                binding.tvPhone.setText(((EventOrganizer) user).getPhoneNumber());
            } else if (user instanceof Owner) {
                Owner owner = (Owner) user;
                Location companyAddress = owner.getCompanyAddress();
                binding.tvCompanyAddress.setText(getResources().getString(R.string.full_address,
                        companyAddress.getAddress(), companyAddress.getCity(), companyAddress.getCountry()));

                if (owner.getCompanyPictures() != null && !owner.getCompanyPictures().isEmpty()) {
                    CompanyPicturesAdapter adapter = new CompanyPicturesAdapter(getContext(), user.getId());
                    binding.rvCompanyPictures.setAdapter(adapter);
                    adapter.submitList(owner.getCompanyPictures());
                }
            }
        });

        viewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.showOnMapButton.setOnClickListener(v -> showOnMap());
        binding.reportButton.setOnClickListener(v -> showReportDialog());

    }

    private void showOnMap() {
        final Owner owner = viewModel.owner.getValue();
        if (owner == null) return;

        Bundle args = new Bundle();
        args.putString(MapFragment.TITLE_ARG, owner.getCompanyName());
        args.putParcelable(MapFragment.LOCATION_ARG, owner.getCompanyAddress());
        navController.navigate(R.id.mapFragment, args);
    }

    private UserReportViewModel reportViewModel;
    private Observer<String> serverErrorObserver;
    private Observer<Boolean> reportSendObserver;

    private void showReportDialog() {
        RegularUser user = viewModel.user.getValue();
        if (user == null) {
            return;
        }

        removeObservers();

        ReportDialogBinding reportBinding = ReportDialogBinding.inflate(getLayoutInflater());

        reportViewModel = new ViewModelProvider(this,
                new UserReportViewModel.Factory(user.getId())).get(UserReportViewModel.class);

        reportBinding.setViewModel(reportViewModel);
        reportBinding.setLifecycleOwner(this);

        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(reportBinding.getRoot())
                .create();

        serverErrorObserver = errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        };

        reportSendObserver = isReportSent -> {
            if (isReportSent) {
                Toast.makeText(getContext(), R.string.report_sent_successfully, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                reportViewModel.onReportDone();
            }
        };

        reportViewModel.serverError.observe(getViewLifecycleOwner(), serverErrorObserver);
        reportViewModel.isReportSend.observe(getViewLifecycleOwner(), reportSendObserver);

        dialog.show();
    }

    private void removeObservers() {
        if (reportViewModel != null) {
            if (serverErrorObserver != null) {
                reportViewModel.serverError.removeObserver(serverErrorObserver);
                serverErrorObserver = null;
            }
            if (reportSendObserver != null) {
                reportViewModel.isReportSend.removeObserver(reportSendObserver);
                reportSendObserver = null;
            }
        }
    }
}