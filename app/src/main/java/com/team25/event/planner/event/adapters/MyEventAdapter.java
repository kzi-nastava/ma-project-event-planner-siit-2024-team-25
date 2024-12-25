package com.team25.event.planner.event.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.databinding.ItemMyEventBinding;
import com.team25.event.planner.event.model.EventCard;

import java.util.List;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.EventViewHolder> {
    private final List<EventCard> events;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(EventCard event);
    }

    public MyEventAdapter(List<EventCard> events, OnItemClickListener onItemClickListener) {
        this.events = events;
        this.onItemClickListener = onItemClickListener;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private final ItemMyEventBinding binding;

        public EventViewHolder(@NonNull ItemMyEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(EventCard event, OnItemClickListener listener) {
            binding.setEvent(event);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(event));
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMyEventBinding binding = ItemMyEventBinding.inflate(inflater, parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        final EventCard event = events.get(position);
        holder.bind(event, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public void addEvents(List<EventCard> newEvents) {
        int oldSize = events.size();
        events.addAll(newEvents);
        notifyItemRangeInserted(oldSize, newEvents.size());
    }
}
