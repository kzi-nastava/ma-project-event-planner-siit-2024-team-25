package com.team25.event.planner.communication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team25.event.planner.R;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.databinding.NotificationCardBinding;

import java.util.List;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationViewHolder> {
    private final List<Notification> _notifications;
    private final NotificationsListAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Notification notification);
        void onStatusIconClick(Notification notification, int position);
    }

    public NotificationsListAdapter(List<Notification> notifications, NotificationsListAdapter.OnItemClickListener onItemClickListener) {
        this._notifications = notifications;
        this.onItemClickListener = onItemClickListener;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final NotificationCardBinding binding;

        public NotificationViewHolder(@NonNull NotificationCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Notification notification, NotificationsListAdapter.OnItemClickListener listener, int position) {
            binding.setNotification(notification);
            binding.executePendingBindings();

            if (notification.getIsViewed()) {
                binding.notificationStatus.setImageResource(R.drawable.ic_notification_read);
            } else {
                binding.notificationStatus.setImageResource(R.drawable.ic_notification_unread);
            }

            binding.notificationStatus.setOnClickListener(v -> {
                listener.onStatusIconClick(notification, position);
            });

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(notification));
        }
    }

    @NonNull
    @Override
    public NotificationsListAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NotificationCardBinding binding = NotificationCardBinding.inflate(inflater, parent, false);
        return new NotificationsListAdapter.NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsListAdapter.NotificationViewHolder holder, int position) {
        final Notification notification = _notifications.get(position);
        holder.bind(notification, onItemClickListener, position);
    }

    @Override
    public int getItemCount() {
        return _notifications != null ? _notifications.size() : 0;
    }

    public void addNotification(List<Notification> newNotifications) {
        int oldSize = _notifications.size();
        _notifications.addAll(newNotifications);
        notifyItemRangeInserted(oldSize, newNotifications.size());
    }
}
