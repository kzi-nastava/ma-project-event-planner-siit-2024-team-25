package com.team25.event.planner.product_service.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentServiceAddFormBinding;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

public class ServiceAddForm extends Fragment {

    private ServiceAddFormViewModel mViewModel;


    public static ServiceAddForm newInstance() {
        return new ServiceAddForm();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentServiceAddFormBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_add_form, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

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
                // Zamenjujemo fragment kada dođe signal
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.addService, new SecondPageCreatingServiceFragment())  // ID kontejnera za fragmente
                        .commit();

                mViewModel.firstToSecond.setValue(false);
            }
        });

        mViewModel.cancelClicked.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                FragmentTransition.to(new OwnerHomePage(),requireActivity(),true,R.id.main_layout);

                mViewModel.cancelClicked.setValue(false);
            }
        });
    }

}