package com.team25.event.planner.event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.databinding.ItemActivityBinding;
import com.team25.event.planner.event.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Activity> activities;
    private final OnRemoveClickListener onRemoveClickListener;
    private final boolean isEditable;

    public interface OnRemoveClickListener {
        void onRemove(Activity activity);
    }

    public ActivityAdapter(List<Activity> activities, OnRemoveClickListener onRemoveClickListener, boolean isEditable) {
        this.activities = activities;
        this.onRemoveClickListener = onRemoveClickListener;
        this.isEditable = isEditable;
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        private final ItemActivityBinding binding;

        public ActivityViewHolder(@NonNull ItemActivityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Activity activity, OnRemoveClickListener listener, boolean isEditable) {
            binding.setActivity(activity);
            if (!isEditable) {
                binding.btnRemove.setVisibility(View.GONE);
            }
            binding.executePendingBindings();
            binding.btnRemove.setOnClickListener(v -> listener.onRemove(activity));
        }
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemActivityBinding binding = ItemActivityBinding.inflate(inflater, parent, false);
        return new ActivityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        final Activity activity = activities.get(position);
        holder.bind(activity, onRemoveClickListener, isEditable);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public void refreshActivities(@NonNull List<Activity> newActivities) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return activities.size();
            }

            @Override
            public int getNewListSize() {
                return newActivities.size();
            }

            @Override
            public boolean areItemsTheSame(int oldPos, int newPos) {
                return activities.get(oldPos).getId().equals(newActivities.get(newPos).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                return activities.get(oldPos).equals(newActivities.get(newPos));
            }
        });

        this.activities = new ArrayList<>(newActivities);
        diffResult.dispatchUpdatesTo(this);
    }
}
