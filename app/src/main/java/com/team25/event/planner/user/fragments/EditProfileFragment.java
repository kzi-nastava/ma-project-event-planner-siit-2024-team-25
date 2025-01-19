package com.team25.event.planner.user.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.databinding.FragmentEditProfileBinding;
import com.team25.event.planner.user.model.RegularUser;
import com.team25.event.planner.user.viewmodels.EditProfileViewModel;

import java.io.File;

public class EditProfileFragment extends Fragment {
    public static final String USER_ID_ARG = "USER_ID";

    private FragmentEditProfileBinding binding;
    private EditProfileViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
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
        viewModel.user.observe(getViewLifecycleOwner(), this::populateUserData);
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

    private void populateUserData(RegularUser user) {
        if (user == null) return;

        // Load profile picture
        String profilePicUrl = ConnectionParams.BASE_URL + "api/users/" + user.getId() + "/profile-picture";
        Glide.with(this)
                .load(profilePicUrl)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .circleCrop()
                .into(binding.ivProfilePic);

        viewModel.setUserFields(user);
    }

    private void setupListeners() {
        binding.profilePicContainer.setOnClickListener(v -> openImagePicker());
        binding.buttonCancel.setOnClickListener(v -> navController.popBackStack());
        binding.buttonSave.setOnClickListener(v -> viewModel.onSubmit());
    }

    private void openImagePicker() {
        ImagePicker.with(this)
                .cropSquare()
                .compress(1024) // max size 1MB
                .createIntent((intent) -> {
                    imagePickerLauncher.launch(intent);
                    return null;
                });
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        int resultCode = result.getResultCode();
        Intent data = result.getData();

        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri = data != null ? data.getData() : null;
            if (fileUri != null) {
                binding.ivProfilePic.setImageTintList(null);
                binding.ivProfilePic.setImageURI(fileUri);
                viewModel.profilePicture.postValue(new File(fileUri.getPath()));
            } else {
                binding.ivProfilePic.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person));
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    });
}