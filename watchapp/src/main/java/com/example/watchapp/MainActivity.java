package com.example.watchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.activities.measuring.PulseActivity;
import com.example.watchapp.activities.measuring.StressActivity;
import com.example.watchapp.services.BpmMeasurementPeriodicallyService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToBpmButton = findViewById(R.id.btnNavigateToBPM);
        Button goToStressButton = findViewById(R.id.btnNavigateToStress);

        goToBpmButton.setOnClickListener(v -> startActivity(new Intent(this, PulseActivity.class)));
        goToStressButton.setOnClickListener(v -> startActivity(new Intent(this, StressActivity.class)));

        startService(new Intent(this, BpmMeasurementPeriodicallyService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, BpmMeasurementPeriodicallyService.class));
    }
}
