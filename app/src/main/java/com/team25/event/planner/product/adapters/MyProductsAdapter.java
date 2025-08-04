package com.team25.event.planner.product.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.ItemMyProductBinding;
import com.team25.event.planner.product.model.MyProductCard;

import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ProductViewHolder> {
    private final List<MyProductCard> products;
    private final MyProductsAdapter.OnItemClickListener onItemClickListener;
    private final MyProductsAdapter.OnItemClickListener onEditClickListener;
    private final MyProductsAdapter.OnItemClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(MyProductCard product);
    }

    public MyProductsAdapter(List<MyProductCard> products, MyProductsAdapter
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
                MyProductCard product,
                MyProductsAdapter.OnItemClickListener onClickListener,
                MyProductsAdapter.OnItemClickListener onEditListener,
                MyProductsAdapter.OnItemClickListener onDeleteListener
        ) {
            binding.setProduct(product);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(v -> onClickListener.onItemClick(product));
            binding.editButton.setOnClickListener(v -> onEditListener.onItemClick(product));
            binding.deleteButton.setOnClickListener(v -> onDeleteListener.onItemClick(product));
            Glide.with(itemView.getContext())
                    .load(product.getThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(binding.productImage);
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
        final MyProductCard product = products.get(position);
        holder.bind(product, onItemClickListener, onEditClickListener, onDeleteClickListener);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public void addProducts(List<MyProductCard> newProducts) {
        int oldSize = products.size();
        products.addAll(newProducts);
        notifyItemRangeInserted(oldSize, newProducts.size());
    }

    public void clearProducts() {
        int oldSize = products.size();
        products.clear();
        notifyItemRangeRemoved(0, oldSize);
    }
}
