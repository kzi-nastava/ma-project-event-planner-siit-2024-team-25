package com.team25.event.planner.offering.fragments;

import static com.team25.event.planner.offering.fragments.OfferingCategoryBaseFragment.ID_ARG_NAME;

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

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentCreateEditOfferingCategoryBinding;
import com.team25.event.planner.databinding.FragmentOfferingCategoryBaseBinding;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;

public class CreateEditOfferingCategoryFragment extends Fragment {

    private FragmentCreateEditOfferingCategoryBinding binding;
    private OfferingCategoryViewModel viewModel;
    private NavController navController;
    private Boolean isEdit = false;

    public CreateEditOfferingCategoryFragment() {
        // Required empty public constructor
    }

    /*public static CreateEditOfferingCategoryFragment newInstance() {

    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateEditOfferingCategoryBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(OfferingCategoryViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );

        if(getArguments()!=null){
            isEdit = true;
            Long categoryId = getArguments().getLong(ID_ARG_NAME);
            viewModel.setUpCategoryId(categoryId);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpUI();
        setUpObservers();
        setUpListeners();
    }
    public void setUpUI(){
        if(isEdit){
            binding.title.setText(R.string.update_offering_category);
            binding.saveButton.setText(R.string.edit);
        }
    }

    public void setUpObservers(){
        viewModel.success.observe(getViewLifecycleOwner(), isCreated ->{
            if(isCreated){
                navController.navigate(R.id.action_createEditOfferingCategoryFragment_to_offeringCategoryFragment);
            }
        });
    }

    public void setUpListeners(){
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_createEditOfferingCategoryFragment_to_offeringCategoryFragment);
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.saveCategory(isEdit);

            }
        });
    }
}