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

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentServiceAddFormBinding;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

public class ServiceAddForm extends Fragment {

    private ServiceAddFormViewModel mViewModel;

    private NavController navController;
    public static ServiceAddForm newInstance() {
        return new ServiceAddForm();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentServiceAddFormBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_add_form, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        mViewModel= new ViewModelProvider(this).get(ServiceAddFormViewModel.class);
        binding.setViewModel(mViewModel);

        setObservers();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServiceAddFormViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setObservers(){
        mViewModel.firstToSecond.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigate(R.id.action_serviceAddForm_to_secondPageCreatingServiceFragment);

                mViewModel.firstToSecond.setValue(false);
            }
        });

        mViewModel.cancelClicked.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigateUp();
                mViewModel.cancelClicked.setValue(false);
            }
        });
    }

}