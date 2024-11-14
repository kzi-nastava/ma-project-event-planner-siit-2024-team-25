package com.team25.event.planner.product_service.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentFinishPageCreateingCerviceBinding;
import com.team25.event.planner.databinding.FragmentSecondPageCreatingBinding;
import com.team25.event.planner.product_service.viewModels.FinishPageCreateingCerviceViewModel;
import com.team25.event.planner.product_service.viewModels.SecondPageCreatingViewModel;

public class FinishPageCreateingCerviceFragment extends Fragment {

    private FinishPageCreateingCerviceViewModel mViewModel;

    private NavController navController;

    public static FinishPageCreateingCerviceFragment newInstance() {
        return new FinishPageCreateingCerviceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentFinishPageCreateingCerviceBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finish_page_createing_cervice, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        mViewModel= new ViewModelProvider(this).get(FinishPageCreateingCerviceViewModel.class);
        binding.setViewModel(mViewModel);

        setObservers();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FinishPageCreateingCerviceViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setObservers(){
        mViewModel.toFinish.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("New service")
                        .setMessage("You are successfully added a new service!")


                        .create();

                alertDialog.show();

                mViewModel.toFinish.setValue(false);

                navController.navigate(R.id.ownerHomePage, null, new NavOptions.Builder()
                        .setPopUpTo(R.id.ownerHomePage, false).build());
            }
        });
        mViewModel.toSecond.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigateUp();
                mViewModel.toSecond.setValue(false);

            }
        });
    }

}