package com.team25.event.planner.review.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.adapters.NotificationsListAdapter;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.databinding.NotificationCardBinding;
import com.team25.event.planner.databinding.ReviewCardBinding;
import com.team25.event.planner.review.model.ReviewCard;

import java.util.List;

public class ReviewCardAdapter extends RecyclerView.Adapter<ReviewCardAdapter.ReviewViewHolder> {
    private final List<ReviewCard> _reviews;
    private final ReviewCardAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ReviewCard reviewCard);
    }

    public ReviewCardAdapter(List<ReviewCard> reviews, ReviewCardAdapter.OnItemClickListener onItemClickListener) {
        this._reviews = reviews;
        this.onItemClickListener = onItemClickListener;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ReviewCardBinding binding;

        public ReviewViewHolder(@NonNull ReviewCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ReviewCard reviewCard, ReviewCardAdapter.OnItemClickListener listener, int position) {
            binding.setReview(reviewCard);
            binding.executePendingBindings();

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(reviewCard));
        }
    }

    @NonNull
    @Override
    public ReviewCardAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReviewCardBinding binding = ReviewCardBinding.inflate(inflater, parent, false);
        return new ReviewCardAdapter.ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewCardAdapter.ReviewViewHolder holder, int position) {
        final ReviewCard notification = _reviews.get(position);
        holder.bind(notification, onItemClickListener, position);
    }

    @Override
    public int getItemCount() {
        return _reviews != null ? _reviews.size() : 0;
    }

    public void addReview(List<ReviewCard> newReviews) {
        int oldSize = _reviews.size();
        _reviews.addAll(newReviews);
        notifyItemRangeInserted(oldSize, newReviews.size());
    }
}
