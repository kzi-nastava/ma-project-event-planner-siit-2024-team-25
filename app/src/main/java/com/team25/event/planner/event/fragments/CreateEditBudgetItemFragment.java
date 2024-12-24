package com.team25.event.planner.event.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentCreateEditBudgetItemBinding;
import com.team25.event.planner.databinding.FragmentCreateEditOfferingCategoryBinding;
import com.team25.event.planner.event.viewmodel.BudgetItemViewModel;
import com.team25.event.planner.event.viewmodel.EventTypeListViewModel;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;

import java.util.ArrayList;
import java.util.List;


public class CreateEditBudgetItemFragment extends Fragment {

    private FragmentCreateEditBudgetItemBinding binding;
    private NavController navController;
    private BudgetItemViewModel viewModel;
    private Spinner categorySpinner;


    public CreateEditBudgetItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateEditBudgetItemBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(BudgetItemViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOfferingCategorySpinner();
        setUpObservers();
        setUpListeners();
    }

    @Override
    public void onResume() {
        setOfferingCategorySpinner();
        super.onResume();
        setUpObservers();
        setUpListeners();
    }
    private void setUpListeners() {
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.saveBudgetItem();
            }
        });

    }

    private void setUpObservers() {
        viewModel.allCategories.observe(getViewLifecycleOwner(), offeringCategories -> {
            List<OfferingCategory> updatedCategories = new ArrayList<>();
            updatedCategories.add(new OfferingCategory(null, "Select an offering category"));
            updatedCategories.addAll(offeringCategories);
            ArrayAdapter<OfferingCategory> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, updatedCategories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        });
        viewModel.success.observe(getViewLifecycleOwner(), check ->{
            if(check){

                String s = "edited";
                if(Boolean.FALSE.equals(viewModel.isEditMode.getValue())){
                    s = "added";
                }
                new AlertDialog.Builder(requireContext())
                        .setTitle("Information")
                        .setMessage("You successfully " + s + " budget item")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                navController.navigateUp();
            }
        });
    }

    private void setOfferingCategorySpinner(){
        categorySpinner = binding.spinnerOfferingCategory;

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OfferingCategory selectedCategory = (OfferingCategory) parent.getItemAtPosition(position);
                viewModel.offeringCategoryId.setValue(selectedCategory.getId());
                viewModel.isSuitableCategoryForEvent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewModel.fetchSuitableOfferingCategories();
    }

}