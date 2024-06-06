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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StressMeasurementPeriodicallyService extends ForegroundService implements SensorEventListener {

    private static final String TAG = "ManualDebug";
    private static final int HR_STANDARD_THRESHOLD = 100;
    private static final int SDNN_STANDARD_THRESHOLD = 50; //100
    private static final int RMSSD_STANDARD_THRESHOLD = 16; //107
    private static final long MEASUREMENT_DURATION = 15 * 1000L;
    private static final long MEASUREMENT_INTERVAL = 5 * 60 * 1000L;
    private SensorManager sensorManager;
    private Sensor hearthSensor;
    private Context context;

    private List<Float> rrIntervals = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // Get profile information and set Male and Female RMSSD and SDNN values

        createNotificationChannel();
        startForeground(1, buildForegroundNotification());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        hearthSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        handler.postDelayed(stressAssessRunnable, MEASUREMENT_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager.registerListener(
                this, hearthSensor, SensorManager.SENSOR_DELAY_UI);
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
            if (bpm > 0) rrIntervals.add(60000 / bpm);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if necessary
    }

    private Runnable measurementRunnable = () -> sensorManager.unregisterListener(
            StressMeasurementPeriodicallyService.this, hearthSensor);


    private Runnable stressAssessRunnable = new Runnable() {

        @Override
        public void run() {
            sensorManager.registerListener(
                    StressMeasurementPeriodicallyService.this,
                    hearthSensor,
                    SensorManager.SENSOR_DELAY_UI);
            handler.postDelayed(measurementRunnable, MEASUREMENT_DURATION);

            handler.postDelayed(() -> {
                long currentTime = System.currentTimeMillis();

                if (!rrIntervals.isEmpty()) {
                    float hr = calculateHR(rrIntervals);
                    float sdnn = calculateSDNN(rrIntervals);
                    float rmssd = calculateRMSSD(rrIntervals);

                    String stressLevel = assessStress(hr, sdnn, rmssd);
                    if (!"Not Stressed".equals(stressLevel)) {
                        notifyUser(stressLevel);
                    }

                    sendDataToPhone(stressLevel, hr, sdnn, rmssd, currentTime);
                    rrIntervals.clear();
                }
                handler.postDelayed(stressAssessRunnable, MEASUREMENT_INTERVAL - MEASUREMENT_DURATION);
            }, MEASUREMENT_DURATION);
        }

        private float calculateHR(List<Float> rrIntervals) {
            float sum = 0;
            for (float rr : rrIntervals) {
                sum += rr;
            }
            return 60000 / (sum / rrIntervals.size());
        }

        private float calculateSDNN(List<Float> rrIntervals) {
            float mean = 0;
            for (float rr : rrIntervals) {
                mean += rr;
            }
            mean /= rrIntervals.size();

            float sumOfSquares = 0;
            for (float rr : rrIntervals) {
                sumOfSquares += (float) Math.pow(rr - mean, 2);
            }

            return (float) Math.sqrt(sumOfSquares / rrIntervals.size());
        }

        private float calculateRMSSD(List<Float> rrIntervals) {
            float sumOfSquaredDifferences = 0;
            for (int i = 1; i < rrIntervals.size(); i++) {
                float difference = rrIntervals.get(i) - rrIntervals.get(i - 1);
                sumOfSquaredDifferences += difference * difference;
            }

            float meanOfSquaredDifferences = sumOfSquaredDifferences / (rrIntervals.size() - 1);

            return (float) Math.sqrt(meanOfSquaredDifferences);
        }

        private String assessStress(float hr, float sdnn, float rmssd) {
            if (hr > HR_STANDARD_THRESHOLD && (sdnn < SDNN_STANDARD_THRESHOLD || rmssd < RMSSD_STANDARD_THRESHOLD)) {
                return "Highly Stressed";
            } else if (hr > HR_STANDARD_THRESHOLD || sdnn < SDNN_STANDARD_THRESHOLD || rmssd < RMSSD_STANDARD_THRESHOLD) {
                return "Stressed";
            } else {
                return "Not Stressed";
            }
        }

        private void notifyUser(String stressLevel) {
            // TODO Implement notification
        }

        private void sendDataToPhone(String stressLevel, float hr, float sdnn, float rmssd, long timestamp) {

            DataClient dataClient = Wearable.getDataClient(context);
            PutDataMapRequest putDataMapRequest =
                    PutDataMapRequest.create("/sensor-data/scheduled/stress");

            putDataMapRequest.getDataMap().putString("stressLevel", stressLevel);
            putDataMapRequest.getDataMap().putFloat("HR", hr);
            putDataMapRequest.getDataMap().putFloat("SDNN", sdnn);
            putDataMapRequest.getDataMap().putFloat("RMSSD", rmssd);
            putDataMapRequest.getDataMap().putLong("timestamp", timestamp);
            Log.d(TAG, LocalDateTime.now().toString());

            dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                    .addOnSuccessListener(e -> Log.d(TAG, "STRESS: Successfully sent data to phone "
                            + stressLevel + " " + hr + " " + sdnn + " " + rmssd))
                    .addOnFailureListener(e -> Log.e(TAG, "Failure sending data to phone "));
        }
    };
}
