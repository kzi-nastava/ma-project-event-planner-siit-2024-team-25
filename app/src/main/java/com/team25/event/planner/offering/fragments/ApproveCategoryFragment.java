package com.team25.event.planner.offering.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentApproveCategoryBinding;


public class ApproveCategoryFragment extends Fragment {

    private NavController navController;
    private FragmentApproveCategoryBinding binding;


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
        //vm i set
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpObservers();
        setUpListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpObservers();
        setUpListeners();
    }

    public void setUpObservers(){

    }

    public void setUpListeners(){
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_approveCategoryFragment_to_submittedOfferingCategoryFragment);
            }
        });
    }
}