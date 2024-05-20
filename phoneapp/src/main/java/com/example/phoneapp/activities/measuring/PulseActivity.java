package com.example.phoneapp.activities.measuring;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;

public class PulseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pulse);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);

        Button bpmButton = findViewById(R.id.btn_measure_bpm);

        bpmButton.setOnClickListener(v -> {
            // TODO start measuring from watch
        });
    }
}