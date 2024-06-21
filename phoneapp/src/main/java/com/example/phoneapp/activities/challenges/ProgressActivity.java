package com.example.phoneapp.activities.challenges;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.ProgressAdapter;
import com.example.phoneapp.dtos.challenges.Progress;

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
        challengeList = new ArrayList<>();

        challengeList.add(new Progress("Go for a run", "Points: 1", false, R.drawable.run, false));
        challengeList.add(new Progress("Take a cold shower", "Points: 2", true, R.drawable.shower, false));
        challengeList.add(new Progress("Meditate", "Points: 3", false, R.drawable.meditate, false));

        challengeAdapter = new ProgressAdapter(challengeList);
        recyclerView.setAdapter(challengeAdapter);
    }
}