package com.example.watchapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class BpmMeasurementPeriodicallyService extends Service implements SensorEventListener {

    private static final String TAG = "ManualDebug";
    private static final int MEASUREMENT_INTERVAL = 10000;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private long time;
    private List<Float> bpmValues = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        time = System.currentTimeMillis();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float bpm = event.values[0];
            if (bpm > 0) bpmValues.add(bpm);
            long currentTime = System.currentTimeMillis();

            if (currentTime - time >= MEASUREMENT_INTERVAL) {
                sendBpmToPhone();
                bpmValues.clear();
                time = currentTime;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if necessary
    }

    private void sendBpmToPhone() {
        if (bpmValues.isEmpty()) {
            return;
        }

        float bpmAverage = getAverageBpm(bpmValues);
        float bpmHigh = getHighestBpm(bpmValues);
        float bpmLow = getLowestBpm(bpmValues);

        DataClient dataClient = Wearable.getDataClient(this);
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/sensor-data/schedule/bpm");

        putDataMapRequest.getDataMap().putFloat("bpmAverage", bpmAverage);
        putDataMapRequest.getDataMap().putFloat("bpmHigh", bpmHigh);
        putDataMapRequest.getDataMap().putFloat("bpmLow", bpmLow);
        putDataMapRequest.getDataMap().putLong("timestamp", System.currentTimeMillis());

        Log.d(TAG, "Data sent: " + putDataMapRequest);
        dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnFailureListener(e -> Log.d(TAG, "Failed to send bpm to phone"))
                .addOnSuccessListener(e -> Log.d(TAG, "Successfully sent bpm to phone "
                        + bpmAverage + " " + bpmHigh + " " + bpmLow));
    }

    private Float getAverageBpm(List<Float> values) {
        float sumOfValues = 0;
        for (Float value : values) {
            sumOfValues += value;
        }
        return sumOfValues / values.size();
    }

    private Float getHighestBpm(List<Float> values) {
        float highestBpm = values.get(0);
        for (Float value : values) {
            if (value > highestBpm) {
                highestBpm = value;
            }
        }
        return highestBpm;
    }

    private Float getLowestBpm(List<Float> values) {
        float lowestBpm = values.get(0);
        for (Float value : values) {
            if (value < lowestBpm) {
                lowestBpm = value;
            }
        }
        return lowestBpm;
    }
}
