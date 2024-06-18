package com.example.phoneapp.activities.challenges;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.activities.BaseActivity;
import com.example.phoneapp.R;
import com.example.phoneapp.adapters.ChallengeAdapter;
import com.example.phoneapp.dtos.challenges.Challenge;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        List<Challenge> challengeList;
        ChallengeAdapter adapter;
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);

        recyclerView = findViewById(R.id.recycler_view_challenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        challengeList = new ArrayList<>();
        challengeList.add(new Challenge(
                "Cold Shower",          // TODO: parameterized variables
                "Hard",
                30,
                R.drawable.shower));
        challengeList.add(new Challenge(
                "Morning Run",
                "Medium",
                60,
                R.drawable.run));
        challengeList.add(new Challenge(
                "Breathing Exercise",
                "Medium",
                40,
                R.drawable.breath));

        adapter = new ChallengeAdapter(challengeList);
        recyclerView.setAdapter(adapter);
    }
}
