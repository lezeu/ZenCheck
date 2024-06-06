package com.example.watchapp.services;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class BpmMeasurementPeriodicallyService extends ForegroundService implements SensorEventListener {

    private static final String TAG = "ManualDebug";
    private static final Long MEASUREMENT_DURATION = 15 * 1000L;
    private static final Long MEASUREMENT_INTERVAL = 5 * 60 * 1000L;
    private Context context;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private List<Float> bpmValues = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        createNotificationChannel();
        startForeground(1, buildForegroundNotification());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        handler.postDelayed(bpmAssessRunnabled, MEASUREMENT_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager.registerListener(
                this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if necessary
    }

    private Runnable measurementRunnable = () -> sensorManager.unregisterListener(
            BpmMeasurementPeriodicallyService.this, heartRateSensor);

    private Runnable bpmAssessRunnabled = new Runnable() {

        @Override
        public void run() {
            sensorManager.registerListener(BpmMeasurementPeriodicallyService.this,
                    heartRateSensor, SensorManager.
                            SENSOR_DELAY_UI);
            handler.postDelayed(measurementRunnable, MEASUREMENT_DURATION);

            handler.postDelayed(() -> {
                sendBpmToPhone(
                        getAverageBpm(bpmValues), getHighestBpm(bpmValues), getLowestBpm(bpmValues));

                bpmValues.clear();
                handler.postDelayed(bpmAssessRunnabled, MEASUREMENT_INTERVAL);
            }, MEASUREMENT_INTERVAL);
        }

        private void sendBpmToPhone(float bpmAverage, float bpmHigh, float bpmLow) {
            DataClient dataClient = Wearable.getDataClient(context);
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/sensor-data/schedule/bpm");

            putDataMapRequest.getDataMap().putFloat("bpmAverage", bpmAverage);
            putDataMapRequest.getDataMap().putFloat("bpmHigh", bpmHigh);
            putDataMapRequest.getDataMap().putFloat("bpmLow", bpmLow);
            putDataMapRequest.getDataMap().putLong("timestamp", System.currentTimeMillis());
            Log.d(TAG, "BPM: Sent data to phone: " + bpmAverage + " " + bpmHigh + " " + bpmLow + " " + bpmHigh);

            dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                    .addOnFailureListener(e -> Log.d(TAG, "Failed to send bpm to phone"))
                    .addOnSuccessListener(e -> Log.d(TAG, "Successfully sent bpm to phone "
                            + bpmAverage + " " + bpmHigh + " " + bpmLow));
        }

        private Float getAverageBpm(List<Float> values) {
            if (values.isEmpty()) return 0f;
            float sumOfValues = 0;
            for (Float value : values) {
                sumOfValues += value;
            }
            return sumOfValues / values.size();
        }

        private Float getHighestBpm(List<Float> values) {
            if (values.isEmpty()) return 0f;
            float highestBpm = values.get(0);
            for (Float value : values) {
                if (value > highestBpm) {
                    highestBpm = value;
                }
            }
            return highestBpm;
        }

        private Float getLowestBpm(List<Float> values) {
            if (values.isEmpty()) return 0f;
            float lowestBpm = values.get(0);
            for (Float value : values) {
                if (value < lowestBpm) {
                    lowestBpm = value;
                }
            }
            return lowestBpm;
        }
    };
}
