package com.example.phoneapp.activities.challenges;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.ProgressAdapter;
import com.example.phoneapp.dtos.challenges.Progress;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ProgressAdapter challengeAdapter;
    private List<Progress> challengeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);
        recyclerView = findViewById(R.id.challenges_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addChallenges();
    }

    private void addChallenges() {
        //TODO get from DB
        challengeList = new ArrayList<>();

        challengeList.add(new Progress("RUN", "", 1, false, R.drawable.run, false, Duration.ofSeconds(360)));
        challengeList.add(new Progress("SHOWER", "", 2, true, R.drawable.shower, false, Duration.ofSeconds(30)));
        challengeList.add(new Progress("MEDITATE", "", 3, false, R.drawable.meditate, false, Duration.ofSeconds(120)));

        challengeAdapter = new ProgressAdapter(challengeList);
        recyclerView.setAdapter(challengeAdapter);
    }
}