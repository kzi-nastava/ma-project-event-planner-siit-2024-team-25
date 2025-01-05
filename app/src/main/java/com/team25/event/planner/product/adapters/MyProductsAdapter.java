package com.team25.event.planner.product.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.databinding.ItemMyProductBinding;
import com.team25.event.planner.offering.model.OfferingCard;

import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ProductViewHolder> {
    private final List<OfferingCard> products;
    private final MyProductsAdapter.OnItemClickListener onItemClickListener;
    private final MyProductsAdapter.OnItemClickListener onEditClickListener;
    private final MyProductsAdapter.OnItemClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(OfferingCard product);
    }

    public MyProductsAdapter(List<OfferingCard> products, MyProductsAdapter
            .OnItemClickListener onItemClickListener, OnItemClickListener onEditClickListener, OnItemClickListener onDeleteClickListener) {
        this.products = products;
        this.onItemClickListener = onItemClickListener;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemMyProductBinding binding;

        public ProductViewHolder(@NonNull ItemMyProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(
                OfferingCard product,
                MyProductsAdapter.OnItemClickListener onClickListener,
                MyProductsAdapter.OnItemClickListener onEditListener,
                MyProductsAdapter.OnItemClickListener onDeleteListener
        ) {
            binding.setProduct(product);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(v -> onClickListener.onItemClick(product));
            binding.editButton.setOnClickListener(v -> onEditListener.onItemClick(product));
            binding.deleteButton.setOnClickListener(v -> onDeleteListener.onItemClick(product));
        }
    }

    @NonNull
    @Override
    public MyProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMyProductBinding binding = ItemMyProductBinding.inflate(inflater, parent, false);
        return new MyProductsAdapter.ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsAdapter.ProductViewHolder holder, int position) {
        final OfferingCard product = products.get(position);
        holder.bind(product, onItemClickListener, onEditClickListener, onDeleteClickListener);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public void addProducts(List<OfferingCard> newProducts) {
        int oldSize = products.size();
        products.addAll(newProducts);
        notifyItemRangeInserted(oldSize, newProducts.size());
    }
}
