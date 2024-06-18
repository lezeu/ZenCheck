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

import com.example.watchapp.api.MyCallback;
import com.example.watchapp.api.profile.ProfileApi;
import com.example.watchapp.dtos.ProfileDto;
import com.example.watchapp.utils.Constants;
import com.example.watchapp.utils.ZenCheckException;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class StressMeasurementPeriodicallyService extends ForegroundService implements SensorEventListener {

    private static final String TAG = "ManualDebug";
    private static final int HR_STANDARD_THRESHOLD = 100;
    private static final int SDNN_STANDARD_THRESHOLD = 50; // 50-100
    private static final int RMSSD_STANDARD_THRESHOLD = 45; // 20-70
    private static final long MEASUREMENT_DURATION = 15 * 1000L;
    private static final long MEASUREMENT_INTERVAL = 5 * 60 * 1000L;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private SensorManager sensorManager;
    private Sensor hearthSensor;
    private Context context;
    private float hrThreshold;
    private float sdnnThreshold;
    private float rmssdThreshold;
    private List<Float> rrIntervals = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // Get profile information


        createNotificationChannel();
        startForeground(1, buildForegroundNotification());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        hearthSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        handler.postDelayed(stressAssessRunnable, MEASUREMENT_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager.registerListener(
                this, hearthSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

    private final Runnable measurementRunnable = () -> sensorManager.unregisterListener(
            StressMeasurementPeriodicallyService.this);


    private final Runnable stressAssessRunnable = new Runnable() {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();

            sensorManager.registerListener(
                    StressMeasurementPeriodicallyService.this,
                    hearthSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            handler.postDelayed(measurementRunnable, MEASUREMENT_DURATION);

            handler.postDelayed(() -> {
                if (!rrIntervals.isEmpty()) {
                    float hr = calculateHR(rrIntervals);
                    float sdnn = calculateSDNN(rrIntervals);
                    float rmssd = calculateRMSSD(rrIntervals);

                    ProfileApi.INSTANCE.getProfile(new MyCallback<>() {
                        @Override
                        public void onSuccess(ProfileDto result) {
                            hrThreshold = result.getHrThreshold();
                            sdnnThreshold = result.getSdnnThreshold();
                            rmssdThreshold = result.getRmssdThreshold();

                            String stressLevel = assessStress(hr, sdnn, rmssd);
                            if (stressLevel.equals(Constants.VERY_HIGH_STRESS)) {
                                notifyUser(stressLevel);
                            }

                            sendDataToPhone(stressLevel, hr, sdnn, rmssd, currentTime);
                            rrIntervals.clear();
                        }

                        @Override
                        public void onFailure(ZenCheckException exception) {
                            hrThreshold = HR_STANDARD_THRESHOLD;
                            sdnnThreshold = SDNN_STANDARD_THRESHOLD;
                            rmssdThreshold = RMSSD_STANDARD_THRESHOLD;

                            String stressLevel = assessStress(hr, sdnn, rmssd);
                            if (stressLevel.equals(Constants.VERY_HIGH_STRESS)) {
                                notifyUser(stressLevel);
                            }

                            sendDataToPhone(stressLevel, hr, sdnn, rmssd, currentTime);
                            rrIntervals.clear();
                        }
                    });
                }
                handler.postDelayed(stressAssessRunnable, MEASUREMENT_INTERVAL - MEASUREMENT_DURATION - (System.currentTimeMillis() - currentTime));
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
            if (hr > hrThreshold
                    && sdnn < sdnnThreshold
                    && rmssd < rmssdThreshold) {
                return Constants.VERY_HIGH_STRESS; // intense physical activity / high stress activity
            } else if ((hr > hrThreshold && sdnn < sdnnThreshold)
                    || (hr > hrThreshold && rmssd < rmssdThreshold)
                    || (sdnn < sdnnThreshold && rmssd < rmssdThreshold)) {
                return Constants.HIGH_STRESS; // some kind of stress / activity
            } else if (hr > hrThreshold
                    || sdnn < sdnnThreshold
                    || rmssd < rmssdThreshold) {
                return Constants.LOW_STRESS; // daily activity
            } else {
                return Constants.VERY_LOW_STRESS; // calm / rest / sleeping
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
