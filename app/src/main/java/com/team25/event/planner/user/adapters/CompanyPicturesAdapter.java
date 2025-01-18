package com.team25.event.planner.user.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;

public class CompanyPicturesAdapter extends ListAdapter<String, CompanyPicturesAdapter.ViewHolder> {
    private final Context context;
    private final Long userId;

    public CompanyPicturesAdapter(Context context, Long userId) {
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShapeableImageView imageView = new ShapeableImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Set fixed size for gallery images
        int size = context.getResources().getDimensionPixelSize(R.dimen.company_picture_size);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));

        // Add margin between items
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(size, size);
        layoutParams.setMargins(8, 0, 8, 0);
        imageView.setLayoutParams(layoutParams);

        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pictureId = getItem(position);
        String pictureUrl = ConnectionParams.BASE_URL + "api/users/" + userId + "/pictures/" + pictureId;

        Glide.with(context)
                .load(pictureUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(holder.imageView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ShapeableImageView imageView;

        ViewHolder(@NonNull ShapeableImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }
}