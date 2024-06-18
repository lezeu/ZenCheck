package com.example.watchapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.watchapp.R;

public class ForegroundService extends Service {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    protected Notification buildForegroundNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.foreground)
                .setContentTitle("Service Running")
                .setContentText("This service is running in the background")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    protected void createNotificationChannel() {
        CharSequence name = "Foreground";
        String description = "Foreground description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
