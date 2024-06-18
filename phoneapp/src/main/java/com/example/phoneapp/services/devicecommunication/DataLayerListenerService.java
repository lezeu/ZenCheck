package com.example.phoneapp.services.devicecommunication;

import static com.example.phoneapp.utils.Constants.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.phoneapp.R;
import com.example.phoneapp.utils.Constants;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.HashMap;
import java.util.Map;

public class DataLayerListenerService extends WearableListenerService implements DataClient.OnDataChangedListener {
    private final Map<String, DataHandler> dataHandlerMap = new HashMap<>();

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        Notification notification = buildForegroundNotification();
        startForeground(1, notification);

        DataClient dataClient = Wearable.getDataClient(this);
        dataClient.addListener(this);

        registerDataHandlers();
    }

    private void registerDataHandlers() {
        dataHandlerMap.put("/sensor-data/demand/bpm", new BpmDataHandler.DemandDataHandler());
        dataHandlerMap.put("/sensor-data/schedule/bpm", new BpmDataHandler.ScheduledDataHandler());
        dataHandlerMap.put("/sensor-data/scheduled/stress", new StressDataHandler.ScheduledDataHandler());
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            String path = event.getDataItem().getUri().getPath();

            if (event.getType() == DataEvent.TYPE_CHANGED && dataHandlerMap.containsKey(path)) {
                dataHandlerMap.get(path).handleData(event);
            } else {
                Log.w(Constants.TAG, "No handler registered for path: " + path);
            }
        }
    }

    private Notification buildForegroundNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Service Running")
                .setContentText("This service is running in the background")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    private void createNotificationChannel() {
        CharSequence name = "Foreground";
        String description = "Foreground description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}