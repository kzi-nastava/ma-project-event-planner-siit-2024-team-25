package com.team25.event.planner.event.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.databinding.ItemEventTypeBinding;
import com.team25.event.planner.event.model.EventType;

import java.util.List;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeViewHolder> {

    private List<EventType> eventTypes;

    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(EventType eventType);
    }

    public EventTypeAdapter(List<EventType> eventTypes, OnItemClickListener onItemClickListener) {
        this.eventTypes = eventTypes;
        this.onItemClickListener = onItemClickListener;
    }

    public static class EventTypeViewHolder extends RecyclerView.ViewHolder {
        private final ItemEventTypeBinding binding;

        public EventTypeViewHolder(@NonNull ItemEventTypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(EventType eventType, OnItemClickListener listener) {
            binding.setEventType(eventType);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(eventType));
        }
    }

    @NonNull
    @Override
    public EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEventTypeBinding binding = ItemEventTypeBinding.inflate(inflater, parent, false);
        return new EventTypeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeViewHolder holder, int position) {
        EventType eventType = eventTypes.get(position);
        holder.bind(eventType, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return eventTypes != null ? eventTypes.size() : 0;
    }

    public void updateEventTypes(List<EventType> newEventTypes) {
        this.eventTypes = newEventTypes;
        notifyDataSetChanged();
    }
}
