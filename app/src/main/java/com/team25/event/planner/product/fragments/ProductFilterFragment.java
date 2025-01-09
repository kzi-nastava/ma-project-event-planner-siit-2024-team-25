package com.team25.event.planner.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentProductFilterBinding;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.product.viewmodel.MyProductsViewModel;

import java.util.ArrayList;

public class ProductFilterFragment extends BottomSheetDialogFragment {
    private FragmentProductFilterBinding binding;
    private final MyProductsViewModel viewModel;
    private final ReloadCallback reloadCallback;

    public interface ReloadCallback {
        void reload();
    }

    public ProductFilterFragment(MyProductsViewModel viewModel, ReloadCallback reloadCallback) {
        this.viewModel = viewModel;
        this.reloadCallback = reloadCallback;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_filter, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.setViewModel(viewModel);

        setupDropdowns();
        setupListeners();

        return binding.getRoot();
    }

    private void setupDropdowns() {
        // Setup Event Type dropdown
        ArrayAdapter<EventType> eventTypeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>()
        );
        binding.eventTypeDropdown.setAdapter(eventTypeAdapter);
        binding.eventTypeDropdown.setOnItemClickListener((parent, view, position, id) -> {
            EventType selectedType = eventTypeAdapter.getItem(position);
            if (selectedType == null) {
                viewModel.productFilter.getEventTypeId().setValue(null);
            } else {
                viewModel.productFilter.getEventTypeId().setValue(selectedType.getId());
            }
        });

        // Setup Category dropdown
        ArrayAdapter<OfferingCategory> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>()
        );
        binding.categoryDropdown.setAdapter(categoryAdapter);
        binding.categoryDropdown.setOnItemClickListener((parent, view, position, id) -> {
            OfferingCategory selectedCategory = categoryAdapter.getItem(position);
            if (selectedCategory == null) {
                viewModel.productFilter.getCategoryId().setValue(null);
            } else {
                viewModel.productFilter.getCategoryId().setValue(selectedCategory.getId());
            }
        });

        // Update dropdowns when data changes
        viewModel.eventTypes.observe(getViewLifecycleOwner(), eventTypes -> {
            eventTypeAdapter.clear();
            eventTypeAdapter.addAll(eventTypes);
            eventTypeAdapter.notifyDataSetChanged();

            // Pre-select the current value if set
            Long selectedEventTypeId = viewModel.productFilter.getEventTypeId().getValue();
            if (selectedEventTypeId != null) {
                for (EventType eventType : eventTypes) {
                    if (eventType.getId().equals(selectedEventTypeId)) {
                        binding.eventTypeDropdown.setText(eventType.toString(), false);
                        break;
                    }
                }
            }
        });

        viewModel.offeringCategories.observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.clear();
            categoryAdapter.addAll(categories);
            eventTypeAdapter.notifyDataSetChanged();

            // Pre-select the current value if set
            Long selectedCategoryId = viewModel.productFilter.getCategoryId().getValue();
            if (selectedCategoryId != null) {
                for (OfferingCategory category : categories) {
                    if (category.getId().equals(selectedCategoryId)) {
                        binding.categoryDropdown.setText(category.toString(), false);
                        break;
                    }
                }
            }
        });
    }

    private void setupListeners() {
        binding.cancelButton.setOnClickListener(v -> dismiss());

        binding.applyButton.setOnClickListener(v -> {
            dismiss();
            reloadCallback.reload();
        });

        binding.clearFiltersButton.setOnClickListener(v -> {
            viewModel.productFilter.getEventTypeId().setValue(null);
            viewModel.productFilter.getCategoryId().setValue(null);
            viewModel.productFilter.getMinPrice().setValue(null);
            viewModel.productFilter.getMaxPrice().setValue(null);
            binding.eventTypeDropdown.setText(null);
            binding.categoryDropdown.setText(null);
        });
    }
}