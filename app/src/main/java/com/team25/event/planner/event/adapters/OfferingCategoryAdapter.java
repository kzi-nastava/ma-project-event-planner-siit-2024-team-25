package com.team25.event.planner.event.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.databinding.ItemOfferingCategoryPreviewBinding;
import com.team25.event.planner.event.model.OfferingCategoryPreviewDTO;

import java.util.List;

public class OfferingCategoryAdapter extends RecyclerView.Adapter<OfferingCategoryAdapter.CategoryViewHolder> {

    private List<OfferingCategoryPreviewDTO> categories;
    private List<Long> selectedCategoryIds;
    private final OnSelectionChangeListener selectionChangeListener;

    public interface OnSelectionChangeListener {
        void onSelectionChange(List<Long> selectedIds);
    }

    public OfferingCategoryAdapter(List<OfferingCategoryPreviewDTO> categories, List<Long> selectedCategoryIds, OnSelectionChangeListener listener) {
        this.categories = categories;
        this.selectedCategoryIds = selectedCategoryIds;
        this.selectionChangeListener = listener;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemOfferingCategoryPreviewBinding binding;

        public CategoryViewHolder(@NonNull ItemOfferingCategoryPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OfferingCategoryPreviewDTO category, boolean isSelected, OnCategorySelectedListener listener) {
            binding.setCategory(category);
            binding.setIsSelected(isSelected);
            binding.checkboxCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listener.onCategorySelected(category, isChecked);
            });
            binding.executePendingBindings();
        }
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(OfferingCategoryPreviewDTO category, boolean isSelected);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOfferingCategoryPreviewBinding binding = ItemOfferingCategoryPreviewBinding.inflate(inflater, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        OfferingCategoryPreviewDTO category = categories.get(position);
        boolean isSelected = selectedCategoryIds.contains(category.getId());
        holder.bind(category, isSelected, (selectedCategory, isSelectedNow) -> {
            if (isSelectedNow) {
                selectedCategoryIds.add(selectedCategory.getId());
            } else {
                selectedCategoryIds.remove(selectedCategory.getId());
            }
            selectionChangeListener.onSelectionChange(selectedCategoryIds);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateCategories(List<OfferingCategoryPreviewDTO> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void updateSelectedCategoryIds(List<Long> selectedCategoryIds) {
        this.selectedCategoryIds = selectedCategoryIds;
    }
}
