package com.example.watchapp.activities.measuring;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.R;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public class PulseActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "ManualDebug";
    private static final long MEASURE_DURATION = 30000;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private Button measurePulseButton;
    private TextView measurePulseTextView;
    private long startTime;
    private float totalBpm;
    private int countReadings;
    private boolean isMeasuring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pulse);

        measurePulseButton = findViewById(R.id.btnPulseMeasurement);
        measurePulseTextView = findViewById(R.id.pulseValue);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        measurePulseButton.setOnClickListener(v -> {
            if (isMeasuring) {
                stopMeasurement();
            } else {
                startMeasurement();
            }
        });
    }

    @Override
    public void onPause() { // in case of display timeout - stop measurement
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() { // continue measurement after turning on the display
        super.onResume();
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float heartRate = event.values[0];

            if (heartRate < 0) {
                measurePulseTextView.setText("Make sure your watch is fixed on your wrist");
                stopMeasurement();
            }

            totalBpm += heartRate;
            countReadings++;

            if (System.currentTimeMillis() - startTime >= MEASURE_DURATION) {
                float averageBpm = totalBpm / countReadings;
                measurePulseTextView.setText("BPM: " + String.format("%.2f", averageBpm));

                sendBpmDataToPhone(averageBpm);
                stopMeasurement();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if necessary
    }

    private void startMeasurement() {
        isMeasuring = true;
        startTime = System.currentTimeMillis();
        totalBpm = 0f;
        countReadings = 0;

        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        measurePulseButton.setText("Stop Measuring");
        measurePulseTextView.setText("This may take a minute");
    }

    private void stopMeasurement() {
        isMeasuring = false;
        sensorManager.unregisterListener(this);
        measurePulseButton.setText("Start Measurement");
    }

    /**
     * send bpm to phone as well as timestamp to /sensor-data/bpm route
     * @param bpm - send bpm to phone
     */
    private void sendBpmDataToPhone(float bpm) {
        DataClient dataClient = Wearable.getDataClient(this);
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/sensor-data/bpm");

        putDataMapRequest.getDataMap().putFloat("bpm", bpm);
        putDataMapRequest.getDataMap().putLong("timestamp", System.currentTimeMillis());

        Log.d(TAG, "Data sent: " + putDataMapRequest);
        dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnFailureListener(e -> Log.d(TAG, "Failed to send bpm to phone"))
                .addOnSuccessListener(e -> Log.d(TAG, "Successfully sent bpm to phone " + bpm));
    }
}