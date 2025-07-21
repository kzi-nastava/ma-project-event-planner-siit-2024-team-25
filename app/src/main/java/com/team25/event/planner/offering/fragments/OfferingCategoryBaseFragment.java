package com.team25.event.planner.offering.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnDeleteButtonClickListener;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.databinding.FragmentOfferingCategoryBaseBinding;
import com.team25.event.planner.offering.adapters.OfferingCategoryAdapter;
import com.team25.event.planner.offering.dialogs.YesOrNoDialogFragment;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.viewmodel.OfferingCategoryViewModel;

import java.util.List;
import java.util.Objects;

public class OfferingCategoryBaseFragment extends Fragment implements OnEditButtonClickListener, OnDeleteButtonClickListener {
    public static final String ID_ARG_NAME = "offeringCategoryId";
    private NavController navController;
    private FragmentOfferingCategoryBaseBinding binding;
    private OfferingCategoryAdapter adapter;
    private OfferingCategoryViewModel offeringCategoryViewModel;
    private ListView listView;


    public OfferingCategoryBaseFragment() {
        // Required empty public constructor
    }

    /*public static OfferingCategoryBaseFragment newInstance(String param1, String param2) {

    }*/

    //initialize viewModels
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //init ui
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOfferingCategoryBaseBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        offeringCategoryViewModel = new ViewModelProvider(this).get(OfferingCategoryViewModel.class);
        binding.setViewModel(offeringCategoryViewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        return binding.getRoot();
    }

    //init adapter, listener
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.list;

        setUpObservers();
        setUpListeners();

        offeringCategoryViewModel.fetchOfferingCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpObservers();
        setUpListeners();
        offeringCategoryViewModel.fetchOfferingCategories();
    }

    public void setUpObservers(){
        offeringCategoryViewModel.allCategories.observe(getViewLifecycleOwner(), categories -> {
            adapter = new OfferingCategoryAdapter(requireContext(), categories);
            adapter.setOnEditButtonClickListener(this);
            adapter.setOnDeleteButtonClickListener(this);
            listView.setAdapter(adapter);
            if(categories.isEmpty()){
                binding.infoTable.setText("There is no offering categories");
                binding.infoTable.setVisibility(View.VISIBLE);
            }else{
                binding.infoTable.setText("");
                binding.infoTable.setVisibility(View.GONE);
            }
        });

        offeringCategoryViewModel.successUpdate.observe(getViewLifecycleOwner(), mess -> {
            if(Objects.equals(mess, "deleted")){
                Toast.makeText(requireContext(), "You successfully " + mess + " offering category", Toast.LENGTH_SHORT).show();
            }
        });
        offeringCategoryViewModel.serverError.observe(getViewLifecycleOwner(), mess->{
            if(mess != null){
                Toast.makeText(requireContext(), mess, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setUpListeners(){
        binding.addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_offeringCategoryFragment_to_createEditOfferingCategoryFragment);
            }
        });
        binding.buttonOfferingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_offeringCategoryFragment_to_submittedOfferingCategoryFragment);
            }
        });
    }

    @Override
    public void onEditButtonClick(Long id, String name) {
        Bundle bundle = new Bundle();
        bundle.putLong(ID_ARG_NAME, id);
        navController.navigate(R.id.action_offeringCategoryFragment_to_createEditOfferingCategoryFragment, bundle);
    }

    @Override
    public void onDeleteButtonClick(Long id, String name) {
        YesOrNoDialogFragment dialog = new YesOrNoDialogFragment(new YesOrNoDialogFragment.ConfirmDialogListener() {
            @Override
            public void onConfirm() {
                offeringCategoryViewModel.deleteOfferingCategory(id);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void refresh() {
                offeringCategoryViewModel.fetchOfferingCategories();
            }
        },name);

        dialog.show(getParentFragmentManager(), "ConfirmDialogFragment");
    }
}