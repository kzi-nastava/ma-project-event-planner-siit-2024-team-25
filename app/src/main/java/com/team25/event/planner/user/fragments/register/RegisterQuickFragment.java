package com.team25.event.planner.user.fragments.register;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentRegisterGeneralInfoBinding;
import com.team25.event.planner.databinding.FragmentRegisterQuickBinding;
import com.team25.event.planner.user.model.UserRole;
import com.team25.event.planner.user.viewmodels.RegisterQuickViewModel;
import com.team25.event.planner.user.viewmodels.RegisterViewModel;

import java.io.File;

public class RegisterQuickFragment extends Fragment {
    private FragmentRegisterQuickBinding binding;
    private RegisterQuickViewModel _registerQuickViewModel;
    private NavController navController;
    private Long _eventId;

    private String _invitationCode;


    public RegisterQuickFragment() {
        _registerQuickViewModel = new RegisterQuickViewModel();
        _eventId = -1l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_quick, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        _registerQuickViewModel = new ViewModelProvider(
                navController.getViewModelStoreOwner(R.id.nav_graph)
        ).get(RegisterQuickViewModel.class);

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        _registerQuickViewModel.setAuthViewModel(authViewModel);

        binding.setViewModel(_registerQuickViewModel);

        setupListeners();

        binding.buttonClose.setOnClickListener(v -> {
            navController.navigate(R.id.homeFragment);
        });

        Bundle args = getArguments();
        if (args != null) {
            _invitationCode = args.getString("invitationCode");
            _registerQuickViewModel.invitationCode.setValue(this._invitationCode);
        }

        //TO DO -> open event fragment
        _registerQuickViewModel.eventId.observe(getViewLifecycleOwner(), id -> {
            this._eventId = id;
        });

        _registerQuickViewModel.loggedIn.observe(getViewLifecycleOwner(), loggedIn ->{
            if(loggedIn){
                navController.navigate(R.id.homeFragment);
                Toast.makeText(getContext(), "Event: " + this._eventId, Toast.LENGTH_SHORT).show();
            }
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
                _registerQuickViewModel.profilePicture.postValue(new File(fileUri.getPath()));
            } else {
                binding.ivProfilePic.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person));
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    });
}