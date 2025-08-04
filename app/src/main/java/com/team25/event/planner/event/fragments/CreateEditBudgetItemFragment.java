package com.team25.event.planner.event.fragments;

import static com.team25.event.planner.event.fragments.BudgetItemListFragment.BUDGET_ITEM_ID;

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
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentCreateEditBudgetItemBinding;
import com.team25.event.planner.databinding.FragmentCreateEditOfferingCategoryBinding;
import com.team25.event.planner.event.viewmodel.BudgetItemViewModel;
import com.team25.event.planner.event.viewmodel.EventTypeListViewModel;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
        viewModel = new ViewModelProvider(requireActivity()).get(BudgetItemViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );

        if(getArguments()!=null){

            binding.title.setText("Edit the budget item");
            viewModel.isEditMode.setValue(true);
            Long budgetItemId = getArguments().getLong(BUDGET_ITEM_ID);
            viewModel.setUoBudgetItemId(budgetItemId);
            binding.spinnerOfferingCategory.setEnabled(false);
            binding.saveButton.setText("Edit");
        }else{
            viewModel.isEditMode.setValue(false);
            viewModel.resetForm();
            binding.spinnerOfferingCategory.setEnabled(true);
            binding.saveButton.setText("Create");
        }

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

                if(Boolean.TRUE.equals(viewModel.isEditMode.getValue())){
                    viewModel.setIsOfferingCategorySuitable(false);
                    viewModel.saveBudgetItem();
                }else{
                    if(viewModel.checkOfferingCategoryId()){
                        viewModel.isSuitableCategoryForEvent();
                    }

                }

            }
        });

    }


    private void setUpObservers() {
        viewModel.isOfferingCategorySuitable.observe(getViewLifecycleOwner(), check -> {
            if(Boolean.TRUE.equals(viewModel.flagCreateBudgetItem.getValue())){
                viewModel.setIsNotReadyTOCreate();
                viewModel.saveBudgetItem();

            }


        });
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
                if(Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.createEditBudgetItemFragment){
                    navController.navigateUp();
                    String s = "edited";
                    if(Boolean.FALSE.equals(viewModel.isEditMode.getValue())){
                        s = "added";
                    }

                    Toast.makeText(requireContext(),"You successfully " + s + " budget item", Toast.LENGTH_SHORT).show();
                    viewModel.resetSuccess();
            }



            }
        });
        viewModel.serverError.observe(getViewLifecycleOwner(), msg -> {
            if(!Objects.equals(msg, "")){

                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                viewModel.resetServerError();
            }

        });
    }

    private void setOfferingCategorySpinner(){
        categorySpinner = binding.spinnerOfferingCategory;
        if(Boolean.FALSE.equals(viewModel.isEditMode.getValue())){
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    OfferingCategory selectedCategory = (OfferingCategory) parent.getItemAtPosition(position);
                    viewModel.offeringCategoryId.setValue(selectedCategory.getId());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            viewModel.fetchSuitableOfferingCategories();
        }

    }

}