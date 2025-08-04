package com.team25.event.planner.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.event.fragments.ProductPurchaseListFragment;
import com.team25.event.planner.offering.dialogs.YesOrNoDialogFragment;
import com.team25.event.planner.product.adapters.MyProductsAdapter;
import com.team25.event.planner.product.model.MyProductCard;
import com.team25.event.planner.product.viewmodel.MyProductsViewModel;

import java.util.ArrayList;

public class MyProductsFragment extends Fragment {
    private FragmentMyProductsBinding binding;
    private MyProductsViewModel viewModel;
    private NavController navController;
    private MyProductsAdapter productsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_products, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(MyProductsViewModel.class);
        binding.setViewModel(viewModel);

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupProductList();
        setupObservers();
        setupListeners();

        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            viewModel.setOwnerId(user == null ? null : user.getId());
            viewModel.loadNextPage();
        });

        viewModel.loadEventTypes();
        viewModel.loadOfferingCategories();

        return binding.getRoot();
    }

    private void setupProductList() {
        productsAdapter = new MyProductsAdapter(
                new ArrayList<>(),
                product -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong(ProductPurchaseListFragment.PRODUCT_ID_ARG, product.getId());
                    bundle.putLong(EventArgumentNames.ID_ARG, 0L);
                    navController.navigate(R.id.action_myProductsFragment_to_productDetailsFragment, bundle);
                },
                product -> {
                    Bundle args = new Bundle();
                    args.putLong(ProductFormFragment.ID_ARG_NAME, product.getId());
                    navController.navigate(R.id.action_myProductsFragment_to_productFormFragment, args);
                },
                this::openDeleteDialog
        );
        binding.recyclerViewProducts.setAdapter(productsAdapter);

        viewModel.products.observe(getViewLifecycleOwner(), productsAdapter::addProducts);

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

    private void reloadProducts() {
        productsAdapter.clearProducts();
        viewModel.reload();
    }

    private void openDeleteDialog(MyProductCard product) {
        YesOrNoDialogFragment dialog = new YesOrNoDialogFragment(new YesOrNoDialogFragment.ConfirmDialogListener() {
            @Override
            public void onConfirm() {
                viewModel.deleteProduct(product.getId());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void refresh() {
                reloadProducts();
            }
        }, product.getName());

        dialog.show(getParentFragmentManager(), "ConfirmDialogFragment");
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

        viewModel.refreshSignal.observe(getViewLifecycleOwner(), succeeded -> {
            if (succeeded) {
                Toast.makeText(getContext(), "Successfully changed the password!", Toast.LENGTH_SHORT).show();
                navController.popBackStack();
                viewModel.onRefreshHandleComplete();
            }
        });
    }

    private void setupListeners() {
        binding.searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.productFilter.name.setValue(query);
                reloadProducts();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    viewModel.productFilter.name.setValue(null);
                    reloadProducts();
                    return true;
                }
                return false;
            }
        });

        binding.filterButton.setOnClickListener(v -> {
            ProductFilterFragment filterBottomSheet = new ProductFilterFragment(viewModel, this::reloadProducts);
            filterBottomSheet.show(getParentFragmentManager(), "ProductFilterFragment");
        });

        binding.createProductButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putLong(ProductFormFragment.ID_ARG_NAME, -1);
            navController.navigate(R.id.action_myProductsFragment_to_productFormFragment, args);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadProducts();
    }
}