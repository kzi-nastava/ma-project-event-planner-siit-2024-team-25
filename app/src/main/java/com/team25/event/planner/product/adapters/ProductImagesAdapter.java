package com.team25.event.planner.product.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.ItemProductImageBinding;
import com.team25.event.planner.product.model.ProductImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ImageViewHolder> {

    private List<ProductImage> images;
    private final OnImageDeleteListener onDeleteListener;

    public interface OnImageDeleteListener {
        void onDeleteNewImage(File image);

        void onDeleteExistingImage(String url);
    }

    public ProductImagesAdapter(List<ProductImage> images, OnImageDeleteListener onDeleteListener) {
        this.images = images;
        this.onDeleteListener = onDeleteListener;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductImageBinding binding;

        ImageViewHolder(@NonNull ItemProductImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProductImage image, OnImageDeleteListener onDeleteListener) {
            Object imageSource = image.isExisting() ? image.getExistingImageUrl() : image.getNewImage();
            Glide.with(itemView.getContext())
                    .load(imageSource)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(binding.productImage);
            binding.deleteButton.setOnClickListener(v -> {
                if (image.isExisting()) {
                    onDeleteListener.onDeleteExistingImage(image.getExistingImageUrl());
                } else {
                    onDeleteListener.onDeleteNewImage(image.getNewImage());
                }
            });
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
        final ProductImage image = images.get(position);
        holder.bind(image, onDeleteListener);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void refreshImages(@NonNull List<ProductImage> newImages) {
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
                final ProductImage oldImage = images.get(oldPos);
                final ProductImage newImage = newImages.get(newPos);
                if (oldImage.isExisting() && newImage.isExisting()) {
                    return oldImage.getExistingImageUrl().equals(newImage.getExistingImageUrl());
                } else if (!oldImage.isExisting() && !newImage.isExisting()) {
                    return oldImage.getNewImage().getAbsolutePath().equals(newImage.getNewImage().getAbsolutePath());
                }
                return false;
            }

            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                final ProductImage oldImage = images.get(oldPos);
                final ProductImage newImage = newImages.get(newPos);
                if (oldImage.isExisting() && newImage.isExisting()) {
                    return oldImage.getExistingImageUrl().equals(newImage.getExistingImageUrl());
                } else if (!oldImage.isExisting() && !newImage.isExisting()) {
                    return oldImage.getNewImage().getAbsolutePath().equals(newImage.getNewImage().getAbsolutePath())
                            && oldImage.getNewImage().lastModified() == newImage.getNewImage().lastModified();
                }
                return false;
            }
        });

        this.images = new ArrayList<>(newImages);
        diffResult.dispatchUpdatesTo(this);
    }
}