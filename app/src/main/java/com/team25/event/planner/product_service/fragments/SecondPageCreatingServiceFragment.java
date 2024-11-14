package com.team25.event.planner.product_service.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentSecondPageCreatingBinding;
import com.team25.event.planner.databinding.FragmentServiceAddFormBinding;
import com.team25.event.planner.product_service.viewModels.SecondPageCreatingViewModel;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

public class SecondPageCreatingServiceFragment extends Fragment {

    private SecondPageCreatingViewModel mViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSecondPageCreatingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_page_creating, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        mViewModel= new ViewModelProvider(this).get(SecondPageCreatingViewModel.class);
        binding.setViewModel(mViewModel);

        setObservers();

        ToggleButton toggleButton = binding.toggleButton;
        ViewSwitcher viewSwitcher = binding.viewSwitcher;

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewSwitcher.showNext();  // Switch to Layout 2
            } else {
                viewSwitcher.showPrevious();  // Switch to Layout 1
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SecondPageCreatingViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setObservers(){
        mViewModel.secondToThird.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigate(R.id.action_secondPageCreatingServiceFragment_to_finishPageCreateingCerviceFragment);

                mViewModel.secondToThird.setValue(false);
            }
        });

        mViewModel.secondToFirst.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigateUp();

                mViewModel.secondToFirst.setValue(false);
            }
        });
    }

}