package com.example.phoneapp.challenges;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.BaseActivity;
import com.example.phoneapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ChallengeAdapter adapter;
    private List<Challenge> challengeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        setupDrawer(R.id.drawer_layout, R.id.design_navigation_view);

        recyclerView = findViewById(R.id.recycler_view_challenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        challengeList = new ArrayList<>();
        challengeList.add(new Challenge(
                "Cold Shower",
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
