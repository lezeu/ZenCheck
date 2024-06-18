package com.example.watchapp.activities.measuring;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.R;
import com.example.watchapp.api.MyCallback;
import com.example.watchapp.api.profile.ProfileApi;
import com.example.watchapp.dtos.ProfileDto;
import com.example.watchapp.utils.Constants;
import com.example.watchapp.utils.ZenCheckException;

import java.util.ArrayList;
import java.util.List;

public class StressActivity extends AppCompatActivity implements SensorEventListener {

    private static final long MEASUREMENT_DURATION = 15000;
    private static final int HR_STANDARD_THRESHOLD = 100;
    private static final int SDNN_STANDARD_THRESHOLD = 50; // 50-150ms
    private static final int RMSSD_STANDARD_THRESHOLD = 45; // 20-70ms
    private float hrThreshold;
    private float sdnnThreshold;
    private float rmssdThreshold;
    private TextView stressTextView;
    private Button measureStressButton;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private long startTime;
    List<Float> rrIntervals = new ArrayList<>();
    private boolean isMeasuring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stress);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        stressTextView = findViewById(R.id.stressValue);
        measureStressButton = findViewById(R.id.btnStressMeasurement);

        measureStressButton.setOnClickListener(v -> {
            if (isMeasuring) {
                stopStressMeasurement();
            } else {
                startStressMeasurement();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float heartRate = event.values[0];
            if (heartRate > 0) rrIntervals.add(6000 / heartRate);

            if (heartRate < 0) {
                stressTextView.setText("Make sure your watch is fixed on your wrist");
                stopStressMeasurement();
            }

            if (System.currentTimeMillis() - startTime >= MEASUREMENT_DURATION) {
                ProfileApi.INSTANCE.getProfile(new MyCallback<>() {
                    @Override
                    public void onSuccess(ProfileDto result) {
                        hrThreshold = result.getHrThreshold();
                        sdnnThreshold = result.getSdnnThreshold();
                        rmssdThreshold = result.getRmssdThreshold();

                        String stressLevel = assesStress(
                                heartRate, calculateSDNN(rrIntervals), calculateRMSSD(rrIntervals));
                        stressTextView.setText(stressLevel);
                        stopStressMeasurement();
                    }

                    @Override
                    public void onFailure(ZenCheckException exception) {
                        hrThreshold = HR_STANDARD_THRESHOLD;
                        sdnnThreshold = SDNN_STANDARD_THRESHOLD;
                        rmssdThreshold = RMSSD_STANDARD_THRESHOLD;

                        String stressLevel = assesStress(
                                heartRate, calculateSDNN(rrIntervals), calculateRMSSD(rrIntervals));
                        stressTextView.setText(stressLevel);
                        stopStressMeasurement();
                    }
                });
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if necessary
    }

    private void startStressMeasurement() {
        isMeasuring = true;
        startTime = System.currentTimeMillis();

        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        measureStressButton.setText("Stop Measuring");
        stressTextView.setText("This may take a minute");
    }

    private void stopStressMeasurement() {
        isMeasuring = false;
        rrIntervals.clear();
        sensorManager.unregisterListener(this);
        measureStressButton.setText("Start Measurement");
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

    private String assesStress(float heartRate, float sdnn, float rmssd) {
        if (heartRate > hrThreshold
                && sdnn < sdnnThreshold
                && rmssd < rmssdThreshold) {
            return Constants.VERY_HIGH_STRESS; // intense physical activity / high stress activity
        } else if ((heartRate > hrThreshold && sdnn < sdnnThreshold)
                || (heartRate > hrThreshold && rmssd < rmssdThreshold)
                || (sdnn < sdnnThreshold && rmssd < rmssdThreshold)) {
            return Constants.HIGH_STRESS; // some kind of stress / activity
        } else if (heartRate > hrThreshold
                || sdnn < sdnnThreshold
                || rmssd < rmssdThreshold) {
            return Constants.LOW_STRESS; // daily activity
        } else {
            return Constants.VERY_LOW_STRESS; // calm / rest / sleeping
        }
    }

}