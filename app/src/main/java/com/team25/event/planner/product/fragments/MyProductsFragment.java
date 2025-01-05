package com.team25.event.planner.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentMyProductsBinding;
import com.team25.event.planner.product.adapters.MyProductsAdapter;
import com.team25.event.planner.product.viewmodel.MyProductsViewModel;

import java.util.ArrayList;

public class MyProductsFragment extends Fragment {
    private FragmentMyProductsBinding binding;
    private MyProductsViewModel viewModel;
    private AuthViewModel authViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_products, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(MyProductsViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupProductList();
        setupObservers();
        setupListeners();

        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            viewModel.setOwnerId(user.getUserId());
            viewModel.loadNextPage();
        });

        return binding.getRoot();
    }

    private void setupProductList() {
        MyProductsAdapter adapter = new MyProductsAdapter(
                new ArrayList<>(),
                product -> { /* TODO: navigate to product details fragment */ },
                product -> { /* TODO: navigate to product form fragment */ },
                product -> { /* TODO: show delete confirmation modal */ }
        );
        binding.recyclerViewProducts.setAdapter(adapter);

        viewModel.products.observe(getViewLifecycleOwner(), adapter::addProducts);

        binding.recyclerViewProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!viewModel.isLoading() && !viewModel.isEndReached()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        viewModel.loadNextPage();
                    }
                }
            }
        });
    }

    private void setupObservers() {
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.loadingSpinner.setVisibility(View.VISIBLE);
            } else {
                binding.loadingSpinner.setVisibility(View.GONE);
            }
        });

        viewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.createProductButton.setOnClickListener(v -> {
            // TODO: navigate to product form fragment
        });
    }

}