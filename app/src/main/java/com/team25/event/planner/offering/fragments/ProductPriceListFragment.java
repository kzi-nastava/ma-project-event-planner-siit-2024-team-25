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
import android.widget.TableLayout;

import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.databinding.FragmentProductPriceListBinding;
import com.team25.event.planner.offering.adapters.ProductPriceListAdapter;
import com.team25.event.planner.offering.viewmodel.PriceListViewModel;


public class ProductPriceListFragment extends Fragment implements OnEditButtonClickListener {
    private FragmentProductPriceListBinding binding;
    private TableLayout tableLayout;
    private NavController navController;
    private PriceListViewModel viewModel;
    private ProductPriceListAdapter adapter;
    private ListView listView;
    private boolean isProductsView = false;


    public ProductPriceListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductPriceListBinding.inflate(inflater, container, false);
        //setlifecycle
        viewModel = new ViewModelProvider(this).get(PriceListViewModel.class);
        //set vm
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.list;

        setUpObservers();
        setUpListeners();
    }

    private void setUpListeners() {
    }

    private void setUpObservers() {
        viewModel.priceListItems.observe(getViewLifecycleOwner(), res ->{
            adapter = new ProductPriceListAdapter(requireContext(), res);
            adapter.setListener(this);
            listView.setAdapter(adapter);
        });
    }

    @Override
    public void onEditButtonClick(Long id, String name) {

    }
}