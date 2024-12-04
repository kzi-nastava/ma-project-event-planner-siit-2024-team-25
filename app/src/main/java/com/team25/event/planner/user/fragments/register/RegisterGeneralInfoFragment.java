package com.team25.event.planner.user.fragments.register;

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
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentRegisterGeneralInfoBinding;
import com.team25.event.planner.user.viewmodels.RegisterViewModel;

import java.io.File;

public class RegisterGeneralInfoFragment extends Fragment {
    private FragmentRegisterGeneralInfoBinding binding;
    private RegisterViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_general_info, container, false);
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
        setupListeners();
    }

    private void setupObservers() {
        viewModel.formStep.observe(getViewLifecycleOwner(), formStep -> {
            if (formStep == null) {
                return;
            }
            if (formStep.equals(RegisterViewModel.RegisterStep.OrganizerSpecific)) {
                navController.navigate(R.id.action_registerGeneralInfoFragment_to_registerOrganizerFragment);
            } else if (formStep.equals(RegisterViewModel.RegisterStep.VendorSpecific)) {
                navController.navigate(R.id.action_registerGeneralInfoFragment_to_registerVendorFragment);
            } else if (formStep.equals(RegisterViewModel.RegisterStep.RoleChoice)) {
                navController.navigateUp();
            } else {
                return;
            }
            viewModel.clearFormStepNavigation();
        });
    }


    private void setupListeners() {
        binding.ivProfilePic.setOnClickListener((view) -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(1024) // max size 1MB
                    .createIntent((intent) -> {
                        startForProfileImageResult.launch(intent);
                        return null;
                    });
        });
    }

    private final ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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