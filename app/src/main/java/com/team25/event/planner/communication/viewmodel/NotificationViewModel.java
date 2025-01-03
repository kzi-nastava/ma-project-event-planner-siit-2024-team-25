package com.team25.event.planner.communication.viewmodel;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.GsonBuilder;
import com.team25.event.planner.R;
import com.team25.event.planner.communication.model.Notification;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.team25.event.planner.communication.model.NotificationCategory;
import com.team25.event.planner.core.api.serialization.LocalDateTimeAdapter;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.event.fragments.EventDetailsFragment;
import com.team25.event.planner.offering.fragments.OfferingCategoryBaseFragment;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.product_service.fragments.OwnerHomePage;
import com.team25.event.planner.product_service.fragments.ServiceDetailsFragment;
import com.team25.event.planner.user.model.User;
import com.team25.event.planner.user.model.UserRole;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationViewModel extends ViewModel {
    private FragmentActivity _fragmentActivity;
    private NotificationWebSocket _notificationWebSocket;

    public NotificationViewModel(FragmentActivity fragmentActivity){
        this._fragmentActivity = fragmentActivity;
    }
    private UserRole _currentUserRole;

    @SuppressLint("CheckResult")
    public void connectToSocket(User currentUser) {
        _currentUserRole = currentUser.getUserRole();
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        _notificationWebSocket = new NotificationWebSocket();
        _notificationWebSocket.connect();
        _notificationWebSocket.subscribeToTopic("/notifications/user/"+currentUser.getUserId())
            .subscribe(stompMessage -> {
                    Notification notification = gson.fromJson(stompMessage.getPayload(), Notification.class);
                    this.showNotification(notification);
                }, throwable -> {
                    Log.e("Tag", throwable.getMessage());
                });
    }

    private void showNotification(Notification notification) {
        NotificationManager notificationManager = (NotificationManager) _fragmentActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "notifications_channel";
            CharSequence channelName = "Event Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = getIntent(notification);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                _fragmentActivity,
                (int) (System.currentTimeMillis() % Integer.MAX_VALUE),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        android.app.Notification.Builder builder = new android.app.Notification.Builder(_fragmentActivity, "notifications_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getMessage())
                .setAutoCancel(true)
                .setPriority(android.app.Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        notificationManager.notify((int) (System.currentTimeMillis() % Integer.MAX_VALUE), builder.build());
    }

    @NonNull
    private Intent getIntent(Notification notification) {
        Intent intent = new Intent(_fragmentActivity, _fragmentActivity.getClass());
        intent.putExtra("notification_category", NotificationCategory.EVENT);
        if(notification.getNotificationCategory().equals(NotificationCategory.OFFERING_CATEGORY)){
            intent = new Intent(_fragmentActivity, _fragmentActivity.getClass());
            intent.putExtra("notification_category", NotificationCategory.OFFERING_CATEGORY);
        }else if(notification.getNotificationCategory().equals(NotificationCategory.PRODUCT)){
            intent = new Intent(_fragmentActivity, _fragmentActivity.getClass());
            intent.putExtra("notification_category", NotificationCategory.PRODUCT);
        }else if(notification.getNotificationCategory().equals(NotificationCategory.SERVICE)){
            intent = new Intent(_fragmentActivity, _fragmentActivity.getClass());
            intent.putExtra("notification_category", NotificationCategory.SERVICE);

        }

        intent.putExtra(EventArgumentNames.ID_ARG, notification.getEntityId());
        intent.putExtra("user_role", _currentUserRole);

        return intent;
    }

}
