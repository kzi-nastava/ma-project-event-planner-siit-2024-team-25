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
import android.widget.TextView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentFinishPageCreatingServiceBinding;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

public class FinishPageCreatingServiceFragment extends Fragment {

    private ServiceAddFormViewModel mViewModel;

    private NavController navController;

    public static FinishPageCreatingServiceFragment newInstance() {
        return new FinishPageCreatingServiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentFinishPageCreatingServiceBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finish_page_creating_service, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mViewModel= new ViewModelProvider(this).get(ServiceAddFormViewModel.class);
        binding.setViewModel(mViewModel);

        if(getArguments() != null){
            TextView textView = binding.EditOrCreateServiceText;
            textView.setText(R.string.edit_the_service);
        }

        setObservers();
        return binding.getRoot();
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