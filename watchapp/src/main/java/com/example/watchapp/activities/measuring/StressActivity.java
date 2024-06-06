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

public class StressActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "ManualDebug";
    private static final long MEASUREMENT_DURATION = 30000;
    private TextView stressTextView;
    private Button measureStressButton;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private boolean isMeasuring = false;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stress);

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

            if (heartRate < 0) {
                stressTextView.setText("Make sure your watch is fixed on your wrist");
                stopStressMeasurement();
            }

            if (System.currentTimeMillis() - startTime >= MEASUREMENT_DURATION) {


                sendDataToPhone();
                stopStressMeasurement();
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
        sensorManager.unregisterListener(this);
        measureStressButton.setText("Start Measurement");
    }

    private void sendDataToPhone() {
        DataClient dataClient = Wearable.getDataClient(this);
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/sensor-data/demand/stress");

        putDataMapRequest.getDataMap().putFloat("stress", 0);
        putDataMapRequest.getDataMap().putLong("timestamp", System.currentTimeMillis());
        dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnFailureListener(e -> Log.d(TAG, "Failed to send stress to phone"))
               .addOnSuccessListener(e -> Log.d(TAG, "Successfully sent stress to phone " + "something")); // TODO replace something
    }
}