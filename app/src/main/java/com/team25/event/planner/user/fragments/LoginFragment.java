package com.team25.event.planner.user.fragments;

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
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentLoginBinding;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.user.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {
    private LoginViewModel viewModel;
    private String _invitationCode;
    private Long _eventId;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            _invitationCode = args.getString("invitationCode");
            _eventId = args.getLong("eventId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        viewModel.setAuthViewModel(authViewModel);

        binding.setViewModel(viewModel);

        setupObservers();

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.loggedIn.observe(getViewLifecycleOwner(), loggedIn -> {
            if (loggedIn) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

                if (this._invitationCode != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(EventArgumentNames.INVITATION_CODE_ARG, _invitationCode);
                    bundle.putLong(EventArgumentNames.NAME_ARG, _eventId);

                    navController.navigate(R.id.eventDetailsFragment, bundle);
                } else {
                    navController.navigate(R.id.action_loginFragment_to_homeFragment);
                }
            }
        });
    }
}