package com.team25.event.planner.service.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.ItemServiceImageBinding;
import com.team25.event.planner.service.model.ServiceImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServiceImageAdapter extends RecyclerView.Adapter<ServiceImageAdapter.ImageViewHolder> {

    private List<ServiceImage> images;
    private final OnImageDeleteListener onDeleteListener;

    public interface OnImageDeleteListener {
        void onDeleteNewImage(File image);
        void onDeleteExistingImage(String url);
    }

    public ServiceImageAdapter(List<ServiceImage> images, OnImageDeleteListener onDeleteListener) {
        this.images = images;
        this.onDeleteListener = onDeleteListener;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemServiceImageBinding binding;

        ImageViewHolder(@NonNull ItemServiceImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ServiceImage image, OnImageDeleteListener onDeleteListener) {
            Object imageSource = image.isExisting() ? image.getExistingImageUrl() : image.getNewImage();
            Glide.with(itemView.getContext())
                    .load(imageSource)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(binding.serviceImage);
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
        ItemServiceImageBinding binding = ItemServiceImageBinding.inflate(inflater, parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final ServiceImage image = images.get(position);
        holder.bind(image, onDeleteListener);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void refreshImages(@NonNull List<ServiceImage> newImages) {
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
                final ServiceImage oldImage = images.get(oldPos);
                final ServiceImage newImage = newImages.get(newPos);
                if (oldImage.isExisting() && newImage.isExisting()) {
                    return oldImage.getExistingImageUrl().equals(newImage.getExistingImageUrl());
                } else if (!oldImage.isExisting() && !newImage.isExisting()) {
                    return oldImage.getNewImage().getAbsolutePath().equals(newImage.getNewImage().getAbsolutePath());
                }
                return false;
            }

            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                final ServiceImage oldImage = images.get(oldPos);
                final ServiceImage newImage = newImages.get(newPos);
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
