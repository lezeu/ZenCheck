package com.example.phoneapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.bpm.Pulse;
import com.example.phoneapp.activities.bpm.Stress;
import com.example.phoneapp.activities.challenges.ChallengeActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);

        CardView statsCard = findViewById(R.id.card_zen_status);
        CardView challengesCard = findViewById(R.id.card_challenges);
        CardView progressCard = findViewById(R.id.card_progress);
        CardView stressCard = findViewById(R.id.card_stress_level);
        CardView pulseCard = findViewById(R.id.card_average_bpm);

//        statsCard.setOnClickListener(v -> startActivity(
//                new Intent(MainActivity.this, StatsActivity.class)));
        challengesCard.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, ChallengeActivity.class)));
//        progressCard.setOnClickListener(v -> startActivity(
//                new Intent(MainActivity.this, ProgressActivity.class)));
        stressCard.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, Stress.class)));
        pulseCard.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, Pulse.class)));
    }
}
