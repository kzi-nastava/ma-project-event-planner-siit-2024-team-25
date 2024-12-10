package com.team25.event.planner.offering.fragments;

import static com.team25.event.planner.offering.fragments.OfferingCategoryBaseFragment.ID_ARG_NAME;
import static com.team25.event.planner.offering.fragments.SubmittedOfferingCategoryFragment.OFFERING_CATEGORY_ID_ARG_NAME;
import static com.team25.event.planner.offering.fragments.SubmittedOfferingCategoryFragment.OFFERING_ID_ARG_NAME;

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
import android.widget.CheckBox;
import android.widget.Spinner;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentApproveCategoryBinding;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;
import com.team25.event.planner.offering.viewmodel.SubmittedCategoryViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ApproveCategoryFragment extends Fragment {

    private NavController navController;
    private FragmentApproveCategoryBinding binding;
    private SubmittedCategoryViewModel viewModel;
    private OfferingCategoryViewModel offeringCategoryViewModel;

    private CheckBox checkBox;
    private Spinner spinner;
    private HashMap<Integer,Long> createdCategories = new HashMap<>();
    private Long acceptedOfferingCategoryId;
    private Boolean isUpdateCategory = true;


    public ApproveCategoryFragment() {
        // Required empty public constructor
    }

    //public static ApproveCategoryFragment newInstance(String param1, String param2) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApproveCategoryBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        offeringCategoryViewModel = new ViewModelProvider(this).get(OfferingCategoryViewModel.class);
        viewModel = new ViewModelProvider(this).get(SubmittedCategoryViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if(getArguments()!=null){
            Long offeringId = getArguments().getLong(OFFERING_ID_ARG_NAME);
            Long categoryId = getArguments().getLong(OFFERING_CATEGORY_ID_ARG_NAME);
            viewModel.offeringId.postValue(offeringId);
            viewModel.categoryId.postValue(categoryId);
            viewModel.fetchSubmittedCategory(categoryId);
        }
        setUpUI();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpUI();
        setUpObservers();
        setUpListeners();
        offeringCategoryViewModel.fetchOfferingCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpUI();
        setUpObservers();
        setUpListeners();
        offeringCategoryViewModel.fetchOfferingCategories();
    }

    public void setUpUI(){
        checkBox = binding.checkBox;
        spinner = binding.spinner;
    }

    public void setUpObservers(){
        viewModel.success.observe(getViewLifecycleOwner(), check ->{
            if(check &&navController.getCurrentDestination() != null&& navController.getCurrentDestination().getId() != R.id.submittedOfferingCategoryFragment){
                navController.navigate(R.id.action_approveCategoryFragment_to_submittedOfferingCategoryFragment);
            }
        });
        offeringCategoryViewModel.allCategories.observe(getViewLifecycleOwner(), categories -> {
            createdCategories.clear();
            List<String> nameOfferingCategory = new ArrayList<>();
            if(categories != null){
                if(!categories.isEmpty()){
                    Integer position = 0;
                    for (OfferingCategory o:categories) {
                        nameOfferingCategory.add(o.getName());
                        createdCategories.put(position,o.getId());
                        position++;
                    }
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    nameOfferingCategory
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        });
    }

    public void setUpListeners(){
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_approveCategoryFragment_to_submittedOfferingCategoryFragment);
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUpdateCategory){
                    viewModel.updateOfferingCategory();
                }else{
                    viewModel.changeOfferingsCategory(acceptedOfferingCategoryId, createdCategories.size());
                }

            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.validSpinner(1);
            if (isChecked) {
                spinner.setVisibility(View.VISIBLE);
                binding.nameInput.setEnabled(false);
                binding.descriptionInput.setEnabled(false);
                isUpdateCategory = false;
            } else {
                spinner.setVisibility(View.GONE);
                binding.nameInput.setEnabled(true);
                binding.descriptionInput.setEnabled(true);
                isUpdateCategory = true;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                acceptedOfferingCategoryId = createdCategories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}