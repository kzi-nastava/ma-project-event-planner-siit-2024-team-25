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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.databinding.FragmentPublicProfileBinding;
import com.team25.event.planner.user.adapters.CompanyPicturesAdapter;
import com.team25.event.planner.user.model.EventOrganizer;
import com.team25.event.planner.user.model.Location;
import com.team25.event.planner.user.model.Owner;
import com.team25.event.planner.user.viewmodels.PublicProfileViewModel;

public class PublicProfileFragment extends Fragment {
    public static final String USER_ID_ARG = "USER_ID";

    private FragmentPublicProfileBinding binding;
    private PublicProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_public_profile, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(PublicProfileViewModel.class);
        binding.setViewModel(viewModel);

        if (getArguments() != null) {
            long userId = getArguments().getLong(USER_ID_ARG);
            if (userId != -1) {
                viewModel.setUserId(userId);
                setupProfilePicture(userId);
            }
        }

        setupObservers();

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

}