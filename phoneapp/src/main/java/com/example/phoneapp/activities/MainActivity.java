package com.example.phoneapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.challenges.ChallengeActivity;
import com.example.phoneapp.activities.measuring.PulseActivity;
import com.example.phoneapp.activities.measuring.StressActivity;
import com.example.phoneapp.services.devicecommunication.DataLayerListenerService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer(R.id.ConstraintLayout, R.id.design_navigation_view);

        CardView challengesCard = findViewById(R.id.card_challenges);
        CardView stressCard = findViewById(R.id.card_stress_level);
        CardView pulseCard = findViewById(R.id.card_average_bpm);

//        statsCard.setOnClickListener(v -> startActivity(
//                new Intent(MainActivity.this, StatsActivity.class)));
        challengesCard.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, ChallengeActivity.class)));
//        progressCard.setOnClickListener(v -> startActivity(
//                new Intent(MainActivity.this, ProgressActivity.class)));
        stressCard.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, StressActivity.class)));
        pulseCard.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, PulseActivity.class)));

        // start services
        startForegroundService(new Intent(this, DataLayerListenerService.class));
    }
}
