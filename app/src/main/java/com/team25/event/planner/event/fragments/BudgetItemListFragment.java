package com.team25.event.planner.event.fragments;

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
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnDeleteButtonClickListener;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.databinding.FragmentBudgetItemListBinding;
import com.team25.event.planner.event.adapters.BudgetItemAdapter;
import com.team25.event.planner.event.viewmodel.BudgetItemViewModel;
import com.team25.event.planner.offering.dialogs.YesOrNoDialogFragment;


public class BudgetItemListFragment extends Fragment implements OnEditButtonClickListener, OnDeleteButtonClickListener {

    public final static String BUDGET_ITEM_ID = "BUDGET_ITEM_ID";
    private NavController navController;
    private FragmentBudgetItemListBinding binding;
    private BudgetItemViewModel viewModel;

    private ListView listView;
    private BudgetItemAdapter adapter;

    public BudgetItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetItemListBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(BudgetItemViewModel.class);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = binding.listView;

        setUpObservers();
        setUpListeners();

        viewModel.fetchBudgetItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpObservers();
        setUpListeners();

        viewModel.fetchBudgetItems();
    }

    private void setUpListeners() {
        binding.buttonNewBudgetItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_budgetItemFragment_to_createEditBudgetItemFragment);
            }
        });
    }

    private void setUpObservers() {
        viewModel.allBudgetItem.observe(getViewLifecycleOwner(), items ->{
                adapter = new BudgetItemAdapter(requireContext(), items);
                adapter.setOnEditButtonClickListener(this);
                adapter.setOnDeleteButtonClickListener(this);
                listView.setAdapter(adapter);
        });
        viewModel.deleted.observe(getViewLifecycleOwner(), check->{
            if(check){
                viewModel.fetchBudgetItems();
            }
        });
        viewModel.serverError.observe(getViewLifecycleOwner(), mess->{
            if(mess != null){
                Toast.makeText(requireContext(), mess, Toast.LENGTH_SHORT).show();

            }
        });
        viewModel.successAllItems.observe(getViewLifecycleOwner(), check->{
            if(check){
                viewModel.refreshBudget();
            }
        });
    }

    @Override
    public void onDeleteButtonClick(Long id, String name) {

        YesOrNoDialogFragment dialog = new YesOrNoDialogFragment(new YesOrNoDialogFragment.ConfirmDialogListener() {
            @Override
            public void onConfirm() {
                viewModel.deleteBudgetItem(id);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void refresh() {
            }
        },name);

        dialog.show(getParentFragmentManager(), "ConfirmDialogFragment");
    }

    @Override
    public void onEditButtonClick(Long id, String name) {
        Bundle bundle = new Bundle();
        bundle.putLong(BUDGET_ITEM_ID, id);
        //bundle.putString("BUDGET_ITEM_NAME", name);
        navController.navigate(R.id.action_budgetItemFragment_to_createEditBudgetItemFragment, bundle);
    }
}