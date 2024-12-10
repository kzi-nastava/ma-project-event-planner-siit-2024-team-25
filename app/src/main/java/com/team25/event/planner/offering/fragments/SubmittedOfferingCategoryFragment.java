package com.team25.event.planner.offering.fragments;

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
import android.widget.ListView;

import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnApproveButtonClickListener;
import com.team25.event.planner.databinding.FragmentSubmittedOfferingCategoryBinding;
import com.team25.event.planner.offering.adapters.SubmittedCategoryAdapter;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;
import com.team25.event.planner.offering.viewmodel.SubmittedCategoryViewModel;

public class SubmittedOfferingCategoryFragment extends Fragment implements OnApproveButtonClickListener {

    public static final String OFFERING_ID_ARG_NAME = "offeringId";
    public static final String OFFERING_CATEGORY_ID_ARG_NAME = "offeringCategoryId";
    private NavController navController;
    private FragmentSubmittedOfferingCategoryBinding binding;
    private SubmittedCategoryAdapter adapter;
    private SubmittedCategoryViewModel viewModel;
    private ListView listView;


    public SubmittedOfferingCategoryFragment() {
        // Required empty public constructor
    }

    /*public static SubmittedOfferingCategoryFragment newInstance(String param1, String param2) {

    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubmittedOfferingCategoryBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(SubmittedCategoryViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.list;

        setUpObservers();
        setUpListeners();

        viewModel.fetchSubmittedCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpObservers();
        setUpListeners();

        viewModel.fetchSubmittedCategories();
    }

    public void setUpObservers(){
        viewModel.submittedCategories.observe(getViewLifecycleOwner(), categories ->{
            adapter = new SubmittedCategoryAdapter(requireContext(),categories);
            adapter.setOnApproveButtonClickListener(this);
            listView.setAdapter(adapter);
            if(categories.isEmpty()){
                binding.infoTableSubmitted.setText("There is no submitted offering categories");
                binding.infoTableSubmitted.setVisibility(View.VISIBLE);
            }else{
                binding.infoTableSubmitted.setText("");
                binding.infoTableSubmitted.setVisibility(View.GONE);
            }
        });
    }
    public void setUpListeners(){
        binding.buttonSubmittedOfferingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_submittedOfferingCategoryFragment_to_offeringCategoryFragment);
            }
        });
    }

    @Override
    public void onApproveButtonClick(Long id, Long oldCategoryId) {
        Bundle bundle = new Bundle();
        bundle.putLong(OFFERING_ID_ARG_NAME, id);
        bundle.putLong(OFFERING_CATEGORY_ID_ARG_NAME, oldCategoryId);
        navController.navigate(R.id.action_submittedOfferingCategoryFragment_to_approveCategoryFragment, bundle);
    }
}