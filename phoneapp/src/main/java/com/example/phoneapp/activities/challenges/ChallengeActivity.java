package com.example.phoneapp.activities.challenges;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.adapters.ChallengeAdapter;
import com.example.phoneapp.dtos.challenges.Progress;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        List<Progress> challengeList;
        ChallengeAdapter adapter;
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);

        recyclerView = findViewById(R.id.recycler_view_challenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        challengeList = new ArrayList<>();
        challengeList.add(new Progress(
                "Cold Shower",
                "Points: 1",
                false,
                R.drawable.shower,
                false));
        challengeList.add(new Progress(
                "Morning Run",
                "Points: 2",
                false,
                R.drawable.run,
                false));
        challengeList.add(new Progress(
                "Breathing Exercise",
                "Points: 3",
                false,
                R.drawable.breath,
                false));

        adapter = new ChallengeAdapter(challengeList);
        recyclerView.setAdapter(adapter);
    }
}
