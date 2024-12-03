package com.team25.event.planner.product_service.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentSecondPageCreatingBinding;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

public class SecondPageCreatingServiceFragment extends Fragment {

    private ServiceAddFormViewModel mViewModel;
    private NavController navController;
    private ToggleButton toggleButton;
    private ViewSwitcher viewSwitcher;
    private FragmentSecondPageCreatingBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_page_creating, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mViewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph)
        ).get(ServiceAddFormViewModel.class);
        binding.setViewModel(mViewModel);

        toggleButton = binding.toggleButton;
        viewSwitcher = binding.viewSwitcher;
        String[] defaultCategories = {"Category 1", "Category 2", "Category 3", "Category 4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, defaultCategories);
        AutoCompleteTextView autoCompleteTextView = binding.autocompleteDropdown;
        autoCompleteTextView.setAdapter(adapter);

        if(getArguments() != null){
            TextView textView = binding.EditOrCreateServiceText;
            textView.setText(R.string.edit_the_service);
            mViewModel.fillTheForm();
            /*String productId = getArguments() != null ? getArguments().getString("productId") : null;*/
        }else{
            mViewModel.restart();
        }

        binding.setLifecycleOwner(this);
        setObservers(getArguments());
        setListeners();
        return binding.getRoot();
    }

    public void setObservers(Bundle argumentsBundle){
        mViewModel.secondToThird.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigate(R.id.action_secondPageCreatingServiceFragment_to_finishPageCreateingCerviceFragment,argumentsBundle);

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

    public void setListeners(){
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewSwitcher.showNext();  // Switch to Layout 2
            } else {
                viewSwitcher.showPrevious();  // Switch to Layout 1
            }
        });
    }

}