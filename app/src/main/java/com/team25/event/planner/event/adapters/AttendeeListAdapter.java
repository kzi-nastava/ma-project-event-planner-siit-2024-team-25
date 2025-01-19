package com.team25.event.planner.event.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.databinding.ItemAttendeeBinding;
import com.team25.event.planner.event.model.Attendee;

import java.util.List;

public class AttendeeListAdapter extends RecyclerView.Adapter<AttendeeListAdapter.ViewHolder> {
    private final List<Attendee> attendees;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Attendee attendee);
    }

    public AttendeeListAdapter(List<Attendee> attendees, OnItemClickListener onItemClickListener) {
        this.attendees = attendees;
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAttendeeBinding binding;

        public ViewHolder(@NonNull ItemAttendeeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Attendee attendee, OnItemClickListener listener) {
            binding.setAttendee(attendee);
            binding.executePendingBindings();

            final String profilePicUrl = ConnectionParams.BASE_URL + "api/users/" + attendee.getUserId() + "/profile-picture";
            Glide.with(itemView.getContext())
                    .load(profilePicUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.ivProfilePic);

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(attendee));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAttendeeBinding binding = ItemAttendeeBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Attendee attendee = attendees.get(position);
        holder.bind(attendee, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    public void addAttendees(List<Attendee> newAttendees) {
        int oldSize = attendees.size();
        attendees.addAll(newAttendees);
        notifyItemRangeInserted(oldSize, newAttendees.size());
    }
}
