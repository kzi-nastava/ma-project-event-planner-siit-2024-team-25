package com.team25.event.planner.product.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.ItemProductImageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ImageViewHolder> {

    private List<File> images;
    private final OnImageDeleteListener onDeleteListener;

    public interface OnImageDeleteListener {
        void onDelete(File image);
    }

    public ProductImagesAdapter(List<File> images, OnImageDeleteListener onDeleteListener) {
        this.images = images;
        this.onDeleteListener = onDeleteListener;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductImageBinding binding;

        ImageViewHolder(@NonNull ItemProductImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(File image, OnImageDeleteListener onDeleteListener) {
            Glide.with(itemView.getContext())
                    .load(image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(binding.productImage);

            binding.deleteButton.setOnClickListener(v -> onDeleteListener.onDelete(image));
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProductImageBinding binding = ItemProductImageBinding.inflate(inflater, parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final File image = images.get(position);
        holder.bind(image, onDeleteListener);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void refreshImages(@NonNull List<File> newImages) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return images.size();
            }

            @Override
            public int getNewListSize() {
                return newImages.size();
            }

            @Override
            public boolean areItemsTheSame(int oldPos, int newPos) {
                return images.get(oldPos).getAbsolutePath().equals(newImages.get(newPos).getAbsolutePath());
            }

            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                return images.get(oldPos).getAbsolutePath().equals(newImages.get(newPos).getAbsolutePath())
                        && images.get(oldPos).lastModified() == newImages.get(newPos).lastModified();
            }
        });

        this.images = new ArrayList<>(newImages);
        diffResult.dispatchUpdatesTo(this);
    }
}