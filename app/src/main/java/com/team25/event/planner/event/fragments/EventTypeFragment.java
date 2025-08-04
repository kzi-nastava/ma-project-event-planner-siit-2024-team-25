package com.team25.event.planner.event.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentEventTypeBinding;
import com.team25.event.planner.event.adapters.OfferingCategoryAdapter;
import com.team25.event.planner.event.viewmodel.EventTypeViewModel;

public class EventTypeFragment extends Fragment {
    public static final String ID_ARG_NAME = "eventTypeId";

    private FragmentEventTypeBinding binding;
    private EventTypeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_type, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);
        binding.setViewModel(viewModel);

        if (getArguments() != null) {
            long eventTypeId = getArguments().getLong(ID_ARG_NAME);
            if (eventTypeId != -1) {
                viewModel.setEventTypeId(eventTypeId);
            }
        }

        setUpObservers();

        viewModel.fetchOfferingCategories();

        return binding.getRoot();
    }

    private void setUpObservers() {
        viewModel.isEditing.observe(getViewLifecycleOwner(), isEditing -> {
            binding.textInputLayoutName.setEnabled(!isEditing);
            if (isEditing) {
                binding.textViewEventTypeTitle.setText(R.string.edit_event_type);
            } else {
                binding.textViewEventTypeTitle.setText(R.string.add_event_type);
            }
        });

        OfferingCategoryAdapter adapter = new OfferingCategoryAdapter(
                viewModel.offeringCategories.getValue(),
                viewModel.selectedCategoryIds.getValue(),
                selectedIds -> viewModel.selectedCategoryIds.setValue(selectedIds)
        );

        RecyclerView recyclerView = binding.recyclerViewCategories;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.offeringCategories.observe(getViewLifecycleOwner(), adapter::updateCategories);
        viewModel.selectedCategoryIds.observe(getViewLifecycleOwner(), adapter::updateSelectedCategoryIds);

        viewModel.successSignal.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Event type save successfully", Toast.LENGTH_SHORT).show();
                viewModel.successSignal.setValue(false);
            }
        });
    }
}